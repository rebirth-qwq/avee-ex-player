package com.daaw.avee.vendor.aosp;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class Context_storage {
    static String internalPath;
    private static String[] physicalPaths = {"/storage/sdcard1", "/storage/extsdcard", "/storage/sdcard0/external_sdcard", "/mnt/extsdcard", "/mnt/sdcard/external_sd", "/mnt/external_sd", "/mnt/media_rw/sdcard1", "/removable/microsd", "/mnt/emmc", "/storage/external_SD", "/storage/ext_sd", "/storage/removable/sdcard1", "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", "/sdcard2", "/storage/usbdisk0", "/storage/usbdisk1", "/storage/usbdisk2"};
    static String sdCardPath;

    public static String getPathFolderName(String str, Context context) {
        if (str == null) {
            return "<unnamed>";
        }
        if (context != null) {
            if (str.equals(getInternalStoragePathOnce())) {
                return context.getString(R.string.directory_internal);
            }
            if (str.equals(getSDCardPathOnce(context))) {
                return context.getString(R.string.directory_sdcard);
            }
        }
        try {
            return str.substring(str.lastIndexOf("/") + 1);
        } catch (Exception unused) {
            return str;
        }
    }

    static boolean isMarshmallowPlus() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static String getInternalStoragePathOnce() {
        if (internalPath == null) {
            internalPath = getInternalStoragePath();
        }
        return internalPath;
    }

    public static String getInternalStoragePath() {
        return trimEnd(Environment.getExternalStorageDirectory().getAbsolutePath(), '/');
    }

    public static String trimEnd(String str, char c) {
        int length = str.length();
        while (length > 0 && str.charAt(length - 1) == c) {
            length--;
        }
        return str.substring(0, length);
    }

    public static String getSDCardPathOnce(Context context) {
        if (sdCardPath == null) {
            sdCardPath = getSDCardPath(context);
        }
        return sdCardPath;
    }

    private static String getSDCardPath(Context context) {
        List<String> storageDirectories = getStorageDirectories(context);
        String lowerCase = getInternalStoragePath().toLowerCase();
        ArrayList<String> arrayList = new ArrayList(storageDirectories.size());
        Iterator<String> it = storageDirectories.iterator();
        while (it.hasNext()) {
            String lowerCase2 = trimEnd(it.next(), '/').toLowerCase();
            if (!lowerCase.equals(lowerCase2)) {
                arrayList.add(lowerCase2);
            }
        }
        List listAsList = Arrays.asList(physicalPaths);
        String str = "";
        for (String str2 : arrayList) {
            if (!listAsList.contains(str2)) {
                str = str2;
            }
        }
        if (trimEnd(str, '/').isEmpty()) {
            File file = new File("/storage/sdcard1");
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            if (arrayList.size() > 0) {
                str = (String) arrayList.get(0);
            }
        }
        return trimEnd(str, '/');
    }

    public static boolean hasExternalSDCard(Context context) {
        if (sdCardPath == null) {
            sdCardPath = getSDCardPath(context);
        }
        return !sdCardPath.isEmpty();
    }

    static List<String> getStorageDirectories(Context context) {
        ArrayList arrayList = new ArrayList();
        String str = System.getenv("EXTERNAL_STORAGE");
        String str2 = System.getenv("SECONDARY_STORAGE");
        String str3 = System.getenv("EMULATED_STORAGE_TARGET");
        if (TextUtils.isEmpty(str3)) {
            if (isMarshmallowPlus()) {
                getExtSdCardPathsForActivity(context, arrayList);
            } else if (TextUtils.isEmpty(str)) {
                Collections.addAll(arrayList, physicalPaths);
            } else {
                arrayList.add(str);
            }
        } else {
            String[] strArrSplit = Pattern.compile("/").split(Environment.getExternalStorageDirectory().getAbsolutePath());
            boolean z = true;
            String str4 = strArrSplit[strArrSplit.length - 1];
            try {
                Integer.valueOf(str4);
            } catch (NumberFormatException unused) {
                z = false;
            }
            if (!z) {
                str4 = "";
            }
            if (TextUtils.isEmpty(str4)) {
                arrayList.add(str3);
            } else {
                arrayList.add(str3 + File.separator + str4);
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            Collections.addAll(arrayList, str2.split(File.pathSeparator));
        }
        return arrayList;
    }

    public static void getExtSdCardPathsForActivity(Context context, List<String> list) {
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null) {
                int iLastIndexOf = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (iLastIndexOf < 0) {
                    Log.w("", "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String strSubstring = file.getAbsolutePath().substring(0, iLastIndexOf);
                    try {
                        strSubstring = new File(strSubstring).getCanonicalPath();
                    } catch (IOException unused) {
                    }
                    list.add(strSubstring);
                }
            }
        }
        if (list.isEmpty()) {
            list.add("/storage/sdcard1");
        }
    }

    private static void addAllArray(List<String> list, String[] strArr) {
        for (String str : strArr) {
            list.add(str);
        }
    }
}
