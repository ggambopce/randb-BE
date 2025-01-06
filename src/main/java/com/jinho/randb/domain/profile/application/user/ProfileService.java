package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.profile.dto.request.UserAddRequest;
import com.jinho.randb.domain.profile.dto.request.UserUpdateRequest;
import com.jinho.randb.domain.profile.dto.response.ProfileDetailResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    void save(UserAddRequest userAddRequest, Long accountId, MultipartFile multipartFile);

    ProfileDetailResponse detailProfile(Long profileId);

    void update(Long profileId, Long accountId, UserUpdateRequest userUpdateRequest, MultipartFile multipartFile);

}
