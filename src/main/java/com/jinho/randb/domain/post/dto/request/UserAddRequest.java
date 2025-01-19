package com.jinho.randb.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAddRequest {

    @Schema(description = "토론글 제목", example = "토론제목!")
    @NotBlank(message = "토론 제목을 입력해주세요")
    private String postTitle;

    @Schema(description = "토론글 내용", example = "토론내용!")
    @NotBlank(message = "토론 내용을 입력해주세요")
    private String postContent;

}



