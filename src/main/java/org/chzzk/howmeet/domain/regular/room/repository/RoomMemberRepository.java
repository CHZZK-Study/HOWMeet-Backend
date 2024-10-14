package org.chzzk.howmeet.domain.regular.room.repository;

import org.chzzk.howmeet.domain.regular.room.dto.get.response.RoomMemberGetResponse;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findByRoomId(final Long roomId);
    Page<RoomMember> findByMemberId(Long memberId, Pageable pageable);
    Optional<RoomMember> findByRoomIdAndMemberId(@Param("roomId") final Long roomId, @Param("memberId") final Long memberId);
    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);

    @Query("SELECT new org.chzzk.howmeet.domain.regular.room.dto.get.response.RoomMemberGetResponse(rm.isLeader) " +
            "FROM RoomMember rm " +
            "WHERE rm.room.id = :roomId AND rm.memberId = :memberId")
    Optional<RoomMemberGetResponse> findRoomMemberGetResponseByRoomIdAndMemberId(@Param("roomId") Long roomId, @Param("memberId") Long memberId);
}