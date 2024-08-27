package org.chzzk.howmeet.infra.oauth.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.infra.oauth.exception.profile.response.OAuthProfileErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.profile.response.detail.GoogleProfileErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.profile.response.detail.KakaoProfileErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.profile.response.detail.NaverProfileErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.response.OAuthTokenIssueErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.response.detail.GoogleTokenIssueErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.response.detail.KakaoTokenIssueErrorResponse;
import org.chzzk.howmeet.infra.oauth.exception.token.response.detail.NaverTokenIssueErrorResponse;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthErrorResponseFactory {
    private static final Map<String, ProviderErrorResponseConfig> errorResponseConfigMap = new HashMap<>();

    static {
        errorResponseConfigMap.put("kakao", new ProviderErrorResponseConfig(
                KakaoProfileErrorResponse.class, KakaoTokenIssueErrorResponse.class)
        );
        errorResponseConfigMap.put("naver", new ProviderErrorResponseConfig(
                NaverProfileErrorResponse.class, NaverTokenIssueErrorResponse.class)
        );
        errorResponseConfigMap.put("google", new ProviderErrorResponseConfig(
                GoogleProfileErrorResponse.class, GoogleTokenIssueErrorResponse.class)
        );
    }

    public static Class<? extends OAuthProfileErrorResponse> getProfileResponseClassFrom(final String providerName) {
        return getProviderErrorResponseConfig(providerName).profileErrorResponseClass;
    }

    public static Class<? extends OAuthTokenIssueErrorResponse> getTokenIssueResponseClassFrom(final String providerName) {
        return getProviderErrorResponseConfig(providerName).tokenIssueErrorResponseClass;
    }

    private static ProviderErrorResponseConfig getProviderErrorResponseConfig(final String providerName) {
        final ProviderErrorResponseConfig config = errorResponseConfigMap.get(providerName);
        if (config == null) {
            throw new UnsupportedProviderException(providerName);
        }
        return config;
    }

    private record ProviderErrorResponseConfig(
            Class<? extends OAuthProfileErrorResponse> profileErrorResponseClass,
            Class<? extends OAuthTokenIssueErrorResponse> tokenIssueErrorResponseClass) {
    }
}
