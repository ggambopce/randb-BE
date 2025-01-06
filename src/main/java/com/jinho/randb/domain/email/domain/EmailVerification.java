package com.jinho.randb.domain.email.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(generator = "UUID_GENERATOR")
    @GenericGenerator(name = "UUID_GENERATER", strategy = "org.hibernate.id.UUIDGenerator")
    private Long verificationId;

    private String username;

    private String email;

    private Integer code;

    private LocalDateTime createAt;

    private LocalDateTime expiredAt;

    public static EmailVerification creatEmailVerification(LocalDateTime expiredAt,String email,int code){
        return EmailVerification.builder().email(email).createAt(LocalDateTime.now()).email(email).code(code).expiredAt(expiredAt).build();
    }
    public boolean expired(EmailVerification emailVerification){
        return  emailVerification.getExpiredAt().isAfter(LocalDateTime.now());
    }
}
