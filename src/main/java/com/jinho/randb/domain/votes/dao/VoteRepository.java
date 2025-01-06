package com.jinho.randb.domain.votes.dao;

import com.jinho.randb.domain.votes.domain.VoteType;
import com.jinho.randb.domain.votes.domain.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Votes, Long> {

    // 특정 토론글의 모든 투표 조회
    List<Votes> findByPostId(Long postId);

    // 특정 토론글과 특정 사용자의 투표 여부 조회
    boolean existsByPostIdAndAccountId(Long postId, Long accountId);

    @Query("SELECT COUNT(v) FROM Votes v WHERE v.post.id = :postId AND v.voteType = :voteType")
    int countByPostAndVoteType(@Param("postId") Long postId, @Param("voteType") VoteType voteType);

}
