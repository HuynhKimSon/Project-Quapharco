package vn.com.irtech.irbot.business.service.impl;

import java.math.BigDecimal;
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

import vn.com.irtech.core.common.page.TableDataInfo;
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
import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhRes;
import vn.com.irtech.irbot.business.dto.response.HdbhDetailRes;
import vn.com.irtech.irbot.business.dto.response.HdbhMasterRes;
import vn.com.irtech.irbot.business.mapper.HdbhModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdbhModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.mqtt.MqttService;
import vn.com.irtech.irbot.business.service.IApiGatewayService;
import vn.com.irtech.irbot.business.service.IHdbhService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdbhServiceImpl implements IHdbhService {

	private static final Logger logger = LoggerFactory.getLogger(HdbhServiceImpl.class);

	@Autowired
	private IApiGatewayService apiGatewayService;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private HdbhModifyMapper hdbhModifyMapper;

	@Autowired
	private HdbhModifyDetailMapper hdbhModifyDetailMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HdbhMasterRes selectHdbhById(String fastId) throws Exception {
		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setFastId(fastId);
		apiRequest.setIsSelectDetail(true);

		ApiGetDataHdbhRes result = getHdbhData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdbhMasterRes> listHdbhFast = result.getData();

		if (CollectionUtils.isEmpty(listHdbhFast)) {
			throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
		}

		HdbhMasterRes hdbh = listHdbhFast.get(0);

		WorkProcess workProcessSelect = new WorkProcess();
		String syncKey = hdbh.getFastId();
		workProcessSelect.setSyncKey(syncKey);

		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {
			hdbh.setProcessStatus(listWorkProcessExist.get(0).getStatus());
		}

		return hdbh;
	}

	@Override
	public TableDataInfo selectHdbhList(Hdbh hdbh) throws Exception {

		if (hdbh.getParams() == null) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		Object beginTimeObj = hdbh.getParams().get("beginTime");
		Object endTimeObj = hdbh.getParams().get("endTime");
		Object fastInvoiceNoObj = hdbh.getParams().get("fastInvoiceNo");

		if (beginTimeObj == null || endTimeObj == null) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		String beginTime = beginTimeObj.toString();
		String endTime = endTimeObj.toString();
		String fastInoviceNo = fastInvoiceNoObj.toString();

		Date fromDate = DateUtils.parseDate(beginTime + " 00:00:00", DateUtils.YYYY_MM_DD_HH_MM_SS);
		Date toDate = DateUtils.parseDate(endTime + " 23:59:59", DateUtils.YYYY_MM_DD_HH_MM_SS);
		Date now = DateUtils.addSeconds(new Date(), -30);
		if (toDate.after(now)) {
			toDate = now;
		}

		if (fromDate.after(toDate)) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		long difference_In_Days = ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24)) % 365;

		if (difference_In_Days > 7) {
			throw new Exception("Ngày tìm kiếm không được vượt quá 7 ngày!");
		}

		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setStartDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, fromDate));
		apiRequest.setEndDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, toDate));
		apiRequest.setFastInvoiceNo(fastInoviceNo);
		apiRequest.setIsSelectDetail(false);

		ApiGetDataHdbhRes result = getHdbhData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdbhMasterRes> listHdbhFast = result.getData();
		logger.info(">> So hoa don lay ve hien thi: " + listHdbhFast.size());

		if (CollectionUtils.isNotEmpty(listHdbhFast)) {
			List<HdbhMasterRes> removeItems = new ArrayList<HdbhMasterRes>();
			Boolean searchByStatus = (hdbh.getStatus() != null && hdbh.getStatus() > 0);

			for (HdbhMasterRes hdbhMasterRes : listHdbhFast) {
				WorkProcess workProcessSelect = new WorkProcess();
				String syncKey = hdbhMasterRes.getFastId();

				workProcessSelect.setSyncKey(syncKey);
				workProcessSelect.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());

				// Set theo trang thai fillter
				if (searchByStatus) {
					workProcessSelect.setStatus(hdbh.getStatus());
				}

				List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

				if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {

					// Add hoa don da ton tai trong work process vao list removeItems
					if (hdbh.getStatus() == ProcessStatus.NOTSEND.value()) {
						removeItems.add(hdbhMasterRes);
					} else {
						hdbhMasterRes.setProcessStatus(listWorkProcessExist.get(0).getStatus());
					}

					// View fast e-invoiceNo neu trang thai thanh cong
					if (hdbhMasterRes.getProcessStatus() == ProcessStatus.SUCCESS.value()) {
						hdbhMasterRes.seteInvoiceNo(listWorkProcessExist.get(0).getData1());
					}

					// Set trang thai goc/ da chinh sua
					if (listWorkProcessExist.get(0).getData2() != null) {
						hdbhMasterRes.setMode(listWorkProcessExist.get(0).getData2());
					}

				} else if (searchByStatus && hdbh.getStatus() > 0) {
					removeItems.add(hdbhMasterRes);
				}
			}

			// Remove tat ca item khong du dieu kien trong list view
			if (CollectionUtils.isNotEmpty(removeItems)) {
				listHdbhFast.removeAll(removeItems);
			}
		}

		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(listHdbhFast);
		rspData.setTotal(listHdbhFast.size());

		return rspData;
	}

	/**
	 * 
	 * @param request
	 * @return ApiGetDataHdbhRes
	 * @throws Exception
	 */
	private ApiGetDataHdbhRes getHdbhData(ApiGetDataReq request) throws Exception {
		ApiGetDataHdbhRes hdbhList = apiGatewayService.getListHdbhMaster(request);
		return hdbhList;
	}

	@Override
	@Transactional
	public void retry(String ids) throws Exception {
		try {
			// Get a robot available, busy
			List<Robot> robots = robotMapper.selectRobotByServices(
					new Integer[] { RobotServiceType.HOA_DON_GTGT_GOC.value() },
					new String[] { RobotStatusType.AVAILABLE.value(), RobotStatusType.BUSY.value() });

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			Map<RobotSyncHdbhReq, Robot> robotRequestMap = new HashMap<RobotSyncHdbhReq, Robot>();

			String[] idArr = Convert.toStrArray(ids);
			Date now = new Date();
			for (String fastId : idArr) {

				// Call Api lay du lieu theo FastId
				ApiGetDataReq apiRequest = new ApiGetDataReq();
				apiRequest.setFastId(fastId);
				apiRequest.setIsSelectDetail(true);

				ApiGetDataHdbhRes result = getHdbhData(apiRequest);

				if (result == null || result.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdbhMasterRes> listHdbhFast = result.getData();

				if (CollectionUtils.isEmpty(listHdbhFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdbhMasterRes hdbhMasterRes = listHdbhFast.get(0);

				// Tim fastId trong table workProcess
				String syncKey = fastId;
				WorkProcess workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(syncKey);
				if (workProcessExist != null) {
					if (workProcessExist.getStatus() == (ProcessStatus.SUCCESS.value())) {
						throw new Exception("Tồn tại hoá đơn đã đồng bộ thành công!");
					}
				}

				// Add Master table workProcess
				WorkProcess workProcessNew = new WorkProcess();
				workProcessNew.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());
				workProcessNew.setSyncId("0");
				workProcessNew.setSyncKey(syncKey);
				workProcessNew.setData2(ProcessMode.GOC.value().toString());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Add data request
				RobotSyncHdbhReq dataRequestRobot = dataRequestRobot(hdbhMasterRes);

				// delete record ton tai table WorkProcess
				if (workProcessExist != null) {
					workProcessMapper.deleteWorkProcessById(workProcessExist.getId());
				}
				// insert record vao table WorkProcess
				workProcessMapper.insertWorkProcess(workProcessNew);

				// set processId cua Json (data_request) sau khi insert thanh cong
				dataRequestRobot.setProcessId(workProcessNew.getId());

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

	private RobotSyncHdbhReq dataRequestRobot(HdbhMasterRes hdbhMasterRes) {

		RobotSyncHdbhReq dataRequest = new RobotSyncHdbhReq();
		dataRequest.setConfig(getProcessConfig(hdbhMasterRes.getUnitCode()));
		dataRequest.setSyncId(0);
		dataRequest.setServiceType(InvoiceMode.HOA_DON_BAN_HANG_GOC.value());
		dataRequest.setServiceName(InvoiceMode.HOA_DON_BAN_HANG_GOC.title());
		dataRequest.setFastId(hdbhMasterRes.getFastId());
		dataRequest.setFastInvoiceNo(hdbhMasterRes.getFastInvoiceNo());
		dataRequest.setUnitCode(hdbhMasterRes.getUnitCode());
		dataRequest.setCustomerId(hdbhMasterRes.getCustomerId());
		dataRequest.setCustomerName(hdbhMasterRes.getCustomerName());
		dataRequest.setCustomerTaxCode(hdbhMasterRes.getCustomerTaxCode());
		dataRequest.setAddress(hdbhMasterRes.getAddress());
		dataRequest.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhMasterRes.getInvoiceDate()));
		dataRequest.setDescription(hdbhMasterRes.getDescription());
		dataRequest.setPaymentMethod(hdbhMasterRes.getPaymentMethod());
		dataRequest.setTotalTaxAmount(hdbhMasterRes.getTotalTaxAmount());
		dataRequest.setSubtotalAmount(hdbhMasterRes.getSubtotalAmount());
		dataRequest.setTotalAmount(hdbhMasterRes.getTotalAmount());
		dataRequest.setOrderQuantity(hdbhMasterRes.getDetails().size());
		dataRequest.setProcessOrder(hdbhMasterRes.getDetails());

		return dataRequest;
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
	public void updateStatus(String ids, Integer status) throws Exception {
		logger.info("Reset Status Hoa don ban hang !");
		String idArr[] = Convert.toStrArray(ids);
		for (String id : idArr) {
			WorkProcess workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(id);
			if (workProcessExist != null) {
				WorkProcess workProcessUpdate = new WorkProcess();
				workProcessUpdate.setSyncKey(id);
				workProcessUpdate.setStatus(status);
				workProcessMapper.updateWorkProcessSyncKey(workProcessUpdate);
			} else {
				// Call Api lay du lieu theo FastId
				ApiGetDataReq apiRequest = new ApiGetDataReq();
				apiRequest.setFastId(id);
				apiRequest.setIsSelectDetail(true);

				ApiGetDataHdbhRes apiGetDataHdbhRes = getHdbhData(apiRequest);

				if (apiGetDataHdbhRes == null || apiGetDataHdbhRes.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdbhMasterRes> listHdbhFast = apiGetDataHdbhRes.getData();

				if (CollectionUtils.isEmpty(listHdbhFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdbhMasterRes hdbhMasterRes = listHdbhFast.get(0);

				RobotSyncHdbhReq dataRequestRobot = dataRequestRobot(hdbhMasterRes);
				String message = new Gson().toJson(dataRequestRobot);

				WorkProcess workProcessInsert = new WorkProcess();
				workProcessInsert.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());
				workProcessInsert.setSyncId("0");
				workProcessInsert.setSyncKey(id);
				workProcessInsert.setData2(ProcessMode.GOC.value().toString());
				workProcessInsert.setStatus(ProcessStatus.SUCCESS.value());
				workProcessInsert.setDataRequest(message);
				workProcessMapper.insertWorkProcess(workProcessInsert);
			}
		}
	}

	@Override
	public void updateMode(String ids) throws Exception {
		logger.info("Reset Mode Hoa don ban hang !");
		String idArr[] = Convert.toStrArray(ids);
		for (String id : idArr) {
			WorkProcess workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(id);
			if (workProcessExist == null) {
				throw new Exception("Phiếu chưa được đồng bộ!");
			}

			WorkProcess workProcessUpdate = new WorkProcess();
			workProcessUpdate.setId(workProcessExist.getId());
			if (Integer.parseInt(workProcessExist.getData2()) == ProcessMode.GOC.value()) {
				workProcessUpdate.setData2(ProcessMode.DA_DIEU_CHINH.value().toString());
			} else {
				workProcessUpdate.setData2(ProcessMode.GOC.value().toString());
			}
			workProcessMapper.updateWorkProcess(workProcessUpdate);
		}
	}

	@Override
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception {
		WorkProcess workProcess = workProcessMapper.selectWorkProcessBySyncKey(request.getFastId());
		if (workProcess == null) {
			throw new Exception("Phiếu chưa được đồng bộ!");
		}

		WorkProcess workProcessUpdate = new WorkProcess();
		workProcessUpdate.setId(workProcess.getId());
		workProcessUpdate.setData1(request.geteInvoiceNo());
		workProcessMapper.updateWorkProcess(workProcessUpdate);
	}

	@Override
	@Transactional
	public TableDataInfo checkModify(String fastId) throws Exception {
		logger.info("Check modify Hoa don ban hang !");

		// Lay fastId(stt_rec)
		String[] temp = fastId.split("_");
		fastId = temp[0];

		List<HdbhMasterRes> listHdbhModify = new ArrayList<HdbhMasterRes>();

		TableDataInfo rspData = new TableDataInfo();
		rspData.setRows(listHdbhModify);

		WorkProcess workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(fastId);
		if (workProcessExist == null) {
			throw new Exception("Không tìm thấy số hoá đơn trên hệ thống IRBot!");
		}

		if (workProcessExist.getData2() != null
				&& Integer.parseInt(workProcessExist.getData2()) == ProcessMode.DA_DIEU_CHINH.value()) {
			throw new Exception("Hoá đơn đã được điều chỉnh 1 lần trước đó!");
		}

		if (workProcessExist.getStatus() != null && workProcessExist.getStatus() != ProcessStatus.SUCCESS.value()) {
			throw new Exception("Hoá đơn gốc bắt buộc phải đồng bộ thành công!");

		}

		// Call API de lay thong tin theo fastId(stt_rec)
		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setFastId(fastId);
		apiRequest.setIsSelectDetail(true);

		ApiGetDataHdbhRes result = getHdbhData(apiRequest);

		// Kiem tra ton tai so hoa don tu API, neu khong ton tai thi bao loi
		if (result == null || result.getData() == null) {
			throw new Exception("Không tìm thấy số hoá đơn trên hệ thống Fast!");
		}

		// Du lieu API tra ve
		List<HdbhMasterRes> listHdbhFast = result.getData();
		HdbhMasterRes hdbhFast = listHdbhFast.get(0);

		// Du lieu lay tu table workprocess
		HdbhMasterRes hdbhIrbot = readDataRequest(workProcessExist);

		// Truong hop thay doi gia
		if (CollectionUtils.isNotEmpty(hdbhFast.getDetails()) && CollectionUtils.isNotEmpty(hdbhIrbot.getDetails())) {

			// Kiem tra so luong mat hang chi tiet
			if (hdbhFast.getDetails().size() != hdbhIrbot.getDetails().size() || hdbhFast.getDetails().size() == 0
					|| hdbhIrbot.getDetails().size() == 0) {
				throw new Exception(
						"Tổng số lượng mặt hàng của Fast và IRBot tại thông tin chi tiết không trùng khớp!");
			}

			// Kiem tra dieu chinh gia tang
			HdbhMasterRes listHdbhMasterPriceIncrease = getListHdbhDetailPriceIncrease(hdbhFast, hdbhIrbot);
			if (listHdbhMasterPriceIncrease.getDetails().size() > 0) {
				listHdbhModify.add(listHdbhMasterPriceIncrease);
			}

			// Kiem tra dieu chinh gia giam
			HdbhMasterRes getListHdbhMasterPriceReduce = getListHdbhDetailPriceReduce(hdbhFast, hdbhIrbot);
			if (getListHdbhMasterPriceReduce.getDetails().size() > 0) {
				listHdbhModify.add(getListHdbhMasterPriceReduce);
			}
		}

		rspData.setCode(0);
		rspData.setRows(listHdbhModify);
		rspData.setTotal(listHdbhModify.size());

		return rspData;
	}

	/**
	 * 
	 * @param workProcessExist
	 * @return HdbhMasterRes
	 */
	private HdbhMasterRes readDataRequest(WorkProcess workProcessExist) {
		String dataRequest = workProcessExist.getDataRequest();
		HdbhMasterRes hdbhMasterRes = new HdbhMasterRes();
		try {
			RobotSyncHdbhReq robotSyncHdbhReq = new Gson().fromJson(dataRequest, RobotSyncHdbhReq.class);
			hdbhMasterRes.setFastId(robotSyncHdbhReq.getFastId());
			hdbhMasterRes.setFastInvoiceNo(robotSyncHdbhReq.getFastInvoiceNo());
			hdbhMasterRes.seteInvoiceNo(workProcessExist.getData1());
			hdbhMasterRes.setDetails(robotSyncHdbhReq.getProcessOrder());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hdbhMasterRes;
	}

	/**
	 * Kiem tra dieu chinh tang
	 * 
	 * @param hdbhFast
	 * @param hdbhIrbot
	 * @return HdbhMasterRes
	 */
	private HdbhMasterRes getListHdbhDetailPriceIncrease(HdbhMasterRes hdbhFast, HdbhMasterRes hdbhIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdbhFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_TANG.value());

		HdbhMasterRes hdbhMasterIncrease = new HdbhMasterRes();
		hdbhMasterIncrease.setMode(String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_TANG.value()));
		hdbhMasterIncrease.setFastId(fastId);
		hdbhMasterIncrease.setFastInvoiceNo(hdbhFast.getFastInvoiceNo());
		hdbhMasterIncrease.setUnitCode(hdbhFast.getUnitCode());
		hdbhMasterIncrease.setCustomerId(hdbhFast.getCustomerId());
		hdbhMasterIncrease.setBuyerName(hdbhFast.getBuyerName());
		hdbhMasterIncrease.setCustomerName(hdbhFast.getCustomerName());
		hdbhMasterIncrease.setCustomerTaxCode(hdbhFast.getCustomerTaxCode());
		hdbhMasterIncrease.setAddress(hdbhFast.getAddress());
		hdbhMasterIncrease.setInvoiceDate(hdbhFast.getInvoiceDate());
		hdbhMasterIncrease.setInvoiceModifyDate(now);
		hdbhMasterIncrease.setDescription(hdbhFast.getDescription());
		hdbhMasterIncrease.setPaymentMethod(hdbhFast.getPaymentMethod());
		hdbhMasterIncrease.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdbhDetailRes> listHdbhDetailIncrease = new ArrayList<HdbhDetailRes>();

		for (HdbhDetailRes hdbhDetailIrbot : hdbhIrbot.getDetails()) {
			for (HdbhDetailRes hdbhDetailFast : hdbhFast.getDetails()) {
				if (hdbhDetailIrbot.getProductId().equals(hdbhDetailFast.getProductId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdbhDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productPriceFast(API) > productPriceIrbot (TANG)
					// return -1: productPriceFast(API) < productPriceIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == 1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdbhDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdbhDetailRes hdbhDetailNew = new HdbhDetailRes();
						hdbhDetailNew.setFastId(hdbhDetailFast.getFastId());
						hdbhDetailNew.setFastDetailId(hdbhDetailFast.getFastDetailId());
						hdbhDetailNew.setProductId(hdbhDetailFast.getProductId());
						hdbhDetailNew.setProductName(hdbhDetailFast.getProductName());
						hdbhDetailNew.setProductUnit(hdbhDetailFast.getProductUnit());
						hdbhDetailNew.setProductTaxId(hdbhDetailFast.getProductTaxId());
						hdbhDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdbhDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdbhDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdbhDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdbhDetailNew.setProductDiscount(hdbhDetailFast.getProductDiscount());
						hdbhDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdbhDetailNew.setProductExpiredDate(hdbhDetailFast.getProductExpiredDate());
						hdbhDetailNew.setStockOut(hdbhDetailFast.getStockOut());
						hdbhDetailNew.setStockOutName(hdbhDetailFast.getStockOutName());
						hdbhDetailNew.setLotNo(hdbhDetailFast.getLotNo());
						listHdbhDetailIncrease.add(hdbhDetailNew);
					}
					break;
				}
			}
		}
		hdbhMasterIncrease.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdbhMasterIncrease.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdbhMasterIncrease.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdbhMasterIncrease.setTotalAmount(String.format("%.2f", totalAmount));
		hdbhMasterIncrease.setDetails(listHdbhDetailIncrease);

		return hdbhMasterIncrease;
	}

	/**
	 * * Kiem tra dieu chinh giam
	 * 
	 * @param hdbhFast
	 * @param hdbhIrbot
	 * @return HdbhMasterRes
	 */
	private HdbhMasterRes getListHdbhDetailPriceReduce(HdbhMasterRes hdbhFast, HdbhMasterRes hdbhIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdbhFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_GIAM.value());

		HdbhMasterRes hdbhMasterReduce = new HdbhMasterRes();
		hdbhMasterReduce.setMode(String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_GIAM.value()));
		hdbhMasterReduce.setFastId(fastId);
		hdbhMasterReduce.setFastInvoiceNo(hdbhFast.getFastInvoiceNo());
		hdbhMasterReduce.setUnitCode(hdbhFast.getUnitCode());
		hdbhMasterReduce.setCustomerId(hdbhFast.getCustomerId());
		hdbhMasterReduce.setBuyerName(hdbhFast.getBuyerName());
		hdbhMasterReduce.setCustomerName(hdbhFast.getCustomerName());
		hdbhMasterReduce.setCustomerTaxCode(hdbhFast.getCustomerTaxCode());
		hdbhMasterReduce.setAddress(hdbhFast.getAddress());
		hdbhMasterReduce.setInvoiceDate(hdbhFast.getInvoiceDate());
		hdbhMasterReduce.setInvoiceModifyDate(now);
		hdbhMasterReduce.setDescription(hdbhFast.getDescription());
		hdbhMasterReduce.setPaymentMethod(hdbhFast.getPaymentMethod());
		hdbhMasterReduce.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdbhDetailRes> listHdbhDetailIncrease = new ArrayList<HdbhDetailRes>();

		for (HdbhDetailRes hdbhDetailIrbot : hdbhIrbot.getDetails()) {
			for (HdbhDetailRes hdbhDetailFast : hdbhFast.getDetails()) {

				if (hdbhDetailIrbot.getProductId().equals(hdbhDetailFast.getProductId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdbhDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productPriceFast(API) > productPriceIrbot (TANG)
					// return -1: productPriceFast(API) < productPriceIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == -1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdbhDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdbhDetailRes hdbhDetailNew = new HdbhDetailRes();
						hdbhDetailNew.setFastId(hdbhDetailFast.getFastId());
						hdbhDetailNew.setFastDetailId(hdbhDetailFast.getFastDetailId());
						hdbhDetailNew.setProductId(hdbhDetailFast.getProductId());
						hdbhDetailNew.setProductName(hdbhDetailFast.getProductName());
						hdbhDetailNew.setProductUnit(hdbhDetailFast.getProductUnit());
						hdbhDetailNew.setProductTaxId(hdbhDetailFast.getProductTaxId());
						hdbhDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdbhDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdbhDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdbhDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdbhDetailNew.setProductDiscount(hdbhDetailFast.getProductDiscount());
						hdbhDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdbhDetailNew.setProductExpiredDate(hdbhDetailFast.getProductExpiredDate());
						hdbhDetailNew.setStockOut(hdbhDetailFast.getStockOut());
						hdbhDetailNew.setStockOutName(hdbhDetailFast.getStockOutName());
						hdbhDetailNew.setLotNo(hdbhDetailFast.getLotNo());
						listHdbhDetailIncrease.add(hdbhDetailNew);
					}
					break;
				}
			}
		}
		hdbhMasterReduce.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdbhMasterReduce.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdbhMasterReduce.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdbhMasterReduce.setTotalAmount(String.format("%.2f", totalAmount));
		hdbhMasterReduce.setDetails(listHdbhDetailIncrease);

		return hdbhMasterReduce;
	}

	@Override
	@Transactional
	public void confirmModify(String fastIds) throws Exception {
		logger.info("Xac nhan dieu chinh Hoa don ban hang !");
		WorkProcess workProcessExist = new WorkProcess();
		String fastArr[] = Convert.toStrArray(fastIds);
		for (String id : fastArr) {

			// Lay fastId(stt_rec), mode
			String[] temp = id.split("_");
			String fastId = temp[0];
			String invoiceMode = temp[1];

			HdbhMasterRes hdbhModify = new HdbhMasterRes();

			// Call API de lay thong tin theo fastId(stt_rec)
			ApiGetDataReq apiRequest = new ApiGetDataReq();
			apiRequest.setFastId(fastId);
			apiRequest.setIsSelectDetail(true);

			ApiGetDataHdbhRes result = getHdbhData(apiRequest);

			// Du lieu API tra ve
			List<HdbhMasterRes> listHdbhFast = result.getData();
			HdbhMasterRes hdbhFast = listHdbhFast.get(0);

			// Du lieu lay tu table workprocess
			workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(fastId);
			HdbhMasterRes hdbhIrbot = readDataRequest(workProcessExist);

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_TANG.value()) {
				hdbhModify = getListHdbhDetailPriceIncrease(hdbhFast, hdbhIrbot);
			}

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_GIAM.value()) {
				hdbhModify = getListHdbhDetailPriceReduce(hdbhFast, hdbhIrbot);
			}

			if (hdbhModify != null) {
				Hdbh hdbhInsert = new Hdbh();
				Date now = DateUtils.addSeconds(new Date(), -30);
				// Insert hdbh_modify
				hdbhInsert.setMode(Integer.parseInt(invoiceMode));
				hdbhInsert.setFastId(id);
				hdbhInsert.setFastInvoiceNo(hdbhModify.getFastInvoiceNo());
				hdbhInsert.setUnitCode(hdbhModify.getUnitCode());
				hdbhInsert.setCustomerId(hdbhModify.getCustomerId());
				hdbhInsert.setBuyerName(hdbhModify.getBuyerName());
				hdbhInsert.setCustomerName(hdbhModify.getCustomerName());
				hdbhInsert.setCustomerTaxCode(hdbhModify.getCustomerTaxCode());
				hdbhInsert.setAddress(hdbhModify.getAddress());
				hdbhInsert.setInvoiceDate(hdbhModify.getInvoiceDate());
				hdbhInsert.setInvoiceModifyDate(hdbhModify.getInvoiceModifyDate());
				hdbhInsert.setDescription(hdbhModify.getDescription());
				hdbhInsert.setTotalQuantity(hdbhModify.getTotalQuantity());
				hdbhInsert.setTotalTaxAmount(hdbhModify.getTotalTaxAmount());
				hdbhInsert.setSubtotalAmount(hdbhModify.getSubtotalAmount());
				hdbhInsert.setTotalAmount(hdbhModify.getTotalAmount());
				hdbhInsert.setPaymentMethod(hdbhModify.getPaymentMethod());
				hdbhInsert.setStatus(ProcessStatus.NOTSEND.value());
				hdbhInsert.setCreateTime(now);
				hdbhModifyMapper.insertHdbhModify(hdbhInsert);

				// Insert detail data
				if (hdbhModify.getDetails() != null) {
					for (HdbhDetailRes detail : hdbhModify.getDetails()) {
						HdbhDetail hdbhDetailInsert = new HdbhDetail();
						hdbhDetailInsert.setHdbhId(hdbhInsert.getId());
						hdbhDetailInsert.setFastId(hdbhInsert.getFastId());
						hdbhDetailInsert.setFastDetailId(detail.getFastDetailId());
						hdbhDetailInsert.setProductId(detail.getProductId());
						hdbhDetailInsert.setProductUnit(detail.getProductUnit());
						hdbhDetailInsert.setProductName(detail.getProductName());
						hdbhDetailInsert.setProductTaxId(detail.getProductTaxId());
						hdbhDetailInsert.setProductQuantity(detail.getProductQuantity());
						hdbhDetailInsert.setProductPrice(detail.getProductPrice());
						hdbhDetailInsert.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdbhDetailInsert.setProductTaxAmount(detail.getProductTaxAmount());
						hdbhDetailInsert.setProductDiscount(detail.getProductDiscount());
						hdbhDetailInsert.setProductTotalAmount(detail.getProductTotalAmount());
						hdbhDetailInsert.setProductExpiredDate(detail.getProductExpiredDate());
						hdbhDetailInsert.setStockOut(detail.getStockOut());
						hdbhDetailInsert.setStockOutName(detail.getStockOutName());
						hdbhDetailInsert.setLotNo(detail.getLotNo());
						hdbhDetailInsert.setCreateTime(now);
						hdbhModifyDetailMapper.insertHdbhModifyDetail(hdbhDetailInsert);
					}
				}
			}
		}
		// Set Mode da dieu chinh
		workProcessExist.setData2(String.valueOf(ProcessMode.DA_DIEU_CHINH.value()));
		workProcessMapper.updateWorkProcessSyncKey(workProcessExist);
	}
}