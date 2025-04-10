package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.payload.ResponseBody;

public interface FriendshipService {

    Boolean checkIsFriend(Long userOtherId);

    //Nguoi dung gui loi moi ket ban
    ResponseBody<?> userSendFriendRequest(String userCode);

    //Nguoi dung chap nhan loi moi ket ban
    ResponseBody<?> userAcceptFriendRequest(String userCode);

    //Nguoi dung xoa ban be
    ResponseBody<?> userRemoveFriend(String userCode);

    //Nguoi dung tu choi loi moi ket ban
    ResponseBody<?> userDeclineFriendRequest(String userCode);

    //Nguoi dung huy loi moi ket ban
    ResponseBody<?> userRemoveFriendRequest(String userCode);

    //Nguoi dung chan hoac mo chan ban be
    ResponseBody<?> userBlockOrUnlockFriend(String userCode, boolean isBlock);

    //Lay danh sach ban be
    ResponseBody<?> getFriendsList(int pageSize, int pageNumber);

    //Lay danh sach ban chung
    ResponseBody<?> getMutualFriendsList(Long userOtherId, int pageNumber, int pageSize);

    //Lay ra danh sach loi moi ket ban toi da gui
    ResponseBody<?> getListFriendRequestSendByMe(int pageNumber, int pageSize);

    //Lay ra danh sach loi moi ket ban gui toi toi
    ResponseBody<?> getListFriendRequestInviteMe(int pageMuber, int pageSize);

    //Lay ra so luong ban be cua toi
    Long getFriendListCount();

    //Lay so luong ban chung
    Long getMutualFriendCount(Long userOtherId);

}
