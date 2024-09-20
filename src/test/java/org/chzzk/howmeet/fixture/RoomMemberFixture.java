package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberRequest;
import org.chzzk.howmeet.domain.regular.room.dto.RoomMemberResponse;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;

import java.util.List;

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

    public RoomMemberRequest createRequest(Room room) {
        return new RoomMemberRequest(memberId, room.getId(), isLeader);
    }

    public RoomMemberResponse createResponse(Room room, String nickname) {
        RoomMember roomMember = RoomMember.of(memberId, room, isLeader);
        return RoomMemberResponse.from(roomMember, nickname);
    }

    public static List<RoomMemberRequest> createRoomMemberRequests(Room room) {
        return List.of(
                MEMBER_1.createRequest(room),
                MEMBER_2.createRequest(room),
                MEMBER_3.createRequest(room)
        );
    }

    public static List<RoomMemberResponse> createRoomMemberResponses(Room room) {
        return List.of(
                MEMBER_1.createResponse(room, "Kim"),
                MEMBER_2.createResponse(room, "Lee"),
                MEMBER_3.createResponse(room, "Park")
        );
    }
}
