package org.chzzk.howmeet.infra.oauth.model.profile.detail;

import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;

import java.util.Map;

public class NaverProfile extends OAuthProfile {
    public NaverProfile(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getSocialId() {
        return (String) getResponse().get("id");
    }

    @Override
    public String getNickname() {
        return (String) getResponse().get("name");
    }

    @Override
    public String getProfileImage() {
        return (String) getResponse().get("profile_image");
    }

    private Map<String, Object> getResponse() {
        return (Map<String, Object>) attributes.get("response");
    }
}
