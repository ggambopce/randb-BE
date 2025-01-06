package com.jinho.randb.global.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.global.exception.ex.JwtTokenException;
import com.jinho.randb.global.jwt.entity.RefreshToken;
import com.jinho.randb.global.jwt.repository.JWTRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${token.access}")
    private int ACCESS_TOKEN_TINE;

    @Value("${token.refresh}")
    private int REFRESH_TOKEN_TINE;

    private final AccountRepository accountRepository;
    private final JWTRefreshTokenRepository jwtRefreshTokenRepository;

    @Value("${security.token}")
    private String secret;

    /**
     * 엑세스 토큰을 만드는 메서드
     *
     * @param loginId
     * @return 새로운 엑세시 토큰을 만들어 String 타입으로 반환
     */
    public String generateAccessToken(String loginId) {

        Account account = accountRepository.findByLoginId(loginId);

        LocalDateTime now = LocalDateTime.now().plusMinutes(ACCESS_TOKEN_TINE);
        Date date = Timestamp.valueOf(now);
        String token = JWT.create()
                .withSubject("Token")
                .withExpiresAt(date)
                .withClaim("id", account.getId())
                .withClaim("loginId", account.getLoginId())
                .withClaim("username", account.getUsername())
                .withClaim("longinType", account.getLoginType())
                .sign(Algorithm.HMAC512(secret));
        return token;
    }

    /**
     * 리프레쉬 토큰을 생성해주는 메서드
     * @param loginId
     * @return
     */
    public String generateRefreshToken(String loginId) {

        Account account = accountRepository.findByLoginId(loginId);
        RefreshToken refreshToken_account = jwtRefreshTokenRepository.findByAccountId(account.getId());

        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(REFRESH_TOKEN_TINE);
        Date expirationDate = java.sql.Timestamp.valueOf(expirationDateTime);

        String refreshToken ="";

        if (refreshToken_account==null) {
            String new_refreshToken = JWT.create()
                    .withSubject("RefreshToken")
                    .withExpiresAt(expirationDate)
                    .withClaim("id", account.getId())
                    .withClaim("loginId", account.getLoginId())
                    .withClaim("username", account.getUsername())
                    .withClaim("longinType", account.getLoginType())
                    .sign(Algorithm.HMAC512(secret));
            refreshToken = new_refreshToken;
            RefreshToken build = RefreshToken.builder().account(account).refreshToken(refreshToken).tokenTime(expirationDateTime).build();
            jwtRefreshTokenRepository.save(build);
        }else {
            refreshToken = refreshToken_account.getRefreshToken();
            refreshToken_account.setRefreshToken(refreshToken);
            jwtRefreshTokenRepository.save(refreshToken_account);
        }
        return refreshToken;
    }

    /**
     * JWT 토큰의 만료를 검증하는 메소드
     *
     * @param token
     * @return 만료된 토큰 이면 ture 만려되지 않았다면 false
     */
    public Boolean TokenExpiration(String token) {

        DecodedJWT decodedJWT = JWT.decode(token);
        Date expiresAt = decodedJWT.getExpiresAt();
        if (expiresAt != null && expiresAt.before(new Date())) {
            return true;
        } else
            return false;
    }

    /**
     * 토큰을 검증하는 메서드
     * @param token
     * @return
     */

    public String validateAccessToken(String token) {

        try {
            String loginId = JWT.require(Algorithm.HMAC512(secret)).build()
                    .verify(token)
                    .getClaim("loginId")
                    .asString();

            return loginId;
        } catch (SignatureVerificationException e) {
            log.error("존재하지 않은 토큰 사용");
            throw new JwtTokenException("토큰이 존재하지 않습니다.");
        }
    }

    public String validateRefreshToken(String refreshToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(refreshToken);

            String loginId = decodedJWT.getClaim("loginId").asString();

            RefreshToken rerefreshToken = jwtRefreshTokenRepository.findByRefreshToken(refreshToken);
            if (refreshToken == null) throw new JwtTokenException("토큰이 존재하지 않습니다.");

            Boolean isTokenTIme = TokenExpiration(refreshToken);

            if (loginId.equals(rerefreshToken.getAccount().getLoginId()) && !isTokenTIme) {
                String token = generateAccessToken(rerefreshToken.getAccount().getLoginId());
                return token;
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JwtTokenException("잘못된 토큰 형식입니다.");
        }
    }

    private Account getAccount(Authentication authentication) {
        String name = authentication.getName();

        Account account = accountRepository.findByLoginId(name);
        return account;
    }
}
