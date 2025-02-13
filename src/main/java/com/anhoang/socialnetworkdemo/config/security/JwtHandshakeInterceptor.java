package com.anhoang.socialnetworkdemo.config.security;

import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.utils.JwtTokenUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtTokenUtils jwtTokenUtils;
    private final UsersRepository usersRepository;
    public JwtHandshakeInterceptor(JwtTokenUtils jwtTokenUtils, UsersRepository usersRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = request.getURI().getQuery().replace("token=", "");
        if (!token.isEmpty()) {
            if (jwtTokenUtils.verifyToken(token)) {
                String email = jwtTokenUtils.extractUsername(token);
                Users users = usersRepository.findUsersByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Users not found!"));
                attributes.put("userCode", users.getUserCode());
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
