package com.anhoang.socialnetworkdemo.service;

import org.springframework.http.ResponseEntity;

public interface GroupService {

    ResponseEntity<?> userCreateGroup();

    ResponseEntity<?> userUpdateGroup();

    ResponseEntity<?> userDeleteGroup();

    ResponseEntity<?> userViewGroup();

    ResponseEntity<?> adminAcceptMember();

    ResponseEntity<?> userCreateNewPostGroup();

    ResponseEntity<?> adminAcceptNewPostGroup();
}
