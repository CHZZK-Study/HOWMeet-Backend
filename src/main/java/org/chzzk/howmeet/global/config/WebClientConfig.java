package org.chzzk.howmeet.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfig {
    private static final String REQUEST_LOG_FORMAT = "[REQUEST URL] {}, [METHOD] {}";
    private static final String REQUEST_HEADER_LOG_FORMAT = "[REQUEST HEADER] {}={}";
    private static final String RESPONSE_LOG_FORMAT = "[STATUS] {}";
    private static final String RESPONSE_HEADER_LOG_FORMAT = "[RESPONSE HEADER] {}={}";
    private static final int TIMEOUT_MILLS = 3_000;

    @Bean
    public DefaultUriBuilderFactory defaultUriBuilderFactory() {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return factory;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(logRequest())
                .filter(logResponse())
                .uriBuilderFactory(defaultUriBuilderFactory())
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MILLS)
                .responseTimeout(Duration.ofMillis(TIMEOUT_MILLS))
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(TIMEOUT_MILLS, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_MILLS, TimeUnit.MILLISECONDS))
                );
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info(REQUEST_LOG_FORMAT, clientRequest.url(), clientRequest.method());
            clientRequest.headers()
                    .forEach((name, values) -> {
                        values.forEach(value -> log.info(REQUEST_HEADER_LOG_FORMAT, name, value));
                    });
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                    log.info(RESPONSE_LOG_FORMAT, clientResponse.statusCode());
                    clientResponse.headers()
                            .asHttpHeaders()
                            .forEach((name, values) -> {
                                values.forEach(value -> log.info(RESPONSE_HEADER_LOG_FORMAT, name, value));
                            });
                    return Mono.just(clientResponse);
                }
        );
    }
}
