package vn.com.irtech.core.system.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.domain.BaseEntity;
import vn.com.irtech.core.common.enums.OnlineStatus;

/**
 * Current online session sys_user_online
 * 
 * @author admin
 */
public class SysUserOnline extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    /** User session id */
    private String sessionId;

    /** Department name*/
    private String deptName;

    /** Login name */
    private String loginName;

    /** Login IP address */
    private String ipaddr;

    /** Login address*/
    private String loginLocation;

    /** Browser type */
    private String browser;

    /** operating system */
    private String os;

    /** session creation time */
    private Date startTimestamp;

    /** session last access time */
    private Date lastAccessTime;

    /** Timeout time, in minutes */
    private Long expireTime;

    /** online status */
    private OnlineStatus status = OnlineStatus.on_line;

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public Date getStartTimestamp()
    {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp)
    {
        this.startTimestamp = startTimestamp;
    }

    public Date getLastAccessTime()
    {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime)
    {
        this.lastAccessTime = lastAccessTime;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public OnlineStatus getStatus()
    {
        return status;
    }

    public void setStatus(OnlineStatus status)
    {
        this.status = status;
    }
    
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sessionId", getSessionId())
            .append("loginName", getLoginName())
            .append("deptName", getDeptName())
            .append("ipaddr", getIpaddr())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("startTimestamp", getStartTimestamp())
            .append("lastAccessTime", getLastAccessTime())
            .append("expireTime", getExpireTime())
            .toString();
    }
}
