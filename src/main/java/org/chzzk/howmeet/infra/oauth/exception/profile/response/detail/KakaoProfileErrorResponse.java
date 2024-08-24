package org.chzzk.howmeet.infra.oauth.exception.profile.response.detail;

import org.chzzk.howmeet.infra.oauth.exception.profile.response.OAuthProfileErrorResponse;

public record KakaoProfileErrorResponse(Integer code, String msg) implements OAuthProfileErrorResponse {
    @Override
    public String getErrorCode() {
        return code.toString();
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
