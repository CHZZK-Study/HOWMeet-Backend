package org.chzzk.howmeet.fixture;

import org.chzzk.howmeet.domain.regular.member.entity.Member;
import org.springframework.test.util.ReflectionTestUtils;

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
        return 생성(null);
    }

    public Member 생성(final Long id) {
        final Member member = Member.of(nickname, profileImage, socialId);
        ReflectionTestUtils.setField(member, "id", id);   // Reflection 을 사용하여 PK 설정
        return member;
    }
}
