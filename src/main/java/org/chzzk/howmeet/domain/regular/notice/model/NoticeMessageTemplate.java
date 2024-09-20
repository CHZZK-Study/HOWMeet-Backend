package org.chzzk.howmeet.domain.regular.notice.model;

import lombok.Getter;

@Getter
public enum NoticeMessageTemplate {
    ALL_MEMBERS_COMPLETED("일정 확정 요청", "일정을 확정해주세요."),
    SCHEDULE_CONFIRMED("일정 확정 완료", "일정 확정이 완료되었습니다.");

    private final String title;
    private final String body;

    NoticeMessageTemplate(final String title, final String body){
        this.title = title;
        this.body = body;
    }

    public String formatTitle(final String msName){
        return msName + title;
    }

    public String formatBody(final String roomName, final String msName){
        return roomName + "방의 " + msName + body;
    }
}
