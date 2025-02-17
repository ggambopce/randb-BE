package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.domain.opinion.dao.OpinionRepository;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinion.dto.UserUpdateOpinionDto;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OpinionServiceImpl implements OpinionService {

    private final OpinionRepository opinionRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Override
    public void save(AddOpinionRequest addOpinionRequest, Long accountId) {

        // Account 조회
        Account account = getAccount(accountId);

        // Account에서 Profile 조회
        Profile profile = account.getProfile();
        if (profile == null) {
            throw new NoSuchElementException("해당 Account에 연결된 Profile이 없습니다.");
        }

        // 게시글 조회
        Post post = postRepository.findById(addOpinionRequest.getPostId()).orElseThrow(() -> new NoSuchDataException(NoSuchErrorType.NO_SUCH_POST));

        // DTO -> domain 변환
        Opinion opinion = Opinion.builder()
                .opinionContent(addOpinionRequest.getOpinionContent())  // 의견 내용 설정
                .opinionType(addOpinionRequest.getOpinionType())  // 카테고리 설정 (RED/BLUE)
                .post(post) // 게시글 설정
                .account(account) // 사용자 정보
                .profile(profile) // 작성자 정보
                .created_at(LocalDateTime.now())
                .build();

        opinionRepository.save(opinion); // 의견 저장
    }

    @Override
    public Optional<Opinion> findById(Long id) {
        return opinionRepository.findById(id);
    }

    @Override
    public List<OpinionContentAndTypeDto> findByPostId(Long postId) {
        return opinionRepository.findByPostId(postId);
    }

    @Override
    public void delete(Long opinionId, Long accountId) {

        // 로그인 사용자id 가져오기
        Account account = getAccount(accountId);
        // 의견 조회
        Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchElementException("해당 의견을 찾을수 없습니다."));
        // 작성자 확인
        validatePostOwner(account, opinion);

        opinionRepository.deleteById(opinion.getId());

    }

    @Override
    public void update(Long opinionId, Long accountId, UserUpdateOpinionDto userUpdateOpinionDto) {
        Account account = getAccount(accountId);
        Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchElementException("해당 의견을 찾을수 없습니다."));
        validatePostOwner(account, opinion);

        opinion.update(userUpdateOpinionDto.getOpinionContent());

        // 트랜잭션범위내 엔티티 변경감지로  변경 사항 자동 저장
    }

    /* 사용자 정보 조회 메서드*/
    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NoSuchDataException(NoSuchErrorType.NO_SUCH_ACCOUNT));
    }

    /* 의견 조회 메서드*/
    private Opinion getOpinion(Long opinionId) {
        return opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchDataException(NoSuchErrorType.NO_SUCH_POST));
    }

    /* 작성자 검증 메서드*/
    private static void validatePostOwner(Account account, Opinion opinion) { // 객체를 생성하지 않고 호출하기위한 static
        if(!opinion.getAccount().getLoginId().equals(account.getLoginId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
    }
}
