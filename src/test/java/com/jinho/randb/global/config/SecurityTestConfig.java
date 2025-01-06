package com.jinho.randb.global.config;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.global.jwt.repository.JWTRefreshTokenRepository;
import com.jinho.randb.global.jwt.utils.JwtProvider;
import com.jinho.randb.global.security.exception.CustomAccessDeniedHandler;
import com.jinho.randb.global.security.exception.JwtAuthenticationEntryPoint;
import com.jinho.randb.global.security.oauth2.CustomOauth2Handler;
import com.jinho.randb.global.security.oauth2.CustomOauth2Service;
import com.jinho.randb.global.utils.CookieUtils;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({JwtAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class SecurityTestConfig {
    @Bean
    public AccountRepository accountRepository() {
        return Mockito.mock(AccountRepository.class);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return Mockito.mock(JwtProvider.class);
    }

    @Bean
    public CustomOauth2Handler customOauth2Handler() {
        return Mockito.mock(CustomOauth2Handler.class);
    }
    @Bean
    public CustomOauth2Service customOauth2Service() {
        return Mockito.mock(CustomOauth2Service.class);
    }

    @Bean
    public CookieUtils cookieUtils() {
        return Mockito.mock(CookieUtils.class);
    }

    @Bean
    public JWTRefreshTokenRepository refreshTokenRepository() {
        return Mockito.mock(JWTRefreshTokenRepository.class);
    }

}