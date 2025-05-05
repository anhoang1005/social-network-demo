package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "friendships", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "friend_id"})
}, indexes = {
        @Index(name = "idx_user_status", columnList = "user_id, status"),
        @Index(name = "idx_friend_status", columnList = "friend_id, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends BaseEntity<Long> implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // Người nhận lời mời hoặc bị chặn

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Users friend; // Người gửi lời mời hoặc chặn

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    public enum FriendshipStatus {
        PENDING,  // Chỉ 1 bản ghi
        ACCEPTED, // 2 bản ghi (2 chiều)
        BLOCKED,   // Chỉ 1 bản ghi
        FOLLOW
    }

}
