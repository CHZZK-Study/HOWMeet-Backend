package org.chzzk.howmeet.infra.oauth.exception;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;

public class UnsupportedProviderException extends RuntimeException {
    public UnsupportedProviderException(final String providerName) {
        super(providerName + " 소셜 로그인은 지원하지 않습니다.");
    }

    public UnsupportedProviderException(final OAuthProvider oAuthProvider) {
        super(oAuthProvider.name());
    }
}
