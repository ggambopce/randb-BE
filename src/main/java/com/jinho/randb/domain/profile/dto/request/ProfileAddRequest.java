package com.jinho.randb.domain.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.profile.domain.Gender;
import com.jinho.randb.domain.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileAddRequest {

    @NotEmpty(message = "별명을 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,}$",message = "사용할수 없는 별명입니다.")
    @Schema(description = "사용자의 별명",example = "진지토론")
    private String nickname;

    @NotNull(message = "성별은 필수 입력값입니다.")
    private Gender gender;

    @NotNull(message = "생년월일은 필수 입력값입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 날짜 포맷 지정
    private LocalDate age;

    @Size(max = 500, message = "자기소개는 500자 이내로 작성해주세요.")
    private String bio;

    @Size(max = 255, message = "Instagram URL은 255자 이내여야 합니다.")
    @Pattern(regexp = "^(https?://)?(www\\.)?instagram\\.com/.*$", message = "유효한 Instagram URL을 입력해주세요.")
    private String instagramUrl;

    @Size(max = 255, message = "Instagram URL은 255자 이내여야 합니다.")
    @Pattern(regexp = "^(https?://)?(www\\.)?blog\\.com/.*$", message = "유효한 Blog URL을 입력해주세요.")
    private String blogUrl;

    @Size(max = 255, message = "Instagram URL은 255자 이내여야 합니다.")
    @Pattern(regexp = "^(https?://)?(www\\.)?youtube\\.com/.*$", message = "유효한 YouTube URL을 입력해주세요.")
    private String youtubeUrl;


    // 요청 데이터를 엔티티로 변환하는 메서드
    public Profile toEntity(Account account) {
        return Profile.createProfile(
                this.nickname,
                this.gender,
                this.age,
                this.bio,
                this.instagramUrl,
                this.blogUrl,
                this.youtubeUrl
        ).toBuilder().account(account).build();
    }
}
