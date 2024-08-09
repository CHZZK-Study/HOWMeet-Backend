package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;

import java.util.Collections;

public enum RoomFixture {
    ROOM_1(
            RoomName.from("Conference Room"),
            RoomDescription.from("A room for conference meetings"),
            RoomMemberFixture.MEMBER_1,
            MSFixture.MEETING_A
    );

    private final RoomName name;
    private final RoomDescription description;
    private final RoomMemberFixture roomMemberFixture;

    private final MSFixture msFixture;

    RoomFixture(final RoomName name, final RoomDescription description, RoomMemberFixture roomMemberFixture, MSFixture msFixture) {
        this.name = name;
        this.description = description;
        this.roomMemberFixture = roomMemberFixture;
        this.msFixture = msFixture;
    }

    public Room create() {
        Room room = new Room(description, name);

        RoomMember roomMember = roomMemberFixture.create(room);
        room.setMembers(Collections.singletonList(roomMember));

        room.setSchedules(Collections.singletonList(msFixture.create(room)));

        return room;
    }
}
