package org.chzzk.howmeet.domain.regular.room.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;

@Converter
public class RoomDescriptionConverter implements AttributeConverter<RoomDescription, String> {
    @Override
    public String convertToDatabaseColumn(final RoomDescription attribute) {
        return attribute.getValue();
    }

    @Override
    public RoomDescription convertToEntityAttribute(final String dbData) {
        return RoomDescription.from(dbData);
    }
}
