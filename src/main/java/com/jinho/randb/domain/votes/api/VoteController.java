package com.jinho.randb.domain.votes.api;

import com.jinho.randb.domain.votes.application.VoteService;
import com.jinho.randb.domain.votes.dto.VoteRequestDto;
import com.jinho.randb.domain.votes.dto.VoteResultDto;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class VoteController {

    private final VoteService voteService;

    @PostMapping("api/user/posts/votes")
    public ResponseEntity<?> addVote(@RequestBody VoteRequestDto voteRequest) {
        try {
            // SecurityContextHolder에서 인증 정보 추출
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ControllerApiResponse<>(false, "로그인이 필요합니다."));
            }

            // PrincipalDetails 객체를 가져옴
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof PrincipalDetails)) {
                throw new IllegalArgumentException("인증 정보가 올바르지 않습니다.");
            }

            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Long accountId = principalDetails.getAccount().getId(); // 로그인 사용자 ID 추출


            voteService.saveVote(voteRequest, accountId); // accountId 전달
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
