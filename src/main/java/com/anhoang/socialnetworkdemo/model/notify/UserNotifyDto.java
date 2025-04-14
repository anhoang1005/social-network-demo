package com.anhoang.socialnetworkdemo.model.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotifyDto {
    @JsonProperty("action_user_id")
    private Long actionUserId;
    @JsonProperty("action_user_code")
    private String actionUserCode;
    @JsonProperty("action_username")
    private String actionUsername;
    @JsonProperty("action_user_avatar")
    private String actionUserAvatar;
}
