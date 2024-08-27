package org.chzzk.howmeet.infra.oauth.exception.profile.response.detail;

import org.chzzk.howmeet.infra.oauth.exception.profile.response.OAuthProfileErrorResponse;

public record NaverProfileErrorResponse(String resultCode, String message) implements OAuthProfileErrorResponse {
    @Override
    public String getErrorCode() {
        return resultCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
