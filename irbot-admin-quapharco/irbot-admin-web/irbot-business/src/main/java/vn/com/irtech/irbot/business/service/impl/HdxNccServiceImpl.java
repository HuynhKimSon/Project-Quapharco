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
import vn.com.irtech.irbot.business.domain.HdxNcc;
import vn.com.irtech.irbot.business.domain.HdxNccDetail;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.ProcessFastConfig;
import vn.com.irtech.irbot.business.dto.RobotSyncHdxNccReq;
import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdxNccRes;
import vn.com.irtech.irbot.business.dto.response.HdxNccDetailRes;
import vn.com.irtech.irbot.business.dto.response.HdxNccMasterRes;
import vn.com.irtech.irbot.business.mapper.HdxNccModifyDetailMapper;
import vn.com.irtech.irbot.business.mapper.HdxNccModifyMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IApiGatewayService;
import vn.com.irtech.irbot.business.service.IHdxNccService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Service
public class HdxNccServiceImpl implements IHdxNccService {

	private static final Logger logger = LoggerFactory.getLogger(HdxNccServiceImpl.class);

	@Autowired
	private IApiGatewayService apiGatewayService;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private HdxNccModifyMapper hdxNccModifyMapper;

	@Autowired
	private HdxNccModifyDetailMapper hdxNccModifyDetailMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Override
	public HdxNccMasterRes selectHdxNccById(String fastId) throws Exception {
		ApiGetDataReq apiRequest = new ApiGetDataReq();
		apiRequest.setFastId(fastId);
		apiRequest.setIsSelectDetail(true);

		ApiGetDataHdxNccRes result = getHdxNccData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdxNccMasterRes> listHdxNccFast = result.getData();

		if (CollectionUtils.isEmpty(listHdxNccFast)) {
			throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
		}

		HdxNccMasterRes hdxNcc = listHdxNccFast.get(0);

		WorkProcess workProcessSelect = new WorkProcess();
		String syncKey = hdxNcc.getFastId();
		workProcessSelect.setSyncKey(syncKey);

		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {
			hdxNcc.setProcessStatus(listWorkProcessExist.get(0).getStatus());
		}

		return hdxNcc;
	}

	@Override
	public TableDataInfo selectHdxNccList(HdxNcc hdxNcc) throws Exception {

		if (hdxNcc.getParams() == null) {
			throw new Exception("Điều kiện tìm kiếm không hợp lệ!");
		}

		Object beginTimeObj = hdxNcc.getParams().get("beginTime");
		Object endTimeObj = hdxNcc.getParams().get("endTime");
		Object fastInvoiceNoObj = hdxNcc.getParams().get("fastInvoiceNo");

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

		ApiGetDataHdxNccRes result = getHdxNccData(apiRequest);

		if (result == null || result.getData() == null) {
			throw new Exception("Lỗi query Fast, vui lòng thử lại sau!");
		}

		List<HdxNccMasterRes> listHdxNccFast = result.getData();
		logger.info(">> So hoa don lay ve hien thi: " + listHdxNccFast.size());

		if (CollectionUtils.isNotEmpty(listHdxNccFast)) {
			List<HdxNccMasterRes> removeItems = new ArrayList<HdxNccMasterRes>();
			Boolean searchByStatus = (hdxNcc.getStatus() != null && hdxNcc.getStatus() > 0);

			for (HdxNccMasterRes hdxNccMasterRes : listHdxNccFast) {
				WorkProcess workProcessSelect = new WorkProcess();
				String syncKey = hdxNccMasterRes.getFastId();

				workProcessSelect.setSyncKey(syncKey);
				workProcessSelect.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());

				// Set theo trang thai fillter
				if (searchByStatus) {
					workProcessSelect.setStatus(hdxNcc.getStatus());
				}

				List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

				if (CollectionUtils.isNotEmpty(listWorkProcessExist)) {

					// Add hoa don da ton tai trong work process vao list removeItems
					if (hdxNcc.getStatus() == ProcessStatus.NOTSEND.value()) {
						removeItems.add(hdxNccMasterRes);
					} else {
						hdxNccMasterRes.setProcessStatus(listWorkProcessExist.get(0).getStatus());
					}

					// View fast e-invoiceNo neu trang thai thanh cong
					if (hdxNccMasterRes.getProcessStatus() == ProcessStatus.SUCCESS.value()) {
						hdxNccMasterRes.seteInvoiceNo(listWorkProcessExist.get(0).getData1());
					}

					// Set trang thai goc/ da chinh sua
					if (listWorkProcessExist.get(0).getData2() != null) {
						hdxNccMasterRes.setMode(listWorkProcessExist.get(0).getData2());
					}

				} else if (searchByStatus && hdxNcc.getStatus() > 0) {
					removeItems.add(hdxNccMasterRes);
				}
			}

			// Remove tat ca item khong du dieu kien trong list view
			if (CollectionUtils.isNotEmpty(removeItems)) {
				listHdxNccFast.removeAll(removeItems);
			}
		}

		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(listHdxNccFast);
		rspData.setTotal(listHdxNccFast.size());

