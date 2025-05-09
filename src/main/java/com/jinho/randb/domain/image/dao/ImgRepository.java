package com.jinho.randb.domain.image.dao;

import com.jinho.randb.domain.image.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgRepository extends JpaRepository<UploadFile, Long>, ImgRepositoryCustom {
}
