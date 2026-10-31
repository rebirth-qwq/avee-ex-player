package com.daaw.avee;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.daaw.avee.Common.LanguageHelper;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseActivity extends AppCompatActivity {
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageHelper.wrapContext(context));
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        applyLocale();
        LanguageHelper.applyLanguage(this, LanguageHelper.getLanguage(this));
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        applyLocale();
        LanguageHelper.applyLanguage(this, LanguageHelper.getLanguage(this));
    }

    private void applyLocale() {
        String language = LanguageHelper.getLanguage(this);
        Locale locale = LanguageHelper.getLocale(this);
        if (language != null && !language.equals(LanguageHelper.LANG_SYSTEM)) {
            Locale.setDefault(locale);
        }
    }
}
