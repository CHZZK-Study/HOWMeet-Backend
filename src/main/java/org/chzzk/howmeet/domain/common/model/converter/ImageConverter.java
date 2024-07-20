package org.chzzk.howmeet.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.common.model.Image;

@Converter
public class ImageConverter implements AttributeConverter<Image, String> {
    @Override
    public String convertToDatabaseColumn(final Image attribute) {
        return attribute.getValue();
    }

    @Override
    public Image convertToEntityAttribute(final String dbData) {
        return Image.from(dbData);
    }
}
