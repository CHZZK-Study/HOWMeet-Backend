package org.chzzk.howmeet.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.common.model.Password;

@Converter
public class PasswordConverter implements AttributeConverter<Password, String> {
    @Override
    public String convertToDatabaseColumn(final Password attribute) {
        return attribute.getValue();
    }

    @Override
    public Password convertToEntityAttribute(final String dbData) {
        return Password.from(dbData);
    }
}
