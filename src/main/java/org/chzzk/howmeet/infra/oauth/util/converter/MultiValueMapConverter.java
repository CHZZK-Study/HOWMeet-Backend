package org.chzzk.howmeet.infra.oauth.util.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public final class MultiValueMapConverter {
    public static MultiValueMap<String, String> convertFrom(final ObjectMapper objectMapper,
                                                            final Object source) {
        try {
            final Map<String, String> convertedValue = objectMapper.convertValue(source, new TypeReference<>() {
            });
            return getEncodedParams(convertedValue);
        } catch (Exception e) {
            log.error("MultiValueMap으로 변환 실패 : {}", e.getMessage());
            throw new IllegalStateException();
        }
    }

    private static MultiValueMap<String, String> getEncodedParams(final Map<String, String> convertedValue) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        convertedValue.forEach((key, value) -> {
            final String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
            final String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
            params.add(encodedKey, encodedValue);
        });

        return params;
    }
}
