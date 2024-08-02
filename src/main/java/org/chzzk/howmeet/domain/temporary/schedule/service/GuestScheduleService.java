package org.chzzk.howmeet.domain.temporary.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GuestScheduleRequest;
import org.chzzk.howmeet.domain.temporary.schedule.dto.GuestScheduleResponse;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GuestScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestScheduleService {
    private final GuestScheduleRepository guestScheduleRepository;

    @Transactional
    public GuestScheduleResponse createGuestSchedule(GuestScheduleRequest guestScheduleRequest) {
        GuestSchedule guestSchedule = GuestSchedule.of(guestScheduleRequest.date(), guestScheduleRequest.name());
        GuestSchedule savedSchedule = guestScheduleRepository.save(guestSchedule);
        String inviteLink = "http://localhost:8080/guest-schedule/invite/" + savedSchedule.getId();
        return GuestScheduleResponse.of(savedSchedule, inviteLink);
    }

    public GuestScheduleResponse getGuestSchedule(Long guestScheduleId) {
        GuestSchedule guestSchedule = guestScheduleRepository.findById(guestScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        String inviteLink = "http://localhost:8080/guest-schedule/invite/" + guestSchedule.getId();
        return GuestScheduleResponse.of(guestSchedule, inviteLink);
    }

    @Transactional
    public void deleteGuestSchedule(Long guestScheduleId) {
        guestScheduleRepository.deleteById(guestScheduleId);
    }
}
