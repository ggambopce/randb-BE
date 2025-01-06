package com.jinho.randb.domain.profile.dto.request;

import com.jinho.randb.domain.profile.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserUpdateRequest {

    private Gender gender;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate age;

    @Size(max = 500, message = "자기소개는 500자 이내로 작성해주세요.")
    private String bio;

    @Pattern(regexp = "^(https?://)?(www\\.)?instagram\\.com/.*$", message = "유효한 Instagram URL을 입력해주세요.")
    private String instagramUrl;

    @Pattern(regexp = "^(https?://)?(www\\.)?blog\\.com/.*$", message = "유효한 Blog URL을 입력해주세요.")
    private String blogUrl;

    @Pattern(regexp = "^(https?://)?(www\\.)?youtube\\.com/.*$", message = "유효한 YouTube URL을 입력해주세요.")
    private String youtubeUrl;
}
