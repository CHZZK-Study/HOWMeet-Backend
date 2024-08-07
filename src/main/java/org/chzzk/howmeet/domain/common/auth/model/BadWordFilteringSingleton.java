package org.chzzk.howmeet.domain.common.auth.model;

import com.vane.badwordfiltering.BadWordFiltering;

public class BadWordFilteringSingleton {
    private static class SingletonInstanceHolder {
        private static final BadWordFiltering INSTANCE = new BadWordFiltering();
    }

    public static boolean containsBadWord(final String value) {
        return SingletonInstanceHolder.INSTANCE.check(value);
    }
}
