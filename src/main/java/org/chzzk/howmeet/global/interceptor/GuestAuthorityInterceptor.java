package org.chzzk.howmeet.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.chzzk.howmeet.domain.common.auth.exception.AuthenticationException;
import org.chzzk.howmeet.domain.common.auth.exception.AuthorizationException;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.temporary.auth.annotation.TemporaryUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

import static org.chzzk.howmeet.domain.common.auth.exception.AuthErrorCode.JWT_FORBIDDEN;
import static org.chzzk.howmeet.domain.common.auth.exception.AuthErrorCode.JWT_NOT_FOUND;

@Component
public class GuestAuthorityInterceptor implements HandlerInterceptor {
    private final String authAttributeKey;

    public GuestAuthorityInterceptor(@Value("${auth.attribute-key}") final String authAttributeKey) {
        this.authAttributeKey = authAttributeKey;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final TemporaryUser temporaryUser = handlerMethod.getMethodAnnotation(TemporaryUser.class);
        if (!Objects.isNull(temporaryUser)) {
            validateGuestAuthorization(request);
        }

        return true;
    }

    private void validateGuestAuthorization(final HttpServletRequest request) {
        final AuthPrincipal authPrincipal = (AuthPrincipal) request.getAttribute(authAttributeKey);
        if (!authPrincipal.isGuest()) {
            throw new AuthorizationException(JWT_FORBIDDEN);
        }
    }
}
