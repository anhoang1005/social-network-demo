package com.anhoang.socialnetworkdemo.model.cache;

import com.anhoang.socialnetworkdemo.entity.Users;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCacheDto {
    private Long userId;
    private String userCode;
    private String fullName;
    private String email;
    private String userAvatar;
    private Boolean online;
    private List<String> role;
    private Users.Status status;
    private LocalDateTime lastLoginAt;
}
