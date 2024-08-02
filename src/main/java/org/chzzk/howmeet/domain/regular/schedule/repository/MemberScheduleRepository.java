package org.chzzk.howmeet.domain.regular.schedule.repository;

import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
}