package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.image.application.S3UploadService;
import com.jinho.randb.domain.image.dao.ImgRepository;
import com.jinho.randb.domain.image.domain.UploadFile;
import com.jinho.randb.domain.profile.dao.ProfileRepository;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.dto.ProfileDto;
import com.jinho.randb.domain.profile.dto.request.ProfileAddRequest;
import com.jinho.randb.domain.profile.dto.request.ProfileUpdateRequest;
import com.jinho.randb.domain.profile.dto.response.ProfileDetailResponse;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final S3UploadService s3UploadService;
    private final ImgRepository imgRepository;

    /**
     * 새로운 프로필 생성
     * @param profileAddRequest - 프로필 생성 요청 DTO
     * @param accountId - 계정 ID
     */
    @Override
    public void save(ProfileAddRequest profileAddRequest, Long accountId, MultipartFile multipartFile){

        // Account 조회
        Account account = getAccount(accountId);
        // DTO -> Entity 변환
        Profile profile = profileAddRequest.toEntity(account);
        // 이미지 파일 업로드
        s3UploadService.uploadFile(multipartFile, profile);
        // 프로필 저장
        profileRepository.save(profile);

    }


    @Override
    public void update(Long profileId, Long accountId, ProfileUpdateRequest profileUpdateRequest, List<MultipartFile> multipartFiles) {
        // Account 및 Profile 조회
        Account account = getAccount(accountId);
        Profile profile = getProfile(profileId);

        // 권한 검증
        validatePostOwner(account, profile);

        // 기존 파일 삭제 및 업데이트
        List<UploadFile> existingFiles = profile.getUploadFiles();

        for (MultipartFile newFile : multipartFiles) {
            String originalFilename = newFile.getOriginalFilename();

            // 기존 파일 삭제 (파일명이 동일한 경우)
            existingFiles.stream()
                    .filter(file -> file.getOriginFileName().equals(originalFilename))
                    .findFirst()
                    .ifPresent(existingFile -> {
                        s3UploadService.deleteFile(existingFile.getStoreFileName());
                        imgRepository.delete(existingFile); // DB에서 삭제
                    });

            // 새로운 파일 업로드
            String newStoreFileName = s3UploadService.uploadFile(newFile, profile);

            // UploadFile 생성 및 Profile 연관 설정
            UploadFile newUploadFile = UploadFile.createUploadFile(originalFilename, newStoreFileName);
            profile.addUploadFile(newUploadFile); // Profile에 추가
        }

        // Profile 정보 업데이트
        profile.updateProfile(
                profileUpdateRequest.getNickname(),
                profileUpdateRequest.getGender(),
                profileUpdateRequest.getAge(),
                profileUpdateRequest.getBio(),
                profileUpdateRequest.getInstagramUrl(),
                profileUpdateRequest.getBlogUrl(),
                profileUpdateRequest.getYoutubeUrl()
        );

        // Profile 저장 (변경된 파일 정보 포함)
        profileRepository.save(profile);
    }

    @Override
    public void deleteProfileImage(Long profileId, Long fileId) {
        Profile profile = getProfile(profileId);

        UploadFile uploadFile = profile.getUploadFiles().stream()
                .filter(file -> file.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));

        s3UploadService.deleteFile(uploadFile.getStoreFileName());
        profile.removeUploadFile(uploadFile);
        imgRepository.delete(uploadFile);
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
