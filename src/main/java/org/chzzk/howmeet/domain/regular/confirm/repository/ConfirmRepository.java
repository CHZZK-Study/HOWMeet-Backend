package org.chzzk.howmeet.domain.regular.confirm.repository;

import java.util.Optional;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ConfirmRepository extends JpaRepository<ConfirmSchedule, Long> {
    Optional<ConfirmSchedule> findByMsId(@Param("memberScheduleId") final Long msId);
}
