package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hashtag")
public class Hashtag extends BaseEntity<Long> implements Serializable {
    @Column(nullable = false, unique = true)
    private String name;
    private Long postCount;
    private Long viewCount;

    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

    @PrePersist
    public void onCreate(){
        this.postCount = 0L;
        this.viewCount = 0L;
    }
}
