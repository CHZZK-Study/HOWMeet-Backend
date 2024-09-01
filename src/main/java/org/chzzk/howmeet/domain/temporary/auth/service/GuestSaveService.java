package org.chzzk.howmeet.domain.temporary.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestSaveService {
    private final GuestRepository guestRepository;
    private final GSRepository gsRepository;

    @Transactional
    public Guest save(final Long guestScheduleId, final String nickname, final EncodedPassword encodedPassword) {
        validateGuestScheduleId(guestScheduleId);
        return guestRepository.save(Guest.of(
                guestScheduleId,
                nickname,
                encodedPassword
        ));
    }

    private void validateGuestScheduleId(final Long guestScheduleId) {
        if (!gsRepository.existsByGuestScheduleId(guestScheduleId)) {
            throw new GSException(SCHEDULE_NOT_FOUND);
        }
    }
}
