package org.chzzk.howmeet.domain.regular.record.repository;

import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpMSRepository extends JpaRepository<MemberSchedule, Long> {

}
