package com.jinho.randb.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.domain.profile.domain.Gender;
import com.jinho.randb.domain.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "프로필 DTO")
public class ProfileDto {

    private Long id;

    private String nickname; // 별명

    private Gender gender; // 성별

    private LocalDate age; // 만 나이

    private String bio; // 자기소개

    private String instagramUrl; // 인스타 Url

    private String blogUrl; // 블로그 Url

    private String youtubeUrl; // 유튜브 Url

    private LocalDate createdAt; // 등록일

    private LocalDateTime updatedAt; // 수정일

    private AccountDto account;

    private String profileImgUrl; // 프로필 이미지 Url


    public static ProfileDto of(Profile profile, String imgUrl) {
        return ProfileDto.builder()
                .id(profile.getId())
                .nickname(profile.getNickname())
                .gender(profile.getGender())
                .age(profile.getAge())
                .bio(profile.getBio())
                .instagramUrl(profile.getInstagramUrl())
                .blogUrl(profile.getBlogUrl())
                .youtubeUrl(profile.getYoutubeUrl())
                .account(AccountDto.of(profile.getAccount()))
                .profileImgUrl(imgUrl)
                .build();
    }

}
