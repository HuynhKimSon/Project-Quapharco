package vn.com.irtech.irbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author admin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = { "vn.com.irtech" })
public class IrbotAdminApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(IrbotAdminApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IrbotAdminApplication.class);
	}
}