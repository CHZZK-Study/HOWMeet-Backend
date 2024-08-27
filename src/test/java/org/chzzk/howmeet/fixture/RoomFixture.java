package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public enum RoomFixture {
    ROOM_A("Room A", "This is room A"),
    ROOM_B("Room B", "This is room B");

    private final String name;
    private final String description;

    RoomFixture(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public static Room createRoomA() {
        Room room = new Room(RoomDescription.from(ROOM_A.description), RoomName.from(ROOM_A.name));
        List<MemberSchedule> schedules = List.of(MSFixture.createMemberScheduleA(room));
        List<RoomMember> members = List.of(
                RoomMemberFixture.MEMBER_1.create(room),
                RoomMemberFixture.MEMBER_2.create(room),
                RoomMemberFixture.MEMBER_3.create(room)
        );
        room.updateSchedules(schedules);
        room.updateMembers(members);
        ReflectionTestUtils.setField(room, "id", 1L);
        return room;
    }

    public static Room createRoomB() {
        Room room = new Room(RoomDescription.from(ROOM_B.description), RoomName.from(ROOM_B.name));
        List<MemberSchedule> schedules = List.of(MSFixture.createMemberScheduleB(room));
        List<RoomMember> members = List.of(
                RoomMemberFixture.MEMBER_1.create(room),
                RoomMemberFixture.MEMBER_2.create(room),
                RoomMemberFixture.MEMBER_3.create(room)
        );
        room.updateSchedules(schedules);
        room.updateMembers(members);
        ReflectionTestUtils.setField(room, "id", 2L);
        return room;
    }

    public static RoomRequest createRoomRequestA() {
        return new RoomRequest(
                RoomName.from(ROOM_A.name),
                RoomDescription.from(ROOM_A.description),
                MSFixture.createMSRequestA(null),
                1L // 리더 멤버 ID
        );
    }

    public static RoomRequest createRoomRequestB() {
        return new RoomRequest(
                RoomName.from(ROOM_B.name),
                RoomDescription.from(ROOM_B.description),
                MSFixture.createMSRequestB(null),
                2L // 리더 멤버 ID
        );
    }

    public static RoomResponse createRoomResponseA() {
        Room room = ROOM_A.createRoomA();
        return RoomResponse.of(room, room.getMembers(), room.getSchedules());
    }

    public static RoomResponse createRoomResponseB() {
        Room room = ROOM_B.createRoomB();
        return RoomResponse.of(room, room.getMembers(), room.getSchedules());
    }
}
