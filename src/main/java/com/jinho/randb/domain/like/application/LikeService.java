package com.jinho.randb.domain.like.application;

public interface LikeService<T> {
    Boolean addLike(T postLikeDto, Long accountId);
}
