package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.config.Constant;
import com.anhoang.socialnetworkdemo.entity.MediaFile;
import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.repository.PostRepository;
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
    private final PostRepository postRepository;
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
                String avatarUrl = "https://randomuser.me/api/portraits/men/" + (i+1) + ".jpg";
                Users user = new Users();
                user.setFullName(faker.name().fullName());
                user.setAvatar(avatarUrl);
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
                user.setAvatar("https://randomuser.me/api/portraits/men/50.jpg");
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

    @Override
    @Transactional
    public void generateFakePosts() {
        try{
            List<Users> usersList = usersRepository.findAll();
            List<Post> postsList = new ArrayList<>();

            for (Users user : usersList) {
                for (int i = 0; i < 5; i++) {

                    Post post = new Post();
                    post.setUsers(user);
                    post.setIsShared(false);
                    post.setContent(faker.lorem().paragraph(random.nextInt(10, 50)));
                    post.setVisibility(faker.options().option(Post.Visibility.class));
                    post.setLocation(faker.address().fullAddress());
                    post.setStatus(Post.Status.NORMAL);
                    post.setHashtags(null);
                    post.setHashtag(null);

                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setFileName("image.jpg");
                    mediaFile.setMediaType(MediaFile.MediaType.IMAGE);
                    mediaFile.setFileSize(1024L);
                    mediaFile.setUrl("https://baogiaothong.mediacdn.vn/upload/2-2022/images/2022-04-18/1-1650247268-869-width740height555.jpg");
                    mediaFile.setMediaFormat("jpg");
                    mediaFile.setUsersUpdated(user);
                    mediaFile.setAccessLevel(MediaFile.AccessLevel.PUBLIC);
                    mediaFile.setPost(post);

                    post.setMediaFiles(Collections.singletonList(mediaFile));
                    postsList.add(post);
                }
            }
            postRepository.saveAll(postsList);
            log.info("Generated {} post for {} users", postsList.size(), usersList.size());
        } catch (Exception e){
            e.printStackTrace();
            log.error("Fake post error!");
        }

    }

    @Override
    public void generateFakeComments() {

    }
}
