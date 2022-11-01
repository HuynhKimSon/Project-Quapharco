package vn.com.irtech.core.common.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.annotation.Excel.Type;
import vn.com.irtech.core.common.utils.StringUtils;

/**
 * System Login User Base Class
 * 
 * @author admin
 */
public class LoginUser extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Excel(name = "ID", cellType = ColumnType.NUMERIC, prompt = "ID")
	private Long userId;

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
	
	// Roles and Dept
	private Long deptId;

    private List<UserRole> roles;

	public LoginUser() {

	}

	public LoginUser(Long userId) {
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

	@NotBlank(message = "Login account cannot be empty")
	@Size(min = 0, max = 30, message = "The length of the login account cannot exceed 30 chars")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Size(min = 0, max = 30, message = "The length of the user nickname cannot exceed 30 chars")
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

	@Email(message = "E-mail format is incorrect")
	@Size(min = 0, max = 250, message = "The length of the mailbox cannot exceed 250")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Size(min = 0, max = 20, message = "The length of the mobile phone number cannot exceed 20 chars")
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

    public List<UserRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<UserRole> roles)
    {
        this.roles = roles;
    }
    
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("userId", getUserId())
				.append("loginName", getLoginName()).append("userName", getUserName()).append("userType", getUserType())
				.append("email", getEmail()).append("phonenumber", getPhonenumber()).append("sex", getSex())
				.append("avatar", getAvatar()).append("password", getPassword()).append("salt", getSalt())
				.append("status", getStatus()).append("delFlag", getDelFlag()).append("loginIp", getLoginIp())
				.append("loginDate", getLoginDate()).append("createBy", getCreateBy())
				.append("createTime", getCreateTime()).append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime()).append("remark", getRemark()).toString();
	}
}
