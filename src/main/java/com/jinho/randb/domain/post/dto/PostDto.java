package com.jinho.randb.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private Long id;

    private String postTitle;

    private String postContent;

    private String username;

    private PostType type; // 토론글 상태

    public PostDto toDto() {
        return new PostDto(id, postTitle, postContent, username, type);
    } //
    // 정적 팩토리 메서드
    public static PostDto from(Long id, String postTitle, String postContent, PostType type){ // 필요한 데이터만 postDto객체 생성
        return new PostDto(id, postTitle, postContent, null, type);
    }

    public static PostDto fromEntity(Post post) { // post엔티티를 postDTO로 변환
        return new PostDto(
                post.getId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getAccount().getUsername(),
                post.getType()
        );
    }

    public static PostDto of(Post post){
        return new PostDto(
                post.getId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getAccount().getUsername(),
                post.getType()
        );
    }



}
