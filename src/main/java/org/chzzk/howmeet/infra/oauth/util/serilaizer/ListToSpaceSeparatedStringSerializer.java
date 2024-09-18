package org.chzzk.howmeet.infra.oauth.util.serilaizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class ListToSpaceSeparatedStringSerializer extends JsonSerializer<List> {

    @Override
    public void serialize(List value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeString("");
            return;
        }

        StringJoiner joiner = new StringJoiner(" ");
        for (Object item : value) {
            joiner.add(item.toString());
        }

        gen.writeString(joiner.toString());
    }
}

