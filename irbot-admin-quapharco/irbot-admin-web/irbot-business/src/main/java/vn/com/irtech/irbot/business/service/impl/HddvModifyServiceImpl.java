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
import vn.com.irtech.irbot.business.domain.Hddv;
import vn.com.irtech.irbot.business.domain.HddvDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHddvReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HddvDetailRes;
import vn.com.irtech.irbot.business.mapper.HddvModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HddvModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IHddvModifyService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HddvModifyServiceImpl implements IHddvModifyService {

	private static final Logger logger = LoggerFactory.getLogger(HddvServiceImpl.class);

	@Autowired
	private HddvModifyMapper hddvModifyMapper;

	@Autowired
	private HddvModifyDetailMapper hddvModifyDetailMapper;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public Hddv selectHddvModifyById(Long id) {
		return hddvModifyMapper.selectHddvModifyById(id);
	}

	@Override
	public List<Hddv> selectHddvModifyList(Hddv hddv) {
		return hddvModifyMapper.selectHddvModifyList(hddv);
	}

	@Override
	public int updateStatus(String ids, Integer status) {
		logger.info("Reset Status Hoa don ban hang dieu chinh !");
		Long idArr[] = Convert.toLongArray(ids);
		int result = 0;
		for (Long id : idArr) {
			Hddv hddvExist = hddvModifyMapper.selectHddvModifyById(id);
			if (hddvExist != null) {
				Hddv hddv = new Hddv();
				hddv.setId(id);
				hddv.setStatus(status);
				hddvModifyMapper.updateHddvModify(hddv);
				result++;
			}
		}
		return result;
	}

	@Override
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception {
		Hddv hddvModifyUpdate = new Hddv();
		hddvModifyUpdate.setId(Long.valueOf(request.getFastId()));
		hddvModifyUpdate.seteInvoiceNo(request.geteInvoiceNo());
		hddvModifyMapper.updateHddvModify(hddvModifyUpdate);
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

			Map<RobotSyncHddvReq, Robot> robotRequestMap = new HashMap<RobotSyncHddvReq, Robot>();
			Long idArr[] = Convert.toLongArray(ids);
			Date now = new Date();
			for (Long id : idArr) {
				Hddv hddvModifySelect = hddvModifyMapper.selectHddvModifyById(id);

				if (hddvModifySelect == null) {
					logger.info("Hoa don ban hang not exist : {}" + id);
					throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
				} else {
					if (hddvModifySelect.getStatus() == (ProcessStatus.SUCCESS.value())) {
						throw new Exception("Tồn tại hoá đơn đã đồng bộ thành công!");
					}
				}

				WorkProcess workProcessNew = new WorkProcess();
				workProcessNew.setServiceId(RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value());
				workProcessNew.setSyncId(hddvModifySelect.getId().toString());
				workProcessNew.setSyncKey(hddvModifySelect.getFastId());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Hoa don modify master
				RobotSyncHddvReq dataRequestRobot = new RobotSyncHddvReq();
				dataRequestRobot.setConfig(getProcessConfig(hddvModifySelect.getUnitCode()));
				dataRequestRobot.setSyncId(hddvModifySelect.getId().intValue());
				dataRequestRobot.setServiceType(hddvModifySelect.getMode());

				if (hddvModifySelect.getMode() == InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_GIAM.value()) {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_GIAM.title());
				} else {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_TANG.title());
				}
				dataRequestRobot.setFastId(hddvModifySelect.getFastId());
				dataRequestRobot.setFastInvoiceNo(hddvModifySelect.getFastInvoiceNo());
				dataRequestRobot.setUnitCode(hddvModifySelect.getUnitCode());
				dataRequestRobot.setCustomerId(hddvModifySelect.getCustomerId());
				dataRequestRobot.setCustomerName(hddvModifySelect.getCustomerName());
				dataRequestRobot.setCustomerTaxCode(hddvModifySelect.getCustomerTaxCode());
				dataRequestRobot.setAddress(hddvModifySelect.getAddress());
				dataRequestRobot
						.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hddvModifySelect.getInvoiceDate()));
				dataRequestRobot.setInvoiceModifyDate(
						DateUtils.parseDateToStr("dd/MM/yyyy", hddvModifySelect.getInvoiceModifyDate()));
				dataRequestRobot.setDescription(hddvModifySelect.getDescription());
				dataRequestRobot.setTotalTaxAmount(hddvModifySelect.getTotalTaxAmount());
				dataRequestRobot.setSubtotalAmount(hddvModifySelect.getSubtotalAmount());
				dataRequestRobot.setTotalAmount(hddvModifySelect.getTotalAmount());

				// Hoa don modify detail
				HddvDetail hddvModifyDetailSelect = new HddvDetail();
				hddvModifyDetailSelect.setHddvId(hddvModifySelect.getId());
				List<HddvDetail> listHddvModifyDetail = hddvModifyDetailMapper
						.selectHddvModifyDetailList(hddvModifyDetailSelect);

				List<HddvDetailRes> listHddvModifyDetailRes = new ArrayList<HddvDetailRes>();

				if (!CollectionUtils.isEmpty(listHddvModifyDetail)) {
					for (HddvDetail detail : listHddvModifyDetail) {
						HddvDetailRes hddvDetailRes = new HddvDetailRes();
						hddvDetailRes.setFastId(detail.getFastId());
						hddvDetailRes.setFastDetailId(detail.getFastDetailId());
						hddvDetailRes.setProductName(detail.getProductName());
						hddvDetailRes.setProductTaxId(detail.getProductTaxId());
						hddvDetailRes.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hddvDetailRes.setProductTaxAmount(detail.getProductTaxAmount());
						hddvDetailRes.setProductTotalAmount(detail.getProductTotalAmount());
						listHddvModifyDetailRes.add(hddvDetailRes);
					}
					dataRequestRobot.setOrderQuantity(listHddvModifyDetailRes.size());
					dataRequestRobot.setProcessOrder(listHddvModifyDetailRes);
				}

				// Tim fastId trong table workProcess
				WorkProcess workProcessExist = workProcessMapper
						.selectWorkProcessBySyncKey(hddvModifySelect.getFastId());

				// delete record ton tai table WorkProcess
				if (workProcessExist != null) {
					workProcessMapper.deleteWorkProcessById(workProcessExist.getId());
				}
				// insert record vao table WorkProcess
				workProcessMapper.insertWorkProcess(workProcessNew);

				// set processId cua Json (data_request) sau khi insert thanh cong
				dataRequestRobot.setProcessId(workProcessNew.getId());
				hddvModifySelect.setProcessId(workProcessNew.getId());

				// update status table hddv_modify
				hddvModifySelect.setStatus(ProcessStatus.WAIT.value());
				hddvModifyMapper.updateHddvModify(hddvModifySelect);

				// them record (data_request) vao robotRequestMap
				robotRequestMap.put(dataRequestRobot, robots.get(0));
			}

			// Loop insert/update dataRequest vao table workProcess
			if (robotRequestMap.size() > 0) {
				for (Map.Entry<RobotSyncHddvReq, Robot> entry : robotRequestMap.entrySet()) {
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
	private void requestRobot(Robot robot, RobotSyncHddvReq process) {

		String message = new Gson().toJson(process);

		WorkProcess processUpdate = new WorkProcess();
		processUpdate.setId(process.getProcessId());
		processUpdate.setDataRequest(message);
		processUpdate.setRobotUuid(robot.getUuid());
		workProcessMapper.updateWorkProcess(processUpdate);
	}
}