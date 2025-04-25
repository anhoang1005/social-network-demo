package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.entity.Group;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.model.group.GroupDto;
import com.anhoang.socialnetworkdemo.model.users.UserShortDto;
import com.anhoang.socialnetworkdemo.repository.GroupMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GroupMapper {
    private final GroupMemberRepository groupMemberRepository;
    private final UsersMapper usersMapper;

    public GroupDto entityToGroupDto(Group group){
        Users users = groupMemberRepository.getAdminMember(group.getId()).getUser();
        UserShortDto dto = UserShortDto.builder()
                .userId(users.getId())
                .fullName(users.getFullName())
                .userAvatar(users.getAvatar())
                .userCode(users.getUserCode())
                .build();
        Long memberCount = groupMemberRepository.countGroupMemberByGroup_Id(group.getId());
        return GroupDto.builder()
                .groupId(group.getId())
                .groupAvatar(group.getGroupAvatar())
                .groupName(group.getGroupName())
                .groupDes(group.getDescription())
                .groupType(group.getGroupType())
                .isSecretGroup(group.getIsSecretGroup())
                .requiresApproval(group.getIsSecretGroup())
                .allowMembersToPost(group.getAllowMembersToPost())
                .allowComments(group.getAllowComments())
                .allowNotifications(group.getAllowNotifications())
                .admin(dto)
                .memberCount(memberCount!=null ? memberCount : 0L)
                .postLastAt(null)
                .build();
    }
}
