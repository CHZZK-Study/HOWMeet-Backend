package org.chzzk.howmeet.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.common.model.Nickname;

@Converter
public class NicknameConverter implements AttributeConverter<Nickname, String> {
    @Override
    public String convertToDatabaseColumn(final Nickname attribute) {
        return attribute.getValue();
    }

    @Override
    public Nickname convertToEntityAttribute(final String dbData) {
        return Nickname.from(dbData);
    }
}
