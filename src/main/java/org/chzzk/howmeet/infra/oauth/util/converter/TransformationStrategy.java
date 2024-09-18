package org.chzzk.howmeet.infra.oauth.util.converter;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public enum TransformationStrategy {
    DECODE(value -> URLDecoder.decode(value, StandardCharsets.UTF_8)),
    ENCODE(value -> URLEncoder.encode(value, StandardCharsets.UTF_8));

    private final Function<String, String> transformer;

    TransformationStrategy(final Function<String, String> transformer) {
        this.transformer = transformer;
    }

    public String transform(final String value) {
        return transformer.apply(value);
    }
}
