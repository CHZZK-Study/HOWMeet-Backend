package org.chzzk.howmeet.domain.regular.fcm.dto;

public record VapidResponse (String key) {
    public static VapidResponse of(String key) {
        return new VapidResponse(key);
    }

}
