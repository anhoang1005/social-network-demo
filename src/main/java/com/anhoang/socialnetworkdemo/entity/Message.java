package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages",
        indexes = {@Index(name = "idx_conversation_created", columnList = "conversation_id, created_at")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity<Long> implements Serializable {

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Users senderUsers;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "parentMessage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> childrenMessage = new ArrayList<>();

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MessageFile> messageFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Message parentMessage;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
}
