package org.chzzk.howmeet.domain.regular.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.dto.MemberScheduleRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MemberScheduleResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MemberScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberScheduleService {
    private final MemberScheduleRepository memberScheduleRepository;

    @Transactional
    public MemberScheduleResponse createMemberSchedule(MemberScheduleRequest memberScheduleRequest) {
        MemberSchedule memberSchedule = MemberSchedule.of(memberScheduleRequest.date(), memberScheduleRequest.name(), memberScheduleRequest.room());
        MemberSchedule savedSchedule = memberScheduleRepository.save(memberSchedule);
        String inviteLink = "http://localhost:8080/guest-schedule/invite/" + savedSchedule.getId();
        return MemberScheduleResponse.of(savedSchedule, inviteLink);
    }

    public MemberScheduleResponse getMemberSchedule(Long memberScheduleId) {
        MemberSchedule memberSchedule = memberScheduleRepository.findById(memberScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        String inviteLink = "http://localhost:8080/guest-schedule/invite/" + memberSchedule.getId();
        return MemberScheduleResponse.of(memberSchedule, inviteLink);
    }

    @Transactional
    public void deleteMemberSchedule(Long memberScheduleId) {
        memberScheduleRepository.deleteById(memberScheduleId);
    }
}
