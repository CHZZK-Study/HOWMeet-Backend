package org.chzzk.howmeet.domain.regular.auth.service;

import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.common.auth.model.Role;
import org.chzzk.howmeet.domain.regular.auth.entity.RefreshToken;
import org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenException;
import org.chzzk.howmeet.domain.regular.auth.repository.RefreshTokenRepository;
import org.chzzk.howmeet.domain.regular.auth.util.RefreshTokenProvider;
import org.chzzk.howmeet.fixture.GuestFixture;
import org.chzzk.howmeet.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenErrorCode.REFRESH_TOKEN_NOT_MATCHED;
import static org.chzzk.howmeet.domain.regular.auth.exception.RefreshTokenErrorCode.REFRESH_TOKEN_NO_AUTHORITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCrudServiceTest {
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    RefreshTokenProvider refreshTokenProvider;

    @InjectMocks
    RefreshTokenCrudService refreshTokenCrudService;

    AuthPrincipal memberPrincipal = AuthPrincipal.from(MemberFixture.KIM.생성());
    AuthPrincipal guestAuthPrincipal = AuthPrincipal.from(GuestFixture.KIM.생성());
    String refreshTokenValue = "refreshToken";
    Long expiration = 360_000L;
    RefreshToken refreshToken = RefreshToken.of(memberPrincipal, refreshTokenValue, expiration);

    @Test
    @DisplayName("리프레시 토큰 저장")
    public void save() throws Exception {
        // given
        final RefreshToken expect = refreshToken;

        // when
        doReturn(expect).when(refreshTokenProvider)
                .createToken(memberPrincipal);
        doReturn(expect).when(refreshTokenRepository)
                .save(any());
        final RefreshToken actual = refreshTokenCrudService.save(memberPrincipal);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("1회용 회원으로 리프레시 토큰 저장시 예외 발생")
    public void saveWhenInvalidAuthPrincipal() throws Exception {
        assertThatThrownBy(() -> refreshTokenCrudService.save(guestAuthPrincipal))
                .isInstanceOf(RefreshTokenException.class)
                .hasMessageContaining(REFRESH_TOKEN_NO_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("토큰 파싱 정보와 리프레시 토큰값을 통해 리프레시 토큰 삭제")
    public void deleteByAuthPrincipalAndValue() throws Exception {
        // when
        doReturn(Optional.of(refreshToken)).when(refreshTokenRepository)
                .findByValue(refreshTokenValue);
        doNothing().when(refreshTokenRepository)
                .delete(refreshToken);

        // then
        assertThatCode(() -> refreshTokenCrudService.delete(memberPrincipal, refreshTokenValue))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("1회용 회원으로 리프레시 토큰 삭제시 예외 발생")
    public void deleteWhenInvalidAuthPrincipal() throws Exception {
        assertThatThrownBy(() -> refreshTokenCrudService.delete(guestAuthPrincipal, refreshTokenValue))
                .isInstanceOf(RefreshTokenException.class)
                .hasMessageContaining(REFRESH_TOKEN_NO_AUTHORITY.getMessage());
    }

    @Test
    @DisplayName("리프레시 토큰 삭제 시 엑세스 토큰 회원 정보와 리프레시 토큰 회원 정보가 일치하지 않는 경우 예외 발생")
    public void deleteWhenNotMatchedRefreshToken() throws Exception {
        // given
        final AuthPrincipal otherPrincipal = new AuthPrincipal(1L, "홍길동", Role.REGULAR);

        // when
        doReturn(Optional.of(refreshToken)).when(refreshTokenRepository)
                .findByValue(refreshTokenValue);

        // then
        assertThatThrownBy(() -> refreshTokenCrudService.delete(otherPrincipal, refreshTokenValue))
                .isInstanceOf(RefreshTokenException.class)
                .hasMessageContaining(REFRESH_TOKEN_NOT_MATCHED.getMessage());
    }
}