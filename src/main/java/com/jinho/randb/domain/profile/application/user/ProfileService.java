package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.profile.dto.request.ProfileAddRequest;
import com.jinho.randb.domain.profile.dto.request.ProfileUpdateRequest;
import com.jinho.randb.domain.profile.dto.response.ProfileDetailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {

    void save(ProfileAddRequest profileAddRequest, Long accountId, MultipartFile multipartFile);

    ProfileDetailResponse detailProfile(Long profileId);

    void update(Long profileId, Long accountId, ProfileUpdateRequest profileUpdateRequest, List<MultipartFile> multipartFiles);

    void deleteProfileImage(Long profileId, Long fileId);
}
