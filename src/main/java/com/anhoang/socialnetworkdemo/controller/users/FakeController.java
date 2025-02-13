package com.anhoang.socialnetworkdemo.controller.users;

import com.anhoang.socialnetworkdemo.service.FakeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FakeController {
    private final FakeService fakeService;

    @GetMapping("/api/guest/fake/users")
    public ResponseEntity<?> fakeUser(){
        fakeService.generateFakeRootUsers();
        fakeService.generateFakeAdminUsers();
        fakeService.generateFakeCustomerUsers();
        return ResponseEntity.ok("done");
    }
}
