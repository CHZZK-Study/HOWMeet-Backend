package org.chzzk.howmeet.domain.regular.record.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.ParticipantDetails;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;

public class MSRecordSelectionDetail extends SelectionDetail {

    private MSRecordSelectionDetail(final LocalDateTime selectTime, final ParticipantDetails participantDetails) {
        super(selectTime, participantDetails);
    }

    public static List<SelectionDetail> convertMapToSelectionDetail(final List<MemberScheduleRecord> msRecords,
            final Map<Long, Nickname> nickNameMap) {
        HashMap<LocalDateTime, MSRecordNicknameList> selectTimeMap = new HashMap<>();

        Nickname nickname;
        LocalDateTime selectTime;
        for (MemberScheduleRecord msRecord : msRecords) {
            nickname = nickNameMap.get(msRecord.getMemberId());
            selectTime = msRecord.getSelectTime();

            selectTimeMap.computeIfAbsent(selectTime, k -> MSRecordNicknameList.create()).add(nickname);
        }
        return convertMapToSelectionDetailsList(selectTimeMap);
    }

}
