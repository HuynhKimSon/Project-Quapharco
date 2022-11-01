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
import vn.com.irtech.irbot.business.domain.HdbhCn;
import vn.com.irtech.irbot.business.domain.HdbhCnDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHdbhCnReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdbhCnDetailRes;
import vn.com.irtech.irbot.business.mapper.HdbhCnModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdbhCnModifyMapper;

import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IHdbhCnModifyService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdbhCnModifyServiceImpl implements IHdbhCnModifyService {

	private static final Logger logger = LoggerFactory.getLogger(HdbhCnServiceImpl.class);

	@Autowired
	private HdbhCnModifyMapper hdbhCnModifyMapper;

	@Autowired
	private HdbhCnModifyDetailMapper hdbhCnModifyDetailMapper;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HdbhCn selectHdbhCnModifyById(Long id) {
		return hdbhCnModifyMapper.selectHdbhCnModifyById(id);
	}

	@Override
	public List<HdbhCn> selectHdbhCnModifyList(HdbhCn hdbhcn) {
		return hdbhCnModifyMapper.selectHdbhCnModifyList(hdbhcn);
	}

	@Override
	public int updateStatus(String ids, Integer status) {
		logger.info("Reset Status Hoa don ban hang chin nhanh dieu chinh !");
		Long idArr[] = Convert.toLongArray(ids);
		int result = 0;
		for (Long id : idArr) {
			HdbhCn hdbhCnExist = hdbhCnModifyMapper.selectHdbhCnModifyById(id);
			if (hdbhCnExist != null) {
				HdbhCn hdbhCn = new HdbhCn();
				hdbhCn.setId(id);
				hdbhCn.setStatus(status);
				hdbhCnModifyMapper.updateHdbhCnModify(hdbhCn);
				result++;
			}
		}
		return result;
	}

	@Override
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception {
		HdbhCn hdbhCnModifyUpdate = new HdbhCn();
		hdbhCnModifyUpdate.setId(Long.valueOf(request.getFastId()));
		hdbhCnModifyUpdate.seteInvoiceNo(request.geteInvoiceNo());
		hdbhCnModifyMapper.updateHdbhCnModify(hdbhCnModifyUpdate);
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

			Map<RobotSyncHdbhCnReq, Robot> robotRequestMap = new HashMap<RobotSyncHdbhCnReq, Robot>();
			Long idArr[] = Convert.toLongArray(ids);
			Date now = new Date();
			for (Long id : idArr) {
				HdbhCn hdbhCnModifySelect = hdbhCnModifyMapper.selectHdbhCnModifyById(id);

				if (hdbhCnModifySelect == null) {
					logger.info("Hoa don ban hang not exist : {}" + id);
					throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
				} else {
					if (hdbhCnModifySelect.getStatus() == (ProcessStatus.SUCCESS.value())) {
						throw new Exception("Tồn tại hoá đơn đã đồng bộ thành công!");
					}
				}

				WorkProcess workProcessNew = new WorkProcess();
				workProcessNew.setServiceId(RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value());
				workProcessNew.setSyncId(hdbhCnModifySelect.getId().toString());
				workProcessNew.setSyncKey(hdbhCnModifySelect.getFastId());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Hoa don modify master
				RobotSyncHdbhCnReq dataRequestRobot = new RobotSyncHdbhCnReq();
				dataRequestRobot.setConfig(getProcessConfig(hdbhCnModifySelect.getUnitCode()));
				dataRequestRobot.setSyncId(hdbhCnModifySelect.getId().intValue());
				dataRequestRobot.setServiceType(hdbhCnModifySelect.getMode());

				if (hdbhCnModifySelect.getMode() == InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM.value()) {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM.title());
				} else {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG.title());
				}
				dataRequestRobot.setFastId(hdbhCnModifySelect.getFastId());
				dataRequestRobot.setFastInvoiceNo(hdbhCnModifySelect.getFastInvoiceNo());
				dataRequestRobot.setUnitCode(hdbhCnModifySelect.getUnitCode());
				dataRequestRobot.setCustomerId(hdbhCnModifySelect.getCustomerId());
				dataRequestRobot.setCustomerName(hdbhCnModifySelect.getCustomerName());
				dataRequestRobot.setCustomerTaxCode(hdbhCnModifySelect.getCustomerTaxCode());
				dataRequestRobot.setAddress(hdbhCnModifySelect.getAddress());
				dataRequestRobot
						.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhCnModifySelect.getInvoiceDate()));
				dataRequestRobot.setInvoiceModifyDate(
						DateUtils.parseDateToStr("dd/MM/yyyy", hdbhCnModifySelect.getInvoiceModifyDate()));
				dataRequestRobot.setDescription(hdbhCnModifySelect.getDescription());
				dataRequestRobot.setPaymentMethod(hdbhCnModifySelect.getPaymentMethod());
				dataRequestRobot.setTotalTaxAmount(hdbhCnModifySelect.getTotalTaxAmount());
				dataRequestRobot.setSubtotalAmount(hdbhCnModifySelect.getSubtotalAmount());
				dataRequestRobot.setTotalAmount(hdbhCnModifySelect.getTotalAmount());

				// Hoa don modify detail
				HdbhCnDetail hdbhCnModifyDetailSelect = new HdbhCnDetail();
				hdbhCnModifyDetailSelect.setHdbhCnId(hdbhCnModifySelect.getId());
				List<HdbhCnDetail> listHdbhCnModifyDetail = hdbhCnModifyDetailMapper
						.selectHdbhCnModifyDetailList(hdbhCnModifyDetailSelect);

				List<HdbhCnDetailRes> listHdbhCnModifyDetailRes = new ArrayList<HdbhCnDetailRes>();

				if (!CollectionUtils.isEmpty(listHdbhCnModifyDetail)) {
					for (HdbhCnDetail detail : listHdbhCnModifyDetail) {
						HdbhCnDetailRes hdbhCnDetailRes = new HdbhCnDetailRes();
						hdbhCnDetailRes.setFastId(detail.getFastId());
						hdbhCnDetailRes.setFastDetailId(detail.getFastDetailId());
						hdbhCnDetailRes.setProductId(detail.getProductId());
						hdbhCnDetailRes.setProductName(detail.getProductName());
						hdbhCnDetailRes.setProductUnit(detail.getProductUnit());
						hdbhCnDetailRes.setProductTaxId(detail.getProductTaxId());
						hdbhCnDetailRes.setProductQuantity(detail.getProductQuantity());
						hdbhCnDetailRes.setProductPrice(detail.getProductPrice());
						hdbhCnDetailRes.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdbhCnDetailRes.setProductTaxAmount(detail.getProductTaxAmount());
						hdbhCnDetailRes.setProductDiscount(detail.getProductDiscount());
						hdbhCnDetailRes.setProductTotalAmount(detail.getProductTotalAmount());
						hdbhCnDetailRes.setProductExpiredDate(detail.getProductExpiredDate());
						hdbhCnDetailRes.setStockOut(detail.getStockOut());
						hdbhCnDetailRes.setLotNo(detail.getLotNo());
						listHdbhCnModifyDetailRes.add(hdbhCnDetailRes);
					}
					dataRequestRobot.setOrderQuantity(listHdbhCnModifyDetailRes.size());
					dataRequestRobot.setProcessOrder(listHdbhCnModifyDetailRes);
				}

				// Tim fastId trong table workProcess
				WorkProcess workProcessExist = workProcessMapper
						.selectWorkProcessBySyncKey(hdbhCnModifySelect.getFastId());

				// delete record ton tai table WorkProcess
				if (workProcessExist != null) {
					workProcessMapper.deleteWorkProcessById(workProcessExist.getId());
				}
				// insert record vao table WorkProcess
				workProcessMapper.insertWorkProcess(workProcessNew);

				// set processId cua Json (data_request) sau khi insert thanh cong
				dataRequestRobot.setProcessId(workProcessNew.getId());
				hdbhCnModifySelect.setProcessId(workProcessNew.getId());

				// update status table hdbh_cn_modify
				hdbhCnModifySelect.setStatus(ProcessStatus.WAIT.value());
				hdbhCnModifyMapper.updateHdbhCnModify(hdbhCnModifySelect);

				// them record (data_request) vao robotRequestMap
				robotRequestMap.put(dataRequestRobot, robots.get(0));
			}

			// Loop insert/update dataRequest vao table workProcess
			if (robotRequestMap.size() > 0) {
				for (Map.Entry<RobotSyncHdbhCnReq, Robot> entry : robotRequestMap.entrySet()) {
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
	private void requestRobot(Robot robot, RobotSyncHdbhCnReq process) {

		String message = new Gson().toJson(process);

		WorkProcess processUpdate = new WorkProcess();
		processUpdate.setId(process.getProcessId());
		processUpdate.setDataRequest(message);
		processUpdate.setRobotUuid(robot.getUuid());
		workProcessMapper.updateWorkProcess(processUpdate);
	}
}