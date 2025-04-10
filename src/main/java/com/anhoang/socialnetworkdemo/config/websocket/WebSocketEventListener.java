package com.anhoang.socialnetworkdemo.config.websocket;

import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WebSocketEventListener {
    private final Map<String, LocalDateTime> onlineUser = new ConcurrentHashMap<>();

    private final SimpMessageSendingOperations messagingTemplate;
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        SimpMessageHeaderAccessor accessor1 = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String header = accessor.toString();
        Pattern patternUser = Pattern.compile("user_code=(\\w+)");
        Matcher matcherUser = patternUser.matcher(header);
        String userCode;
        if (matcherUser.find()) {
            userCode = matcherUser.group(1);
            onlineUser.put(userCode, LocalDateTime.now());
            log.info(userCode + " connected at "
                    + TimeMapperUtils.localDateTimeToString(LocalDateTime.now()));
        }
        //messagingTemplate.convertAndSend("/topic/customer/status", onlineUser);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String header = accessor.toString();
        Pattern patternUser = Pattern.compile("user_code=(\\w+)");
        Matcher matcherUser = patternUser.matcher(header);
        String userCode;
        if (matcherUser.find()) {
            userCode = matcherUser.group(1);
            onlineUser.remove(userCode);
            //messagingTemplate.convertAndSend("/topic/customer/status", onlineUser);
            log.info(userCode + " disconnected at "
                    + TimeMapperUtils.localDateTimeToString(LocalDateTime.now()));
        }
    }

    public boolean checkCustomerConnection(String userCode){
        return onlineUser.containsKey(userCode);
    }
}
