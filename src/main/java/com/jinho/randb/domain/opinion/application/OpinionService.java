package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinion.dto.OpinionDto;
import com.jinho.randb.domain.opinion.dto.UserUpdateOpinionDto;

import java.util.List;
import java.util.Optional;

public interface OpinionService {

    void save(AddOpinionRequest addOpinionRequest, Long accountId);

    Optional<Opinion> findById(Long id);

    List<OpinionContentAndTypeDto> findByPostId(Long postId);

    void delete(Long opinionId, Long accountId);

    void update(Long opinionId, Long accountId, UserUpdateOpinionDto userUpdateOpinionDto);
}
