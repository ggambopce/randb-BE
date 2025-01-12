package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostStatistics;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostStatisticsResponseDto;
import com.jinho.randb.domain.post.dto.request.UserAddRequest;
import com.jinho.randb.domain.post.dto.request.UserUpdateRequest;
import com.jinho.randb.domain.post.dto.response.*;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void save(UserAddRequest userAddPostDto, Long accountId);

    PostSearchResponse searchPost(String searchKeyword, PostType postType, Long postId, Pageable pageable);

    Optional<Post> findById(Long id);

    PostDetailResponse getPostDetail(Long postId);

    PostResponse findAll(Integer lastCount, Long lastId, Pageable pageable);

    MainPagePostResponse mainPagePost();

    void delete(Long postId, Long accountId);

    void update(Long postId, Long accountId, UserUpdateRequest userUpdatePostDto);

    void updatePostType(Long postId, PostType newType);


    PostStatistics completePost(Long postId);

    PostStatisticsResponseDto getPostStatistics(Long id);
}
