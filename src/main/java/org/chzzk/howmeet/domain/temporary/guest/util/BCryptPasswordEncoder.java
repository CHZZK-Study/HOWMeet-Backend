package org.chzzk.howmeet.domain.temporary.guest.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(final String planePassword) {
        return BCrypt.hashpw(planePassword, BCrypt.gensalt());
    }

    @Override
    public boolean matches(final String planePassword, final String encodedPassword) {
        return BCrypt.checkpw(planePassword, encodedPassword);
    }
}
