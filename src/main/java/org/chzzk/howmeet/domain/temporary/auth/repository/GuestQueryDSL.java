package org.chzzk.howmeet.domain.temporary.auth.repository;

import org.chzzk.howmeet.domain.common.model.Nickname;
import org.springframework.data.repository.query.Param;

public interface GuestQueryDSL {
    boolean existsByGuestScheduleIdAndNickname(@Param("guestScheduleId") final Long guestScheduleId, @Param("nickname") final Nickname nickname);
}
