package vn.com.irtech.core.common.domain.entity;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * 
 * @author admin
 */
public class SysDictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID", cellType = ColumnType.NUMERIC)
    private Long dictId;

    @Excel(name = "Name")
    private String dictName;

    @Excel(name = "Type")
    private String dictType;

    @Excel(name = "Status", readConverterExp = "0=Active,1=Disabled")
    private String status;

    public Long getDictId()
    {
        return dictId;
    }

    public void setDictId(Long dictId)
    {
        this.dictId = dictId;
    }

    @NotBlank(message = "Dictionary name cannot be empty")
    @Size(min = 0, max = 100, message = "The length of the dictionary type name cannot exceed 100 chars")
    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
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
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
