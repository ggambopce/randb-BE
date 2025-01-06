package com.jinho.randb.domain.opinionsummary.application;

import com.jinho.randb.domain.opinion.domain.OpinionType;
import com.jinho.randb.domain.opinionsummary.domain.OpinionSummary;
import com.jinho.randb.domain.opinionsummary.dto.OpinionSummaryDto;
import com.jinho.randb.domain.opinionsummary.dto.OpinionSummaryResponseDto;

import java.util.Map;

public interface OpinionSummaryService {


    /**
     * 특정 토론글의 의견 데이터를 가져와 요약 후 저장
     * @param postId 요약 대상이 되는 토론글 ID
     * @return RED 또는 BLUE 요약 결과
     */
    Map<String, String> summarizeAndSave(Long postId);

    OpinionSummaryResponseDto findOpinionSummaryByPostId(Long postId);
}
