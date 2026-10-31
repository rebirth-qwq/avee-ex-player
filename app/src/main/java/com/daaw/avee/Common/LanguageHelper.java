package com.daaw.avee.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.daaw.avee.MainActivity;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class LanguageHelper {
    public static final String LANG_EN = "en";
    public static final String LANG_IN = "in";
    public static final String LANG_SYSTEM = "system";
    public static final String LANG_ZH = "zh";
    public static final String LANG_ZH_SG = "zh-rSG";
    public static final String LANG_ZH_TW = "zh-rTW";
    public static final String PREF_LANGUAGE = "pref_language";

    public static void setLanguage(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_LANGUAGE, str).apply();
    }

    public static String getLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LANGUAGE, LANG_SYSTEM);
    }

    public static Locale getLocale(Context context) {
        return getLocaleFromCode(getLanguage(context));
    }

    public static Context wrapContext(Context context) {
        return wrapContext(context, getLanguage(context));
    }

    public static Context wrapContext(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 24) {
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(getLocaleFromCode(str));
            return context.createConfigurationContext(configuration);
        }
        applyLanguage(context, str);
        return context;
    }

    public static void applyLanguage(Context context, String str) {
        if (context != null) {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            Locale localeFromCode = getLocaleFromCode(str);
            Locale.setDefault(localeFromCode);
            if (Build.VERSION.SDK_INT >= 24) {
                configuration.setLocale(localeFromCode);
            } else {
                configuration.locale = localeFromCode;
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }

    private static Locale getLocaleFromCode(String str) {
        if (str == null || str.equals(LANG_SYSTEM)) {
            return Locale.getDefault();
        }
        if (str.equals(LANG_ZH_TW)) {
            return new Locale(LANG_ZH, "TW");
        }
        if (str.equals(LANG_ZH_SG)) {
            return new Locale(LANG_ZH, "SG");
        }
        if (str.equals(LANG_IN)) {
            return new Locale(LANG_IN);
        }
        return new Locale(str);
    }

    public static void switchLanguageAndRestart(final Activity activity, String str) {
        final String str2;
        setLanguage(activity, str);
        applyLanguage(activity, str);
        Locale.setDefault(getLocaleFromCode(str));
        if (str.equals(LANG_ZH) || str.equals(LANG_ZH_TW) || str.equals(LANG_ZH_SG)) {
            str2 = "语言已切换 应用即将重启";
        } else {
            str2 = str.equals(LANG_IN) ? "" : "Language switched App will restart";
        }
        activity.runOnUiThread(new Runnable() { // from class: com.daaw.avee.Common.LanguageHelper.1
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(activity, str2, 1).show();
            }
        });
        new Handler().postDelayed(new Runnable() { // from class: com.daaw.avee.Common.LanguageHelper.2
            @Override // java.lang.Runnable
            public void run() {
                Intent intent = new Intent(activity, (Class<?>) MainActivity.class);
                intent.addFlags(268468224);
                activity.startActivity(intent);
                activity.finishAffinity();
                try {
                    Thread.sleep(200L);
                } catch (Exception e) {
                }
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }, 800L);
    }
}
