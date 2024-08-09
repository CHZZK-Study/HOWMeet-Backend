package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;

public enum RoomMemberFixture {
    MEMBER_1(1L, true),
    MEMBER_2(2L, false),
    MEMBER_3(3L, false);

    private final Long memberId;
    private final Boolean isLeader;

    RoomMemberFixture(final Long memberId, final Boolean isLeader) {
        this.memberId = memberId;
        this.isLeader = isLeader;
    }

    public RoomMember create(Room room) {
        return RoomMember.of(memberId, room, isLeader);
    }

}
