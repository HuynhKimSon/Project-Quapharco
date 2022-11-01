package vn.com.irtech.irbot.system.domain;

import vn.com.irtech.core.common.annotation.Excel;

public class UserExcelDto {
	@Excel(name="Mã cán bộ")
	private String userCode;
	
	@Excel(name="Tên cán bộ")
	private String userName;
	
	@Excel(name="Mã CN")
	private String branchCode;
	
	@Excel(name="Mã phòng ban")
	private String deptCode;
	
	@Excel(name="Trạng thái user")
	private String status;
	
	@Excel(name="Tên chương trình")
	private String projectName;
	
	@Excel(name="Loại ứng dụng")
	private String typeName;
	
	@Excel(name="Trạng thái làm việc")
	private String statusWorking;
	
	@Excel(name="Tên phòng ban")
	private String deptName;

	
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatusWorking() {
		return statusWorking;
	}

	public void setStatusWorking(String statusWorking) {
		this.statusWorking = statusWorking;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
