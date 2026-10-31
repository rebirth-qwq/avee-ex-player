package com.daaw.avee.Common;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import com.daaw.avee.vendor.os.Environmenu;
import android.os.ParcelFileDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class UtilsFileSys {
    static final char UNIX_SEPARATOR = '/';
    static final char WINDOWS_SEPARATOR = '\\';

    public static String fixToValidFilename(String str) {
        return str.replaceAll("[^a-zA-Z0-9(){}.-]", "_");
    }

    public static String extractFilenameExt(File file) {
        return extractFilenameExt(file.getName());
    }

    public static String extractFilenameExt(String str) {
        if (str == null) {
            return "";
        }
        try {
            return str.substring(str.lastIndexOf(".") + 1).toLowerCase();
        } catch (Exception unused) {
            return "";
        }
    }

    public static String extractFilenameExtWithDot(String str) {
        String strSubstring = "";
        if (str == null) {
            return "";
        }
        try {
            strSubstring = str.substring(str.lastIndexOf("."));
        } catch (Exception unused) {
        }
        return strSubstring.toLowerCase();
    }

    public static String extractFilename(String str) {
        try {
            return str.substring(str.lastIndexOf("/") + 1);
        } catch (Exception unused) {
            return str;
        }
    }

    public static String extractFilenameAdditionalSeperator(String str, char c) {
        try {
            return str.substring(Math.max(str.lastIndexOf(47), Math.max(str.lastIndexOf(92), str.lastIndexOf(c))) + 1);
        } catch (Exception unused) {
            return str;
        }
    }

    public static String extractFilenameWithoutExt(String str) {
        try {
            str = str.substring(str.lastIndexOf("/") + 1);
        } catch (Exception unused) {
        }
        int iLastIndexOf = str.lastIndexOf(".");
        if (iLastIndexOf <= 0) {
            return str;
        }
        try {
            return str.substring(0, iLastIndexOf);
        } catch (Exception unused2) {
            return str;
        }
    }

    public static boolean fileExists(String str) {
        return new File(str).exists();
    }

    public static String getRelativePath(String str, String str2, String str3) {
        String strSeparatorsToWindows;
        String strSeparatorsToWindows2;
        boolean zIsFile;
        if (str3.equals("/")) {
            strSeparatorsToWindows = separatorsToUnix(str);
            strSeparatorsToWindows2 = separatorsToUnix(str2);
        } else if (str3.equals("\\")) {
            strSeparatorsToWindows = separatorsToWindows(str);
            strSeparatorsToWindows2 = separatorsToWindows(str2);
        } else {
            throw new IllegalArgumentException("Unrecognised dir separator '" + str3 + "'");
        }
        String[] strArrSplit = strSeparatorsToWindows2.split(Pattern.quote(str3));
        String[] strArrSplit2 = strSeparatorsToWindows.split(Pattern.quote(str3));
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < strArrSplit2.length && i < strArrSplit.length && strArrSplit2[i].equals(strArrSplit[i])) {
            stringBuffer.append(strArrSplit2[i] + str3);
            i++;
        }
        if (i == 0) {
            throw new PathResolutionException("No common path element found for '" + strSeparatorsToWindows + "' and '" + strSeparatorsToWindows2 + "'");
        }
        File file = new File(strSeparatorsToWindows2);
        if (file.exists()) {
            zIsFile = file.isFile();
        } else {
            zIsFile = !str2.endsWith(str3);
        }
        StringBuilder sb = new StringBuilder();
        if (strArrSplit.length != i) {
            int length = zIsFile ? (strArrSplit.length - i) - 1 : strArrSplit.length - i;
            for (int i2 = 0; i2 < length; i2++) {
                sb.append("..");
                sb.append(str3);
            }
        }
        sb.append(strSeparatorsToWindows.substring(stringBuffer.length()));
        return sb.toString();
    }

    public static String separatorsToUnix(String str) {
        return (str == null || str.indexOf(92) == -1) ? str : str.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
    }

    public static String separatorsToWindows(String str) {
        return (str == null || str.indexOf(47) == -1) ? str : str.replace(UNIX_SEPARATOR, WINDOWS_SEPARATOR);
    }

    public boolean isExternalStorageWritable() {
        return Environmenu.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public boolean isExternalStorageReadable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environmenu.MEDIA_MOUNTED.equals(externalStorageState) || Environmenu.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    static class PathResolutionException extends RuntimeException {
        PathResolutionException(String str) {
            super(str);
        }
    }

    public static FileDescriptor getFilePathFromDocumentUri(Context context, Uri uri) {
        return getFilePathFromDocumentUri(context, uri, "r");
    }

    public static FileDescriptor getFilePathFromDocumentUri(Context context, Uri uri, String str) {
        ParcelFileDescriptor parcelFileDescriptorOpenFileDescriptor;
        try {
            parcelFileDescriptorOpenFileDescriptor = context.getContentResolver().openFileDescriptor(uri, str);
        } catch (Exception unused) {
            parcelFileDescriptorOpenFileDescriptor = null;
        }
        if (parcelFileDescriptorOpenFileDescriptor != null) {
            try {
                return parcelFileDescriptorOpenFileDescriptor.getFileDescriptor();
            } catch (Exception unused2) {
            }
        }
        return null;
    }

    public static void scanFile(Context context, String str) {
        MediaScannerConnection.scanFile(context, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.daaw.avee.Common.UtilsFileSys.1
            @Override // android.media.MediaScannerConnection.OnScanCompletedListener
            public void onScanCompleted(String str2, Uri uri) {
            }
        });
    }

    public static String readInputStreamAsString(InputStream inputStream) {
        try {
            char[] cArr = new char[1024];
            StringBuilder sb = new StringBuilder(100000);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            while (true) {
                int i = inputStreamReader.read(cArr, 0, 1024);
                if (i >= 0) {
                    sb.append(cArr, 0, i);
                } else {
                    return sb.toString();
                }
            }
        } catch (Exception unused) {
            return null;
        }
    }

    public static void deleteCache(Context context) {
        try {
            deleteDir(context.getCacheDir());
        } catch (Exception unused) {
        }
    }

    public static boolean _deleteDir(File file) {
        if (file != null && file.isDirectory()) {
            for (String str : file.list()) {
                if (!deleteDir(new File(file, str))) {
                    return false;
                }
            }
            return file.delete();
        }
        if (file == null || !file.isFile()) {
            return false;
        }
        return file.delete();
    }

    public static boolean deleteDir(File file) {
        if (file != null && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (!deleteDir(file2)) {
                    return false;
                }
            }
            return file.delete();
        }
        if (file == null || !file.isFile()) {
            return false;
        }
        return file.delete();
    }

    public static String readResource(Resources resources, int i) {
        StringBuilder sb = new StringBuilder(128);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(i)));
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    sb.append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return sb.toString();
        }
    }

    public static void CopyFile(File file, File file2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int i = fileInputStream.read(bArr);
                    if (i > 0) {
                        fileOutputStream.write(bArr, 0, i);
                    } else {
                        fileOutputStream.close();
                        fileInputStream.close();
                        return;
                    }
                    try {
                        fileInputStream.close();
                    } catch (Throwable unused) {
                    }
                }
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable unused2) {
                    }
                    throw th3;
                }
            }
        } catch (Throwable th4) {
            throw th4;
        }
    }

    public static void CopyFile(InputStream inputStream, File file) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int i = inputStream.read(bArr);
                    if (i <= 0) {
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, i);
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable unused) {
                        }
                    }
                }
                fileOutputStream.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable unused2) {
                    }
                    throw th3;
                }
            }
        } catch (Throwable th4) {
            throw th4;
        }
    }

    public static File GetUniqueFileName(File file, String str) {
        String str_getUniqueFileName_getFileExtension = _getUniqueFileName_getFileExtension(str);
        String str_getUniqueFileName_getFileName = _getUniqueFileName_getFileName(str);
        File file2 = new File(file, str);
        int i = 0;
        while (file2.exists()) {
            i++;
            file2 = new File(file, str_getUniqueFileName_getFileName + " (" + i + ")" + str_getUniqueFileName_getFileExtension);
        }
        return file2;
    }

    public static String _getUniqueFileName_getFileExtension(String str) {
        if (str == null || str.lastIndexOf(46) == -1) {
            return null;
        }
        return str.substring(str.lastIndexOf(46));
    }

    public static String _getUniqueFileName_getFileName(String str) {
        int iLastIndexOf = str.lastIndexOf(46);
        return iLastIndexOf >= 0 ? str.substring(0, iLastIndexOf) : str;
    }
}
