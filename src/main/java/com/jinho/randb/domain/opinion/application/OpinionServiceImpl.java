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
    public void save(AddOpinionRequest addOpinionRequest) {
        // SecurityContextHolder에서 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Principal 객체를 안전하게 캐스팅
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof PrincipalDetails principalDetails)) {
            throw new IllegalStateException("인증된 사용자 정보가 PrincipalDetails 타입이 아닙니다.");
        }

        // PrincipalDetails에서 AccountDto 가져오기
        AccountDto accountDto = principalDetails.getAccountDto();
        Long accountId = accountDto.getId();

        // Account 조회
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정을 찾을 수 없습니다."));

        // DTO -> domain 변환
        Opinion opinion = Opinion.builder()
                .opinionContent(addOpinionRequest.getOpinionContent())  // 의견 내용 설정
                .opinionType(addOpinionRequest.getOpinionType())  // 카테고리 설정 (RED/BLUE)
                .post(postRepository.findById(addOpinionRequest.getPostId())
                        .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."))) // 게시글 설정
                .account(account) // 작성자 정보 설정
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
    public void delete(Long opinionId) {

        Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을수 없습니다."));
        opinionRepository.deleteById(opinion.getId());

    }

    @Override
    public void update(Long opinionId, UserUpdateOpinionDto userUpdateOpinionDto) {
        Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을수 없습니다."));

        opinion.update(userUpdateOpinionDto.getOpinionContent());

        opinionRepository.save(opinion);

    }
}
