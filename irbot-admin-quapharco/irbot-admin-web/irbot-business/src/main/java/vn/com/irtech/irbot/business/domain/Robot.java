package vn.com.irtech.irbot.business.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Object Robot robot
 *
 * @author irtech
 * @date 2021-12-04
 */
public class Robot extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** Robot Id */
	private Long id;

	/** Robot UUID */
	@Excel(name = "Robot UUID")
	private String uuid;

	/** IP Address */
	@Excel(name = "IP Address")
	private String ipAddress;

	/** Robot Status（0 Offline 1 Busy 2 Available） */
	@Excel(name = "Robot Status", readConverterExp = "0=,O=ffline,1=,B=usy,2=,A=vailable")
	private String status;

	/** Whether robot disable（0 Enable 1 Disable） */
	@Excel(name = "Whether robot disable", readConverterExp = "0=,E=nable,1=,D=isable")
	private String disabled;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date pingTime;

	/** Delete flag (0 existence 2 deletion) */
	private String delFlag;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public Date getPingTime() {
		return pingTime;
	}

	public void setPingTime(Date pingTime) {
		this.pingTime = pingTime;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("uuid", getUuid())
				.append("status", getStatus()).append("disable", getDisabled()).append("delFlag", getDelFlag())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).toString();
	}
}