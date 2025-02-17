package com.jinho.randb.domain.like.dao;

import com.jinho.randb.domain.like.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>, CustomPostLikeRepository {

    Boolean existsByAccountIdAndPostId(Long accountId, Long postId);

    void deleteByAccountIdAndPostId(Long id, Long id1);
}
