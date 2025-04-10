package com.anhoang.socialnetworkdemo.config.websocket;

import com.anhoang.socialnetworkdemo.repository.ConversationMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@AllArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final ConversationMemberRepository conversationMemberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        if (accessor.getCommand()==StompCommand.SUBSCRIBE) {
//            String destination = accessor.getDestination();
//            Principal user = accessor.getUser();
//            if (destination.startsWith("/topic/conversation/")) {
//                String conversationIdString = destination.replace("/topic/conversation/", "");
//                if (!isUserInConversation(user, Long.valueOf(conversationIdString))) {
//                    throw new IllegalArgumentException("Ban khong co quyen subscribe vao topic nay!");
//                } else{
//                    System.out.println("User " + user.getName() + " subscribe to " + destination);
//                }
//            }
//        }
        return message;
    }

    private Boolean isUserInConversation(Principal user, Long conversationId) {
        System.out.println("ok");
        Boolean exists = conversationMemberRepository.existsByConversation_IdAndUsers_Id(conversationId, Long.valueOf(user.getName()));
        return exists!=null ? exists : false;
    }
}
