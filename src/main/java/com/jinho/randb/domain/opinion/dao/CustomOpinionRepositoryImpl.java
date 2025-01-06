package com.jinho.randb.domain.opinion.dao;

import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.jinho.randb.domain.account.domain.QAccount.account;
import static com.jinho.randb.domain.opinion.domain.QOpinion.opinion;

@RequiredArgsConstructor
public class CustomOpinionRepositoryImpl implements CustomOpinionRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OpinionContentAndTypeDto> findByPostId(Long postId) {
        List<Tuple> list = queryFactory
                .select(opinion.id, opinion.opinionContent, opinion.opinionType, account.username, opinion.created_at, opinion.updated_at)
                .from(opinion)
                .join(opinion.account, account)
                .where(opinion.post.id.eq(postId))
                .fetch();

        return list.stream()
                .map(tuple -> OpinionContentAndTypeDto.builder()
                        .id(tuple.get(opinion.id))
                        .opinionContent(tuple.get(opinion.opinionContent))
                        .username(tuple.get(account.username))
                        .opinionType(tuple.get(opinion.opinionType))
                        .create_at(tuple.get(opinion.created_at))  // 등록일
                        .updated_at(tuple.get(opinion.updated_at)) // 수정일
                        .build())
                .collect(Collectors.toList());
    }
}
