package org.chzzk.howmeet.domain.regular.record.dto.get;

import java.util.List;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;

public record MSRecordGetResponse(Long msId, RoomName roomName, Nicknames totalPersonnel,
                                  Nicknames participatedPersonnel, List<SelectionDetail> selectTime) {

    public static MSRecordGetResponse of(final Long msId, final RoomName roomName, final Nicknames totalPersonnel,
            final Nicknames participatedPersonnel, final List<SelectionDetail> selectTime) {
        return new MSRecordGetResponse(msId, roomName, totalPersonnel, participatedPersonnel, selectTime);
    }
}
