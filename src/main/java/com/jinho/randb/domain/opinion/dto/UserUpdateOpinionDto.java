package com.jinho.randb.domain.opinion.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class UserUpdateOpinionDto {

    @Schema(description = "수정할 의견 내용", example = "수정할 의견 작성")
    @NotBlank(message = "수정할 의견을 입력해주세요")
    private String opinionContent;

    @Schema(description = "의견 id", example = "1")
    private Long opinionId;

    @Schema(hidden = true)
    private LocalDateTime updated_at;

    @JsonIgnore
    @JsonCreator
    public UserUpdateOpinionDto(@JsonProperty("opinionContent") String opinionContent,
                             @JsonProperty("opinionId") Long opinionId,
                             @JsonProperty("updated_at")LocalDateTime updated_at) {
        this.opinionContent = opinionContent;
        this.opinionId = opinionId;
        this.updated_at = updated_at;
    }
    
}
