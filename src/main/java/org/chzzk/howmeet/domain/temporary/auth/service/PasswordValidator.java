package org.chzzk.howmeet.domain.temporary.auth.service;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public static final String PASSWORD_REGEX = "^[A-Za-z0-9]{4,}$";

    public void validate(final String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException();
        }
    }
}
