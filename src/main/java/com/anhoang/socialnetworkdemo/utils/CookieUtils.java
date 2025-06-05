package com.anhoang.socialnetworkdemo.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CookieUtils {

    public void setCookie(HttpServletResponse response,
                          String name,
                          String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(3 * 24 * 60 * 60); // Hết hạn sau 7 ngày (tính bằng giây)
        cookie.setPath("/"); // Cookie có hiệu lực trên toàn bộ ứng dụng
        cookie.setHttpOnly(true); // Ngăn JavaScript truy cập
        cookie.setSecure(false); // Chỉ gửi qua HTTPS
        // Thêm cookie vào response
        response.addCookie(cookie);
    }

    public String getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // Xóa cookie
        cookie.setPath("/"); // Đảm bảo cùng path với cookie gốc
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Phù hợp với setCookie
        response.addCookie(cookie);
    }

    public boolean hasCookie(HttpServletRequest request, String name) {
        if (name == null) {
            return false;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, String> getAllCookies(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }
}
