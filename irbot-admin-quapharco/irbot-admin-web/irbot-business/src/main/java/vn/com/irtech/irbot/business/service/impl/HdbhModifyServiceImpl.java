package vn.com.irtech.irbot.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
import vn.com.irtech.irbot.business.domain.Hdbh;
import vn.com.irtech.irbot.business.domain.HdbhDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHdbhReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdbhDetailRes;
import vn.com.irtech.irbot.business.mapper.HdbhModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdbhModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.mqtt.MqttService;
import vn.com.irtech.irbot.business.service.IHdbhModifyService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdbhModifyServiceImpl implements IHdbhModifyService {

	private static final Logger logger = LoggerFactory.getLogger(HdbhServiceImpl.class);

	@Autowired
	private HdbhModifyMapper hdbhModifyMapper;

	@Autowired
	private HdbhModifyDetailMapper hdbhModifyDetailMapper;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public Hdbh selectHdbhModifyById(Long id) {
		return hdbhModifyMapper.selectHdbhModifyById(id);
	}

	@Override
	public List<Hdbh> selectHdbhModifyList(Hdbh hdbh) {
		return hdbhModifyMapper.selectHdbhModifyList(hdbh);
	}

	@Override
	public int updateStatus(String ids, Integer status) {
		logger.info("Reset Status Hoa don ban hang dieu chinh !");
		Long idArr[] = Convert.toLongArray(ids);
		int result = 0;
		for (Long id : idArr) {
			Hdbh hdbhExist = hdbhModifyMapper.selectHdbhModifyById(id);
			if (hdbhExist != null) {
				Hdbh hdbh = new Hdbh();
				hdbh.setId(id);
				hdbh.setStatus(status);
				hdbhModifyMapper.updateHdbhModify(hdbh);
				result++;
			}
		}
		return result;
	}

	@Override
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception {
		Hdbh hdbhModifyUpdate = new Hdbh();
		hdbhModifyUpdate.setId(Long.valueOf(request.getFastId()));
		hdbhModifyUpdate.seteInvoiceNo(request.geteInvoiceNo());
		hdbhModifyMapper.updateHdbhModify(hdbhModifyUpdate);
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

			Map<RobotSyncHdbhReq, Robot> robotRequestMap = new HashMap<RobotSyncHdbhReq, Robot>();
			Long idArr[] = Convert.toLongArray(ids);
			Date now = new Date();
			for (Long id : idArr) {
				Hdbh hdbhModifySelect = hdbhModifyMapper.selectHdbhModifyById(id);

				if (hdbhModifySelect == null) {
					logger.info("Hoa don ban hang not exist : {}" + id);
					throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
				} else {
					if (hdbhModifySelect.getStatus() == (ProcessStatus.SUCCESS.value())) {
						throw new Exception("Tồn tại hoá đơn đã đồng bộ thành công!");
					}
				}

				WorkProcess workProcessNew = new WorkProcess();
				workProcessNew.setServiceId(RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value());
				workProcessNew.setSyncId(hdbhModifySelect.getId().toString());
				workProcessNew.setSyncKey(hdbhModifySelect.getFastId());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Hoa don modify master
				RobotSyncHdbhReq dataRequestRobot = new RobotSyncHdbhReq();
				dataRequestRobot.setConfig(getProcessConfig(hdbhModifySelect.getUnitCode()));
				dataRequestRobot.setSyncId(hdbhModifySelect.getId().intValue());
				dataRequestRobot.setServiceType(hdbhModifySelect.getMode());

				if (hdbhModifySelect.getMode() == InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_GIAM.value()) {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_GIAM.title());
				} else {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_TANG.title());
				}
				dataRequestRobot.setFastId(hdbhModifySelect.getFastId());
				dataRequestRobot.setFastInvoiceNo(hdbhModifySelect.getFastInvoiceNo());
				dataRequestRobot.setUnitCode(hdbhModifySelect.getUnitCode());
				dataRequestRobot.setCustomerId(hdbhModifySelect.getCustomerId());
				dataRequestRobot.setCustomerName(hdbhModifySelect.getCustomerName());
				dataRequestRobot.setCustomerTaxCode(hdbhModifySelect.getCustomerTaxCode());
				dataRequestRobot.setAddress(hdbhModifySelect.getAddress());
				dataRequestRobot
						.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhModifySelect.getInvoiceDate()));
				dataRequestRobot.setInvoiceModifyDate(
						DateUtils.parseDateToStr("dd/MM/yyyy", hdbhModifySelect.getInvoiceModifyDate()));
				dataRequestRobot.setDescription(hdbhModifySelect.getDescription());
				dataRequestRobot.setPaymentMethod(hdbhModifySelect.getPaymentMethod());
				dataRequestRobot.setTotalTaxAmount(hdbhModifySelect.getTotalTaxAmount());
				dataRequestRobot.setSubtotalAmount(hdbhModifySelect.getSubtotalAmount());
				dataRequestRobot.setTotalAmount(hdbhModifySelect.getTotalAmount());

				// Hoa don modify detail
				HdbhDetail hdbhModifyDetailSelect = new HdbhDetail();
				hdbhModifyDetailSelect.setHdbhId(hdbhModifySelect.getId());
				List<HdbhDetail> listHdbhModifyDetail = hdbhModifyDetailMapper
						.selectHdbhModifyDetailList(hdbhModifyDetailSelect);

				List<HdbhDetailRes> listHdbhModifyDetailRes = new ArrayList<HdbhDetailRes>();

				if (!CollectionUtils.isEmpty(listHdbhModifyDetail)) {
					for (HdbhDetail detail : listHdbhModifyDetail) {
						HdbhDetailRes hdbhDetailRes = new HdbhDetailRes();
						hdbhDetailRes.setFastId(detail.getFastId());
						hdbhDetailRes.setFastDetailId(detail.getFastDetailId());
						hdbhDetailRes.setProductId(detail.getProductId());
						hdbhDetailRes.setProductName(detail.getProductName());
						hdbhDetailRes.setProductUnit(detail.getProductUnit());
						hdbhDetailRes.setProductTaxId(detail.getProductTaxId());
						hdbhDetailRes.setProductQuantity(detail.getProductQuantity());
						hdbhDetailRes.setProductPrice(detail.getProductPrice());
						hdbhDetailRes.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdbhDetailRes.setProductTaxAmount(detail.getProductTaxAmount());
						hdbhDetailRes.setProductDiscount(detail.getProductDiscount());
						hdbhDetailRes.setProductTotalAmount(detail.getProductTotalAmount());
						hdbhDetailRes.setProductExpiredDate(detail.getProductExpiredDate());
						hdbhDetailRes.setStockOut(detail.getStockOut());
						hdbhDetailRes.setStockOutName(detail.getStockOutName());
						hdbhDetailRes.setLotNo(detail.getLotNo());
						listHdbhModifyDetailRes.add(hdbhDetailRes);
					}
					dataRequestRobot.setOrderQuantity(listHdbhModifyDetailRes.size());
					dataRequestRobot.setProcessOrder(listHdbhModifyDetailRes);
				}

				// Tim fastId trong table workProcess
				WorkProcess workProcessExist = workProcessMapper
						.selectWorkProcessBySyncKey(hdbhModifySelect.getFastId());

				// delete record ton tai table WorkProcess
				if (workProcessExist != null) {
					workProcessMapper.deleteWorkProcessById(workProcessExist.getId());
				}
				// insert record vao table WorkProcess
				workProcessMapper.insertWorkProcess(workProcessNew);

				// set processId cua Json (data_request) sau khi insert thanh cong
				dataRequestRobot.setProcessId(workProcessNew.getId());
				hdbhModifySelect.setProcessId(workProcessNew.getId());

				// update status table hdbh_modify
				hdbhModifySelect.setStatus(ProcessStatus.WAIT.value());
				hdbhModifyMapper.updateHdbhModify(hdbhModifySelect);

				// them record (data_request) vao robotRequestMap
				robotRequestMap.put(dataRequestRobot, robots.get(0));
			}

			// Loop insert/update dataRequest vao table workProcess
			if (robotRequestMap.size() > 0) {
				for (Map.Entry<RobotSyncHdbhReq, Robot> entry : robotRequestMap.entrySet()) {
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
	private void requestRobot(Robot robot, RobotSyncHdbhReq process) {

		String message = new Gson().toJson(process);

		WorkProcess processUpdate = new WorkProcess();
		processUpdate.setId(process.getProcessId());
		processUpdate.setDataRequest(message);
		processUpdate.setRobotUuid(robot.getUuid());
		workProcessMapper.updateWorkProcess(processUpdate);
	}

	@Override
	public void sendRobot(Map<Robot, WorkProcess> processList) throws Exception {
		if (processList == null) {
			return;
		}
		for (Map.Entry<Robot, WorkProcess> entry : processList.entrySet()) {
			// Push Mqtt
			WorkProcess process = entry.getValue();
			Robot robot = entry.getKey();
			String message = process.getDataRequest();

			WorkProcess processUpdate = new WorkProcess();
			processUpdate.setId(process.getId());
			processUpdate.setStatus(ProcessStatus.SENT.value());
			processUpdate.setRobotUuid(robot.getUuid());
			workProcessMapper.updateWorkProcess(processUpdate);

			String topic = MqttService.TOPIC_ROBOT_BASE + "/" + robot.getUuid() + "/process/request";
			logger.info("Send Mqtt topic {} with message {}", topic, message);
			try {
				mqttService.publish(topic, new MqttMessage(message.getBytes("UTF-8")));
			} catch (Exception e) {
				processUpdate = new WorkProcess();
				processUpdate.setId(process.getId());
				processUpdate.setStatus(ProcessStatus.FAIL.value());
				processUpdate.setStartDate(new Date());
				processUpdate.setEndDate(new Date());
				processUpdate.setError(e.getMessage());
				workProcessMapper.updateWorkProcess(processUpdate);

				logger.error("Error when try sending mqtt message: " + e);
			}
		}
	}
}