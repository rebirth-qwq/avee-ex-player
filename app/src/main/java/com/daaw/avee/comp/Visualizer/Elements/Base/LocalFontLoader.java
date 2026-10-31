package com.daaw.avee.comp.Visualizer.Elements.Base;

import com.daaw.avee.Common.Utils;

/* JADX INFO: loaded from: classes.dex */
public class LocalFontLoader {
    public static final String Prefix = "font:";
    private static final char Separator = ':';

    public static String getFileNameFromPath(String str) {
        if (str == null) {
            return null;
        }
        String[] strArr = new String[2];
        Utils.stringSplitInTwo(str, 58, strArr);
        if ("font".equals(strArr[0])) {
            return strArr[1];
        }
        return null;
    }

    public static boolean isFontPath(String str) {
        return str != null && str.startsWith(Prefix);
    }

    public static boolean isFontFile(String str) {
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return lowerCase.endsWith(".ttf") || lowerCase.endsWith(".otf") || lowerCase.endsWith(".ttc") || lowerCase.endsWith(".otc");
    }
}
