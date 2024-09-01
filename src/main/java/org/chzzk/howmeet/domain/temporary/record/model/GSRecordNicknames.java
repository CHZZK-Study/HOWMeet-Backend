package org.chzzk.howmeet.domain.temporary.record.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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
            final Map<Long, Nickname> nicknamesById) {

        GSRecordNicknames nicknames = create();
        final Set<Nickname> nickNameSet = gsRecords.stream()
                .map(GuestScheduleRecord::getGuestId)
                .map(nicknamesById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        nicknames.addAll(nickNameSet);

        return nicknames;
    }
}
