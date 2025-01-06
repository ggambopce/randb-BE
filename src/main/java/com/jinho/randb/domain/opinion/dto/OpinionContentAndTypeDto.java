package com.jinho.randb.domain.opinion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpinionContentAndTypeDto {

    private Long id;

    private String opinionContent;

    @Schema(description = "작성자 이름", example = "정의란 무엇인가")
    private String username;

    private OpinionType opinionType;

    private LocalDateTime create_at;        //등록일

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updated_at;       //수정일

    public static OpinionContentAndTypeDto from(Opinion opinion) {

        return OpinionContentAndTypeDto.builder()
                .id(opinion.getId())
                .opinionContent(opinion.getOpinionContent())
                .username(opinion.getAccount().getUsername())
                .opinionType(opinion.getOpinionType())
                .create_at(opinion.getCreated_at())
                .updated_at(opinion.getUpdated_at())
                .build();
    }
}
