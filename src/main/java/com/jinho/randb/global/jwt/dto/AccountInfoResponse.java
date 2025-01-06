package com.jinho.randb.global.jwt.dto;

import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.Data;

@Data
public class AccountInfoResponse {

    private Long id;
    private String loginId;
    private String username;
    private String loginType;
    private String roles;

    private AccountInfoResponse(Long id, String loginId, String username, String loginType, String roles) {
        this.id = id;
        this.loginId = loginId;
        this.username = username;
        this.loginType = loginType;
        this.roles = roles;
    }

    public static AccountInfoResponse of(AccountDto accountDto){
        return new AccountInfoResponse(accountDto.getId(), accountDto.getLoginId(), accountDto.getUsername(), accountDto.getLogin_type(), accountDto.getRoles());
    }
}
