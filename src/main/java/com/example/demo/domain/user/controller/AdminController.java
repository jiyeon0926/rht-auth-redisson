package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.SignupReqDto;
import com.example.demo.domain.user.dto.SignupResDto;
import com.example.demo.domain.user.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> adminSignup(@Valid @RequestBody SignupReqDto signupReqDto) {
        SignupResDto adminSignup = adminService.adminSignup(signupReqDto.getEmail(), signupReqDto.getPassword(), signupReqDto.getName());

        return new ResponseEntity<>(adminSignup, HttpStatus.CREATED);
    }
}
