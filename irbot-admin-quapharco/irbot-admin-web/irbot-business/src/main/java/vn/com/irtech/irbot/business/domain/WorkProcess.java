package vn.com.irtech.irbot.business.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.core.common.domain.BaseEntity;

public class WorkProcess extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer serviceId;

	private String syncId;

	private String syncKey;

	private String data1;

	private String data2;

	private String data3;

	private String data4;

	private String data5;

	private Integer priority;

	private Integer status;

	private String error;

	private String dataRequest;

	private String dataResponse;

	private String robotUuid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String string) {
		this.syncId = string;
	}

	public String getSyncKey() {
		return syncKey;
	}

	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setDataRequest(String dataRequest) {
		this.dataRequest = dataRequest;
	}

	public String getDataRequest() {
		return dataRequest;
	}

	public void setDataResponse(String dataResponse) {
		this.dataResponse = dataResponse;
	}

	public String getDataResponse() {
		return dataResponse;
	}

	public void setRobotUuid(String robotUuid) {
		this.robotUuid = robotUuid;
	}

	public String getRobotUuid() {
		return robotUuid;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

}
