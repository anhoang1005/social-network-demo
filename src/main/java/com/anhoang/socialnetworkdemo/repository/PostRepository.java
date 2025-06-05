package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.Hashtag;
import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p from Post p " +
            "where p.status = :status " +
            "and (:hashTag is null or :hashTag member of p.hashtags)")
    Page<Post> userGetPostHome(@Param("status") Post.Status status,
                               @Param("hashTag") Hashtag hashtag,
                               Pageable pageable);

    @Query(value = "select p from Post p " +
            "where p.status = :status " +
            "and p.users.id = :userId " +
            "and (:hashTag is null or :hashTag member of p.hashtags) ")
    Page<Post> userGetTheirPostOneHashTag(@Param("status") Post.Status status,
                                          @Param("userId") Long userId,
                                          @Param("hashTag") Hashtag hashTag,
                                          Pageable pageable);

    @Query(value = "select p from Post p " +
            "where p.status = :status " +
            "and p.visibility = :visibility " +
            "and p.users.id = :userId " +
            "and (:hashtag is null or :hashtag member of p.hashtags)")
    Page<Post> userGetUserPostOther(@Param("status") Post.Status status,
                                    @Param("userId") Long userId,
                                    @Param("hashtag") Hashtag hashTag,
                                    @Param("visibility") Post.Visibility visibility,
                                    Pageable pageable);

    @Modifying
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    default void updatePostLikeCount(Long postId, boolean isAdd){
        Post post = findById(postId).orElseThrow(() -> new RequestNotFoundException("ERROR"));
        if(isAdd) post.setLikeCount(post.getLikeCount()+1);
        else  post.setLikeCount(post.getLikeCount()-1);
    }

    @Query("SELECT COUNT(r) FROM Post p JOIN p.postReactionList r WHERE p.id = :postId")
    Long countReactionsByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(c) FROM Post p JOIN p.postCommentList c WHERE p.id = :postId")
    Long countCommentsByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(sp) FROM Post p JOIN p.sharePostList sp WHERE p.id = :postId")
    Long countSharesByPostId(@Param("postId") Long postId);
}
