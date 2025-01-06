package com.jinho.randb.domain.opinion.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.jinho.randb.domain.opinion.domain.OpinionType.RED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpinionController.class)
class OpinionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpinionService opinionService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddOpinionRequest addOpinionRequest;

    @BeforeEach
    void setUp() {
        addOpinionRequest = AddOpinionRequest.builder()
                .opinionContent("This is a test opinion")
                .opinionType(RED) // Enum 타입인 opinionType 값을 넣어줘야 함
                .postId(1L)
                .build();
    }

    @Test
    @DisplayName("의견 작성 성공 테스트")
    void opinionAdd_Success() throws Exception {
        // Mocking OpinionService's save method
        Mockito.doNothing().when(opinionService).save(any(AddOpinionRequest.class));

        // Performing POST request
        mockMvc.perform(post("/api/user/opinions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addOpinionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("작성 성공"));
    }

    @Test
    @DisplayName("의견 조회 테스트")
    void findAllOpinion_Success() throws Exception {
        // Mocking findByPostId method
        Mockito.when(opinionService.findByPostId(anyLong()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/opinions")
                        .param("postId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("조회성공"));
    }

    @Test
    @DisplayName("의견 삭제 성공 테스트")
    void deleteOpinion_Success() throws Exception {
        // Mocking delete method
        Mockito.doNothing().when(opinionService).delete(anyLong());

        mockMvc.perform(delete("/api/user/opinions/{opinion-id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("게시글 삭제 성공"));
    }

    @Test
    @DisplayName("의견 수정 성공 테스트")
    void updateOpinion_Success() throws Exception {
        // Mocking update method
        Mockito.doNothing().when(opinionService).update(anyLong(), any());

        mockMvc.perform(post("/api/user/update/opinions/{opinion-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addOpinionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("작성 성공"));
    }
}