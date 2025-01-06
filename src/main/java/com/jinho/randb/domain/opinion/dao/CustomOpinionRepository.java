package com.jinho.randb.domain.opinion.dao;

import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;

import java.util.List;

public interface CustomOpinionRepository {

    List<OpinionContentAndTypeDto> findByPostId(Long postId);
}
