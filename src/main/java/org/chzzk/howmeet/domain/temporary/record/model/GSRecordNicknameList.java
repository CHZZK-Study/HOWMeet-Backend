package org.chzzk.howmeet.domain.temporary.record.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.NicknameList;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;

public class GSRecordNicknameList extends NicknameList {
    public static NicknameList convertMapToNickNameList(List<GuestScheduleRecord> gsRecords, Map<Long, Nickname> nickNameMap){
        NicknameList nicknameList = new NicknameList();
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
