package org.chzzk.howmeet.domain.regular.fcm.repository;


import org.chzzk.howmeet.domain.regular.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

}
