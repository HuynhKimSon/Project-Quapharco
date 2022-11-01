package vn.com.irtech.irbot.system.domain;

import vn.com.irtech.core.common.annotation.Excel;

public class Employee {

	@Excel(name="Mã CBNV")
	private String userCode;
	
	@Excel(name="Họ và tên")
	private String userName;
	
	@Excel(name="Phòng ban")
	private String deptCode;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	
}
