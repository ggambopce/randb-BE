package com.jinho.randb.global.jwt.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {

    @NotEmpty(message = "아이디를 입력해주세요")
    @Schema(description = "로그인 아이디", example = "testId")
    private String loginId;

    @Schema(description = "로그인 비밀번호", example = "password")
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;
}
