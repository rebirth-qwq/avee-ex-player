package com.daaw.avee.vendor.playlists.lang;

/* JADX INFO: loaded from: classes.dex */
public final class StringUtils {
    public static String toString(int i, int i2) {
        StringBuilder sb = new StringBuilder(Integer.toString(i));
        while (sb.length() < i2) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    public static String toString(long j, int i) {
        StringBuilder sb = new StringBuilder(Long.toString(j));
        while (sb.length() < i) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    public static String normalize(String str) {
        if (str != null) {
            String strTrim = str.trim();
            if (!strTrim.isEmpty()) {
                return strTrim;
            }
        }
        return null;
    }

    private StringUtils() {
    }
}
