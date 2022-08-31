package com.anabada.anabadaBackend.config;

import com.anabada.anabadaBackend.common.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket") //소켓 연결
                .setAllowedOrigins("http://localhost:8080")
                .setAllowedOrigins("http://127.0.0.1:8080")
                .setAllowedOrigins("http://localhost:3000")
                .setAllowedOrigins("http://127.0.0.1:3000")
                .setAllowedOriginPatterns("*")
                .withSockJS(); //소켓 지원하지 않는 브라우저는 sockJS 사용하도록 설정.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub", "/topic");
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        // StompHandler 가 Websocket 앞단에서 token 을 체크할 수 있도록 인터셉터로 설정
        registration.interceptors(stompHandler);
    }

}
