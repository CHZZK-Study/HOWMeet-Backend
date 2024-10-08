package org.chzzk.howmeet.infra.oauth.util.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.infra.oauth.exception.UnsupportedProviderException;
import org.chzzk.howmeet.infra.oauth.util.serilaizer.ListToCommaSeparatedStringSerializer;
import org.chzzk.howmeet.infra.oauth.util.serilaizer.ListToSpaceSeparatedStringSerializer;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthObjectMapperFactory {
    private static final Map<String, ObjectMapper> MAPPER_MAP;
    private static final ObjectMapper LIST_TO_COMMA_MAPPER;
    private static final ObjectMapper LIST_TO_SPACE_MAPPER;

    static {
        LIST_TO_COMMA_MAPPER = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new SimpleModule().addSerializer(List.class, new ListToCommaSeparatedStringSerializer()))
                .build();

        LIST_TO_SPACE_MAPPER = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new SimpleModule().addSerializer(List.class, new ListToSpaceSeparatedStringSerializer()))
                .build();

        MAPPER_MAP = Map.of(
                "kakao", LIST_TO_COMMA_MAPPER,
                "naver", LIST_TO_COMMA_MAPPER,
                "google", LIST_TO_SPACE_MAPPER
        );
    }

    public static ObjectMapper getFrom(final String providerName) {
        if (!MAPPER_MAP.containsKey(providerName)) {
            throw new UnsupportedProviderException(providerName);
        }

        return MAPPER_MAP.get(providerName);
    }
}
