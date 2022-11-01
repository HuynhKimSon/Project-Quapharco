package vn.com.irtech.core.common.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * 字典数据表 sys_dict_data
 * 
 * @author admin
 */
public class SysDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "Code", cellType = ColumnType.NUMERIC)
    private Long dictCode;

    @Excel(name = "Sort", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    @Excel(name = "Label")
    private String dictLabel;

    @Excel(name = "Value")
    private String dictValue;

    @Excel(name = "Type")
    private String dictType;

    @Excel(name = "Css Class")
    private String cssClass;

    private String listClass; 

    @Excel(name = "Default", readConverterExp = "Y=Yes,N=No")
    private String isDefault;

    @Excel(name = "Status", readConverterExp = "0=Active,1=Disabled")
    private String status;

    public Long getDictCode()
    {
        return dictCode;
    }

    public void setDictCode(Long dictCode)
    {
        this.dictCode = dictCode;
    }

    public Long getDictSort()
    {
        return dictSort;
    }

    public void setDictSort(Long dictSort)
    {
        this.dictSort = dictSort;
    }

    @NotBlank(message = "Dictionary tag cannot be empty")
	@Size(min = 0, max = 5000, message = "Dictionary label length cannot exceed 500 chars")
    public String getDictLabel()
    {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel)
    {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message = "Dictionary key value cannot be empty")
    @Size(min = 0, max = 100, message = "The length of dictionary keys cannot exceed 100 chars")
    public String getDictValue()
    {
        return dictValue;
    }

    public void setDictValue(String dictValue)
    {
        this.dictValue = dictValue;
    }

    @NotBlank(message = "Dictionary type cannot be empty")
    @Size(min = 0, max = 100, message = "The length of the dictionary type cannot exceed 100 chars")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    @Size(min = 0, max = 100, message = "The style attribute length cannot exceed 100 chars")
    public String getCssClass()
    {
        return cssClass;
    }

    public void setCssClass(String cssClass)
    {
        this.cssClass = cssClass;
    }

    public String getListClass()
    {
        return listClass;
    }

    public void setListClass(String listClass)
    {
        this.listClass = listClass;
    } 

	public boolean getDefault()
    {
        return UserConstants.YES.equals(this.isDefault) ? true : false;
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE) 
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
