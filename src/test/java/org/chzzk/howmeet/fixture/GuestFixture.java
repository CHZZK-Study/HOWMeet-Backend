package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;

public enum GuestFixture {
    KIM("김민우", "qwer1234!");
    private final String nickname;
    private final String password;

    GuestFixture(final String nickname, final String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Guest 생성() {
        return Guest.of(1L, nickname, EncodedPassword.from(password));
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
