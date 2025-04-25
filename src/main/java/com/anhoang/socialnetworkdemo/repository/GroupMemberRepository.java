package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    @Query(value = "select m from GroupMember m " +
            "WHERE m.group.id = :groupId " +
            "AND m.role = 'ADMIN'")
    GroupMember getAdminMember(@Param("groupId") Long groupId);

    Long countGroupMemberByGroup_Id(Long groupId);
}
