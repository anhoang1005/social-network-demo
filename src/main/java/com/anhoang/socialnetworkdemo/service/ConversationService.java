package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.model.message_chat.ConversationFilter;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.payload.socket_payload.MessageData;

import java.util.List;

public interface ConversationService {
    ResponseBody<?> userGetAllConversation(ConversationFilter filter);
    ResponseBody<?> userCreatePrivateConversation(String userCode);
    ResponseBody<?> userCreateGroupConversation(List<String> userCodeList);
    ResponseBody<?> userGetMessageOfConversation(Long conversationId, int pageNumber, int pageSize);
    void userSendMessage(MessageData req);
    void userSeenMessage(MessageData req);
    ResponseBody<?> userRemoveMessage(Long messageId);
}
