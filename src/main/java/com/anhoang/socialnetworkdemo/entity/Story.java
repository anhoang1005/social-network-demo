package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "story")
public class Story extends BaseEntity<Long>{

    @Column
    private String mediaUrl;
    @Column
    private String capture;
    @Column
    private String backgroundColor;
    @Column(nullable = true)
    private String music;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false  )
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoryViewer> storyViewerList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public enum Visibility{
        PUBLIC,
        FRIEND,
        PRIVATE
    }

    public enum Type{
        IMAGE,
        VIDEO,
        TEXT
    }
}
