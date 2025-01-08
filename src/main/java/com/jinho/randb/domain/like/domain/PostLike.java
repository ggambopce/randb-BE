package com.jinho.randb.domain.like.domain;

import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",foreignKey =  @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public static PostLike createPostLike(Post post, Account account){
        return PostLike.builder().post(post).account(account).build();
    }


}
