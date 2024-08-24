package org.chzzk.howmeet.domain.temporary.record.dto.get.response;

import java.util.List;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;

public record GSRecordGetResponse(Long gsId, NicknameList totalPersonnel, NicknameList participatedPersonnel,
                                  List<SelectionDetail> selectTime) {

    public static GSRecordGetResponse of(final Long gsId, final NicknameList totalPersonnel,
            final NicknameList participatedPersonnel, final List<SelectionDetail> selectTime) {
        return new GSRecordGetResponse(gsId, totalPersonnel, participatedPersonnel, selectTime);
    }
}
