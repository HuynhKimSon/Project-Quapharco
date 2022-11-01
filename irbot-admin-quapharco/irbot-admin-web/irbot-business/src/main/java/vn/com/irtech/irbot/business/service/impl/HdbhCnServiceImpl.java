package vn.com.irtech.irbot.business.service.impl;

import java.math.BigDecimal;
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

import vn.com.irtech.core.common.page.TableDataInfo;
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
import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhCnRes;
import vn.com.irtech.irbot.business.dto.response.HdbhCnDetailRes;
import vn.com.irtech.irbot.business.dto.response.HdbhCnMasterRes;
import vn.com.irtech.irbot.business.mapper.HdbhCnModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdbhCnModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IApiGatewayService;
import vn.com.irtech.irbot.business.service.IHdbhCnService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdbhCnServiceImpl implements IHdbhCnService {

	private static final Logger logger = LoggerFactory.getLogger(HdbhServiceImpl.class);

	@Autowired
	private IApiGatewayService apiGatewayService;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private HdbhCnModifyMapper hdbhCnModifyMapper;

	@Autowired
	private HdbhCnModifyDetailMapper hdbhCnModifyDetailMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HdbhCnMasterRes selectHdbhCnById(String fastId) throws Exception {
		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setFastId(fastId);
		apiRequest.setIsSelectDetail(true);

		ApiGetDataHdbhCnRes result = getHdbhCnData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdbhCnMasterRes> listHdbhCnFast = result.getData();

		if (CollectionUtils.isEmpty(listHdbhCnFast)) {
			throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
		}

		HdbhCnMasterRes hdbhCn = listHdbhCnFast.get(0);

		WorkProcess workProcessSelect = new WorkProcess();
		String syncKey = hdbhCn.getFastId();
		workProcessSelect.setSyncKey(syncKey);

		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {
			hdbhCn.setProcessStatus(listWorkProcessExist.get(0).getStatus());
		}

		return hdbhCn;
	}

	@Override
	public TableDataInfo selectHdbhCnList(HdbhCn hdbhCn) throws Exception {

		if (hdbhCn.getParams() == null) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		Object beginTimeObj = hdbhCn.getParams().get("beginTime");
		Object endTimeObj = hdbhCn.getParams().get("endTime");
		Object fastInvoiceNoObj = hdbhCn.getParams().get("fastInvoiceNo");

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

		ApiGetDataHdbhCnRes result = getHdbhCnData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdbhCnMasterRes> listHdbhCnFast = result.getData();
		logger.info(">> So hoa don lay ve hien thi: " + listHdbhCnFast.size());

		if (CollectionUtils.isNotEmpty(listHdbhCnFast)) {
			List<HdbhCnMasterRes> removeItems = new ArrayList<HdbhCnMasterRes>();
			Boolean searchByStatus = (hdbhCn.getStatus() != null && hdbhCn.getStatus() > 0);

			for (HdbhCnMasterRes hdbhCnMasterRes : listHdbhCnFast) {
				WorkProcess workProcessSelect = new WorkProcess();
				String syncKey = hdbhCnMasterRes.getFastId();

				workProcessSelect.setSyncKey(syncKey);
				workProcessSelect.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());

				// Set theo trang thai fillter
				if (searchByStatus) {
					workProcessSelect.setStatus(hdbhCn.getStatus());
				}

				List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);
				if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {

					// Add hoa don da ton tai trong work process vao list removeItems
					if (hdbhCn.getStatus() == ProcessStatus.NOTSEND.value()) {
						removeItems.add(hdbhCnMasterRes);
					} else {
						hdbhCnMasterRes.setProcessStatus(listWorkProcessExist.get(0).getStatus());
					}

					// View fast e-invoiceNo neu trang thai thanh cong
					if (hdbhCnMasterRes.getProcessStatus() == ProcessStatus.SUCCESS.value()) {
						hdbhCnMasterRes.seteInvoiceNo(listWorkProcessExist.get(0).getData1());
					}

					// Set trang thai goc/ da chinh sua
					if (listWorkProcessExist.get(0).getData2() != null) {
						hdbhCnMasterRes.setMode(listWorkProcessExist.get(0).getData2());
					}

				} else if (searchByStatus && hdbhCn.getStatus() > 0) {
					removeItems.add(hdbhCnMasterRes);
				}
			}

			// Remove tat ca item khong du dieu kien trong list view
			if (CollectionUtils.isNotEmpty(removeItems)) {
				listHdbhCnFast.removeAll(removeItems);
			}
		}

		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(listHdbhCnFast);
		rspData.setTotal(listHdbhCnFast.size());

		return rspData;
	}

	private ApiGetDataHdbhCnRes getHdbhCnData(ApiGetDataReq request) throws Exception {
		ApiGetDataHdbhCnRes hdbhCnList = apiGatewayService.getHdbhChiNhanh(request);
		return hdbhCnList;
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

			Map<RobotSyncHdbhCnReq, Robot> robotRequestMap = new HashMap<RobotSyncHdbhCnReq, Robot>();

			String[] idArr = Convert.toStrArray(ids);
			Date now = new Date();
			for (String fastId : idArr) {

				// Call Api lay du lieu theo FastId
				ApiGetDataReq apiRequest = new ApiGetDataReq();
				apiRequest.setFastId(fastId);
				apiRequest.setIsSelectDetail(true);

				ApiGetDataHdbhCnRes result = getHdbhCnData(apiRequest);

				if (result == null || result.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdbhCnMasterRes> listHdbhCnFast = result.getData();

				if (CollectionUtils.isEmpty(listHdbhCnFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdbhCnMasterRes hdbhCnMasterRes = listHdbhCnFast.get(0);

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
				RobotSyncHdbhCnReq dataRequestRobot = dataRequestRobot(hdbhCnMasterRes);

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

	private RobotSyncHdbhCnReq dataRequestRobot(HdbhCnMasterRes hdbhCnMasterRes) {

		RobotSyncHdbhCnReq dataRequest = new RobotSyncHdbhCnReq();

		dataRequest.setConfig(getProcessConfig(hdbhCnMasterRes.getUnitCode()));
		dataRequest.setSyncId(0);
		dataRequest.setServiceType(InvoiceMode.HOA_DON_BAN_HANG_CN_GOC.value());
		dataRequest.setServiceName(InvoiceMode.HOA_DON_BAN_HANG_CN_GOC.title());
		dataRequest.setFastId(hdbhCnMasterRes.getFastId());
		dataRequest.setFastInvoiceNo(hdbhCnMasterRes.getFastInvoiceNo());
		dataRequest.setUnitCode(hdbhCnMasterRes.getUnitCode());
		dataRequest.setCustomerId(hdbhCnMasterRes.getCustomerId());
		dataRequest.setCustomerName(hdbhCnMasterRes.getCustomerName());
		dataRequest.setCustomerTaxCode(hdbhCnMasterRes.getCustomerTaxCode());
		dataRequest.setAddress(hdbhCnMasterRes.getAddress());
		dataRequest.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdbhCnMasterRes.getInvoiceDate()));
		dataRequest.setDescription(hdbhCnMasterRes.getDescription());
		dataRequest.setPaymentMethod(hdbhCnMasterRes.getPaymentMethod());
		dataRequest.setTotalTaxAmount(hdbhCnMasterRes.getTotalTaxAmount());
		dataRequest.setSubtotalAmount(hdbhCnMasterRes.getSubtotalAmount());
		dataRequest.setTotalAmount(hdbhCnMasterRes.getTotalAmount());
		dataRequest.setOrderQuantity(hdbhCnMasterRes.getDetails().size());
		dataRequest.setProcessOrder(hdbhCnMasterRes.getDetails());

		return dataRequest;
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
		logger.info("Reset Status Hoa don ban hang chi nhanh !");
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

				ApiGetDataHdbhCnRes apiGetDataHdbhCnRes = getHdbhCnData(apiRequest);

				if (apiGetDataHdbhCnRes == null || apiGetDataHdbhCnRes.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdbhCnMasterRes> listHdbhCnFast = apiGetDataHdbhCnRes.getData();

				if (CollectionUtils.isEmpty(listHdbhCnFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdbhCnMasterRes hdbhCnMasterRes = listHdbhCnFast.get(0);

				RobotSyncHdbhCnReq dataRequestRobot = dataRequestRobot(hdbhCnMasterRes);
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
		logger.info("Reset Mode Hoa don ban hang chi nhanh !");
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
		logger.info("Check modify Hoa don ban hang chinh nhanh !");

		// Lay fastId(stt_rec)
		String[] temp = fastId.split("_");
		fastId = temp[0];

		List<HdbhCnMasterRes> listHdbhCnModify = new ArrayList<HdbhCnMasterRes>();

		TableDataInfo rspData = new TableDataInfo();
		rspData.setRows(listHdbhCnModify);

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

		ApiGetDataHdbhCnRes result = getHdbhCnData(apiRequest);

		// Kiem tra ton tai so hoa don tu API, neu khong ton tai thi bao loi
		if (result == null || result.getData() == null) {
			throw new Exception("Không tìm thấy số hoá đơn trên hệ thống Fast!");
		}

		// Du lieu API tra ve
		List<HdbhCnMasterRes> listHdbhCnFast = result.getData();
		HdbhCnMasterRes hdbhCnFast = listHdbhCnFast.get(0);

		// Du lieu lay tu table workprocess
		HdbhCnMasterRes hdbhCnIrbot = readDataRequest(workProcessExist);

		// Truong hop thay doi gia
		if (CollectionUtils.isNotEmpty(hdbhCnFast.getDetails())
				&& CollectionUtils.isNotEmpty(hdbhCnIrbot.getDetails())) {

			// Kiem tra so luong mat hang chi tiet
			if (hdbhCnFast.getDetails().size() != hdbhCnIrbot.getDetails().size() || hdbhCnFast.getDetails().size() == 0
					|| hdbhCnIrbot.getDetails().size() == 0) {
				throw new Exception(
						"Tổng số lượng mặt hàng của Fast và IRBot tại thông tin chi tiết không trùng khớp!");
			}

			// Kiem tra dieu chinh gia tang
			HdbhCnMasterRes listHdbhCnMasterPriceIncrease = getListHdbhCnDetailPriceIncrease(hdbhCnFast, hdbhCnIrbot);
			if (listHdbhCnMasterPriceIncrease.getDetails().size() > 0) {
				listHdbhCnModify.add(listHdbhCnMasterPriceIncrease);
			}

			// Kiem tra dieu chinh gia giam
			HdbhCnMasterRes getListHdbhCnMasterPriceReduce = getListHdbhCnDetailPriceReduce(hdbhCnFast, hdbhCnIrbot);
			if (getListHdbhCnMasterPriceReduce.getDetails().size() > 0) {
				listHdbhCnModify.add(getListHdbhCnMasterPriceReduce);
			}
		}

		rspData.setCode(0);
		rspData.setRows(listHdbhCnModify);
		rspData.setTotal(listHdbhCnModify.size());

		return rspData;
	}

	/**
	 * 
	 * @param workProcessExist
	 * @return HdbhCnMasterRes
	 */
	private HdbhCnMasterRes readDataRequest(WorkProcess workProcessExist) {
		String dataRequest = workProcessExist.getDataRequest();
		HdbhCnMasterRes hdbhCnMasterRes = new HdbhCnMasterRes();
		try {
			RobotSyncHdbhCnReq robotSyncHdbhCnReq = new Gson().fromJson(dataRequest, RobotSyncHdbhCnReq.class);
			hdbhCnMasterRes.setFastId(robotSyncHdbhCnReq.getFastId());
			hdbhCnMasterRes.setFastInvoiceNo(robotSyncHdbhCnReq.getFastInvoiceNo());
			hdbhCnMasterRes.seteInvoiceNo(workProcessExist.getData1());
			hdbhCnMasterRes.setDetails(robotSyncHdbhCnReq.getProcessOrder());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hdbhCnMasterRes;
	}

	/**
	 * Kiem tra dieu chinh tang
	 * 
	 * @param hdbhCnFast
	 * @param hdbhCnIrbot
	 * @return HdbhCnMasterRes
	 */
	private HdbhCnMasterRes getListHdbhCnDetailPriceIncrease(HdbhCnMasterRes hdbhCnFast, HdbhCnMasterRes hdbhCnIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdbhCnFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG.value());

		HdbhCnMasterRes hdbhCnMasterIncrease = new HdbhCnMasterRes();
		hdbhCnMasterIncrease.setMode(String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG.value()));
		hdbhCnMasterIncrease.setFastId(fastId);
		hdbhCnMasterIncrease.setFastInvoiceNo(hdbhCnFast.getFastInvoiceNo());
		hdbhCnMasterIncrease.setUnitCode(hdbhCnFast.getUnitCode());
		hdbhCnMasterIncrease.setCustomerId(hdbhCnFast.getCustomerId());
		hdbhCnMasterIncrease.setBuyerName(hdbhCnFast.getBuyerName());
		hdbhCnMasterIncrease.setCustomerName(hdbhCnFast.getCustomerName());
		hdbhCnMasterIncrease.setCustomerTaxCode(hdbhCnFast.getCustomerTaxCode());
		hdbhCnMasterIncrease.setAddress(hdbhCnFast.getAddress());
		hdbhCnMasterIncrease.setInvoiceDate(hdbhCnFast.getInvoiceDate());
		hdbhCnMasterIncrease.setInvoiceModifyDate(now);
		hdbhCnMasterIncrease.setDescription(hdbhCnFast.getDescription());
		hdbhCnMasterIncrease.setPaymentMethod(hdbhCnFast.getPaymentMethod());
		hdbhCnMasterIncrease.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdbhCnDetailRes> listHdbhCnDetailIncrease = new ArrayList<HdbhCnDetailRes>();

		for (HdbhCnDetailRes hdbhCnDetailIrbot : hdbhCnIrbot.getDetails()) {
			for (HdbhCnDetailRes hdbhCnDetailFast : hdbhCnFast.getDetails()) {
				if (hdbhCnDetailIrbot.getProductId().equals(hdbhCnDetailFast.getProductId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productPriceFast(API) > productPriceIrbot (TANG)
					// return -1: productPriceFast(API) < productPriceIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == 1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdbhCnDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdbhCnDetailRes hdbhCnDetailNew = new HdbhCnDetailRes();
						hdbhCnDetailNew.setFastId(hdbhCnDetailFast.getFastId());
						hdbhCnDetailNew.setFastDetailId(hdbhCnDetailFast.getFastDetailId());
						hdbhCnDetailNew.setProductId(hdbhCnDetailFast.getProductId());
						hdbhCnDetailNew.setProductName(hdbhCnDetailFast.getProductName());
						hdbhCnDetailNew.setProductUnit(hdbhCnDetailFast.getProductUnit());
						hdbhCnDetailNew.setProductTaxId(hdbhCnDetailFast.getProductTaxId());
						hdbhCnDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdbhCnDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdbhCnDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdbhCnDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdbhCnDetailNew.setProductDiscount(hdbhCnDetailFast.getProductDiscount());
						hdbhCnDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdbhCnDetailNew.setProductExpiredDate(hdbhCnDetailFast.getProductExpiredDate());
						hdbhCnDetailNew.setStockOut(hdbhCnDetailFast.getStockOut());
						hdbhCnDetailNew.setStockOutName(hdbhCnDetailFast.getStockOutName());
						hdbhCnDetailNew.setLotNo(hdbhCnDetailFast.getLotNo());
						listHdbhCnDetailIncrease.add(hdbhCnDetailNew);
					}
					break;
				}
			}
		}
		hdbhCnMasterIncrease.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdbhCnMasterIncrease.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdbhCnMasterIncrease.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdbhCnMasterIncrease.setTotalAmount(String.format("%.2f", totalAmount));
		hdbhCnMasterIncrease.setDetails(listHdbhCnDetailIncrease);

		return hdbhCnMasterIncrease;
	}

	/**
	 * * Kiem tra dieu chinh giam
	 * 
	 * @param hdbhCnFast
	 * @param hdbhCnIrbot
	 * @return HdbhCnMasterRes
	 */
	private HdbhCnMasterRes getListHdbhCnDetailPriceReduce(HdbhCnMasterRes hdbhCnFast, HdbhCnMasterRes hdbhCnIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdbhCnFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM.value());

		HdbhCnMasterRes hdbhMasterReduce = new HdbhCnMasterRes();
		hdbhMasterReduce.setMode(String.valueOf(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM.value()));
		hdbhMasterReduce.setFastId(fastId);
		hdbhMasterReduce.setFastInvoiceNo(hdbhCnFast.getFastInvoiceNo());
		hdbhMasterReduce.setUnitCode(hdbhCnFast.getUnitCode());
		hdbhMasterReduce.setCustomerId(hdbhCnFast.getCustomerId());
		hdbhMasterReduce.setBuyerName(hdbhCnFast.getBuyerName());
		hdbhMasterReduce.setCustomerName(hdbhCnFast.getCustomerName());
		hdbhMasterReduce.setCustomerTaxCode(hdbhCnFast.getCustomerTaxCode());
		hdbhMasterReduce.setAddress(hdbhCnFast.getAddress());
		hdbhMasterReduce.setInvoiceDate(hdbhCnFast.getInvoiceDate());
		hdbhMasterReduce.setInvoiceModifyDate(now);
		hdbhMasterReduce.setDescription(hdbhCnFast.getDescription());
		hdbhMasterReduce.setPaymentMethod(hdbhCnFast.getPaymentMethod());
		hdbhMasterReduce.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdbhCnDetailRes> listHdbhCnDetailIncrease = new ArrayList<HdbhCnDetailRes>();

		for (HdbhCnDetailRes hdbhCnDetailIrbot : hdbhCnIrbot.getDetails()) {
			for (HdbhCnDetailRes hdbhCnDetailFast : hdbhCnFast.getDetails()) {
				if (hdbhCnDetailIrbot.getProductId().equals(hdbhCnDetailFast.getProductId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productPriceFast(API) > productPriceIrbot (TANG)
					// return -1: productPriceFast(API) < productPriceIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == -1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdbhCnDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdbhCnDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);
						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdbhCnDetailRes hdbhCnDetailNew = new HdbhCnDetailRes();
						hdbhCnDetailNew.setFastId(hdbhCnDetailFast.getFastId());
						hdbhCnDetailNew.setFastDetailId(hdbhCnDetailFast.getFastDetailId());
						hdbhCnDetailNew.setProductId(hdbhCnDetailFast.getProductId());
						hdbhCnDetailNew.setProductName(hdbhCnDetailFast.getProductName());
						hdbhCnDetailNew.setProductUnit(hdbhCnDetailFast.getProductUnit());
						hdbhCnDetailNew.setProductTaxId(hdbhCnDetailFast.getProductTaxId());
						hdbhCnDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdbhCnDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdbhCnDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdbhCnDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdbhCnDetailNew.setProductDiscount(hdbhCnDetailFast.getProductDiscount());
						hdbhCnDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdbhCnDetailNew.setProductExpiredDate(hdbhCnDetailFast.getProductExpiredDate());
						hdbhCnDetailNew.setStockOut(hdbhCnDetailFast.getStockOut());
						hdbhCnDetailNew.setStockOutName(hdbhCnDetailFast.getStockOutName());
						hdbhCnDetailNew.setLotNo(hdbhCnDetailFast.getLotNo());
						listHdbhCnDetailIncrease.add(hdbhCnDetailNew);
					}
					break;
				}
			}
		}
		hdbhMasterReduce.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdbhMasterReduce.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdbhMasterReduce.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdbhMasterReduce.setTotalAmount(String.format("%.2f", totalAmount));
		hdbhMasterReduce.setDetails(listHdbhCnDetailIncrease);

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

			HdbhCnMasterRes hdbhCnModify = new HdbhCnMasterRes();

			// Call API de lay thong tin theo fastId(stt_rec)
			ApiGetDataReq apiRequest = new ApiGetDataReq();
			apiRequest.setFastId(fastId);
			apiRequest.setIsSelectDetail(true);

			ApiGetDataHdbhCnRes result = getHdbhCnData(apiRequest);

			// Du lieu API tra ve
			List<HdbhCnMasterRes> listHdbhCnFast = result.getData();
			HdbhCnMasterRes hdbhCnFast = listHdbhCnFast.get(0);

			// Du lieu lay tu table workprocess
			workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(fastId);
			HdbhCnMasterRes hdbhCnIrbot = readDataRequest(workProcessExist);

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG.value()) {
				hdbhCnModify = getListHdbhCnDetailPriceIncrease(hdbhCnFast, hdbhCnIrbot);
			}

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM.value()) {
				hdbhCnModify = getListHdbhCnDetailPriceReduce(hdbhCnFast, hdbhCnIrbot);
			}

			if (hdbhCnModify != null) {
				HdbhCn hdbhCnInsert = new HdbhCn();
				Date now = DateUtils.addSeconds(new Date(), -30);
				// Insert hdbh_modify
				hdbhCnInsert.setMode(Integer.parseInt(invoiceMode));
				hdbhCnInsert.setFastId(id);
				hdbhCnInsert.setFastInvoiceNo(hdbhCnModify.getFastInvoiceNo());
				hdbhCnInsert.setUnitCode(hdbhCnModify.getUnitCode());
				hdbhCnInsert.setCustomerId(hdbhCnModify.getCustomerId());
				hdbhCnInsert.setBuyerName(hdbhCnModify.getBuyerName());
				hdbhCnInsert.setCustomerName(hdbhCnModify.getCustomerName());
				hdbhCnInsert.setCustomerTaxCode(hdbhCnModify.getCustomerTaxCode());
				hdbhCnInsert.setAddress(hdbhCnModify.getAddress());
				hdbhCnInsert.setInvoiceDate(hdbhCnModify.getInvoiceDate());
				hdbhCnInsert.setInvoiceModifyDate(hdbhCnModify.getInvoiceModifyDate());
				hdbhCnInsert.setDescription(hdbhCnModify.getDescription());
				hdbhCnInsert.setTotalQuantity(hdbhCnModify.getTotalQuantity());
				hdbhCnInsert.setTotalTaxAmount(hdbhCnModify.getTotalTaxAmount());
				hdbhCnInsert.setSubtotalAmount(hdbhCnModify.getSubtotalAmount());
				hdbhCnInsert.setTotalAmount(hdbhCnModify.getTotalAmount());
				hdbhCnInsert.setPaymentMethod(hdbhCnModify.getPaymentMethod());
				hdbhCnInsert.setStatus(ProcessStatus.NOTSEND.value());
				hdbhCnInsert.setCreateTime(now);
				hdbhCnModifyMapper.insertHdbhCnModify(hdbhCnInsert);

				// Insert detail data
				if (hdbhCnModify.getDetails() != null) {
					for (HdbhCnDetailRes detail : hdbhCnModify.getDetails()) {
						HdbhCnDetail hdbhCnDetailInsert = new HdbhCnDetail();
						hdbhCnDetailInsert.setHdbhCnId(hdbhCnInsert.getId());
						hdbhCnDetailInsert.setFastId(hdbhCnInsert.getFastId());
						hdbhCnDetailInsert.setFastDetailId(detail.getFastDetailId());
						hdbhCnDetailInsert.setProductId(detail.getProductId());
						hdbhCnDetailInsert.setProductUnit(detail.getProductUnit());
						hdbhCnDetailInsert.setProductName(detail.getProductName());
						hdbhCnDetailInsert.setProductTaxId(detail.getProductTaxId());
						hdbhCnDetailInsert.setProductQuantity(detail.getProductQuantity());
						hdbhCnDetailInsert.setProductPrice(detail.getProductPrice());
						hdbhCnDetailInsert.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdbhCnDetailInsert.setProductTaxAmount(detail.getProductTaxAmount());
						hdbhCnDetailInsert.setProductDiscount(detail.getProductDiscount());
						hdbhCnDetailInsert.setProductTotalAmount(detail.getProductTotalAmount());
						hdbhCnDetailInsert.setProductExpiredDate(detail.getProductExpiredDate());
						hdbhCnDetailInsert.setStockOut(detail.getStockOut());
						hdbhCnDetailInsert.setStockOutName(detail.getStockOutName());
						hdbhCnDetailInsert.setLotNo(detail.getLotNo());
						hdbhCnDetailInsert.setCreateTime(now);
						hdbhCnModifyDetailMapper.insertHdbhCnModifyDetail(hdbhCnDetailInsert);
					}
				}
			}
		}
		// Set Mode da dieu chinh
		workProcessExist.setData2(String.valueOf(ProcessMode.DA_DIEU_CHINH.value()));
		workProcessMapper.updateWorkProcessSyncKey(workProcessExist);
	}
}