package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface UsersService {

    ResponseBody<?> usersChangePassword(String oldPassword, String newPassword);

    ResponseBody<?> usersChangeUserInfo(UserRegisterRequest req);

    ResponseBody<?> usersChangeAvatar(MultipartFile file);

    ResponseBody<?> usersChangeCoverImage(MultipartFile file);

    ResponseBody<?> usersGetUsersDetailResponse();

    ResponseBody<?> rootChangeRoleUsers(String userCode, boolean isAdmin);

    ResponseBody<?> getListUsersAccount(int pageNumber, int pageSize);

    ResponseBody<?> getListUsersAccountByFullName(String fullName, int pageNumber, int pageSize);
}
