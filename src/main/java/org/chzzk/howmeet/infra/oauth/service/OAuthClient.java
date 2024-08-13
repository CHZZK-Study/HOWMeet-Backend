package org.chzzk.howmeet.infra.oauth.service;

import org.chzzk.howmeet.infra.oauth.dto.token.request.OAuthTokenRequest;
import org.chzzk.howmeet.infra.oauth.dto.token.response.OAuthTokenResponse;
import org.chzzk.howmeet.infra.oauth.model.OAuthProvider;
import org.chzzk.howmeet.infra.oauth.util.MultiValueMapConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class OAuthClient {
    private final MultiValueMapConverter multiValueMapConverter;
    private final WebClient webClient;
    private final int timeout;
    private final int maxRetry;

    public OAuthClient(final MultiValueMapConverter multiValueMapConverter,
                       final WebClient webClient,
                       @Value("${oauth.timeout}") final int timeout,
                       @Value("${oauth.max-retry}") final int maxRetry) {
        this.multiValueMapConverter = multiValueMapConverter;
        this.webClient = webClient;
        this.timeout = timeout;
        this.maxRetry = maxRetry;
    }

    public Mono<Map<String, Object>> getProfile(final OAuthProvider provider, final String code) {
        return issueToken(provider, code)
                .flatMap(response -> getProfileFromToken(provider, response.access_token()));
    }

    private Mono<Map<String, Object>> getProfileFromToken(final OAuthProvider provider, final String socialAccessToken) {
        return webClient.method(provider.profileMethod())
                .uri(provider.profileUrl())
                .headers(header -> header.setBearerAuth(socialAccessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .timeout(Duration.ofMillis(timeout))
                .retryWhen(Retry.max(maxRetry).filter(this::isRetryable))
                .onErrorMap(TimeoutException.class, e -> new IllegalStateException("요청 시간이 초과되었습니다.", e));
    }

    private Mono<OAuthTokenResponse> issueToken(final OAuthProvider provider, final String code) {
        return webClient.method(provider.tokenMethod())
                .uri(provider.tokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getIssueTokenParams(provider, code)))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .retryWhen(Retry.max(maxRetry).filter(this::isRetryable))
                .onErrorMap(TimeoutException.class, e -> new IllegalStateException("요청 시간이 초과되었습니다.", e));
    }

    private MultiValueMap<String, String> getIssueTokenParams(final OAuthProvider provider, final String code) {
        return multiValueMapConverter.convertFrom(OAuthTokenRequest.of(provider, code));
    }

    private boolean isRetryable(Throwable ex) {
        return (ex instanceof IllegalStateException) || (ex instanceof TimeoutException);
    }
}
