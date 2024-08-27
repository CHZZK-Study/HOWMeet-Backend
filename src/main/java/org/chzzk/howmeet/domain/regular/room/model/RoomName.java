package org.chzzk.howmeet.domain.regular.room.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class RoomName {
    private final String value;

    @JsonCreator
    private RoomName(final String value) {
        validateName(value);
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static RoomName from(final String value) {
        return new RoomName(value);
    }

    private void validateName(final String value) {

    }

    public boolean isNullOrEmpty() {
        return value == null || value.trim().isEmpty();
    }
}
