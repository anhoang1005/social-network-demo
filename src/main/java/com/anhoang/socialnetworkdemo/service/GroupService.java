package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.model.group.CreateGroupRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.http.ResponseEntity;

public interface GroupService {

    ResponseBody<?> userCreateGroup(CreateGroupRequest request);

    ResponseEntity<?> userUpdateGroup();

    ResponseEntity<?> userDeleteGroup();

    ResponseEntity<?> userViewGroup();

    ResponseEntity<?> adminAcceptMember();

    ResponseEntity<?> userCreateNewPostGroup();

    ResponseEntity<?> adminAcceptNewPostGroup();
}
