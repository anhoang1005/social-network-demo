package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post extends BaseEntity<Long> {

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String hashtag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(nullable = false)
    private Boolean isShared;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Long sharedCount;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Long commentCount;

    @Column(nullable = false)
    private Long viewCount;

    @PrePersist
    public void onCreate(){
        this.sharedCount = 0L;
        this.commentCount = 0L;
        this.likeCount = 0L;
        this.commentCount = 0L;
        this.viewCount = 0L;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_hashtag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_post_id")
    private Post sharedPost;

    @OneToMany(mappedBy = "sharedPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> sharePostList;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostComment> postCommentList;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostReaction> postReactionList;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MediaFile> mediaFiles;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notifications> notificationsList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public enum Visibility{
        PUBLIC,
        PRIVATE,
        FRIEND,
        ANY
    }

    public enum Status{
        NORMAL,
        WAIT,
        LOCK,
        DELETE,
        DISABLE
    }
}
