package org.chzzk.howmeet.domain.common.auth.model;

import org.chzzk.howmeet.domain.common.auth.entity.UserDetails;

public record AuthPrincipal(Long id, String nickname, Role role) {
    public static AuthPrincipal from(final UserDetails userDetails) {
        return new AuthPrincipal(userDetails.getId(), userDetails.getNickname().getValue(), userDetails.getRole());
    }

    public boolean isGuest() {
        return role.equals(Role.TEMPORARY);
    }

    public boolean isMember() {
        return role.equals(Role.REGULAR);
    }
}
