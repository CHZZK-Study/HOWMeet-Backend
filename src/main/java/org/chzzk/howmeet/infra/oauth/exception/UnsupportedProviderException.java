package org.chzzk.howmeet.infra.oauth.exception;

public class UnsupportedProviderException extends RuntimeException {
    public UnsupportedProviderException(final String providerName) {
        super(providerName + " 소셜 로그인은 지원하지 않습니다.");
    }
}
