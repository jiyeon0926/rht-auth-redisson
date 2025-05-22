package com.example.demo.domain.auth.controller;

import com.example.demo.domain.auth.dto.LoginReqDto;
import com.example.demo.domain.auth.dto.LoginResDto;
import com.example.demo.domain.auth.dto.RefreshReqDto;
import com.example.demo.domain.auth.dto.TokenDto;
import com.example.demo.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto) {
        LoginResDto login = authService.login(loginReqDto.getEmail(), loginReqDto.getPassword());

        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody RefreshReqDto refreshReqDto) {
        TokenDto refresh = authService.refresh(refreshReqDto.getRefreshToken());

        return new ResponseEntity<>(refresh, HttpStatus.OK);
    }
}
