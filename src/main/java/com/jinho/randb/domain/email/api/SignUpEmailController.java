package com.jinho.randb.domain.email.api;

import com.jinho.randb.domain.email.application.EmailService;
import com.jinho.randb.domain.email.dto.request.EmailVerificationRequest;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Tag(name = "공용 - 이메일 전송 컨트롤러",description = "이메일 인증 번호 전송 및 검증")
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class SignUpEmailController {

    @Qualifier("JoinEmail")
    private final EmailService emailService;

    @Operation(summary = "회원가입 이메일 인증번호 전송", description = "회원가입시 이메일 인증번호 전송 API 이메일이 이미 가입된 경우에는 false가 반환됩니다. 단, 소셜 로그인은 이 기능에서 제외.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"메일 전송 성공\"}")))
    })
    @PostMapping("/email-confirmation")
    public ResponseEntity<?> sendVerificationCode(@Parameter(description = "이메일 주소") @RequestParam("email") String email){
        emailService.sendMailMessage(email);
        return ResponseEntity.ok(new ControllerApiResponse(true,"메일 전송 성공"));
    }

    @Operation(summary = "회원가입 이메일 인증번호 검증",    description = "이메일 인증을 위한 인증 번호를 검증하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"인증 번호 검증\" ,\"data\" : {\"isVerified\": true, \"isExpired\" :true}}"))),
            @ApiResponse(responseCode = "400",description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":false,\"message\":\"실패\",\"data\":{\"필드명\" : \"필드 오류 내용\"}}")))
    })
    @PostMapping("/email-confirmation/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody EmailVerificationRequest signUpEmailVerificationRequest, BindingResult bindingResult){

        Map<String, Boolean> stringBooleanMap = emailService.verifyCode(signUpEmailVerificationRequest.getEmail(), signUpEmailVerificationRequest.getCode());

        return ResponseEntity.ok(new ControllerApiResponse<>(true,"인증 번호 검증",stringBooleanMap));
    }
}
