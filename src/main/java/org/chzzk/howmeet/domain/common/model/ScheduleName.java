package org.chzzk.howmeet.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class ScheduleName {
    private final String value;

    private ScheduleName(final String value) {
        this.value = value;
    }

    public static ScheduleName from(final String value) {
        return new ScheduleName(value);
    }

    private void validateName(final String value) {

    }
}
