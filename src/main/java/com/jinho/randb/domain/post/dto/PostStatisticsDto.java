package com.jinho.randb.domain.post.dto;

import com.jinho.randb.domain.post.domain.PostStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostStatisticsDto {

    private int redVotes;
    private int blueVotes;
    private String winningVoteType;
    private double redVotePercentage;
    private double blueVotePercentage;

    public static PostStatisticsDto fromEntity(PostStatistics statistics) {
        return new PostStatisticsDto(
                statistics.getRedVotes(),
                statistics.getBlueVotes(),
                statistics.getWinningVoteType(),
                statistics.getRedVotePercentage(),
                statistics.getBlueVotePercentage()
        );
    }
}
