package org.chzzk.howmeet.fixture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;

public enum GSRecordFixture {

    KIMGSRECORD_A(GSFixture.createGuestScheduleA(), createMockGuest(), LocalDateTime.of(2023, 1, 1, 10, 30, 0));
//    KIMGSRECORD_B(2L, GSFixture.createGuestScheduleA(), KIM.생성(), LocalDateTime.of(2023, 1, 1, 11, 0, 0)),
//    KIMGSRECORD_C(3L, GSFixture.createGuestScheduleA(), KIM.생성(), LocalDateTime.of(2023, 1, 1, 11, 30, 0)),
//    KIMGSRECORD_D(3L, GSFixture.createGuestScheduleA(), KIM.생성(), LocalDateTime.of(2024, 1, 1, 11, 30, 0)),
//    KIMGSRECORD_E(3L, GSFixture.createGuestScheduleA(), KIM.생성(), LocalDateTime.of(2024, 1, 1, 11, 30, 0));

    private Guest guest;
    private GuestSchedule gs;
    private Long id;
    private LocalDateTime selectTime;

    GSRecordFixture(final GuestSchedule gs, final Guest guest, final LocalDateTime selectTime) {
//        this.id = id;
        this.gs = gs;
        this.guest = guest;
        this.selectTime = selectTime;
    }

    private static Guest createMockGuest() {
        Guest guest = mock(Guest.class);
        when(guest.getId()).thenReturn(1L); // 원하는 ID 값 설정
        when(guest.getNickname()).thenReturn(Nickname.from("김민우"));
        when(guest.getPassword()).thenReturn(EncodedPassword.from("123testpassword"));
        return guest;
    }

    public GuestScheduleRecord CREATE() {

        Long guestId = this.guest.getId();
        Long gsId = this.gs.getId();
        GuestScheduleRecord gsRecord = GuestScheduleRecord.of(this.guest, this.gs, this.selectTime);
        return gsRecord;
    }
}