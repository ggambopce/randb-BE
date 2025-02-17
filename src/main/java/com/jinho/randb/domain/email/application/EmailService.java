package com.jinho.randb.domain.email.application;

import java.util.Map;

public interface EmailService {

    default String sendMailMessage(String email) {
        return null;
    }
    default String sendMail(String email, String subject, String body) {
        return null;
    }

    default Integer createCode() {
        return null;
    }

    default Map<String, Boolean> verifyCode(String email, int code) {
        return null;
    }

    default void deleteCode(String email, int code) {
    }
}
