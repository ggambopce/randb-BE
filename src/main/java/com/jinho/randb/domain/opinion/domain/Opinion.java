package com.jinho.randb.domain.opinion.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@ToString(exclude = {"post"})
@NoArgsConstructor
public class Opinion {

    @Id
    @GeneratedValue
    @Column(name= "opinion_id", updatable = false)
    private Long id;

    @Lob
    @Column(name = "opinion_content", length = 1000, nullable = false)
    private String opinionContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "opinion_type", nullable = false)
    private OpinionType opinionType;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;


    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(hidden = true)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(hidden = true)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    public LocalDateTime getLocDateTime(){
        return this.created_at = LocalDateTime.now().withSecond(0).withNano(0);
    }

    @JsonIgnore
    public LocalDateTime getUpdate_LocDateTime(){
        return this.updated_at = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void update(String opinionContent) {
        this.opinionContent = opinionContent;
    }

    public void updateTime(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

}
