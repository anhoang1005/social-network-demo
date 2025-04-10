package com.anhoang.socialnetworkdemo.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final WebSocketChannelInterceptor webSocketInterceptor;
    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor,
                           WebSocketChannelInterceptor webSocketInterceptor) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
        this.webSocketInterceptor = webSocketInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // client subscribe
        config.setApplicationDestinationPrefixes("/app"); // prefix cho cac destination gui tu client
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(new UserHandshakeHandler()) //
                .setAllowedOrigins("http://127.0.0.1:5500", "http://127.0.0.1:5501", "http://127.0.0.1:3000")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(2048 * 1024); // 512KB
        registry.setSendBufferSizeLimit(2048 * 1024); // 1MB
        registry.setSendTimeLimit(20 * 1000); // 20 seconds
    }
}