package com.jinho.randb.domain.profile.dao;

import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.domain.QProfile;
import com.jinho.randb.domain.profile.dto.ProfileDto;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jinho.randb.domain.account.domain.QAccount.account;
import static com.jinho.randb.domain.profile.domain.QProfile.*;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    /**
     * 프로필 상세 정보를 위해 프로필의 id를 통해서 해당 프로필 데이터 모두 조회
     */
    @Override
    public ProfileDto profileDetails(Long profileId) {

        Profile profileEntity = jpaQueryFactory
                .select(profile)
                .from(profile)
                .where(profile.id.eq(profileId))
                .fetchOne();
        if (profileEntity == null) {
            throw new NoSuchDataException(NoSuchErrorType.NO_SUCH_PROFILE);
        }

        return ProfileDto.of(profileEntity);
    }
}
