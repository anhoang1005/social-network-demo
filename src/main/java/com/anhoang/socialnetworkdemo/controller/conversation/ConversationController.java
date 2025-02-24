package com.anhoang.socialnetworkdemo.controller.conversation;

import com.anhoang.socialnetworkdemo.model.message_chat.ConversationFilter;
import com.anhoang.socialnetworkdemo.payload.socket_payload.MessageData;
import com.anhoang.socialnetworkdemo.service.ConversationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/api/user/conversation/get-all")
    public ResponseEntity<?> userGetAllConversation(@RequestBody ConversationFilter filter){
        return ResponseEntity.ok(conversationService.userGetAllConversation(filter));
    }

    @GetMapping("/api/user/conversation/get-conversation")
    public ResponseEntity<?> userGetConversation(@RequestParam("conversation_id") Long id,
                                                 @RequestParam("page_number") int pageNumber,
                                                 @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(conversationService.userGetMessageOfConversation(id, pageNumber, pageSize));
    }

    @PostMapping("/api/user/conversation/create-private")
    public ResponseEntity<?> userCreatePrivateConversation(@RequestParam("user_code") String userCode){
        return ResponseEntity.ok(conversationService.userCreatePrivateConversation(userCode));
    }

    @PostMapping("/api/user/conversation/create-group")
    public ResponseEntity<?> userCreateGroupConversation(@RequestParam("user_code_list") List<String> userCodeList){
        return ResponseEntity.ok(conversationService.userCreateGroupConversation(userCodeList));
    }

    @PostMapping("/api/user/conversation/send-mess")
    public ResponseEntity<?> userSendMessConversation(@RequestBody MessageData messageData){
        conversationService.userSendMessage(messageData);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/api/user/conversation/seen-mess")
    public ResponseEntity<?> userSeenMessConversation(@RequestBody MessageData messageData){
        conversationService.userSeenMessage(messageData);
        return ResponseEntity.ok("OK");
    }
}
