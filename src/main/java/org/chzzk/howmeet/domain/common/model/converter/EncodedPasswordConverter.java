package org.chzzk.howmeet.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;

@Converter
public class EncodedPasswordConverter implements AttributeConverter<EncodedPassword, String> {
    @Override
    public String convertToDatabaseColumn(final EncodedPassword attribute) {
        return attribute.getValue();
    }

    @Override
    public EncodedPassword convertToEntityAttribute(final String dbData) {
        return EncodedPassword.from(dbData);
    }
}
