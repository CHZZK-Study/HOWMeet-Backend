package org.chzzk.howmeet.domain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantDetails {
    private final int count;
    private final NicknameList nicknames;

    public ParticipantDetails(NicknameList nicknames){
        this.nicknames = nicknames;
        this.count = nicknames.size();
    }
}
