package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.springframework.test.util.ReflectionTestUtils;

public enum GuestFixture {
    KIM("김민우", "qwer1234");
    private final String nickname;
    private final String password;

    GuestFixture(final String nickname, final String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Guest 생성() {
        return 생성(null, 1L);
    }

    public Guest 생성(final Long id) {
        return 생성(id, 1L);
    }

    public Guest 생성(final Long id, final Long guestScheduleId) {
        final Guest guest = Guest.of(guestScheduleId, nickname, EncodedPassword.from(password));
        ReflectionTestUtils.setField(guest, "id", id);// Reflection 을 사용하여 PK 설정
        return guest;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
