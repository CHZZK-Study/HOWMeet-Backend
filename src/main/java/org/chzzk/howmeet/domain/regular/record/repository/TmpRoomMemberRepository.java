package org.chzzk.howmeet.domain.regular.record.repository;

import java.util.List;
import java.util.Optional;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TmpRoomMemberRepository extends JpaRepository<RoomMember, Long> {

    Optional<RoomMember> findByMemberId(@Param("memberId") final Long memberId);

    List<RoomMember> findByRoomId(@Param("roomId") final Long roomId);
}
