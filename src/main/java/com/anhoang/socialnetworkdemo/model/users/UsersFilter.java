package com.anhoang.socialnetworkdemo.model.users;

import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.entity.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersFilter {
    private Roles.BaseRole role;
    private Users.Status status;
    private String sort;
    private String startDate;
    private String endDate;
    private String email;
    private String phoneNumber;
    private String fullName;
    private int pageNumber;
    private int pageSize;
}
