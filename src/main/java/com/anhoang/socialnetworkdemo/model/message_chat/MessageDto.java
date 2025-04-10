package com.anhoang.socialnetworkdemo.model.message_chat;

import com.anhoang.socialnetworkdemo.model.media.MessageFileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user_code_send")
    private String userCodeSend;
    @JsonProperty("is_my_message")
    private boolean isMyMessage;
    @JsonProperty("user_name_send")
    private String userNameSend;
    @JsonProperty("user_avatar_send")
    private String userAvatarSend;
    @JsonProperty("reply_of")
    private MessageDto replyOf;
    @JsonProperty("content")
    private String content;
    @JsonProperty("media_files")
    private List<MessageFileDto> mediaFiles;
    @JsonProperty("is_read")
    private Boolean isRead;
    @JsonProperty("reaction_list")
    private List<String> reactionList;
    @JsonProperty("created_at")
    private String createdAt;
}
