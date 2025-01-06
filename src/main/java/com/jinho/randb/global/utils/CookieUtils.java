package com.jinho.randb.global.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtils {

    public ResponseCookie createCookie(String cookieName, String value, int expiredTime){

        String cookieValue = value;

        cookieValue = encodeCookieValueIfNeeded(cookieName, value, cookieValue);

        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(expiredTime)
                .build();
    }


    private static String encodeCookieValueIfNeeded(String cookieName, String value, String cookieValue) {
        if(!cookieName.equals("RefreshToken")) {
            cookieValue = new String(Base64.getEncoder().encode(value.getBytes()));
        }
        return cookieValue;
    }
}
