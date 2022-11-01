package vn.com.irtech.core.framework.shiro.session;

import java.io.Serializable;
import java.util.Date;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import vn.com.irtech.core.common.enums.OnlineStatus;
import vn.com.irtech.core.framework.manager.AsyncManager;
import vn.com.irtech.core.framework.manager.factory.AsyncFactory;
import vn.com.irtech.core.framework.shiro.service.SysShiroService;

/**
 * Db operation for custom ShiroSession
 * 
 * @author admin
 */
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO
{
    /**
     * The period of synchronizing session to database, in milliseconds (default 1 minute)
     */
    @Value("${shiro.session.dbSyncPeriod}")
    private int dbSyncPeriod;

    /**
     * Timestamp of the last time the database was synchronized
     */
    private static final String LAST_SYNC_DB_TIMESTAMP = OnlineSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    @Autowired
    private SysShiroService sysShiroService;

    public OnlineSessionDAO()
    {
        super();
    }

    public OnlineSessionDAO(long expireTime)
    {
        super();
    }

    /**
     * Get the session based on the session ID
     *
     * @param sessionId Session id
     * @return ShiroSession
     */
    @Override
    protected Session doReadSession(Serializable sessionId)
    {
        return sysShiroService.getSession(sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException
    {
        super.update(session);
    }

    /**
     * Update the session; such as update the last access time of the session/stop the session/set the timeout time/set the removal property, etc. will be called
     */
    public void syncToDb(OnlineSession onlineSession)
    {
        Date lastSyncTimestamp = (Date) onlineSession.getAttribute(LAST_SYNC_DB_TIMESTAMP);
        if (lastSyncTimestamp != null)
        {
            boolean needSync = true;
            long deltaTime = onlineSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < dbSyncPeriod * 60 * 1000)
            {
                // The time difference is not enough, no need to synchronize
                needSync = false;
            }
            // isGuest = true Visitors
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;

            // session data has changed synchronization
            if (!isGuest == false && onlineSession.isAttributeChanged())
            {
                needSync = true;
            }

            if (!needSync)
            {
                return;
            }
        }
        // Update the last synchronization database time
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        // Reset the logo after updating
        if (onlineSession.isAttributeChanged())
        {
            onlineSession.resetAttributeChanged();
        }
        AsyncManager.me().execute(AsyncFactory.syncSessionToDb(onlineSession));
    }

    /**
     * When the session expires/stops (such as when the user logs out) properties, etc. will be called
     */
    @Override
    protected void doDelete(Session session)
    {
        OnlineSession onlineSession = (OnlineSession) session;
        if (null == onlineSession)
        {
            return;
        }
        onlineSession.setStatus(OnlineStatus.off_line);
        sysShiroService.deleteSession(onlineSession);
    }
}
