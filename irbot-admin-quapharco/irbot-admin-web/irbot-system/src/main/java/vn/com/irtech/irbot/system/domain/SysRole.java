package vn.com.irtech.irbot.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.domain.UserRole;

/**
 * 
 * @author admin
 */
public class SysRole extends UserRole {
	private static final long serialVersionUID = 1L;

	@Excel(name = "RoleID", cellType = ColumnType.NUMERIC)
	private Long roleId;

	@Excel(name = "Role Name")
	private String roleName;

	@Excel(name = "Role Key")
	private String roleKey;

	@Excel(name = "Sort", cellType = ColumnType.NUMERIC)
	private String roleSort;

	@Excel(name = "Data Range", readConverterExp = "1=All,2=Self,3=Department,4=Department and Childs")
	private String dataScope;

	@Excel(name = "Status", readConverterExp = "0=Active,1=Disabled")
	private String status;

	private String delFlag;

	private boolean flag = false;

	private Long[] menuIds;

	private Long[] deptIds;

	public SysRole() {

	}

	public SysRole(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public boolean isAdmin() {
		return isAdmin(this.roleId);
	}

	public static boolean isAdmin(Long roleId) {
		return roleId != null && 1L == roleId;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	@NotBlank(message = "Role name cannot be empty")
	@Size(min = 0, max = 30, message = "The character name cannot exceed 30 chars")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@NotBlank(message = "The permission character cannot be empty")
	@Size(min = 0, max = 100, message = "The permission character length cannot exceed 100 chars")
	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	@NotBlank(message = "Display order cannot be empty")
	public String getRoleSort() {
		return roleSort;
	}

	public void setRoleSort(String roleSort) {
		this.roleSort = roleSort;
	}

	public String getStatus() {
		return status;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Long[] getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(Long[] menuIds) {
		this.menuIds = menuIds;
	}

	public Long[] getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(Long[] deptIds) {
		this.deptIds = deptIds;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("roleId", getRoleId())
				.append("roleName", getRoleName()).append("roleKey", getRoleKey()).append("roleSort", getRoleSort())
				.append("dataScope", getDataScope()).append("status", getStatus()).append("delFlag", getDelFlag())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
