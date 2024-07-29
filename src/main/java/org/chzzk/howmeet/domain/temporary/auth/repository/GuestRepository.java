package org.chzzk.howmeet.domain.temporary.auth.repository;

import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByGuestScheduleIdAndNickname(@Param("guestScheduleId") final Long guestScheduleId, @Param("nickname") final Nickname nickname);

    // todo 7/24 (수) 김민우 : QueryDSL로 구현해야함 (JPQL에서 select exists 문법을 제공하지 않으므로)
    @Query(
            nativeQuery = true,
            value = "SELECT CASE WHEN EXISTS(SELECT * FROM guest g WHERE g.guest_schedule_id =:#{#guestScheduleId} AND g.nickname =:#{#nickname.value}) THEN 'true' ELSE 'false' END"
    )
    boolean existsByGuestScheduleIdAndNickname(@Param("guestScheduleId") final Long guestScheduleId, @Param("nickname") final Nickname nickname);
}
