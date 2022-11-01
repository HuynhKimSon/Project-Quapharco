package vn.com.irtech.core.framework.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.core.framework.shiro.web.session.SpringSessionValidationScheduler;

import javax.annotation.PreDestroy;

/**
 *
 * @author 
 */
@Component
public class ShutdownManager
{
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @Autowired(required = false)
    private SpringSessionValidationScheduler springSessionValidationScheduler;

    @PreDestroy
    public void destroy()
    {
        shutdownSpringSessionValidationScheduler();
        shutdownAsyncManager();
    }

    /**
     * Stop session check
     */
    private void shutdownSpringSessionValidationScheduler()
    {
        if (springSessionValidationScheduler != null && springSessionValidationScheduler.isEnabled())
        {
            try
            {
                logger.info("====Close session verification task====");
                springSessionValidationScheduler.disableSessionValidation();
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     */
    private void shutdownAsyncManager()
    {
        try
        {
            logger.info("====Close background task thread pool====");
            AsyncManager.me().shutdown();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}
