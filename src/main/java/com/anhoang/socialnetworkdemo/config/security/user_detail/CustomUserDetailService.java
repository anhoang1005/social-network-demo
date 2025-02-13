package com.anhoang.socialnetworkdemo.config.security.user_detail;

import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findUsersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Users not found!"));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : users.getRolesList()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()));
        }
        return CustomUserDetails.builder()
                .id(users.getId())
                .userCode(users.getUserCode())
                .email(users.getEmail())
                .status(users.getStatus())
                .password(users.getPassword())
                .fullName(users.getFullName())
                .authorities(authorities)
                .build();
    }
}
