package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostStatistics;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostStatisticsResponseDto;
import com.jinho.randb.domain.post.dto.request.UserAddRequest;
import com.jinho.randb.domain.post.dto.request.UserUpdateRequest;
import com.jinho.randb.domain.post.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void save(UserAddRequest userAddPostDto, Long accountId);

    PostResponse postPage(Long postId, Pageable pageable);

    Optional<Post> findById(Long id);

    PostDetailResponse getPostDetail(Long postId);

    PostResponse findAll(Integer lastCount, Long lastId, Pageable pageable);

    MainPagePostResponse mainPagePost();

    void delete(Long postId);

    void update(Long postId, UserUpdateRequest userUpdatePostDto);

    void updatePostType(Long postId, PostType newType);


    PostStatistics completePost(Long postId);

    PostStatisticsResponseDto getPostStatistics(Long id);
}
