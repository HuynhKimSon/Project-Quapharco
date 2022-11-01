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
import vn.com.irtech.irbot.business.domain.Hddv;
import vn.com.irtech.irbot.business.domain.HddvDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHddvReq;
import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHddvRes;
import vn.com.irtech.irbot.business.dto.response.HddvDetailRes;
import vn.com.irtech.irbot.business.dto.response.HddvMasterRes;
import vn.com.irtech.irbot.business.mapper.HddvModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HddvModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IApiGatewayService;
import vn.com.irtech.irbot.business.service.IHddvService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HddvServiceImpl implements IHddvService {

	private static final Logger logger = LoggerFactory.getLogger(HddvServiceImpl.class);

	@Autowired
	private IApiGatewayService apiGatewayService;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private HddvModifyMapper hddvModifyMapper;

	@Autowired
	private HddvModifyDetailMapper hddvModifyDetailMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HddvMasterRes selectHddvById(String fastId) throws Exception {
		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setFastId(fastId);
		apiRequest.setIsSelectDetail(true);

		ApiGetDataHddvRes result = getHddvData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HddvMasterRes> listHddvFast = result.getData();

		if (CollectionUtils.isEmpty(listHddvFast)) {
			throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
		}

		HddvMasterRes hddv = listHddvFast.get(0);

		WorkProcess workProcessSelect = new WorkProcess();
		String syncKey = hddv.getFastId();
		workProcessSelect.setSyncKey(syncKey);

		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {
			hddv.setProcessStatus(listWorkProcessExist.get(0).getStatus());
		}

		return hddv;
	}

	@Override
	public TableDataInfo selectHddvList(Hddv hdhv) throws Exception {

		if (hdhv.getParams() == null) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		Object beginTimeObj = hdhv.getParams().get("beginTime");
		Object endTimeObj = hdhv.getParams().get("endTime");
		Object fastInvoiceNoObj = hdhv.getParams().get("fastInvoiceNo");

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

		ApiGetDataHddvRes result = getHddvData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HddvMasterRes> listHddvFast = result.getData();
		logger.info(">> So hoa don lay ve hien thi: " + listHddvFast.size());

		if (CollectionUtils.isNotEmpty(listHddvFast)) {
			List<HddvMasterRes> removeItems = new ArrayList<HddvMasterRes>();
			Boolean searchByStatus = (hdhv.getStatus() != null && hdhv.getStatus() > 0);

			for (HddvMasterRes hddvMasterRes : listHddvFast) {
				WorkProcess workProcessSelect = new WorkProcess();
				String syncKey = hddvMasterRes.getFastId();

				workProcessSelect.setSyncKey(syncKey);
				workProcessSelect.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());

				// Set theo trang thai fillter
				if (searchByStatus) {
					workProcessSelect.setStatus(hdhv.getStatus());
				}

				List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

				if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {

					// Add hoa don da ton tai trong work process vao list removeItems
					if (hdhv.getStatus() == ProcessStatus.NOTSEND.value()) {
						removeItems.add(hddvMasterRes);
					} else {
						hddvMasterRes.setProcessStatus(listWorkProcessExist.get(0).getStatus());
					}

					// View fast e-invoiceNo neu trang thai thanh cong
					if (hddvMasterRes.getProcessStatus() == ProcessStatus.SUCCESS.value()) {
						hddvMasterRes.seteInvoiceNo(listWorkProcessExist.get(0).getData1());
					}

					// Set trang thai goc/ da chinh sua
					if (listWorkProcessExist.get(0).getData2() != null) {
						hddvMasterRes.setMode(listWorkProcessExist.get(0).getData2());
					}

				} else if (searchByStatus && hdhv.getStatus() > 0) {
					removeItems.add(hddvMasterRes);
				}
			}

			// Remove tat ca item khong du dieu kien trong list view
			if (CollectionUtils.isNotEmpty(removeItems)) {
				listHddvFast.removeAll(removeItems);
			}
		}

		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(listHddvFast);
		rspData.setTotal(listHddvFast.size());

		return rspData;
	}

	private ApiGetDataHddvRes getHddvData(ApiGetDataReq request) throws Exception {

		ApiGetDataHddvRes hddvList = apiGatewayService.getHdDichVu(request);
		return hddvList;
	}

	@Override
	@Transactional
	public void retry(String ids) throws Exception {
		try {
			// Get a robot available
			List<Robot> robots = robotMapper.selectRobotByServices(
					new Integer[] { RobotServiceType.HOA_DON_GTGT_GOC.value() },
					new String[] { RobotStatusType.AVAILABLE.value(), RobotStatusType.BUSY.value() });

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			Map<RobotSyncHddvReq, Robot> robotRequestMap = new HashMap<RobotSyncHddvReq, Robot>();

			String[] idArr = Convert.toStrArray(ids);
			Date now = new Date();
			for (String fastId : idArr) {

				// Call Api lay du lieu theo FastId
				ApiGetDataReq apiRequest = new ApiGetDataReq();
				apiRequest.setFastId(fastId);
				apiRequest.setIsSelectDetail(true);

				ApiGetDataHddvRes result = getHddvData(apiRequest);

				if (result == null || result.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HddvMasterRes> listHddvFast = result.getData();

				if (CollectionUtils.isEmpty(listHddvFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HddvMasterRes hddvMasterRes = listHddvFast.get(0);

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
				RobotSyncHddvReq dataRequestRobot = dataRequestRobot(hddvMasterRes);

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

	private RobotSyncHddvReq dataRequestRobot(HddvMasterRes hddvMasterRes) {

		RobotSyncHddvReq dataRequest = new RobotSyncHddvReq();
		dataRequest.setConfig(getProcessConfig(hddvMasterRes.getUnitCode()));
		dataRequest.setSyncId(0);
		dataRequest.setServiceType(InvoiceMode.HOA_DON_DICH_VU_GOC.value());
		dataRequest.setServiceName(InvoiceMode.HOA_DON_DICH_VU_GOC.title());
		dataRequest.setFastId(hddvMasterRes.getFastId());
		dataRequest.setFastInvoiceNo(hddvMasterRes.getFastInvoiceNo());
		dataRequest.setUnitCode(hddvMasterRes.getUnitCode());
		dataRequest.setCustomerId(hddvMasterRes.getCustomerId());
		dataRequest.setCustomerName(hddvMasterRes.getCustomerName());
		dataRequest.setCustomerTaxCode(hddvMasterRes.getCustomerTaxCode());
		dataRequest.setAddress(hddvMasterRes.getAddress());
		dataRequest.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hddvMasterRes.getInvoiceDate()));
		dataRequest.setDescription(hddvMasterRes.getDescription());
		dataRequest.setTotalTaxAmount(hddvMasterRes.getTotalTaxAmount());
		dataRequest.setSubtotalAmount(hddvMasterRes.getSubtotalAmount());
		dataRequest.setTotalAmount(hddvMasterRes.getTotalAmount());
		dataRequest.setOrderQuantity(hddvMasterRes.getDetails().size());
		dataRequest.setProcessOrder(hddvMasterRes.getDetails());

		return dataRequest;
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
		logger.info("Reset Status Hoa don dich vu !");
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

				ApiGetDataHddvRes apiGetDataHddvRes = getHddvData(apiRequest);

				if (apiGetDataHddvRes == null || apiGetDataHddvRes.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HddvMasterRes> listHddvFast = apiGetDataHddvRes.getData();

				if (CollectionUtils.isEmpty(listHddvFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HddvMasterRes hddvMasterRes = listHddvFast.get(0);

				RobotSyncHddvReq dataRequestRobot = dataRequestRobot(hddvMasterRes);
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
		logger.info("Reset Mode Hoa don dich vu !");
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
	public TableDataInfo checkModify(String fastId) throws Exception {
		logger.info("Check modify Hoa don dich vu !");

		// Lay fastId(stt_rec)
		String[] temp = fastId.split("_");
		fastId = temp[0];

		List<HddvMasterRes> listHddvModify = new ArrayList<HddvMasterRes>();

		TableDataInfo rspData = new TableDataInfo();
		rspData.setRows(listHddvModify);

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

		ApiGetDataHddvRes result = getHddvData(apiRequest);

		// Kiem tra ton tai so hoa don tu API, neu khong ton tai thi bao loi
		if (result == null || result.getData() == null) {
			throw new Exception("Không tìm thấy số hoá đơn trên hệ thống Fast!");
		}

		// Du lieu API tra ve
		List<HddvMasterRes> listHddvFast = result.getData();
		HddvMasterRes hddvFast = listHddvFast.get(0);

		// Du lieu lay tu table workprocess
		HddvMasterRes hddvIrbot = readDataRequest(workProcessExist);

		// Truong hop thay doi gia
		if (CollectionUtils.isNotEmpty(hddvFast.getDetails()) && CollectionUtils.isNotEmpty(hddvIrbot.getDetails())) {
			// Kiem tra so luong mat hang chi tiet
			if (hddvFast.getDetails().size() != hddvIrbot.getDetails().size() || hddvFast.getDetails().size() == 0
					|| hddvIrbot.getDetails().size() == 0) {
				throw new Exception(
						"Tổng số lượng mặt hàng của Fast và IRBot tại thông tin chi tiết không trùng khớp!");
			}

			// Kiem tra dieu chinh gia tang
			HddvMasterRes listHddvMasterPriceIncrease = getListHddvDetailPriceIncrease(hddvFast, hddvIrbot);
			if (listHddvMasterPriceIncrease.getDetails().size() > 0) {
				listHddvModify.add(listHddvMasterPriceIncrease);
			}

			// Kiem tra dieu chinh gia giam
			HddvMasterRes getListHddvMasterPriceReduce = getListHddvDetailPriceReduce(hddvFast, hddvIrbot);
			if (getListHddvMasterPriceReduce.getDetails().size() > 0) {
				listHddvModify.add(getListHddvMasterPriceReduce);
			}
		}

		rspData.setCode(0);
		rspData.setRows(listHddvModify);
		rspData.setTotal(listHddvModify.size());

		return rspData;
	}

	/**
	 * 
	 * @param workProcessExist
	 * @return HddvMasterRes
	 */
	private HddvMasterRes readDataRequest(WorkProcess workProcessExist) {
		String dataRequest = workProcessExist.getDataRequest();
		HddvMasterRes hddvMasterRes = new HddvMasterRes();
		try {
			RobotSyncHddvReq robotSyncHddvReq = new Gson().fromJson(dataRequest, RobotSyncHddvReq.class);
			hddvMasterRes.setFastId(robotSyncHddvReq.getFastId());
			hddvMasterRes.setFastInvoiceNo(robotSyncHddvReq.getFastInvoiceNo());
			hddvMasterRes.seteInvoiceNo(workProcessExist.getData1());
			hddvMasterRes.setDetails(robotSyncHddvReq.getProcessOrder());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hddvMasterRes;
	}

	/**
	 * Kiem tra dieu chinh tang
	 * 
	 * @param hddvFast
	 * @param hddvIrbot
	 * @return HddvMasterRes
	 */
	private HddvMasterRes getListHddvDetailPriceIncrease(HddvMasterRes hddvFast, HddvMasterRes hddvIrbot) {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hddvFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_TANG.value());

		HddvMasterRes hddvMasterIncrease = new HddvMasterRes();
		hddvMasterIncrease.setMode(String.valueOf(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_TANG.value()));
		hddvMasterIncrease.setFastId(fastId);
		hddvMasterIncrease.setFastInvoiceNo(hddvFast.getFastInvoiceNo());
		hddvMasterIncrease.setUnitCode(hddvFast.getUnitCode());
		hddvMasterIncrease.setCustomerId(hddvFast.getCustomerId());
		hddvMasterIncrease.setBuyerName(hddvFast.getBuyerName());
		hddvMasterIncrease.setCustomerName(hddvFast.getCustomerName());
		hddvMasterIncrease.setCustomerTaxCode(hddvFast.getCustomerTaxCode());
		hddvMasterIncrease.setAddress(hddvFast.getAddress());
		hddvMasterIncrease.setInvoiceDate(hddvFast.getInvoiceDate());
		hddvMasterIncrease.setInvoiceModifyDate(now);
		hddvMasterIncrease.setDescription(hddvFast.getDescription());
		hddvMasterIncrease.setPaymentMethod(hddvFast.getPaymentMethod());
		hddvMasterIncrease.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HddvDetailRes> listHddvDetailIncrease = new ArrayList<HddvDetailRes>();

		for (HddvDetailRes hddvDetailIrbot : hddvIrbot.getDetails()) {
			for (HddvDetailRes hddvDetailFast : hddvFast.getDetails()) {
				if (hddvDetailIrbot.getProductName().equals(hddvDetailFast.getProductName())) {

					BigDecimal productSubTotalAmountFast = BigDecimal
							.valueOf(Double.parseDouble(hddvDetailFast.getProductSubtotalAmount()));

					BigDecimal productSubTotalAmountIrbot = BigDecimal
							.valueOf(Double.parseDouble(hddvDetailIrbot.getProductSubtotalAmount()));

					// return 0: equal
					// return 1: productSubTotalAmountFast(API) > productSubTotalAmountIrbot (TANG)
					// return -1: productSubTotalAmountFast(API) < productSubTotalAmountIrbot (GIAM)
					if (productSubTotalAmountFast.compareTo(productSubTotalAmountIrbot) == 1) {

						// SubTotal Amount
						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HddvDetailRes hddvDetailNew = new HddvDetailRes();
						hddvDetailNew.setFastId(hddvDetailFast.getFastId());
						hddvDetailNew.setFastDetailId(hddvDetailFast.getFastDetailId());
						hddvDetailNew.setProductName(hddvDetailFast.getProductName());
						hddvDetailNew.setProductTaxId(hddvDetailFast.getProductTaxId());
						hddvDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hddvDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hddvDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						listHddvDetailIncrease.add(hddvDetailNew);
					}
					break;
				}
			}
		}
		hddvMasterIncrease.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hddvMasterIncrease.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hddvMasterIncrease.setTotalAmount(String.format("%.2f", totalAmount));
		hddvMasterIncrease.setDetails(listHddvDetailIncrease);

		return hddvMasterIncrease;
	}

	/**
	 * Kiem tra dieu chinh giam
	 * 
	 * @param hddvFast
	 * @param hddvIrbot
	 * @return
	 */
	private HddvMasterRes getListHddvDetailPriceReduce(HddvMasterRes hddvFast, HddvMasterRes hddvIrbot) {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hddvFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_GIAM.value());

		HddvMasterRes hddvMasterReduce = new HddvMasterRes();
		hddvMasterReduce.setMode(String.valueOf(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_GIAM.value()));
		hddvMasterReduce.setFastId(fastId);
		hddvMasterReduce.setFastInvoiceNo(hddvFast.getFastInvoiceNo());
		hddvMasterReduce.setUnitCode(hddvFast.getUnitCode());
		hddvMasterReduce.setCustomerId(hddvFast.getCustomerId());
		hddvMasterReduce.setBuyerName(hddvFast.getBuyerName());
		hddvMasterReduce.setCustomerName(hddvFast.getCustomerName());
		hddvMasterReduce.setCustomerTaxCode(hddvFast.getCustomerTaxCode());
		hddvMasterReduce.setAddress(hddvFast.getAddress());
		hddvMasterReduce.setInvoiceDate(hddvFast.getInvoiceDate());
		hddvMasterReduce.setInvoiceModifyDate(now);
		hddvMasterReduce.setDescription(hddvFast.getDescription());
		hddvMasterReduce.setPaymentMethod(hddvFast.getPaymentMethod());
		hddvMasterReduce.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HddvDetailRes> listHddvDetailReduce = new ArrayList<HddvDetailRes>();

		for (HddvDetailRes hddvDetailIrbot : hddvIrbot.getDetails()) {
			for (HddvDetailRes hddvDetailFast : hddvFast.getDetails()) {
				if (hddvDetailIrbot.getProductName().equals(hddvDetailFast.getProductName())) {

					BigDecimal productSubTotalAmountFast = BigDecimal
							.valueOf(Double.parseDouble(hddvDetailFast.getProductSubtotalAmount()));

					BigDecimal productSubTotalAmountIrbot = BigDecimal
							.valueOf(Double.parseDouble(hddvDetailIrbot.getProductSubtotalAmount()));

					// return 0: equal
					// return 1: productSubTotalAmountFast(API) > productSubTotalAmountIrbot (TANG)
					// return -1: productSubTotalAmountFast(API) < productSubTotalAmountIrbot (GIAM)
					if (productSubTotalAmountFast.compareTo(productSubTotalAmountIrbot) == -1) {

						// SubTotal Amount
						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hddvDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HddvDetailRes hddvDetailNew = new HddvDetailRes();
						hddvDetailNew.setFastId(hddvDetailFast.getFastId());
						hddvDetailNew.setFastDetailId(hddvDetailFast.getFastDetailId());
						hddvDetailNew.setProductName(hddvDetailFast.getProductName());
						hddvDetailNew.setProductTaxId(hddvDetailFast.getProductTaxId());
						hddvDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hddvDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hddvDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						listHddvDetailReduce.add(hddvDetailNew);
					}
					break;
				}
			}
		}
		hddvMasterReduce.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hddvMasterReduce.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hddvMasterReduce.setTotalAmount(String.format("%.2f", totalAmount));
		hddvMasterReduce.setDetails(listHddvDetailReduce);

		return hddvMasterReduce;
	}

	@Override
	@Transactional
	public void confirmModify(String fastIds) throws Exception {
		logger.info("Xac nhan dieu chinh Hoa don dich vu !");
		WorkProcess workProcessExist = new WorkProcess();
		String fastArr[] = Convert.toStrArray(fastIds);
		for (String id : fastArr) {

			// Lay fastId(stt_rec), mode
			String[] temp = id.split("_");
			String fastId = temp[0];
			String invoiceMode = temp[1];

			HddvMasterRes hddvModify = new HddvMasterRes();

			// Call API de lay thong tin theo fastId(stt_rec)
			ApiGetDataReq apiRequest = new ApiGetDataReq();
			apiRequest.setFastId(fastId);
			apiRequest.setIsSelectDetail(true);

			ApiGetDataHddvRes result = getHddvData(apiRequest);

			// Du lieu API tra ve
			List<HddvMasterRes> listHddvFast = result.getData();
			HddvMasterRes hddvFast = listHddvFast.get(0);

			// Du lieu lay tu table workprocess
			workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(fastId);
			HddvMasterRes hddvIrbot = readDataRequest(workProcessExist);

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_TANG.value()) {
				hddvModify = getListHddvDetailPriceIncrease(hddvFast, hddvIrbot);
			}

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_GIAM.value()) {
				hddvModify = getListHddvDetailPriceReduce(hddvFast, hddvIrbot);
			}

			if (hddvModify != null) {
				Hddv hddvInsert = new Hddv();
				Date now = DateUtils.addSeconds(new Date(), -30);
				// Insert hddv_modify
				hddvInsert.setMode(Integer.parseInt(invoiceMode));
				hddvInsert.setFastId(id);
				hddvInsert.setFastInvoiceNo(hddvModify.getFastInvoiceNo());
				hddvInsert.setUnitCode(hddvModify.getUnitCode());
				hddvInsert.setCustomerId(hddvModify.getCustomerId());
				hddvInsert.setBuyerName(hddvModify.getBuyerName());
				hddvInsert.setCustomerName(hddvModify.getCustomerName());
				hddvInsert.setCustomerTaxCode(hddvModify.getCustomerTaxCode());
				hddvInsert.setAddress(hddvModify.getAddress());
				hddvInsert.setInvoiceDate(hddvModify.getInvoiceDate());
				hddvInsert.setInvoiceModifyDate(hddvModify.getInvoiceModifyDate());
				hddvInsert.setDescription(hddvModify.getDescription());
				hddvInsert.setTotalTaxAmount(hddvModify.getTotalTaxAmount());
				hddvInsert.setSubtotalAmount(hddvModify.getSubtotalAmount());
				hddvInsert.setTotalAmount(hddvModify.getTotalAmount());
				hddvInsert.setPaymentMethod(hddvModify.getPaymentMethod());
				hddvInsert.setStatus(ProcessStatus.NOTSEND.value());
				hddvInsert.setCreateTime(now);
				hddvModifyMapper.insertHddvModify(hddvInsert);

				// Insert detail data
				if (hddvModify.getDetails() != null) {
					for (HddvDetailRes detail : hddvModify.getDetails()) {
						HddvDetail hddvDetailInsert = new HddvDetail();
						hddvDetailInsert.setHddvId(hddvInsert.getId());
						hddvDetailInsert.setFastId(hddvInsert.getFastId());
						hddvDetailInsert.setFastDetailId(detail.getFastDetailId());
						hddvDetailInsert.setProductName(detail.getProductName());
						hddvDetailInsert.setProductTaxId(detail.getProductTaxId());
						hddvDetailInsert.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hddvDetailInsert.setProductTaxAmount(detail.getProductTaxAmount());
						hddvDetailInsert.setProductTotalAmount(detail.getProductTotalAmount());
						hddvDetailInsert.setCreateTime(now);
						hddvModifyDetailMapper.insertHddvModifyDetail(hddvDetailInsert);
					}
				}
			}
		}
		// Set Mode da dieu chinh
		workProcessExist.setData2(String.valueOf(ProcessMode.DA_DIEU_CHINH.value()));
		workProcessMapper.updateWorkProcessSyncKey(workProcessExist);
	}
}