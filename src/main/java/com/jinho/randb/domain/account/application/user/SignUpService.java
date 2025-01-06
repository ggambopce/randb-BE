package com.jinho.randb.domain.account.application.user;

import com.jinho.randb.domain.account.dto.AccountDto;

import java.util.Map;

public interface SignUpService {
    void joinAccount(AccountDto accountDto);

    boolean ValidationOfSignUp(AccountDto accountDto);

    Map<String, Boolean> LoginIdValid(String loginId);

    Map<String, Boolean> emailValid(String email);

    void nickNameValid(String nickName);

    Map<String,String> ValidationErrorMessage(AccountDto accountDto);
}
