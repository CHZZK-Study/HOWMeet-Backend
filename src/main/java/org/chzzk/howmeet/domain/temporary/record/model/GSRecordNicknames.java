package org.chzzk.howmeet.domain.temporary.record.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;

@NoArgsConstructor
public class GSRecordNicknames extends Nicknames {

    public static GSRecordNicknames create() {
        return new GSRecordNicknames();
    }

    public static GSRecordNicknames distinctNicknames(final List<GuestScheduleRecord> gsRecords,
            final Map<Long, Nickname> nickNameMap) {
        GSRecordNicknames nicknameList = create();
        Set<Nickname> nickNameSet = new HashSet<>();

        Nickname nickname;
        for (GuestScheduleRecord gsRecord : gsRecords) {
            nickname = nickNameMap.get(gsRecord.getGuestId());
            nickNameSet.add(nickname);
        }
        nicknameList.addFromNicknameSet(nickNameSet);

        return nicknameList;
    }
}
