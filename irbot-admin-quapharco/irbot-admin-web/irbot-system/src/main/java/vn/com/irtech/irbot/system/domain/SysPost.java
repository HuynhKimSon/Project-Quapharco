package vn.com.irtech.irbot.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * 
 * @author admin
 */
public class SysPost extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Excel(name = "岗位序号", cellType = ColumnType.NUMERIC)
	private Long postId;

	@Excel(name = "岗位编码")
	private String postCode;

	@Excel(name = "岗位名称")
	private String postName;

	@Excel(name = "岗位排序", cellType = ColumnType.NUMERIC)
	private String postSort;

	@Excel(name = "状态", readConverterExp = "0=正常,1=停用")
	private String status;

	private boolean flag = false;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	@NotBlank(message = "岗位编码不能为空")
	@Size(min = 0, max = 64, message = "岗位编码长度不能超过64 ký tự")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@NotBlank(message = "岗位名称不能为空")
	@Size(min = 0, max = 50, message = "岗位名称长度不能超过50 ký tự")
	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	@NotBlank(message = "显示顺序不能为空")
	public String getPostSort() {
		return postSort;
	}

	public void setPostSort(String postSort) {
		this.postSort = postSort;
	}

	public String getStatus() {
		return status;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("postId", getPostId())
				.append("postCode", getPostCode()).append("postName", getPostName()).append("postSort", getPostSort())
				.append("status", getStatus()).append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
