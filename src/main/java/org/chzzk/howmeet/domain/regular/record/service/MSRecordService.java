package org.chzzk.howmeet.domain.regular.record.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.member.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetRequest;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordNicknames;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordSelectionDetail;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.repository.RoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.repository.RoomRepository;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.chzzk.howmeet.domain.regular.schedule.repository.MSRepository;
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


    @Transactional
    public void postMSRecord(final MSRecordPostRequest msRecordPostRequest, final AuthPrincipal authPrincipal) {
        Member member = findMemberByMemberId(authPrincipal.id());
        MemberSchedule ms = findMSByMSId(msRecordPostRequest.msId());

        msRecordRepository.deleteByMemberScheduleIdAndMemberId(ms.getId(), member.getId());

        List<LocalDateTime> selectTimes = msRecordPostRequest.selectTime();
        List<MemberScheduleRecord> msRecords = convertSeletTimesToMSRecords(selectTimes, ms, member);
        msRecordRepository.saveAll(msRecords);
    }

    private List<MemberScheduleRecord> convertSeletTimesToMSRecords(final List<LocalDateTime> selectTimes,
            final MemberSchedule ms, final Member member) {

        List<String> dates = ms.getDates();
        LocalTime startTime = ms.getTime().getStartTime();
        LocalTime endTime = ms.getTime().getEndTime();

        List<MemberScheduleRecord> msRecords = selectTimes.stream().map(selectTime -> {
            validateSelectTime(selectTime, dates, startTime, endTime);
            return MemberScheduleRecord.of(member, ms, selectTime);
        }).collect(Collectors.toList());
        return msRecords;
    }

    private void validateSelectTime(final LocalDateTime selectTime, final List<String> dates, final LocalTime startTime,
            final LocalTime endTime) {

        LocalDate selectDate = selectTime.toLocalDate();
        LocalTime selectHour = selectTime.toLocalTime();

        if (!dates.contains(selectDate.toString())) {
            throw new IllegalArgumentException("선택할 수 없는 날짜를 선택하셨습니다.");
        }

        if (selectHour.isBefore(startTime) || selectHour.isAfter(
                endTime.minusMinutes(30))) {
            throw new IllegalArgumentException("유효하지 않은 시간을 선택하셨습니다.");
        }
    }

    private MemberSchedule findMSByMSId(final Long msId) {
        return msRepository.findById(msId).orElseThrow(() -> new IllegalArgumentException("일치하는 일정을 찾을 수 없습니다."));
    }

    private Member findMemberByMemberId(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 id를 찾을 수 없습니다."));
    }

    public MSRecordGetResponse getMSRecord(final MSRecordGetRequest msRecordGetRequest,
            final AuthPrincipal authPrincipal) {
        checkLeaderAuthority(authPrincipal.id(), msRecordGetRequest.roomId());

        List<Member> memberList = findMemberByRoomId(msRecordGetRequest.roomId());
        Room room = findRoomByRoomId(msRecordGetRequest.roomId());
        Map<Long, Nickname> nickNameMap = memberList.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

        List<MemberScheduleRecord> msRecords = findMSRecordByMSId(msRecordGetRequest.msId());

        Nicknames allNickname = MSRecordNicknames.convertNicknameProvidersList(memberList);

        Nicknames participants = MSRecordNicknames.convertMapToNickNameList(msRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = MSRecordSelectionDetail.convertMapToSelectionDetail(msRecords,
                nickNameMap);

        return MSRecordGetResponse.of(msRecordGetRequest.msId(), room.getName(), allNickname, participants,
                selectedInfoList);
    }

    private Room findRoomByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 방 정보를 확인할 수 없습니다."));
        return room;
    }

    private void checkLeaderAuthority(final Long memberId, final Long roomId) {
        Member member = findMemberByMemberId(memberId);

        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        RoomMember loginMember = roomMembers.stream().filter(roomMember -> roomMember.getMemberId().equals(memberId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("일치하는 방회원 정보를 찾을 수 없습니다."));

        if (!loginMember.getIsLeader()) {
            throw new IllegalArgumentException("방장 권한이 없어 해당 일정 내역을 조회할 수 없습니다.");
        }
    }

    private List<Member> findMemberByRoomId(final Long roomId) {
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomId(roomId);
        if (roomMembers == null) {
            return Collections.emptyList();
        }

        return roomMembers.stream().map(roomMember -> memberRepository.findById(roomMember.getMemberId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원이 방에 포함되어 있습니다.")))
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
