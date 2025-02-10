package com.app.commentservice.configuration;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.app.commentservice.socket.CommentWebSocketHandler;




@Configuration
public class WebSocketConfig {

    private final CommentWebSocketHandler commentWebSocketHandler;

    public WebSocketConfig(CommentWebSocketHandler commentWebSocketHandler) {
        this.commentWebSocketHandler = commentWebSocketHandler;
    }

    @Bean
     HandlerMapping handlerMapping() {
        return new SimpleUrlHandlerMapping(Map.of(
                "/ws/comments", commentWebSocketHandler
        ), 1);
    }

    @Bean
     WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
