package vn.com.irtech.irbot.system.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.domain.BaseEntity;
import vn.com.irtech.core.common.utils.StringUtils;

/**
 * 
 * @author admin
 */
public class SysMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private Long menuId;

	private String menuName;

	private String parentName;

	private Long parentId;

	private String orderNum;

	private String url;

	private String target;

	private String menuType;

	private String visible;

	private String isRefresh;

	private String perms;

	private String icon;

	private List<SysMenu> children = new ArrayList<SysMenu>();

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@NotBlank(message = "Menu name cannot be empty")
	@Size(min = 0, max = 50, message = "The length of the menu name cannot exceed 50 chars")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@NotBlank(message = "Display order cannot be empty")
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Size(min = 0, max = 200, message = "The requested address cannot exceed 200 chars")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		if (StringUtils.isEmpty(target)) {
			return StringUtils.EMPTY;
		}
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@NotBlank(message = "Menu type cannot be empty")
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getIsRefresh() {
		return isRefresh;
	}

	public void setIsRefresh(String isRefresh) {
		this.isRefresh = isRefresh;
	}

	@Size(min = 0, max = 100, message = "The length of the authorization ID cannot exceed 100 chars")
	public String getPerms() {
		if (StringUtils.isEmpty(perms)) {
			return StringUtils.EMPTY;
		}
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("menuId", getMenuId())
				.append("menuName", getMenuName()).append("parentId", getParentId()).append("orderNum", getOrderNum())
				.append("url", getUrl()).append("target", getTarget()).append("menuType", getMenuType())
				.append("visible", getVisible()).append("perms", getPerms()).append("icon", getIcon())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
