package org.chzzk.howmeet.domain.regular.auth.repository;

import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    void deleteByMemberIdAndValue(@Param("memberId") final Long memberId, @Param("value") final String value);
}
