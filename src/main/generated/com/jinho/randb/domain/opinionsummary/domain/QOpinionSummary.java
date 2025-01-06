package com.jinho.randb.domain.opinionsummary.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOpinionSummary is a Querydsl query type for OpinionSummary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOpinionSummary extends EntityPathBase<OpinionSummary> {

    private static final long serialVersionUID = -709781992L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOpinionSummary opinionSummary = new QOpinionSummary("opinionSummary");

    public final DateTimePath<java.time.LocalDateTime> created_at = createDateTime("created_at", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath opinionSummaryContent = createString("opinionSummaryContent");

    public final EnumPath<com.jinho.randb.domain.opinion.domain.OpinionType> opinionType = createEnum("opinionType", com.jinho.randb.domain.opinion.domain.OpinionType.class);

    public final com.jinho.randb.domain.post.domain.QPost post;

    public final DateTimePath<java.time.LocalDateTime> updated_at = createDateTime("updated_at", java.time.LocalDateTime.class);

    public QOpinionSummary(String variable) {
        this(OpinionSummary.class, forVariable(variable), INITS);
    }

    public QOpinionSummary(Path<? extends OpinionSummary> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOpinionSummary(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOpinionSummary(PathMetadata metadata, PathInits inits) {
        this(OpinionSummary.class, metadata, inits);
    }

    public QOpinionSummary(Class<? extends OpinionSummary> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new com.jinho.randb.domain.post.domain.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

