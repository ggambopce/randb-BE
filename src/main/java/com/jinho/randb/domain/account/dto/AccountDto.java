package com.jinho.randb.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.account.domain.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(name="사용자 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto { // 내부 로직 사용 객체

    @Schema(nullable = true,hidden = true)
    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String passwordRe;

    private String loginId;

    private String email;

    @JsonIgnore
    private String roles;

    @JsonIgnore
    private LocalDate join_date;

    @JsonIgnore
    private String login_type;

    @JsonIgnore
    private boolean verified;

    @JsonIgnore
    private Integer code;

    // AccountDto객체를 Account로 변환
    public static Account toEntity(AccountDto accountDto) {  // static으로 클래스 수준에서 호출, 객체생성없이 호출가능
        return Account.builder()
                .id(accountDto.getId())
                .loginId(accountDto.getLoginId())
                .password(accountDto.getPassword())
                .email(accountDto.getEmail())
                .createAt(LocalDate.now())
                .nickname(accountDto.getNickname())
                .username(accountDto.getUsername())
                .createAt(accountDto.getJoin_date())
                .roles("ROLE_USER")
                .loginType("normal")
                .verified(true)
                .build();
    }

    private AccountDto(Long accountId, String loginId, String email, String username, String nickname, LocalDate join_date) {
        this.id = accountId;
        this.loginId = loginId;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.join_date = join_date;
    }

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .loginId(account.getLoginId())
                .username(account.getUsername())
                .nickname(account.getNickname())
                .password(account.getPassword())
                .email(account.getEmail())
                .roles(account.getRoles())
                .login_type(account.getLoginType())
                .build();
    }

    public static AccountDto of(Long accountId, String loginId, String email, String username, String nickname, LocalDate join_date) {
        return new AccountDto(accountId, loginId, email, username, nickname, join_date);
    }

    /**
     * Account 엔티티에서 AccountDto로 변환
     */
    public static AccountDto of(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .loginId(account.getLoginId())
                .username(account.getUsername())
                .nickname(account.getNickname())
                .email(account.getEmail())
                .build();
    }


}
