package com.anhoang.socialnetworkdemo.controller;

import com.anhoang.socialnetworkdemo.payload.socket_payload.MessageData;
import com.anhoang.socialnetworkdemo.payload.socket_payload.SocketBody;
import com.anhoang.socialnetworkdemo.service.ConversationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class PrivateChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationService conversationService;
    private final ObjectMapper objectMapper;

    @MessageMapping("/chat.sendPrivate/{conversation_topic}")
    @SendTo("/topic/private/{conversation_topic}")
    public SocketBody<?> sendMessageToPrivateConversation(@Payload SocketBody<?> body,
                                        @DestinationVariable String conversationTopic) {
        if(body.getType().name().equals("SENT")){
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            conversationService.userSendMessage(message);
        } else if(body.getType().name().equals("SEEN")){
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            conversationService.userSeenMessage(message);
        }
        return body;
    }

    @MessageMapping("/chat.sendGroup/{conversation_topic}")
    @SendTo("/topic/group/{conversation_topic}")
    public SocketBody<?> sendMessageToCustomer(@Payload SocketBody<?> body,
                                               @DestinationVariable String conversationTopic) {
        if(body.getType().name().equals("SENT")) {
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            conversationService.userSendMessage(message);
        } else if(body.getType().name().equals("SEEN")){
            MessageData message = objectMapper.convertValue(body.getBody(), MessageData.class);
            conversationService.userSeenMessage(message);
        }
        return body;
    }

}
