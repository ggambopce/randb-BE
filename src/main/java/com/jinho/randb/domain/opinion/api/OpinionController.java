package com.jinho.randb.domain.opinion.api;

import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinion.dto.OpinionDto;
import com.jinho.randb.domain.opinion.dto.UserUpdateOpinionDto;
import com.jinho.randb.domain.opinion.exception.OpinionException;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping
@OpenAPIDefinition(tags = {
        @Tag(name = "일반 사용자 의견 컨트롤러", description = "일반 사용자 의견 관련 작업")
})
@Slf4j
public class OpinionController {

    private final OpinionService opinionService;

    @Operation(summary = "의견 작성 API", description = "로그인한 사용자만 의견을 작성 가능", tags = {"일반 사용자 의견 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\":\"의견 작성 성공\", \"data\" : {\"opinionContent\": \"[작성한 의견]\", \"memberId\": \"[사용자 ID]\", \"postId\": \"[게시글 ID]\", \"created_at\": \"LocalDateTime\"}}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "[{\"success\":false,\"message\":\"의견을 입력해주세요\"}, {\"success\":false,\"message\":\"회원정보나 게시글을 찾을수 없습니다.\"}]"))),
    })
    @PostMapping("/api/user/opinions")
    public ResponseEntity<?> opinionAdd(@Valid @RequestBody AddOpinionRequest addOpinionRequest, BindingResult bindingResult){

        try {
            // 요청 유효성 검사
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(new ErrorResponse<>(false, bindingResult.getFieldError().getDefaultMessage()));
            }

            // 의견 저장
            opinionService.save(addOpinionRequest);

            return ResponseEntity.ok(new ControllerApiResponse<>(true, "성공"));
        } catch (NoSuchElementException e) {
            throw new OpinionException("존재하지 않는 항목: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException("서버 오류 발생", e);
        }
    }

    @Operation(summary = "의견 전체 조회 API", description = "해당 토론글의 의견을 모두 조회", tags = {"일반 사용자 의견 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Post.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"조회 성공\",\"opinions\":[{\"id\":23, \"opinionContent\" : \"이것은 의견의 내용입니다.\"}]}")))
    })
    @GetMapping("/api/opinions")
    public ResponseEntity<?> findAllOpinion(@Parameter(description = "토론 Id")@RequestParam(value = "postId", required = true)Long postId) {
        List<OpinionContentAndTypeDto> opinions = opinionService.findByPostId(postId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", opinions));
    }


    @Operation(summary = "의견 삭제 API",description = "삭제시 해당 게시물과 관련된 데이터는 모두 삭제",tags = {"일반 사용자 의견 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"의견 삭제 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"게시글을 찾을수 없습니다.\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"작성자만 삭제할수 있습니다.\"}")))
    })
    @DeleteMapping("/api/user/opinions/{opinion-id}")
    public ResponseEntity<?> deleteOpinion(@PathVariable("opinion-id") Long opinionId){
        opinionService.delete(opinionId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "게시글 삭제 성공"));
    }

    @Operation(summary = "의견 수정 API", description = "로그인한 사용자만 수정이 가능하며 작성자만 수정이 가능하도록 이전에 비밀번호 검증을 통해서 검증확인해 해당 API 접근가능", tags = {"일반 사용자 의견 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"의견 수정 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"게시글을 찾을수 없습니다.\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"작성자만 수정할 수 있습니다.\"}")))
    })
    @PostMapping("/api/user/update/opinions/{opinion-id}")
    public ResponseEntity<?> updateOpinion(@Valid @RequestBody UserUpdateOpinionDto userUpdateOpinionDto, @PathVariable("opinion-id") Long opinionId){

        opinionService.update(opinionId,userUpdateOpinionDto);

        return ResponseEntity.ok(new ControllerApiResponse(true,"작성 성공"));
    }

}