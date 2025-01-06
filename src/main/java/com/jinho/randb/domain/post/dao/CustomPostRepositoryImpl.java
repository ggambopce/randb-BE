package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.PostDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.jinho.randb.domain.account.domain.QAccount.account;
import static com.jinho.randb.domain.post.domain.QPost.post;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {
    
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 토론글의 상세 정보를 위해 게시글의 id를 통해서 해당 토론글 조회
     */
    @Override
    public PostDto getPostDetail(Long postId) {

        Post postDetail = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.account).fetchJoin()  // account와 조인하여 작성자 정보도 가져옴
                .where(post.id.eq(postId))
                .fetchOne();

        if (postDetail == null) {
            throw new RuntimeException("Post not found for id: " + postId);
        }
        return PostDto.of(postDetail);

    }

    /**
     * 토론글의 대해서 무한 페이징을 통해 페이징 처리 no-offset 방식을 사용(무한스크롤)
     */
    @Override
    public Slice<PostDto> getAllPost(Long postId, Pageable pageable) {

        // 동적 쿼리 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (postId != null) {
            builder.and(post.id.gt(postId)); // postId 이후의 게시글만 조회
        }

        // QueryDSL을 사용해 데이터 조회
        List<Tuple> list = jpaQueryFactory.select(post.id, post.postTitle, post.postContent, post.type, post.createdAt, account.username)
                .from(post)
                .leftJoin(post.account, account) // 게시글 작성자와 조인
                .where(builder)
                .orderBy(post.createdAt.desc()) // 생성일 기준 내림차순 정렬
                .limit(pageable.getPageSize() + 1) // 페이지 크기 + 1로 데이터 조회
                .fetch();

        // 조회된 Tuple 데이터를 PostDto로 변환
        List<PostDto> collect = list.stream()
                .map(tuple -> new PostDto(
                        tuple.get(post.id),
                        tuple.get(post.postTitle),
                        tuple.get(post.postContent),
                        tuple.get(account.username),
                        tuple.get(post.type)
                ))
                .collect(Collectors.toList());

        // 다음 페이지 여부 확인
        boolean hasNext = isHasNextSize(pageable, collect);

        // Slice로 반환
        return new SliceImpl<>(collect, pageable, hasNext);
    }

    @Override
    public List<PostDto> mainPagePost() {

        //튜플로 토론글 id, 제목, 내용을 조회
        List<Tuple> list = jpaQueryFactory.select(post.id, post.postTitle, post.postContent, post.type)
                .from(post)
                .fetch();

        return list.stream().map(tuple -> PostDto.from(tuple.get(post.id),
                tuple.get(post.postTitle),
                tuple.get(post.postContent),
                tuple.get(post.type)
                )).collect(Collectors.toList());
    } //from은 정적 팩토리 메서드로 new 키워드를 사용하는 것과는 다른 방식. 팩토리 메서드는 new 없이 호출

    /**
     * 토론글 전체 조회 (페이지네이션)
     * @param lastCount, lastId, pageable
     * @return
     */
    @Override
    public Slice<PostDto> findAllWithPage(Integer lastCount, Long lastId, Pageable pageable) {
        // 동적 조건 없이 전체 데이터를 페이징 처리
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .offset(pageable.getOffset()) // 시작 위치
                .limit(pageable.getPageSize() + 1) // 페이지 크기 + 1 (다음 페이지 확인용)
                .fetch();

        // Post 엔티티를 PostDto로 변환
        List<PostDto> postDtos = posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());

        // 다음 페이지 여부 확인
        boolean hasNext = isHasNextSize(pageable, postDtos);

        // Slice로 반환
        return new SliceImpl<>(postDtos, pageable, hasNext);

    }

    private static boolean isHasNextSize(Pageable pageable, List<PostDto> collect) {
        boolean hasNextSize = false;
        if(collect.size()> pageable.getPageSize()){
            collect.remove(pageable.getPageSize());
            hasNextSize = true;
        }
        return hasNextSize;
    }

}
