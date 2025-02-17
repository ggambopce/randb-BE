package com.jinho.randb.domain.like.api;

import com.jinho.randb.domain.like.application.LikeService;
import com.jinho.randb.domain.like.dto.request.PostLikeRequest;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final LikeService likeService;

    @PostMapping("/posts/like")
    public ResponseEntity<?> addLike(@RequestBody PostLikeRequest postLikeRequest,
                                     @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){
        Boolean addLike = likeService.addLike(postLikeRequest,principalDetails.getAccountId());

        ControllerApiResponse response;
        if (!addLike){
            response = new ControllerApiResponse(true,"좋아요 성공");
        }else
            response = new ControllerApiResponse(false, "좋아요 해제");

        return ResponseEntity.ok(response);
    }
}
