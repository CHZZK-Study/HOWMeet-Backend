package org.chzzk.howmeet.infra.oauth.service.decorator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
public class OAuthTimeoutDecorator {
    private final int timeout;
    private final int maxRetry;

    public OAuthTimeoutDecorator(@Value("${oauth.timeout}") final int timeout,
                                 @Value("${oauth.max-retry}") final int maxRetry) {
        this.timeout = timeout;
        this.maxRetry = maxRetry;
    }

    public <T> Mono<T> decorate(final Mono<T> mono) {
        return mono.timeout(Duration.ofMillis(timeout))
                .retryWhen(Retry.max(maxRetry).filter(this::isRetryable))
                .onErrorMap(TimeoutException.class, e -> new IllegalStateException("요청 시간이 초과되었습니다.", e));
    }

    private boolean isRetryable(Throwable ex) {
        return (ex instanceof IllegalStateException) || (ex instanceof TimeoutException);
    }
}
