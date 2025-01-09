package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jinho.randb.domain.account.domain.QAccount.account;
import static com.jinho.randb.domain.post.domain.QPost.post;
import static com.jinho.randb.domain.post.domain.QPostStatistics.postStatistics;

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
                .leftJoin(post.profile).fetchJoin()  // profile와 조인하여 작성자 정보도 가져옴
                .where(post.id.eq(postId))
                .fetchOne();

        if (postDetail == null) {
            throw new RuntimeException("Post not found for id: " + postId);
        }
        return PostDto.of(postDetail); // 엔티티를 DTO로 변환

    }

    /**
     * 토론중으로 검색시 랜덤 순 4개 무한스크롤
     * 투표중으로 검색시 RED와 BLUE비율이 박빙인 순 4개 무한스크롤
     * 토론완료로 검색시 좋아요 순 으로 4개 무한스크롤
     * 토론글의 대해서 무한 페이징을 통해 페이징 처리 no-offset 방식을 사용(무한스크롤)
     */
    @Override
    public Slice<PostDto> searchPosts(String searchKeyword, PostType postType, Long lastPostId, Pageable pageable) {

        // 동적 쿼리 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            builder.and(post.postTitle.containsIgnoreCase(searchKeyword)); // 제목 검색
        }
        if (postType != null) {
            builder.and(post.postType.eq(postType)); // postType 필터
        }
        if (lastPostId != null) {
            builder.and(post.id.lt(lastPostId)); // 마지막 postId 이후의 데이터
        }

        // QueryDSL로 데이터 조회 및 정렬 조건 설정
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(post.id, post.postTitle, post.postContent, post.postType, post.likeCount, post.createdAt, account.username)
                .from(post)
                .leftJoin(post.account, account) // 게시글 작성자와 조인
                .where(builder)
                .limit(pageable.getPageSize() + 1); // 페이지 크기 + 1 조회

        switch (postType) {
            case DISCUSSING:
                query.orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc()); // 랜덤 정렬
                break;
            case VOTING:
                query.leftJoin(postStatistics).on(post.id.eq(postStatistics.post.id))
                        .orderBy(Expressions.numberTemplate(Double.class,
                                "ABS({0} - {1})",
                                postStatistics.redVotePercentage,
                                postStatistics.blueVotePercentage).asc()); // 박빙 순
                break;
            case COMPLETED:
                query.orderBy(post.likeCount.desc()); // 좋아요 순
                break;
            default:
                throw new IllegalArgumentException("Unsupported PostType: " + postType);
        }

        // 결과 조회
        List<Tuple> list = query.fetch();

        // 조회된 Tuple 데이터를 PostDto로 변환
        List<PostDto> collect = list.stream()
                .map(tuple -> new PostDto(
                        tuple.get(post.id),
                        tuple.get(post.postTitle),
                        tuple.get(post.postContent),
                        tuple.get(account.username),
                        tuple.get(post.postType),
                        tuple.get(post.likeCount)
                ))
                .collect(Collectors.toList());

        // 다음 페이지 여부 확인
        boolean hasNext = isHasNextSize(pageable, collect);

        // Slice로 반환
        return new SliceImpl<>(collect, pageable, hasNext);
    }

    /**
     * 메인 페이지 토론글 4개 조회
     * 상태에 따라 다른 조건으로 조회 토론중/투표중 랜덤1개, 투표완료 좋아요순2개
     */
    @Override
    public List<PostDto> mainPagePost() {

        // "토론중" 상태 랜덤 1개
        List<Tuple> discussingPost = jpaQueryFactory.select(
                        post.id,
                        post.postTitle,
                        post.postContent,
                        post.postType,
                        post.likeCount
                )
                .from(post)
                .where(post.postType.eq(PostType.DISCUSSING)) // "토론중" 조건
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc()) // 랜덤 정렬
                .limit(1) // 1개만 가져오기
                .fetch();

        // "투표중" 상태 랜덤 1개
        List<Tuple> votingPost = jpaQueryFactory.select(
                        post.id,
                        post.postTitle,
                        post.postContent,
                        post.postType,
                        post.likeCount
                )
                .from(post)
                .where(post.postType.eq(PostType.VOTING)) // "투표중" 조건
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc()) // 랜덤 정렬
                .limit(1) // 1개만 가져오기
                .fetch();

        // "토론 완료" 상태 좋아요 순 상위 2개
        List<Tuple> completedPosts = jpaQueryFactory.select(
                        post.id,
                        post.postTitle,
                        post.postContent,
                        post.postType,
                        post.likeCount
                )
                .from(post)
                .where(post.postType.eq(PostType.COMPLETED)) // "토론 완료" 조건
                .orderBy(post.likeCount.desc()) // 좋아요 내림차순 정렬
                .limit(2) // 상위 2개 가져오기
                .fetch();

        // 모든 리스트를 병합
        List<Tuple> combinedList = Stream.concat(
                Stream.concat(discussingPost.stream(), votingPost.stream()),
                completedPosts.stream()
        ).collect(Collectors.toList());

        // DTO로 변환하여 반환
        return combinedList.stream()
                .map(tuple -> PostDto.from(
                        tuple.get(post.id),
                        tuple.get(post.postTitle),
                        tuple.get(post.postContent),
                        tuple.get(post.postType),
                        tuple.get(post.likeCount)
                ))
                .collect(Collectors.toList());
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
