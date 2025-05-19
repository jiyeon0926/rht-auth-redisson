package com.example.demo.domain.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/auth/check")
    public ResponseEntity<String> authCheck() {
        return new ResponseEntity<>("인증된 사용자입니다.", HttpStatus.OK);
    }

    @GetMapping("/admins/check")
    public ResponseEntity<String> adminCheck() {
        return new ResponseEntity<>("관리자만 접근할 수 있습니다.", HttpStatus.OK);
    }

    @GetMapping("/users/check")
    public ResponseEntity<String> userCheck() {
        return new ResponseEntity<>("사용자와 관리자 모두 접근할 수 있습니다.", HttpStatus.OK);
    }
}
