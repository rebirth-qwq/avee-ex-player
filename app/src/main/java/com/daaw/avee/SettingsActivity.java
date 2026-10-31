package com.daaw.avee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import com.daaw.avee.vendor.aosp.AppCompatPreferenceActivity;
import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.LanguageHelper;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Design.VisualizerSharingDesign;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.VisualizerCutomization.CustomizeVisDialog;
import com.daaw.avee.vendor.licenses.LicensesFragment;

/* JADX INFO: loaded from: classes.dex */
public class SettingsActivity extends AppCompatPreferenceActivity {
    public static WeakEvent1<ContextData> onSendGeneralFeedbackAction = new WeakEvent1<>();
    public static WeakEvent1<Object> onPrefAppThemeChanged = new WeakEvent1<>();
    public static WeakEvent1<Boolean> onPrefHighQualityBlurChanged = new WeakEvent1<>();
    public static WeakEvent1<Integer> onPrefAudioVisOffsetChanged = new WeakEvent1<>();
    public static WeakEvent onClearCacheAction = new WeakEvent();
    public static WeakEvent1<ContextData> onEulaAction = new WeakEvent1<>();

    @Override // com.daaw.avee.vendor.aosp.AppCompatPreferenceActivity, android.preference.PreferenceActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LanguageHelper.applyLanguage(this, LanguageHelper.getLanguage(this));
        super.onCreate(bundle);
        setContentView(re.ex.avee.R.layout.preference_toolbar);
        setSupportActionBar((Toolbar) findViewById(re.ex.avee.R.id.toolbar));
        setupActionBar();
        getFragmentManager().beginTransaction().replace(re.ex.avee.R.id.content_frame, new SettingsFragment()).commit();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        LanguageHelper.applyLanguage(this, LanguageHelper.getLanguage(this));
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageHelper.wrapContext(context));
    }

    void setupActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override // android.preference.PreferenceFragment, android.app.Fragment
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            AppPreferences.createOrGetInstance().save(getActivity());
            addPreferencesFromResource(re.ex.avee.R.xml.preferences);
            addMyResetListener();
            addVisualizerSettingsListeners();
        }

        @Override // android.app.Fragment
        public void onResume() {
            super.onResume();
            LanguageHelper.applyLanguage(getActivity(), LanguageHelper.getLanguage(getActivity()));
        }

        private void addMyResetListener() {
            findPreference("pref_resetToDefault").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.1
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsFragment.this.getActivity());
                    builder.setTitle(re.ex.avee.R.string.pref_reset_dialog_title);
                    builder.setMessage(re.ex.avee.R.string.pref_reset_dialog_message);
                    builder.setPositiveButton(re.ex.avee.R.string.dialog_ok, new DialogInterface.OnClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SettingsFragment.this.resetSettings(SettingsFragment.this.getActivity().getApplicationContext());
                        }
                    });
                    builder.setNegativeButton(re.ex.avee.R.string.dialog_cancel, (DialogInterface.OnClickListener) null);
                    builder.create().show();
                    return true;
                }
            });
            findPreference("pref_appTheme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.4
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    SettingsActivity.onPrefAppThemeChanged.invoke(obj);
                    return true;
                }
            });
            findPreference("pref_resetTips").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.5
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    AppPreferences.createOrGetInstance().resetTips();
                    return true;
                }
            });
            findPreference("pref_fontsLicenses").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.6
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    LicensesFragment.displayLicensesFragment(SettingsFragment.this.getFragmentManager(), true, re.ex.avee.R.raw.licenses_fonts, re.ex.avee.R.string.pref_fontsLicenses, "");
                    return true;
                }
            });
            findPreference("pref_openSourceLicenses").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.7
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    LicensesFragment.displayLicensesFragment(SettingsFragment.this.getFragmentManager(), true, re.ex.avee.R.raw.licenses, re.ex.avee.R.string.pref_openSourceLicenses, "");
                    return true;
                }
            });
            EditTextPreference editTextPreference = (EditTextPreference) findPreference("pref_playlistDefaultPath");
            editTextPreference.setSummary(editTextPreference.getText());
            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.8
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    preference.setSummary(obj.toString());
                    return true;
                }
            });
            EditTextPreference editTextPreference2 = (EditTextPreference) findPreference("pref_audioVisOffset");
            editTextPreference2.setSummary(editTextPreference2.getText());
            editTextPreference2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.9
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    preference.setSummary(obj.toString());
                    SettingsActivity.onPrefAudioVisOffsetChanged.invoke(Integer.valueOf(Utils.strToIntSafe(obj.toString(), 0)));
                    return true;
                }
            });
            ((CheckBoxPreference) findPreference("pref_highQualityBlur")).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.10
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    SettingsActivity.onPrefHighQualityBlurChanged.invoke((Boolean) obj);
                    return true;
                }
            });
            findPreference("pref_clearCache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.11
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    SettingsActivity.onClearCacheAction.invoke();
                    return true;
                }
            });
            findPreference("pref_eula").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.12
                @Override // android.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    SettingsActivity.onEulaAction.invoke(new ContextData(SettingsFragment.this.getActivity()));
                    return true;
                }
            });
        }

        private void addVisualizerSettingsListeners() {
            EditTextPreference editTextPreference;
            final Activity activity = getActivity();
            if (activity != null && (editTextPreference = (EditTextPreference) findPreference("pref_export_path")) != null) {
                String exportPath = AppPreferences.createOrGetInstance().getExportPath(activity);
                editTextPreference.setSummary(exportPath);
                editTextPreference.setText(exportPath);
                editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.daaw.avee.SettingsActivity.SettingsFragment.15
                    @Override // android.preference.Preference.OnPreferenceChangeListener
                    public boolean onPreferenceChange(Preference preference, Object obj) {
                        String string = obj.toString();
                        AppPreferences.createOrGetInstance().setExportPath(activity, string);
                        preference.setSummary(string);
                        VisualizerSharingDesign.ensureExportDirectoryExists(activity);
                        return true;
                    }
                });
            }
        }


        void resetSettings(Context context) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
            PreferenceManager.setDefaultValues(context, re.ex.avee.R.xml.preferences, true);
            setPreferenceScreen(null);
            addPreferencesFromResource(re.ex.avee.R.xml.preferences);
            addMyResetListener();
            addVisualizerSettingsListeners();
        }
    }
}
