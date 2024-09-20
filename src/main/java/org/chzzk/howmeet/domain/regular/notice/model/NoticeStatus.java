package org.chzzk.howmeet.domain.regular.notice.model;

import lombok.Getter;

@Getter
public enum NoticeStatus {

    SENDING("전송 중"),
    SENT_FAILED("전송 실패"),
    SENT_SUCCESS("전송 완료"),
    READ("읽음"),
    UNREAD("읽지 않음");


    private final String value;

    NoticeStatus(final String value) {
        this.value = value;
    }

}
