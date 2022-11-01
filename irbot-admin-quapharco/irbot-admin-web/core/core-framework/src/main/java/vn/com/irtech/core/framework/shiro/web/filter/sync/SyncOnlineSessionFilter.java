package vn.com.irtech.core.framework.shiro.web.filter.sync;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import vn.com.irtech.core.common.constant.ShiroConstants;
import vn.com.irtech.core.framework.shiro.session.OnlineSession;
import vn.com.irtech.core.framework.shiro.session.OnlineSessionDAO;

/**
 * Synchronize session data to Db
 * 
 * @author admin
 */
public class SyncOnlineSessionFilter extends PathMatchingFilter
{
    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * Synchronize the session data to the DB. A request can be synchronized at most once to prevent excessive processing. It needs to be placed before the Shiro filter
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        OnlineSession session = (OnlineSession) request.getAttribute(ShiroConstants.ONLINE_SESSION);
        // If the session is stopped, it will not be synchronized
        // Session stop time, if stopTimestamp is not null, it means it has stopped
        if (session != null && session.getUserId() != null && session.getStopTimestamp() == null)
        {
            onlineSessionDAO.syncToDb(session);
        }
        return true;
    }
}
