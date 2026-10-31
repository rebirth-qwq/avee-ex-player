package com.daaw.avee.Common;

/* JADX INFO: loaded from: classes.dex */
public class DbgUtils {
    public static String getStackTrace() {
        StringBuffer stringBuffer = new StringBuffer();
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            stringBuffer.append("\tat " + stackTraceElement);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
