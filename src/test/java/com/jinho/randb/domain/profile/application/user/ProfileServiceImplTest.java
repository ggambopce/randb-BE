package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.profile.dao.ProfileRepository;
import com.jinho.randb.domain.profile.domain.Gender;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.dto.user.UserAddRequest;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    ProfileRepository profileRepository;
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    ProfileServiceImpl profileService;

    @Test
    @DisplayName("프로필 생성 테스트")
    void profile_save_success() {
        //Given
        Long accountId = 1L;
        UserAddRequest userAddRequest = UserAddRequest.builder()
                .gender(Gender.MALE)
                .age(LocalDate.of(1988,1,24))
                .bio("자기소개")
                .instagramUrl("https://www.instagram.com/developer")
                .blogUrl("https://www.blog.com/developer")
                .youtubeUrl("https://www.youtube.com/channel/developer")
                .build();

        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // When
        profileService.save(userAddRequest, accountId);

        // Then
        // ArgumentCaptor를 사용하여 저장된 Profile 객체 검증
        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepository, times(1)).save(profileCaptor.capture()); // 캡처

        Profile capturedProfile = profileCaptor.getValue(); // 캡처된 Profile 객체 가져오기

        // 검증: 캡처된 Profile 객체의 값 확인
        assertThat(capturedProfile.getGender()).isEqualTo(Gender.MALE);
        assertThat(capturedProfile.getAge()).isEqualTo(LocalDate.of(1988, 1, 24));
        assertThat(capturedProfile.getBio()).isEqualTo("자기소개");
        assertThat(capturedProfile.getInstagramUrl()).isEqualTo("https://www.instagram.com/developer");
        assertThat(capturedProfile.getBlogUrl()).isEqualTo("https://www.blog.com/developer");
        assertThat(capturedProfile.getYoutubeUrl()).isEqualTo("https://www.youtube.com/channel/developer");
        assertThat(capturedProfile.getAccount()).isEqualTo(mockAccount);

        // 호출 횟수 검증
        verify(accountRepository, times(1)).findById(accountId);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    @DisplayName("프로필 생성 실패 AccountId 예외 테스트")
    void save_ThrowsNoSuchDataExceptionWhenAccountNotFound() {
        // Given
        Long accountId = 1L;
        UserAddRequest userAddRequest = UserAddRequest.builder()
                .gender(Gender.MALE)
                .age(LocalDate.of(1990, 1, 1))
                .bio("I am a passionate developer.")
                .instagramUrl("https://www.instagram.com/developer")
                .blogUrl("https://www.blog.com/developer")
                .youtubeUrl("https://www.youtube.com/channel/developer")
                .build();

        // Stubbing AccountRepository to return empty
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchDataException.class, () -> profileService.save(userAddRequest, accountId));
        verify(accountRepository, times(1)).findById(accountId);
        verify(profileRepository, never()).save(any(Profile.class));
    }


}