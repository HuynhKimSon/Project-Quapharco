package vn.com.irtech.irbot.web.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * Application Config
 *
 * @author admin
 */
@Configuration
// Indicates that the proxy object is exposed through the aop framework, and AopContext can access
@EnableAspectJAutoProxy(exposeProxy = true)
// Specify the path of the package of the Mapper class to be scanned
@MapperScan("vn.com.irtech.irbot.**.mapper")
public class ApplicationConfig {
	@Bean
	public AnnotationMBeanExporter annotationMBeanExporter() {
		AnnotationMBeanExporter annotationMBeanExporter = new AnnotationMBeanExporter();
		annotationMBeanExporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);

		return annotationMBeanExporter;
	}
}
