package com.example.demo.domain.auth.service;

import com.example.demo.global.auth.jwt.JwtProvider;
import com.example.demo.global.common.constants.TokenConstants;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedissonClient redissonClient;
    private final JwtProvider jwtProvider;

    // 이메일을 Key로 가지는 Refresh 토큰 저장
    public void saveRefreshToken(String refreshToken, String email) {
        Claims claims = jwtProvider.getClaims(refreshToken);
        long expiration = claims.getExpiration().getTime();

        RBucket<String> bucket = redissonClient.getBucket(TokenConstants.REFRESH_TOKEN_KEY + email);
        bucket.set(refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String email) {
        redissonClient.getBucket(TokenConstants.REFRESH_TOKEN_KEY + email).delete();
    }

    public String getRefreshToken(String email) {
        RBucket<String> bucket = redissonClient.getBucket(TokenConstants.REFRESH_TOKEN_KEY + email);

        return bucket.get();
    }

    public void validRefreshToken(String email, String refreshToken) {
        String token = getRefreshToken(email);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token이 존재하지 않습니다.");
        }

        if (!token.equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }
    }

    // Access 토큰을 블랙리스트에 저장하여 관리
    public void saveAccessToken(String accessToken) {
        long now = new Date().getTime();
        Claims claims = jwtProvider.getClaims(accessToken);
        Date expiration = claims.getExpiration();
        long remainExpiration = expiration.getTime() - now;

        // Access 토큰이 아직 유효하다면 블랙리스트에 저장
        if (remainExpiration > 0) {
            RBucket<String> bucket = redissonClient.getBucket(TokenConstants.BLACKLIST_KEY + accessToken);
            bucket.set(TokenConstants.BLACKLIST_VALUE, remainExpiration, TimeUnit.MILLISECONDS);
        }
    }

    // BlackList key가 존재하면 true
    public boolean validBlackList(String accessToken) {
        return redissonClient.getBucket(TokenConstants.BLACKLIST_KEY + accessToken).isExists();
    }
}