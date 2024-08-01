package org.chzzk.howmeet.domain.temporary.auth.util;

public interface PasswordEncoder {
    String encode(final String password);
    boolean matches(final String planePassword, final String encodedPassword);
}
