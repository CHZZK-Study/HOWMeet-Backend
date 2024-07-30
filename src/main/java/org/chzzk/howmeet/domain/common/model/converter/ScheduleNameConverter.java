package org.chzzk.howmeet.domain.common.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.chzzk.howmeet.domain.common.model.ScheduleName;

@Converter
public class ScheduleNameConverter implements AttributeConverter<ScheduleName, String> {
    @Override
    public String convertToDatabaseColumn(final ScheduleName attribute) {
        return attribute.getValue();
    }

    @Override
    public ScheduleName convertToEntityAttribute(final String dbData) {
        return ScheduleName.from(dbData);
    }
}
