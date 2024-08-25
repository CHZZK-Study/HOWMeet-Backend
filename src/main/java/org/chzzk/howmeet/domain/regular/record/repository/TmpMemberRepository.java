package org.chzzk.howmeet.domain.regular.record.repository;

import java.util.List;
import org.chzzk.howmeet.domain.regular.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TmpMemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByMemberScheduleId(@Param("memberScheduleId") final Long memberScheduleId);
}
