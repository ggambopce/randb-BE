package com.jinho.randb.domain.profile.dao;

import com.jinho.randb.domain.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositoryCustom {

    Optional<Profile> findByAccountId(Long id);
}
