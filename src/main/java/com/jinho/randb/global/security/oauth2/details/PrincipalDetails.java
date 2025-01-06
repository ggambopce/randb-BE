package com.jinho.randb.global.security.oauth2.details;

import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Account account;
    private Map<String, Object> attributes;

    // 일반 로그인 사용자
    public PrincipalDetails(Account account) {
        this.account = account;
        this.attributes = null;
    }

    // OAuth2 로그인 사용자
    public PrincipalDetails(Account account, Map<String, Object> attributes) {
        this.account = account;
        this.attributes = attributes;
    }

    public AccountDto getAccountDto() {
        return AccountDto.from(this.account);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String roleName : account.getRoleList()) {
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.isVerified();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.isVerified();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.isVerified();
    }

    @Override
    public boolean isEnabled() {
        return account.isVerified();
    }

    @Override
    public String getName() {
        return account.getLoginId();
    }

    public Long getAccountId() { return account.getId();}

}
