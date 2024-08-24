package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public enum RoomFixture {
    ROOM_A("Room A", "Description for Room A"),
    ROOM_B("Room B", "Description for Room B");

    private final String name;
    private final String description;

    RoomFixture(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public static Room createRoomA() {
        Room room = new Room(RoomDescription.from(ROOM_A.description), RoomName.from(ROOM_A.name));
        ReflectionTestUtils.setField(room, "id", 1L);
        return room;
    }

    public static Room createRoomB() {
        Room room = new Room(RoomDescription.from(ROOM_B.description), RoomName.from(ROOM_B.name));
        ReflectionTestUtils.setField(room, "id", 2L);
        return room;
    }

    public static Room createRoomWithSchedulesAndMembers(RoomFixture fixture, List<MemberSchedule> schedules, List<RoomMember> members) {
        Room room = new Room(RoomDescription.from(fixture.description), RoomName.from(fixture.name));
        room.updateSchedules(schedules);
        room.updateMembers(members);
        return room;
    }

    public RoomName getName() {
        return RoomName.from(name);
    }

    public RoomDescription getDescription() {
        return RoomDescription.from(description);
    }
}