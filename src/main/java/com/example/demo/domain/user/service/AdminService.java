package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.SignupResDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 관리자 회원가입
    @Transactional
    public SignupResDto adminSignup(String email, String password, String name) {
        userRepository.findByEmail(email)
                .ifPresent(admin -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
                });

        String encodedPassword = passwordEncoder.encode(password);

        User admin = new User(email, encodedPassword, name, UserRole.ADMIN.name());
        User savedAdmin = userRepository.save(admin);

        return new SignupResDto(
                savedAdmin.getId(),
                savedAdmin.getEmail(),
                savedAdmin.getName(),
                savedAdmin.getRole().name(),
                savedAdmin.getCreateAt(),
                savedAdmin.getUpdateAt()
        );
    }
}
