package org.chzzk.howmeet.domain.regular.member.dto.summary.response;

import org.chzzk.howmeet.domain.regular.member.dto.summary.dto.MemberSummaryDto;

public record MemberSummaryResponse(Long id, String nickname) {
    public static MemberSummaryResponse from(final MemberSummaryDto memberSummaryDto) {
        return new MemberSummaryResponse(memberSummaryDto.id(), memberSummaryDto.nickname().getValue());
    }
}
