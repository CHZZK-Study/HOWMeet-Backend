package org.chzzk.howmeet.domain.regular.confirm.service;

import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.ROOM_MEMBER_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.SCHEDULE_NOT_FOUND;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.confirm.dto.SelectTimeCount;
import org.chzzk.howmeet.domain.regular.fcm.service.FcmService;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleResponse;
import org.chzzk.howmeet.domain.regular.confirm.dto.ConfirmScheduleRequest;
import org.chzzk.howmeet.domain.regular.confirm.entity.ConfirmSchedule;
import org.chzzk.howmeet.domain.regular.confirm.repository.ConfirmRepository;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordNicknames;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordSelectionDetail;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.exception.MSException;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ConfirmService {

    private final MSRepository msRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final ConfirmRepository confirmRepository;
    private final MSRecordRepository msRecordRepository;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    @Transactional
    public Long postConfirmSchedule(final Long msId, final ConfirmScheduleRequest confirmSchedulePostRequest, final AuthPrincipal authPrincipal) {
        MemberSchedule ms = findMSByMSId(msId);
        checkLeaderAuthority(authPrincipal.id(), ms.getRoom().getId());

        if(ms.isComplete()) throw new ConfirmException(SCHEDULE_ALREADY_CONFIRMED);
        ms.complete();

        ConfirmSchedule confirmSchedule = confirmSchedulePostRequest.toEntity(ms);
        confirmRepository.save(confirmSchedule);

        List<RoomMember> members = ms.getRoom().getMembers();
        fcmService.sendToRoomMember(members, ms);
        return confirmSchedule.getId();
    }

    private void checkLeaderAuthority(final Long memberId, final Long roomId) {
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        RoomMember loginMember = roomMembers.stream().filter(roomMember -> roomMember.getMemberId().equals(memberId))
                .findFirst().orElseThrow(() -> new RoomException(ROOM_MEMBER_NOT_FOUND));

        if (!loginMember.getIsLeader()) {
            throw new IllegalArgumentException("방장이 아니므로 일정을 확정할 수 없습니다.");
        }
    }
    private MemberSchedule findMSByMSId(final Long msId) {
        return msRepository.findById(msId).orElseThrow(() -> new MSException(SCHEDULE_NOT_FOUND));
    }

    public ConfirmScheduleResponse getConfirmSchedule(final Long roomId, final Long msId){
        ConfirmSchedule confirmSchedule = findConfirmScheduleByMsId(msId);

        List<Member> members = findMemberByRoomId(roomId);
        Map<Long, Nickname> nickNameMap = members.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));
        List<MemberScheduleRecord> msRecords = findMSRecordByMSId(msId);

        Nicknames allNickname = MSRecordNicknames.convertNicknameProvidersList(members);
        List<SelectionDetail> selectedInfoList = MSRecordSelectionDetail.convertMapToSelectionDetail(msRecords,
                nickNameMap);

        return ConfirmScheduleResponse.of(confirmSchedule, allNickname, selectedInfoList);
    }

    private List<Member> findMemberByRoomId(final Long roomId) {
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        if (roomMembers == null) {
            return Collections.emptyList();
        }

        return roomMembers.stream().map(roomMember -> memberRepository.findById(roomMember.getMemberId())
                        .orElseThrow(() -> new RoomException(ROOM_MEMBER_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    private List<MemberScheduleRecord> findMSRecordByMSId(final Long msId){
        List<MemberScheduleRecord> msRecords = msRecordRepository.findByMemberScheduleId(msId);
        if(msRecords == null) {
            return Collections.emptyList();
        }
        return msRecords;
    }

    private ConfirmSchedule findConfirmScheduleByMsId(final Long msId){
        return confirmRepository.findByMsId(msId).orElseThrow(() -> new IllegalArgumentException("일치하는 일정 결과를 찾을 수 없습니다."));
    }
}
