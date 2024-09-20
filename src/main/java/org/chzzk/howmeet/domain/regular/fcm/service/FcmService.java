package org.chzzk.howmeet.domain.regular.fcm.service;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.fcm.dto.FcmTokenRequest;
import org.chzzk.howmeet.domain.regular.fcm.entity.FcmToken;
import org.chzzk.howmeet.domain.regular.fcm.repository.FcmTokenRepository;
import org.chzzk.howmeet.domain.regular.notice.entity.Notice;
import org.chzzk.howmeet.domain.regular.notice.entity.NoticeMessageTemplate;
import org.chzzk.howmeet.domain.regular.notice.entity.NoticeStatus;
import org.chzzk.howmeet.domain.regular.notice.repository.NoticeRepository;
import org.chzzk.howmeet.domain.regular.room.entity.RoomMember;
import org.chzzk.howmeet.domain.regular.schedule.entity.MemberSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final NoticeRepository noticeRepository;

    public void saveFcmToken(final FcmTokenRequest fcmTokenRequest, final AuthPrincipal authPrincipal){
        FcmToken fcmToken = fcmTokenRepository.findById(authPrincipal.id())
                .map(existingToken -> {
                    existingToken.updateValue(fcmTokenRequest.token());
                    return existingToken;
                })
                .orElseGet(() -> {
                    return FcmToken.of(authPrincipal, fcmTokenRequest.token());
                });

        fcmTokenRepository.save(fcmToken);

    }

    public void sendToLeader(final List<RoomMember> members, final MemberSchedule ms) {
        final RoomMember leader = members.stream().filter(RoomMember::getIsLeader).findFirst().orElseThrow(() -> new IllegalArgumentException("방장이 없습니다."));
        final FcmToken fcmToken = findFcmTokenById(leader.getMemberId());
        if(fcmToken == null) {
            return;
        }

        final Notice notice = Notice.of(fcmToken, ms, NoticeStatus.SENDING, NoticeMessageTemplate.ALL_MEMBERS_COMPLETED);

        send(fcmToken, notice);
        noticeRepository.save(notice);
    }

    public void sendToRoomMember(final List<RoomMember> members, final MemberSchedule ms) {

        members.stream()
            .filter(member -> !member.getIsLeader())
            .map(member -> findFcmTokenById(member.getMemberId()))
            .filter(Objects::nonNull)
            .forEach(fcmToken -> {
                Notice notice = Notice.of(fcmToken, ms, NoticeStatus.SENDING, NoticeMessageTemplate.ALL_MEMBERS_COMPLETED);
                send(fcmToken, notice);
                noticeRepository.save(notice);
            });
    }

    private void send(final FcmToken fcmToken, final Notice notice) {
        Message message = Message.builder()
                .setToken(fcmToken.getValue())
                .setNotification(Notification.builder()
                        .setTitle(notice.getTitle())
                        .setBody(notice.getBody())
                        .build())
                .build();
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    private FcmToken findFcmTokenById(final Long id){
        return fcmTokenRepository.findById(id).orElse(null);
    }
}
