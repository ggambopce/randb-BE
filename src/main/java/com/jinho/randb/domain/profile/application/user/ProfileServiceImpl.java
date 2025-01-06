package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.image.application.S3UploadService;
import com.jinho.randb.domain.image.domain.UploadFile;
import com.jinho.randb.domain.profile.dao.ProfileRepository;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.dto.ProfileDto;
import com.jinho.randb.domain.profile.dto.request.UserAddRequest;
import com.jinho.randb.domain.profile.dto.request.UserUpdateRequest;
import com.jinho.randb.domain.profile.dto.response.ProfileDetailResponse;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final S3UploadService s3UploadService;

    /**
     * 새로운 프로필 생성
     * @param userAddRequest - 프로필 생성 요청 DTO
     * @param accountId - 계정 ID
     */
    @Override
    public void save(UserAddRequest userAddRequest, Long accountId, MultipartFile multipartFile){

        // Account 조회
        Account account = getAccount(accountId);
        // DTO -> Entity 변환
        Profile profile = userAddRequest.toEntity(account);
        // 이미지 파일 업로드
        s3UploadService.uploadFile(multipartFile, profile);
        // 프로필 저장
        profileRepository.save(profile);

    }


    @Override
    public void update(Long profileId, Long accountId, UserUpdateRequest userUpdateRequest, MultipartFile multipartFile) {

        Account account = getAccount(accountId);
        Profile profile = getProfile(profileId);
        validatePostOwner(account, profile);

        // 기존 이미지 파일명 가져오기
        String existingFileName = profile.getProfileImage().getStoreFileName();

        // 이미지 파일 업로드
        s3UploadService.updateFile(existingFileName, multipartFile, profile);

        profile.updateProfile( // Transactional에서 엔티티의 상태변경으로 수정
                userUpdateRequest.getGender(),
                userUpdateRequest.getAge(),
                userUpdateRequest.getBio(),
                userUpdateRequest.getInstagramUrl(),
                userUpdateRequest.getBlogUrl(),
                userUpdateRequest.getYoutubeUrl());
    }


    /**
     * 프로필의 상제 정보를 보기위한 로직 해당 로직은 그저 전달체
     */
    @Override
    public ProfileDetailResponse detailProfile(Long profileId) {
        ProfileDto profileDto = profileRepository.profileDetails(profileId);
        return new ProfileDetailResponse(profileDto);
    }


    /* 사용자 정보 조회 메서드*/
    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NoSuchDataException(NoSuchErrorType.NO_SUCH_ACCOUNT));
    }

    /*프로필 조회 메서드*/
    private Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId).orElseThrow(() -> new NoSuchDataException(NoSuchErrorType.NO_SUCH_POST));
    }

    /* 작성자 및 관리자인지 검증 메서드*/
    private static void validatePostOwner(Account account, Profile profile) {
        if(!profile.getAccount().getLoginId().equals(account.getLoginId()) && !account.getRoles().equals("ROLE_ADMIN"))
            throw new IllegalArgumentException("작성자만 이용 가능합니다.");
    }
}
