package org.chzzk.howmeet.domain.temporary.guest.repository;

import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long>, GuestQueryDSL {
    Optional<Guest> findByGuestScheduleIdAndNickname(@Param("guestScheduleId") final Long guestScheduleId, @Param("nickname") final Nickname nickname);
}
