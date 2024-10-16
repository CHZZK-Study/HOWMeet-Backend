package org.chzzk.howmeet.domain.regular.record.service;

import static org.chzzk.howmeet.domain.regular.record.exception.MSRecordErrorCode.DATE_INVALID_SELECT;
import static org.chzzk.howmeet.domain.regular.record.exception.MSRecordErrorCode.TIME_INVALID_SELECT;
import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.ROOM_MEMBER_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.room.exception.RoomErrorCode.ROOM_NOT_FOUND;
import static org.chzzk.howmeet.domain.regular.schedule.exception.MSErrorCode.SCHEDULE_NOT_FOUND;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.fcm.service.FcmService;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.record.exception.MSRecordException;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordNicknames;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordSelectionDetail;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.exception.RoomException;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.exception.MSException;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
import org.chzzk.howmeet.domain.temporary.record.exception.GSRecordErrorCode;
import org.chzzk.howmeet.domain.temporary.record.exception.GSRecordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MSRecordService {

    private final MemberRepository memberRepository;
    private final MSRecordRepository msRecordRepository;
    private final MSRepository msRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final FcmService fcmService;


    @Transactional
    public void postMSRecord(final MSRecordPostRequest msRecordPostRequest, final AuthPrincipal authPrincipal) {
        MemberSchedule ms = findMSByMSId(msRecordPostRequest.msId());
        if (!roomMemberRepository.existsByRoomIdAndMemberId(ms.getRoom().getId(), authPrincipal.id())) {
            throw new RoomException(ROOM_MEMBER_NOT_FOUND);
        }
        msRecordRepository.deleteByMemberScheduleIdAndMemberId(ms.getId(), authPrincipal.id());

        List<LocalDateTime> selectTimes = msRecordPostRequest.selectTime();
        List<MemberScheduleRecord> msRecords = convertSeletTimesToMSRecords(selectTimes, ms, authPrincipal.id());
        msRecordRepository.saveAll(msRecords);

        if(validateAllMemberMSRecord(ms)){
            final List<RoomMember> members = roomMemberRepository.findByRoomId(ms.getRoom().getId());

            fcmService.sendToLeader(members, ms);
        }
    }

    private List<MemberScheduleRecord> convertSeletTimesToMSRecords(final List<LocalDateTime> selectTimes,
            final MemberSchedule ms, final Long memberId) {

        final List<String> dates = ms.getDates();
        final LocalTime startTime = ms.getTime().getStartTime();
        final LocalTime endTime = ms.getTime().getEndTime();

        return selectTimes.stream().map(selectTime -> {
            validateSelectTime(selectTime, dates, startTime, endTime);
            return MemberScheduleRecord.of(memberId, ms, selectTime);
        }).collect(Collectors.toList());
    }

    private void validateSelectTime(final LocalDateTime selectTime, final List<String> dates, final LocalTime startTime,
            final LocalTime endTime) {

        LocalDate startDate = LocalDate.parse(dates.get(0));
        LocalDate endDate = LocalDate.parse(dates.get(1));

        final LocalDate selectDate = selectTime.toLocalDate();
        final LocalTime selectHour = selectTime.toLocalTime();

        if (selectDate.isBefore(startDate) || selectDate.isAfter(endDate)) {
            throw new GSRecordException(GSRecordErrorCode.DATE_INVALID_SELECT);
        }

        if (startTime.isBefore(endTime)) { // 같은 날 처리
            if (selectHour.isBefore(startTime) || selectHour.isAfter(endTime.minusMinutes(30))) {
                throw new GSRecordException(GSRecordErrorCode.TIME_INVALID_SELECT);
            }
        } else {
            if(selectHour.isAfter(startTime.minusMinutes(30))) {return;}
            if(selectHour.equals(LocalTime.MIDNIGHT) || (selectHour.isAfter(LocalTime.MIDNIGHT) && selectHour.isBefore(endTime))) {return;}
            throw new GSRecordException(GSRecordErrorCode.TIME_INVALID_SELECT);
        }
    }

    private boolean validateAllMemberMSRecord(MemberSchedule ms) {
        final List<MemberScheduleRecord> msRecords = findMSRecordByMSId(ms.getId());
        final List<RoomMember> members = roomMemberRepository.findByRoomId(ms.getRoom().getId());

        final Set<Long> msRecordMemberIdSet = msRecords.stream()
                .map(MemberScheduleRecord::getMemberId)
                .collect(Collectors.toSet());

        return members.stream()
                .map(RoomMember::getMemberId)
                .allMatch(msRecordMemberIdSet::contains);
    }

    private MemberSchedule findMSByMSId(final Long msId) {
        return msRepository.findById(msId).orElseThrow(() -> new MSException(SCHEDULE_NOT_FOUND));
    }

    public MSRecordGetResponse getMSRecord(final Long roomId, final Long msId) {

        List<Member> memberList = findMemberByRoomId(roomId);
        Room room = findRoomByRoomId(roomId);
        Map<Long, Nickname> nickNameMap = memberList.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

        List<MemberScheduleRecord> msRecords = findMSRecordByMSId(msId);

        Nicknames allNickname = MSRecordNicknames.convertNicknameProvidersList(memberList);

        Nicknames participants = MSRecordNicknames.distinctNicknames(msRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = MSRecordSelectionDetail.convertMapToSelectionDetail(msRecords,
                nickNameMap);

        return MSRecordGetResponse.of(msId, room.getName(), allNickname, participants,
                selectedInfoList);
    }

    private Room findRoomByRoomId(final Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
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

    private List<MemberScheduleRecord> findMSRecordByMSId(final Long msId) {
        List<MemberScheduleRecord> msRecords = msRecordRepository.findByMemberScheduleId(msId);
        if (msRecords == null) {
            return Collections.emptyList();
        }
        return msRecords;
    }
}
