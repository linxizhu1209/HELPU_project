package com.github.backend.Chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    /**
     * enableSimpleBroker: simple message broker 활성화 후 브로커를 타겟으로 하는 하나 이상의 접두사 구성. /topic/... 경로로 메시지를 보내면
     *                    브로커에게 전달 후 이 경로를 구독 중인 구독자들에게 메시지 발송.
     * setApplicationDestinationPrefixes: application annotated method를 타겟으로하는 하나 이상의 접두사 구성. /app/... 경로로 메시지를 보내면
     *                                    @MessageMapping이 붙은 곳을 타겟.
    * */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat");
    }

}