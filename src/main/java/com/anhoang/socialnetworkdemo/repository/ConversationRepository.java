package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.Conversation;
import com.anhoang.socialnetworkdemo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT DISTINCT c FROM Conversation c " +
            "JOIN c.members cm " +
            "WHERE cm.users.id = :userId")
    Page<Conversation> findAllByUserId(@Param("userId") Long userId,
                                       Pageable pageable);

    Optional<Conversation> findConversationById(Long id);


    @Query("SELECT COUNT(c) FROM Conversation c " +
            "WHERE c.id = :conversationId " +
            "AND EXISTS (SELECT 1 FROM ConversationMember cm " +
            "            WHERE cm.conversation.id = c.id " +
            "            AND cm.users.id = :userId)")
    Long userExistedInConversation(Long conversationId, Long userId);
}
