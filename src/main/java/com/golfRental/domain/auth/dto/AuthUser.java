package com.golfRental.domain.auth.dto;

import com.golfRental.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, UserRole role) {
        this.userId = userId;
        this.authorities = List.of(new SimpleGrantedAuthority(role.getUserRole()));
    }
}
