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
import vn.com.irtech.irbot.business.domain.HdbhNb;
import vn.com.irtech.irbot.business.domain.HdbhNbDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHdbhNbReq;
import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhNbRes;
import vn.com.irtech.irbot.business.dto.response.HdbhNbDetailRes;
import vn.com.irtech.irbot.business.dto.response.HdbhNbMasterRes;
import vn.com.irtech.irbot.business.mapper.HdbhNbModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdbhNbModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.mqtt.MqttService;
import vn.com.irtech.irbot.business.service.IApiGatewayService;
import vn.com.irtech.irbot.business.service.IHdbhNbService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdbhNbServiceImpl implements IHdbhNbService {

	private static final Logger logger = LoggerFactory.getLogger(HdbhNbServiceImpl.class);

	@Autowired
	private IApiGatewayService apiGatewayService;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private HdbhNbModifyMapper hdbhNbModifyMapper;

	@Autowired
	private HdbhNbModifyDetailMapper hdbhNbModifyDetailMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HdbhNbMasterRes selectHdbhNbById(String fastId) throws Exception {
		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setFastId(fastId);
		apiRequest.setIsSelectDetail(true);

		ApiGetDataHdbhNbRes result = getHdbhNbData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdbhNbMasterRes> listHdbhNbFast = result.getData();

		if (CollectionUtils.isEmpty(listHdbhNbFast)) {
			throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
		}

		HdbhNbMasterRes hdbhNb = listHdbhNbFast.get(0);

		WorkProcess workProcessSelect = new WorkProcess();
		String syncKey = hdbhNb.getFastId();
		workProcessSelect.setSyncKey(syncKey);

		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {
			hdbhNb.setProcessStatus(listWorkProcessExist.get(0).getStatus());
		}

		return hdbhNb;
	}

	@Override
	public TableDataInfo selectHdbhNbList(HdbhNb hdbhNb) throws Exception {

		if (hdbhNb.getParams() == null) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		Object beginTimeObj = hdbhNb.getParams().get("beginTime");
		Object endTimeObj = hdbhNb.getParams().get("endTime");
		Object fastInvoiceNoObj = hdbhNb.getParams().get("fastInvoiceNo");

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

		ApiGetDataHdbhNbRes result = getHdbhNbData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdbhNbMasterRes> listHdbhNbFast = result.getData();
		logger.info(">> So hoa don lay ve hien thi: " + listHdbhNbFast.size());

		if (CollectionUtils.isNotEmpty(listHdbhNbFast)) {
			List<HdbhNbMasterRes> removeItems = new ArrayList<HdbhNbMasterRes>();
			Boolean searchByStatus = (hdbhNb.getStatus() != null && hdbhNb.getStatus() > 0);

			for (HdbhNbMasterRes hdbhNbMasterRes : listHdbhNbFast) {
				WorkProcess workProcessSelect = new WorkProcess();
				String syncKey = hdbhNbMasterRes.getFastId();

				workProcessSelect.setSyncKey(syncKey);
				workProcessSelect.setServiceId(RobotServiceType.XUAT_NOI_BO_GOC.value());

				// Set theo trang thai fillter
				if (searchByStatus) {
					workProcessSelect.setStatus(hdbhNb.getStatus());
				}

				List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

				if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {

					// Add hoa don da ton tai trong work process vao list removeItems
					if (hdbhNb.getStatus() == ProcessStatus.NOTSEND.value()) {
						removeItems.add(hdbhNbMasterRes);
					} else {
						hdbhNbMasterRes.setProcessStatus(listWorkProcessExist.get(0).getStatus());
					}

					// View fast e-invoiceNo neu trang thai thanh cong
					if (hdbhNbMasterRes.getProcessStatus() == ProcessStatus.SUCCESS.value()) {
						hdbhNbMasterRes.seteInvoiceNo(listWorkProcessExist.get(0).getData1());
					}

					// Set trang thai goc/ da chinh sua
					if (listWorkProcessExist.get(0).getData2() != null) {
						hdbhNbMasterRes.setMode(listWorkProcessExist.get(0).getData2());
					}

				} else if (searchByStatus && hdbhNb.getStatus() > 0) {
					removeItems.add(hdbhNbMasterRes);
				}
			}

			// Remove tat ca item khong du dieu kien trong list view
			if (CollectionUtils.isNotEmpty(removeItems)) {
				listHdbhNbFast.removeAll(removeItems);
			}
		}

		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(listHdbhNbFast);
		rspData.setTotal(listHdbhNbFast.size());

		return rspData;
	}

	/**
	 * 
	 * @param request
	 * @return ApiGetDataHdbhNbRes
	 * @throws Exception
	 */
	private ApiGetDataHdbhNbRes getHdbhNbData(ApiGetDataReq request) throws Exception {

		ApiGetDataHdbhNbRes hddvList = apiGatewayService.getHdbhNoiBo(request);
		return hddvList;
	}

	@Override
	@Transactional
	public void retry(String ids) throws Exception {
		try {
			// Get a robot available, busy
			List<Robot> robots = robotMapper.selectRobotByServices(
					new Integer[] { RobotServiceType.XUAT_NOI_BO_GOC.value() },
					new String[] { RobotStatusType.AVAILABLE.value(), RobotStatusType.BUSY.value() });

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			Map<RobotSyncHdbhNbReq, Robot> robotRequestMap = new HashMap<RobotSyncHdbhNbReq, Robot>();

			String[] idArr = Convert.toStrArray(ids);
			Date now = new Date();
			for (String fastId : idArr) {

				// Call Api lay du lieu theo FastId
				ApiGetDataReq apiRequest = new ApiGetDataReq();
				apiRequest.setFastId(fastId);
				apiRequest.setIsSelectDetail(true);

				ApiGetDataHdbhNbRes result = getHdbhNbData(apiRequest);

				if (result == null || result.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdbhNbMasterRes> listHdbhNbFast = result.getData();

				if (CollectionUtils.isEmpty(listHdbhNbFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdbhNbMasterRes hdbhNbMasterRes = listHdbhNbFast.get(0);

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
				workProcessNew.setServiceId(RobotServiceType.XUAT_NOI_BO_GOC.value());
				workProcessNew.setSyncId("0");
				workProcessNew.setSyncKey(syncKey);
				workProcessNew.setData2(ProcessMode.GOC.value().toString());
				workProcessNew.setPriority(RandomUtils.nextInt(0, 9));
				workProcessNew.setStatus(ProcessStatus.WAIT.value());
				workProcessNew.setStartDate(now);

				// Add data request
				RobotSyncHdbhNbReq dataRequestRobot = dataRequestRobot(hdbhNbMasterRes);

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

	private RobotSyncHdbhNbReq dataRequestRobot(HdbhNbMasterRes hdbhNbMasterRes) {

		RobotSyncHdbhNbReq dataRequest = new RobotSyncHdbhNbReq();

		dataRequest.setConfig(getProcessConfig(hdbhNbMasterRes.getUnitCode()));
		dataRequest.setSyncId(0);
		dataRequest.setServiceType(InvoiceMode.HOA_DON_NOI_BO_GOC.value());
		dataRequest.setServiceName(InvoiceMode.HOA_DON_NOI_BO_GOC.title());
		dataRequest.setFastId(hdbhNbMasterRes.getFastId());
		dataRequest.setFastInvoiceNo(hdbhNbMasterRes.getFastInvoiceNo());
		dataRequest.setUnitCode(hdbhNbMasterRes.getUnitCode());
		dataRequest.setCustomerId(hdbhNbMasterRes.getCustomerId());
		dataRequest.setCustomerName(hdbhNbMasterRes.getCustomerName());
		dataRequest.setCustomerTaxCode(hdbhNbMasterRes.getCustomerTaxCode());
		dataRequest.setAddress(hdbhNbMasterRes.getAddress());
		dataRequest.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhNbMasterRes.getInvoiceDate()));
		dataRequest.setTransporter(hdbhNbMasterRes.getTransporter());
		if (hdbhNbMasterRes.getDescription() != null) {
			dataRequest.setDescription(hdbhNbMasterRes.getDescription());
		}
		if (hdbhNbMasterRes.getVehicle() != null) {
			dataRequest.setVehicle(hdbhNbMasterRes.getVehicle());
		}
		if (hdbhNbMasterRes.getStockIn() != null) {
			dataRequest.setStockIn(hdbhNbMasterRes.getStockIn());
		} else {
			dataRequest.setStockIn("");
		}
		if (hdbhNbMasterRes.getStockInName() != null) {
			dataRequest.setStockInName(hdbhNbMasterRes.getStockInName());
		} else {
			dataRequest.setStockInName("");
		}
		if (hdbhNbMasterRes.getOrderNo() != null) {
			dataRequest.setOrderNo(hdbhNbMasterRes.getOrderNo());
		} else {
			dataRequest.setOrderNo("");
		}
		if (hdbhNbMasterRes.getOrderDate() != null) {
			dataRequest.setOrderDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhNbMasterRes.getOrderDate()));
		} else {
			dataRequest.setOrderDate("");
		}
		dataRequest.setTotalTaxAmount(hdbhNbMasterRes.getTotalTaxAmount());
		dataRequest.setSubtotalAmount(hdbhNbMasterRes.getSubtotalAmount());
		dataRequest.setTotalAmount(hdbhNbMasterRes.getTotalAmount());
		dataRequest.setOrderQuantity(hdbhNbMasterRes.getDetails().size());
		dataRequest.setProcessOrder(hdbhNbMasterRes.getDetails());

		return dataRequest;
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
		logger.info("Reset Status Hoa don noi bo !");
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

				ApiGetDataHdbhNbRes apiGetDataHdbhNbRes = getHdbhNbData(apiRequest);

				if (apiGetDataHdbhNbRes == null || apiGetDataHdbhNbRes.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdbhNbMasterRes> listHdbhNbFast = apiGetDataHdbhNbRes.getData();

				if (CollectionUtils.isEmpty(listHdbhNbFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdbhNbMasterRes hdbhNbMasterRes = listHdbhNbFast.get(0);

				RobotSyncHdbhNbReq dataRequestRobot = dataRequestRobot(hdbhNbMasterRes);
				String message = new Gson().toJson(dataRequestRobot);

				WorkProcess workProcessInsert = new WorkProcess();
				workProcessInsert.setServiceId(RobotServiceType.XUAT_NOI_BO_GOC.value());
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
		logger.info("Reset Mode Hoa don noi bo !");
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
		logger.info("Check modify Hoa don noi bo !");

		// Lay fastId(stt_rec)
		String[] temp = fastId.split("_");
		fastId = temp[0];

		List<HdbhNbMasterRes> listHdbhNbModify = new ArrayList<HdbhNbMasterRes>();

		TableDataInfo rspData = new TableDataInfo();
		rspData.setRows(listHdbhNbModify);

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

		ApiGetDataHdbhNbRes result = getHdbhNbData(apiRequest);

		// Kiem tra ton tai so hoa don tu API, neu khong ton tai thi bao loi
		if (result == null || result.getData() == null) {
			throw new Exception("Không tìm thấy số hoá đơn trên hệ thống Fast!");
		}

		// Du lieu API tra ve
		List<HdbhNbMasterRes> listHdbhNbFast = result.getData();
		HdbhNbMasterRes hdbhNbFast = listHdbhNbFast.get(0);

		// Du lieu lay tu table workprocess
		HdbhNbMasterRes hdbhNbIrbot = readDataRequest(workProcessExist);

		// Truong hop thay doi gia
		if (CollectionUtils.isNotEmpty(hdbhNbFast.getDetails())
				&& CollectionUtils.isNotEmpty(hdbhNbIrbot.getDetails())) {

			// Kiem tra so luong mat hang chi tiet
			if (hdbhNbFast.getDetails().size() != hdbhNbIrbot.getDetails().size() || hdbhNbFast.getDetails().size() == 0
					|| hdbhNbIrbot.getDetails().size() == 0) {
				throw new Exception(
						"Tổng số lượng mặt hàng của Fast và IRBot tại thông tin chi tiết không trùng khớp!");
			}

			// Kiem tra dieu chinh gia tang
			HdbhNbMasterRes listHdbhNbMasterPriceIncrease = getListHdbhNbDetailPriceIncrease(hdbhNbFast, hdbhNbIrbot);
			if (listHdbhNbMasterPriceIncrease.getDetails().size() > 0) {
				listHdbhNbModify.add(listHdbhNbMasterPriceIncrease);
			}

			// Kiem tra dieu chinh gia giam
			HdbhNbMasterRes getListHdbhNbMasterPriceReduce = getListHdbhNbDetailPriceReduce(hdbhNbFast, hdbhNbIrbot);
			if (getListHdbhNbMasterPriceReduce.getDetails().size() > 0) {
				listHdbhNbModify.add(getListHdbhNbMasterPriceReduce);
			}
		}

		rspData.setCode(0);
		rspData.setRows(listHdbhNbModify);
		rspData.setTotal(listHdbhNbModify.size());

		return rspData;
	}

	/**
	 * 
	 * @param workProcessExist
	 * @return HdbhNbMasterRes
	 */
	private HdbhNbMasterRes readDataRequest(WorkProcess workProcessExist) {
		String dataRequest = workProcessExist.getDataRequest();
		HdbhNbMasterRes hdbhNbMasterRes = new HdbhNbMasterRes();
		try {
			RobotSyncHdbhNbReq robotSyncHdbhNbReq = new Gson().fromJson(dataRequest, RobotSyncHdbhNbReq.class);
			hdbhNbMasterRes.setFastId(robotSyncHdbhNbReq.getFastId());
			hdbhNbMasterRes.setFastInvoiceNo(robotSyncHdbhNbReq.getFastInvoiceNo());
			hdbhNbMasterRes.seteInvoiceNo(workProcessExist.getData1());
			hdbhNbMasterRes.setDetails(robotSyncHdbhNbReq.getProcessOrder());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hdbhNbMasterRes;
	}

	/**
	 * Kiem tra dieu chinh tang
	 * 
	 * @param hdbhNbFast
	 * @param hdbhNbIrbot
	 * @return HdbhNbMasterRes
	 */
	private HdbhNbMasterRes getListHdbhNbDetailPriceIncrease(HdbhNbMasterRes hdbhNbFast, HdbhNbMasterRes hdbhNbIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdbhNbFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_TANG.value());

		HdbhNbMasterRes hdbhNbMasterIncrease = new HdbhNbMasterRes();
		hdbhNbMasterIncrease.setMode(String.valueOf(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_TANG.value()));
		hdbhNbMasterIncrease.setFastId(fastId);
		hdbhNbMasterIncrease.setFastInvoiceNo(hdbhNbFast.getFastInvoiceNo());
		hdbhNbMasterIncrease.setUnitCode(hdbhNbFast.getUnitCode());
		hdbhNbMasterIncrease.setCustomerId(hdbhNbFast.getCustomerId());
		hdbhNbMasterIncrease.setBuyerName(hdbhNbFast.getBuyerName());
		hdbhNbMasterIncrease.setCustomerName(hdbhNbFast.getCustomerName());
		hdbhNbMasterIncrease.setCustomerTaxCode(hdbhNbFast.getCustomerTaxCode());
		hdbhNbMasterIncrease.setAddress(hdbhNbFast.getAddress());
		hdbhNbMasterIncrease.setInvoiceDate(hdbhNbFast.getInvoiceDate());
		hdbhNbMasterIncrease.setInvoiceModifyDate(now);
		hdbhNbMasterIncrease.setDescription(hdbhNbFast.getDescription());
		hdbhNbMasterIncrease.setTransporter(hdbhNbFast.getTransporter());
		hdbhNbMasterIncrease.setVehicle(hdbhNbFast.getVehicle());
		hdbhNbMasterIncrease.setStockIn(hdbhNbFast.getStockIn());
		hdbhNbMasterIncrease.setStockInName(hdbhNbFast.getStockInName());
		hdbhNbMasterIncrease.setOrderNo(hdbhNbFast.getOrderNo());
		hdbhNbMasterIncrease.setOrderDate(hdbhNbFast.getOrderDate());
		hdbhNbMasterIncrease.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdbhNbDetailRes> listHdbhNbDetailIncrease = new ArrayList<HdbhNbDetailRes>();

		for (HdbhNbDetailRes hdbhNbDetailIrbot : hdbhNbIrbot.getDetails()) {
			for (HdbhNbDetailRes hdbhNbDetailFast : hdbhNbFast.getDetails()) {
				if (hdbhNbDetailIrbot.getFastDetailId().equals(hdbhNbDetailFast.getFastDetailId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productSubTotalAmountFast(API) > productSubTotalAmountIrbot (TANG)
					// return -1: productSubTotalAmountFast(API) < productSubTotalAmountIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == 1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdbhNbDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdbhNbDetailRes hdbhNbDetailNew = new HdbhNbDetailRes();
						hdbhNbDetailNew.setFastId(hdbhNbDetailFast.getFastId());
						hdbhNbDetailNew.setFastDetailId(hdbhNbDetailFast.getFastDetailId());
						hdbhNbDetailNew.setProductId(hdbhNbDetailFast.getProductId());
						hdbhNbDetailNew.setProductName(hdbhNbDetailFast.getProductName());
						hdbhNbDetailNew.setProductUnit(hdbhNbDetailFast.getProductUnit());
						hdbhNbDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdbhNbDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdbhNbDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdbhNbDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdbhNbDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdbhNbDetailNew.setProductExpiredDate(hdbhNbDetailFast.getProductExpiredDate());
						hdbhNbDetailNew.setStockOut(hdbhNbDetailFast.getStockOut());
						hdbhNbDetailNew.setStockOutName(hdbhNbDetailFast.getStockOutName());
						hdbhNbDetailNew.setLotNo(hdbhNbDetailFast.getLotNo());
						listHdbhNbDetailIncrease.add(hdbhNbDetailNew);
					}
					break;
				}
			}
		}
		hdbhNbMasterIncrease.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdbhNbMasterIncrease.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdbhNbMasterIncrease.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdbhNbMasterIncrease.setTotalAmount(String.format("%.2f", totalAmount));
		hdbhNbMasterIncrease.setDetails(listHdbhNbDetailIncrease);

		return hdbhNbMasterIncrease;
	}

	/**
	 * * Kiem tra dieu chinh giam
	 * 
	 * @param hdbhNbFast
	 * @param hdbhNbIrbot
	 * @return HdbhNbMasterRes
	 */
	private HdbhNbMasterRes getListHdbhNbDetailPriceReduce(HdbhNbMasterRes hdbhNbFast, HdbhNbMasterRes hdbhNbIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdbhNbFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_GIAM.value());

		HdbhNbMasterRes hdbhNbMasterReduce = new HdbhNbMasterRes();
		hdbhNbMasterReduce.setMode(String.valueOf(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_GIAM.value()));
		hdbhNbMasterReduce.setFastId(fastId);
		hdbhNbMasterReduce.setFastInvoiceNo(hdbhNbFast.getFastInvoiceNo());
		hdbhNbMasterReduce.setUnitCode(hdbhNbFast.getUnitCode());
		hdbhNbMasterReduce.setCustomerId(hdbhNbFast.getCustomerId());
		hdbhNbMasterReduce.setBuyerName(hdbhNbFast.getBuyerName());
		hdbhNbMasterReduce.setCustomerName(hdbhNbFast.getCustomerName());
		hdbhNbMasterReduce.setCustomerTaxCode(hdbhNbFast.getCustomerTaxCode());
		hdbhNbMasterReduce.setAddress(hdbhNbFast.getAddress());
		hdbhNbMasterReduce.setInvoiceDate(hdbhNbFast.getInvoiceDate());
		hdbhNbMasterReduce.setInvoiceModifyDate(now);
		hdbhNbMasterReduce.setDescription(hdbhNbFast.getDescription());
		hdbhNbMasterReduce.setTransporter(hdbhNbFast.getTransporter());
		hdbhNbMasterReduce.setVehicle(hdbhNbFast.getVehicle());
		hdbhNbMasterReduce.setStockIn(hdbhNbFast.getStockIn());
		hdbhNbMasterReduce.setStockInName(hdbhNbFast.getStockInName());
		hdbhNbMasterReduce.setOrderNo(hdbhNbFast.getOrderNo());
		hdbhNbMasterReduce.setOrderDate(hdbhNbFast.getOrderDate());
		hdbhNbMasterReduce.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdbhNbDetailRes> listHdbhNbDetailReduce = new ArrayList<HdbhNbDetailRes>();

		for (HdbhNbDetailRes hdbhNbDetailIrbot : hdbhNbIrbot.getDetails()) {
			for (HdbhNbDetailRes hdbhNbDetailFast : hdbhNbFast.getDetails()) {
				if (hdbhNbDetailIrbot.getFastDetailId().equals(hdbhNbDetailFast.getFastDetailId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productSubTotalAmountFast(API) > productSubTotalAmountIrbot (TANG)
					// return -1: productSubTotalAmountFast(API) < productSubTotalAmountIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == -1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdbhNbDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhNbDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdbhNbDetailRes hdbhNbDetailNew = new HdbhNbDetailRes();
						hdbhNbDetailNew.setFastId(hdbhNbDetailFast.getFastId());
						hdbhNbDetailNew.setFastDetailId(hdbhNbDetailFast.getFastDetailId());
						hdbhNbDetailNew.setProductId(hdbhNbDetailFast.getProductId());
						hdbhNbDetailNew.setProductName(hdbhNbDetailFast.getProductName());
						hdbhNbDetailNew.setProductUnit(hdbhNbDetailFast.getProductUnit());
						hdbhNbDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdbhNbDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdbhNbDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdbhNbDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdbhNbDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdbhNbDetailNew.setProductExpiredDate(hdbhNbDetailFast.getProductExpiredDate());
						hdbhNbDetailNew.setStockOut(hdbhNbDetailFast.getStockOut());
						hdbhNbDetailNew.setStockOutName(hdbhNbDetailFast.getStockOutName());
						hdbhNbDetailNew.setLotNo(hdbhNbDetailFast.getLotNo());
						listHdbhNbDetailReduce.add(hdbhNbDetailNew);
					}
					break;
				}
			}
		}
		hdbhNbMasterReduce.setTotalQuantity(String.format("%.2f", totalAmount));
		hdbhNbMasterReduce.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdbhNbMasterReduce.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdbhNbMasterReduce.setTotalAmount(String.format("%.2f", totalAmount));
		hdbhNbMasterReduce.setDetails(listHdbhNbDetailReduce);

		return hdbhNbMasterReduce;
	}

	@Override
	public void confirmModify(String fastIds) throws Exception {
		logger.info("Xac nhan dieu chinh Hoa don noi bo !");
		WorkProcess workProcessExist = new WorkProcess();
		String fastArr[] = Convert.toStrArray(fastIds);
		for (String id : fastArr) {

			// Lay fastId(stt_rec), mode
			String[] temp = id.split("_");
			String fastId = temp[0];
			String invoiceMode = temp[1];

			HdbhNbMasterRes hdbhNbModify = new HdbhNbMasterRes();

			// Call API de lay thong tin theo fastId(stt_rec)
			ApiGetDataReq apiRequest = new ApiGetDataReq();
			apiRequest.setFastId(fastId);
			apiRequest.setIsSelectDetail(true);

			ApiGetDataHdbhNbRes result = getHdbhNbData(apiRequest);

			// Du lieu API tra ve
			List<HdbhNbMasterRes> listHdbhNbFast = result.getData();
			HdbhNbMasterRes hdbhNbFast = listHdbhNbFast.get(0);

			// Du lieu lay tu table workprocess
			workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(fastId);
			HdbhNbMasterRes hdbhNbIrbot = readDataRequest(workProcessExist);

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_TANG.value()) {
				hdbhNbModify = getListHdbhNbDetailPriceIncrease(hdbhNbFast, hdbhNbIrbot);
			}

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_GIAM.value()) {
				hdbhNbModify = getListHdbhNbDetailPriceReduce(hdbhNbFast, hdbhNbIrbot);
			}

			if (hdbhNbModify != null) {
				HdbhNb hdbhNbInsert = new HdbhNb();
				Date now = DateUtils.addSeconds(new Date(), -30);
				// Insert hdbhNb_modify
				hdbhNbInsert.setMode(Integer.parseInt(invoiceMode));
				hdbhNbInsert.setFastId(id);
				hdbhNbInsert.setFastInvoiceNo(hdbhNbModify.getFastInvoiceNo());
				hdbhNbInsert.setUnitCode(hdbhNbModify.getUnitCode());
				hdbhNbInsert.setCustomerId(hdbhNbModify.getCustomerId());
				hdbhNbInsert.setBuyerName(hdbhNbModify.getBuyerName());
				hdbhNbInsert.setCustomerName(hdbhNbModify.getCustomerName());
				hdbhNbInsert.setCustomerTaxCode(hdbhNbModify.getCustomerTaxCode());
				hdbhNbInsert.setAddress(hdbhNbModify.getAddress());
				hdbhNbInsert.setInvoiceDate(hdbhNbModify.getInvoiceDate());
				hdbhNbInsert.setInvoiceModifyDate(hdbhNbModify.getInvoiceModifyDate());
				hdbhNbInsert.setDescription(hdbhNbModify.getDescription());
				hdbhNbInsert.setTotalQuantity(hdbhNbModify.getTotalQuantity());
				hdbhNbInsert.setTotalTaxAmount(hdbhNbModify.getTotalTaxAmount());
				hdbhNbInsert.setSubtotalAmount(hdbhNbModify.getSubtotalAmount());
				hdbhNbInsert.setTotalAmount(hdbhNbModify.getTotalAmount());
				hdbhNbInsert.setTransporter(hdbhNbModify.getTransporter());
				hdbhNbInsert.setVehicle(hdbhNbModify.getVehicle());
				hdbhNbInsert.setStockIn(hdbhNbModify.getStockIn());
				hdbhNbInsert.setStockInName(hdbhNbModify.getStockInName());
				hdbhNbInsert.setOrderNo(hdbhNbModify.getOrderNo());
				hdbhNbInsert.setOrderDate(hdbhNbInsert.getOrderDate());
				hdbhNbInsert.setStatus(ProcessStatus.NOTSEND.value());
				hdbhNbInsert.setCreateTime(now);
				hdbhNbModifyMapper.insertHdbhNbModify(hdbhNbInsert);

				// Insert detail data
				if (hdbhNbModify.getDetails() != null) {
					for (HdbhNbDetailRes detail : hdbhNbModify.getDetails()) {
						HdbhNbDetail hdbhNbDetailInsert = new HdbhNbDetail();
						hdbhNbDetailInsert.setHdbhNbId(hdbhNbInsert.getId());
						hdbhNbDetailInsert.setFastId(hdbhNbInsert.getFastId());
						hdbhNbDetailInsert.setFastDetailId(detail.getFastDetailId());
						hdbhNbDetailInsert.setProductId(detail.getProductId());
						hdbhNbDetailInsert.setProductUnit(detail.getProductUnit());
						hdbhNbDetailInsert.setProductName(detail.getProductName());
						hdbhNbDetailInsert.setProductQuantity(detail.getProductQuantity());
						hdbhNbDetailInsert.setProductPrice(detail.getProductPrice());
						hdbhNbDetailInsert.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdbhNbDetailInsert.setProductTaxAmount(detail.getProductTaxAmount());
						hdbhNbDetailInsert.setProductTotalAmount(detail.getProductTotalAmount());
						hdbhNbDetailInsert.setProductExpiredDate(detail.getProductExpiredDate());
						hdbhNbDetailInsert.setStockOut(detail.getStockOut());
						hdbhNbDetailInsert.setStockOutName(detail.getStockOutName());
						hdbhNbDetailInsert.setLotNo(detail.getLotNo());
						hdbhNbDetailInsert.setCreateTime(now);
						hdbhNbModifyDetailMapper.insertHdbhNbModifyDetail(hdbhNbDetailInsert);
					}
				}
			}
		}
		// Set Mode da dieu chinh
		workProcessExist.setData2(String.valueOf(ProcessMode.DA_DIEU_CHINH.value()));
		workProcessMapper.updateWorkProcessSyncKey(workProcessExist);
	}
}