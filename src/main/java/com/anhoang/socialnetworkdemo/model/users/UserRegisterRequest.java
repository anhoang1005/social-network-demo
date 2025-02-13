package com.anhoang.socialnetworkdemo.model.users;

import com.anhoang.socialnetworkdemo.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String fullName;
    private String dob;
    private Users.Gender gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String bio;
}
