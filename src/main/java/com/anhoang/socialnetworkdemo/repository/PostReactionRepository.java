package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    @Query(value = "select r from PostReaction r " +
            "where r.users.userCode = :userCode " +
            "and r.post.id = :postId ")
    PostReaction findPostReactionByPostIdAndUserCode(@Param("userCode") String userCode,
                                                     @Param("postId") Long postId);

    @Query(value = "select r from PostReaction r " +
            "where r.users.id = :userId " +
            "and r.postComment.id = :commentId ")
    PostReaction findPostReactionByCommentIdAndUserId(@Param("userId") Long userId,
                                                        @Param("commentId") Long commentId);
}
