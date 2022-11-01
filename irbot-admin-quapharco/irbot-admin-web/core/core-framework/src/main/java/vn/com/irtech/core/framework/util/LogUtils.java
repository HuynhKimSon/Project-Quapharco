package vn.com.irtech.core.framework.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.irtech.core.common.json.JSON;
import vn.com.irtech.core.common.utils.IpUtils;

/**
 * Process and record log files
 * 
 * @author admin
 */
public class LogUtils
{
    public static final Logger ERROR_LOG = LoggerFactory.getLogger("sys-error");
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("sys-access");

    /**
     * Record access log [username][jsessionid][ip][accept][UserAgent][url][params][Referer]
     *
     * @param request
     * @throws Exception
     */
    public static void logAccess(HttpServletRequest request) throws Exception
    {
        String username = getUsername();
        String jsessionId = request.getRequestedSessionId();
        String ip = IpUtils.getIpAddr(request);
        String accept = request.getHeader("accept");
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURI();
        String params = getParams(request);

        StringBuilder s = new StringBuilder();
        s.append(getBlock(username));
        s.append(getBlock(jsessionId));
        s.append(getBlock(ip));
        s.append(getBlock(accept));
        s.append(getBlock(userAgent));
        s.append(getBlock(url));
        s.append(getBlock(params));
        s.append(getBlock(request.getHeader("Referer")));
        getAccessLog().info(s.toString());
    }

    /**
     * Record abnormal error format [exception]
     *
     * @param message
     * @param e
     */
    public static void logError(String message, Throwable e)
    {
        String username = getUsername();
        StringBuilder s = new StringBuilder();
        s.append(getBlock("exception"));
        s.append(getBlock(username));
        s.append(getBlock(message));
        ERROR_LOG.error(s.toString(), e);
    }

    /**
     * Record page fault Error log record [page/eception][username][statusCode][errorMessage][servletName][uri][exceptionName][ip][exception]
     *
     * @param request
     */
    public static void logPageError(HttpServletRequest request)
    {
        String username = getUsername();

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");

        if (statusCode == null)
        {
            statusCode = 0;
        }

        StringBuilder s = new StringBuilder();
        s.append(getBlock(t == null ? "page" : "exception"));
        s.append(getBlock(username));
        s.append(getBlock(statusCode));
        s.append(getBlock(message));
        s.append(getBlock(IpUtils.getIpAddr(request)));

        s.append(getBlock(uri));
        s.append(getBlock(request.getHeader("Referer")));
        StringWriter sw = new StringWriter();

        while (t != null)
        {
            t.printStackTrace(new PrintWriter(sw));
            t = t.getCause();
        }
        s.append(getBlock(sw.toString()));
        getErrorLog().error(s.toString());

    }

    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

    protected static String getParams(HttpServletRequest request) throws Exception
    {
        Map<String, String[]> params = request.getParameterMap();
        return JSON.marshal(params);
    }

    protected static String getUsername()
    {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    public static Logger getAccessLog()
    {
        return ACCESS_LOG;
    }

    public static Logger getErrorLog()
    {
        return ERROR_LOG;
    }
}
