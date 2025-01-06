package com.jinho.randb.domain.account.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "닉네임검증 Reqeust")
public class NicknameValidRequest {

    @Schema(description = "사용자 닉네임", example = "철학자")
    private String nickname;
}