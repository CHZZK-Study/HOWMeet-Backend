package org.chzzk.howmeet.domain.temporary.record.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.record.dto.get.response.GSRecordGetResponse;
import org.chzzk.howmeet.domain.temporary.record.dto.post.request.GSRecordPostRequest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.record.model.GSRecordNicknameList;
import org.chzzk.howmeet.domain.temporary.record.model.GSRecordSelectionDetail;
import org.chzzk.howmeet.domain.temporary.record.repository.GSRecordRepository;
import org.chzzk.howmeet.domain.temporary.record.repository.TmpGSRepository;
import org.chzzk.howmeet.domain.temporary.record.repository.TmpGuestRepository;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GSRecordService {

    private final TmpGSRepository tmpGSRepository;
    private final TmpGuestRepository tmpGuestRepository;
    private final GSRecordRepository gsRecordRepository;

    public void postGSRecord(final GSRecordPostRequest gsRecordPostRequest, final AuthPrincipal authPrincipal) {
        Guest guest = findGuestByGuestId(authPrincipal.id());
        List<LocalDateTime> selectTimes = gsRecordPostRequest.selectTime();
        GuestSchedule gs = findGSByGSId(gsRecordPostRequest.gsId());

        GuestScheduleRecord gsRecord;

        for (LocalDateTime selectTime : selectTimes) {
            gsRecord = GuestScheduleRecord.of(guest, gs, selectTime);
            gsRecordRepository.save(gsRecord);
        }
    }

    public GSRecordGetResponse getGSRecord(Long gsId){

        GuestSchedule gs = findGSByGSId(gsId);
        List<Guest> guestList = tmpGuestRepository.findByGuestScheduleId(gsId); //note
        Map<Long, Nickname> nickNameMap = guestList.stream()
                .collect(Collectors.toMap(Guest::getId, Guest::getNickname));

        List<GuestScheduleRecord> gsRecords = findGSRecordByGSId(gsId);

        NicknameList allNickname = GSRecordNicknameList.convertNicknameProvidersList(guestList);


        NicknameList participants = GSRecordNicknameList.convertMapToNickNameList(gsRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = GSRecordSelectionDetail.convertMapToSelectionDetail(gsRecords, nickNameMap);

        return new GSRecordGetResponse(gsId, allNickname, participants, selectedInfoList);
    };

    // comment: gsId를 이용하여 GSRecord리스트 찾는 메소드
    private List<GuestScheduleRecord> findGSRecordByGSId(final Long gsId) {
        List<GuestScheduleRecord> gsRecords = gsRecordRepository.findByGuestScheduleId(gsId);
        if (gsRecords == null) {
            throw new IllegalArgumentException();
        }
        return gsRecords;
    }

    // comment: guestId를 이용하여 gsRecord 찾는 메소드
    private List<GuestScheduleRecord> findByGSRecordByGuestId(final Long guestId) {
        List<GuestScheduleRecord> gsRecords = gsRecordRepository.findByGuestId(guestId);
        if (gsRecords == null) {
            throw new IllegalArgumentException();
        } else {
            return gsRecords;
        }
    }

    private GuestSchedule findGSByGSId(final Long gsId) {
        return tmpGSRepository.findById(gsId).orElseThrow(() -> new IllegalArgumentException());
    }

    private Guest findGuestByGuestId(final Long guestId) {
        return tmpGuestRepository.findById(guestId).orElseThrow(() -> new IllegalArgumentException());
    }
}
