package com.anhoang.socialnetworkdemo.model.message_chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("is_conversation_admin")
    private Boolean isConversationAdmin;
    @JsonProperty("online")
    private Boolean online;
}
