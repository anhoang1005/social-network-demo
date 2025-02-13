package com.anhoang.socialnetworkdemo.entity.Initializer;

import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.repository.RolesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class RoleUserRootInitializer implements CommandLineRunner {
    private final RolesRepository rolesRepository;

    private void createRole(){
        try {
            List<Roles.BaseRole> listRole = List.of(Roles.BaseRole.SUPER_ADMIN,
                    Roles.BaseRole.ADMIN, Roles.BaseRole.USER, Roles.BaseRole.MODERATOR);
            List<Roles> listEntity = new ArrayList<>();
            for(Roles.BaseRole role : listRole){
                if(!rolesRepository.existsRolesByRoleName(role)){
                    Roles roles = new Roles();
                    roles.setRoleName(role);
                    listEntity.add(roles);
                }
            }
            if(!listEntity.isEmpty()){
                rolesRepository.saveAll(listEntity);
                log.info("Create all root roles success!");
            } else {
                log.info("All root existed!");
            }
        } catch (Exception e) {
            log.error("Create root roles error! Error: {}", e.getMessage());
        }
    }

    @Override
    public void run(String... args) {
        createRole();
    }
}
