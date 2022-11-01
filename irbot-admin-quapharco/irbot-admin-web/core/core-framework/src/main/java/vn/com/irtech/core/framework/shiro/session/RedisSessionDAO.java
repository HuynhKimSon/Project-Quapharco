package vn.com.irtech.core.framework.shiro.session;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import vn.com.irtech.core.common.enums.OnlineStatus;
import vn.com.irtech.core.system.service.impl.SysUserOnlineServiceImpl;
import vn.com.irtech.core.framework.manager.AsyncManager;
import vn.com.irtech.core.framework.manager.factory.AsyncFactory;

/**
 * 
 * @author admin
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisSessionDAO extends EnterpriseCacheSessionDAO
{
    /**
     * The period of synchronizing session to database, in milliseconds (default 1 minute)
     */
    @Value("${shiro.session.dbSyncPeriod}")
    private int dbSyncPeriod;

    /**
     * Session expires in redis
     */
    private int expireTime;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SysUserOnlineServiceImpl onlineService;

    /**
     * shiro redis prefix
     */
    private final static String SYS_SHIRO_SESSION_ID = "admin_session:";

    /**
     * Timestamp of the last time the database was synchronized
     */
    private static final String LAST_SYNC_DB_TIMESTAMP = RedisSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    public void setExpireTime(int expireTime)
    {
        this.expireTime = expireTime;
    }

    /**
     * Get the session according to the session ID, first get the session from the cache
     * @param sessionId Session id
     * @return Session
     */
    @Override
    protected Session doReadSession(Serializable sessionId)
    {
        String key = SYS_SHIRO_SESSION_ID + sessionId;
        System.out.println("key:::"+key);
        Object obj = redisTemplate.opsForValue().get(key);
        OnlineSession session = (OnlineSession)obj ;
        return session;
    }

    /**
     * Create session
     *
     * @param session Session information
     * @return Serializable
     */
    @Override
    protected Serializable doCreate(Session session)
    {
        Serializable sessionId = generateSessionId(session);
        this.assignSessionId(session, sessionId);
        String key = SYS_SHIRO_SESSION_ID + sessionId;
        redisTemplate.opsForValue().set(key, session);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return sessionId;
    }

    // Update session
    @Override
    protected void doUpdate(Session session)
    {
        // No need to update if the session expires/stops
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid())
        {
            return;
        }
        super.doUpdate(session);
        if (session != null && session.getId() != null)
        {
            String key = SYS_SHIRO_SESSION_ID + session.getId();
            redisTemplate.opsForValue().set(key, session);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
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
        String key = SYS_SHIRO_SESSION_ID + session.getId();
        boolean result = redisTemplate.delete(key);
        if (result)
        {
            onlineSession.setStatus(OnlineStatus.off_line);
            onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
        }
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
                // Insufficient time difference, no need to synchronize
                needSync = false;
            }
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;

            // The session data has changed. Synchronization
            if (isGuest == false && onlineSession.isAttributeChanged())
            {
                needSync = true;
            }

            if (needSync == false)
            {
                return;
            }
        }
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        // After updating, reset the logo
        if (onlineSession.isAttributeChanged())
        {
            onlineSession.resetAttributeChanged();
        }
        AsyncManager.me().execute(AsyncFactory.syncSessionToDb(onlineSession));
        String key = SYS_SHIRO_SESSION_ID + onlineSession.getId();
        redisTemplate.opsForValue().set(key, onlineSession);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }
}
