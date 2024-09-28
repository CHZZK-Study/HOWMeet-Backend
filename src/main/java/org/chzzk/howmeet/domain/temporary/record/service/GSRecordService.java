package org.chzzk.howmeet.domain.temporary.record.service;

import static org.chzzk.howmeet.domain.temporary.guest.exception.GuestErrorCode.GUEST_NOT_FOUND;
import static org.chzzk.howmeet.domain.temporary.record.exception.GSRecordErrorCode.DATE_INVALID_SELECT;
import static org.chzzk.howmeet.domain.temporary.record.exception.GSRecordErrorCode.TIME_INVALID_SELECT;
import static org.chzzk.howmeet.domain.temporary.schedule.exception.GSErrorCode.SCHEDULE_NOT_FOUND;

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
import org.chzzk.howmeet.domain.temporary.guest.exception.GuestException;
import org.chzzk.howmeet.domain.temporary.guest.repository.GuestRepository;
import org.chzzk.howmeet.domain.temporary.record.dto.get.response.GSRecordGetResponse;
import org.chzzk.howmeet.domain.temporary.record.dto.post.request.GSRecordPostRequest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;
import org.chzzk.howmeet.domain.temporary.record.exception.GSRecordException;
import org.chzzk.howmeet.domain.temporary.record.model.GSRecordNicknames;
import org.chzzk.howmeet.domain.temporary.record.model.GSRecordSelectionDetail;
import org.chzzk.howmeet.domain.temporary.record.repository.GSRecordRepository;
import org.chzzk.howmeet.domain.temporary.record.repository.TmpGuestRepository;
import org.chzzk.howmeet.domain.temporary.schedule.entity.GuestSchedule;
import org.chzzk.howmeet.domain.temporary.schedule.exception.GSException;
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

        LocalDate startDate = LocalDate.parse(dates.get(0));
        LocalDate endDate = LocalDate.parse(dates.get(1));

        LocalDate selectDate = selectTime.toLocalDate();
        LocalTime selectHour = selectTime.toLocalTime();

        if (selectDate.isBefore(startDate) || selectDate.isAfter(endDate)) {
            throw new GSRecordException(DATE_INVALID_SELECT);
        }

        if (startTime.isBefore(endTime)) { // 같은 날 처리
            if (selectHour.isBefore(startTime) || selectHour.isAfter(endTime.minusMinutes(30))) {
                throw new GSRecordException(TIME_INVALID_SELECT);
            }
        } else {
            if(selectHour.isAfter(startTime.minusMinutes(30))) {return;}
            if(selectHour.equals(LocalTime.MIDNIGHT) || (selectHour.isAfter(LocalTime.MIDNIGHT) && selectHour.isBefore(endTime))) {return;}
            throw new GSRecordException(TIME_INVALID_SELECT);
        }
    }

    public GSRecordGetResponse getGSRecord(final Long gsId) {
        GuestSchedule guestSchedule = findGSByGSId(gsId);
        List<Guest> guests = tmpGuestRepository.findByGuestScheduleId(guestSchedule.getId());

        Map<Long, Nickname> nickNameMap = guests.stream()
                .collect(Collectors.toMap(Guest::getId, Guest::getNickname));

        List<GuestScheduleRecord> gsRecords = findGSRecordByGSId(guestSchedule.getId());

        Nicknames allNickname = GSRecordNicknames.convertNicknameProvidersList(guests);

        Nicknames participants = GSRecordNicknames.distinctNicknames(gsRecords, nickNameMap);
        List<SelectionDetail> selectedInfoList = GSRecordSelectionDetail.convertMapToSelectionDetail(gsRecords,
                nickNameMap);

        return GSRecordGetResponse.of(guestSchedule.getId(), allNickname, participants, selectedInfoList);
    }


    private List<GuestScheduleRecord> findGSRecordByGSId(final Long gsId) {
        final List<GuestScheduleRecord> gsRecords = gsRecordRepository.findByGuestScheduleId(gsId);
        if (gsRecords.isEmpty()) {
            return Collections.emptyList();
        }
        return gsRecords;
    }

    private GuestSchedule findGSByGSId(final Long gsId) {
        return gsRepository.findById(gsId).orElseThrow(() -> new GSException(SCHEDULE_NOT_FOUND));
    }

    private Guest findGuestByGuestId(final Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestException(GUEST_NOT_FOUND));
    }
}
