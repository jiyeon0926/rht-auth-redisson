package com.example.demo.global.common.enums;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public enum UserRole {
    USER,
    ADMIN;

    public static UserRole of(String roleName) {
        for (UserRole role : values()) {
            if (role.name().equals(roleName.toUpperCase())) {
                return role;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 이름의 권한을 찾을 수 없습니다: " + roleName);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + name()));
    }
}
