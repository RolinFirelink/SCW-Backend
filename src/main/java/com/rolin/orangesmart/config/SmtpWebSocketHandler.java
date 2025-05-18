//package com.rolin.orangesmart.config;
//
///**
// * Author: Rolin
// * Date: 2025/5/18
// * Time: 06:53
// */
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//public class SmtpWebSocketHandler extends TextWebSocketHandler {
//
//  @Override
//  public void afterConnectionEstablished(WebSocketSession session) {
//    System.out.println("[服务器] WebSocket连接已建立：" + session.getId());
//    sendMessage(session, "220 smtp.simulator.com ESMTP Service Ready");
//  }
//
//  @Override
//  protected void handleTextMessage(WebSocketSession session, TextMessage message) {
//    String payload = message.getPayload();
//    System.out.println("[服务器接收到命令] " + payload);
//
//    String response;
//    if (payload.startsWith("HELO")) {
//      response = "250 Hello";
//    } else if (payload.startsWith("MAIL FROM:")) {
//      response = "250 OK";
//    } else if (payload.startsWith("RCPT TO:")) {
//      response = "250 OK";
//    } else if (payload.equals("DATA")) {
//      response = "354 End data with <CR><LF>.<CR><LF>";
//    } else if (payload.equals(".")) {
//      response = "250 Message accepted for delivery";
//    } else if (payload.equals("QUIT")) {
//      response = "221 Bye";
//    } else {
//      response = "250 OK";
//    }
//
//    sendMessage(session, response);
//  }
//
//  private void sendMessage(WebSocketSession session, String text) {
//    try {
//      session.sendMessage(new TextMessage(text));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}