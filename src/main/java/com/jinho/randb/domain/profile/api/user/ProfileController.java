package com.jinho.randb.domain.profile.api.user;

import com.jinho.randb.domain.image.application.S3UploadService;
import com.jinho.randb.domain.profile.application.user.ProfileService;
import com.jinho.randb.domain.profile.dto.request.ProfileAddRequest;
import com.jinho.randb.domain.profile.dto.request.ProfileUpdateRequest;
import com.jinho.randb.domain.profile.dto.response.ProfileDetailResponse;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ProfileController {

    private final ProfileService profileService;
    private final S3UploadService s3UploadService;

    @PostMapping(value = "/user/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfile(@Valid @RequestPart(value = "profileAddRequest") ProfileAddRequest profileAddRequest, @RequestPart(value = "multipartFile") MultipartFile multipartFile, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        profileService.save(profileAddRequest, principalDetails.getAccountId(), multipartFile);
        return ResponseEntity.ok(new ControllerApiResponse(true,"작성 성공"));
    }

    @GetMapping(value = "/user/profiles/{profileId}")
    public ResponseEntity<?> findProfile(@PathVariable("profileId") Long profileId) {
        ProfileDetailResponse profileDetailResponse = profileService.detailProfile(profileId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", profileDetailResponse));
    }

    @PostMapping(value = "/user/update/profiles/{profileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestPart ProfileUpdateRequest profileUpdateRequest, @RequestPart(required = false) List<MultipartFile> multipartFile, @PathVariable("profileId") Long profileId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        profileService.update(profileId, principalDetails.getAccountId(), profileUpdateRequest, multipartFile);
        return ResponseEntity.ok(new ControllerApiResponse(true,"수정 성공"));
    }

    @DeleteMapping("/profiles/{profileId}/image")
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long profileId,
                                                @PathVariable Long fileId) {
        // 프로필 조회 및 이미지 삭제
        profileService.deleteProfileImage(profileId, fileId);
        // 응답 반환
        return ResponseEntity.ok(new ControllerApiResponse(true, "프로필 이미지가 성공적으로 삭제되었습니다."));
    }





}
