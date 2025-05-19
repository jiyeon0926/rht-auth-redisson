package com.example.demo.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SignupResDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;
}
