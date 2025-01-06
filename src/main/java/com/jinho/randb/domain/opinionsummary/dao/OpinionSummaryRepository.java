package com.jinho.randb.domain.opinionsummary.dao;

import com.jinho.randb.domain.opinion.domain.OpinionType;
import com.jinho.randb.domain.opinionsummary.domain.OpinionSummary;
import com.jinho.randb.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpinionSummaryRepository extends JpaRepository<OpinionSummary, Long> {

    Optional<OpinionSummary> findByPostAndOpinionType(Post post, OpinionType opinionType);

    List<OpinionSummary> findByPostId(Long postId);
}
