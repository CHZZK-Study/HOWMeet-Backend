package org.chzzk.howmeet.infra.oauth.exception.token.response;

public interface OAuthTokenIssueErrorResponse {
    String getErrorCode();
    String getMessage();
}
