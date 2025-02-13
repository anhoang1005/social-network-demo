package com.anhoang.socialnetworkdemo.controller.users;

import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/api/user/users/detail")
    public ResponseEntity<?> userGetUserDetailApi(){
        return ResponseEntity.ok(usersService.usersGetUsersDetailResponse());
    }

    @PutMapping("/api/user/users/change-password")
    public ResponseEntity<?> userChangePasswordApi(
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ){
        return ResponseEntity.ok(usersService.usersChangePassword(oldPassword, newPassword));
    }

    @PutMapping("/api/user/users/change-info")
    public ResponseEntity<?> userChangeInfoApi(
            @RequestBody UserRegisterRequest userInfo){
        return ResponseEntity.ok(usersService.usersChangeUserInfo(userInfo));
    }

    @PutMapping("/api/user/users/change-avatar")
    public ResponseEntity<?> userChangeImageUrlApi(
            @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(usersService.usersChangeAvatar(file));
    }

    @PutMapping("/api/root/users/change-admin")
    public ResponseEntity<?> rootChangeAdminRoleApi(
            @RequestParam("user_code") String userCode,
            @RequestParam("is_admin") boolean isAdmin){
        return ResponseEntity.ok(usersService.rootChangeRoleUsers(userCode, isAdmin));
    }

}
