package org.chzzk.howmeet.infra.oauth.fixture;

import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

public enum OAuthProviderFixture {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google");

    private final String providerName;

    OAuthProviderFixture(final String providerName) {
        this.providerName = providerName;
    }

    public OAuthProvider 생성() {
        return new OAuthProvider(
                providerName,
                "clientId",
                "clientSecret",
                "code",
                "authorizeUrl",
                HttpMethod.GET,
                List.of("profile", "image"),
                "redirectUrl",
                "grantType",
                HttpMethod.GET,
                "tokenUrl",
                HttpMethod.GET,
                "profileUrl"
        );
    }

    public static OAuthProvider getFrom(final String providerName) {
        return Arrays.stream(values())
                .filter(value -> value.providerName.equals(providerName))
                .findAny()
                .map(OAuthProviderFixture::생성)
                .orElseThrow(IllegalArgumentException::new);
    }
}
