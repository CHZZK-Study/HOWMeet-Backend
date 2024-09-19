package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.room.dto.RoomListResponse;
import org.chzzk.howmeet.domain.regular.room.dto.RoomRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.room.model.RoomDescription;
import org.chzzk.howmeet.domain.regular.room.model.RoomName;
import org.chzzk.howmeet.domain.regular.room.util.RoomListMapper;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.chzzk.howmeet.fixture.MemberFixture.KIM;

public enum RoomFixture {
    ROOM_A("Room A"),
    ROOM_B("Room B");

    private final String name;

    RoomFixture(final String name) {
        this.name = name;
    }

    public static Room createRoomA() {
        Room room = new Room(RoomName.from(ROOM_A.name));
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
        Room room = new Room(RoomName.from(ROOM_B.name));
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
                1L
        );
    }

    public static RoomRequest createRoomRequestB() {
        return new RoomRequest(
                RoomName.from(ROOM_B.name),
                1L
        );
    }

    public static RoomResponse createRoomResponseA() {
        Room room = createRoomA();
        return RoomResponse.of(room, room.getMembers(), room.getSchedules());
    }

    public static RoomResponse createRoomResponseB() {
        Room room = createRoomB();
        return RoomResponse.of(room, room.getMembers(), room.getSchedules());
    }

    public static RoomListResponse createRoomListResponseA() {
        Room room = createRoomA();
        List<MemberSchedule> memberSchedules = room.getSchedules();
        Member leader = KIM.생성();
        return RoomListMapper.toRoomListResponse(room, memberSchedules, leader.getNickname().toString());
    }

    public static RoomListResponse createRoomListResponseB() {
        Room room = createRoomB();
        List<MemberSchedule> memberSchedules = room.getSchedules();
        Member leader = KIM.생성();
        return RoomListMapper.toRoomListResponse(room, memberSchedules, leader.getNickname().toString());
    }
}
