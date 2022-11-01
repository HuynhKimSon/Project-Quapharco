package vn.com.irtech.core.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.core.system.service.ISysConfigService;

/**
 * html calling thymeleaf to implement parameter management
 * 
 * @author admin
 */
@Service("config")
public class ConfigService
{
    @Autowired
    private ISysConfigService configService;

    /**
     * Query parameter configuration information based on key name
     * 
     * @param configKey Parameter key name
     * @return Parameter key value
     */
    public String getKey(String configKey)
    {
        return configService.selectConfigByKey(configKey);
    }
}
