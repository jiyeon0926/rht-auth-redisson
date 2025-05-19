package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.dto.LoginResDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.jwt.JwtProvider;
import com.example.demo.global.common.enums.AuthenticationScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    // 로그인
    public LoginResDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        validPassword(password, user.getPassword()); // 비밀번호 검증

        // 인증된 객체를 SecurityContext에 저장
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기반으로 JWT 생성
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), accessToken, refreshToken);
    }

    private void validPassword(String rawPassword, String encodedPassword) {
        boolean notValid = !passwordEncoder.matches(rawPassword, encodedPassword);

        if (notValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }
    }
}
