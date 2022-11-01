package vn.com.irtech.core.framework.web.exception;

import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.exception.BusinessException;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.common.utils.security.PermissionUtils;

/**
 * Global exception handler
 * 
 * @author admin
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权Limit verification failed If the request returns json for ajax, the normal request jumps to the page
     */
    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(HttpServletRequest request, AuthorizationException e)
    {
        log.error(e.getMessage(), e);
        if (ServletUtils.isAjaxRequest(request))
        {
            return AjaxResult.error(PermissionUtils.getMsg(e.getMessage()));
        }
        else
        {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error/unauth");
            return modelAndView;
        }
    }

    /**
     * Request method is not supported
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public AjaxResult handleException(HttpRequestMethodNotSupportedException e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error("Do not support get method: ' " + e.getMethod() + "'");
    }

    /**
     * Intercepting unknown runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult notFount(RuntimeException e)
    {
        log.error("Runtime Exception:", e);
        return AjaxResult.error("Runtime Exception:" + e.getMessage());
    }

    /**
     * System exception
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error("Server error, please contact the administrator");
    }

    /**
     * Business abnormal
     */
    @ExceptionHandler(BusinessException.class)
    public Object businessException(HttpServletRequest request, BusinessException e)
    {
        log.error(e.getMessage(), e);
        if (ServletUtils.isAjaxRequest(request))
        {
            return AjaxResult.error(e.getMessage());
        }
        else
        {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("error/business");
            return modelAndView;
        }
    }

    /**
     * Custom validation exception
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult validatedBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

}
