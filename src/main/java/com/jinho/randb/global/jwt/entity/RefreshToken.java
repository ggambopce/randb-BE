package com.jinho.randb.global.jwt.entity;

import com.jinho.randb.domain.account.domain.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    String refreshToken;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDateTime tokenTime;
}
