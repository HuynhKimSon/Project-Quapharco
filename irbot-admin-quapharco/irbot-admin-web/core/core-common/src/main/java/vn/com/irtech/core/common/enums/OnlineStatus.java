package vn.com.irtech.core.common.enums;

/**
 * User session
 * 
 * @author admin
 */
public enum OnlineStatus
{
    /** user status */
    on_line("Online"), off_line("Offline");

    private final String info;

    private OnlineStatus(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }
}
