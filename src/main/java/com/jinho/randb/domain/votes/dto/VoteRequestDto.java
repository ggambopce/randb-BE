package com.jinho.randb.domain.votes.dto;

import com.jinho.randb.domain.votes.domain.VoteType;
import lombok.Data;

@Data
public class VoteRequestDto {
    private Long postId; // 투표할 토론글 ID
    private VoteType voteType; // RED 또는 BLUE
}
