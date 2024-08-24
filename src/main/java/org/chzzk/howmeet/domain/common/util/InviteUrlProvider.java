//package org.chzzk.howmeet.domain.common.util;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class InviteUrlProvider {
//
//    private final String baseUrl;
//
//    public InviteUrlProvider(@Value("${invite.url.base-url}") String baseUrl) {
//        this.baseUrl = baseUrl;
//    }
//
//    public String generateInviteUrl(final String scheduleType, final Long scheduleId) {
//        return baseUrl + "/" + scheduleType + "/invite/" + scheduleId;
//    }
//}
