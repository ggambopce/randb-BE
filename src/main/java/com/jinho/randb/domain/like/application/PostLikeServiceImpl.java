package com.jinho.randb.domain.like.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.like.dao.PostLikeRepository;
import com.jinho.randb.domain.like.domain.PostLike;
import com.jinho.randb.domain.like.dto.request.PostLikeRequest;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType.NO_SUCH_ACCOUNT;
import static com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType.NO_SUCH_POST;

/**
 * 좋아요 api 호출시 해당 게시글의 좋아요가 있으면 DB 에서 삭제하고 좋아요가 되어있지않다면 DB에 추가하는 식으로 구현
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeServiceImpl<T extends PostLikeRequest,U> implements LikeService<T>{

    private final PostLikeRepository postLikeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Override
    public Boolean addLike(PostLikeRequest postLikeRequest, Long accountId) {
        Boolean alreadyLiked  = postLikeRepository.existsByAccountIdAndPostId(accountId, postLikeRequest.getPostId());    // 해당 테이블의 있는지검사
        Post post = postRepository.findById(postLikeRequest.getPostId()).orElseThrow(() -> new NoSuchDataException(NO_SUCH_POST));
        Account account = getAccount(accountId);

        if (alreadyLiked) {
            removeLike(post, account);
        } else {
            addLike(post, account);
        }
        return alreadyLiked;
    }
    /* 좋아요 시도 */
    private void addLike(Post post, Account account) {
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
        postLikeRepository.save(PostLike.createPostLike(post, account));
    }

    /* 좋아요 해제 */
    private void removeLike(Post post, Account account) {
        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
        postLikeRepository.deleteByAccountIdAndPostId(account.getId(), post.getId());
    }


    /* 사용자 정보 조회 */
    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NoSuchDataException(NO_SUCH_ACCOUNT));
    }
}
