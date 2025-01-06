package com.jinho.randb.global.security.oauth2;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import com.jinho.randb.global.security.oauth2.provider.GoogleUserInfo;
import com.jinho.randb.global.security.oauth2.provider.NaverUserInfo;
import com.jinho.randb.global.security.oauth2.provider.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();

        String requestId = userRequest.getClientRegistration().getRegistrationId();  // 요청한 oath2 사이트 회사명

        log.info("reWho={}",requestId);
        Oauth2UserInfo oauth2UserInfo = null;
        Account account = null;

         if (requestId.equals("google")) {
            oauth2UserInfo = new GoogleUserInfo(attributes);
            account = save(oauth2UserInfo.getId(), oauth2UserInfo.getName(), oauth2UserInfo.getEmail(), requestId);
        } else if (requestId.equals("naver")) {
            oauth2UserInfo = new NaverUserInfo(attributes);
            log.info("inaver={}",oauth2UserInfo.getId());
            account = save(oauth2UserInfo.getId(), oauth2UserInfo.getName(), oauth2UserInfo.getEmail(), requestId);
        }
        return new PrincipalDetails(account);
    }

    /**
     * oauth2 로그인시에 사용되는 메소드 만약 이전의 로그인한 사용자가 있다면 email정보만 업데이트
     * 만약 로그인한 정보가 없다면 자동으로 회원가입 진행
     * @return account 객체 반환
     */
    private Account save(String id, String name, String email, String requestId) {
        Account account = accountRepository.findByLoginId(id);
        if(account==null){
            Account user = Account.builder()
                    .loginId(id)
                    .username(name)
                    .email(email)
                    .loginType(requestId)
                    .createAt(LocalDate.now())
                    .verified(true).roles("ROLE_USER").build();
            Account save = accountRepository.save(user);
            return save;
        }else{
            account.setEmail(email);
            accountRepository.save(account);
        }
        return account;
    }

}
