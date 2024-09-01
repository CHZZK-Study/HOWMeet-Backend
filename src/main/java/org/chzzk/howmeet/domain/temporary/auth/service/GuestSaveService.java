package org.chzzk.howmeet.domain.temporary.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.EncodedPassword;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestSaveService {
    private final GuestRepository guestRepository;

    @Transactional
    public Guest save(final Long guestScheduleId, final String nickname, final EncodedPassword encodedPassword) {
        return guestRepository.save(Guest.of(
                guestScheduleId,
                nickname,
                encodedPassword
        ));
    }
}
