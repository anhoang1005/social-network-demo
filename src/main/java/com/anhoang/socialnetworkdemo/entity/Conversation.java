package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation extends BaseEntity<Long> implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationType type;

    @Column(nullable = true)
    private String conversationName;

    @Column(nullable = true)
    private String conversationAvatar;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConversationMember> members = new HashSet<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageFile> messageFiles = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String latestMessage;

    @Column
    private LocalDateTime sendLastAt;

    @Column(nullable = false)
    private Boolean active;

    @Column
    private String socketSessionId;

    @PrePersist
    public void initializeDefaultValues() {
        this.sendLastAt = null;
        this.latestMessage = null;
        this.active = true;
    }

    public enum ConversationType{
        PRIVATE,
        GROUP
    }
}
