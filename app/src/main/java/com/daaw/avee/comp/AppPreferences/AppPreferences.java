package com.daaw.avee.comp.AppPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.core.view.PointerIconCompat;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.daaw.avee.vendor.aosp.Context_storage;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.MultiList;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.UtilsSerialize;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomScene;
import com.daaw.avee.comp.VisualizerExporter.EncodingUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/* JADX INFO: loaded from: classes.dex */
public class AppPreferences implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String DEFAULT_EXPORT_PATH = "/storage/emulated/0/Avee/Template/EX/";
    private static final String PREF_EXPORT_PATH = "pref_export_path";
    public static WeakEvent3<Integer, Integer, Boolean> onIntPreferenceChanged = new WeakEvent3<>();
    public static WeakEvent2<Integer, Boolean> onBoolPreferenceChanged = new WeakEvent2<>();
    public static WeakEvent3<Integer, String, Boolean> onStringPreferenceChanged = new WeakEvent3<>();
    public static WeakEvent2<String, AppPreferences> onSharedPreferenceChanged = new WeakEvent2<>();
    private static final Object createInstanceLock = new Object();
    private static volatile WeakReference<AppPreferences> instanceWeak = new WeakReference<>(null);
    private static int offset_Bool = 1000;
    public static int PREF_Bool_pref_visControlsTimeout = 1000;
    public static int PREF_Bool_visualPreferShowVideoContent = 1001;
    public static int PREF_Bool_fixAssumeMonoOutputFromMonoInput = PointerIconCompat.TYPE_HAND;
    public static int PREF_Bool_followCurrentState = PointerIconCompat.TYPE_HELP;
    public static int PREF_Bool_audioMuteState = PointerIconCompat.TYPE_WAIT;
    public static int PREF_Bool_showAlbumArtInstead = 1005;
    public static int PREF_Bool_tipShow_reorder = PointerIconCompat.TYPE_CELL;
    public static int PREF_Bool_firstLaunch = PointerIconCompat.TYPE_CROSSHAIR;
    public static int PREF_Bool_uiSectionOpened0 = PointerIconCompat.TYPE_TEXT;
    public static int PREF_Bool_uiSectionOpened1 = PointerIconCompat.TYPE_VERTICAL_TEXT;
    public static int PREF_Bool_uiSectionOpened00 = PointerIconCompat.TYPE_ALIAS;
    public static int PREF_Bool_uiSectionOpened01 = PointerIconCompat.TYPE_COPY;
    public static int PREF_Bool_uiSectionOpened2 = PointerIconCompat.TYPE_NO_DROP;
    public static int PREF_Bool_visualizerUseGlobalSession = PointerIconCompat.TYPE_ALL_SCROLL;
    public static int PREF_Bool_equalizerEnabled = PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW;
    private static int PREF_Bool_COUNT = PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW;
    private static int offset_Int = 2000;
    public static int PREF_Int_mainPageIndex = 2000;
    public static int PREF_Int_recentlyAddedWeeks = 2001;
    public static int PREF_Int_visualizerThemeId = 2002;
    public static int PREF_Int_lockOrient = 2003;
    public static int PREF_Int_playbackEngine = 2004;
    public static int PREF_Int_videoScalingMode = 2005;
    public static int PREF_Int_SortSelectedRadioOption = 2006;
    public static int PREF_Int_SortMaskCheckOptions = 2007;
    public static int PREF_Int_volumeStereoBalance = 2008;
    public static int PREF_Int_crossFadeValue = 2009;
    public static int PREF_Int_equalizerPreset = 2010;
    public static int PREF_Int_equalizerBassValue = 2011;
    public static int PREF_Int_equalizerTrebleValue = 2012;
    public static int PREF_Int_virtualizerStrength = 2013;
    public static int PREF_Int_reverbPreset = 2014;
    public static int PREF_Int_containerItemViewMode = 2015;
    public static int PREF_Int_audioVisOffset = 2016;
    public static int PREF_Int_acceptedEula = 2017;
    public static int PREF_Int_aspectRatioWidth = 2018;
    public static int PREF_Int_aspectRatioHeight = 2019;
    private static int PREF_Int_COUNT = 2020;
    private static int offset_String = PathInterpolatorCompat.MAX_NUM_POINTS;
    public static int PREF_String_currentAbsoluteLibraryAddress = PathInterpolatorCompat.MAX_NUM_POINTS;
    public static int PREF_String_vThemeCustomization0 = 3001;
    public static int PREF_String_vThemeCustomization1 = 3002;
    public static int PREF_String_vThemeCustomization2 = 3003;
    public static int PREF_String_vThemeCustomization3 = 3004;
    public static int PREF_String_vThemeCustomization4 = 3005;
    public static int PREF_String_vThemeCustomization5 = 3006;
    public static int PREF_String_vThemeCustomization6 = 3007;
    public static int PREF_String_vThemeCustomization7 = 3008;
    public static int PREF_String_vThemeCustomization8 = 3009;
    public static int PREF_String_vThemeCustomization9 = 3010;
    public static int PREF_String_vThemeCustomization10 = 3011;
    public static int PREF_String_equalizerBarsValues = 3012;
    public static int PREF_String_vThemeCustomization11 = 3013;
    public static int PREF_String_vThemeCustomization12 = 3014;
    public static int PREF_String_vThemeCustomization13 = 3015;
    public static int PREF_String_vThemeCustomization14 = 3016;
    public static int PREF_String_vThemeCustomization15 = 3017;
    public static int PREF_String_vThemeCustomization16start = 3018;
    public static int PREF_String_vThemeCustomization116end = 3118;
    public static int PREF_String_themeCustomizationIndexes = 3119;
    public static int PREF_String_currentExportSettingsExportDir_absolute = 3220;
    public static int PREF_String_currentExportSettings = 3221;
    public static int PREF_String_currentExportSettingsProfile = 3222;
    public static int PREF_String_aspectRatioMode = 3223;
    public static int PREF_String_language = 3224;
    private static int PREF_String_COUNT = 114514;
    private static final AtomicInteger sCurrentThemeId = new AtomicInteger(-1);
    private AtomicIntegerArray prefBool = new AtomicIntegerArray(PREF_Bool_COUNT - offset_Bool);
    private AtomicIntegerArray prefsInt = new AtomicIntegerArray(PREF_Int_COUNT - offset_Int);
    private AtomicReferenceArray<String> prefsString = new AtomicReferenceArray<>(PREF_String_COUNT - offset_String);
    private String defaultFolderString = null;
    private String standartFolderString = null;
    private boolean loaded = false;

    public String getExportPath(Context context) {
        return preferencesGetStringSafe(getPreferences(context), PREF_EXPORT_PATH, DEFAULT_EXPORT_PATH);
    }

    public void setExportPath(Context context, String str) {
        try {
            SharedPreferences.Editor editorEdit = getPreferences(context).edit();
            editorEdit.putString(PREF_EXPORT_PATH, str);
            editorEdit.apply();
        } catch (Exception e) {
        }
    }

    public static void setCurrentThemeId(int i) {
        sCurrentThemeId.set(i);
    }

    public static int getCurrentThemeId() {
        return sCurrentThemeId.get();
    }


    private AppPreferences() {
        setBoolDefault(PREF_Bool_pref_visControlsTimeout, false);
        setBoolDefault(PREF_Bool_visualPreferShowVideoContent, false);
        setBoolDefault(PREF_Bool_fixAssumeMonoOutputFromMonoInput, true);
        setBoolDefault(PREF_Bool_followCurrentState, true);
        setBoolDefault(PREF_Bool_audioMuteState, false);
        setBoolDefault(PREF_Bool_showAlbumArtInstead, true);
        setBoolDefault(PREF_Bool_tipShow_reorder, true);
        setBoolDefault(PREF_Bool_firstLaunch, true);
        setBoolDefault(PREF_Bool_uiSectionOpened0, true);
        setBoolDefault(PREF_Bool_uiSectionOpened1, true);
        setBoolDefault(PREF_Bool_uiSectionOpened00, true);
        setBoolDefault(PREF_Bool_uiSectionOpened01, true);
        setBoolDefault(PREF_Bool_visualizerUseGlobalSession, true);
        setBoolDefault(PREF_Bool_equalizerEnabled, false);
        setIntDefault(PREF_Int_mainPageIndex, 1);
        setIntDefault(PREF_Int_recentlyAddedWeeks, 1);
        setIntDefault(PREF_Int_visualizerThemeId, 14);
        setIntDefault(PREF_Int_lockOrient, 0);
        setIntDefault(PREF_Int_playbackEngine, 1);
        setIntDefault(PREF_Int_videoScalingMode, 1);
        setIntDefault(PREF_Int_SortSelectedRadioOption, 8);
        setIntDefault(PREF_Int_SortMaskCheckOptions, 0);
        setIntDefault(PREF_Int_volumeStereoBalance, 0);
        setIntDefault(PREF_Int_crossFadeValue, -1000);
        setIntDefault(PREF_Int_equalizerPreset, -1);
        setIntDefault(PREF_Int_equalizerBassValue, 0);
        setIntDefault(PREF_Int_equalizerTrebleValue, 0);
        setIntDefault(PREF_Int_virtualizerStrength, 0);
        setIntDefault(PREF_Int_reverbPreset, 0);
        setIntDefault(PREF_Int_containerItemViewMode, 1);
        setIntDefault(PREF_Int_audioVisOffset, 10);
        setIntDefault(PREF_Int_acceptedEula, 0);
        setIntDefault(PREF_Int_aspectRatioWidth, 16);
        setIntDefault(PREF_Int_aspectRatioHeight, 9);
        setStringDefault(PREF_String_currentAbsoluteLibraryAddress, "");
        setStringDefault(PREF_String_equalizerBarsValues, "");
        setStringDefault(PREF_String_currentExportSettingsExportDir_absolute, "");
        setStringDefault(PREF_String_currentExportSettings, "");
        setStringDefault(PREF_String_currentExportSettingsProfile, "");
        setStringDefault(PREF_String_aspectRatioMode, EncodingUtils.DEFAULT_CODEC_NAME);
        setStringDefault(PREF_String_language, "");
    }

    public static AppPreferences createOrGetInstance() {
        AppPreferences appPreferences = instanceWeak.get();
        if (appPreferences == null) {
            synchronized (createInstanceLock) {
                appPreferences = instanceWeak.get();
                if (appPreferences == null) {
                    appPreferences = new AppPreferences();
                    instanceWeak = new WeakReference<>(appPreferences);
                }
            }
        }
        return appPreferences;
    }

    public static boolean preferencesGetBoolSafe(SharedPreferences sharedPreferences, String str, boolean z) {
        try {
            return sharedPreferences.getBoolean(str, z);
        } catch (Exception e) {
            return z;
        }
    }

    public void preferencesSetBoolSafe(Context context, String str, boolean z) {
        preferencesSetBoolSafe(getPreferences(context), str, z);
    }

    public static void preferencesSetBoolSafe(SharedPreferences sharedPreferences, String str, boolean z) {
        try {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putBoolean(str, z);
            editorEdit.apply();
        } catch (Exception e) {
        }
    }

    public static int preferencesGetIntSafe(SharedPreferences sharedPreferences, String str, int i) {
        try {
            return sharedPreferences.getInt(str, i);
        } catch (Exception e) {
            return i;
        }
    }

    public static long preferencesGetLongSafe(SharedPreferences sharedPreferences, String str, long j) {
        try {
            return sharedPreferences.getLong(str, j);
        } catch (Exception e) {
            return j;
        }
    }

    public static float preferencesGetFloatSafe(SharedPreferences sharedPreferences, String str, float f) {
        try {
            return sharedPreferences.getFloat(str, f);
        } catch (Exception e) {
            return f;
        }
    }

    public static String preferencesGetStringSafe(SharedPreferences sharedPreferences, String str, String str2) {
        try {
            return sharedPreferences.getString(str, str2);
        } catch (Exception e) {
            return str2;
        }
    }

    public boolean preferencesGetBoolSafe(Context context, String str, boolean z) {
        return preferencesGetBoolSafe(getPreferences(context), str, z);
    }

    public int preferencesGetIntSafe(Context context, String str, int i) {
        return preferencesGetIntSafe(getPreferences(context), str, i);
    }

    public String preferencesGetStringSafe(Context context, String str, String str2) {
        return preferencesGetStringSafe(getPreferences(context), str, str2);
    }

    public boolean getBool(int i) {
        return this.prefBool.get(i - offset_Bool) != 0;
    }

    public int getInt(int i) {
        return this.prefsInt.get(i - offset_Int);
    }

    public String getString(int i) {
        return this.prefsString.get(i - offset_String);
    }


    public void toggleBool(int i) {
        setBool(i, this.prefBool.get(i - offset_Bool) == 0);
    }

    public void setBool(int i, boolean z) {
        if (z != (this.prefBool.getAndSet(i - offset_Bool, z ? 1 : 0) != 0)) {
            onBoolPreferenceChanged.invoke(Integer.valueOf(i), Boolean.valueOf(z));
        }
    }

    public void setInt(int i, int i2) {
        setInt(i, i2, false);
    }

    public void setInt(int i, int i2, boolean z) {
        int andSet = this.prefsInt.getAndSet(i - offset_Int, i2);
        if (z || i2 != andSet) {
            onIntPreferenceChanged.invoke(Integer.valueOf(i), Integer.valueOf(i2), Boolean.valueOf(z));
        }
    }

    public void setString(int i, String str) {
        setString(i, str, false);
    }

    public void setString(int i, String str, boolean z) {
        String andSet = this.prefsString.getAndSet(i - offset_String, str);
        if (z || !Utils.compareNullEqual(andSet, str)) {
            onStringPreferenceChanged.invoke(Integer.valueOf(i), str, Boolean.valueOf(z));
        }
    }

    public void setBoolDefault(int i, boolean z) {
        this.prefBool.set(i - offset_Bool, z ? 1 : 0);
    }

    public void setIntDefault(int i, int i2) {
        this.prefsInt.set(i - offset_Int, i2);
    }

    public void setStringDefault(int i, String str) {
        this.prefsString.set(i - offset_String, str);
    }

    private void onContext(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        load_pref_visControlsTimeout(defaultSharedPreferences);
        load_pref_visualizerGlobalSession(defaultSharedPreferences);
        load_pref_audioVisOffset(defaultSharedPreferences);
    }

    public void load(Context context) {
        if (!this.loaded) {
            this.loaded = true;
            onContext(context);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            for (int i = 0; i < this.prefBool.length(); i++) {
                try {
                    setBool(offset_Bool + i, preferencesGetBoolSafe(defaultSharedPreferences, "bool" + i, this.prefBool.get(i) != 0));
                } catch (Exception e) {
                }
            }
            for (int i2 = 0; i2 < this.prefsInt.length(); i2++) {
                try {
                    setInt(offset_Int + i2, preferencesGetIntSafe(defaultSharedPreferences, "int" + i2, this.prefsInt.get(i2)));
                } catch (Exception e2) {
                }
            }
            for (int i3 = 0; i3 < this.prefsString.length(); i3++) {
                try {
                    setString(offset_String + i3, preferencesGetStringSafe(defaultSharedPreferences, "string" + i3, this.prefsString.get(i3)));
                } catch (Exception e3) {
                }
            }
        }
    }

    public void save(Context context) {
        if (this.loaded) {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
            for (int i = 0; i < this.prefBool.length(); i++) {
                editorEdit.putBoolean("bool" + i, this.prefBool.get(i) != 0);
            }
            for (int i2 = 0; i2 < this.prefsInt.length(); i2++) {
                editorEdit.putInt("int" + i2, this.prefsInt.get(i2));
            }
            for (int i3 = 0; i3 < this.prefsString.length(); i3++) {
                editorEdit.putString("string" + i3, this.prefsString.get(i3));
            }
            save_pref_visControlsTimeout(editorEdit);
            save_pref_visualizerGlobalSession(editorEdit);
            save_pref_audioVisOffset(editorEdit);
            editorEdit.apply();
        }
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        int iHashCode = str.hashCode();
        if (iHashCode == -546842823) {
            if (str.equals("pref_visualizerGlobalSession")) {
                load_pref_visualizerGlobalSession(sharedPreferences);
            }
        } else if (iHashCode == -373283545) {
            if (str.equals("pref_visControlsTimeout")) {
                load_pref_visControlsTimeout(sharedPreferences);
            }
        } else if (iHashCode == 2099578649) {
            if (str.equals("pref_audioVisOffset")) {
                load_pref_audioVisOffset(sharedPreferences);
            }
        }
        onSharedPreferenceChanged.invoke(str, this);
    }

    void load_pref_visControlsTimeout(SharedPreferences sharedPreferences) {
        setBool(PREF_Bool_pref_visControlsTimeout, preferencesGetBoolSafe(sharedPreferences, "pref_visControlsTimeout", false));
    }

    void save_pref_visControlsTimeout(SharedPreferences.Editor editor) {
        editor.putBoolean("pref_visControlsTimeout", getBool(PREF_Bool_pref_visControlsTimeout));
    }

    void load_pref_visualizerGlobalSession(SharedPreferences sharedPreferences) {
        setBool(PREF_Bool_visualizerUseGlobalSession, preferencesGetBoolSafe(sharedPreferences, "pref_visualizerGlobalSession", true));
    }

    void save_pref_visualizerGlobalSession(SharedPreferences.Editor editor) {
        editor.putBoolean("pref_visualizerGlobalSession", getBool(PREF_Bool_visualizerUseGlobalSession));
    }

    void load_pref_audioVisOffset(SharedPreferences sharedPreferences) {
        setInt(PREF_Int_audioVisOffset, Utils.strToIntSafe(preferencesGetStringSafe(sharedPreferences, "pref_audioVisOffset", "0")));
    }

    void save_pref_audioVisOffset(SharedPreferences.Editor editor) {
        editor.putString("pref_audioVisOffset", new StringBuilder().append(getInt(PREF_Int_audioVisOffset)).toString());
    }



    public SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveAddTokenList(List<String> list, Context context, String str) {
        SharedPreferences preferences = getPreferences(context);
        List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", preferencesGetStringSafe(preferences, str, ""));
        SharedPreferences.Editor editorEdit = preferences.edit();
        editorEdit.putString(str, UtilsSerialize.serializeIterableSkipInvalidWithAdd((CharSequence) ";", (Iterable) listDeserializeIterableAsList, (Iterable) list, true));
        editorEdit.apply();
    }

    public void prefAddLibraryFolderGenerateHash(String str, Context context) {
        String string;
        MultiList<String, String> multiListPrefGetLibraryFolders = prefGetLibraryFolders(context);
        Random random = new Random();
        int i = 0;
        do {
            i++;
            string = new StringBuilder().append(random.nextInt(1000000)).toString();
            if (!multiListPrefGetLibraryFolders.contains1(string)) {
                break;
            }
        } while (i < 1000000);
        prefAddLibraryFolder(string, str, context);
    }

    public void prefAddLibraryFolder(String str, String str2, Context context) {
        if (!str.contains(";") && !str.contains(":") && !str2.contains(";") && !str2.contains(":")) {
            SharedPreferences preferences = getPreferences(context);
            List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", getLibFoldersString(preferences));
            String str3 = String.valueOf(String.valueOf(str)) + ":" + str2;
            SharedPreferences.Editor editorEdit = preferences.edit();
            editorEdit.putString("libFolders", UtilsSerialize.serializeIterableSkipInvalidWithAdd((CharSequence) ";", (Iterable) listDeserializeIterableAsList, (Object) str3, true));
            editorEdit.apply();
        }
    }

    public void prefRemoveLibraryFolder(String str, String str2, Context context) {
        SharedPreferences preferences = getPreferences(context);
        List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", getLibFoldersString(preferences));
        String str3 = String.valueOf(String.valueOf(str)) + ":" + str2;
        SharedPreferences.Editor editorEdit = preferences.edit();
        editorEdit.putString("libFolders", UtilsSerialize.serializeIterableSkipInvalidWithExclude(";", listDeserializeIterableAsList, str3, true));
        editorEdit.apply();
    }

    public MultiList<String, String> prefGetLibraryFolders(Context context) {
        List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", getLibFoldersString(getPreferences(context)));
        MultiList<String, String> multiList = new MultiList<>(listDeserializeIterableAsList.size());
        for (String str : listDeserializeIterableAsList) {
            int iIndexOf = str.indexOf(":");
            if (iIndexOf >= 0) {
                multiList.add(new Tuple2<>(str.substring(0, iIndexOf), str.substring(iIndexOf + 1)));
            }
        }
        return multiList;
    }

    String getStandartFolderString() {
        if (this.standartFolderString == null) {
            StringBuilder sb = new StringBuilder();
            try {
                String internalStoragePath = Context_storage.getInternalStoragePath();
                if (!internalStoragePath.isEmpty()) {
                    sb.append("-02:");
                    sb.append(internalStoragePath);
                    sb.append(";");
                }
            } catch (Exception e) {
            }
            try {
                Context appContext = PlayerCore.s().getAppContext();
                if (appContext != null) {
                    String sDCardPathOnce = Context_storage.getSDCardPathOnce(appContext);
                    if (!sDCardPathOnce.isEmpty()) {
                        sb.append("-01:");
                        sb.append(sDCardPathOnce);
                        sb.append(";");
                    }
                }
            } catch (Exception e2) {
            }
            this.standartFolderString = sb.toString();
        }
        return this.standartFolderString;
    }

    String getDefaultFolderString() {
        if (this.defaultFolderString == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("001:");
            sb.append("/storage");
            sb.append(";");
            try {
                sb.append("002:");
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getCanonicalPath());
                sb.append(";");
            } catch (IOException e) {
            }
            try {
                sb.append("003:");
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getCanonicalPath());
                sb.append(";");
            } catch (IOException e2) {
            }
            try {
                sb.append("004:");
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getCanonicalPath());
                sb.append(";");
            } catch (IOException e3) {
            }
            try {
                sb.append("005:");
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getCanonicalPath());
            } catch (IOException e4) {
            }
            this.defaultFolderString = sb.toString();
        }
        return this.defaultFolderString;
    }

    public String getLibFoldersString(SharedPreferences sharedPreferences) {
        return preferencesGetStringSafe(sharedPreferences, "libFolders", String.valueOf(String.valueOf(getStandartFolderString())) + getDefaultFolderString());
    }

    public void prefAddStandalonePlaylistGenerateHash(Context context, String str, boolean z) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        prefAddStandalonePlaylistGenerateHash(context, arrayList, z);
    }

    public void prefAddStandalonePlaylistGenerateHash(Context context, List<String> list, boolean z) {
        String string;
        MultiList<String, String> multiListPrefGetStandalonePlaylists = prefGetStandalonePlaylists(context);
        ArrayList arrayList = new ArrayList();
        Random random = new Random();
        for (String str : list) {
            if (!z || !multiListPrefGetStandalonePlaylists.contains2(str)) {
                do {
                    string = new StringBuilder().append(random.nextInt(1000000)).toString();
                } while (multiListPrefGetStandalonePlaylists.contains1(string));
                if (!string.contains(";") && !string.contains(":") && !str.contains(";") && !str.contains(":")) {
                    arrayList.add(String.valueOf(String.valueOf(string)) + ":" + str);
                }
            }
        }
        saveAddTokenList(arrayList, context, "libStandalonePlaylists");
    }

    public void prefRemoveStandalonePlaylist(String str, String str2, Context context) {
        SharedPreferences preferences = getPreferences(context);
        List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", preferencesGetStringSafe(preferences, "libStandalonePlaylists", ""));
        String str3 = String.valueOf(String.valueOf(str)) + ":" + str2;
        SharedPreferences.Editor editorEdit = preferences.edit();
        editorEdit.putString("libStandalonePlaylists", UtilsSerialize.serializeIterableSkipInvalidWithExclude(";", listDeserializeIterableAsList, str3, true));
        editorEdit.apply();
    }

    public MultiList<String, String> prefGetStandalonePlaylists(Context context) {
        List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", preferencesGetStringSafe(getPreferences(context), "libStandalonePlaylists", ""));
        MultiList<String, String> multiList = new MultiList<>(listDeserializeIterableAsList.size());
        for (String str : listDeserializeIterableAsList) {
            int iIndexOf = str.indexOf(":");
            if (iIndexOf >= 0) {
                String strSubstring = str.substring(0, iIndexOf);
                String strSubstring2 = str.substring(iIndexOf + 1);
                if (UtilsFileSys.fileExists(strSubstring2)) {
                    multiList.add(new Tuple2<>(strSubstring, strSubstring2));
                }
            }
        }
        return multiList;
    }

    public static int generateVisualizerCustomizationHash(String str) {
        if (str == null) {
            return 0;
        }
        return str.hashCode();
    }

    public List<Integer> getPrefThemeCustomizationIndexes() {
        List<String> listDeserializeIterableAsList = UtilsSerialize.deserializeIterableAsList(";", getString(PREF_String_themeCustomizationIndexes));
        ArrayList arrayList = new ArrayList(listDeserializeIterableAsList.size());
        Iterator<String> it = listDeserializeIterableAsList.iterator();
        while (it.hasNext()) {
            int iStrToIntSafe = Utils.strToIntSafe(it.next(), -1);
            if (iStrToIntSafe >= 0) {
                arrayList.add(Integer.valueOf(iStrToIntSafe));
            }
        }
        return arrayList;
    }

    public void setPrefThemeCustomizationIndexes(List<Integer> list) {
        setString(PREF_String_themeCustomizationIndexes, UtilsSerialize.serializeIterable(";", list));
    }

    private String getPrefThemeCustomization(int i) {
        int iThemeCustomizationIdentifierToPref = themeCustomizationIdentifierToPref(i);
        if (iThemeCustomizationIdentifierToPref < 0) {
            tlog.w("invalid _identifier");
            return null;
        }
        String string = getString(iThemeCustomizationIdentifierToPref);
        if (Crave.iscry(string)) {
            string = Crave.decry(string);
        }
        if (string == null || string.length() <= 0) {
            return null;
        }
        return string;
    }

    public CustomScene getPrefThemeCustomizationData(int i, int[] iArr) {
        String prefThemeCustomization = getPrefThemeCustomization(i);
        if (iArr != null) {
            iArr[0] = generateVisualizerCustomizationHash(null);
        }
        if (prefThemeCustomization == null) {
            return null;
        }
        if (iArr != null) {
            iArr[0] = generateVisualizerCustomizationHash(prefThemeCustomization);
        }
        return CustomScene.deserialize(prefThemeCustomization);
    }

    public void savePrefThemeCustomizationData(int i, CustomScene customScene, boolean z) {
        savePrefThemeCustomization(i, customScene != null ? customScene.serialize() : null, z);
    }

    private void savePrefThemeCustomization(int i, String str, boolean z) {
        int iThemeCustomizationIdentifierToPref = themeCustomizationIdentifierToPref(i);
        if (iThemeCustomizationIdentifierToPref < 0) {
            tlog.w("invalid _identifier");
        } else if (str == null) {
            setString(iThemeCustomizationIdentifierToPref, "", z);
        } else {
            setString(iThemeCustomizationIdentifierToPref, str, z);
        }
    }

    public static int themeCustomizationIdentifierToPref(int i) {
        if (i < 0) {
            return -1;
        }
        return PREF_String_vThemeCustomization16start + i;
    }

    public void resetTips() {
        setBool(PREF_Bool_tipShow_reorder, true);
    }

    public static File GetVisualizerFolderFromIdentifier(int i) {
        return AppSettingsCore.CreateOrGetScenesFolder("visualizer" + i);
    }

    public static File SaveFileToLocal(File file, File file2) {
        File fileGetUniqueFileName;
        if (file2 == null || (fileGetUniqueFileName = UtilsFileSys.GetUniqueFileName(file, file2.getName())) == null) {
            return null;
        }
        try {
            UtilsFileSys.CopyFile(file2, fileGetUniqueFileName);
            return fileGetUniqueFileName;
        } catch (IOException e) {
            return null;
        }
    }

    public static File SaveFileToLocal(File file, InputStream inputStream, String str) {
        File fileGetUniqueFileName;
        if (inputStream == null || (fileGetUniqueFileName = UtilsFileSys.GetUniqueFileName(file, str)) == null) {
            return null;
        }
        try {
            UtilsFileSys.CopyFile(inputStream, fileGetUniqueFileName);
            return fileGetUniqueFileName;
        } catch (IOException e) {
            return null;
        }
    }

    public String getLanguage() {
        return getString(PREF_String_language);
    }

    public void setLanguage(String str) {
        setString(PREF_String_language, str);
    }
}
