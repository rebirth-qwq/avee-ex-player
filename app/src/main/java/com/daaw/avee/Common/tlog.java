package com.daaw.avee.Common;

import android.util.Log;
 
/* JADX INFO: loaded from: classes.dex */
public class tlog {
    static final boolean LOG = false;

    private static String getLogTagWithMethod(String str) {
        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        return str + stackTrace[2].getFileName() + "." + stackTrace[2].getMethodName() + ":" + stackTrace[2].getLineNumber();
    }

    public static void w(String str) {
        Log.d("rebirth-dbg", str);
    }

    public static void d(String str) {
        Log.d("rebirth-info", str);
    }

    public static void e(Exception exc, String str) {
        if (exc != null) {
            
        }
    }

    public static void infoLarge(String str, String str2) {
        int i = 0;
        while (i <= str2.length() / 2000) {
            int i2 = i * 2000;
            i++;
            Log.w(str, str2.substring(i2, Math.min(i * 2000, str2.length())));
        }
    }
}
