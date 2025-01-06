package com.jinho.randb.domain.post.dto.response;

import com.jinho.randb.domain.post.dto.PostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Schema(name = "토론글 페이징 Response")
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Boolean nextPage; // 다음페이지 존재여부
    private List<PostDto> posts; // 현재페이지 객체리스트

    public PostResponse(boolean nextPage, List<PostDto> posts) {
        this.nextPage = nextPage;
        this.posts = posts;
    }
}
