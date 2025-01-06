package com.jinho.randb.domain.email.dao;

import com.jinho.randb.domain.email.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {

    EmailVerification findByEmailAndCode(String email,int code);

    void deleteByEmailAndCode(String email, int code);

}
