package com.jinho.randb.global.jwt.utils;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.global.exception.ex.JwtTokenException;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.accountRepository= accountRepository;
        this.jwtProvider= jwtProvider;
    }

    /***
     * jwt 토큰 검증 필터 JWT 토큰이 유효한 토큰이거나 서버에서 발급된 토큰인지 확인하는 메서드
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        try {
            Boolean isValid = jwtProvider.TokenExpiration(jwtToken);        //토큰검증 만료시간검증 만료:false 유효:true
            if (!isValid) {
                String loginId = jwtProvider.validateAccessToken(jwtToken);        // 토큰 검증
                if (loginId != null) {
                    Account account = accountRepository.findByLoginId(loginId);
                    PrincipalDetails principalDetails = new PrincipalDetails(account);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                }
            } else {
                throw new JwtTokenException("토큰이 만료되었습니다.");
            }
        } catch (JWTDecodeException e) {
            log.error("JWT 토큰을 디코딩하는 중에 오류가 발생했습니다.");
            throw new JwtTokenException("JWT 토큰을 디코딩하는 중에 오류가 발생했습니다.");
        }

    }

}
