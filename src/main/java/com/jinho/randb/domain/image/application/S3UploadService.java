package com.jinho.randb.domain.image.application;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jinho.randb.domain.image.dao.ImgRepository;
import com.jinho.randb.domain.image.domain.UploadFile;
import com.jinho.randb.domain.profile.dao.ProfileRepository;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.global.exception.ex.img.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.jinho.randb.global.exception.ex.img.ImageErrorType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private final ImgRepository imgRepository;
    private final ProfileRepository profileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile, Profile profile){

        if (multipartFile==null){
            throw new ImageException(MISSING_PRIMARY_IMAGE);
        }

        // 원본 파일명과 저장될 파일명 생성
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFile(originalFilename);

        // 객체 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // UploadFile 엔티티 생성 및 Profile과 연관 설정
        UploadFile uploadFile = UploadFile.createUploadFile(originalFilename, storeFilename);
        profile.addUploadFile(uploadFile); // Profile과 UploadFile 간의 연관 관계 설정

        // UploadFile 저장
        imgRepository.save(uploadFile);

        // 파일을 S3에 업로드
        try {
            InputStream inputStream = multipartFile.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket,storeFilename,inputStream,metadata));
        }catch (IOException e) {
            e.printStackTrace();
            throw new ImageException(UPLOAD_FAILS);
        }

        return amazonS3.getUrl(bucket, storeFilename).toString();
    }

    /**
     * S3 이미지를 수정하는 메서드
     * AWS S3는 덮어쓰는방식은 지원되지 않으므로 삭제후 재 업로드
     * 연관관계 및 이름 재설정
     */
    public void updateFile(List<MultipartFile> newFiles, Profile profile) {

        // 기존 파일 삭제 및 업로드
        List<UploadFile> existingFiles = profile.getUploadFiles();

        // 새로운 파일 업로드 및 Profile 연관 설정
        for (MultipartFile newFile : newFiles) {
            // 기존 파일 삭제 로직 (파일명이 겹치는 경우 삭제)
            existingFiles.stream()
                    .filter(file -> file.getOriginFileName().equals(newFile.getOriginalFilename()))
                    .findFirst()
                    .ifPresent(existingFile -> {
                        deleteFile(existingFile.getStoreFileName());
                        imgRepository.delete(existingFile); // DB에서 삭제
                    });

            // 새로운 파일 업로드
            String newStoreFileName = uploadFile(newFile, profile);

            // UploadFile 생성 및 Profile 연관 설정
            UploadFile uploadFile = UploadFile.createUploadFile(newFile.getOriginalFilename(), newStoreFileName);
            profile.addUploadFile(uploadFile); // Profile에 추가
        }

        // 변경된 파일 정보 저장
        profileRepository.save(profile);
    }
    /**
     * 프로필 이미지 삭제 메서드
     * @param profile 프로필 객체
     */
    public void deleteProfileImage(Long profileId, Long fileId) {
        // 프로필 조회
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        // 프로필에 연관된 파일 리스트에서 파일 조회
        UploadFile uploadFile = profile.getUploadFiles().stream()
                .filter(file -> file.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));

        // S3에서 파일 삭제
        deleteFile(uploadFile.getStoreFileName());

        // 데이터베이스에서 연관 정보 삭제
        profile.removeUploadFile(uploadFile); // Profile과의 연관 관계 제거
        imgRepository.delete(uploadFile); // UploadFile 엔티티 삭제
    }

    /**
     * S3 이미지를 삭제하는 메서드
     * 저장된 파일명을 사용해 S3 버킷에서 해당 이미지를 삭제
     */
    public void deleteFile(String uploadFileName){
        try{
            amazonS3.deleteObject(bucket, uploadFileName);
        }catch (SdkClientException e){
            throw new ImageException(UPLOAD_FAILS);
        }
    }

    /* 저장될 파일명 생성 */
    private String createStoreFile(String originalFilename) {
        int lastIndexOf = originalFilename.lastIndexOf(".");                    // 마지막 종류
        String extension = originalFilename.substring(lastIndexOf + 1).toLowerCase();     //확장자 종류

        Set<String> allowedExtensions = Set.of("jpeg", "jpg", "png");
        if (!allowedExtensions.contains(extension)) {
            throw new ImageException(INVALID_IMAGE_FORMAT);
        }

        // 폴더 경로 설정
        String folderPath = "randb/";
        String uuid = UUID.randomUUID().toString();
        return folderPath + uuid + "." + extension;
    }
}

