package com.jinho.randb.domain.opinion.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class OpinionDto {

    private Long id;

    @Schema(description = "의견 내용", example = "의견 작성!")
    private String opinionContent;

    @Schema(description = "작성자 이름", example = "정의란 무엇인가")
    private String username;

    private OpinionType opinionType;

    private AccountDto account;

    private LocalDateTime create_at;        //등록일

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updated_at;       //수정일

    public OpinionDto of(Opinion opinion) {
        return OpinionDto.builder()
                .id(opinion.getId())
                .opinionContent(opinion.getOpinionContent())
                .opinionType(opinion.getOpinionType())
                .username(opinion.getAccount().getUsername())
                .create_at(opinion.getCreated_at())
                .updated_at(opinion.getUpdated_at()).build();
    }

    // Opinion 객체로부터 OpinionDto로 변환하는 메서드
    public static OpinionDto from(Opinion opinion) {

        return OpinionDto.builder()
                .id(opinion.getId())
                .opinionContent(opinion.getOpinionContent())
                .opinionType(opinion.getOpinionType())
                .username(opinion.getAccount().getUsername())
                .account(AccountDto.from(opinion.getAccount()))  // Account도 DTO로 변환
                .create_at(opinion.getCreated_at())
                .updated_at(opinion.getUpdated_at())
                .build();
    }

}
