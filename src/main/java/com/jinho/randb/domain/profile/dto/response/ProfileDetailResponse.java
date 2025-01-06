package com.jinho.randb.domain.profile.dto.response;

import com.jinho.randb.domain.profile.dto.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetailResponse {
    // 클라이언트에게 응답 객체
    private ProfileDto profile;
}
