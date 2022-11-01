package vn.com.irtech.core.framework.shiro.session;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.session.mgt.SimpleSession;

import vn.com.irtech.core.common.enums.OnlineStatus; 

/**
 * Online user session attributes
 * 
 * @author admin
 */
public class OnlineSession extends SimpleSession
{
    private static final long serialVersionUID = 1L;

    /** User ID */
    private Long userId;

    /** user name */
    private String loginName;

    /** Department name */
    private String deptName;
	
	/** profile picture */
	private String avatar;

    /** Login IP address */
    private String host;

    /** Browser type */
    private String browser;

    /** operating system */
    private String os;

    /** online status */
    private OnlineStatus status = OnlineStatus.on_line;

    /** Whether the attribute is changed to optimize session data synchronization */
    private transient boolean attributeChanged = false;

    @Override
    public String getHost()
    {
        return host;
    }

    @Override
    public void setHost(String host)
    {
        this.host = host;
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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public OnlineStatus getStatus()
    {
        return status;
    }

    public void setStatus(OnlineStatus status)
    {
        this.status = status;
    }

    public void markAttributeChanged()
    {
        this.attributeChanged = true;
    }

    public void resetAttributeChanged()
    {
        this.attributeChanged = false;
    }

    public boolean isAttributeChanged()
    {
        return attributeChanged;
    }

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
    @Override
    public void setAttribute(Object key, Object value)
    {
        super.setAttribute(key, value);
    }

    @Override
    public Object removeAttribute(Object key)
    {
        return super.removeAttribute(key);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("loginName", getLoginName())
            .append("deptName", getDeptName())
            .append("avatar", getAvatar())
            .append("host", getHost())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("attributeChanged", isAttributeChanged())
            .toString();
    }
}
