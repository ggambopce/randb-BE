package com.jinho.randb.domain.account.dao;

import com.jinho.randb.domain.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>,CustomAccountRepository {

    Account findByLoginId(String loginId);

    long countAllBy();

    List<Account> findByEmail(String email);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    @Query("select m from Account m where  binary(m.loginId)=:loginId") //MySQL의 BINARY 타입을 사용해 대소문자를 구문하기 위한 쿼리 사용
    Account findByCaseSensitiveLoginId(String loginId);
}
