package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_members")
public class GroupMember extends BaseEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    private Role role; // Quyền của thành viên

    @Enumerated(EnumType.STRING)
    private MemberStatus status; // Trạng thái tham gia nhóm

    private LocalDateTime joinedAt;

    public enum Role {
        ADMIN,     // Quản trị viên
        MODERATOR, // Kiểm duyệt viên
        MEMBER     // Thành viên bình thường
    }

    public enum MemberStatus {
        PENDING,  // Đang chờ phê duyệt
        APPROVED, // Đã duyệt
        BANNED    // Bị cấm khỏi nhóm
    }

    @PrePersist
    protected void onJoin() {
        joinedAt = LocalDateTime.now();
    }
}
