package org.chzzk.howmeet.domain.temporary.record.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;

@NoArgsConstructor
public class GSRecordNicknameList extends NicknameList {

    public static GSRecordNicknameList create() {
        return new GSRecordNicknameList();
    }

    public static GSRecordNicknameList convertMapToNickNameList(final List<GuestScheduleRecord> gsRecords,
            final Map<Long, Nickname> nickNameMap) {
        GSRecordNicknameList nicknameList = create();
        HashSet<Nickname> nickNameSet = new HashSet<>();

        Nickname nickname;
        for (GuestScheduleRecord gsRecord : gsRecords) {
            nickname = nickNameMap.get(gsRecord.getGuestId());
            nickNameSet.add(nickname);
        }
        nicknameList.addFromNicknameSet(nickNameSet);

        return nicknameList;
    }
}
