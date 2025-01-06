package com.jinho.randb.domain.votes.application;

import com.jinho.randb.domain.votes.dto.VoteRequestDto;
import com.jinho.randb.domain.votes.dto.VoteResultDto;

public interface VoteService {

    void saveVote(VoteRequestDto voteRequest, Long accountId);

    VoteResultDto getVoteResults(Long postId);


}
