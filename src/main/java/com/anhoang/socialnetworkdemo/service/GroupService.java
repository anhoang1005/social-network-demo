package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.model.group.CreateGroupRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.http.ResponseEntity;

public interface GroupService {

    ResponseBody<?> userCreateGroup(CreateGroupRequest request);

    ResponseBody<?> userUpdateGroup(CreateGroupRequest request);

    ResponseBody<?> userDeleteGroup(Long groupId);

    ResponseBody<?> userViewGroup(Long groupId);

    ResponseEntity<?> adminAcceptMember();

    ResponseEntity<?> userCreateNewPostGroup();

    ResponseEntity<?> adminAcceptNewPostGroup();
}
