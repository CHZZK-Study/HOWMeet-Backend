package org.chzzk.howmeet.domain.regular.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.chzzk.howmeet.domain.common.auth.entity.UserDetails;
import org.chzzk.howmeet.domain.common.auth.model.Role;
import org.chzzk.howmeet.domain.common.entity.BaseEntity;
import org.chzzk.howmeet.domain.common.model.Image;
import org.chzzk.howmeet.domain.common.model.Nickname;
import org.chzzk.howmeet.domain.common.model.NicknameProvider;
import org.chzzk.howmeet.domain.common.model.converter.ImageConverter;
import org.chzzk.howmeet.domain.common.model.converter.NicknameConverter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity implements UserDetails, NicknameProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    @Convert(converter = NicknameConverter.class)
    private Nickname nickname;

    @Column(name = "profile_image", nullable = false)
    @Convert(converter = ImageConverter.class)
    private Image profileImage;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    private Member(final Nickname nickname, final Image profileImage, final String socialId) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.socialId = socialId;
    }

    public static Member of(String nickname, String profileImage, String socialId) {
        return new Member(Nickname.from(nickname), Image.from(profileImage), socialId);
    }

    @Override
    public Role getRole() {
        return Role.REGULAR;
    }
}
