package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomPostRepository {

    PostDto getPostDetail(Long postId);

    Slice<PostDto> searchPosts(String searchKeyword, PostType postType, Long lastPostId, Pageable pageable);

    List<PostDto> mainPagePost();

    Slice<PostDto> findAllWithPage(Integer lastCount, Long lastId, Pageable pageable);
}
