package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {

    Boolean existsByConversation_IdAndUsers_Id(Long conversationId, Long userId);

}
