package vn.com.irtech.irbot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Notification Object sys_notification
 * 
 * @author
 * @date 2020-08-22
 */
public class SysNotification extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Long id;

	/** Title */
	@Excel(name = "Title")
	private String title;

	/** Content */
	@Excel(name = "Content")
	private String content;

	/** Level */
	@Excel(name = "Level")
	private Long notifyLevel;

	/** Link */
	@Excel(name = "Link")
	private String notifyLink;

	/** Status */
	@Excel(name = "Status")
	private Long status;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setNotifyLevel(Long notifyLevel) {
		this.notifyLevel = notifyLevel;
	}

	public Long getNotifyLevel() {
		return notifyLevel;
	}

	public void setNotifyLink(String notifyLink) {
		this.notifyLink = notifyLink;
	}

	public String getNotifyLink() {
		return notifyLink;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("title", getTitle()).append("content", getContent()).append("notifyLevel", getNotifyLevel())
				.append("notifyLink", getNotifyLink()).append("status", getStatus())
				.append("createTime", getCreateTime()).append("createBy", getCreateBy())
				.append("updateTime", getUpdateTime()).append("updateBy", getUpdateBy()).toString();
	}
}
