package org.chzzk.howmeet.domain.regular.record.dto.get;

import java.util.List;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;

public record MSRecordGetResponse(Long msId, RoomName roomName, NicknameList totalPersonnel,
                                  NicknameList participatedPersonnel, List<SelectionDetail> selectTime) {

    public static MSRecordGetResponse of(final Long msId, final RoomName roomName, final NicknameList totalPersonnel,
            final NicknameList participatedPersonnel, final List<SelectionDetail> selectTime) {
        return new MSRecordGetResponse(msId, roomName, totalPersonnel, participatedPersonnel, selectTime);
    }
}
