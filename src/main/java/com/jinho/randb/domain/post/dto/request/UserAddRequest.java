package com.jinho.randb.domain.post.dto.request;

import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserAddRequest {

    @Schema(description = "토론글 제목", example = "토론제목!")
    @NotBlank(message = "토론 제목을 입력해주세요")
    private String postTitle;

    @Schema(description = "토론글 내용", example = "토론내용!")
    @NotBlank(message = "토론 내용을 입력해주세요")
    private String postContent;

}



