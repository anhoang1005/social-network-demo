package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.config.websocket.WebSocketEventListener;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.model.users.UserDto;
import com.anhoang.socialnetworkdemo.model.users.UserShortDto;
import com.anhoang.socialnetworkdemo.model.users.UsersDetailResponse;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsersMapper {
    private final WebSocketEventListener webSocketEventListener;

    public UsersDetailResponse entityToUsersDetailResponse(Users entity){
        return UsersDetailResponse.builder()
                .userCode(entity.getUserCode())
                .avatar(entity.getAvatar())
                .fullName(entity.getFullName())
                .gender(entity.getGender().name())
                .dob(TimeMapperUtils.localDateToString(entity.getDob()))
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .role(entity.getRolesList().stream()
                        .map(roles -> roles.getRoleName().name())
                        .collect(Collectors.toList()))
                .createdAt(TimeMapperUtils.localDateTimeToString(entity.getCreatedAt()))
                .modifiedAt(TimeMapperUtils.localDateTimeToString(entity.getUpdatedAt()))
                .modifiedBy(entity.getUpdatedBy())
                .build();
    }

    public UserDto entityToUserDto(Users entity){
        return UserDto.builder()
                .userCode(entity.getUserCode())
                .avatar(entity.getAvatar())
                .fullName(entity.getFullName())
                .gender(entity.getGender().name())
                .dob(TimeMapperUtils.localDateToString(entity.getDob()))
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .role(entity.getRolesList().stream()
                        .map(roles -> roles.getRoleName().name())
                        .collect(Collectors.toList()))
                .createdAt(TimeMapperUtils.localDateTimeToString(entity.getCreatedAt()))
                .modifiedAt(TimeMapperUtils.localDateTimeToString(entity.getUpdatedAt()))
                .modifiedBy(entity.getUpdatedBy())
                .status(entity.getStatus())
                .build();
    }

    public UserShortDto entityToUserShortDto(Users users){
        return UserShortDto.builder()
                .userId(users.getId())
                .userCode(users.getUserCode())
                .fullName(users.getFullName())
                .userAvatar(users.getAvatar())
                .online(webSocketEventListener.checkCustomerConnection(users.getUserCode()))
                .build();
    }
}
