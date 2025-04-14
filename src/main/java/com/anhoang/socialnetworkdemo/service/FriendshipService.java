package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.payload.ResponseBody;

public interface FriendshipService {

    //Kiem tra xem 2 nguoi co phai ban be khong
    Boolean checkIsFriend(Long userOtherId);

    //Thuc hien cac hanh dong voi friendship
    ResponseBody<?> userFriendshipActionEvent(String type, Long userOtherId);

    //Lay danh sach friendship
    ResponseBody<?> getFriendshipList(String type, Long userOtherId, int pageSize, int pageNumber);

    //Lay ra so luong friendship
    Long getFriendshipCount(String type, Long userOtherId);

}
