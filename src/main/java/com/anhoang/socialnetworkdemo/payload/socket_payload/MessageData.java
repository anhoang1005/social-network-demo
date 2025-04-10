package com.anhoang.socialnetworkdemo.payload.socket_payload;

import com.anhoang.socialnetworkdemo.entity.Conversation;
import com.anhoang.socialnetworkdemo.model.media.MessageFileDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageData {
    private Long conversationId;
    private String conversationAvatar;
    private String conversationName;
    private Conversation.ConversationType conversationType;
    private String userAvatar;
    private String userName;
    private String userCode;
    private Long replyOf;
    private String content;
    private List<MessageFileDto> mediaFiles;
    private Type type;
    private String createdAt;

    public enum Type{
        CHAT,
        UPDATE,
        SENT,
        SEEN
    }
}
