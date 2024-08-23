package org.chzzk.howmeet.infra.oauth.exception.token.response.detail;

import org.chzzk.howmeet.infra.oauth.exception.token.response.OAuthTokenIssueErrorResponse;

public record KakaoTokenIssueErrorResponse(String error, String error_description) implements OAuthTokenIssueErrorResponse {
    @Override
    public String getErrorCode() {
        return error;
    }

    @Override
    public String getMessage() {
        return error_description;
    }
}
