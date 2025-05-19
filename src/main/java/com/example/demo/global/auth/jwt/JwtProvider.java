package com.example.demo.global.auth.jwt;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    @Getter
    @Value("${jwt.refresh-expiry-millis}")
    private long refreshExpiryMillis;

    private final UserRepository userRepository;

    public String generateRefreshToken(Authentication authentication) {
        String username = authentication.getName();

        return generateRefreshTokenBy(username);
    }

    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();

        return generateAccessTokenBy(username);
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);

        return claims.getSubject();
    }

    public boolean validToken(String token) throws JwtException {
        try {
            return !tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰입니다.: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다.: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("유효하지 않은 JWT 서명입니다.: {}", e.getMessage());
        }

        return false;
    }

    // Access 토큰과 Refresh 토큰을 Claims 객체에 있는 role Key 존재 여부를 통해 구분
    public boolean validAccessToken(String token) {
        Claims claims = getClaims(token);

        return claims.containsKey("role");
    }

    // 토큰 재발급 용도이기 때문에 Claims 객체에 role 정보 안 넣음
    private String generateRefreshTokenBy(String email) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + refreshExpiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    private String generateAccessTokenBy(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean tokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return resolveClaims(token, Claims::getExpiration);
    }

    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);

        return claimsResolver.apply(claims);
    }
}
