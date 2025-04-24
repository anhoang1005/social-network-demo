package com.anhoang.socialnetworkdemo.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDetailResponse {
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
    @JsonProperty("friend_count")
    private Long friendCount;
    private String email;
    private List<String> role;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("modified_at")
    private String modifiedAt;
    @JsonProperty("modified_by")
    private String modifiedBy;
}
