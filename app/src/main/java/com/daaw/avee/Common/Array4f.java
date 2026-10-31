package com.daaw.avee.Common;

import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class Array4f {
    public static void fromString(String str, float[] fArr, float[] fArr2) {
        if (str == null) {
            fArr[0] = fArr2[0];
            fArr[1] = fArr2[1];
            fArr[2] = fArr2[2];
            fArr[3] = fArr2[3];
        }
        try {
            int iIndexOf = str.indexOf(" ");
            int i = iIndexOf + 1;
            int iIndexOf2 = str.indexOf(" ", i);
            int i2 = iIndexOf2 + 1;
            int iIndexOf3 = str.indexOf(" ", i2);
            if (iIndexOf >= 0 && iIndexOf2 >= 0 && iIndexOf3 >= 0) {
                String strSubstring = str.substring(0, iIndexOf);
                String strSubstring2 = str.substring(i, iIndexOf2);
                String strSubstring3 = str.substring(i2, iIndexOf3);
                String strSubstring4 = str.substring(iIndexOf3 + 1);
                fArr[0] = Float.parseFloat(strSubstring);
                fArr[1] = Float.parseFloat(strSubstring2);
                fArr[2] = Float.parseFloat(strSubstring3);
                fArr[3] = Float.parseFloat(strSubstring4);
            }
        } catch (NumberFormatException unused) {
            fArr[0] = fArr2[0];
            fArr[1] = fArr2[1];
            fArr[2] = fArr2[2];
            fArr[3] = fArr2[3];
        }
    }

    public static String toString(float[] fArr) {
        return String.format(Locale.US, "%f %f %f %f", Float.valueOf(fArr[0]), Float.valueOf(fArr[1]), Float.valueOf(fArr[2]), Float.valueOf(fArr[3]));
    }
}
