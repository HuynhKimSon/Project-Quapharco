package vn.com.irtech.core.quartz.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.annotation.Excel.ColumnType;
import vn.com.irtech.core.common.constant.ScheduleConstants;
import vn.com.irtech.core.common.domain.BaseEntity;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.quartz.util.CronUtils;

/**
 * 定时任务调度表 sys_job
 * 
 * @author admin
 */
public class SysJob extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @Excel(name = "Job ID", cellType = ColumnType.NUMERIC)
    private Long jobId;

    /** 任务名称 */
    @Excel(name = "Job Name")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "Job Group")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "Invoke Target")
    private String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "Cron Exp ")
    private String cronExpression;

    /** cron计划策略 */
    @Excel(name = "Policy", readConverterExp = "0=Default,1=Execute Immediately,2=Execute One,3=Execute Without Trigger")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** 是否并发执行（0允许 1禁止） */
    @Excel(name = "Concurrent", readConverterExp = "0=Allow,1=Not Allow")
    private String concurrent;

    /** 任务状态（0正常 1暂停） */
    @Excel(name = "Status", readConverterExp = "0=Normal,1=Timeout")
    private String status;

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    @NotBlank(message = "Task name cannot be empty")
    @Size(min = 0, max = 64, message = "Task name cannot exceed 64 characters")
    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobGroup()
    {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    @NotBlank(message = "Call target string cannot be empty")
    @Size(min = 0, max = 1000, message = "Call target string length cannot exceed 500 characters")
    public String getInvokeTarget()
    {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    @NotBlank(message = "Cron execution expression cannot be empty")
    @Size(min = 0, max = 255, message = "Cron execution expression cannot exceed 255 characters")
    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    public Date getNextValidTime()
    {
        if (StringUtils.isNotEmpty(cronExpression))
        {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    public String getMisfirePolicy()
    {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy)
    {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent()
    {
        return concurrent;
    }

    public void setConcurrent(String concurrent)
    {
        this.concurrent = concurrent;
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
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("cronExpression", getCronExpression())
            .append("nextValidTime", getNextValidTime())
            .append("misfirePolicy", getMisfirePolicy())
            .append("concurrent", getConcurrent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}