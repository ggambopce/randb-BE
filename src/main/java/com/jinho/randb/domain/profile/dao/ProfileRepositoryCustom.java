package com.jinho.randb.domain.profile.dao;

import com.jinho.randb.domain.profile.dto.ProfileDto;

public interface ProfileRepositoryCustom {

    ProfileDto profileDetails(Long profileId);
}
