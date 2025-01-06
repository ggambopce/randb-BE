package com.jinho.randb.global.jwt.service;

import com.jinho.randb.global.jwt.dto.AccountInfoResponse;
import com.jinho.randb.global.jwt.controller.LoginDto;
import com.jinho.randb.global.jwt.entity.RefreshToken;

import java.util.Map;

public interface JwtAuthService {

    void logout(Long id);

    void save(RefreshToken refreshToken);

    Map<String, String> login(LoginDto loginDto);

    AccountInfoResponse accessTokenMemberInfo(String accessToken);
}
