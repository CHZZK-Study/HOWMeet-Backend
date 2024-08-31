package org.chzzk.howmeet.infra.oauth.exception.profile;

import lombok.extern.slf4j.Slf4j;
import org.chzzk.howmeet.infra.oauth.exception.OAuthClientException;
import org.chzzk.howmeet.infra.oauth.exception.profile.response.OAuthProfileErrorResponse;

@Slf4j
public class OAuthProfileException extends OAuthClientException {
    public OAuthProfileException(final OAuthProfileErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        log.error("소셜 프로필 조회 실패. 에러 코드 : {}, 에러 메시지 : {}", errorResponse.getErrorCode(), errorResponse.getMessage());
    }
}
