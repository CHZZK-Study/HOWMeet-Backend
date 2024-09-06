package org.chzzk.howmeet.domain.regular.auth.controller;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {
    protected static final String REFRESH_TOKEN = "refreshToken";
    private static final String ALL_PATH = "/";
    private static final int REMOVAL_MAX_AGE = 0;

    private final Long expiration;

    public RefreshTokenCookieProvider(@Value("${auth.refresh-token.expiration}") final Long expiration) {
        this.expiration = expiration;
    }

    public ResponseCookie createCookie(final String value) {
        return createCookieBuilder(value)
                .maxAge(expiration)
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        return createCookieBuilder(Strings.EMPTY)
                .maxAge(REMOVAL_MAX_AGE)
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder createCookieBuilder(final String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path(ALL_PATH)
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }
}
