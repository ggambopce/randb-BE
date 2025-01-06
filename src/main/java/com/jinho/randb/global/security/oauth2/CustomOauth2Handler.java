package com.jinho.randb.global.security.oauth2;

import com.jinho.randb.global.jwt.utils.JwtProvider;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2Handler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Value("${host.path}")
    private String successUrl;

    //소셜 로그인 성공시 해당로직을 타게되며 accessToken 과 RefreshToken을 발급해준다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("onAuthenticationSuccess실행");
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String loginId = principal.getAccount().getLoginId();

        String jwtToken = jwtProvider.generateAccessToken(loginId);
        String refreshToken = jwtProvider.generateRefreshToken(loginId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(successUrl);

        String redirectURI = builder
                .queryParam("access-token", jwtToken)
                .build().toString();

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .build();


        if (jwtToken != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
            response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            response.sendRedirect(redirectURI);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "페이지를 찾을 수 없습니다.");
        }
    }
}
