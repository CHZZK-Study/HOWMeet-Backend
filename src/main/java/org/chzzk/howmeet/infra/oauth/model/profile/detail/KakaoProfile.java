package org.chzzk.howmeet.infra.oauth.model.profile.detail;

import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;

import java.util.Map;

public class KakaoProfile extends OAuthProfile {
    public KakaoProfile(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getNickname() {
        return String.valueOf(getAccount().get("name"));
    }

    @Override
    public String getProfileImage() {
        return String.valueOf(getProfile().get("profile_image_url"));
    }

    @Override
    public String getSocialId() {
        return String.valueOf(attributes.get("id"));
    }

    private Map<String, Object> getAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getAccount().get("profile");
    }
}
