package com.anhoang.socialnetworkdemo.model.message_chat;

import com.anhoang.socialnetworkdemo.entity.Conversation;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationDto {
    @JsonProperty("conversation_id")
    private Long conversationId;
    @JsonProperty("conversation_avatar")
    private String conversationAvatar;
    @JsonProperty("conversation_name")
    private String conversationName;
    @JsonProperty("last_message")
    private String lastMessage;
    @JsonProperty("send_last_at")
    private String sendLastAt;
    @JsonProperty("unread_message_count")
    private Integer unreadMessageCount;
    @JsonProperty("member_list")
    private Set<MemberDto> memberList;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("type")
    private Conversation.ConversationType type;
    @JsonProperty("online")
    private boolean online;
    @JsonProperty("message_page")
    private PageData<?> messagePage;
}
