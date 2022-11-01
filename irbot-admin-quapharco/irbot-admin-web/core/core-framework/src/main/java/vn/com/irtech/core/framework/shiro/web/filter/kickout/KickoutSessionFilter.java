package vn.com.irtech.core.framework.shiro.web.filter.kickout;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.core.common.constant.ShiroConstants;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;

/**
 * Login account control filter
 * 
 * @author admin
 */
public class KickoutSessionFilter extends AccessControlFilter
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Maximum number of sessions for the same user
     **/
    private int maxSession = -1;

    /**
     * Kick out users who have logged in before/after logging in. Default false Kick out users who have logged in before
     **/
    private Boolean kickoutAfter = false;

    /**
     * Address after kicked out
     **/
    private String kickoutUrl;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o)
            throws Exception
    {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered() || maxSession == -1)
        {
            // If there is no login or the maximum number of user sessions is -1, proceed directly to the following process
            return true;
        }
        try
        {
            Session session = subject.getSession();
            // Currently logged in user
            LoginUser user = ShiroUtils.getLoginUser();
            String loginName = user.getLoginName();
            Serializable sessionId = session.getId();

            // Read the cache user and save it if not
            Deque<Serializable> deque = cache.get(loginName);
            if (deque == null)
            {
                // Initialize the queue
                deque = new ArrayDeque<Serializable>();
            }

            // If there is no such sessionId in the queue and the user has not been kicked out; put it in the queue
            if (!deque.contains(sessionId) && session.getAttribute("kickout") == null)
            {
                // Store sessionId in the queue
                deque.push(sessionId);
                // Cache the user's sessionId queue
                cache.put(loginName, deque);
            }

            // If the number of sessionIds in the queue exceeds the maximum number of sessions, start kicking people
            while (deque.size() > maxSession)
            {
                Serializable kickoutSessionId = null;
                // Whether to kick out those who log in later, the default is false; that is, the user who logs in in the latter kicks out the user who logs in in the former;
                if (kickoutAfter)
                {
                    // Kick out the latter
                    kickoutSessionId = deque.removeFirst();
                }
                else
                {
                    // Kick the former
                    kickoutSessionId = deque.removeLast();
                }
                // Update the cache queue after kicking out
                cache.put(loginName, deque);

                try
                {
                    // Get the session object of the kicked sessionId
                    Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (null != kickoutSession)
                    {
                        // Set the kickout attribute of the session to indicate a kickout
                        kickoutSession.setAttribute("kickout", true);
                    }
                }
                catch (Exception e)
                {
                    // Faced with exceptions, we choose to ignore
                }
            }

            // If you are kicked out, (the former or the latter) exit directly and redirect to the address after kicking out
            if ((Boolean) session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout") == true)
            {
                // sign out
                subject.logout();
                saveRequest(request);
                return isAjaxResponse(request, response);
            }
            return true;
        }
        catch (Exception e)
        {
            return isAjaxResponse(request, response);
        }
    }

    private boolean isAjaxResponse(ServletRequest request, ServletResponse response) throws IOException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (ServletUtils.isAjaxRequest(req))
        {
            AjaxResult ajaxResult = AjaxResult.error("You are logged in elsewhere, please change your password or log in again");
            ServletUtils.renderString(res, objectMapper.writeValueAsString(ajaxResult));
        }
        else
        {
            WebUtils.issueRedirect(request, response, kickoutUrl);
        }
        return false;
    }

    public void setMaxSession(int maxSession)
    {
        this.maxSession = maxSession;
    }

    public void setKickoutAfter(boolean kickoutAfter)
    {
        this.kickoutAfter = kickoutAfter;
    }

    public void setKickoutUrl(String kickoutUrl)
    {
        this.kickoutUrl = kickoutUrl;
    }

    public void setSessionManager(SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    // Set the prefix of the cache key
    public void setCacheManager(CacheManager cacheManager)
    {
        // Must be consistent with the cache name in the ehcache cache configuration
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
