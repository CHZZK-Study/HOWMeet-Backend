package org.chzzk.howmeet.domain.regular.record.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.Nicknames;
import org.chzzk.howmeet.domain.regular.record.entity.MemberScheduleRecord;

@NoArgsConstructor
public class MSRecordNicknames extends Nicknames {

    public static MSRecordNicknames create() {
        return new MSRecordNicknames();
    }

    public static MSRecordNicknames distinctNicknames(final List<MemberScheduleRecord> msRecords,
            final Map<Long, Nickname> nicknamesById) {

        MSRecordNicknames nicknames = create();

        final Set<Nickname> nickNameSet = msRecords.stream()
                .map(MemberScheduleRecord::getMemberId)
                .map(nicknamesById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        nicknames.addAll(nickNameSet);
        return nicknames;
    }
}
