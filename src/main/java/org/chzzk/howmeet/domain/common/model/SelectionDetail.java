package org.chzzk.howmeet.domain.common.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chzzk.howmeet.domain.regular.confirm.dto.SelectTimeCount;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;

@Getter
@AllArgsConstructor
public class SelectionDetail {

    private final LocalDateTime selectTime;
    private final ParticipantDetails participantDetails;

    public static SelectionDetail of(final LocalDateTime selectTime,
            final ParticipantDetails participantDetails) {
        return new SelectionDetail(selectTime, participantDetails);
    }

    public static List<SelectionDetail> convertMapToSelectionDetailsList(
            final Map<LocalDateTime, ? extends Nicknames> selectTimeMap) {
        List<SelectionDetail> selectTimeList = new ArrayList<>();

        for (Map.Entry<LocalDateTime, ? extends Nicknames> entry : selectTimeMap.entrySet()) {
            selectTimeList.add(of(entry.getKey(), ParticipantDetails.of(entry.getValue())));
        }
        return selectTimeList;
    }
    public static List<SelectTimeCount> convertToSelectionTimeCount(final List<MemberScheduleRecord> msRecords) {
        Map<LocalDateTime, Integer> selectTimeCountMap = new HashMap<>();

        for (MemberScheduleRecord record : msRecords) {
            LocalDateTime selectTime = record.getSelectTime();
            selectTimeCountMap.put(selectTime, selectTimeCountMap.getOrDefault(selectTime, 0) + 1);
        }

        return selectTimeCountMap.entrySet().stream()
                .map(entry -> SelectTimeCount.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
