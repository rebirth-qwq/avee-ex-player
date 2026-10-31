package com.daaw.avee.comp.VisualUI;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes.dex */
public class AspectRatioHelper {
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_RATIO = "ratio";
    private static final String KEY_WIDTH = "width";
    private static final String PREF_NAME = "avee_vis_aspect";

    public static void saveAspectRatio(Context context, float f, int i, int i2) {
        context.getSharedPreferences(PREF_NAME, 0).edit().putFloat(KEY_RATIO, f).putInt(KEY_WIDTH, i).putInt(KEY_HEIGHT, i2).apply();
    }

    public static float[] loadAspectRatio(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        return new float[]{sharedPreferences.getFloat(KEY_RATIO, 0.0f), sharedPreferences.getInt(KEY_WIDTH, 16), sharedPreferences.getInt(KEY_HEIGHT, 9)};
    }
}
