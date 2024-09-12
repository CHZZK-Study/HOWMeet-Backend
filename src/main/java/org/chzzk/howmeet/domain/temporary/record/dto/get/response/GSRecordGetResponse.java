package org.chzzk.howmeet.domain.temporary.record.dto.get.response;

import java.util.List;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.common.model.SelectionDetail;

public record GSRecordGetResponse(Long gsId, Nicknames totalPersonnel, Nicknames participatedPersonnel,
                                  List<SelectionDetail> time) {

    public static GSRecordGetResponse of(final Long gsId, final Nicknames totalPersonnel,
            final Nicknames participatedPersonnel, final List<SelectionDetail> time) {
        return new GSRecordGetResponse(gsId, totalPersonnel, participatedPersonnel, time);
    }
}
