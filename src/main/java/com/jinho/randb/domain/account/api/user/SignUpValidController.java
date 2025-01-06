package com.jinho.randb.domain.account.api.user;

import com.jinho.randb.domain.account.application.user.SignUpService;
import com.jinho.randb.domain.account.dto.request.EmailValidRequest;
import com.jinho.randb.domain.account.dto.request.JoinRequest;
import com.jinho.randb.domain.account.dto.request.LoginIdValidRequest;
import com.jinho.randb.domain.account.dto.request.NicknameValidRequest;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name="일반 - 회원가입 컨트롤러", description = "이메일 회원가입 및 검증 처리")
@RequestMapping("/api")
public class SignUpValidController {

    private final SignUpService signUpService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"회원가입 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":false,\"message\":\"실패\",\"data\":{\"필드명\" : \"필드 오류 내용\"}}"))),
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody JoinRequest joinRequest, BindingResult bindingResult) {
        boolean validationOfSignUp = signUpService.ValidationOfSignUp(JoinRequest.fromDto(joinRequest));

        if (!validationOfSignUp || bindingResult.hasErrors()){
            Map<String, String> map = signUpService.ValidationErrorMessage(JoinRequest.fromDto(joinRequest));
            return getErrorResponseResponse(bindingResult,map);
        }

        // 유효성 검증 통과 시 서비스 로직 실행
        signUpService.joinAccount(JoinRequest.fromDto(joinRequest));

        return ResponseEntity.ok(new ControllerApiResponse<>(true,"회원가입 성공"));
    }

    @Operation(summary = "로그인아이디 검증", description = "사용 가능한 아이디인지를 검증하는 API. 검증 조건은 대소문자를 포함한 5~16자의 문자열이며, 중복 여부도 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"사용 가능\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"사용할수 없는 아이디입니다.\"}")))
    })
    @PostMapping("/signup/register/validation")
    public ResponseEntity<ControllerApiResponse> LoginIdValid(@RequestBody LoginIdValidRequest loginIdValidRequest){


        signUpService.LoginIdValid(loginIdValidRequest.getLoginId());;
        return ResponseEntity.ok(new ControllerApiResponse<>(true,"사용 가능"));
    }

    @Operation(summary = "이메일 검증",description = "사용 가능한 이메일인지를 검증하는 API 가입할 수 있는 이메일 형식은 '이메일주소@도메인'이며, 'com' 또는 'net' 도메인만 허용됩니다. 이메일 중복 여부와 사용 가능 여부가 모두 true일 때에만 사용할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"이메일 사용 가능\",\"data\" :{ \"duplicateEmail\":true, \"useEmail\":true,\"blackListEmail\":false}}"))),
            @ApiResponse(responseCode = "400",description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":false,\"message\":\"실패\",\"data\":{\"필드명\" : \"필드 오류 내용\"}}")))
    })
    @PostMapping("/join/email/validation")
    public ResponseEntity<ControllerApiResponse> emailValid(@javax.validation.Valid @RequestBody EmailValidRequest emailValidRequest, BindingResult result){
        Map<String, Boolean> stringBooleanMap = signUpService.emailValid(emailValidRequest.getEmail());
        return ResponseEntity.ok(new ControllerApiResponse<>(true,"이메일 사용 가능",stringBooleanMap));
    }

    @Operation(summary = "닉네임 검증",description = "닉네임이 대소문자, 한글, 숫자로 이뤄진 4글자 이상이어야 하며 중복 여부도 검증가능. (true = 사용 가능, false = 사용 불가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"닉네임 사용 가능\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"사용 불가능한 닉네임 입니다.\"}")))
    })
    @PostMapping("/join/nickname/validation")
    public ResponseEntity<?> nickName(@RequestBody NicknameValidRequest nicknameValidRequest){
        signUpService.nickNameValid(nicknameValidRequest.getNickname());
        return ResponseEntity.ok(new ControllerApiResponse<>(true,"닉네임 사용 가능"));
    }



    private static ResponseEntity<ErrorResponse<Map<String, String>>> getErrorResponseResponse(BindingResult bindingResult,Map<String, String> map) {
        Map<String, String> result = new LinkedHashMap<>();

        for(Map.Entry<String,String> entry : map.entrySet()){
            result.put(entry.getKey(),entry.getValue());
        }
        for (FieldError error : bindingResult.getFieldErrors()) {
            result.put(error.getField(),error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(new ErrorResponse<>(false, "실패", result));
    }

}
