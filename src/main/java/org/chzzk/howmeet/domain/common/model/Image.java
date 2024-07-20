package org.chzzk.howmeet.domain.common.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Image {
    private final String value;

    public static Image from(final String value) {
        return new Image(value);
    }
}
