package com.example.demo.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupReqDto {

    @NotBlank(message = "email은 필수 항목 입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank(message = "password는 필수 항목 입니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private final String password;

    @NotBlank(message = "name은 필수 항목 입니다.")
    private final String name;

    @JsonCreator
    public SignupReqDto(@JsonProperty("email") String email,
                        @JsonProperty("password") String password,
                        @JsonProperty("name") String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
