package org.chzzk.howmeet.infra.oauth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
@Slf4j
public class MultiValueMapConverter {
    private final ObjectMapper objectMapper;

    public MultiValueMapConverter(@Qualifier("oauthObjectMapper") final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MultiValueMap<String, String> convertFrom(final Object object) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        try {
            final Map<String, String> convertedValue = objectMapper.convertValue(object, new TypeReference<>() {
            });
            params.setAll(convertedValue);
            return params;
        } catch (Exception e) {
            log.error("MultiValueMap으로 변환 실패 : {}", e.getMessage());
            throw new IllegalStateException();
        }
    }
}
