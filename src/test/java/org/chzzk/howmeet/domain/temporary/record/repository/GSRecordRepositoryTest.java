package org.chzzk.howmeet.domain.temporary.record.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.chzzk.howmeet.fixture.GSFixture;
import org.chzzk.howmeet.fixture.GSRecordFixture;
import org.chzzk.howmeet.fixture.GuestFixture;
import org.chzzk.howmeet.global.config.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class GSRecordRepositoryTest {

    @Autowired
    GSRecordRepository gsRecordRepository;

    @Autowired
    GSRepository gsRepository;

    @Autowired
    TmpGuestRepository tmpGuestRepository;

    @Test
    @DisplayName("GuestScheduleId로 GuestScheduleRecord 찾기")
    public void findByGuestScheduleId() {

        GuestScheduleRecord gsRecord = GSRecordFixture.KIMGSRECORD_A.CREATE();

        gsRecordRepository.save(gsRecord);
        List<GuestScheduleRecord> records = gsRecordRepository.findByGuestScheduleId(1L);

        assertThat(records).isNotEmpty();
        assertThat(records.get(0).getGuestScheduleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("GuestId로 GuestScheduleRecord 찾기")
    public void findByGuestId() {
        GuestScheduleRecord gsRecord = GSRecordFixture.KIMGSRECORD_A.CREATE();
        gsRecordRepository.save(gsRecord);

        List<GuestScheduleRecord> result = gsRecordRepository.findByGuestId(gsRecord.getGuestId());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(gsRecord);
    }

    @Test
    @DisplayName("GuestId로 GuestScheduleRecord 삭제하기")
    public void deleteByGuestId() {
        GuestScheduleRecord gsRecord = GSRecordFixture.KIMGSRECORD_A.CREATE();
        gsRecordRepository.save(gsRecord);

        gsRecordRepository.deleteByGuestId(gsRecord.getGuestId());
        List<GuestScheduleRecord> result = gsRecordRepository.findByGuestId(gsRecord.getGuestId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("tmpGuestRepository에서 GuestScheduleId로 Guest 찾기")
    public void findByGuestScheduleIdTmpGuestRepository() {
        GuestSchedule guestSchedule = GSFixture.createGuestScheduleA();
        guestSchedule = gsRepository.save(guestSchedule);  // GuestSchedule 저장

        Guest guest = GuestFixture.KIM.생성();
        tmpGuestRepository.save(guest);

        gsRepository.save(guestSchedule);

        List<Guest> result = tmpGuestRepository.findByGuestScheduleId(guestSchedule.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(guest);
    }
}
