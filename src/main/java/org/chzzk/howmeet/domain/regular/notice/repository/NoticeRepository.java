package org.chzzk.howmeet.domain.regular.notice.repository;

import org.chzzk.howmeet.domain.regular.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
