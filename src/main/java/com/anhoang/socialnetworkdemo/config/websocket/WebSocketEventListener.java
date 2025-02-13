package com.anhoang.socialnetworkdemo.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WebSocketEventListener {
    private final Set<String> onlineUser = ConcurrentHashMap.newKeySet();

//    private final SimpMessageSendingOperations messagingTemplate;
//    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        String header = event.getMessage().getHeaders().toString();
        Pattern patternUser = Pattern.compile("userCode=(\\w+)");
        Matcher matcherUser = patternUser.matcher(header);
        String userCode;
        if (matcherUser.find()) {
            userCode = matcherUser.group(1);
            onlineUser.add(userCode);
            log.info("User connected: " + userCode);
        }
        //messagingTemplate.convertAndSend("/topic/customer/status", onlineUser);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String header = event.getMessage().getHeaders().toString();
        Pattern patternUser = Pattern.compile("userCode=(\\w+)");
        Matcher matcherUser = patternUser.matcher(header);
        String userCode;
        if (matcherUser.find()) {
            userCode = matcherUser.group(1);
            onlineUser.remove(userCode);
            //messagingTemplate.convertAndSend("/topic/customer/status", onlineUser);
            log.info("User disconnected: " + userCode);
        }
    }

    public boolean checkCustomerConnection(String userCode){
        return onlineUser.contains(userCode);
    }
}
