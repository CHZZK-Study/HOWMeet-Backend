package org.chzzk.howmeet.domain.회원.room.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.회원.room.model.RoomDescription;

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
