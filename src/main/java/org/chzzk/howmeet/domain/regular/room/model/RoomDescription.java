package org.chzzk.howmeet.domain.regular.room.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class RoomDescription {
    private final String value;

    private RoomDescription(final String value) {
        validateDescription(value);
        this.value = value;
    }

    public static RoomDescription from(final String value) {
        return new RoomDescription(value);
    }

    private void validateDescription(final String value) {

    }
}
