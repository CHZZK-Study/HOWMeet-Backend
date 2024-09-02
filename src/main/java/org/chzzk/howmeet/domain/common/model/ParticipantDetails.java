package org.chzzk.howmeet.domain.common.model;

import lombok.Getter;

@Getter
public class ParticipantDetails {

    private final int count;
    private final Nicknames nicknames;

    public static ParticipantDetails of(final Nicknames nicknames) {
        return new ParticipantDetails(nicknames);
    }

    private ParticipantDetails(final Nicknames nicknames) {
        this.nicknames = nicknames;
        this.count = nicknames.size();
    }
}
