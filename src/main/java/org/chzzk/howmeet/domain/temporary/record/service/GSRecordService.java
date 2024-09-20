package org.chzzk.howmeet.domain.temporary.record.service;

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
import org.chzzk.howmeet.domain.temporary.guest.entity.Guest;
import org.chzzk.howmeet.domain.temporary.guest.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.record.dto.get.response.GSRecordGetResponse;
import org.chzzk.howmeet.domain.temporary.record.dto.post.request.GSRecordPostRequest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.record.model.GSRecordNicknames;
import org.chzzk.howmeet.domain.temporary.record.model.GSRecordSelectionDetail;
import org.chzzk.howmeet.domain.temporary.record.repository.GSRecordRepository;
import org.chzzk.howmeet.domain.temporary.record.repository.TmpGuestRepository;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.repository.GSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GSRecordService {

    private final TmpGuestRepository tmpGuestRepository;
    private final GSRecordRepository gsRecordRepository;
    private final GSRepository gsRepository;
    private final GuestRepository guestRepository;

    @Transactional
    public void postGSRecord(final GSRecordPostRequest gsRecordPostRequest, final AuthPrincipal authPrincipal) {
        Guest guest = findGuestByGuestId(authPrincipal.id());
        gsRecordRepository.deleteByGuestId(guest.getId());

        List<LocalDateTime> selectTimes = gsRecordPostRequest.selectTime();
        GuestSchedule gs = findGSByGSId(gsRecordPostRequest.gsId());

        List<GuestScheduleRecord> gsRecords = convertSeletTimesToGSRecords(selectTimes, gs, guest);
        gsRecordRepository.saveAll(gsRecords);
    }

    private List<GuestScheduleRecord> convertSeletTimesToGSRecords(final List<LocalDateTime> selectTimes,
            final GuestSchedule gs, final Guest guest) {

        List<String> dates = gs.getDates();
        LocalTime startTime = gs.getTime().getStartTime();
        LocalTime endTime = gs.getTime().getEndTime();

        final List<GuestScheduleRecord> gsRecords = selectTimes.stream().map(selectTime -> {
            validateSelectTime(selectTime, dates, startTime, endTime);
            return GuestScheduleRecord.of(guest, gs, selectTime);
        }).collect(Collectors.toList());
        return gsRecords;
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

    public GSRecordGetResponse getGSRecord(final Long gsId) {
        List<Guest> guestList = tmpGuestRepository.findByGuestScheduleId(gsId);
        Map<Long, Nickname> nickNameMap = guestList.stream()
                .collect(Collectors.toMap(Guest::getId, Guest::getNickname));

        List<GuestScheduleRecord> gsRecords = findGSRecordByGSId(gsId);

        Nicknames allNickname = GSRecordNicknames.convertNicknameProvidersList(guestList);

        Nicknames participants = GSRecordNicknames.distinctNicknames(gsRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = GSRecordSelectionDetail.convertMapToSelectionDetail(gsRecords,
                nickNameMap);

        return GSRecordGetResponse.of(gsId, allNickname, participants, selectedInfoList);
    }


    private List<GuestScheduleRecord> findGSRecordByGSId(final Long gsId) {
        final List<GuestScheduleRecord> gsRecords = gsRecordRepository.findByGuestScheduleId(gsId);
        if (gsRecords == null) {
            return Collections.emptyList();
        }
        return gsRecords;
    }

    private GuestSchedule findGSByGSId(final Long gsId) {
        return gsRepository.findById(gsId).orElseThrow(() -> new IllegalArgumentException("일치하는 일정을 찾을 수 없습니다."));
    }

    private Guest findGuestByGuestId(final Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 비회원 id를 찾을 수 없습니다."));
    }
}
