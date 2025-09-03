package com.yunesh.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Simple in-memory broker
        config.enableSimpleBroker("/topic", "/queue"); // /topic = broadcast, /queue = private
        config.setApplicationDestinationPrefixes("/app"); // prefix for client → server messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // WebSocket endpoint clients connect to
                .setAllowedOriginPatterns("*") // allow all origins for dev
                .withSockJS(); // fallback for browsers without native WebSocket
    }
}
