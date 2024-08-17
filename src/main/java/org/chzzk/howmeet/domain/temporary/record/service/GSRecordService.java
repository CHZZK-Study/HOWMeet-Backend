package org.chzzk.howmeet.domain.temporary.record.service;

import static org.chzzk.howmeet.domain.temporary.record.exception.GSRecordErrorCode.GS_NOT_FOUND;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.record.dto.get.response.GSRecordGetResponse;
import org.chzzk.howmeet.domain.temporary.record.dto.post.request.GSRecordPostRequest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.record.exception.GSRecordException;
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

    public void postGSRecord(final GSRecordPostRequest gsRecordPostRequest, final Guest guest){
        List<LocalDateTime> selectTimes = gsRecordPostRequest.selectTime();
        GuestSchedule gs = validateGS(gsRecordPostRequest.gsId());
        GuestScheduleRecord gsRecord;

        for (LocalDateTime selectTime : selectTimes) {
            gsRecord = GuestScheduleRecord.of(guest, gs, selectTime);
            gsRecordRepository.save(gsRecord);
        }
    }

    public GSRecordGetResponse getGSRecord(Long gsId){

        GuestSchedule gs = validateGS(gsId);
        List<Guest> guestList = tmpGuestRepository.findByGuestScheduleId(gsId);
        Map<Long, Nickname> nickNameMap = guestList.stream().collect(Collectors.toMap(Guest::getId, Guest::getNickname));

        List<GuestScheduleRecord> gsRecords = validateGSRecord(gsId);

        NicknameList allNickname = GSRecordNicknameList.convertNicknameProvidersList(guestList);


        NicknameList participants = GSRecordNicknameList.convertMapToNickNameList(gsRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = GSRecordSelectionDetail.convertMapToSelectionDetail(gsRecords, nickNameMap);

        return new GSRecordGetResponse(gsId, allNickname, participants, selectedInfoList);
    };

    private List<GuestScheduleRecord> validateGSRecord(Long id){
        List<GuestScheduleRecord> gsRecords = gsRecordRepository.findByGsId(id);
        if(gsRecords == null) throw new GSRecordException(GS_NOT_FOUND);
        return gsRecords;
    };

    private GuestSchedule validateGS(Long id) throws GSRecordException {
        return tmpGSRepository.findById(id).orElseThrow(() -> new GSRecordException(GS_NOT_FOUND));
    };
}
