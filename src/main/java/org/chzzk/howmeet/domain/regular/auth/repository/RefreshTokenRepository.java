package org.chzzk.howmeet.domain.regular.auth.repository;

import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByValue(final String value);
}
