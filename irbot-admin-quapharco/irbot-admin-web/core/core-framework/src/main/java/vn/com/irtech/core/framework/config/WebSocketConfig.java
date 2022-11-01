//package vn.com.irtech.core.framework.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//import vn.com.irtech.core.framework.interceptor.HttpHandshakeInterceptor;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//	
//	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
//
//	@Autowired
//	private HttpHandshakeInterceptor handshakeInterceptor;
//
//	@Value("${websocket.endpoint}")
//	private String endpoint;
//
//	@Value("${websocket.prefix}")
//	private String appDestinationPrefix;
//
//	@Value("${websocket.broker}")
//	private String messageBroker;
//
//	@Override
//	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		logger.info("Websocket - register stomp endpoint: " + this.endpoint);
//		registry.addEndpoint(endpoint).withSockJS().setInterceptors(handshakeInterceptor);
//	}
//
//	@Override
//	public void configureMessageBroker(MessageBrokerRegistry registry) {
//		logger.info("Websocket - register app destination prefix: " + this.appDestinationPrefix);
//		registry.setApplicationDestinationPrefixes(appDestinationPrefix);
//		
//		logger.info("Websocket - register message broker: " + this.messageBroker);
//		registry.enableSimpleBroker(messageBroker);
//	}
//
//}
