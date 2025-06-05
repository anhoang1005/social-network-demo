package com.anhoang.socialnetworkdemo.controller;

import com.anhoang.socialnetworkdemo.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CookieController {
    private final CookieUtils cookieUtils;

    @GetMapping("/api/guest/set-cookie")
    public String setCookie(HttpServletResponse response) {
        cookieUtils.setCookie(response, "userId", "1234");
        return "Cookie đã được thiết lập!";
    }

    @GetMapping("/api/guest/read-cookie")
    public String readCookie(HttpServletRequest request) {
        String userId = cookieUtils.getCookie(request, "userId");
        if(userId!=null){
            return "userId: " + userId;
        }
        return "Không tìm thấy cookie userId";
    }
}
