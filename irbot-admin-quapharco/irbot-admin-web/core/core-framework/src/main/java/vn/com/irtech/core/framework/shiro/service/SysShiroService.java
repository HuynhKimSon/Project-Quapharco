package vn.com.irtech.core.framework.shiro.service;

import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.system.domain.SysUserOnline;
import vn.com.irtech.core.system.service.ISysUserOnlineService;
import vn.com.irtech.core.framework.shiro.session.OnlineSession;

/**
 * Session db operation processing
 * 
 * @author admin
 */
@Component
public class SysShiroService
{
    @Autowired
    private ISysUserOnlineService onlineService;

    /**
     *
     */
    public void deleteSession(OnlineSession onlineSession)
    {
        onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
    }

    /**
     *
     * @param sessionId
     * @return
     */
    public Session getSession(Serializable sessionId)
    {
        SysUserOnline userOnline = onlineService.selectOnlineById(String.valueOf(sessionId));
        return StringUtils.isNull(userOnline) ? null : createSession(userOnline);
    }

    public Session createSession(SysUserOnline userOnline)
    {
        OnlineSession onlineSession = new OnlineSession();
        if (StringUtils.isNotNull(userOnline))
        {
            onlineSession.setId(userOnline.getSessionId());
            onlineSession.setHost(userOnline.getIpaddr());
            onlineSession.setBrowser(userOnline.getBrowser());
            onlineSession.setOs(userOnline.getOs());
            onlineSession.setDeptName(userOnline.getDeptName());
            onlineSession.setLoginName(userOnline.getLoginName());
            onlineSession.setStartTimestamp(userOnline.getStartTimestamp());
            onlineSession.setLastAccessTime(userOnline.getLastAccessTime());
            if(userOnline.getExpireTime() != null) {
            	// TODO Check default timeout here
            	onlineSession.setTimeout(userOnline.getExpireTime());
            }
        }
        return onlineSession;
    }
}
