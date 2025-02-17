package com.jinho.randb.global.jwt.dto;

import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.domain.profile.domain.Profile;
import lombok.Data;

@Data
public class AccountInfoResponse {

    private Long id;
    private String loginId;
    private String username;
    private String loginType;
    private String roles;

    private Long profileId;          // 프로필 ID
    private String nickname;         // 닉네임

    private AccountInfoResponse(Long id, String loginId, String username, String loginType, String roles, Long profileId, String nickname) {
        this.id = id;
        this.loginId = loginId;
        this.username = username;
        this.loginType = loginType;
        this.roles = roles;
        this.profileId = profileId;
        this.nickname = nickname;
    }

    public static AccountInfoResponse of(AccountDto accountDto, Profile profile){
        return new AccountInfoResponse(
                accountDto.getId(),
                accountDto.getLoginId(),
                accountDto.getUsername(),
                accountDto.getLogin_type(),
                accountDto.getRoles(),
                profile != null ? profile.getId() : null,
                profile != null ? profile.getNickname() : null);
    }
}
