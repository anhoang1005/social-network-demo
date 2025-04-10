package com.anhoang.socialnetworkdemo.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("user_avatar")
    private String userAvatar;
    @JsonProperty("is_friend")
    private Boolean isFriend;
    @JsonProperty("mutual_friend_count")
    private Long mutualFriendCount;
    @JsonProperty("online")
    private Boolean online;
}
