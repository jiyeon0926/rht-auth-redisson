package com.example.demo.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResDto {

    private final String tokenAuthScheme;
    private final String accessToken;
    private final String refreshToken;
}
