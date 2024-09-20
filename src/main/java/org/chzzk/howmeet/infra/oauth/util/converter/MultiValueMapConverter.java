package org.chzzk.howmeet.infra.oauth.util.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j
public final class MultiValueMapConverter {
    public static MultiValueMap<String, String> convertFrom(final ObjectMapper objectMapper,
                                                            final Object source,
                                                            final TransformationStrategy strategy) {
        try {
            final Map<String, String> convertedValue = objectMapper.convertValue(source, new TypeReference<>() {
            });
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            convertedValue.forEach((key, value) -> {
                final String transformedKey = strategy.transform(key);
                final String transformedValue = strategy.transform(value);
                params.add(transformedKey, transformedValue);
            });

            return params;
        } catch (Exception e) {
            log.error("MultiValueMap으로 변환 실패 : {}", e.getMessage());
            throw new IllegalStateException();
        }
    }
}
