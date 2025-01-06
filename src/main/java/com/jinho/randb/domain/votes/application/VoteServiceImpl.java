package com.jinho.randb.domain.votes.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.votes.dao.VoteRepository;
import com.jinho.randb.domain.votes.domain.VoteType;
import com.jinho.randb.domain.votes.domain.Votes;
import com.jinho.randb.domain.votes.dto.VoteRequestDto;
import com.jinho.randb.domain.votes.dto.VoteResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jinho.randb.domain.account.domain.QAccount.account;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    // 투표 생성
    public void saveVote(VoteRequestDto voteRequest, Long accountId) {
        // 중복 투표 방지
        boolean hasVoted = voteRepository.existsByPostIdAndAccountId(voteRequest.getPostId(), accountId);
        if (hasVoted) {
            throw new IllegalArgumentException("이미 해당 토론글에 투표했습니다.");
        }

        // 토론글 조회
        Post post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 토론글을 찾을 수 없습니다."));

        // 사용자 조회
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 투표 저장
        Votes votes = Votes.builder()
                .post(post)
                .account(account)
                .voteType(voteRequest.getVoteType())
                .build();

        voteRepository.save(votes);
    }

    // 특정 토론글의 투표 결과 조회
    public VoteResultDto getVoteResults(Long postId) {
        List<Votes> votes = voteRepository.findByPostId(postId);

        long redVotes = votes.stream()
                .filter(vote -> vote.getVoteType() == VoteType.RED)
                .count();

        long blueVotes = votes.stream()
                .filter(vote -> vote.getVoteType() == VoteType.BLUE)
                .count();

        VoteResultDto resultDto = new VoteResultDto();
        resultDto.setRedVotes(redVotes);
        resultDto.setBlueVotes(blueVotes);
        return resultDto;
    }
}
