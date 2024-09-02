package org.chzzk.howmeet.domain.regular.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenException;
import org.chzzk.howmeet.domain.regular.auth.repository.RefreshTokenRepository;
import org.chzzk.howmeet.domain.regular.auth.util.RefreshTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenErrorCode.REFRESH_TOKEN_NO_AUTHORITY;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenCrudService {
    private final RefreshTokenProvider refreshTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken save(final AuthPrincipal authPrincipal) {
        validateAuthPrincipal(authPrincipal);
        return refreshTokenRepository.save(refreshTokenProvider.createToken(authPrincipal));
    }

    public void delete(final AuthPrincipal authPrincipal, final String value) {
        validateAuthPrincipal(authPrincipal);
        refreshTokenRepository.deleteByMemberIdAndValue(authPrincipal.id(), value);
    }

    private void validateAuthPrincipal(final AuthPrincipal authPrincipal) {
        if (!authPrincipal.isMember()) {
            throw new RefreshTokenException(REFRESH_TOKEN_NO_AUTHORITY);
        }
    }
}
