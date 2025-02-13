package com.anhoang.socialnetworkdemo.controller.users;

import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.payload.JwtData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/api/guest/users/login")
    public ResponseEntity<?> loginUsersApi(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        ResponseBody<JwtData> responseBody = new ResponseBody<>(
                accountService.loginUsers(username, password),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/api/guest/users/register")
    public ResponseEntity<?> registerUserApi(
            @RequestBody UserRegisterRequest user) {
        return new ResponseEntity<>(accountService.registerUsers(user), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/check-register")
    public ResponseEntity<?> checkRegisterApi(
            @RequestParam("email") String email,
            @RequestParam("verify_code") String verifyCode
    ) {
        return new ResponseEntity<>(accountService.checkVerifyCodeRegister(email, verifyCode), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/forgot-password")
    public ResponseEntity<?> forgotPasswordApi(
            @RequestParam("email") String email
    ) {
        return new ResponseEntity<>(accountService.userForgotPassword(email), HttpStatus.OK);
    }

    @PutMapping("/api/guest/users/check-forgot")
    public ResponseEntity<?> checkVerifyForgotPasswordApi(
            @RequestParam("email") String email,
            @RequestParam("new_password") String newPassword,
            @RequestParam("verify_code") String verifyCode
    ) {
        return new ResponseEntity<>(accountService.checkVerifyCodeForgotPassword(email, newPassword, verifyCode), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/create-root")
    public ResponseEntity<?> generate(){
        return ResponseEntity.ok(accountService.generateRootUsers());
    }
}
