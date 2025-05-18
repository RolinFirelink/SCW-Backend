//package com.rolin.orangesmart.service;
//
//import javax.websocket.*;
//import java.net.URI;
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import java.net.URI;
//import java.util.Arrays;
//import java.util.List;
//
//@ClientEndpoint
//@Component
//public class SmtpWebSocketClient implements CommandLineRunner {
//
//  private Session session;
//  private int commandIndex = 0;
//  private final List<String> smtpCommands = Arrays.asList(
//      "HELO mydomain.com",
//      "MAIL FROM:<test@mine.com>",
//      "RCPT TO:<test@nullht.com>",
//      "DATA",
//      "Hello World",
//      ".",
//      "QUIT"
//  );
//
//  private static final String WS_URL = "ws://localhost:8080/smtp";
//
//  @Override
//  public void run(String... args) {
//    connect();
//  }
//
//  public void connect() {
//    try {
//      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//      container.connectToServer(this, URI.create(WS_URL));
//    } catch (Exception e) {
//      System.err.println("连接失败，1 秒后重试...");
//      reconnect();
//    }
//  }
//
//  @OnOpen
//  public void onOpen(Session session) {
//    System.out.println("[WebSocket] 连接已建立");
//    this.session = session;
//    this.commandIndex = 0;
//    sendNextCommand();
//  }
//
//  @OnMessage
//  public void onMessage(String message) {
//    System.out.println("[服务器回应] " + message);
//    if (message.startsWith("250") || message.startsWith("354") || message.startsWith("221")) {
//      sendNextCommand();
//    }
//  }
//
//  @OnClose
//  public void onClose(Session session, CloseReason reason) {
//    System.out.println("[WebSocket] 连接关闭，原因：" + reason.getReasonPhrase());
//    reconnect();
//  }
//
//  @OnError
//  public void onError(Session session, Throwable thr) {
//    System.err.println("[WebSocket 错误] " + thr.getMessage());
//  }
//
//  private void sendNextCommand() {
//    if (commandIndex < smtpCommands.size()) {
//      String cmd = smtpCommands.get(commandIndex);
//      System.out.println("[发送命令] " + cmd);
//      session.getAsyncRemote().sendText(cmd);
//      commandIndex++;
//    }
//  }
//
//  private void reconnect() {
//    try {
//      Thread.sleep(1000);
//      connect();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
//}