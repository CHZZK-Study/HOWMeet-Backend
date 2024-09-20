package org.chzzk.howmeet.domain.temporary.auth.service;

import org.chzzk.howmeet.domain.temporary.guest.exception.GuestException;
import org.springframework.stereotype.Component;

import static org.chzzk.howmeet.domain.temporary.guest.exception.GuestErrorCode.INVALID_PASSWORD;

@Component
public class PasswordValidator {
    public static final String PASSWORD_REGEX = "^[A-Za-z0-9]{4,}$";

    public void validate(final String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new GuestException(INVALID_PASSWORD);
        }
    }
}
