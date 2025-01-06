package com.jinho.randb.global.security.oauth2;

public interface UserDisConnectService {

    String getAccessToken(String auth2Code);

    Boolean disconnect(String accessToken);
}
