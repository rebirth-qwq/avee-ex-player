package com.daaw.avee.Design;

import android.view.View;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR2;
import com.daaw.avee.Common.Events.WeakEventR3;
import com.daaw.avee.Common.MultiList;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.ContextData;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.IGeneralItemContainerIdentifier;
import com.daaw.avee.comp.LibraryQueueUI.Dialog.SortDialog;
import com.daaw.avee.comp.LibraryQueueUI.Fragment0;
import com.daaw.avee.comp.LibraryQueueUI.LibraryQueueFragmentBase;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class SortDesign {
    public static final String SortDialogArg_Files = "files";
    public static final String SortDialogArg_Regular = "regular";
    public static final String SortDialogArg_Stations = "stations";
    public static final int Sort_Descending = 1;
    public static final int Sort_Mode_Album = 2;
    public static final int Sort_Mode_Artist = 1;
    public static final int Sort_Mode_Bitrate = 9;
    public static final int Sort_Mode_DateAdded = 4;
    public static final int Sort_Mode_DateModified = 5;
    public static final int Sort_Mode_Default = 8;
    public static final int Sort_Mode_Duration = 6;
    public static final int Sort_Mode_Path = 3;
    public static final int Sort_Mode_Size = 7;
    public static final int Sort_Mode_Title = 0;
    public static final int Sort_Mode_Unsorted = 10;
    public static final Tuple2<Integer, String> Sort_Mode_Default_ = new Tuple2<>(8, "Default");
    public static final Tuple2<Integer, String> Sort_Mode_Title_ = new Tuple2<>(0, "Title/Name");
    public static final Tuple2<Integer, String> Sort_Mode_Path_ = new Tuple2<>(3, "Path/Url");
    public static final Tuple2<Integer, String> Sort_Mode_Bitrate_ = new Tuple2<>(9, "Bitrate");
    public static final Tuple2<Integer, String> Sort_Mode_Unsorted_ = new Tuple2<>(10, "Unsorted");
    private MultiList<Integer, String> radioOptions = new MultiList<>();
    private MultiList<Integer, String> checkOptions = new MultiList<>();
    private List<Object> listenerRefHolder = new LinkedList();

    public static class SortDesc {
        public boolean sortDescending;
        public int sortModeIndex;
    }

    public SortDesign() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            this.radioOptions.add(8, mainActivity.getString(R.string.sort_default));
            this.radioOptions.add(0, mainActivity.getString(R.string.sort_title));
            this.radioOptions.add(1, mainActivity.getString(R.string.sort_artist));
            this.radioOptions.add(2, mainActivity.getString(R.string.sort_album));
            this.radioOptions.add(3, mainActivity.getString(R.string.sort_file_path));
            this.radioOptions.add(4, mainActivity.getString(R.string.sort_add_time));
            this.radioOptions.add(5, mainActivity.getString(R.string.sort_edit_time));
            this.radioOptions.add(6, mainActivity.getString(R.string.sort_duration));
            this.radioOptions.add(7, mainActivity.getString(R.string.sort_file_size));
            this.checkOptions.add(1, mainActivity.getString(R.string.sort_reverse));
        }
        LibraryQueueFragmentBase.onActionChooseSort.subscribeWeak(new WeakEvent2.Handler<ContextData, View>() { // from class: com.daaw.avee.Design.SortDesign.1
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(ContextData contextData, View view) {
                SortDialog.createAndShowDialog(contextData, view, SortDesign.SortDialogArg_Regular);
            }
        }, this.listenerRefHolder);
        LibraryQueueFragmentBase.onActionChooseSortFiles.subscribeWeak(new WeakEvent2.Handler<ContextData, View>() { // from class: com.daaw.avee.Design.SortDesign.2
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(ContextData contextData, View view) {
                SortDialog.createAndShowDialog(contextData, view, SortDesign.SortDialogArg_Files);
            }
        }, this.listenerRefHolder);
        LibraryQueueFragmentBase.onActionChooseSortStations.subscribeWeak(new WeakEvent2.Handler<ContextData, View>() { // from class: com.daaw.avee.Design.SortDesign.3
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(ContextData contextData, View view) {
                SortDialog.createAndShowDialog(contextData, view, SortDesign.SortDialogArg_Stations);
            }
        }, this.listenerRefHolder);
        SortDialog.onRequestSortOptions.subscribeWeak(new WeakEventR3.Handler<Integer, IGeneralItemContainerIdentifier, String, SortOptions>() { // from class: com.daaw.avee.Design.SortDesign.4
            @Override // com.daaw.avee.Common.Events.WeakEventR3.Handler
            public SortOptions invoke(Integer num, IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier, String str) {
                SortOptions sortOptions = SortDesign.this.getSortOptions();
                if (!str.equals(SortDesign.SortDialogArg_Regular)) {
                    if (str.equals(SortDesign.SortDialogArg_Files)) {
                        return sortOptions.getRefined(8, 0, 3, 5, 7);
                    }
                    if (str.equals(SortDesign.SortDialogArg_Stations)) {
                        sortOptions.setRadioOptions(SortDesign.Sort_Mode_Default_, SortDesign.Sort_Mode_Title_, SortDesign.Sort_Mode_Path_, SortDesign.Sort_Mode_Bitrate_);
                        return sortOptions;
                    }
                    return sortOptions;
                }
                return sortOptions;
            }
        }, this.listenerRefHolder);
        SortDialog.onSubmitSortOptions.subscribeWeak(new WeakEvent4.Handler<Integer, IGeneralItemContainerIdentifier, Integer, Integer>() { // from class: com.daaw.avee.Design.SortDesign.5
            @Override // com.daaw.avee.Common.Events.WeakEvent4.Handler
            public void invoke(Integer num, IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier, Integer num2, Integer num3) {
                AppPreferences.createOrGetInstance().setInt(AppPreferences.PREF_Int_SortSelectedRadioOption, num2.intValue());
                AppPreferences.createOrGetInstance().setInt(AppPreferences.PREF_Int_SortMaskCheckOptions, num3.intValue());
                SortDesign.this.updateLibraryItems();
            }
        }, this.listenerRefHolder);
        LibraryQueueFragmentBase.onRequestCurrentSortDesc.subscribeWeak(new WeakEventR2.Handler<Integer, IGeneralItemContainerIdentifier, SortDesc>() { // from class: com.daaw.avee.Design.SortDesign.6
            @Override // com.daaw.avee.Common.Events.WeakEventR2.Handler
            public SortDesc invoke(Integer num, IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier) {
                int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_SortSelectedRadioOption);
                int i2 = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_SortMaskCheckOptions);
                SortDesc sortDesc = new SortDesc();
                sortDesc.sortModeIndex = i;
                sortDesc.sortDescending = (i2 & 1) != 0;
                return sortDesc;
            }
        }, this.listenerRefHolder);
        LibraryQueueFragmentBase.onRequestFilterFileResult.subscribeWeak(new WeakEventR3.Handler<Integer, IGeneralItemContainerIdentifier, File, Boolean>() { // from class: com.daaw.avee.Design.SortDesign.7
            @Override // com.daaw.avee.Common.Events.WeakEventR3.Handler
            public Boolean invoke(Integer num, IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier, File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String strExtractFilenameExt = UtilsFileSys.extractFilenameExt(file);
                if (strExtractFilenameExt.equals("mp3") || strExtractFilenameExt.equals("wav") || strExtractFilenameExt.equals("mp4") || strExtractFilenameExt.equals("m4a") || strExtractFilenameExt.equals("m4p") || strExtractFilenameExt.equals("m4b") || strExtractFilenameExt.equals("m4r") || strExtractFilenameExt.equals("m4v") || strExtractFilenameExt.equals("mp4v") || strExtractFilenameExt.equals("3gp") || strExtractFilenameExt.equals("3g2") || strExtractFilenameExt.equals("3gp2") || strExtractFilenameExt.equals("3gpp") || strExtractFilenameExt.equals("3ga") || strExtractFilenameExt.equals("webm") || strExtractFilenameExt.equals("flv") || strExtractFilenameExt.equals("aac") || strExtractFilenameExt.equals("mkv") || strExtractFilenameExt.equals("fmp4") || strExtractFilenameExt.equals("ts") || strExtractFilenameExt.equals("tsv") || strExtractFilenameExt.equals("tsa") || strExtractFilenameExt.equals("flac") || strExtractFilenameExt.equals("mid") || strExtractFilenameExt.equals("midi") || strExtractFilenameExt.equals("rmi") || strExtractFilenameExt.equals("xmf") || strExtractFilenameExt.equals("mxmf") || strExtractFilenameExt.equals("rtttl") || strExtractFilenameExt.equals("rtx") || strExtractFilenameExt.equals("ota") || strExtractFilenameExt.equals("imy") || strExtractFilenameExt.equals("ogg") || strExtractFilenameExt.equals("asf") || strExtractFilenameExt.equals("wma") || strExtractFilenameExt.equals("wmv") || strExtractFilenameExt.equals("wm") || strExtractFilenameExt.equals("asx") || strExtractFilenameExt.equals("wax") || strExtractFilenameExt.equals("wvx") || strExtractFilenameExt.equals("wmx") || strExtractFilenameExt.equals("wpl") || strExtractFilenameExt.equals("dvr-ms") || strExtractFilenameExt.equals("wmd") || strExtractFilenameExt.equals("avi") || strExtractFilenameExt.equals("mpg") || strExtractFilenameExt.equals("mpeg") || strExtractFilenameExt.equals("m1v") || strExtractFilenameExt.equals("mp2") || strExtractFilenameExt.equals("mpa") || strExtractFilenameExt.equals("mpe") || strExtractFilenameExt.equals("mpga") || strExtractFilenameExt.equals("aif") || strExtractFilenameExt.equals("aifc") || strExtractFilenameExt.equals("aiff") || strExtractFilenameExt.equals("au") || strExtractFilenameExt.equals("snd") || strExtractFilenameExt.equals("cda") || strExtractFilenameExt.equals("ivf") || strExtractFilenameExt.equals("mov") || strExtractFilenameExt.equals("adt") || strExtractFilenameExt.equals("adts") || strExtractFilenameExt.equals("m2ts") || strExtractFilenameExt.equals("amr") || strExtractFilenameExt.equals("aup") || strExtractFilenameExt.equals("caf") || strExtractFilenameExt.equals("kar") || strExtractFilenameExt.equals("mmf") || strExtractFilenameExt.equals("oma") || strExtractFilenameExt.equals("opus") || strExtractFilenameExt.equals("qcp") || strExtractFilenameExt.equals("ra") || strExtractFilenameExt.equals("ram") || strExtractFilenameExt.equals("xspf") || strExtractFilenameExt.equals("m3u") || strExtractFilenameExt.equals("m3u8")) {
                    return true;
                }
                return false;
            }
        }, this.listenerRefHolder);
    }

    public static <T> boolean contains(T[] tArr, T t) {
        for (T t2 : tArr) {
            if (t2 == t) {
                return true;
            }
            if (t != null && t.equals(t2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public SortOptions getSortOptions() {
        int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_SortSelectedRadioOption);
        int i2 = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_SortMaskCheckOptions);
        SortOptions sortOptions = new SortOptions();
        sortOptions.radioOptions = this.radioOptions;
        sortOptions.checkOptions = this.checkOptions;
        sortOptions.selectedRadioOption = i;
        sortOptions.maskCheckOptions = i2;
        return sortOptions;
    }

    public void updateLibraryItems() {
        Fragment0 fragment0Instance = MainActivity.getFragment0Instance();
        if (fragment0Instance != null) {
            fragment0Instance.updateLibraryItems();
        }
    }

    public static class SortOptions {
        public MultiList<Integer, String> checkOptions;
        public int maskCheckOptions;
        public MultiList<Integer, String> radioOptions;
        public int selectedRadioOption;

        public SortOptions getRefined(int... iArr) {
            SortOptions sortOptions = new SortOptions();
            sortOptions.radioOptions = new MultiList<>();
            for (Tuple2<Integer, String> tuple2 : this.radioOptions) {
                if (SortDesign.contains(iArr, tuple2.obj1.intValue())) {
                    sortOptions.radioOptions.add(tuple2);
                }
            }
            sortOptions.checkOptions = this.checkOptions;
            sortOptions.selectedRadioOption = this.selectedRadioOption;
            sortOptions.maskCheckOptions = this.maskCheckOptions;
            return sortOptions;
        }

        public void setRadioOptions(Tuple2<Integer, String>... tuple2Arr) {
            this.radioOptions = new MultiList<>();
            for (Tuple2<Integer, String> tuple2 : tuple2Arr) {
                this.radioOptions.add(tuple2);
            }
        }
    }
}
