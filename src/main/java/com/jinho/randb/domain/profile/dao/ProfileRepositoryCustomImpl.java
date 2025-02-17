package com.jinho.randb.domain.profile.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.domain.QProfile;
import com.jinho.randb.domain.profile.dto.ProfileDto;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jinho.randb.domain.account.domain.QAccount.account;
import static com.jinho.randb.domain.image.domain.QUploadFile.uploadFile;
import static com.jinho.randb.domain.profile.domain.QProfile.*;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}") // S3 버킷 이름 주입
    private String bucketName;


    /**
     * 프로필 상세 정보를 위해 프로필의 id를 통해서 해당 프로필 데이터 모두 조회
     * 프로필id를 가진 이미지 url도 모두 조회
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

        // 프로필 ID로 연관된 이미지 URL 조회
        List<String> imageUrls = jpaQueryFactory
                .select(uploadFile.storeFileName)
                .from(uploadFile)
                .where(uploadFile.profile.id.eq(profileId))
                .fetch();

        // 대표 이미지 URL을 가져옴 (여기서는 첫 번째 이미지 사용)
        String profileImgUrl = imageUrls.isEmpty() ? null : amazonS3.getUrl(bucketName, imageUrls.get(0)).toString();


        return ProfileDto.of(profileEntity, profileImgUrl);
    }
}
