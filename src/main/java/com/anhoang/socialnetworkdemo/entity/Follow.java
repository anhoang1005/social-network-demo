package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follow")
public class Follow extends BaseEntity<Long> implements Serializable {

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Users follower; // Người theo dõi

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private Users following; // Người được theo dõi

}
