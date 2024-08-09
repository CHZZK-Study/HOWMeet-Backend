package org.chzzk.howmeet.infra.oauth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MultiValueMapConverter {
    private final ObjectMapper objectMapper;

    public MultiValueMap<String, String> convertFrom(final Object object) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        try {
            final Map<String, String> convertedValue = objectMapper.convertValue(object, new TypeReference<>() {
            });
            params.setAll(convertedValue);
            return params;
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
