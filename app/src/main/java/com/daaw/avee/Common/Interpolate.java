package com.daaw.avee.Common;

import android.graphics.Color;

/* JADX INFO: loaded from: classes.dex */
public class Interpolate {
    public static double Lerp(double d, double d2, double d3) {
        return d + ((d2 - d) * d3);
    }

    public static float Lerp(float f, float f2, double d) {
        return f + ((f2 - f) * ((float) d));
    }

    public static void Lerp(Vec2f vec2f, Vec2f vec2f2, Vec2f vec2f3, double d) {
        float f = (float) d;
        vec2f.x = vec2f2.x + ((vec2f3.x - vec2f2.x) * f);
        vec2f.y = vec2f2.y + ((vec2f3.y - vec2f2.y) * f);
    }

    public static int LerpColor(int i, int i2, float f) {
        float fMax = Math.max(Color.red(i), Math.max(Color.green(i), Color.blue(i)));
        float fMax2 = fMax + ((Math.max(Color.red(i2), Math.max(Color.green(i2), Color.blue(i2))) - fMax) * f);
        float fRed = Color.red(i) + ((Color.red(i2) - Color.red(i)) * f);
        float fGreen = Color.green(i) + ((Color.green(i2) - Color.green(i)) * f);
        float fBlue = Color.blue(i) + ((Color.blue(i2) - Color.blue(i)) * f);
        float fAlpha = Color.alpha(i) + ((Color.alpha(i2) - Color.alpha(i)) * f);
        float fSqrt = (float) Math.sqrt((fRed * fRed) + (fGreen * fGreen) + (fBlue * fBlue));
        return Color.argb((int) fAlpha, (int) ((fRed / fSqrt) * fMax2), (int) ((fGreen / fSqrt) * fMax2), (int) ((fBlue / fSqrt) * fMax2));
    }
}
