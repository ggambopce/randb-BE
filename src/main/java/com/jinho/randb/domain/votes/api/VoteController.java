package com.jinho.randb.domain.votes.api;

import com.jinho.randb.domain.votes.application.VoteService;
import com.jinho.randb.domain.votes.dto.VoteRequestDto;
import com.jinho.randb.domain.votes.dto.VoteResultDto;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.jinho.randb.domain.post.api.PostController.getErrorResponseResponseEntity;

@RequiredArgsConstructor
@RestController
public class VoteController {

    private final VoteService voteService;

    @PostMapping("api/user/posts/votes")
    public ResponseEntity<?> addVote(@RequestBody VoteRequestDto voteRequest, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {

            // 요청 유효성 검사
            ResponseEntity<ErrorResponse<Map<String, String>>> errorMap = getErrorResponseResponseEntity(bindingResult);
            if (errorMap != null) return errorMap;


            voteService.saveVote(voteRequest, principalDetails.getAccountId()); // accountId 전달
            return ResponseEntity.ok(new ControllerApiResponse<>(true, "투표가 성공적으로 처리되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ControllerApiResponse<>(false, e.getMessage()));
        }
    }

    // 투표 결과 조회 API
    @GetMapping("api/user/posts/votes/{postId}")
    public ResponseEntity<?> getVoteResults(@PathVariable("postId") Long postId) {
        VoteResultDto results = voteService.getVoteResults(postId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "투표 결과 조회 성공", results));
    }


}
