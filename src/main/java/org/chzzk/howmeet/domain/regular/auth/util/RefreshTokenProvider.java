package org.chzzk.howmeet.domain.regular.auth.util;

import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RefreshTokenProvider {
    private final Long expiration;

    public RefreshTokenProvider(@Value("${auth.refresh-token.expiration}") final Long expiration) {
        this.expiration = expiration;
    }

    public RefreshToken createToken(final AuthPrincipal authPrincipal) {
        final String value = UUID.randomUUID().toString();
        return RefreshToken.of(authPrincipal, value, expiration);
    }
}
