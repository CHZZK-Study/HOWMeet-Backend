package org.chzzk.howmeet.domain.temporary.guest.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.domain.temporary.guest.repository.GuestRepository;
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
