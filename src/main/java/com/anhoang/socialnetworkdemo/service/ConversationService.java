package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.model.message_chat.ConversationFilter;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.payload.socket_payload.MessageData;

import java.util.List;

public interface ConversationService {
    //Nguoi dung lay ra trang thai cua cac cuoc tro chuyen
    ResponseBody<?> userGetAllConversation(ConversationFilter filter);

    //Nguoi dung tao ra 1 cuoc tro chuyen rieng tu
    ResponseBody<?> userCreatePrivateConversation(String userCode);

    //Nguoi dung tao ra 1 cuoc tro chuyen nhom
    ResponseBody<?> userCreateGroupConversation(List<String> userCodeList);

    //Nguoi dung lay ra danh sach tin nhan cua cuoc tro chuyen
    ResponseBody<?> userGetMessageOfConversation(Long conversationId, int pageNumber, int pageSize);

    //Nguoi dung gui tin nhan
    MessageData userSendMessage(MessageData req);

    //Nguoi dung xem tin nhan
    MessageData userSeenMessage(MessageData req);

    //Nguoi dung xoa tin nhan
    ResponseBody<?> userRemoveMessage(Long messageId);

    //Lay ra danh sach member cua cuoc tro chuyen
    List<String> systemGetListMemberOfConversation(Long conversationId);
}
