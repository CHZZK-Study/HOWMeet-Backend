package org.chzzk.howmeet.domain.regular.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.auth.repository.RefreshTokenRepository;
import org.chzzk.howmeet.domain.regular.auth.util.RefreshTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenCrudService {
    private final RefreshTokenProvider refreshTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken save(final AuthPrincipal authPrincipal) {
        return refreshTokenRepository.save(refreshTokenProvider.createToken(authPrincipal));
    }

    public void delete(final AuthPrincipal authPrincipal, final String value) {
        refreshTokenRepository.deleteByMemberIdAndValue(authPrincipal.id(), value);
    }
}
