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
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.chzzk.howmeet.domain.regular.auth.repository.MemberRepository;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetRequest;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetResponse;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordNicknameList;
import org.chzzk.howmeet.domain.regular.record.model.MSRecordSelectionDetail;
import org.chzzk.howmeet.domain.regular.record.repository.MSRecordRepository;
import org.chzzk.howmeet.domain.regular.record.repository.TmpMSRepository;
import org.chzzk.howmeet.domain.regular.record.repository.TmpMemberRepository;
import org.chzzk.howmeet.domain.regular.record.repository.TmpRoomMemberRepository;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MSRecordService {

    final MemberRepository memberRepository;
    final MSRecordRepository msRecordRepository;
    final TmpMSRepository tmpMSRepository;
    final TmpMemberRepository tmpMemberRepository;
    final TmpRoomMemberRepository tmpRoomMemberRepository;

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

        LocalDateTime startTime = ms.getDate().getStartDate();
        LocalDateTime endTime = ms.getDate().getEndDate();

        List<MemberScheduleRecord> msRecords = selectTimes.stream().map(selectTime -> {
            validateSelectTime(selectTime, startTime, endTime);
            return MemberScheduleRecord.of(member, ms, selectTime);
        }).collect(Collectors.toList());
        return msRecords;
    }

    private void validateSelectTime(final LocalDateTime selectTime, final LocalDateTime startTime,
            final LocalDateTime endTime) {
        LocalDate selectDate = selectTime.toLocalDate();
        LocalTime selectHour = selectTime.toLocalTime();

        if (selectDate.isBefore(startTime.toLocalDate()) || selectDate.isAfter(endTime.toLocalDate())) {
            throw new IllegalArgumentException("유효하지 않은 시간을 선택하셨습니다.");
        }

        if (selectHour.isBefore(startTime.toLocalTime()) || selectHour.isAfter(
                endTime.toLocalTime().minusMinutes(30))) {
            throw new IllegalArgumentException("유효하지 않은 시간을 선택하셨습니다.");
        }
    }

    private MemberSchedule findMSByMSId(final Long msId) {
        return tmpMSRepository.findById(msId).orElseThrow(() -> new IllegalArgumentException("일치하는 일정을 찾을 수 없습니다."));
    }

    private Member findMemberByMemberId(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 id를 찾을 수 없습니다."));
    }

    public MSRecordGetResponse getMSRecord(final MSRecordGetRequest msRecordGetRequest,
            final AuthPrincipal authPrincipal) {
        checkLeaderAuthority(authPrincipal.id());

        // note: 여기 수정 필요!!
        List<Member> memberList = tmpMemberRepository.findByMemberScheduleId(msRecordGetRequest.msId());
        Map<Long, Nickname> nickNameMap = memberList.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

        List<MemberScheduleRecord> msRecords = findMSRecordByMSId(msRecordGetRequest.msId());

        NicknameList allNickname = MSRecordNicknameList.convertNicknameProvidersList(memberList);

        NicknameList participants = MSRecordNicknameList.convertMapToNickNameList(msRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = MSRecordSelectionDetail.convertMapToSelectionDetail(msRecords,
                nickNameMap);

        return MSRecordGetResponse.of(msRecordGetRequest.msId(), allNickname, participants, selectedInfoList);
    }

    private void checkLeaderAuthority(final Long memberId) {
        Member member = findMemberByMemberId(memberId);
        RoomMember roomMember = tmpRoomMemberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 방회원 정보를 찾을 수 없습니다."));
        if (!roomMember.getIsLeader()) {
            throw new IllegalArgumentException("방장 권한이 없어 해당 일정 내역을 조회할 수 없습니다.");
        }
    }

//    //방 id가지고 있는 회원을 찾아야 됨, 방 참가자테이블에서 roomid를 가지고 참가자 Id리스트(roommemberlist)를 찾고, 이걸 멤버리스트로 변경
//    private List<Member> findMemberByRoomId(final Long roomId) {
//        List<Member> memberList = new ArrayList<>();
//        List<RoomMember> roomMembers = tmpRoomMemberRepository.findByRoomId(roomId);
//        if (roomMembers == null) {
//            return Collections.emptyList();
//        }
//
//        tmpMemberRepository.findByMemberScheduleId(msRecordGetRequest.msId());
//
//    }

    private List<MemberScheduleRecord> findMSRecordByMSId(final Long msId) {
        List<MemberScheduleRecord> msRecords = msRecordRepository.findByMemberScheduleId(msId);
        if (msRecords == null) {
            return Collections.emptyList();
        }
        return msRecords;
    }
}
