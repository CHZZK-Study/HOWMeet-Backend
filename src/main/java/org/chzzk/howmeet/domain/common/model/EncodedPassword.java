package org.chzzk.howmeet.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.chzzk.howmeet.domain.temporary.guest.util.PasswordEncoder;

@EqualsAndHashCode
@Getter
@ToString
public class EncodedPassword {
    private final String value;

    private EncodedPassword(final String value) {
        this.value = value;
    }

    public static EncodedPassword from(final String value) {
        return new EncodedPassword(value);
    }

    public static EncodedPassword of(final String rawPassword, final PasswordEncoder passwordEncoder) {
        return new EncodedPassword(passwordEncoder.encode(rawPassword));
    }

    public boolean isMatch(final String rawPassword, final PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, value);
    }
}
