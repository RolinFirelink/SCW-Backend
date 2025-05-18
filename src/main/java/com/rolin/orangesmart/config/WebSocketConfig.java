//package com.rolin.orangesmart.config;
//
///**
// * Author: Rolin
// * Date: 2025/5/18
// * Time: 06:52
// */
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//  @Override
//  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    // 注册 /smtp 端点，并允许所有来源
//    registry.addHandler(new SmtpWebSocketHandler(), "/smtp")
//        .setAllowedOrigins("*");
//  }
//}
//
