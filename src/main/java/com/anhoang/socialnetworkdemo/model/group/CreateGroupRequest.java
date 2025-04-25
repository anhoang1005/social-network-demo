package com.anhoang.socialnetworkdemo.model.group;

import com.anhoang.socialnetworkdemo.entity.Group;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupRequest {
    private String groupName;
    private String groupAvatar;
    private String groupDes;
    private Boolean isSecretGroup;
    private Group.GroupType groupType;
    private List<String> groupMember;
    private boolean requiresApproval; // Cần phê duyệt khi thành viên muốn tham gia?
    private boolean allowMembersToPost; // Cho phép thành viên đăng bài?
    private boolean allowComments; // Cho phép bình luận không?
    private boolean allowNotifications; // Có gửi thông báo nhóm không?
}
