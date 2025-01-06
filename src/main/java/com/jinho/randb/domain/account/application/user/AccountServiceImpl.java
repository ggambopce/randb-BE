package com.jinho.randb.domain.account.application.user;

import com.jinho.randb.domain.account.application.user.AccountService;
import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.global.jwt.repository.JWTRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private static String LOGIN_TYPE = "normal";

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTRefreshTokenRepository jwtRefreshTokenRepository;

    @Override
    public void saveDto(AccountDto accountDto) {

        Account account = Account.builder()
                .loginId(accountDto.getLoginId())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .username(accountDto.getUsername())
                .loginType(LOGIN_TYPE)
                .email(accountDto.getEmail())
                .createAt(LocalDate.now())
                .roles("ROLE_USER")
                .verified(true)
                .build();

        accountRepository.save(account);
    }

    @Override
    public AccountDto findByLoginId(String loginId) {
        Account byLoginId = accountRepository.findByLoginId(loginId);
        if (byLoginId == null){
            log.info("오류발생");
        }

        return AccountDto.from(byLoginId);
    }

    @Override
    public void deleteAccount(Long accountId) {
        jwtRefreshTokenRepository.DeleteByAccountId(accountId);
        accountRepository.deleteById(accountId);
    }
}
