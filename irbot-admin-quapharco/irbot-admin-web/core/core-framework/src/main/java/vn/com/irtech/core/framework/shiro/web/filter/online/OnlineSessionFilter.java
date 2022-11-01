package vn.com.irtech.core.framework.shiro.web.filter.online;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;

import vn.com.irtech.core.common.constant.ShiroConstants;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.enums.OnlineStatus;
import vn.com.irtech.core.common.utils.spring.SpringUtils;
import vn.com.irtech.core.framework.shiro.session.OnlineSession;
import vn.com.irtech.core.framework.shiro.session.OnlineSessionDAO;
import vn.com.irtech.core.framework.shiro.session.RedisSessionDAO;
import vn.com.irtech.core.framework.util.ShiroUtils;

/**
 * Custom access control
 * 
 * @author admin
 */
public class OnlineSessionFilter extends AccessControlFilter
{
    /**
     * Redirected address after forced exit
     */
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    @Value ( "${spring.redis.enabled}" )
    private  boolean  redisEnabled  =  false ;
    
    @Value("${shiro.session.expireTime}")
    private int expireTime;

    private SessionDAO sessionDao() {
    	if(redisEnabled) {
    		return SpringUtils.getBean(RedisSessionDAO.class);
    	}
    	return SpringUtils.getBean(OnlineSessionDAO.class);
    }
    /**
     * Indicates whether access is allowed; mappedValue is the interceptor parameter part in the [urls] configuration, if access is allowed, return true, otherwise false;
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception
    {
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getSession() == null)
        {
            return true;
        }
        Session session = sessionDao().readSession(subject.getSession().getId());
        if (session != null && session instanceof OnlineSession)
        {
            OnlineSession onlineSession = (OnlineSession) session;
            request.setAttribute(ShiroConstants.ONLINE_SESSION, onlineSession);
            // Set the user object in
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;
            if (isGuest == true)
            {
                LoginUser user = ShiroUtils.getLoginUser();
                if (user != null)
                {
                    onlineSession.setUserId(user.getUserId());
                    onlineSession.setLoginName(user.getLoginName());
					onlineSession.setAvatar(user.getAvatar());
                    // onlineSession.setDeptName(user.getDept().getDeptName());
                    onlineSession.markAttributeChanged();
                }
            }

            if (onlineSession.getStatus() == OnlineStatus.off_line)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Indicates whether it has been processed when the access is denied; if it returns true, it means that processing needs to continue; if it returns false, it means that the interceptor instance has been processed, and it will return directly.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        if (subject != null)
        {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }

    // Jump to login page
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException
    {
        WebUtils.issueRedirect(request, response, loginUrl);
    }
}
