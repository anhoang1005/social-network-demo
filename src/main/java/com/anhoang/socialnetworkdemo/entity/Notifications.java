package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notifications implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    private Users senderUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true)
    private PostComment postComment;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updatedAt;

    public enum Type{
        REACTION_POST,
        COMMENT_POST,
        SHARE_POST,
        REACTION_COMMENT,
        REPLY_COMMENT,
        MENTION,
        FRIEND_REQUEST,
        FRIEND_ACCEPTED,
        FOLLOW_REQUEST,
        MESSAGE,
        SYSTEM
    }
}
