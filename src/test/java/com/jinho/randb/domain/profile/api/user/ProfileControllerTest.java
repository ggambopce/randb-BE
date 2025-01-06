package com.jinho.randb.domain.profile.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.profile.application.user.ProfileService;
import com.jinho.randb.domain.profile.domain.Gender;
import com.jinho.randb.domain.profile.dto.user.UserAddRequest;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"}) // 인증된 사용자 설정
    void createProfile_Success() throws Exception {
        //Given
        UserAddRequest userAddRequest = UserAddRequest.builder()
                .gender(Gender.MALE)
                .age(LocalDate.of(1988,1,24))
                .bio("자기소개입니다.")
                .instagramUrl("https://instagram.com/test")
                .blogUrl("https://blog.com/test")
                .youtubeUrl("https://youtube.com/test")
                .build();

        PrincipalDetails principalDetails = new PrincipalDetails(
                Account.builder().id(1L).build() // Account 객체에 ID 설정
        );


        objectMapper.registerModule(new JavaTimeModule()); // LocalDate 지원 모듈 등록

        //When
        mockMvc.perform(post("/api/user/profiles")
                        .with(csrf()) // CSRF 토큰 추가
                        .principal(new UsernamePasswordAuthenticationToken(
                                principalDetails, null, principalDetails.getAuthorities()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("작성 성공"));

        // Then
        verify(profileService, times(1)).save(any(UserAddRequest.class), anyLong());

    }
}