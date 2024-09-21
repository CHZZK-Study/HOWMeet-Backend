package org.chzzk.howmeet.domain.regular.member.repository;

import org.chzzk.howmeet.domain.regular.member.dto.nickname.dto.MemberNicknameDto;
import org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryDSL {
    Optional<Member> findBySocialId(final String socialId);

    @Query("SELECT NEW org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto(m.id, m.nickname) " +
            "FROM Member m " +
            "WHERE m.id =:id")
    Optional<MemberSummaryDto> findSummaryById(@Param("id") final Long id);

    @Query("SELECT NEW org.chzzk.howmeet.domain.regular.member.dto.nickname.dto.MemberNicknameDto(m.id, m.nickname) " +
            "FROM Member m " +
            "WHERE m.id = :id")
    Optional<MemberNicknameDto> findIdAndNicknameById(@Param("id") final Long id);

    @Query("SELECT NEW org.chzzk.howmeet.domain.regular.member.dto.nickname.dto.MemberNicknameDto(m.id, m.nickname) " +
            "FROM Member m " +
            "WHERE m.id IN :memberIds")
    List<MemberNicknameDto> findNicknamesByMemberIds(@Param("memberIds") List<Long> memberIds);
}
