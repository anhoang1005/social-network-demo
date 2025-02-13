package com.anhoang.socialnetworkdemo.controller.friendship;

import com.anhoang.socialnetworkdemo.service.FriendshipService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/api/user/friend/check-friend/{user_other_id}")
    public ResponseEntity<?> getMyFriendList(@PathVariable("user_other_id") Long userOtherId){
        return ResponseEntity.ok(friendshipService.checkIsFriend(userOtherId));
    }

    @GetMapping("/api/user/friend/get-friend")
    public ResponseEntity<?> getMyFriendList(@RequestParam("page_number") int pageNumber,
                                             @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(friendshipService.getFriendsList(pageNumber, pageSize));
    }

    @GetMapping("/api/user/friend/get-friend-count")
    public ResponseEntity<?> getMyFriendListCount(){
        return ResponseEntity.ok(friendshipService.getFriendListCount());
    }

    @GetMapping("/api/user/friend/mutual-friend")
    public ResponseEntity<?> getListMyFriend(@RequestParam("user_other_id") Long userOtherId,
                                             @RequestParam("page_number") int pageNumber,
                                             @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(friendshipService.getMutualFriendsList(userOtherId ,pageNumber, pageSize));
    }

    @GetMapping("/api/user/friend/mutual-friend-count")
    public ResponseEntity<?> getListMyFriend(@RequestParam("user_other_id") Long userOtherId){
        return ResponseEntity.ok(friendshipService.getMutualFriendCount(userOtherId));
    }

    @GetMapping("/api/user/friend/send-request/{user_code}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable("user_code") String userCode){
        return ResponseEntity.ok(friendshipService.userSendFriendRequest(userCode));
    }

    @GetMapping("/api/user/friend/accept-request/{user_code}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable("user_code") String userCode){
        return ResponseEntity.ok(friendshipService.userAcceptFriendRequest(userCode));
    }

    @DeleteMapping("/api/user/friend/remove-request/{user_code}")
    public ResponseEntity<?> removeFriendRequest(@PathVariable("user_code") String userCode){
        return ResponseEntity.ok(friendshipService.userRemoveFriendRequest(userCode));
    }

    @DeleteMapping("/api/user/friend/reject-request/{user_code}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable("user_code") String userCode){
        return ResponseEntity.ok(friendshipService.userDeclineFriendRequest(userCode));
    }

    @DeleteMapping("/api/user/friend/remove-friend/{user_code}")
    public ResponseEntity<?> removeFriend(@PathVariable("user_code") String userCode){
        return ResponseEntity.ok(friendshipService.userRemoveFriend(userCode));
    }

    @DeleteMapping("/api/user/friend/block/{user_code}")
    public ResponseEntity<?> blockFriendRequest(@PathVariable("user_code") String userCode,
                                                @RequestParam("status") Boolean status){
        return ResponseEntity.ok(friendshipService.userBlockOrUnlockFriend(userCode, status));
    }



}
