package org.chzzk.howmeet.domain.common.auth.entity;

import org.chzzk.howmeet.domain.common.auth.model.Role;
import org.chzzk.howmeet.domain.common.model.Nickname;

public interface UserDetails {
    Long getId();
    Nickname getNickname();
    Role getRole();
}
