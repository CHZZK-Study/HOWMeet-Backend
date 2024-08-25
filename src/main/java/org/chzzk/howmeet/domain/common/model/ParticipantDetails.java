package org.chzzk.howmeet.domain.common.model;

import lombok.Getter;

@Getter
public class ParticipantDetails {

    private final int count;
    private final NicknameList nicknames;

    public static ParticipantDetails of(final NicknameList nicknames) {
        return new ParticipantDetails(nicknames);
    }

    private ParticipantDetails(final NicknameList nicknames) {
        this.nicknames = nicknames;
        this.count = nicknames.size();
    }
}
