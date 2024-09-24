package org.chzzk.howmeet.domain.regular.confirm.dto;

import java.time.LocalDateTime;

public record SelectTimeCount(LocalDateTime time, Integer count) {
    public static SelectTimeCount of(final LocalDateTime time, final Integer count){
      return new SelectTimeCount(time, count);
    }
}
