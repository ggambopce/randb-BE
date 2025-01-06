package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.dao.PostStatisticsRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostStatistics;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostDto;
import com.jinho.randb.domain.post.dto.PostStatisticsResponseDto;
import com.jinho.randb.domain.post.dto.request.UserAddRequest;
import com.jinho.randb.domain.post.dto.request.UserUpdateRequest;
import com.jinho.randb.domain.post.dto.response.*;
import com.jinho.randb.domain.votes.dao.VoteRepository;
import com.jinho.randb.domain.votes.domain.VoteType;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final VoteRepository voteRepository;
    private final PostStatisticsRepository postStatisticsRepository;

    @Override
    public void save(UserAddRequest userAddRequest, Long accountId) {

        // Account 조회
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정을 찾을 수 없습니다."));

        // DTO -> domain 변환
        Post post = Post.builder()
                .postTitle(userAddRequest.getPostTitle())
                .postContent(userAddRequest.getPostContent())
                .account(account) // 작성자 정보 설정
                .type(PostType.DISCUSSING) // 기본상태를 DISCUSSING으로 설정
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }


    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * 토론글의 상세정보를 보는 로직,
     * @param postId  찾을 토론글 번호
     * @return      Response로 변환해 해당 토론글의 상세 정보를 반환
     */
    @Override
    public PostDetailResponse getPostDetail(Long postId) {
        PostDto postDetail = postRepository.getPostDetail(postId);
        return PostDetailResponse.of(postDetail.toDto());
    }

    /**
     * 토론글을 전체조회하는 로직(페이지네이션)
     * @param
     * @return      Response로 변환해 해당 토론글 전체목록을 반환
     */
    @Override
    public PostResponse findAll(Integer lastCount, Long lastId, Pageable pageable) {

        // Repository에서 페이징된 데이터를 조회
        Slice<PostDto> postDtoSlice = postRepository.findAllWithPage(lastCount, lastId, pageable);
        // 다음 페이지 여부와 데이터 리스트를 사용해 PostResponse 반환
        return new PostResponse(postDtoSlice.hasNext(), postDtoSlice.getContent());

    }


    /**
     * 토론글을  전제조회하는 로직(무한페이징),
     * @param postId  찾을 토론글 번호
     * @return Response로 변환해 해당 토론글 전체목록을 반환
     */
    @Override
    public PostResponse postPage(Long postId, Pageable pageable) {

        Slice<PostDto> allPost = postRepository.getAllPost(postId, pageable);
        // 다음 페이지 여부 및 현재 페이지 데이터를 `PostResponse`로 반환
        return new PostResponse(allPost.hasNext(),allPost.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public MainPagePostResponse mainPagePost() {
        List<PostDto> postDtoList = postRepository.mainPagePost();
        return MainPagePostResponse.of(postDtoList);
    }


    @Override
    public void delete(Long postId) {
    // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof PrincipalDetails)) {
            throw new AccessDeniedException("로그인된 사용자만 접근 가능합니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        // 작성자 확인
        if (!post.getAccount().getUsername().equals(username)) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        postRepository.delete(post);
    }

    @Override
    public void update(Long postId, UserUpdateRequest userUpdatePostDto) {

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof PrincipalDetails)) {
            throw new AccessDeniedException("로그인된 사용자만 접근 가능합니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        // 작성자 검증
        if (!post.getAccount().getUsername().equals(username)) {
            throw new AccessDeniedException("작성자만 수정 가능합니다.");
        }

        // 게시글 수정
        post.update(userUpdatePostDto.getPostTitle(), userUpdatePostDto.getPostContent());

        // 변경 사항 저장
        postRepository.save(post);
    }

    @Override
    public void updatePostType(Long postId, PostType newType) {
        // Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));

        // 상태 변경
        post.updatePostType(newType);
        postRepository.save(post); // 변경사항 저장
    }

    @Override
    public PostStatistics completePost(Long postId) {

        // Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));


        post.setCompletedAt(LocalDateTime.now());
        postRepository.save(post);

        // 투표 수 계산
        int redVotes = voteRepository.countByPostAndVoteType(postId, VoteType.RED);
        int blueVotes = voteRepository.countByPostAndVoteType(postId, VoteType.BLUE);

        // 승리한 의견 계산
        String winningVoteType = redVotes > blueVotes ? "RED" : (blueVotes > redVotes ? "BLUE" : "TIE");

        // 투표 비율 계산
        int totalVotes = redVotes + blueVotes;
        double redVotePercentage = totalVotes > 0 ? (double) redVotes / totalVotes * 100 : 0;
        double blueVotePercentage = totalVotes > 0 ? (double) blueVotes / totalVotes * 100 : 0;

        // 기존 통계 데이터 조회
        Optional<PostStatistics> existingStatistics = postStatisticsRepository.findByPost(post);

        PostStatistics statistics;
        if (existingStatistics.isPresent()) {
            // 기존 데이터 업데이트
            statistics = existingStatistics.get();
            statistics.setRedVotes(redVotes);
            statistics.setBlueVotes(blueVotes);
            statistics.setWinningVoteType(winningVoteType);
            statistics.setRedVotePercentage(redVotePercentage);
            statistics.setBlueVotePercentage(blueVotePercentage);
        } else {
            // 새 데이터 생성
            statistics = PostStatistics.builder()
                    .post(post)
                    .redVotes(redVotes)
                    .blueVotes(blueVotes)
                    .winningVoteType(winningVoteType)
                    .redVotePercentage(redVotePercentage)
                    .blueVotePercentage(blueVotePercentage)
                    .build();
        }

        return postStatisticsRepository.save(statistics);
    }

    @Override
    public PostStatisticsResponseDto getPostStatistics(Long postId) {
        PostStatistics statistics = postStatisticsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 통계가 존재하지 않습니다."));

        return new PostStatisticsResponseDto(
                statistics.getRedVotes(),
                statistics.getBlueVotes(),
                statistics.getWinningVoteType(),
                statistics.getRedVotePercentage(),
                statistics.getBlueVotePercentage()
        );
    }


}
