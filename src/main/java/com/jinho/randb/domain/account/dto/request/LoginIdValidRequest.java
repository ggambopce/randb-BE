package com.jinho.randb.domain.account.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "로그인 아이디 검증 Request")
public class LoginIdValidRequest {
    @NotEmpty(message = "아이디를 입력해주세요")
    @Schema(description = "아이디 조건검사 대문자나 소문자(5~16)",example = "exampleId")
    private String loginId;
}
