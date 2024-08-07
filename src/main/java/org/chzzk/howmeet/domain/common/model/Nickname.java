package org.chzzk.howmeet.domain.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.auth.model.BadWordFilteringSingleton;
import org.chzzk.howmeet.domain.common.exception.NicknameException;

import static org.chzzk.howmeet.domain.common.exception.NicknameErrorCode.INVALID_NICKNAME;

@EqualsAndHashCode
@Getter
@ToString
public class Nickname {
    private static final String NICKNAME_REGEX = "^(?![-_]{2,10}$)[A-Za-z0-9가-힣]{2,10}$";

    private final String value;

    private Nickname(final String value) {
        validateNickname(value);
        this.value = value;
    }

    public static Nickname from(final String value) {
        return new Nickname(value);
    }

    private void validateNickname(final String value) {
        if (!value.matches(NICKNAME_REGEX) || BadWordFilteringSingleton.containsBadWord(value)) {
            throw new NicknameException(INVALID_NICKNAME);
        }
    }
}
