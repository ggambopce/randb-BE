package com.jinho.randb.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.like.domain.PostLike;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostLikeDto {
    private Long like_id;
    private Long content_id;
    private String content;
    private String title;

    public PostLikeDto(Long id,Long content_id, String content, String title) {
        this.like_id = id;
        this.content_id=content_id;
        this.content = content;
        this.title = title;
    }

    public PostLikeDto(Long id,Long content_id, String title) {
        this.like_id = id;
        this.content_id=content_id;
        this.title = title;
    }

    public static PostLikeDto Post_of(PostLike postLike){
        return new PostLikeDto(postLike.getId(),postLike.getPost().getId(),postLike.getPost().getPostContent(),postLike.getPost().getPostTitle());
    }
}
