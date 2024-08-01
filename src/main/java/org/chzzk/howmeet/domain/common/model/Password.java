package org.chzzk.howmeet.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Password {
    private final String value;

    private Password(final String value) {
        validatePassword(value);
        this.value = value;
    }

    public static Password from(final String value) {
        return new Password(value);
    }

    private void validatePassword(final String value) {

    }
}
