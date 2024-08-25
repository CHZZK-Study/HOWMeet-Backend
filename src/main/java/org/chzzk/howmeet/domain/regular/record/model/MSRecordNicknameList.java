package org.chzzk.howmeet.domain.regular.record.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;

@NoArgsConstructor
public class MSRecordNicknameList extends NicknameList {

    public static MSRecordNicknameList create() {
        return new MSRecordNicknameList();
    }

    public static NicknameList convertMapToNickNameList(final List<MemberScheduleRecord> msRecords,
            final Map<Long, Nickname> nickNameMap) {
        NicknameList nicknameList = create();
        HashSet<Nickname> nickNameSet = new HashSet<>();

        Nickname nickname;
        for (MemberScheduleRecord msRecord : msRecords) {
            nickname = nickNameMap.get(msRecord.getMemberId());
            nickNameSet.add(nickname);
        }
        nicknameList.addFromNicknameSet(nickNameSet);

        return nicknameList;
    }
}
