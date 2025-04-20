package com.anhoang.socialnetworkdemo.controller;

import com.anhoang.socialnetworkdemo.payload.socket_payload.MessageData;
import com.anhoang.socialnetworkdemo.payload.socket_payload.SignalData;
import com.anhoang.socialnetworkdemo.payload.socket_payload.SocketBody;
import com.anhoang.socialnetworkdemo.service.ConversationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class PrivateChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationService conversationService;
    private final ObjectMapper objectMapper;

    @MessageMapping("/chat.sendPrivate/{conversation_topic}")
    @SendTo("/topic/conversation/{conversation_topic}")
    public MessageData sendMessageToPrivateConversation(@Payload MessageData message,
                                                        @DestinationVariable("conversation_topic") String conversationTopic,
                                                        SimpMessageHeaderAccessor accessor) {
        String userCode = accessor.getSessionAttributes().get("user_code").toString();
        message.setUserCode(userCode);
        System.out.println("Message: " + message.toString());
        if(message.getType().name().equals("SENT")){
            message = conversationService.userSendMessage(message);
        } else if(message.getType().name().equals("SEEN")){
            message = conversationService.userSeenMessage(message);
        } else{
            System.out.println("chat");
        }
        List<String> userMembers = conversationService.systemGetListMemberOfConversation(Long.valueOf(conversationTopic));
        SocketBody<?> messageSocket = SocketBody.builder()
                .type(SocketBody.Type.MESSAGE)
                .body(message).createdAt(null).build();
        for (String member : userMembers) {
            if(!member.equals(userCode)) {
                messagingTemplate.convertAndSendToUser(member, "/queue/private", messageSocket);
            }
        }
        //sendMessageToUser(userCode, message.toString(), userMembers);
        return message;
    }


    @MessageMapping("/chat.sendSignal/{conversation_topic}")
    public SignalData sendCallSignalingData(@Payload SignalData signal,
                                            @DestinationVariable("conversation_topic") String conversationTopic,
                                            SimpMessageHeaderAccessor accessor) {
        String userCode = accessor.getSessionAttributes().get("user_code").toString();
        SocketBody<?> messageSocket = SocketBody.builder()
                .type(SocketBody.Type.CALL)
                .body(signal).createdAt(null).build();
        List<String> userMembers = conversationService.systemGetListMemberOfConversation(Long.valueOf(conversationTopic));
        if (signal.getType()== SignalData.Type.OFFER){
            for (String member : userMembers) {
                if(!member.equals(userCode)) {
                    messagingTemplate.convertAndSendToUser(member, "/queue/private", messageSocket);
                }
            }
        } else if(signal.getType()== SignalData.Type.ANSWER){
            for (String member : userMembers) {
                if(!member.equals(userCode)) {
                    messagingTemplate.convertAndSendToUser(member, "/queue/private", messageSocket);
                }
            }
        } else{
            for (String member : userMembers) {
                if(!member.equals(userCode)) {
                    messagingTemplate.convertAndSendToUser(member, "/queue/private", messageSocket);
                }
            }
        }
        return signal;
    }
    //Lam sao de khi nhung nguoi da chon chap nhan cuoc goi thi se chuyen den 1 topic rieng de lang nghe cuoc goi

}
