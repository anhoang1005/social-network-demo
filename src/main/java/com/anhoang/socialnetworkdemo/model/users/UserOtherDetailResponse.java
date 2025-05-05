package com.anhoang.socialnetworkdemo.model.users;

import com.anhoang.socialnetworkdemo.entity.Friendship;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOtherDetailResponse {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("cover_image")
    private String coverImage;
    @JsonProperty("full_name")
    private String fullName;
    private String gender;
    private String dob;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String bio;
    private String email;
    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("friend_count")
    private Long friendCount;
    @JsonProperty("is_friend")
    private Boolean isFriend;
    @JsonProperty("mutual_friend_count")
    private Long mutualFriendCount;
    private Boolean online;
    private String friendship;
}
