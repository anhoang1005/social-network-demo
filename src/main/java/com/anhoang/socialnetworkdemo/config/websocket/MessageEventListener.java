//package com.anhoang.socialnetworkdemo.config.websocket;
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionSubscribeEvent;
//
////Kiem tra xem co tin nhan gui den khong
//@Component
//public class MessageEventListener implements ApplicationListener<SessionSubscribeEvent> {
//
//    @Override
//    public void onApplicationEvent(SessionSubscribeEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        String destination = accessor.getDestination();
//
//        if (destination.startsWith("/user/")) {
//            System.out.println("📩 Server thấy có tin nhắn gửi đến: " + destination);
//        }
//    }
//}
