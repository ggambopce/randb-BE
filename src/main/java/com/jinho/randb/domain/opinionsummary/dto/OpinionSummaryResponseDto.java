package com.jinho.randb.domain.opinionsummary.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpinionSummaryResponseDto {

    @Schema(description = "RED 요약 내용", example = "이것은 RED 입장을 요약한 내용입니다.")
    private String redSummary;

    @Schema(description = "BLUE 요약 내용", example = "이것은 BLUE 입장을 요약한 내용입니다.")
    private String blueSummary;

    // 정적 팩토리 메서드로 엔티티 목록을 DTO로 변환
    public static OpinionSummaryResponseDto fromSummaries(String redSummary, String blueSummary) {
        return OpinionSummaryResponseDto.builder()
                .redSummary(redSummary)
                .blueSummary(blueSummary)
                .build();
    }

}
