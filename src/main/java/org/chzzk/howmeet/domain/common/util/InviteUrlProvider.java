package org.chzzk.howmeet.domain.common.util;

public class InviteUrlProvider {

    private final String baseUrl;

    public InviteUrlProvider(final String basePath) {
        this.baseUrl = "http://localhost:8080/" + basePath + "/invite/";
    }

    public String generateInviteUrl(final Long memberScheduleId) {
        return baseUrl + memberScheduleId;
    }
}
