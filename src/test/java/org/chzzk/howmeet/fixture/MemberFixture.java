package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.auth.entity.Member;

public enum MemberFixture {
    KIM("김민우", "프로필 사진 URL", "123");
    private final String nickname;
    private final String profileImage;
    private final String socialId;

    MemberFixture(final String nickname, final String profileImage, final String socialId) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.socialId = socialId;
    }

    public Member 생성() {
        return Member.of(nickname, profileImage, socialId);
    }
}
