package com.anhoang.socialnetworkdemo.controller.notify;

import com.anhoang.socialnetworkdemo.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotifyController {
    private final NotificationService notificationService;

    @GetMapping("/api/user/notify/get-list")
    public ResponseEntity<?> getPageOfNotify(@RequestParam("page_number") int pageNumber,
                                             @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(notificationService.userGetNotifyList(pageNumber, pageSize));
    }
}
