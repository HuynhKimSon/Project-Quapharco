package vn.com.irtech.irbot.system.domain;

import java.util.Arrays;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.annotation.Excel.Type;
import vn.com.irtech.core.common.annotation.Excels;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.utils.StringUtils;

/**
 * 
 * @author admin
 */
public class SysUser extends LoginUser {
	private static final long serialVersionUID = 1L;

	@Excel(name = "UserID", cellType = ColumnType.NUMERIC, prompt = "UserID")
	private Long userId;

	/** user code */
	@Excel(name = "userCode")
	private String userCode;

	@Excel(name = "Department ID", type = Type.IMPORT)
	private Long deptId;

	private Long parentId;

	private Long roleId;

	@Excel(name = "Login Name")
	private String loginName;

	@Excel(name = "Username")
	private String userName;

	private String userType;

	@Excel(name = "Email")
	private String email;

	@Excel(name = "Mobile")
	private String phonenumber;

	@Excel(name = "Gender", readConverterExp = "0=Male,1=Female,2=Unknow")
	private String sex;

	private String avatar;

	private String password;

	private String salt;

	@Excel(name = "Status", readConverterExp = "0=Active,1=Disabled")
	private String status;

	private String delFlag;

	@Excel(name = "Login IP", type = Type.EXPORT)
	private String loginIp;

	@Excel(name = "Login Date", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
	private Date loginDate;

	/** password update date */
	private Date pwdUpdateDate;

	@Excels({ @Excel(name = "Department name", targetAttr = "deptName", type = Type.EXPORT),
			@Excel(name = "Head of Department", targetAttr = "leader", type = Type.EXPORT) })
	private SysDept dept;

//    private List<SysRole> roles;

	private Long[] roleIds;

	private Long[] postIds;

	/** Tên chương trình */
	@Excel(name = "Tên chương trình")
	private String projectName;

	/** Loại ứng dụng */
	@Excel(name = "Loại ứng dụng")
	private String typeName;

	/** Trạng thái làm việc */
	@Excel(name = "Trạng thái làm việc")
	private String statusWorking;

	/** Mã chi nhánh */
	@Excel(name = "Mã chi nhánh")
	private String branchCode;

	/** QR salt */
	private String saltQr;

	/** Qr Status: 0/1 (Mở khóa/Khóa) */
	private Integer qrLockStatus;

	public SysUser() {

	}

	public SysUser(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isAdmin() {
		return isAdmin(this.userId);
	}

	public static boolean isAdmin(Long userId) {
		return userId != null && 1L == userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@NotBlank(message = "LoginName không được để trống")
	@Size(min = 0, max = 30, message = "LoginName không vượt quá 30 ký tự")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Size(min = 0, max = 30, message = "userName không vượt quá 30 ký tự")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Email(message = "Email không được để trống")
	@Size(min = 0, max = 50, message = "Email không được vượt quá 50 ký tự")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Size(min = 0, max = 11, message = "Số điện thoại không vượt quá 11 ký tự")
	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		if (StringUtils.isEmpty(avatar)) {
			return StringUtils.EMPTY;
		} else {
			return avatar;
		}
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getPwdUpdateDate() {
		return pwdUpdateDate;
	}

	public void setPwdUpdateDate(Date pwdUpdateDate) {
		this.pwdUpdateDate = pwdUpdateDate;
	}

	public SysDept getDept() {
		if (dept == null) {
			dept = new SysDept();
		}
		return dept;
	}

	public void setDept(SysDept dept) {
		this.dept = dept;
	}

//    public List<SysRole> getRoles()
//    {
//        return roles;
//    }
//
//    public void setRoles(List<SysRole> roles)
//    {
//        this.roles = roles;
//    }

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public Long[] getPostIds() {
		return postIds;
	}

	public void setPostIds(Long[] postIds) {
		this.postIds = postIds;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setStatusWorking(String statusWorking) {
		this.statusWorking = statusWorking;
	}

	public String getStatusWorking() {
		return statusWorking;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setSaltQr(String saltQr) {
		this.saltQr = saltQr;
	}

	public String getSaltQr() {
		return saltQr;
	}

	public Integer getQrLockStatus() {
		return qrLockStatus;
	}

	public void setQrLockStatus(Integer qrLockStatus) {
		this.qrLockStatus = qrLockStatus;
	}

	@Override
	public String toString() {
		return "SysUser [userId=" + userId + ", deptId=" + deptId + ", parentId=" + parentId + ", roleId=" + roleId
				+ ", loginName=" + loginName + ", userName=" + userName + ", userType=" + userType + ", email=" + email
				+ ", phonenumber=" + phonenumber + ", sex=" + sex + ", avatar=" + avatar + ", password=" + password
				+ ", salt=" + salt + ", status=" + status + ", delFlag=" + delFlag + ", loginIp=" + loginIp
				+ ", loginDate=" + loginDate + ", pwdUpdateDate=" + pwdUpdateDate + ", dept=" + dept + ", roleIds="
				+ Arrays.toString(roleIds) + ", postIds=" + Arrays.toString(postIds) + ", userCode=" + userCode
				+ ", projectName=" + projectName + ", typeName=" + typeName + ", statusWorking=" + statusWorking
				+ ", branchCode=" + branchCode + ", saltQr=" + saltQr + ", qrLockStatus=" + qrLockStatus + "]";
	}

}
