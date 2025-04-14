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

    @GetMapping("/api/user/friend/friendship-list")
    public ResponseEntity<?> userGetFriendshipList(@RequestParam("type") String type,
                                                   @RequestParam(name = "user_other_id", required = false) Long userOtherId,
                                                   @RequestParam("page_number") int pageNumber,
                                                   @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(friendshipService.getFriendshipList(type, userOtherId, pageNumber, pageSize));
    }

    @PostMapping("/api/user/friend/friendship-action")
    public ResponseEntity<?> userFriendshipAction(@RequestParam("type") String type,
                                                  @RequestParam("user_id") Long userId){
        return ResponseEntity.ok(friendshipService.userFriendshipActionEvent(type, userId));
    }

    @GetMapping("/api/user/friend/friendship-count")
    public ResponseEntity<?> getMyFriendListCount(@RequestParam("type") String type,
                                                  @RequestParam(name = "user_other_id", required = false) Long userOtherId){
        return ResponseEntity.ok(friendshipService.getFriendshipCount(type, userOtherId));
    }
}
