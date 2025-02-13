package com.anhoang.socialnetworkdemo.model.users;

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
    private String userCode;
    private String avatar;
    private String fullName;
    private String gender;
    private String dob;
    private String phoneNumber;
    private String email;
    private List<String> role;
    private String createdAt;
    private String modifiedAt;
    private String modifiedBy;
}
