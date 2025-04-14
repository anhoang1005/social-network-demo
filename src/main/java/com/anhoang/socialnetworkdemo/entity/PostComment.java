package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "post_comment", indexes = {
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_post_id", columnList = "post_id"),
})
public class PostComment extends BaseEntity<Long>{

    @Column(nullable = false)
    private Integer lever;

    @Column(nullable = false)
    private Integer childrenCount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String mediaUrl;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PostComment commentParent;

    @OneToMany(mappedBy = "commentParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostComment> childrenCommentList;

    @OneToMany(mappedBy = "postComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostReaction> reactionList;

    @OneToMany(mappedBy = "postComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MediaFile> mediaFileList;

    @OneToMany(mappedBy = "postComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notifications> notificationsList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private Users users;

    @PrePersist
    public void onCreate(){
        this.likeCount = 0;
        this.active = true;
        this.childrenCount = 0;
    }
}
