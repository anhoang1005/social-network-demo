package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity<Long> implements Serializable {

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

//    @ManyToMany(mappedBy = "permissions")
//    private List<Roles> roles = new ArrayList<>();
}
