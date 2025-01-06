package com.jinho.randb.domain.opinion.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class AddOpinionRequest {

    @Schema(description = "의견 내용", example = "의견 작성")
    @NotBlank(message = "의견을 입력해주세요")
    private String opinionContent;

    @NotNull
    private OpinionType opinionType;

    @Schema(description = "사용자 id", example = "1")
    private  Long accountId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private  String username;

    @Schema(description = "토론글 id", example = "1")
    private Long postId;

    @Schema(hidden = true)
    private LocalDateTime created_at;

    @JsonIgnore
    @JsonCreator
    public AddOpinionRequest(@JsonProperty("opinionContent") String opinionContent,
                             @JsonProperty("opinionType") OpinionType opinionType,
                             @JsonProperty("accountId") Long accountId,
                             @JsonProperty("username") String username,
                             @JsonProperty("postId") Long postId,
                             @JsonProperty("create_at")LocalDateTime created_at) {
        this.opinionContent = opinionContent;
        this.opinionType = opinionType;
        this.accountId = accountId;
        this.username = username;
        this.postId = postId;
        this.created_at = created_at;
    }

}
