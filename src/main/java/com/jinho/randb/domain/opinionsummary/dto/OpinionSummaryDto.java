package com.jinho.randb.domain.opinionsummary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
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
public class OpinionSummaryDto {

    @Schema(description = "토론글 ID", example = "1")
    private Long postId; // 토론글 ID 추가

    @Schema(description = "의견 요약", example = "요약 완성!")
    private String opinionSummaryContent;

    private OpinionType opinionType;

    private LocalDateTime created_at;

    public OpinionSummaryDto of(Opinion opinion) {
        return OpinionSummaryDto.builder()
                .postId(opinion.getPost().getId()) // 의견의 토론글 ID 추가
                .opinionSummaryContent(opinion.getOpinionContent())
                .opinionType(opinion.getOpinionType())
                .created_at(opinion.getCreated_at()).build();
    }

    // Opinion 객체로부터 OpinionSummaryDto로 변환하는 메서드
    public static OpinionSummaryDto from(Opinion opinion) {
        return OpinionSummaryDto.builder()
                .postId(opinion.getPost().getId()) // 의견의 토론글 ID 추가
                .opinionSummaryContent(opinion.getOpinionContent())
                .opinionType(opinion.getOpinionType())
                .created_at(opinion.getCreated_at()).build();
    }
}
