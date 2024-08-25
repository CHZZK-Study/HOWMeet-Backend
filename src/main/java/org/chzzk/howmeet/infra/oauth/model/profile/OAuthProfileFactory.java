package org.chzzk.howmeet.infra.oauth.model.profile;


import org.chzzk.howmeet.infra.oauth.model.profile.detail.GoogleProfile;
import org.chzzk.howmeet.infra.oauth.model.profile.detail.KakaoProfile;
import org.chzzk.howmeet.infra.oauth.model.profile.detail.NaverProfile;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthProfileFactory {
    KAKAO("kakao", KakaoProfile::new),
    NAVER("naver", NaverProfile::new),
    GOOGLE("google", GoogleProfile::new);

    private final String socialName;
    private final Function<Map<String, Object>, OAuthProfile> mapper;

    OAuthProfileFactory(final String socialName,
                        final Function<Map<String, Object>, OAuthProfile> mapper) {
        this.socialName = socialName;
        this.mapper = mapper;
    }

    public static OAuthProfile of(final Map<String, Object> attributes,
                                  final String providerName) {
        return Arrays.stream(values())
                .filter(value -> value.socialName.equals(providerName.toLowerCase()))
                .findAny()
                .map(value -> value.mapper.apply(attributes))
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
