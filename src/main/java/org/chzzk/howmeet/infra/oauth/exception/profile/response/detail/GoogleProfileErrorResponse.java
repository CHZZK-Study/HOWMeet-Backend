package org.chzzk.howmeet.infra.oauth.exception.profile.response.detail;

import org.chzzk.howmeet.infra.oauth.exception.profile.response.OAuthProfileErrorResponse;

public record GoogleProfileErrorResponse(Error error) implements OAuthProfileErrorResponse {
    @Override
    public String getErrorCode() {
        return error.code;
    }

    @Override
    public String getMessage() {
        return error.message;
    }

    record Error(String code, String message, String status) {
    }
}
