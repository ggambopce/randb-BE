package com.jinho.randb.domain.opinionsummary.application;

import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinionsummary.dao.OpinionSummaryRepository;
import com.jinho.randb.domain.opinionsummary.domain.OpinionSummary;
import com.jinho.randb.domain.opinionsummary.domain.QOpinionSummary;
import com.jinho.randb.domain.opinionsummary.dto.OpinionSummaryDto;
import com.jinho.randb.domain.opinionsummary.dto.OpinionSummaryResponseDto;
import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class OpinionSummaryServiceImpl implements OpinionSummaryService {


    private final OpinionService opinionService;
    private final OpinionSummaryRepository opinionSummaryRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final OpenAiChatModel openAiChatModel;
    //private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    @Override
    public Map<String, String> summarizeAndSave(Long postId) {
        // 토론글 ID로 Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 토론글을 찾을 수 없습니다."));

        // 의견 데이터 가져오기
        List<OpinionContentAndTypeDto> opinionDtos = opinionService.findByPostId(postId);

        // RED와 BLUE 의견 필터링 및 요약
        String redSummary = summarizeOpinions(opinionDtos, OpinionType.RED);
        String blueSummary = summarizeOpinions(opinionDtos, OpinionType.BLUE);

        // RED 의견 요약 저장
        OpinionSummary redOpinionSummary = OpinionSummary.builder()
                .post(post)
                .opinionSummaryContent(redSummary)
                .opinionType(OpinionType.RED)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        // BLUE 의견 요약 저장
        OpinionSummary blueOpinionSummary = OpinionSummary.builder()
                .post(post)
                .opinionSummaryContent(blueSummary)
                .opinionType(OpinionType.BLUE)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        // 저장
        opinionSummaryRepository.save(redOpinionSummary);
        opinionSummaryRepository.save(blueOpinionSummary);

        // 요약된 내용 반환
        return Map.of(
                "RED", redSummary,
                "BLUE", blueSummary
        );
    }

    @Override
    public OpinionSummaryResponseDto findOpinionSummaryByPostId(Long postId) {

        // 토론글 ID로 요약 정보 조회
        List<OpinionSummary> summaries = opinionSummaryRepository.findByPostId(postId);

        String redSummary = summaries.stream()
                .filter(summary -> summary.getOpinionType() == OpinionType.RED)
                .map(OpinionSummary::getOpinionSummaryContent)
                .findFirst()
                .orElse("RED 요약이 없습니다.");

        String blueSummary = summaries.stream()
                .filter(summary -> summary.getOpinionType() == OpinionType.BLUE)
                .map(OpinionSummary::getOpinionSummaryContent)
                .findFirst()
                .orElse("BLUE 요약이 없습니다.");

        return OpinionSummaryResponseDto.fromSummaries(redSummary, blueSummary);
    }

    /**
     * 특정 의견 타입에 대한 의견 목록을 요약하는 메서드
     */
    private String summarizeOpinions(List<OpinionContentAndTypeDto> opinionDtos, OpinionType opinionType) {
        // 의견 필터링
        List<String> opinions = opinionDtos.stream()
                .filter(opinion -> opinion.getOpinionType() == opinionType)
                .map(OpinionContentAndTypeDto::getOpinionContent)
                .collect(Collectors.toList());

        if (opinions.isEmpty()) {
            throw new IllegalArgumentException(opinionType + "에 대한 의견이 없습니다.");
        }

        // GPT 요약 요청
        String prompt = "다음은 " + opinionType + " 입장의 의견 목록입니다. 비속어나 비방하는 부분은 제외하고 논리정연하게 이 의견들을 요약하여 하나의 논설문으로 만들어 주세요:\n" + String.join("\n", opinions);
        return openAiChatModel.call(prompt);
    }
}
