package vn.com.irtech.core.system.domain;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * System config
 * @author admin
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "Config ID", cellType = ColumnType.NUMERIC)
    private Long configId;

    @Excel(name = "Config Name")
    private String configName;

    @Excel(name = "Config Key")
    private String configKey;

    @Excel(name = "Config Value")
    private String configValue;

    @Excel(name = "System Default", readConverterExp = "Y=Yes,N=No")
    private String configType;

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    @NotBlank(message = "Parameter name cannot be empty")
    @Size(min = 0, max = 200, message = "Độ dài chuỗi không được vượt quá 100 ký tự")
    public String getConfigName()
    {
        return configName;
    }

    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    @NotBlank(message = "Parameter key name length cannot be empty")
    @Size(min = 0, max = 200, message = "Độ dài chuỗi không được vượt quá 100 ký tự")
    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    @NotBlank(message = "Parameter key cannot be empty")
    @Size(min = 0, max = 3000, message = "Độ dài chuỗi không được vượt quá 3000 ký tự")
    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
