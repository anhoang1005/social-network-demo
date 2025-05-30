package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
}
