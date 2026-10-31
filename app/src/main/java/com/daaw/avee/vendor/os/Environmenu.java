package com.daaw.avee.vendor.os;

import android.annotation.TargetApi;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public class Environmenu {
    public static String DIRECTORY_ALARMS = null;
    public static String DIRECTORY_DCIM = null;
    public static String DIRECTORY_DOCUMENTS = null;
    public static String DIRECTORY_DOWNLOADS = null;
    public static String DIRECTORY_MOVIES = null;
    public static String DIRECTORY_MUSIC = null;
    public static String DIRECTORY_NOTIFICATIONS = null;
    public static String DIRECTORY_PICTURES = null;
    public static String DIRECTORY_PODCASTS = null;
    public static String DIRECTORY_RINGTONES = null;
    public static final String MEDIA_BAD_REMOVAL = "bad_removal";
    public static final String MEDIA_CHECKING = "checking";
    public static final String MEDIA_EJECTING = "ejecting";
    public static final String MEDIA_MOUNTED = "mounted";
    public static final String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";
    public static final String MEDIA_NOFS = "nofs";
    public static final String MEDIA_REMOVED = "removed";
    public static final String MEDIA_SHARED = "shared";
    public static final String MEDIA_UNKNOWN = "unknown";
    public static final String MEDIA_UNMOUNTABLE = "unmountable";
    public static final String MEDIA_UNMOUNTED = "unmounted";
    public static final String[] STANDARD_DIRECTORIES;
    private static final String TAG = Environmenu.class.getSimpleName();
    private static String externalStorageEncapsulationName;
    private static Set<File> sExistingDirectories;

    static {
        externalStorageEncapsulationName = "//////////////////////////////////////////////////";
        Log.i(TAG, "static initializer; externalStorageEncapsulationName (1): " + externalStorageEncapsulationName);
        int pos = -1;
        for (int n = 0; n < externalStorageEncapsulationName.length(); n++) {
            if (externalStorageEncapsulationName.charAt(n) != '/') {
                pos = n;
                break;
            }
        }
        if (pos != -1) {
            externalStorageEncapsulationName = externalStorageEncapsulationName.substring(pos);
        } else {
            externalStorageEncapsulationName = null;
        }
        Log.i(TAG, "static initializer; externalStorageEncapsulationName (2): " + externalStorageEncapsulationName);
        sExistingDirectories = new HashSet();
        DIRECTORY_MUSIC = "Music";
        DIRECTORY_PODCASTS = "Podcasts";
        DIRECTORY_RINGTONES = "Ringtones";
        DIRECTORY_ALARMS = "Alarms";
        DIRECTORY_NOTIFICATIONS = "Notifications";
        DIRECTORY_PICTURES = "Pictures";
        DIRECTORY_MOVIES = "Movies";
        DIRECTORY_DOWNLOADS = "Download";
        DIRECTORY_DCIM = "DCIM";
        DIRECTORY_DOCUMENTS = "Documents";
        STANDARD_DIRECTORIES = new String[]{DIRECTORY_MUSIC, DIRECTORY_PODCASTS, DIRECTORY_RINGTONES, DIRECTORY_ALARMS, DIRECTORY_NOTIFICATIONS, DIRECTORY_PICTURES, DIRECTORY_MOVIES, DIRECTORY_DOWNLOADS, DIRECTORY_DCIM, DIRECTORY_DOCUMENTS};
    }

    public static boolean isStandardDirectory(String dir) {
        for (String valid : STANDARD_DIRECTORIES) {
            if (valid.equals(dir)) {
                return true;
            }
        }
        return false;
    }

    public static File getDataDirectory() {
        return Environment.getDataDirectory();
    }

    public static File getDownloadCacheDirectory() {
        return Environment.getDownloadCacheDirectory();
    }

    public static File getExternalStorageDirectory() {
        File file = Environment.getExternalStorageDirectory();
        synchronized (Environmenu.class) {
            if (externalStorageEncapsulationName != null) {
                file = new File(file, externalStorageEncapsulationName);
                if (!sExistingDirectories.contains(file)) {
                    Log.i(TAG, "getExternalStorageDirectory; file: " + file);
                    if ((file.exists() && file.isDirectory()) || file.mkdirs()) {
                        sExistingDirectories.add(file);
                    } else {
                        Log.i(TAG, "getExternalStorageDirectory; mkdirs() failed");
                    }
                }
            } else {
                Log.i(TAG, "getExternalStorageDirectory; externalStorageEncapsulationName == null");
            }
        }
        return file;
    }

    public static File getExternalStoragePublicDirectory(String type) {
        File directory = Environment.getExternalStoragePublicDirectory(type);
        Log.i(TAG, "getExternalStoragePublicDirectory; type: " + type + ", directory: " + directory);
        File newDirectory = new File(getExternalStorageDirectory(), directory.getName());
        Log.i(TAG, "getExternalStoragePublicDirectory; newDirectory: " + newDirectory);
        return newDirectory;
    }

    public static String getExternalStorageState() {
        return Environment.getExternalStorageState();
    }

    @TargetApi(21)
    public static String getExternalStorageState(File path) {
        return Environment.getExternalStorageState(path);
    }

    public static File getRootDirectory() {
        return Environment.getRootDirectory();
    }

    @TargetApi(19)
    public static String getStorageState(File path) {
        return Environment.getStorageState(path);
    }

    public static boolean isExternalStorageEmulated() {
        return Environment.isExternalStorageEmulated();
    }

    @TargetApi(21)
    public static boolean isExternalStorageEmulated(File path) {
        return Environment.isExternalStorageEmulated(path);
    }

    public static boolean isExternalStorageRemovable() {
        return Environment.isExternalStorageRemovable();
    }

    @TargetApi(21)
    public static boolean isExternalStorageRemovable(File path) {
        return Environment.isExternalStorageRemovable(path);
    }
}
