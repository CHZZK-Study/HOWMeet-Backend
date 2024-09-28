package org.chzzk.howmeet.domain.common.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
