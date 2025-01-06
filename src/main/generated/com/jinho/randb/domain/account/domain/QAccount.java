package com.jinho.randb.domain.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -620576232L;

    public static final QAccount account = new QAccount("account");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> join_date = createDate("join_date", java.time.LocalDate.class);

    public final ListPath<com.jinho.randb.domain.opinion.domain.Opinion, com.jinho.randb.domain.opinion.domain.QOpinion> opinions = this.<com.jinho.randb.domain.opinion.domain.Opinion, com.jinho.randb.domain.opinion.domain.QOpinion>createList("opinions", com.jinho.randb.domain.opinion.domain.Opinion.class, com.jinho.randb.domain.opinion.domain.QOpinion.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<com.jinho.randb.domain.post.domain.Post, com.jinho.randb.domain.post.domain.QPost> posts = this.<com.jinho.randb.domain.post.domain.Post, com.jinho.randb.domain.post.domain.QPost>createList("posts", com.jinho.randb.domain.post.domain.Post.class, com.jinho.randb.domain.post.domain.QPost.class, PathInits.DIRECT2);

    public final StringPath roles = createString("roles");

    public final StringPath username = createString("username");

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

