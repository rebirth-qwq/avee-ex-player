package com.daaw.avee.Design;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.NotificationCompat;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.EventsGlobal.EventsGlobalTextNotifier;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.VisualizerThemeInfo;
import com.daaw.avee.comp.VisualUI.VisualizerChooseDialog;
import com.daaw.avee.comp.Visualizer.CustomScene;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class VisualizerSharingDesign extends DesignBase {
    public static final String PackedContent_SceneFileName = "scene.json";
    public static final String StringContent_FileExtWithDotAlt = ".json";
    WeakReference<List<Integer>> currentTemplateIndexes = new WeakReference<>(null);
    final String StringContent_FileExtWithDot = ".txt";
    final String PackedContent_FileExtWithDot = ".zip";
    final String PackedContent_FileExtWithDotAlt = ".viz";
    final long maxSizeOfContentEntry = 100000000;

    class AnonymousClass4 implements WeakEvent2.Handler<VisualizerChooseDialog, List<Integer>> {
        AnonymousClass4() {
        }

        @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
        public void invoke(VisualizerChooseDialog visualizerChooseDialog, List<Integer> list) {
            try {
                InputStream inputStreamOpenRawResource = visualizerChooseDialog.getActivity().getResources().openRawResource(R.raw.creatnew);
                String inputStreamAsString = UtilsFileSys.readInputStreamAsString(inputStreamOpenRawResource);
                inputStreamOpenRawResource.close();
                CustomScene customSceneDeserialize = CustomScene.deserialize(inputStreamAsString);
                if (customSceneDeserialize != null) {
                    VisualizerDesign.addNewVisualizer(customSceneDeserialize, AppPreferences.createOrGetInstance().getPrefThemeCustomizationIndexes(), -1);
                    visualizerChooseDialog.requestUpdateVisualizerTemplates();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean DetectVizFileTypeByFileSignature(ContentResolver contentResolver, Uri uri) {
        int i;
        byte[] bArr = new byte[2];
        try {
            InputStream inputStreamOpenInputStream = contentResolver.openInputStream(uri);
            i = inputStreamOpenInputStream != null ? inputStreamOpenInputStream.read(bArr) : 0;
            if (inputStreamOpenInputStream != null) {
                inputStreamOpenInputStream.close();
            }
        } catch (Exception e) {
            i = 0;
        }
        return i == 2 && bArr[0] == 80 && bArr[1] == 75;
    }

    public VisualizerSharingDesign() {
        VisualizerChooseDialog.onSaveToFileVisTemplateAction.subscribeWeak(new WeakEvent2.Handler<VisualizerChooseDialog, VisualizerThemeInfo>() { // from class: com.daaw.avee.Design.VisualizerSharingDesign.1
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(VisualizerChooseDialog visualizerChooseDialog, VisualizerThemeInfo visualizerThemeInfo) {
                if (visualizerThemeInfo != null) {
                    Activity activity = visualizerChooseDialog.getActivity();
                    CustomScene prefThemeCustomizationDataOrTemplateData = VisualizerDesign.getPrefThemeCustomizationDataOrTemplateData(visualizerThemeInfo.id, null);
                    if (prefThemeCustomizationDataOrTemplateData != null) {
                        String strSerializePretty = prefThemeCustomizationDataOrTemplateData.serializePretty();
                        String str = Environment.DIRECTORY_DOWNLOADS;
                        String string = visualizerChooseDialog.getTextCustomVizName().getText().toString();
                        if (string.isEmpty()) {
                            string = "visualizer_" + visualizerThemeInfo.id;
                        }
                        File fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(visualizerThemeInfo.id);
                        String strSavePackedFileToPublicDirectory = VisualizerSharingDesign.this.SavePackedFileToPublicDirectory(visualizerChooseDialog.getActivity(), str, string, ".viz", strSerializePretty, Arrays.asList(fileGetVisualizerFolderFromIdentifier != null ? fileGetVisualizerFolderFromIdentifier.listFiles() : new File[0]));
                        UtilsFileSys.scanFile(activity, strSavePackedFileToPublicDirectory);
                        String strExtractFilename = UtilsFileSys.extractFilename(strSavePackedFileToPublicDirectory);
                        String exportPath = AppPreferences.createOrGetInstance().getExportPath(activity);
                        if (exportPath.endsWith("/")) {
                            exportPath = exportPath.substring(0, exportPath.length() - 1);
                        }
                        if (exportPath.startsWith("/storage/emulated/0/")) {
                            exportPath = exportPath.substring("/storage/emulated/0/".length());
                        }
                        String str2 = String.format(MainActivity.getInstance().getString(R.string.saved_to_template_prefix), strExtractFilename, exportPath);
                        VisualizerExporterDesign.showFileSavedNotification(2, activity, str2, Uri.parse(strSavePackedFileToPublicDirectory), strExtractFilename, "text/plain");
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.parse(strSavePackedFileToPublicDirectory), "text/plain");
                        PendingIntent activity2 = PendingIntent.getActivity(activity, 0, intent, 0);
                        NotificationManager notificationManager = (NotificationManager) activity.getSystemService("notification");
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
                        builder.setContentTitle(strExtractFilename).setContentText(str2).setSmallIcon(R.drawable.ic_save5).setContentIntent(activity2).setLocalOnly(true).setAutoCancel(true);
                        notificationManager.notify(2, builder.build());
                        EventsGlobalTextNotifier.onTextMsg.invoke(str2);
                    }
                }
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onLoadFromFileVisTemplateAction.subscribeWeak(new WeakEvent2.Handler<VisualizerChooseDialog, List<Integer>>() { // from class: com.daaw.avee.Design.VisualizerSharingDesign.2
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(VisualizerChooseDialog visualizerChooseDialog, List<Integer> list) {
                VisualizerSharingDesign.this.currentTemplateIndexes = new WeakReference<>(list);
                int i = Build.VERSION.SDK_INT;
                Intent intent = new Intent();
                intent.addCategory("android.intent.category.OPENABLE");
                Intent intentPutExtra = intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                intentPutExtra.setType("*/*");
                intentPutExtra.setAction("android.intent.action.GET_CONTENT");
                visualizerChooseDialog.startActivityForResult(Intent.createChooser(intentPutExtra, visualizerChooseDialog.getResources().getString(R.string.vis_action_loadFromFile_title)), 64);
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onNewVisTemplateAction.subscribeWeak(new AnonymousClass4(), this.listenerRefHolder);
        VisualizerChooseDialog.onActivityResult.subscribeWeak(new WeakEvent3.Handler<VisualizerChooseDialog, Integer, Intent>() { // from class: com.daaw.avee.Design.VisualizerSharingDesign.3
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(VisualizerChooseDialog visualizerChooseDialog, Integer num, Intent intent) {
                Activity activity;
                List<Integer> list = VisualizerSharingDesign.this.currentTemplateIndexes.get();
                if (list == null || (activity = visualizerChooseDialog.getActivity()) == null) {
                    return;
                }
                int iIntValue = num.intValue();
                if (iIntValue == 64 || iIntValue == 65) {
                    if (intent == null) {
                        tlog.w("PICK_IMAGE_PROPERTY_REQUEST_CODE invalid data");
                        return;
                    }
                    ClipData clipData = intent.getClipData();
                    if (clipData == null) {
                        VisualizerSharingDesign.this.onVisLoadFilePicked(activity, intent.getData(), list);
                        return;
                    }
                    int itemCount = clipData.getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        if (uri != null) {
                            try {
                                VisualizerSharingDesign.this.onVisLoadFilePicked(activity, uri, list);
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, this.listenerRefHolder);
    }

    public static int AddNewVisualizerFromFile(Context context, Uri uri) {
        return onVisLoadFilePickedAsPackedContent(context, uri, AppPreferences.createOrGetInstance().getPrefThemeCustomizationIndexes());
    }

    public void onVisLoadFilePicked(Context context, Uri uri, List<Integer> list) {
        String path = uri.getPath();
        if (path == null) {
            path = "";
        }
        int iLastIndexOf = path.lastIndexOf(".zip");
        int iLastIndexOf2 = path.lastIndexOf(".viz");
        if (iLastIndexOf >= 0 && iLastIndexOf2 >= 0 && DetectVizFileTypeByFileSignature(context.getContentResolver(), uri)) {
            onVisLoadFilePickedAsStringContent(context, uri, list);
        } else {
            onVisLoadFilePickedAsPackedContent(context, uri, list);
        }
    }

    private void onVisLoadFilePickedAsStringContent(Context context, Uri uri, List<Integer> list) {
        if (uri != null) {
            InputStream inputStream = null;
            try {
                InputStream inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
                String inputStreamAsString = UtilsFileSys.readInputStreamAsString(inputStreamOpenInputStream);
                if (inputStreamAsString != null) {
                    CustomScene customSceneDeserialize = CustomScene.deserialize(inputStreamAsString);
                    if (customSceneDeserialize != null && customSceneDeserialize.getEntryCount() > 0) {
                        VisualizerDesign.addNewVisualizer(customSceneDeserialize, list, -1);
                    } else if (DetectVizFileTypeByFileSignature(context.getContentResolver(), uri)) {
                        onVisLoadFilePickedAsPackedContent(context, uri, list);
                    }
                }
                if (inputStreamOpenInputStream != null) {
                    try {
                        inputStreamOpenInputStream.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0037 A[Catch: IOException -> 0x0093, TRY_LEAVE, TryCatch #2 {IOException -> 0x0093, blocks: (B:15:0x0032, B:17:0x0037), top: B:63:0x0032 }] */
    /* JADX WARN: Code duplicated, block: B:40:0x0070 A[Catch: IOException -> 0x0081, TRY_LEAVE, TryCatch #5 {IOException -> 0x0081, blocks: (B:38:0x006b, B:40:0x0070), top: B:67:0x006b }] */
    private static int onVisLoadFilePickedAsPackedContent(Context context, Uri uri, List<Integer> list) {
        ZipInputStream zipInputStream;
        InputStream inputStreamOpenInputStream;
        int i = -1;
        if (uri == null) {
            return -1;
        }
        byte[] bArr = new byte[1024];
        try {
            inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
            try {
                zipInputStream = new ZipInputStream(inputStreamOpenInputStream);
                try {
                    try {
                        int iAddNewVisualizer = VisualizerDesign.addNewVisualizer(CustomScene.deserialize(""), list, -1);
                        if (iAddNewVisualizer < 0) {
                            if (zipInputStream != null) {
                                try {
                                    zipInputStream.close();
                                } catch (IOException e) {
                                    return -1;
                                }
                            }
                            if (inputStreamOpenInputStream == null) {
                                return -1;
                            }
                            inputStreamOpenInputStream.close();
                            return -1;
                        }
                        try {
                            File fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(iAddNewVisualizer);
                            while (true) {
                                ZipEntry nextEntry = zipInputStream.getNextEntry();
                                if (nextEntry == null) {
                                    break;
                                }
                                FileOutputStream fileOutputStream = new FileOutputStream(new File(fileGetVisualizerFolderFromIdentifier, nextEntry.getName()));
                                while (true) {
                                    int i2 = zipInputStream.read(bArr);
                                    if (i2 <= 0) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, i2);
                                }
                                fileOutputStream.close();
                            }
                            Fix_TransferCustomizationDataFromLocalFile(iAddNewVisualizer);
                            if (zipInputStream != null) {
                                try {
                                    zipInputStream.close();
                                    if (inputStreamOpenInputStream != null) {
                                        inputStreamOpenInputStream.close();
                                    }
                                } catch (IOException e2) {
                                }
                            } else if (inputStreamOpenInputStream != null) {
                                inputStreamOpenInputStream.close();
                            }
                            return iAddNewVisualizer;
                        } catch (IOException e3) {
                            i = iAddNewVisualizer;
                            if (zipInputStream != null) {
                                try {
                                    zipInputStream.close();
                                } catch (IOException e4) {
                                    return i;
                                }
                            }
                            if (inputStreamOpenInputStream == null) {
                                return i;
                            }
                            inputStreamOpenInputStream.close();
                            return i;
                        }
                    } catch (IOException e5) {
                    }
                } catch (Throwable th) {
                    if (zipInputStream != null) {
                        try {
                            zipInputStream.close();
                            if (inputStreamOpenInputStream != null) {
                                inputStreamOpenInputStream.close();
                            }
                        } catch (IOException e6) {
                            throw th;
                        }
                    } else if (inputStreamOpenInputStream != null) {
                        inputStreamOpenInputStream.close();
                    }
                    throw th;
                }
            } catch (IOException e7) {
                zipInputStream = null;
            } catch (Throwable th2) {
                zipInputStream = null;
            }
        } catch (IOException e8) {
            zipInputStream = null;
            inputStreamOpenInputStream = null;
        } catch (Throwable th3) {
            zipInputStream = null;
            inputStreamOpenInputStream = null;
        }
        return 0;
    }

    static void Fix_TransferCustomizationDataFromLocalFile(int i) throws Throwable {
        File fileGetVisualizerFolderFromIdentifier;
        File[] fileArrListFiles;
        Throwable th;
        FileInputStream fileInputStream;
        String inputStreamAsString = null;
        inputStreamAsString = null;
        inputStreamAsString = null;
        FileInputStream fileInputStream2 = null;
        if (i < 0 || (fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(i)) == null || (fileArrListFiles = fileGetVisualizerFolderFromIdentifier.listFiles()) == null) {
            return;
        }
        int length = fileArrListFiles.length;
        int i2 = 0;
        File file = null;
        File file2 = null;
        while (i2 < length) {
            File file3 = fileArrListFiles[i2];
            if (StringContent_FileExtWithDotAlt.equals(UtilsFileSys.extractFilenameExtWithDot(file3.getName()))) {
                file = file3;
            }
            if (!PackedContent_SceneFileName.equals(file3.getName())) {
                file3 = file2;
            }
            i2++;
            file2 = file3;
        }
        if (file2 != null) {
            file = file2;
        }
        if (file != null) {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    inputStreamAsString = UtilsFileSys.readInputStreamAsString(fileInputStream);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                        }
                    }
                    try {
                        file.delete();
                    } catch (Exception e2) {
                    }
                } catch (Exception e3) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                    try {
                        file.delete();
                    } catch (Exception e5) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream2 = fileInputStream;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e6) {
                        }
                    }
                    try {
                        file.delete();
                        throw th;
                    } catch (Exception e7) {
                        throw th;
                    }
                }
            } catch (IOException e8) {
                fileInputStream = null;
            } catch (Throwable th3) {
                th = th3;
            }
        }
        if (inputStreamAsString != null) {
            AppPreferences.createOrGetInstance().savePrefThemeCustomizationData(i, CustomScene.deserialize(inputStreamAsString), false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [int] */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8, types: [java.io.FileOutputStream] */
    private String SaveFileToPublicDirectory(Context context, String str, String str2, String str3, String str4) throws Throwable {
        File file;
        Throwable th;
        FileOutputStream r2 = null;
        String absolutePath = null;
        int retryCount = 0;
        int i = 0;
        File fileCreateFileToPublicDirectory = null;
        while (true) {
            if (retryCount < 100) {
                fileCreateFileToPublicDirectory = i == 0 ? createFileToPublicDirectory(context, str, String.valueOf(str2) + str3) : createFileToPublicDirectory(context, str, String.valueOf(str2) + "(" + i + ")" + str3);
                if (fileCreateFileToPublicDirectory != null && !fileCreateFileToPublicDirectory.exists()) {
                    file = fileCreateFileToPublicDirectory;
                    break;
                }
                i++;
                retryCount++;
            } else {
                file = fileCreateFileToPublicDirectory;
                break;
            }
        }
        try {
            if (file != null) {
                try {
                    r2 = new FileOutputStream(file);
                    try {
                        r2.write(str4.getBytes());
                        r2.flush();
                        absolutePath = file.getAbsolutePath();
                        if (r2 != null) {
                            try {
                                r2.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        absolutePath = file.getAbsolutePath();
                        if (r2 != null) {
                            try {
                                r2.close();
                            } catch (IOException e3) {
                            }
                        }
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                    r2 = null;
                } catch (Throwable th2) {
                    th = th2;
                    if (r2 != null) {
                        try {
                            r2.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            }
            return absolutePath;
        } catch (Throwable th3) {
            th = th3;
        }
        return absolutePath;
    }

    private File createFileToPublicDirectory(Context context, String str, String str2) {
        try {
            String exportPath = AppPreferences.createOrGetInstance().getExportPath(context);
            File file = new File(String.valueOf(!exportPath.endsWith("/") ? String.valueOf(exportPath) + "/" : exportPath) + UtilsFileSys.fixToValidFilename(str2));
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.isDirectory()) {
                parentFile.mkdirs();
                return file;
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                File file2 = new File("/storage/emulated/0/Avee/Template/EX/" + UtilsFileSys.fixToValidFilename(str2));
                File parentFile2 = file2.getParentFile();
                if (parentFile2 != null && !parentFile2.isDirectory()) {
                    parentFile2.mkdirs();
                    return file2;
                }
                return file2;
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public static boolean ensureExportDirectoryExists(Context context) {
        try {
            File file = new File(AppPreferences.createOrGetInstance().getExportPath(context));
            if (!file.exists()) {
                return file.mkdirs();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createFile(DialogFragment dialogFragment, String str, String str2) {
        Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType(str);
        intent.putExtra("android.intent.extra.TITLE", str2);
        dialogFragment.startActivityForResult(intent, 63);
    }

    /* JADX WARN: Code duplicated, block: B:75:0x00c2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:77:0x00bd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:94:? A[SYNTHETIC] */
    public String SavePackedFileToPublicDirectory(Context context, String str, String str2, String str3, String str4, List<File> list) {
        File file;
        ZipOutputStream zipOutputStream;
        FileOutputStream fileOutputStream;
        String absolutePath = null;
        int i = 0;
        int i2 = 0;
        File fileCreateFileToPublicDirectory = null;
        while (true) {
            if (i >= 100) {
                file = fileCreateFileToPublicDirectory;
                break;
            }
            fileCreateFileToPublicDirectory = i2 == 0 ? createFileToPublicDirectory(context, str, String.valueOf(str2) + str3) : createFileToPublicDirectory(context, str, String.valueOf(str2) + "(" + i2 + ")" + str3);
            if (fileCreateFileToPublicDirectory != null && !fileCreateFileToPublicDirectory.exists()) {
                file = fileCreateFileToPublicDirectory;
                break;
            }
            i++;
            i2++;
        }
        if (file != null) {
            try {
                fileOutputStream = new FileOutputStream(file);
                try {
                    zipOutputStream = new ZipOutputStream(fileOutputStream);
                    try {
                        try {
                            addToZipFile(str4, PackedContent_SceneFileName, zipOutputStream);
                            for (File file2 : list) {
                                if (!PackedContent_SceneFileName.equals(file2.getName())) {
                                    addToZipFile(file2, zipOutputStream);
                                }
                            }
                            absolutePath = file.getAbsolutePath();
                            if (zipOutputStream != null) {
                                try {
                                    zipOutputStream.close();
                                } catch (IOException e) {
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (IOException e2) {
                                }
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            absolutePath = file.getAbsolutePath();
                            if (zipOutputStream != null) {
                                try {
                                    zipOutputStream.close();
                                } catch (IOException e4) {
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (IOException e5) {
                                }
                            }
                        }
                    } catch (Throwable th) {
                        if (zipOutputStream != null) {
                            try {
                                zipOutputStream.close();
                            } catch (IOException e6) {
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                throw th;
                            } catch (IOException e7) {
                                throw th;
                            }
                        }
                        throw th;
                    }
                } catch (Exception e8) {
                    zipOutputStream = null;
                } catch (Throwable th2) {
                    zipOutputStream = null;
                    if (zipOutputStream != null) {
                        zipOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        throw th2;
                    }
                    throw th2;
                }
            } catch (Exception e9) {
                zipOutputStream = null;
                fileOutputStream = null;
            } catch (Throwable th3) {
                zipOutputStream = null;
                fileOutputStream = null;
            }
        }
        return absolutePath;
    }

    public static void addToZipFile(String str, String str2, ZipOutputStream zipOutputStream) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(str2));
        zipOutputStream.write(str.getBytes());
        zipOutputStream.closeEntry();
    }

    public static void addToZipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        byte[] bArr = new byte[1024];
        while (true) {
            int i = fileInputStream.read(bArr);
            if (i >= 0) {
                zipOutputStream.write(bArr, 0, i);
            } else {
                zipOutputStream.closeEntry();
                fileInputStream.close();
                return;
            }
        }
    }
}
