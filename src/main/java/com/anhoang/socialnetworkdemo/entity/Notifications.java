package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "notifications")
public class Notifications extends BaseEntity<Long> implements Serializable {

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private String description;

    public enum Type{
        LIKE,
        COMMENTS,
        FOLLOW,
        MESSAGE,
        SYSTEM
    }
}