		return rspData;
	}

	/**
	 * 
	 * @param request
	 * @return ApiGetDataHdxNccRes
	 * @throws Exception
	 */
	private ApiGetDataHdxNccRes getHdxNccData(ApiGetDataReq request) throws Exception {
		ApiGetDataHdxNccRes hdxNccList = apiGatewayService.getHdxNhaCungCap(request);
		return hdxNccList;
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

			Map<RobotSyncHdxNccReq, Robot> robotRequestMap = new HashMap<RobotSyncHdxNccReq, Robot>();

			String[] idArr = Convert.toStrArray(ids);
			Date now = new Date();
			for (String fastId : idArr) {

				// Call Api lay du lieu theo FastId
				ApiGetDataReq apiRequest = new ApiGetDataReq();
				apiRequest.setFastId(fastId);
				apiRequest.setIsSelectDetail(true);

				ApiGetDataHdxNccRes result = getHdxNccData(apiRequest);

				if (result == null || result.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdxNccMasterRes> listHdxNccFast = result.getData();

				if (CollectionUtils.isEmpty(listHdxNccFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdxNccMasterRes hdxNccMasterRes = listHdxNccFast.get(0);

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
				RobotSyncHdxNccReq dataRequestRobot = dataRequestRobot(hdxNccMasterRes);

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

	private RobotSyncHdxNccReq dataRequestRobot(HdxNccMasterRes hdxNccMasterRes) {

		RobotSyncHdxNccReq dataRequest = new RobotSyncHdxNccReq();
		dataRequest.setConfig(getProcessConfig(hdxNccMasterRes.getUnitCode()));
		dataRequest.setSyncId(0);
		dataRequest.setServiceType(InvoiceMode.HOA_DON_XUAT_NCC_GOC.value());
		dataRequest.setServiceName(InvoiceMode.HOA_DON_XUAT_NCC_GOC.title());
		dataRequest.setFastId(hdxNccMasterRes.getFastId());
		dataRequest.setFastInvoiceNo(hdxNccMasterRes.getFastInvoiceNo());
		dataRequest.setUnitCode(hdxNccMasterRes.getUnitCode());
		dataRequest.setCustomerId(hdxNccMasterRes.getCustomerId());
		dataRequest.setCustomerName(hdxNccMasterRes.getCustomerName());
		dataRequest.setCustomerTaxCode(hdxNccMasterRes.getCustomerTaxCode());
		dataRequest.setAddress(hdxNccMasterRes.getAddress());
		dataRequest.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", hdxNccMasterRes.getInvoiceDate()));
		dataRequest.setDescription(hdxNccMasterRes.getDescription());
		dataRequest.setPaymentMethod(hdxNccMasterRes.getPaymentMethod());
		dataRequest.setTransporter(hdxNccMasterRes.getTransporter());
		dataRequest.setTotalTaxAmount(hdxNccMasterRes.getTotalTaxAmount());
		dataRequest.setSubtotalAmount(hdxNccMasterRes.getSubtotalAmount());
		dataRequest.setTotalAmount(hdxNccMasterRes.getTotalAmount());
		dataRequest.setOrderQuantity(hdxNccMasterRes.getDetails().size());
		dataRequest.setProcessOrder(hdxNccMasterRes.getDetails());

		return dataRequest;
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
		logger.info("Reset Status Hoa don xuat tra ncc !");
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

				ApiGetDataHdxNccRes apiGetDataHdxNccRes = getHdxNccData(apiRequest);

				if (apiGetDataHdxNccRes == null || apiGetDataHdxNccRes.getData() == null) {
					throw new Exception("Lỗi lấy dữ liệu từ Fast, vui lòng thử lại sau!");
				}

				List<HdxNccMasterRes> listHdxNccFast = apiGetDataHdxNccRes.getData();

				if (CollectionUtils.isEmpty(listHdxNccFast)) {
					throw new Exception("Hoá đơn không tồn tại hoặc đã bị xoá!");
				}

				// Du lieu API tra ve
				HdxNccMasterRes hdxNccMasterRes = listHdxNccFast.get(0);

				RobotSyncHdxNccReq dataRequestRobot = dataRequestRobot(hdxNccMasterRes);
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
		logger.info("Reset Mode Hoa don xuat tra ncc !");
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
		logger.info("Check modify Hoa don xuat tra lai nha cung cap !");

		// Lay fastId(stt_rec)
		String[] temp = fastId.split("_");
		fastId = temp[0];

		List<HdxNccMasterRes> listHdxNccModify = new ArrayList<HdxNccMasterRes>();

		TableDataInfo rspData = new TableDataInfo();
		rspData.setRows(listHdxNccModify);

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

		ApiGetDataHdxNccRes result = getHdxNccData(apiRequest);

		// Kiem tra ton tai so hoa don tu API, neu khong ton tai thi bao loi
		if (result == null || result.getData() == null) {
			throw new Exception("Không tìm thấy số hoá đơn trên hệ thống Fast!");
		}

		// Du lieu API tra ve
		List<HdxNccMasterRes> listHdxNccFast = result.getData();
		HdxNccMasterRes hdxNccFast = listHdxNccFast.get(0);

		// Du lieu lay tu table workprocess
		HdxNccMasterRes hdxNccIrbot = readDataRequest(workProcessExist);

		// Truong hop thay doi gia
		if (CollectionUtils.isNotEmpty(hdxNccFast.getDetails())
				&& CollectionUtils.isNotEmpty(hdxNccIrbot.getDetails())) {

			// Kiem tra so luong mat hang chi tiet
			if (hdxNccFast.getDetails().size() != hdxNccIrbot.getDetails().size() || hdxNccFast.getDetails().size() == 0
					|| hdxNccIrbot.getDetails().size() == 0) {
				throw new Exception(
						"Tổng số lượng mặt hàng của Fast và IRBot tại thông tin chi tiết không trùng khớp!");
			}

			// Kiem tra dieu chinh gia tang
			HdxNccMasterRes listHdxNccMasterPriceIncrease = getListHdxNccDetailPriceIncrease(hdxNccFast, hdxNccIrbot);
			if (listHdxNccMasterPriceIncrease.getDetails().size() > 0) {
				listHdxNccModify.add(listHdxNccMasterPriceIncrease);
			}

			// Kiem tra dieu chinh gia giam
			HdxNccMasterRes getListHdxNccMasterPriceReduce = getListHdxNccDetailPriceReduce(hdxNccFast, hdxNccIrbot);
			if (getListHdxNccMasterPriceReduce.getDetails().size() > 0) {
				listHdxNccModify.add(getListHdxNccMasterPriceReduce);
			}
		}

		rspData.setCode(0);
		rspData.setRows(listHdxNccModify);
		rspData.setTotal(listHdxNccModify.size());

		return rspData;
	}

	/**
	 * 
	 * @param workProcessExist
	 * @return HdxNccMasterRes
	 */
	private HdxNccMasterRes readDataRequest(WorkProcess workProcessExist) {
		String dataRequest = workProcessExist.getDataRequest();
		HdxNccMasterRes hdxNccMasterRes = new HdxNccMasterRes();
		try {
			RobotSyncHdxNccReq robotSyncHdxNccReq = new Gson().fromJson(dataRequest, RobotSyncHdxNccReq.class);
			hdxNccMasterRes.setFastId(robotSyncHdxNccReq.getFastId());
			hdxNccMasterRes.setFastInvoiceNo(robotSyncHdxNccReq.getFastInvoiceNo());
			hdxNccMasterRes.seteInvoiceNo(workProcessExist.getData1());
			hdxNccMasterRes.setDetails(robotSyncHdxNccReq.getProcessOrder());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hdxNccMasterRes;
	}

	/**
	 * Kiem tra dieu chinh tang
	 * 
	 * @param hdxNccFast
	 * @param hdxNccIrbot
	 * @return HdxNccMasterRes
	 */
	private HdxNccMasterRes getListHdxNccDetailPriceIncrease(HdxNccMasterRes hdxNccFast, HdxNccMasterRes hdxNccIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdxNccFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_TANG.value());

		HdxNccMasterRes hdxNccMasterIncrease = new HdxNccMasterRes();
		hdxNccMasterIncrease.setMode(String.valueOf(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_TANG.value()));
		hdxNccMasterIncrease.setFastId(fastId);
		hdxNccMasterIncrease.setFastInvoiceNo(hdxNccFast.getFastInvoiceNo());
		hdxNccMasterIncrease.setUnitCode(hdxNccFast.getUnitCode());
		hdxNccMasterIncrease.setCustomerId(hdxNccFast.getCustomerId());
		hdxNccMasterIncrease.setBuyerName(hdxNccFast.getBuyerName());
		hdxNccMasterIncrease.setCustomerName(hdxNccFast.getCustomerName());
		hdxNccMasterIncrease.setCustomerTaxCode(hdxNccFast.getCustomerTaxCode());
		hdxNccMasterIncrease.setAddress(hdxNccFast.getAddress());
		hdxNccMasterIncrease.setInvoiceDate(hdxNccFast.getInvoiceDate());
		hdxNccMasterIncrease.setInvoiceModifyDate(now);
		hdxNccMasterIncrease.setDescription(hdxNccFast.getDescription());
		hdxNccMasterIncrease.setPaymentMethod(hdxNccFast.getPaymentMethod());
		hdxNccMasterIncrease.setTransporter(hdxNccFast.getTransporter());
		hdxNccMasterIncrease.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdxNccDetailRes> listHdxNccDetailIncrease = new ArrayList<HdxNccDetailRes>();

		for (HdxNccDetailRes hdxNccDetailIrbot : hdxNccIrbot.getDetails()) {
			for (HdxNccDetailRes hdxNccDetailFast : hdxNccFast.getDetails()) {
				if (hdxNccDetailIrbot.getProductId().equals(hdxNccDetailFast.getProductId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdxNccDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productPriceFast(API) > productPriceIrbot (TANG)
					// return -1: productPriceFast(API) < productPriceIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == 1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdxNccDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdxNccDetailRes hdxNccDetailNew = new HdxNccDetailRes();
						hdxNccDetailNew.setFastId(hdxNccDetailFast.getFastId());
						hdxNccDetailNew.setFastDetailId(hdxNccDetailFast.getFastDetailId());
						hdxNccDetailNew.setProductId(hdxNccDetailFast.getProductId());
						hdxNccDetailNew.setProductName(hdxNccDetailFast.getProductName());
						hdxNccDetailNew.setProductUnit(hdxNccDetailFast.getProductUnit());
						hdxNccDetailNew.setProductTaxId(hdxNccDetailFast.getProductTaxId());
						hdxNccDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdxNccDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdxNccDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdxNccDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdxNccDetailNew.setProductDiscount(hdxNccDetailFast.getProductDiscount());
						hdxNccDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdxNccDetailNew.setProductExpiredDate(hdxNccDetailFast.getProductExpiredDate());
						hdxNccDetailNew.setStockOut(hdxNccDetailFast.getStockOut());
						hdxNccDetailNew.setStockOutName(hdxNccDetailFast.getStockOutName());
						hdxNccDetailNew.setLotNo(hdxNccDetailFast.getLotNo());
						listHdxNccDetailIncrease.add(hdxNccDetailNew);
					}
					break;
				}
			}
		}
		hdxNccMasterIncrease.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdxNccMasterIncrease.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdxNccMasterIncrease.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdxNccMasterIncrease.setTotalAmount(String.format("%.2f", totalAmount));
		hdxNccMasterIncrease.setDetails(listHdxNccDetailIncrease);

		return hdxNccMasterIncrease;
	}

	/**
	 * * Kiem tra dieu chinh giam
	 * 
	 * @param hdxNccFast
	 * @param hdxNccIrbot
	 * @return HdxNccMasterRes
	 */
	private HdxNccMasterRes getListHdxNccDetailPriceReduce(HdxNccMasterRes hdxNccFast, HdxNccMasterRes hdxNccIrbot)
			throws Exception {

		Date now = DateUtils.addSeconds(new Date(), -30);

		BigDecimal totalQuantity = BigDecimal.valueOf(0.0d);
		BigDecimal totalTaxAmount = BigDecimal.valueOf(0.0d);
		BigDecimal subTotalAmount = BigDecimal.valueOf(0.0d);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0d);

		String fastId = hdxNccFast.getFastId() + "_"
				+ String.valueOf(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM.value());

		HdxNccMasterRes hdxNccMasterReduce = new HdxNccMasterRes();
		hdxNccMasterReduce.setMode(String.valueOf(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM.value()));
		hdxNccMasterReduce.setFastId(fastId);
		hdxNccMasterReduce.setFastInvoiceNo(hdxNccFast.getFastInvoiceNo());
		hdxNccMasterReduce.setUnitCode(hdxNccFast.getUnitCode());
		hdxNccMasterReduce.setCustomerId(hdxNccFast.getCustomerId());
		hdxNccMasterReduce.setBuyerName(hdxNccFast.getBuyerName());
		hdxNccMasterReduce.setCustomerName(hdxNccFast.getCustomerName());
		hdxNccMasterReduce.setCustomerTaxCode(hdxNccFast.getCustomerTaxCode());
		hdxNccMasterReduce.setAddress(hdxNccFast.getAddress());
		hdxNccMasterReduce.setInvoiceDate(hdxNccFast.getInvoiceDate());
		hdxNccMasterReduce.setInvoiceModifyDate(now);
		hdxNccMasterReduce.setDescription(hdxNccFast.getDescription());
		hdxNccMasterReduce.setPaymentMethod(hdxNccFast.getPaymentMethod());
		hdxNccMasterReduce.setTransporter(hdxNccFast.getTransporter());
		hdxNccMasterReduce.setProcessStatus(ProcessStatus.NOTSEND.value());

		List<HdxNccDetailRes> listHdxNccDetailIncrease = new ArrayList<HdxNccDetailRes>();

		for (HdxNccDetailRes hdxNccDetailIrbot : hdxNccIrbot.getDetails()) {
			for (HdxNccDetailRes hdxNccDetailFast : hdxNccFast.getDetails()) {

				if (hdxNccDetailIrbot.getProductId().equals(hdxNccDetailFast.getProductId())) {

					BigDecimal productPriceFast = BigDecimal
							.valueOf(Double.parseDouble(hdxNccDetailFast.getProductPrice()));

					BigDecimal productPriceIrbot = BigDecimal
							.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductPrice()));

					// return 0: equal
					// return 1: productPriceFast(API) > productPriceIrbot (TANG)
					// return -1: productPriceFast(API) < productPriceIrbot (GIAM)
					if (productPriceFast.compareTo(productPriceIrbot) == -1) {

						// Price
						BigDecimal diffPrice = productPriceFast.subtract(productPriceIrbot);

						// Quantity
						BigDecimal productQuantityFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductQuantity()));

						BigDecimal productQuantityIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductQuantity()));

						// Kiem tra thay doi so luong tung mat hang
						if (productQuantityFast.compareTo(productQuantityIrbot) != 0) {
							throw new Exception("Mã " + hdxNccDetailFast.getProductId() + " bị điều chỉnh số lượng!");
						}

						BigDecimal diffQuantity = productQuantityFast;

						// SubTotal Amount
						BigDecimal productSubTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductSubtotalAmount()));

						BigDecimal productSubTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductSubtotalAmount()));

						BigDecimal diffProductSubTotalAmount = productSubTotalAmountFast
								.subtract(productSubTotalAmountIrbot);

						// Tax Amount
						BigDecimal productTaxAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductTaxAmount()));

						BigDecimal productTaxAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductTaxAmount()));

						BigDecimal diffProductTaxAmount = productTaxAmountFast.subtract(productTaxAmountIrbot);

						// Total Amount
						BigDecimal productTotalAmountFast = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailFast.getProductTotalAmount()));

						BigDecimal productTotalAmountIrbot = BigDecimal
								.valueOf(Double.parseDouble(hdxNccDetailIrbot.getProductTotalAmount()));

						BigDecimal diffProductTotalAmount = productTotalAmountFast.subtract(productTotalAmountIrbot);

						totalQuantity = totalQuantity.add(diffQuantity);
						subTotalAmount = subTotalAmount.add(diffProductSubTotalAmount);
						totalTaxAmount = totalTaxAmount.add(diffProductTaxAmount);
						totalAmount = totalAmount.add(diffProductTotalAmount);

						HdxNccDetailRes hdxNccDetailNew = new HdxNccDetailRes();
						hdxNccDetailNew.setFastId(hdxNccDetailFast.getFastId());
						hdxNccDetailNew.setFastDetailId(hdxNccDetailFast.getFastDetailId());
						hdxNccDetailNew.setProductId(hdxNccDetailFast.getProductId());
						hdxNccDetailNew.setProductName(hdxNccDetailFast.getProductName());
						hdxNccDetailNew.setProductUnit(hdxNccDetailFast.getProductUnit());
						hdxNccDetailNew.setProductTaxId(hdxNccDetailFast.getProductTaxId());
						hdxNccDetailNew.setProductQuantity(String.format("%.2f", diffQuantity));
						hdxNccDetailNew.setProductPrice(String.format("%.2f", diffPrice));
						hdxNccDetailNew.setProductSubtotalAmount(String.format("%.2f", diffProductSubTotalAmount));
						hdxNccDetailNew.setProductTaxAmount(String.format("%.2f", diffProductTaxAmount));
						hdxNccDetailNew.setProductDiscount(hdxNccDetailFast.getProductDiscount());
						hdxNccDetailNew.setProductTotalAmount(String.format("%.2f", diffProductTotalAmount));
						hdxNccDetailNew.setProductExpiredDate(hdxNccDetailFast.getProductExpiredDate());
						hdxNccDetailNew.setStockOut(hdxNccDetailFast.getStockOut());
						hdxNccDetailNew.setStockOutName(hdxNccDetailFast.getStockOutName());
						hdxNccDetailNew.setLotNo(hdxNccDetailFast.getLotNo());
						listHdxNccDetailIncrease.add(hdxNccDetailNew);
					}
					break;
				}
			}
		}
		hdxNccMasterReduce.setTotalQuantity(String.format("%.2f", totalQuantity));
		hdxNccMasterReduce.setTotalTaxAmount(String.format("%.2f", totalTaxAmount));
		hdxNccMasterReduce.setSubtotalAmount(String.format("%.2f", subTotalAmount));
		hdxNccMasterReduce.setTotalAmount(String.format("%.2f", totalAmount));
		hdxNccMasterReduce.setDetails(listHdxNccDetailIncrease);

		return hdxNccMasterReduce;
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

			HdxNccMasterRes hdxNccModify = new HdxNccMasterRes();

			// Call API de lay thong tin theo fastId(stt_rec)
			ApiGetDataReq apiRequest = new ApiGetDataReq();
			apiRequest.setFastId(fastId);
			apiRequest.setIsSelectDetail(true);

			ApiGetDataHdxNccRes result = getHdxNccData(apiRequest);

			// Du lieu API tra ve
			List<HdxNccMasterRes> listHdxNccFast = result.getData();
			HdxNccMasterRes hdxNccFast = listHdxNccFast.get(0);

			// Du lieu lay tu table workprocess
			workProcessExist = workProcessMapper.selectWorkProcessBySyncKey(fastId);
			HdxNccMasterRes hdxNccIrbot = readDataRequest(workProcessExist);

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_TANG.value()) {
				hdxNccModify = getListHdxNccDetailPriceIncrease(hdxNccFast, hdxNccIrbot);
			}

			if (Integer.parseInt(invoiceMode) == InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM.value()) {
				hdxNccModify = getListHdxNccDetailPriceReduce(hdxNccFast, hdxNccIrbot);
			}

			if (hdxNccModify != null) {
				HdxNcc hdxNccInsert = new HdxNcc();
				Date now = DateUtils.addSeconds(new Date(), -30);
				// Insert hdxNcc_modify
				hdxNccInsert.setMode(Integer.parseInt(invoiceMode));
				hdxNccInsert.setFastId(id);
				hdxNccInsert.setFastInvoiceNo(hdxNccModify.getFastInvoiceNo());
				hdxNccInsert.setUnitCode(hdxNccModify.getUnitCode());
				hdxNccInsert.setCustomerId(hdxNccModify.getCustomerId());
				hdxNccInsert.setBuyerName(hdxNccModify.getBuyerName());
				hdxNccInsert.setCustomerName(hdxNccModify.getCustomerName());
				hdxNccInsert.setCustomerTaxCode(hdxNccModify.getCustomerTaxCode());
				hdxNccInsert.setAddress(hdxNccModify.getAddress());
				hdxNccInsert.setInvoiceDate(hdxNccModify.getInvoiceDate());
				hdxNccInsert.setInvoiceModifyDate(hdxNccModify.getInvoiceModifyDate());
				hdxNccInsert.setDescription(hdxNccModify.getDescription());
				hdxNccInsert.setTotalQuantity(hdxNccModify.getTotalQuantity());
				hdxNccInsert.setTotalTaxAmount(hdxNccModify.getTotalTaxAmount());
				hdxNccInsert.setSubtotalAmount(hdxNccModify.getSubtotalAmount());
				hdxNccInsert.setTotalAmount(hdxNccModify.getTotalAmount());
				hdxNccInsert.setPaymentMethod(hdxNccModify.getPaymentMethod());
				hdxNccInsert.setTransporter(hdxNccModify.getTransporter());
				hdxNccInsert.setStatus(ProcessStatus.NOTSEND.value());
				hdxNccInsert.setCreateTime(now);
				hdxNccModifyMapper.insertHdxNccModify(hdxNccInsert);

				// Insert detail data
				if (hdxNccModify.getDetails() != null) {
					for (HdxNccDetailRes detail : hdxNccModify.getDetails()) {
						HdxNccDetail hdxNccDetailInsert = new HdxNccDetail();
						hdxNccDetailInsert.setHdxNccId(hdxNccInsert.getId());
						hdxNccDetailInsert.setFastId(hdxNccInsert.getFastId());
						hdxNccDetailInsert.setFastDetailId(detail.getFastDetailId());
						hdxNccDetailInsert.setProductId(detail.getProductId());
						hdxNccDetailInsert.setProductUnit(detail.getProductUnit());
						hdxNccDetailInsert.setProductName(detail.getProductName());
						hdxNccDetailInsert.setProductTaxId(detail.getProductTaxId());
						hdxNccDetailInsert.setProductQuantity(detail.getProductQuantity());
						hdxNccDetailInsert.setProductPrice(detail.getProductPrice());
						hdxNccDetailInsert.setProductSubtotalAmount(detail.getProductSubtotalAmount());
						hdxNccDetailInsert.setProductTaxAmount(detail.getProductTaxAmount());
						hdxNccDetailInsert.setProductDiscount(detail.getProductDiscount());
						hdxNccDetailInsert.setProductTotalAmount(detail.getProductTotalAmount());
						hdxNccDetailInsert.setProductExpiredDate(detail.getProductExpiredDate());
						hdxNccDetailInsert.setStockOut(detail.getStockOut());
						hdxNccDetailInsert.setStockOutName(detail.getStockOutName());
						hdxNccDetailInsert.setLotNo(detail.getLotNo());
						hdxNccDetailInsert.setCreateTime(now);
						hdxNccModifyDetailMapper.insertHdxNccModifyDetail(hdxNccDetailInsert);
					}
				}
			}
		}
		// Set Mode da dieu chinh
		workProcessExist.setData2(String.valueOf(ProcessMode.DA_DIEU_CHINH.value()));
		workProcessMapper.updateWorkProcessSyncKey(workProcessExist);
	}
}