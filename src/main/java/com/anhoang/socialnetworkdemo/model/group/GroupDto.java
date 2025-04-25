package com.anhoang.socialnetworkdemo.model.group;

import com.anhoang.socialnetworkdemo.entity.Group;
import com.anhoang.socialnetworkdemo.model.users.UserShortDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private Long groupId;
    private String groupName;
    private String groupAvatar;
    private String groupDes;
    private Group.GroupType groupType;
    private Boolean isSecretGroup;
    private Boolean requiresApproval;
    private Boolean allowMembersToPost;
    private Boolean allowComments;
    private Boolean allowNotifications;
    private UserShortDto admin;

    private Long memberCount;
    private String postLastAt;
}
