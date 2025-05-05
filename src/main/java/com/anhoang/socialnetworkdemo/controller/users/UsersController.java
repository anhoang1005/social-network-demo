package com.anhoang.socialnetworkdemo.controller.users;

import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/api/user/users/detail")
    public ResponseEntity<?> userGetUserDetailApi(){
        return ResponseEntity.ok(usersService.usersGetUsersDetailResponse());
    }

    @GetMapping("/api/user/users/other-detail/{user_id}")
    public ResponseEntity<?> userGetOtherUserDetailApi(@PathVariable("user_id") Long userId){
        return ResponseEntity.ok(usersService.userGetOtherUserDetail(userId));
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

    @PutMapping("/api/root/users/change-admin")
    public ResponseEntity<?> rootChangeAdminRoleApi(
            @RequestParam("user_code") String userCode,
            @RequestParam("is_admin") boolean isAdmin){
        return ResponseEntity.ok(usersService.rootChangeRoleUsers(userCode, isAdmin));
    }

    @GetMapping("/api/user/users/get-all")
    public ResponseEntity<?> userGetListAccount(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(usersService.getListUsersAccount(pageNumber, pageSize));
    }

    @GetMapping("/api/user/users/search-by-name")
    public ResponseEntity<?> userGetListAccount(
            @RequestParam("full_name") String fullName,
            @RequestParam("page_number") int pageNumber,
            @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(usersService.getListUsersAccountByFullName(fullName, pageNumber, pageSize));
    }

}
