package vn.com.irtech.core.common.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import vn.com.irtech.core.common.utils.ServletUtils;

/**
 * Service related configuration
 * 
 * @author admin
 *
 */
@Component
public class ServerConfig
{
    /**
     * Get the complete request path, including: domain name, port, context access path
     * 
     * @return Service address
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
