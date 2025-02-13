package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.payload.JwtData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;

public interface AccountService {

    JwtData loginUsers(String username, String password);

    ResponseBody<String> registerUsers(UserRegisterRequest user);

    ResponseBody<String> checkVerifyCodeRegister(String email, String verifyCode);

    ResponseBody<String> userForgotPassword(String email);

    ResponseBody<String> checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode);

    ResponseBody<String> generateRootUsers();

    boolean changeRoleUsers(String userCode, Roles.BaseRole role, boolean isAdd);
}
