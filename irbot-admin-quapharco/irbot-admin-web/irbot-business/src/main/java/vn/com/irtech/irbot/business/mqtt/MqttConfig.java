package vn.com.irtech.irbot.business.mqtt;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Classname MqttConfig
 * @Description mqtt Related configuration information
 * @Date 2020/3/5 11:00
 * @Created by bam
 */
@Component
@Configuration
public class MqttConfig {

	@Value("${mqtt.host-url}")
	private String hostUrl;
	@Value("${mqtt.username}")
	private String username;
	@Value("${mqtt.password}")
	private String password;
	@Value("${mqtt.qos:0}")
	private Integer qos;
	@Value("${mqtt.clientId:}")
	private String clientId;
	@Value("${mqtt.enabled:}")
	private Boolean enabled;
	// connection configuration
	@Value("${mqtt.maxInFlight:10000}")
	private int maxInFlight;

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getQos() {
		return qos;
	}

	public void setQos(Integer qos) {
		this.qos = qos;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getMaxInFlight() {
		return maxInFlight;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setMaxInFlight(int maxInFlight) {
		this.maxInFlight = maxInFlight;
	}

	private String getConnectClientId() {
		Map<String, String> env = System.getenv();
		if (env.containsKey("COMPUTERNAME"))
			return "IRBot-" + env.get("COMPUTERNAME") + "-" + System.currentTimeMillis();
		else if (env.containsKey("HOSTNAME"))
			return "IRBot-" + env.get("HOSTNAME") + "-" + System.currentTimeMillis();
		else
			return "IRBot-" + System.currentTimeMillis();
	}

	@Bean
	public MqttAsyncClient getMqttClient() throws Exception {
		if (StringUtils.isBlank(clientId)) {
			clientId = getConnectClientId();
		}
		return new MqttAsyncClient(hostUrl, clientId, new MemoryPersistence());
	}
}
