package com.jinho.randb.domain.profile.api.user;

import com.jinho.randb.domain.image.application.S3UploadService;
import com.jinho.randb.domain.post.dto.response.PostDetailResponse;
import com.jinho.randb.domain.profile.application.user.ProfileService;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.dto.request.UserAddRequest;
import com.jinho.randb.domain.profile.dto.request.UserUpdateRequest;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ProfileController {

    private final ProfileService profileService;
    private final S3UploadService s3UploadService;

    @PostMapping(value = "/user/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfile(@Valid @RequestPart(value = "userAddRequest") UserAddRequest userAddRequest, @RequestPart(value = "multipartFile") MultipartFile multipartFile, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        profileService.save(userAddRequest, principalDetails.getAccountId(), multipartFile);
        return ResponseEntity.ok(new ControllerApiResponse(true,"작성 성공"));
    }

    @GetMapping(value = "/user/profiles/{profileId}")
    public ResponseEntity<?> findProfile(@PathVariable("profileId") Long profileId) {
        ProfileDetailResponse profileDetailResponse = profileService.detailProfile(profileId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", profileDetailResponse));
    }

    @PostMapping(value = "/user/update/profiles/{profileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestPart UserUpdateRequest userUpdateRequest, @RequestPart(required = false) MultipartFile multipartFile,  @PathVariable("profileId") Long profileId,@AuthenticationPrincipal PrincipalDetails principalDetails) {
        profileService.update(profileId, principalDetails.getAccountId(), userUpdateRequest, multipartFile);
        return ResponseEntity.ok(new ControllerApiResponse(true,"수정 성공"));
    }

    @DeleteMapping("/profiles/{profileId}/image")
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long profileId) {
        // 프로필 조회 및 이미지 삭제
        s3UploadService.deleteProfileImage(profileId); // 프로필 이미지 삭제
        // 응답 반환
        return ResponseEntity.ok(new ControllerApiResponse(true, "프로필 이미지가 성공적으로 삭제되었습니다."));
    }





}
