package com.jinho.randb.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStatisticsResponseDto {

    private int redVotes;
    private int blueVotes;
    private String winningVoteType;
    private double redVotePercentage;
    private double blueVotePercentage;
}
