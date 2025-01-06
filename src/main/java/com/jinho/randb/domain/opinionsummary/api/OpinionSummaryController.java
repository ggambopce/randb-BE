package com.jinho.randb.domain.opinionsummary.api;

import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinion.dto.OpinionDto;
import com.jinho.randb.domain.opinionsummary.application.OpinionSummaryService;
import com.jinho.randb.domain.opinionsummary.dto.OpinionSummaryDto;
import com.jinho.randb.domain.opinionsummary.dto.OpinionSummaryResponseDto;
import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.exception.PostException;
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
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.jinho.randb.domain.post.api.PostController.getErrorResponseResponseEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@OpenAPIDefinition(tags = {
        @Tag(name = "일반 사용자 의견요약 컨트롤러", description = "토론글 작성자 의견요약 관련 작업")
})
@Slf4j
public class OpinionSummaryController {

    private final OpinionSummaryService opinionSummaryService;
    private final PostService postService;


    @Operation(summary = "의견요약 작성 API", description = "의견요약 작성", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"작성 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"모든 값을 입력해 주세요\"}")))
    })
    @PostMapping(value = "/user/opinionSummary")
    public ResponseEntity<?> opinionSummaryAdd(@RequestParam("postId") Long postId){

        try{
            // 의견 요약 저장 및 토론글 상태 변경
            Map<String, String> summaries = opinionSummaryService.summarizeAndSave(postId);
            postService.updatePostType(postId,PostType.VOTING);

            return  ResponseEntity.ok(new ControllerApiResponse<>(true,"요약글 작성 성공 및 상태 변경 완료", summaries));
        } catch (NoSuchElementException e) {
            throw new PostException(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException("서버오류", e);
        }
    }

    @Operation(summary = "토론글 요약 조회 API", description = "특정 토론글의 요약 정보를 조회합니다.", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"조회 성공\", \"data\" : {\"RED\" : \"요약 내용\", \"BLUE\" : \"요약 내용\"}}"))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"요약 정보를 찾을 수 없습니다.\"}")))
    })
    @GetMapping(value = "/user/opinionSummary")
    public ResponseEntity<?> getOpinionSummary(@RequestParam("postId") Long postId) {
        try {
            // 서비스 호출하여 DTO 반환
            OpinionSummaryResponseDto response = opinionSummaryService.findOpinionSummaryByPostId(postId);

            return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회 성공", response));
        } catch (NoSuchElementException e) {
            throw new PostException("해당 토론글에 대한 요약 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException("서버오류", e);
        }
    }

}
