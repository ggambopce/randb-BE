package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.dto.PostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomPostRepository {

    PostDto getPostDetail(Long postId);

    Slice<PostDto> getAllPost(Long postId, Pageable pageable);

    List<PostDto> mainPagePost();

    Slice<PostDto> findAllWithPage(Integer lastCount, Long lastId, Pageable pageable);
}
