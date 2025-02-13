package com.anhoang.socialnetworkdemo.config.security.user_detail;

import com.anhoang.socialnetworkdemo.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String userCode;
    private String fullName;
    private String email;
    private String password;
    private Users.Status status;
    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(this.status == Users.Status.BINH_THUONG){
           return true;
        } else if(this.status == Users.Status.KHOA){
            return false;
        }
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(this.status == Users.Status.BINH_THUONG){
            return true;
        } else if(this.status == Users.Status.CHUA_XAC_NHAN){
            return false;
        }
        return false;
    }

}
