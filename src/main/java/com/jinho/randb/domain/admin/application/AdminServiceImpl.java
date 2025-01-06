package com.jinho.randb.domain.admin.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.post.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Override
    public long searchAcountCount() {
        return accountRepository.countAllBy();
    }

    @Override
    public long searchPostCount() {
        return postRepository.countAllBy();
    }
}
