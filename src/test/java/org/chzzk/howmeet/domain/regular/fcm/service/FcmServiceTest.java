package org.chzzk.howmeet.domain.regular.fcm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.fcm.dto.FcmTokenRequest;
import org.chzzk.howmeet.domain.regular.fcm.entity.FcmToken;
import org.chzzk.howmeet.domain.regular.fcm.repository.FcmTokenRepository;
import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.chzzk.howmeet.domain.regular.notice.repository.NoticeRepository;
import org.chzzk.howmeet.domain.regular.room.entity.Room;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.chzzk.howmeet.fixture.RoomFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FcmServiceTest {

    @Mock
    private FcmTokenRepository fcmTokenRepository;

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private FcmService fcmService;

    Member member = MemberFixture.KIM.생성();


    AuthPrincipal authPrincipal = new AuthPrincipal(1L, member.getNickname().getValue(), member.getRole());
    Room room = RoomFixture.createRoomA();

    @Test
    @DisplayName("fcm토큰 저장 성공 테스트")
    public void saveFcmToken() {
        FcmTokenRequest fcmTokenRequest = new FcmTokenRequest("dummyToken");

        when(fcmTokenRepository.findById(authPrincipal.id())).thenReturn(Optional.empty());
        when(fcmTokenRepository.save(any(FcmToken.class))).thenReturn(FcmToken.of(authPrincipal, fcmTokenRequest.token()));

        fcmService.saveFcmToken(fcmTokenRequest, authPrincipal);

        verify(fcmTokenRepository).save(any(FcmToken.class));
    }

}
