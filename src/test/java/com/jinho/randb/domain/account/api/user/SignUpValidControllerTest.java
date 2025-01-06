package com.jinho.randb.domain.account.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinho.randb.domain.account.application.user.SignUpService;
import com.jinho.randb.domain.account.dto.request.JoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignUpValidController.class) // 컨트롤러 레벨에서 테스트
class SignUpValidControllerTest {

    @MockBean SignUpService signUpService; // Mocking하여 대체
    @Autowired MockMvc mockMvc; // http요청 수행 도구

    private final ObjectMapper objectMapper = new ObjectMapper(); // java객체를 JSON으로 변환


    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    @DisplayName("회원가입 API - 성공")
    void join_Success() throws Exception {
        JoinRequest request = new JoinRequest();
        request.setUsername("강진호");
        request.setEmail("test@test.com");
        request.setLoginId("test1234");
        request.setNickname("nickname");
        request.setPassword("asdAsd12!@");
        request.setPasswordRe("asdAsd12!@");
        request.setCode(123456);

        // When: Mock Service 호출 설정
        doNothing().when(signUpService).joinAccount(any());

        // Then: MockMvc로 POST 요청 수행 및 검증
        mockMvc.perform(post("/api/signup")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))) // JSON 변환
                .andExpect(status().isOk()) // HTTP 200 응답 검증
                .andExpect(jsonPath("$.success").value(true)) // JSON 응답 검증
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    @DisplayName("회원가입 API - 실패")
    void join_Fail() throws Exception {

        // Given: nickname 필드를 설정하지 않음
        JoinRequest request = new JoinRequest();
        request.setUsername("강진호");
        request.setEmail("test@test.com");
        request.setLoginId("test1234");
        request.setPassword("asdAsd12!@");
        request.setPasswordRe("asdAsd12!@");
        request.setCode(123456);

        // When: Mock Service 호출 설정
        doNothing().when(signUpService).joinAccount(any());

        // Then: MockMvc로 POST 요청 수행 및 검증
        mockMvc.perform(post("/api/signup")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.nickname").value("별명을 입력해주세요"));
    }

}