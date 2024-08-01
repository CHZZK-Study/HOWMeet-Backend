package org.chzzk.howmeet.global.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.repository.MemberRepository;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.chzzk.howmeet.global.util.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final String BEARER_TYPE = "Bearer";

    private final String authAttributeKey;
    private final GuestRepository guestRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    public AuthenticationInterceptor(@Value("${auth.attribute-key}") final String authAttributeKey,
                                     final GuestRepository guestRepository,
                                     final MemberRepository memberRepository,
                                     final ObjectMapper objectMapper,
                                     final TokenProvider tokenProvider) {
        this.authAttributeKey = authAttributeKey;
        this.guestRepository = guestRepository;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (isEmptyAuthHeader(request)) {
            return true;
        }

        final String accessToken = getAccessToken(request);
        validateToken(accessToken);

        final AuthPrincipal authPrincipal = getAuthPrincipal(accessToken);
        validateMember(authPrincipal);
        request.setAttribute(authAttributeKey, authPrincipal);

        return true;
    }

    private boolean isEmptyAuthHeader(final HttpServletRequest request) {
        return StringUtils.isNullOrEmpty(request.getHeader(AUTHORIZATION));
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String value = request.getHeader(AUTHORIZATION);
        if (StringUtils.isNullOrEmpty(value) || !value.startsWith(BEARER_TYPE)) {
            throw new IllegalArgumentException();
        }

        return value.substring(BEARER_TYPE.length())
                .trim();
    }

    private void validateToken(final String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMember(final AuthPrincipal authPrincipal) {
        final Long id = authPrincipal.id();
        if (!guestRepository.existsById(id) && !memberRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
    }

    private AuthPrincipal getAuthPrincipal(final String accessToken) {
        try {
            return objectMapper.readValue(tokenProvider.getPayload(accessToken), AuthPrincipal.class);
        } catch (JsonProcessingException e) {
            log.info("AuthPrincipal 변환 중 에러가 발생했습니다. accessToken : {}", accessToken);
            throw new RuntimeException(e);
        }
    }
}
