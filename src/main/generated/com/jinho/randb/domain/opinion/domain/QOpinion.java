package com.jinho.randb.domain.opinion.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOpinion is a Querydsl query type for Opinion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOpinion extends EntityPathBase<Opinion> {

    private static final long serialVersionUID = 1169837432L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOpinion opinion = new QOpinion("opinion");

    public final com.jinho.randb.domain.account.domain.QAccount account;

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath opinionContent = createString("opinionContent");

    public final EnumPath<OpinionType> opinionType = createEnum("opinionType", OpinionType.class);

    public final com.jinho.randb.domain.post.domain.QPost post;

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QOpinion(String variable) {
        this(Opinion.class, forVariable(variable), INITS);
    }

    public QOpinion(Path<? extends Opinion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOpinion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOpinion(PathMetadata metadata, PathInits inits) {
        this(Opinion.class, metadata, inits);
    }

    public QOpinion(Class<? extends Opinion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.jinho.randb.domain.account.domain.QAccount(forProperty("account")) : null;
        this.post = inits.isInitialized("post") ? new com.jinho.randb.domain.post.domain.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

