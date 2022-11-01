package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;

public class RobotProcessRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long processId;

	private Integer syncId;

	private String fastId;

	private Integer serviceType;

	private String serviceName;

	private String result;

	private String errorMsg;

	private RobotProcessDataRes data;

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Integer getSyncId() {
		return syncId;
	}

	public void setSyncId(Integer syncId) {
		this.syncId = syncId;
	}

	public String getFastId() {
		return fastId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public RobotProcessDataRes getData() {
		return data;
	}

	public void setData(RobotProcessDataRes data) {
		this.data = data;
	}

}
