package org.chzzk.howmeet.infra.oauth.model.profile;

import org.chzzk.howmeet.domain.regular.auth.entity.Member;

import java.util.Map;

public abstract class OAuthProfile {
    protected final Map<String, Object> attributes;

    protected OAuthProfile(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getNickname();
    public abstract String getProfileImage();
    public abstract String getSocialId();

    public Member toEntity() {
        return Member.of(getNickname(), getProfileImage(), getSocialId());
    }
}
