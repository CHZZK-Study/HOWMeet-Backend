package org.chzzk.howmeet.domain.회원.room.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class RoomName {
    private final String value;

    private RoomName(final String value) {
        validateName(value);
        this.value = value;
    }

    public static RoomName from(final String value) {
        return new RoomName(value);
    }

    private void validateName(final String value) {

    }
}
