package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.opinion.dao.OpinionRepository;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.opinion.dto.UserUpdateOpinionDto;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class OpinionServiceImplTest {

    @Autowired
    private OpinionServiceImpl opinionService;

    @MockBean
    private OpinionRepository opinionRepository;

    @MockBean
    private PostRepository postRepository;

    private Opinion opinion;
    private Post post;
    private AddOpinionRequest addOpinionRequest;
    private UserUpdateOpinionDto userUpdateOpinionDto;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .postTitle("Test Post")
                .postContent("Test Content")
                .createdAt(LocalDateTime.now())
                .build();

        opinion = Opinion.builder()
                .id(1L)
                .opinionContent("Test Opinion")
                .opinionType(null)  // OpinionType의 값을 넣어주어야 한다면 적절히 설정
                .post(post)
                .created_at(LocalDateTime.now())
                .build();

        addOpinionRequest = AddOpinionRequest.builder()
                .opinionContent("New Opinion Content")
                .opinionType(null)  // OpinionType 설정 필요
                .postId(1L)
                .build();

        userUpdateOpinionDto = UserUpdateOpinionDto.builder()
                .opinionContent("Updated Opinion Content")
                .build();
    }

    @Test
    @DisplayName("의견 저장 테스트")
    void saveOpinionTest() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        opinionService.save(addOpinionRequest);

        ArgumentCaptor<Opinion> opinionCaptor = ArgumentCaptor.forClass(Opinion.class);
        verify(opinionRepository, times(1)).save(opinionCaptor.capture());

        Opinion savedOpinion = opinionCaptor.getValue();
        assertEquals("New Opinion Content", savedOpinion.getOpinionContent());
        assertEquals(post, savedOpinion.getPost());
        assertNotNull(savedOpinion.getCreated_at());
    }

    @Test
    @DisplayName("게시글이 없는 경우 예외 발생 테스트")
    void saveOpinion_PostNotFoundTest() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opinionService.save(addOpinionRequest);
        });

        assertEquals("해당 postId에 대한 Post가 존재하지 않습니다.", exception.getMessage());
        verify(opinionRepository, never()).save(any());
    }

    @Test
    @DisplayName("의견 ID로 찾기 테스트")
    void findByIdTest() {
        when(opinionRepository.findById(anyLong())).thenReturn(Optional.of(opinion));

        Optional<Opinion> foundOpinion = opinionService.findById(1L);

        assertTrue(foundOpinion.isPresent());
        assertEquals("Test Opinion", foundOpinion.get().getOpinionContent());
    }

    @Test
    @DisplayName("의견 삭제 테스트")
    void deleteOpinionTest() {
        when(opinionRepository.findById(anyLong())).thenReturn(Optional.of(opinion));

        opinionService.delete(1L);

        verify(opinionRepository, times(1)).deleteById(opinion.getId());
    }

    @Test
    @DisplayName("의견 삭제 예외 테스트 - 존재하지 않는 경우")
    void deleteOpinion_NotFoundTest() {
        when(opinionRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            opinionService.delete(1L);
        });

        assertEquals("해당 게시물을 찾을수 없습니다.", exception.getMessage());
        verify(opinionRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("의견 업데이트 테스트")
    void updateOpinionTest() {
        when(opinionRepository.findById(anyLong())).thenReturn(Optional.of(opinion));

        opinionService.update(1L, userUpdateOpinionDto);

        verify(opinionRepository, times(1)).save(opinion);
        assertEquals("Updated Opinion Content", opinion.getOpinionContent());
    }

    @Test
    @DisplayName("의견 업데이트 예외 테스트 - 존재하지 않는 경우")
    void updateOpinion_NotFoundTest() {
        when(opinionRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            opinionService.update(1L, userUpdateOpinionDto);
        });

        assertEquals("해당 게시물을 찾을수 없습니다.", exception.getMessage());
        verify(opinionRepository, never()).save(any(Opinion.class));
    }
}