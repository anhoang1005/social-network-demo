package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_user")
public class Group extends BaseEntity<Long>{

    @Column(nullable = false, length = 100)
    private String groupName;

    private String groupAvatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupType groupType;
    @Column(nullable = false)
    private Boolean isSecretGroup;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupMember> members = new ArrayList<>();
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private Boolean requiresApproval; // Cần phê duyệt khi thành viên muốn tham gia?
    @Column(nullable = false)
    private Boolean allowMembersToPost; // Cho phép thành viên đăng bài?
    @Column(nullable = false)
    private Boolean allowComments; // Cho phép bình luận không?
    @Column(nullable = false)
    private Boolean allowNotifications; // Có gửi thông báo nhóm không?

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupStatus groupStatus;


    public enum GroupStatus{
        NORMAL,
        DELETED,
        BLOCK
    }

    public enum GroupType {
        PUBLIC,
        PRIVATE
    }
}
