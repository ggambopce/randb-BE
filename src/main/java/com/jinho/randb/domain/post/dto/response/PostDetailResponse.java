package com.jinho.randb.domain.post.dto.response;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDetailResponse {

    private PostDto post;

    public PostDetailResponse(PostDto postDto) {
        this.post = postDto;
    }

    public static PostDetailResponse of(PostDto postDto){
        return new PostDetailResponse(postDto);
    }


}
