package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.config.websocket.WebSocketEventListener;
import com.anhoang.socialnetworkdemo.entity.Friendship;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.model.users.UserDto;
import com.anhoang.socialnetworkdemo.model.users.UserShortDto;
import com.anhoang.socialnetworkdemo.model.users.UsersDetailResponse;
import com.anhoang.socialnetworkdemo.repository.FriendshipRepository;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsersMapper {
    private final WebSocketEventListener webSocketEventListener;
    private final FriendshipRepository friendshipRepository;

    public UsersDetailResponse entityToUsersDetailResponse(Users entity){
        Long friendCount = friendshipRepository.countFriendOfUser(entity.getId());
        return UsersDetailResponse.builder()
                .userId(entity.getId())
                .userCode(entity.getUserCode())
                .avatar(entity.getAvatar())
                .coverImage(entity.getCoverImage())
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
                .bio(entity.getBio())
                .friendCount(friendCount!=null ? friendCount : 0L)
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

    public UserShortDto entityToUserShortDto(Long meId, Users users){
        Long mutualCount = friendshipRepository.countMutualFriends(meId, users.getId());
        Long checkFriend = friendshipRepository.checkExistedFriend(meId, users.getId(), Friendship.FriendshipStatus.ACCEPTED);
        return UserShortDto.builder()
                .userId(users.getId())
                .userCode(users.getUserCode())
                .fullName(users.getFullName())
                .userAvatar(users.getAvatar())
                .isFriend( checkFriend != null && checkFriend > 0)
                .mutualFriendCount(mutualCount!=null ? mutualCount : 0L)
                .online(webSocketEventListener.checkCustomerConnection(users.getUserCode()))
                .build();
    }
}
