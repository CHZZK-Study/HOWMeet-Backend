package org.chzzk.howmeet.domain.regular.record.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.fixture.MSFixture;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.chzzk.howmeet.global.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class MSRecordRepositoryTest {

    @Autowired
    MSRecordRepository msRecordRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("MemberScheduleID와 MemberId로 MemberSheduleRecord 삭제하기")
    public void deleteByMemberScheduleIdAndMemberId() {

        Member member = MemberFixture.KIM.생성();
        MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());

        memberRepository.save(member);

        MemberScheduleRecord msRecord = MemberScheduleRecord.of(member.getId(), memberSchedule, LocalDateTime.now());
        msRecordRepository.save(msRecord);
        msRecordRepository.deleteByMemberScheduleIdAndMemberId(msRecord.getMemberScheduleId(), msRecord.getMemberId());

        MemberScheduleRecord deletedRecord = msRecordRepository.findById(msRecord.getId()).orElse(null);
        assertThat(deletedRecord).isNull();
    }

    @Test
    @DisplayName("MemberScheduleId로 MemberScheduleRecord 찾기")
    public void findByMemberScheduleId() {

        Member member = MemberFixture.KIM.생성();
        MemberSchedule memberSchedule = MSFixture.createMemberScheduleA(RoomFixture.createRoomA());

        memberRepository.save(member);

        MemberScheduleRecord msRecord = MemberScheduleRecord.of(member.getId(), memberSchedule, LocalDateTime.now());
        msRecordRepository.save(msRecord);

        List<MemberScheduleRecord> records = msRecordRepository.findByMemberScheduleId(1L);
        assertThat(records.get(0).getMemberScheduleId()).isEqualTo(1L);
    }
}
