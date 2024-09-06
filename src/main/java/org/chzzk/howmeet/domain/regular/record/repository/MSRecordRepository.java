package org.chzzk.howmeet.domain.regular.record.repository;

import java.util.List;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MSRecordRepository extends JpaRepository<MemberScheduleRecord, Long> {

    void deleteByMemberScheduleIdAndMemberId(@Param("memberScheduleId") final Long msId,
            @Param("memberId") final Long memberId);

    List<MemberScheduleRecord> findByMemberScheduleId(@Param("memberScheduleId") final Long msId);

}
