package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.config.Constant;
import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.exceptions.users.AccountLockedException;
import com.anhoang.socialnetworkdemo.exceptions.users.InvalidCredentialsException;
import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.payload.JwtData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.RolesRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.AccountService;
import com.anhoang.socialnetworkdemo.service.MailService;
import com.anhoang.socialnetworkdemo.utils.JwtTokenUtils;
import com.anhoang.socialnetworkdemo.utils.RandomUtils;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IAccountService implements AccountService {
    private final UsersRepository usersRepository;
    private final RolesRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final MailService mailService;

    @Override
    public JwtData loginUsers(String username, String password) {
        Users users = usersRepository.findUsersByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Users not found!"));
        if (users != null && passwordEncoder.matches(password, users.getPassword())) {
            if (users.getStatus() == Users.Status.BINH_THUONG) {
                List<String> listRoleString = users.getRolesList().stream()
                        .map(roles -> roles.getRoleName().name()).collect(Collectors.toList());
                String accessJws = jwtTokenUtils.generateAccessTokens(username, listRoleString);
                String refreshJws = jwtTokenUtils.generateRefreshTokens(username, listRoleString);
                return JwtData.builder()
                        .tokenType("Bearer ")
                        .accessToken(accessJws)
                        .refreshToken(refreshJws)
                        .dob(TimeMapperUtils.localDateToString(users.getDob()))
                        .gender(users.getGender().name())
                        .userCode(users.getUserCode())
                        .fullName(users.getFullName())
                        .avatar(users.getAvatar())
                        .email(users.getEmail())
                        .phoneNumber(users.getPhoneNumber())
                        .issuedAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusDays(3))
                        .role(listRoleString)
                        .build();
            } else if (users.getStatus() == Users.Status.KHOA) {
                log.info("Account Locked!");
                throw new AccountLockedException("Account Locked!");
            } else if (users.getStatus() == Users.Status.CHUA_XAC_NHAN) {
                log.info("Invalid Account!");
                throw new InvalidCredentialsException("Invalid Account");
            }
        } else {
            log.info("Invalid Account!");
            throw new InvalidCredentialsException("Invalid Account!");
        }
        return null;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public ResponseBody<String> registerUsers(UserRegisterRequest user) {
        try {
            Users checkEmail = usersRepository.findUsersByEmail(user.getEmail())
                    .orElse(null);
            if (checkEmail != null &&
                    (checkEmail.getStatus() == Users.Status.BINH_THUONG ||
                            checkEmail.getStatus() == Users.Status.KHOA)) {
                return new ResponseBody<>(null, ResponseBody.Status.SUCCESS, "EMAIL_EXISTED", ResponseBody.Code.SUCCESS);
            } else if(checkEmail !=null && checkEmail.getStatus() == Users.Status.CHUA_XAC_NHAN){
                if(Duration.between(checkEmail.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5){
                    createUser(user);
                } else{
                    return new ResponseBody<>(checkEmail.getCreatedAt().plusMinutes(5).toString(),
                            ResponseBody.Status.SUCCESS, "REGISTER_LATER", ResponseBody.Code.SUCCESS);
                }
            } else {
                createUser(user);
            }
            return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Register account failed " + e.getMessage());
            throw new RequestNotFoundException("Register Failed!");
        }
    }

    private void createUser(UserRegisterRequest user){
        Roles role = roleRepository.findRolesByRoleName(Roles.BaseRole.USER)
                .orElseThrow(() -> new RequestNotFoundException("ERROR"));
        List<Roles> roleList = new ArrayList<>();
        roleList.add(role);
        Users users = new Users();
        String verifyCode = RandomUtils.verifyCode();
        users.setDob(TimeMapperUtils.stringToLocalDate(user.getDob()));
        users.setFullName(user.getFullName());
        users.setGender(user.getGender());
        users.setEmail(user.getEmail());
        users.setPhoneNumber(user.getPhoneNumber());
        users.setRolesList(roleList);
        users.setAvatar(Constant.USER_IMAGE);
        users.setCoverImage(Constant.USER_IMAGE);
        users.setStatus(Users.Status.CHUA_XAC_NHAN);
        users.setVerifyCode(passwordEncoder.encode(verifyCode));
        users.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(users);
        mailService.sendVerifyRegisterEmail(user.getEmail(), verifyCode);
    }

    @Transactional
    @Override
    public ResponseBody<String> checkVerifyCodeRegister(String email, String verifyCode) {
        Users users = usersRepository.findUsersByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        if (passwordEncoder.matches(verifyCode, users.getVerifyCode())) {
            users.setStatus(Users.Status.BINH_THUONG);
            users.setVerifyCode(null);
            usersRepository.save(users);
            return new ResponseBody<>("OK");
        } else {
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
        }
    }

    @Transactional
    @Override
    public ResponseBody<String> userForgotPassword(String email) {
        try {
            Users users = usersRepository.findUsersByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
            String verifyCode = RandomUtils.verifyCode();
            users.setVerifyCode(passwordEncoder.encode(verifyCode));
            mailService.sendFogotPasswordMail(
                    users.getFullName(),
                    email,
                    verifyCode);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.warn("Forget Password Failed");
            throw new RequestNotFoundException("Error");
        }
    }

    @Transactional
    @Override
    public ResponseBody<String> checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode) {
        Users users = usersRepository.findUsersByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        if (passwordEncoder.matches(verifyCode, users.getVerifyCode())) {
            users.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(users);
            return new ResponseBody<>("OK");
        } else {
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    public ResponseBody<String> generateRootUsers() {
        return null;
    }

    @Transactional
    @Override
    public boolean changeRoleUsers(String userCode, Roles.BaseRole role, boolean isAdd) {
        Users users = usersRepository.findUsersByUserCode(userCode)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        try {
            List<Roles> userRoles = users.getRolesList();
            Roles roles = roleRepository.findRolesByRoleName(role)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            if (isAdd) {
                if (!userRoles.contains(roles)) {
                    userRoles.add(roles);
                }
            } else {
                if (!userRoles.contains(roles)) {
                    userRoles.remove(roles);
                }
            }
            usersRepository.save(users);
            log.info("Change roles users {} to {} success", users.getEmail(), userRoles);
            return true;
        } catch (Exception e) {
            log.error("Change roles users {} failed", users.getEmail());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
