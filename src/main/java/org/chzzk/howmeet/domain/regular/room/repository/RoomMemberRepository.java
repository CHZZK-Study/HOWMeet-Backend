package org.chzzk.howmeet.domain.regular.room.repository;

import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findByRoomId(final Long roomId);
    Page<RoomMember> findByMemberId(Long memberId, Pageable pageable);
    Optional<RoomMember> findByRoomIdAndMemberId(@Param("roomId") final Long roomId, @Param("memberId") final Long memberId);
    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);
}