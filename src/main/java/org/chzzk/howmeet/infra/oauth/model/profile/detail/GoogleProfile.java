package org.chzzk.howmeet.infra.oauth.model.profile.detail;

import org.chzzk.howmeet.infra.oauth.model.profile.OAuthProfile;

import java.util.Map;

public class GoogleProfile extends OAuthProfile {
    public GoogleProfile(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileImage() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getSocialId() {
        return (String) attributes.get("id");
    }
}
