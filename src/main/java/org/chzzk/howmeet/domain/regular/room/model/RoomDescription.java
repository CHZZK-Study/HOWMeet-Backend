package org.chzzk.howmeet.domain.regular.room.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class RoomDescription {
    private final String value;

    @JsonCreator
    private RoomDescription(final String value) {
        validateDescription(value);
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }


    public static RoomDescription from(final String value) {
        return new RoomDescription(value);
    }

    private void validateDescription(final String value) {

    }

    public boolean isNullOrEmpty() {
        return value == null || value.trim().isEmpty();
    }
}
