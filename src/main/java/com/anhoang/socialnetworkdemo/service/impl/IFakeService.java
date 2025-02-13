package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.config.Constant;
import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.repository.RolesRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.FakeService;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class IFakeService implements FakeService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository roleRepository;
    private final Random random;
    private final Faker faker = new Faker(new Locale("vi"));
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void generateFakeCustomerUsers() {
        try{
            Roles customer = roleRepository.findRolesByRoleName(Roles.BaseRole.USER).orElseThrow(() ->
                    new RuntimeException("Role CUSTOMER not found"));
            for (int i = 0; i < 20; i++) {
                Users user = new Users();
                user.setFullName(faker.name().fullName());
                user.setAvatar(Constant.USER_IMAGE);
                user.setCoverImage(Constant.USER_IMAGE);
                user.setDob(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                user.setGender(faker.options().option(Users.Gender.class));
                user.setPhoneNumber(faker.phoneNumber().cellPhone());
                user.setEmail(faker.internet().emailAddress());
                user.setPassword(passwordEncoder.encode("123456"));
                user.setStatus(Users.Status.BINH_THUONG);
                user.setVerifyCode(null);
                user.setRolesList(Collections.singletonList(customer));
                usersRepository.save(user);
            }
            log.info("Generated 20 fake customer users");
        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake root error!");
        }
    }

    @Override
    @Transactional
    public void generateFakeAdminUsers() {
        try{
            Roles customer = roleRepository.findRolesByRoleName(Roles.BaseRole.USER).orElseThrow(() ->
                    new RuntimeException("Role CUSTOMER not found"));
            Roles admin = roleRepository.findRolesByRoleName(Roles.BaseRole.ADMIN).orElseThrow(() ->
                    new RuntimeException("Role ADMIN not found"));
            for (int i = 0; i < 2; i++) {
                Users user = new Users();
                user.setFullName(faker.name().fullName());
                user.setAvatar(Constant.USER_IMAGE);
                user.setCoverImage(Constant.USER_IMAGE);
                user.setDob(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                user.setGender(faker.options().option(Users.Gender.class));
                user.setPhoneNumber(faker.phoneNumber().cellPhone());
                user.setEmail(faker.internet().emailAddress());
                user.setPassword(passwordEncoder.encode("123456"));
                user.setStatus(Users.Status.BINH_THUONG);
                user.setVerifyCode(null);
                user.setRolesList(Arrays.asList(customer, admin));
                usersRepository.save(user);
            }
            log.info("Generated 2 fake admin users");
        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake admin error!");
        }
    }

    @Override
    @Transactional
    public void generateFakeRootUsers() {
        try{
            Roles customer = entityManager.merge(
                    roleRepository.findRolesByRoleName(Roles.BaseRole.USER)
                            .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"))
            );
            Roles admin = entityManager.merge(
                    roleRepository.findRolesByRoleName(Roles.BaseRole.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role ADMIN not found"))
            );
            Roles root = entityManager.merge(
                    roleRepository.findRolesByRoleName(Roles.BaseRole.SUPER_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role ROOT not found"))
            );
            Users user = new Users();
            user.setFullName("Hoàng Văn An");
            user.setAvatar(Constant.USER_IMAGE);
            user.setCoverImage(Constant.USER_IMAGE);
            user.setDob(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            user.setGender(Users.Gender.NAM);
            user.setPhoneNumber(faker.phoneNumber().cellPhone());
            user.setEmail("anhoang10052002@gmail.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setStatus(Users.Status.BINH_THUONG);
            user.setVerifyCode(null);
            user.setRolesList(Arrays.asList(customer, admin, root));
            usersRepository.save(user);
            log.info("Generated 1 fake root user");
        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake root error!");
        }
    }
}
