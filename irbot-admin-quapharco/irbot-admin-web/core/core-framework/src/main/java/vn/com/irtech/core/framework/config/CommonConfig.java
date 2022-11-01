package vn.com.irtech.core.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Application Config
 *
 * @author admin
 */
@Configuration
// Indicates that the proxy object is exposed through the aop framework, and AopContext can access
@EnableAspectJAutoProxy(exposeProxy = true)
// Specify the path of the package of the Mapper class to be scanned
@MapperScan("vn.com.irtech.core.**.mapper")
public class CommonConfig {
    // TODO check package here
}
