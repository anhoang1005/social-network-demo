package com.anhoang.socialnetworkdemo.model.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {
    private Long userId;
    private String userCode;
    private String fullName;
    private String userAvatar;
    private Boolean online;
}
