package org.chzzk.howmeet.domain.회원.room.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.회원.room.model.RoomName;

@Converter
public class RoomNameConverter implements AttributeConverter<RoomName, String> {
    @Override
    public String convertToDatabaseColumn(final RoomName attribute) {
        return attribute.getValue();
    }

    @Override
    public RoomName convertToEntityAttribute(final String dbData) {
        return RoomName.from(dbData);
    }
}
