package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation_members",
        indexes = {@Index(name = "idx_user_conversation", columnList = "user_id, conversation_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMember {

    public ConversationMember(Conversation conversation, Users users, Boolean isConversationAdmin) {
        this.conversation = conversation;
        this.users = users;
        this.isConversationAdmin = isConversationAdmin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(nullable = false)
    private Boolean isConversationAdmin;
}
