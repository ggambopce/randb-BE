package com.jinho.randb.domain.opinion.dao;

import com.jinho.randb.domain.opinion.domain.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long>, CustomOpinionRepository {

}
