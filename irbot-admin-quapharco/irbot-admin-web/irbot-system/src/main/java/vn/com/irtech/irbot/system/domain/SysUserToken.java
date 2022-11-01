package vn.com.irtech.irbot.system.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Notify Token Object sys_user_token
 * 
 * @author
 * @date 2020-08-22
 */
public class SysUserToken extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Long id;

	/** User ID */
	@Excel(name = "User ID")
	private Long userId;

	/** User Type */
	@Excel(name = "User Type")
	private Long userType;

	/** Login Token */
	@Excel(name = "Login Token")
	private String userLoginToken;

	/** Device Token */
	@Excel(name = "Device Token")
	private String deviceToken;

	/** Expired Time */
	@Excel(name = "Expired Time", width = 30, dateFormat = "yyyy-MM-dd")
	private Date expireTime;

	/** Expired Flag */
	@Excel(name = "Expired Flag")
	private Long expireFlg;

	/** Login IP */
	@Excel(name = "Login IP")
	private String loginIp;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserType(Long userType) {
		this.userType = userType;
	}

	public Long getUserType() {
		return userType;
	}

	public void setUserLoginToken(String userLoginToken) {
		this.userLoginToken = userLoginToken;
	}

	public String getUserLoginToken() {
		return userLoginToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireFlg(Long expireFlg) {
		this.expireFlg = expireFlg;
	}

	public Long getExpireFlg() {
		return expireFlg;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginIp() {
		return loginIp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("userId", getUserId()).append("userType", getUserType())
				.append("userLoginToken", getUserLoginToken()).append("deviceToken", getDeviceToken())
				.append("expireTime", getExpireTime()).append("expireFlg", getExpireFlg())
				.append("loginIp", getLoginIp()).append("createTime", getCreateTime()).append("createBy", getCreateBy())
				.append("updateTime", getUpdateTime()).append("updateBy", getUpdateBy()).toString();
	}
}
