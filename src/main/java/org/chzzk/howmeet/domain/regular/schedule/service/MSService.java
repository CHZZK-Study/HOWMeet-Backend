package org.chzzk.howmeet.domain.regular.schedule.service;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.exception.MSException;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.ROOM_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MSService {
    private final MSRepository msRepository;
    private final RoomRepository roomRepository;
    //private final InviteUrlProvider inviteUrlProvider;

    @Transactional
    public MSResponse createMemberSchedule(final MSRequest msRequest) {
        Room room = roomRepository.findById(msRequest.roomId())
                .orElseThrow(() -> new MSException(ROOM_NOT_FOUND));

        MemberSchedule memberSchedule = msRequest.toEntity(room);

        MemberSchedule savedSchedule = msRepository.save(memberSchedule);

        // String inviteLink = inviteUrlProvider.generateInviteUrl("member-schedule", savedSchedule.getId());

        return MSResponse.from(savedSchedule);
    }

    public MSResponse getMemberSchedule(final Long memberScheduleId) {
        MemberSchedule memberSchedule = findMemberScheduleById(memberScheduleId);
        // String inviteLink = inviteUrlProvider.generateInviteUrl("member-schedule", memberScheduleId);

        return MSResponse.from(memberSchedule);
    }

    @Transactional
    public void deleteMemberSchedule(final Long memberScheduleId) {
        MemberSchedule memberSchedule = findMemberScheduleById(memberScheduleId);
        msRepository.delete(memberSchedule);
    }

    private MemberSchedule findMemberScheduleById(Long memberScheduleId) {
        return msRepository.findById(memberScheduleId)
                .orElseThrow(() -> new MSException(SCHEDULE_NOT_FOUND));
    }
}
