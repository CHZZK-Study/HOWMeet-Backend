package org.chzzk.howmeet.domain.temporary.auth.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.auth.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestFindService {
    private final GuestRepository guestRepository;

    public Optional<Guest> find(final Long guestScheduleId, final String nickname) {
        return guestRepository.findByGuestScheduleIdAndNickname(guestScheduleId, Nickname.from(nickname));
    }
}
