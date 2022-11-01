package vn.com.irtech.irbot.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.system.service.ISysConfigService;
import vn.com.irtech.core.system.service.ISysDictDataService;
import vn.com.irtech.irbot.business.domain.HdxNcc;
import vn.com.irtech.irbot.business.domain.HdxNccDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHdxNccReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdxNccDetailRes;
import vn.com.irtech.irbot.business.mapper.HdxNccModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdxNccModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IHdxNccModifyService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdxNccModifyServiceImpl implements IHdxNccModifyService {

	private static final Logger logger = LoggerFactory.getLogger(HdxNccServiceImpl.class);

	@Autowired
	private HdxNccModifyMapper hdxNccModifyMapper;

	@Autowired
	private HdxNccModifyDetailMapper hdxNccModifyDetailMapper;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HdxNcc selectHdxNccModifyById(Long id) {
		return hdxNccModifyMapper.selectHdxNccModifyById(id);
	}

	@Override
	public List<HdxNcc> selectHdxNccModifyList(HdxNcc hdxNcc) {
		return hdxNccModifyMapper.selectHdxNccModifyList(hdxNcc);
	}

	@Override
	public int updateStatus(String ids, Integer status) {
		logger.info("Reset Status Hoa don xuat tra lai ncc dieu chinh !");
		Long idArr[] = Convert.toLongArray(ids);
		int result = 0;
		for (Long id : idArr) {
			HdxNcc hdxNccExist = hdxNccModifyMapper.selectHdxNccModifyById(id);
			if (hdxNccExist != null) {
				HdxNcc hdxNcc = new HdxNcc();
				hdxNcc.setId(id);
				hdxNcc.setStatus(status);
				hdxNccModifyMapper.updateHdxNccModify(hdxNcc);
				result++;
			}
		}
		return result;
	}

	@Override
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception {
		HdxNcc hdxNccModifyUpdate = new HdxNcc();
		hdxNccModifyUpdate.setId(Long.valueOf(request.getFastId()));
		hdxNccModifyUpdate.seteInvoiceNo(request.geteInvoiceNo());
		hdxNccModifyMapper.updateHdxNccModify(hdxNccModifyUpdate);
	}

	/**
	 * 
	 * @param unitCode
	 * @return ProcessFastConfig
	 */
	private ProcessFastConfig getProcessConfig(String unitCode) {
		ProcessFastConfig processFastConfig = new ProcessFastConfig();
		processFastConfig.setUsername(configService.selectConfigByKey("business.web.fast.username"));
		processFastConfig.setPassword(configService.selectConfigByKey("business.web.fast.password"));
		processFastConfig.setUrl(configService.selectConfigByKey("business.web.fast.url"));
		processFastConfig.setCompanyName(configService.selectConfigByKey("business.web.fast.companyName"));

		String bookHD = sysDictDataService.selectDictValue(DictType.BUSINESS_UNIT_QUYEN_HOA_DON.value(), unitCode);
		if (bookHD == null) {
			bookHD = "";
		}
		processFastConfig.setBookHD(bookHD);
		String bookPXK = sysDictDataService.selectDictValue(DictType.BUSINESS_UNIT_QUYEN_XUAT_KHO.value(), unitCode);
		if (bookPXK == null) {
			bookPXK = "";
		}
		processFastConfig.setBookPXK(bookPXK);

		return processFastConfig;
	}

	@Override
	@Transactional
	public void retry(String ids) throws Exception {
		try {
			// Get a robot available, busy
			List<Robot> robots = robotMapper.selectRobotByServices(
					new Integer[] { RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value() },
					new String[] { RobotStatusType.AVAILABLE.value(), RobotStatusType.BUSY.value() });

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			Map<RobotSyncHdxNccReq, Robot> robotRequestMap = new HashMap<RobotSyncHdxNccReq, Robot>();
			Long idArr[] = Convert.toLongArray(ids);
			Date now = new Date();
			for (Long id : idArr) {
				HdxNcc hdxNccModifySelect = hdxNccModifyMapper.selectHdxNccModifyById(id);

				if (hdxNccModifySelect == null) {
					logger.info("Hoa don xuat tra lai ncc not exist : {}" + id);
					throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
				} else {
					if (hdxNccModifySelect.getStatus() == (ProcessStatus.SUCCESS.value())) {
						throw new Exception("Tồn tại hoá đơn đã đồng bộ thành công!");
					}
				}

				WorkProcess workProcessNew = new WorkProcess();
				workProcessNew.setServiceId(RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value());
				workProcessNew.setSyncId(hdxNccModifySelect.getId().toString());
				workProcessNew.setSyncKey(hdxNccModifySelect.getFastId());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Hoa don modify master
				RobotSyncHdxNccReq dataRequestRobot = new RobotSyncHdxNccReq();
				dataRequestRobot.setConfig(getProcessConfig(hdxNccModifySelect.getUnitCode()));
				dataRequestRobot.setSyncId(hdxNccModifySelect.getId().intValue());
				dataRequestRobot.setServiceType(hdxNccModifySelect.getMode());

				if (hdxNccModifySelect.getMode() == InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM.value()) {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM.title());
				} else {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_TANG.title());
				}
				dataRequestRobot.setFastId(hdxNccModifySelect.getFastId());
				dataRequestRobot.setFastInvoiceNo(hdxNccModifySelect.getFastInvoiceNo());
				dataRequestRobot.setUnitCode(hdxNccModifySelect.getUnitCode());
				dataRequestRobot.setCustomerId(hdxNccModifySelect.getCustomerId());
				dataRequestRobot.setCustomerName(hdxNccModifySelect.getCustomerName());
				dataRequestRobot.setCustomerTaxCode(hdxNccModifySelect.getCustomerTaxCode());
				dataRequestRobot.setAddress(hdxNccModifySelect.getAddress());
				dataRequestRobot
						.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdxNccModifySelect.getInvoiceDate()));
				dataRequestRobot.setInvoiceModifyDate(
						DateUtils.parseDateToStr("dd/MM/yyyy", hdxNccModifySelect.getInvoiceModifyDate()));
				dataRequestRobot.setDescription(hdxNccModifySelect.getDescription());
				dataRequestRobot.setPaymentMethod(hdxNccModifySelect.getPaymentMethod());
				dataRequestRobot.setTransporter(hdxNccModifySelect.getTransporter());
				dataRequestRobot.setTotalTaxAmount(hdxNccModifySelect.getTotalTaxAmount());
				dataRequestRobot.setSubtotalAmount(hdxNccModifySelect.getSubtotalAmount());
				dataRequestRobot.setTotalAmount(hdxNccModifySelect.getTotalAmount());

				// Hoa don modify detail
				HdxNccDetail hdxNccModifyDetailSelect = new HdxNccDetail();
				hdxNccModifyDetailSelect.setHdxNccId(hdxNccModifySelect.getId());
				List<HdxNccDetail> listHdxNccModifyDetail = hdxNccModifyDetailMapper
						.selectHdxNccModifyDetailList(hdxNccModifyDetailSelect);

				List<HdxNccDetailRes> listHdxNccModifyDetailRes = new ArrayList<HdxNccDetailRes>();

				if (!CollectionUtils.isEmpty(listHdxNccModifyDetail)) {
					for (HdxNccDetail detail : listHdxNccModifyDetail) {
						HdxNccDetailRes hdxNccDetailRes = new HdxNccDetailRes();
						hdxNccDetailRes.setFastId(detail.getFastId());
						hdxNccDetailRes.setFastDetailId(detail.getFastDetailId());
						hdxNccDetailRes.setProductId(detail.getProductId());
						hdxNccDetailRes.setProductName(detail.getProductName());
						hdxNccDetailRes.setProductUnit(detail.getProductUnit());
						hdxNccDetailRes.setProductTaxId(detail.getProductTaxId());
						hdxNccDetailRes.setProductQuantity(detail.getProductQuantity());
						hdxNccDetailRes.setProductPrice(detail.getProductPrice());
						hdxNccDetailRes.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdxNccDetailRes.setProductTaxAmount(detail.getProductTaxAmount());
						hdxNccDetailRes.setProductDiscount(detail.getProductDiscount());
						hdxNccDetailRes.setProductTotalAmount(detail.getProductTotalAmount());
						hdxNccDetailRes.setProductExpiredDate(detail.getProductExpiredDate());
						hdxNccDetailRes.setStockOut(detail.getStockOut());
						hdxNccDetailRes.setStockOutName(detail.getStockOutName());
						hdxNccDetailRes.setLotNo(detail.getLotNo());
						listHdxNccModifyDetailRes.add(hdxNccDetailRes);
					}
					dataRequestRobot.setOrderQuantity(listHdxNccModifyDetailRes.size());
					dataRequestRobot.setProcessOrder(listHdxNccModifyDetailRes);
				}

				// Tim fastId trong table workProcess
				WorkProcess workProcessExist = workProcessMapper
						.selectWorkProcessBySyncKey(hdxNccModifySelect.getFastId());

				// delete record ton tai table WorkProcess
				if (workProcessExist != null) {
					workProcessMapper.deleteWorkProcessById(workProcessExist.getId());
				}
				// insert record vao table WorkProcess
				workProcessMapper.insertWorkProcess(workProcessNew);

				// set processId cua Json (data_request) sau khi insert thanh cong
				dataRequestRobot.setProcessId(workProcessNew.getId());
				hdxNccModifySelect.setProcessId(workProcessNew.getId());

				// update status table hdxNcc_modify
				hdxNccModifySelect.setStatus(ProcessStatus.WAIT.value());
				hdxNccModifyMapper.updateHdxNccModify(hdxNccModifySelect);

				// them record (data_request) vao robotRequestMap
				robotRequestMap.put(dataRequestRobot, robots.get(0));
			}

			// Loop insert/update dataRequest vao table workProcess
			if (robotRequestMap.size() > 0) {
				for (Map.Entry<RobotSyncHdxNccReq, Robot> entry : robotRequestMap.entrySet()) {
					requestRobot(entry.getValue(), entry.getKey());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>>>>> Error: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * 
	 * @param robot
	 * @param process
	 */
	private void requestRobot(Robot robot, RobotSyncHdxNccReq process) {

		String message = new Gson().toJson(process);

		WorkProcess processUpdate = new WorkProcess();
		processUpdate.setId(process.getProcessId());
		processUpdate.setDataRequest(message);
		processUpdate.setRobotUuid(robot.getUuid());
		workProcessMapper.updateWorkProcess(processUpdate);
	}
}