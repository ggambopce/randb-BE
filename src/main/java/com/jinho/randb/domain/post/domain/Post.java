package com.jinho.randb.domain.post.domain;

import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_content", length = 1000)
    private String postContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(hidden = true)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile; // 작성자의 프로필 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType; // 상태

    private LocalDateTime completedAt;

    public void updatePostType(PostType newType) {
        this.postType = newType;
        this.updatedAt = LocalDateTime.now(); // 상태 변경 시 업데이트 시간 갱신
    }

    public void update(String postTitle, String postContent){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.updatedAt = LocalDateTime.now().withNano(0).withSecond(0);
    }

    public static Post createPost(String postTitle, String postContent, Profile profile,Account account, PostType postType) {
        return Post.builder()
                .postTitle(postTitle)
                .postContent(postContent)
                .profile(profile)
                .account(account)
                .postType(postType)
                .build();
    }
}
