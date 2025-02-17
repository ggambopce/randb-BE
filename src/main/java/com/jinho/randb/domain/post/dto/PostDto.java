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

    private String nickname;

    private Long profileId;

    private PostType postType; // 토론글 상태

    private Integer likeCount;      // 좋아요 수

    public PostDto toDto() {
        return new PostDto(id, postTitle, postContent, nickname, profileId, postType, likeCount);
    }
    // 정적 팩토리 메서드
    public static PostDto from(Long id, String postTitle, String postContent, PostType type, Integer likeCount){ // 메이페이지에는 작성자 정보 노출 안함
        return new PostDto(id, postTitle, postContent, null, null, type, likeCount);
    }

    public static PostDto fromEntity(Post post) { // 전체 목록 조회시 사용, 엔티티를 DTO로 변환
        return new PostDto(
                post.getId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getProfile().getNickname(),
                post.getProfile().getId(),
                post.getPostType(),
                post.getLikeCount()
        );
    }

    public static PostDto of(Post post){ // 단건 조회시 사용
        return new PostDto(
                post.getId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getProfile().getNickname(),
                post.getProfile().getId(),
                post.getPostType(),
                post.getLikeCount()
        );
    }



}
