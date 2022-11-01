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
import vn.com.irtech.irbot.business.domain.HdbhNb;
import vn.com.irtech.irbot.business.domain.HdbhNbDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHdbhNbReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdbhNbDetailRes;
import vn.com.irtech.irbot.business.mapper.HdbhNbModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdbhNbModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.mqtt.MqttService;
import vn.com.irtech.irbot.business.service.IHdbhNbModifyService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdbhNbModifyServiceImpl implements IHdbhNbModifyService {

	private static final Logger logger = LoggerFactory.getLogger(HdbhNbServiceImpl.class);

	@Autowired
	private HdbhNbModifyMapper hdbhNbModifyMapper;

	@Autowired
	private HdbhNbModifyDetailMapper hdbhNbModifyDetailMapper;

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
	public HdbhNb selectHdbhNbModifyById(Long id) {
		return hdbhNbModifyMapper.selectHdbhNbModifyById(id);
	}

	@Override
	public List<HdbhNb> selectHdbhNbModifyList(HdbhNb hdbhNb) {
		return hdbhNbModifyMapper.selectHdbhNbModifyList(hdbhNb);
	}

	@Override
	public int updateStatus(String ids, Integer status) {
		logger.info("Reset Status Hoa don noi bo dieu chinh !");
		Long idArr[] = Convert.toLongArray(ids);
		int result = 0;
		for (Long id : idArr) {
			HdbhNb hdbhNbExist = hdbhNbModifyMapper.selectHdbhNbModifyById(id);
			if (hdbhNbExist != null) {
				HdbhNb hdbhNb = new HdbhNb();
				hdbhNb.setId(id);
				hdbhNb.setStatus(status);
				hdbhNbModifyMapper.updateHdbhNbModify(hdbhNb);
				result++;
			}
		}
		return result;
	}

	@Override
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception {
		HdbhNb hdbhNbModifyUpdate = new HdbhNb();
		hdbhNbModifyUpdate.setId(Long.valueOf(request.getFastId()));
		hdbhNbModifyUpdate.seteInvoiceNo(request.geteInvoiceNo());
		hdbhNbModifyMapper.updateHdbhNbModify(hdbhNbModifyUpdate);
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
					new Integer[] { RobotServiceType.XUAT_NOI_BO_DIEU_CHINH.value() },
					new String[] { RobotStatusType.AVAILABLE.value(), RobotStatusType.BUSY.value() });

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			Map<RobotSyncHdbhNbReq, Robot> robotRequestMap = new HashMap<RobotSyncHdbhNbReq, Robot>();
			Long idArr[] = Convert.toLongArray(ids);
			Date now = new Date();
			for (Long id : idArr) {
				HdbhNb hdbhNbModifySelect = hdbhNbModifyMapper.selectHdbhNbModifyById(id);

				if (hdbhNbModifySelect == null) {
					logger.info("Hoa don boi bo dieu chinh not exist : {}" + id);
					throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
				} else {
					if (hdbhNbModifySelect.getStatus() == (ProcessStatus.SUCCESS.value())) {
						throw new Exception("Tồn tại hoá đơn đã đồng bộ thành công!");
					}
				}

				WorkProcess workProcessNew = new WorkProcess();
				workProcessNew.setServiceId(RobotServiceType.XUAT_NOI_BO_DIEU_CHINH.value());
				workProcessNew.setSyncId(hdbhNbModifySelect.getId().toString());
				workProcessNew.setSyncKey(hdbhNbModifySelect.getFastId());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Hoa don modify master
				RobotSyncHdbhNbReq dataRequestRobot = new RobotSyncHdbhNbReq();
				dataRequestRobot.setConfig(getProcessConfig(hdbhNbModifySelect.getUnitCode()));
				dataRequestRobot.setSyncId(hdbhNbModifySelect.getId().intValue());
				dataRequestRobot.setServiceType(hdbhNbModifySelect.getMode());

				if (hdbhNbModifySelect.getMode() == InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_GIAM.value()) {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_GIAM.title());
				} else {
					dataRequestRobot.setServiceName(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_TANG.title());
				}
				dataRequestRobot.setFastId(hdbhNbModifySelect.getFastId());
				dataRequestRobot.setFastInvoiceNo(hdbhNbModifySelect.getFastInvoiceNo());
				dataRequestRobot.setUnitCode(hdbhNbModifySelect.getUnitCode());
				dataRequestRobot.setCustomerId(hdbhNbModifySelect.getCustomerId());
				dataRequestRobot.setCustomerName(hdbhNbModifySelect.getCustomerName());
				dataRequestRobot.setCustomerTaxCode(hdbhNbModifySelect.getCustomerTaxCode());
				dataRequestRobot.setAddress(hdbhNbModifySelect.getAddress());
				dataRequestRobot
						.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhNbModifySelect.getInvoiceDate()));
				dataRequestRobot.setInvoiceModifyDate(
						DateUtils.parseDateToStr("dd/MM/yyyy", hdbhNbModifySelect.getInvoiceModifyDate()));
				dataRequestRobot.setTransporter(hdbhNbModifySelect.getTransporter());
				
				// Description
				if (hdbhNbModifySelect.getDescription() != null) {
					dataRequestRobot.setDescription(hdbhNbModifySelect.getDescription());
				}
				
				// Vehicle
				if (hdbhNbModifySelect.getVehicle() != null) {
					dataRequestRobot.setVehicle(hdbhNbModifySelect.getVehicle());
				}
				
				// StockIn
				if (hdbhNbModifySelect.getStockIn() != null) {
					dataRequestRobot.setStockIn(hdbhNbModifySelect.getStockIn());
				} else {
					dataRequestRobot.setStockIn("");
				}
				
				// StockInName
				if (hdbhNbModifySelect.getStockInName() != null) {
					dataRequestRobot.setStockInName(hdbhNbModifySelect.getStockInName());
				} else {
					dataRequestRobot.setStockInName("");
				}
				
				// OrderNo
				if (hdbhNbModifySelect.getOrderNo() != null) {
					dataRequestRobot.setOrderNo(hdbhNbModifySelect.getOrderNo());
				} else {
					dataRequestRobot.setOrderNo("");
				}
				
				// OrderDate
				if (hdbhNbModifySelect.getOrderDate() != null) {
					dataRequestRobot
							.setOrderDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhNbModifySelect.getOrderDate()));
				} else {
					dataRequestRobot.setOrderDate("");
				}
				
				dataRequestRobot.setTotalTaxAmount(hdbhNbModifySelect.getTotalTaxAmount());
				dataRequestRobot.setSubtotalAmount(hdbhNbModifySelect.getSubtotalAmount());
				dataRequestRobot.setTotalAmount(hdbhNbModifySelect.getTotalAmount());

				// Hoa don modify detail
				HdbhNbDetail hdbhNbModifyDetailSelect = new HdbhNbDetail();
				hdbhNbModifyDetailSelect.setHdbhNbId(hdbhNbModifySelect.getId());
				List<HdbhNbDetail> listHdbhNbModifyDetail = hdbhNbModifyDetailMapper
						.selectHdbhNbModifyDetailList(hdbhNbModifyDetailSelect);

				List<HdbhNbDetailRes> listHdbhNbModifyDetailRes = new ArrayList<HdbhNbDetailRes>();

				if (!CollectionUtils.isEmpty(listHdbhNbModifyDetail)) {
					for (HdbhNbDetail detail : listHdbhNbModifyDetail) {
						HdbhNbDetailRes hdbhNbDetailRes = new HdbhNbDetailRes();
						hdbhNbDetailRes.setFastId(detail.getFastId());
						hdbhNbDetailRes.setFastDetailId(detail.getFastDetailId());
						hdbhNbDetailRes.setProductId(detail.getProductId());
						hdbhNbDetailRes.setProductName(detail.getProductName());
						hdbhNbDetailRes.setProductUnit(detail.getProductUnit());
						hdbhNbDetailRes.setProductQuantity(detail.getProductQuantity());
						hdbhNbDetailRes.setProductPrice(detail.getProductPrice());
						hdbhNbDetailRes.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdbhNbDetailRes.setProductTaxAmount(detail.getProductTaxAmount());
						hdbhNbDetailRes.setProductTotalAmount(detail.getProductTotalAmount());
						hdbhNbDetailRes.setProductExpiredDate(detail.getProductExpiredDate());
						hdbhNbDetailRes.setStockOut(detail.getStockOut());
						hdbhNbDetailRes.setStockOutName(detail.getStockOutName());
						hdbhNbDetailRes.setLotNo(detail.getLotNo());
						listHdbhNbModifyDetailRes.add(hdbhNbDetailRes);
					}
					dataRequestRobot.setOrderQuantity(listHdbhNbModifyDetailRes.size());
					dataRequestRobot.setProcessOrder(listHdbhNbModifyDetailRes);
				}

				// Tim fastId trong table workProcess
				WorkProcess workProcessExist = workProcessMapper
						.selectWorkProcessBySyncKey(hdbhNbModifySelect.getFastId());

				// delete record ton tai table WorkProcess
				if (workProcessExist != null) {
					workProcessMapper.deleteWorkProcessById(workProcessExist.getId());
				}
				// insert record vao table WorkProcess
				workProcessMapper.insertWorkProcess(workProcessNew);

				// set processId cua Json (data_request) sau khi insert thanh cong
				dataRequestRobot.setProcessId(workProcessNew.getId());
				hdbhNbModifySelect.setProcessId(workProcessNew.getId());

				// update status table hdbhNb_modify
				hdbhNbModifySelect.setStatus(ProcessStatus.WAIT.value());
				hdbhNbModifyMapper.updateHdbhNbModify(hdbhNbModifySelect);

				// them record (data_request) vao robotRequestMap
				robotRequestMap.put(dataRequestRobot, robots.get(0));
			}

			// Loop insert/update dataRequest vao table workProcess
			if (robotRequestMap.size() > 0) {
				for (Map.Entry<RobotSyncHdbhNbReq, Robot> entry : robotRequestMap.entrySet()) {
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
	private void requestRobot(Robot robot, RobotSyncHdbhNbReq process) {

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