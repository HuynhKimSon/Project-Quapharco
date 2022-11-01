package vn.com.irtech.irbot.business.mqtt.listener;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.irbot.business.domain.Hdbh;
import vn.com.irtech.irbot.business.domain.HdbhCn;
import vn.com.irtech.irbot.business.domain.HdbhNb;
import vn.com.irtech.irbot.business.domain.Hddv;
import vn.com.irtech.irbot.business.domain.HdxNcc;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.request.ApiUpdateInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.RobotProcessDataRes;
import vn.com.irtech.irbot.business.dto.response.RobotProcessRes;
import vn.com.irtech.irbot.business.mapper.HdbhCnModifyMapper;
import vn.com.irtech.irbot.business.mapper.HdbhModifyMapper;
import vn.com.irtech.irbot.business.mapper.HdbhNbModifyMapper;
import vn.com.irtech.irbot.business.mapper.HddvModifyMapper;
import vn.com.irtech.irbot.business.mapper.HdxNccModifyMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IApiGatewayUpdateInoviceNoService;
import vn.com.irtech.irbot.business.type.InvoiceMode;
import vn.com.irtech.irbot.business.type.ProcessStatus;

@Component
public class ProcessResponseHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ProcessResponseHandler.class);

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	@Autowired
	private IApiGatewayUpdateInoviceNoService apiGatewayUpdateInoviceNoService;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private HdbhModifyMapper hdbhModifyMapper;

	@Autowired
	private HdbhCnModifyMapper hdbhCnModifyMapper;

	@Autowired
	private HddvModifyMapper hddvModifyMapper;

	@Autowired
	HdxNccModifyMapper hdxNccModifyMapper;

	@Autowired
	HdbhNbModifyMapper hdbhNbModifyMapper;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					processMessage(topic, message);
				} catch (Exception e) {
					logger.error("Error while process mq message", e);
					e.printStackTrace();
				}
			}
		});
	}

	@Transactional
	private void processMessage(String topic, MqttMessage message) throws Exception {
		String messageContent = new String(message.getPayload(), StandardCharsets.UTF_8);
		logger.info(">>>> Receive message topic: {}, content {}", topic, messageContent);
		RobotProcessRes response = null;
		try {
			response = new Gson().fromJson(messageContent, RobotProcessRes.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (response.getSyncId() == null) {
			return;
		}

		Long processId = response.getProcessId();
		Integer syncId = response.getSyncId();
		String fastId = response.getFastId();
		Integer serviceType = response.getServiceType();
		String serviceName = response.getServiceName();
		String result = response.getResult();
		String errMsg = response.getErrorMsg();
		String robotUuid = topic.split("/")[3];
		RobotProcessDataRes data = response.getData();

		updateProcessQuapharcoStatus(robotUuid, messageContent, processId, syncId, fastId, serviceType, serviceName,
				result, errMsg, data);
		return;
	}

	private void updateProcessQuapharcoStatus(String robotUuid, String messageContent, Long processId, Integer syncId,
			String fastId, Integer serviceType, String serviceName, String result, String errorMsg,
			RobotProcessDataRes data) {
		// Get process by Id
		WorkProcess process = workProcessMapper.selectWorkProcessById(processId);
		if (process == null) {
			logger.warn("Work Process not exist: {}", processId);
			return;
		}
		String errMsg = null;
		ProcessStatus processStatus = null;
		String eInvoiceNo = null;
		switch (result.toUpperCase()) {
		case "SUCCESS":
			processStatus = ProcessStatus.SUCCESS;
			eInvoiceNo = data.getInvoiceNo();
			break;
		case "PROCESSING":
			processStatus = ProcessStatus.PROCESSING;
			break;
		case "FAIL":
			processStatus = ProcessStatus.FAIL;
			errMsg = errorMsg;
			break;
		default:
			return;
		}

		WorkProcess processUpdate = new WorkProcess();
		processUpdate.setId(processId);
		processUpdate.setDataResponse(messageContent);
		processUpdate.setRobotUuid(robotUuid);
		processUpdate.setData1(eInvoiceNo);
		if (processStatus != ProcessStatus.PROCESSING) {
			processUpdate.setEndDate(new Date());
		}
		processUpdate.setStatus(processStatus.value());
		processUpdate.setError(errMsg);
		workProcessMapper.updateWorkProcess(processUpdate);

		// Update table hdbh_modify
		if (serviceName.equals(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_TANG.title())
				|| serviceName.equals(InvoiceMode.HOA_DON_BAN_HANG_DIEU_CHINH_GIAM.title())) {

			Hdbh hdbhModifyUpdate = new Hdbh();
			hdbhModifyUpdate.setId(syncId.longValue());
			hdbhModifyUpdate.setStatus(processStatus.value());
			hdbhModifyUpdate.seteInvoiceNo(eInvoiceNo);
			hdbhModifyMapper.updateHdbhModify(hdbhModifyUpdate);
			return;
		}

		// Update table hdbh_cn_modify
		if (serviceName.equals(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG.title())
				|| serviceName.equals(InvoiceMode.HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM.title())) {

			HdbhCn hdbhCnModifyUpdate = new HdbhCn();
			hdbhCnModifyUpdate.setId(syncId.longValue());
			hdbhCnModifyUpdate.setStatus(processStatus.value());
			hdbhCnModifyUpdate.seteInvoiceNo(eInvoiceNo);
			hdbhCnModifyMapper.updateHdbhCnModify(hdbhCnModifyUpdate);
			return;
		}

		// Update table hddv_modify
		if (serviceName.equals(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_TANG.title())
				|| serviceName.equals(InvoiceMode.HOA_DON_DICH_VU_DIEU_CHINH_GIAM.title())) {

			Hddv hddvModifyUpdate = new Hddv();
			hddvModifyUpdate.setId(syncId.longValue());
			hddvModifyUpdate.setStatus(processStatus.value());
			hddvModifyUpdate.seteInvoiceNo(eInvoiceNo);
			hddvModifyMapper.updateHddvModify(hddvModifyUpdate);
			return;
		}

		// Update table hdx_ncc_modify
		if (serviceName.equals(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_TANG.title())
				|| serviceName.equals(InvoiceMode.HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM.title())) {
			HdxNcc hdxNccModifyUpdate = new HdxNcc();
			hdxNccModifyUpdate.setId(syncId.longValue());
			hdxNccModifyUpdate.setStatus(processStatus.value());
			hdxNccModifyUpdate.seteInvoiceNo(eInvoiceNo);
			hdxNccModifyMapper.updateHdxNccModify(hdxNccModifyUpdate);
			return;

		}

		// Update table hdnb_modify
		if (serviceName.equals(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_TANG.title())
				|| serviceName.equals(InvoiceMode.HOA_DON_NOI_BO_DIEU_CHINH_GIAM.title())) {
			HdbhNb hdbhNbModifyDetail = new HdbhNb();
			hdbhNbModifyDetail.setId(syncId.longValue());
			hdbhNbModifyDetail.setStatus(processStatus.value());
			hdbhNbModifyDetail.seteInvoiceNo(eInvoiceNo);
			hdbhNbModifyMapper.updateHdbhNbModify(hdbhNbModifyDetail);
			return;
		}

		// Call Api Update InvoiceNo vao table khach hang
		if (processStatus.equals(ProcessStatus.SUCCESS)) {
			ApiUpdateInvoiceNoReq request = new ApiUpdateInvoiceNoReq();

			request.setFastId(fastId);
			request.setFastInvoiceNo(eInvoiceNo);

			switch (serviceName) {
			case "HOA_DON_BAN_HANG_GOC":
				try {
					apiGatewayUpdateInoviceNoService.updateInvoiceNoHdbhMaster(request);
				} catch (Exception e) {
					logger.error(">>>>>> Call Api Update InvoiceNo HOA_DON_BAN_HANG_GOC FAIL");
					e.printStackTrace();
				}
				break;
			case "HOA_DON_BAN_HANG_CN_GOC":
				try {
					apiGatewayUpdateInoviceNoService.updateInvoiceNoHdbhChiNhanh(request);
				} catch (Exception e) {
					logger.error(">>>>>> Call Api Update InvoiceNo HOA_DON_BAN_HANG_CN_GOC FAIL");
					e.printStackTrace();
				}
				break;
			case "HOA_DON_DICH_VU_GOC":
				try {
					apiGatewayUpdateInoviceNoService.updateInvoiceNoHdDichVu(request);
				} catch (Exception e) {
					logger.error(">>>>>> Call Api Update InvoiceNo HOA_DON_DICH_VU_GOC FAIL");
					e.printStackTrace();
				}
				break;
			case "HOA_DON_XUAT_NCC_GOC":
				try {
					apiGatewayUpdateInoviceNoService.updateInvoiceNoHdxNhaCungCap(request);
				} catch (Exception e) {
					logger.error(">>>>>> Call Api Update InvoiceNo HOA_DON_XUAT_NCC_GOC FAIL");
					e.printStackTrace();
				}
				break;
			case "HOA_DON_NOI_BO_GOC":
				try {
					apiGatewayUpdateInoviceNoService.updateInvoiceNoHdbhNoiBo(request);
				} catch (Exception e) {
					logger.error(">>>>>> Call Api Update InvoiceNo HOA_DON_NOI_BO_GOC FAIL");
					e.printStackTrace();
				}
				break;
			default:
				logger.error("Service Name not exist: " + serviceName);
				return;
			}
		}
	}

}