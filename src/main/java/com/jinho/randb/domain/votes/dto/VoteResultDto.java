package com.jinho.randb.domain.votes.dto;

import lombok.Data;

@Data
public class VoteResultDto {
    private Long redVotes; // RED 투표 수
    private Long blueVotes; // BLUE 투표 수
}
