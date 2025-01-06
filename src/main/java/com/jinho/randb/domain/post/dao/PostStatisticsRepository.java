package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostStatisticsRepository extends JpaRepository<PostStatistics, Long> {
    Optional<PostStatistics> findByPost(Post post);

    Optional<PostStatistics> findByPostId(Long postId);
}
