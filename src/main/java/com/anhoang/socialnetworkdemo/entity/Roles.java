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
@Table(name = "roles")
public class Roles extends BaseEntity<Long>{

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private BaseRole roleName;

    @ManyToMany(mappedBy = "rolesList", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Users> usersList = new ArrayList<>();

//    @ManyToMany
//    @JoinTable(
//            name = "role_permissions",
//            joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_id")
//    )
//    private List<Permission> permissions = new ArrayList<>();

    public enum BaseRole{
        ADMIN,
        USER,
        SUPER_ADMIN,
        MODERATOR
    }

}
