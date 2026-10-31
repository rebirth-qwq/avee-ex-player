package com.daaw.avee.comp.VisualizerExporter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsSerialize;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.ContextData;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class VisExportDialog extends DialogFragment {
    private Button btnDestDir;
    private Button btnEndTime;
    private Button btnStartTime;
    private CheckBox chkAudioStereo;
    private CheckBox chkBlurQuality;
    private CheckBox chkExportAudio;
    private CheckBox chkForceCompatibleWh;
    private CheckBox chkHideAppLogo;
    private EditText editAudioBitrate;
    private EditText editTxtBitrate;
    private EditText editTxtFilename;
    private EditText editTxtFramerate;
    private EditText editTxtHeight;
    private EditText editTxtWidth;
    private ViewGroup layoutContent;
    private ViewGroup layoutContentExporting;
    private ProgressBar progressDecode;
    private ProgressBar progressRender;
    private Spinner spinnerProfile;
    private TextView txtDecodeValue;
    private TextView txtRender;
    private TextView txtRenderValue;
    private Spinner videoCodec;
    private Spinner videoCodecProfile;
    public static WeakEvent4<Fragment, ContextData, ExportSettings, Boolean> onExportAction = new WeakEvent4<>();
    public static WeakEvent onCancelAction = new WeakEvent();
    public static WeakEvent3<Fragment, ContextData, String> onDestDirAction = new WeakEvent3<>();
    public static WeakEvent3<VisExportDialog, Integer, Intent> onActivityResult = new WeakEvent3<>();
    public static WeakEventR<ExportSettings> onRequestExportSettings = new WeakEventR<>();
    private static WeakEvent1<String> internalOnDestDirUpdated = new WeakEvent1<>();
    private static WeakEvent1<ExportingProgressStatus> internalOnExportingProgressStatusUpdated = new WeakEvent1<>();
    private static WeakEvent1<ExportingInfoStatus> internalOnExportingInfoStatusUpdated = new WeakEvent1<>();
    ExportSettings loadedExportSettings = null;
    private int timeStartMs = 0;
    private int timeEndMs = 0;
    private List<Object> listenerRefHolder = new LinkedList();

    public static class ExportingInfoStatus {
    }

    public static class ExportingProgressStatus {
        public boolean active;
        public boolean isLoadingResources;
        public int progressMax;
        public int progressValue;
        public String statText;
    }

    public static void updateDestinationDirValue(String str) {
        internalOnDestDirUpdated.invoke(str);
    }

    public static void updateExportingProgressStatus(ExportingProgressStatus exportingProgressStatus) {
        internalOnExportingProgressStatusUpdated.invoke(exportingProgressStatus);
    }

    public static void updateExportingInfoStatus(ExportingInfoStatus exportingInfoStatus) {
        internalOnExportingInfoStatusUpdated.invoke(exportingInfoStatus);
    }

    public static VisExportDialog createAndShowVisExportDialog(ContextData contextData) {
        VisExportDialog visExportDialog = new VisExportDialog();
        UtilsUI.showDialogSafe(visExportDialog, "VisExportDialog", contextData);
        return visExportDialog;
    }

    boolean isViewCreated() {
        return this.layoutContent != null;
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewInflate = View.inflate(getActivity(), R.layout.dialog_export_vis, null);
        builder.setView(viewInflate);
        this.layoutContent = (ViewGroup) viewInflate.findViewById(R.id.layoutContent);
        this.layoutContentExporting = (ViewGroup) viewInflate.findViewById(R.id.layoutContentExporting);
        Button button = (Button) viewInflate.findViewById(R.id.btnExport);
        button.setTextColor(UtilsUI.getAttrColor(button, R.attr.colorAccent));
        button.setSelected(true);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisExportDialog.onExportAction.invoke(VisExportDialog.this, new ContextData(view), VisExportDialog.this.getCurrentExportSettings(), false);
            }
        });
        Button button2 = (Button) viewInflate.findViewById(R.id.btnExportPreview);
        button2.setTextColor(UtilsUI.getAttrColor(button, R.attr.colorAccent));
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisExportDialog.onExportAction.invoke(VisExportDialog.this, new ContextData(view), VisExportDialog.this.getCurrentExportSettings(), true);
            }
        });
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.btnStopExport);
        imageButton.setColorFilter(UtilsUI.getAttrColor(button, R.attr.colorAccent));
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisExportDialog.onCancelAction.invoke();
            }
        });
        Button button3 = (Button) viewInflate.findViewById(R.id.btnDestDir);
        this.btnDestDir = button3;
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisExportDialog.onDestDirAction.invoke(VisExportDialog.this, new ContextData(view), VisExportDialog.this.btnDestDir.getText().toString());
            }
        });
        this.editTxtFilename = (EditText) viewInflate.findViewById(R.id.editTxtFilename);
        this.spinnerProfile = (Spinner) viewInflate.findViewById(R.id.spinnerProfile);
        this.videoCodec = (Spinner) viewInflate.findViewById(R.id.spinnerVideoCodec);
        this.videoCodecProfile = (Spinner) viewInflate.findViewById(R.id.spinnerVideoCodecProfile);
        this.editTxtWidth = (EditText) viewInflate.findViewById(R.id.editTxtWidth);
        this.editTxtHeight = (EditText) viewInflate.findViewById(R.id.editTxtHeight);
        this.editTxtFramerate = (EditText) viewInflate.findViewById(R.id.editTxtFramerate);
        this.editTxtBitrate = (EditText) viewInflate.findViewById(R.id.editTxtBitrate);
        this.txtRender = (TextView) viewInflate.findViewById(R.id.txtRender);
        this.progressRender = (ProgressBar) viewInflate.findViewById(R.id.progressBarRender);
        this.txtRenderValue = (TextView) viewInflate.findViewById(R.id.txtRenderValue);
        this.chkForceCompatibleWh = (CheckBox) viewInflate.findViewById(R.id.chkForceCompatibleWh);
        this.chkBlurQuality = (CheckBox) viewInflate.findViewById(R.id.chkBlurQuality);
        this.chkExportAudio = (CheckBox) viewInflate.findViewById(R.id.chkExportAudio);
        this.chkAudioStereo = (CheckBox) viewInflate.findViewById(R.id.chkAudioStereo);
        this.editAudioBitrate = (EditText) viewInflate.findViewById(R.id.editAudioBitrate);
        this.chkHideAppLogo = (CheckBox) viewInflate.findViewById(R.id.chkHideAppLogo);
        this.btnStartTime = (Button) viewInflate.findViewById(R.id.btnStartTime);
        this.btnEndTime = (Button) viewInflate.findViewById(R.id.btnEndTime);
        this.btnStartTime.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerExporter.-$$Lambda$VisExportDialog$ar0CODVjUAVj2ENK9wucbcN5QiA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                lambda$onCreateDialog$1$VisExportDialog(view);
            }
        });
        this.btnEndTime.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerExporter.-$$Lambda$VisExportDialog$ZpdWo_67szR0KSjENq4FqgOyWrI
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                lambda$onCreateDialog$3$VisExportDialog(view);
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.setCancelable(false);
        alertDialogCreate.setCanceledOnTouchOutside(false);
        updateExportSettings(onRequestExportSettings.invoke(null));
        return alertDialogCreate;
    }

    public /* synthetic */ void lambda$onCreateDialog$1$VisExportDialog(View view) {
        Activity activity = getActivity();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() { // from class: com.daaw.avee.comp.VisualizerExporter.-$$Lambda$VisExportDialog$gPPsslRfx8di18K2bQBGwlR-xU4
            @Override // android.app.TimePickerDialog.OnTimeSetListener
            public final void onTimeSet(TimePicker timePicker, int i, int i2) {
                lambda$null$0$VisExportDialog(timePicker, i, i2);
            }
        };
        int i = this.timeStartMs;
        new TimePickerDialog(activity, onTimeSetListener, i / 60000, (i % 60000) / 1000, true).show();
    }

    public /* synthetic */ void lambda$null$0$VisExportDialog(TimePicker timePicker, int i, int i2) {
        setTimeStartMs((i2 + (i * 60)) * 1000);
    }

    public /* synthetic */ void lambda$onCreateDialog$3$VisExportDialog(View view) {
        Activity activity = getActivity();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() { // from class: com.daaw.avee.comp.VisualizerExporter.-$$Lambda$VisExportDialog$-WUxOfgYVw4qJ9WuBjzxpyJFzFg
            @Override // android.app.TimePickerDialog.OnTimeSetListener
            public final void onTimeSet(TimePicker timePicker, int i, int i2) {
                lambda$null$2$VisExportDialog(timePicker, i, i2);
            }
        };
        int i = this.timeEndMs;
        new TimePickerDialog(activity, onTimeSetListener, i / 60000, (i % 60000) / 1000, true).show();
    }

    public /* synthetic */ void lambda$null$2$VisExportDialog(TimePicker timePicker, int i, int i2) {
        setTimeEndMs((i2 + (i * 60)) * 1000);
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        internalOnDestDirUpdated.subscribeWeak(new WeakEvent1.Handler<String>() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.5
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(String str) {
                VisExportDialog.this.internalDestDirUpdate(str);
            }
        }, this.listenerRefHolder);
        internalOnExportingProgressStatusUpdated.subscribeWeak(new WeakEvent1.Handler<ExportingProgressStatus>() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.6
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(ExportingProgressStatus exportingProgressStatus) {
                VisExportDialog.this.internalUpdateExportingProgressStatus(exportingProgressStatus);
            }
        }, this.listenerRefHolder);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        internalOnDestDirUpdated.unSubscribeWeak(this.listenerRefHolder);
        internalOnExportingProgressStatusUpdated.unSubscribeWeak(this.listenerRefHolder);
        this.listenerRefHolder.clear();
        super.onDestroy();
    }

    void updateExportSettings(final ExportSettings exportSettings) {
        if (this.layoutContent == null) {
            return;
        }
        this.loadedExportSettings = exportSettings;
        if (exportSettings == null) {
            return;
        }
        final String[] strArr = exportSettings.videoCodecProfileNames;
        this.btnDestDir.setText(exportSettings.destinationDir);
        this.editTxtFilename.setText(exportSettings.filename);
        int i = exportSettings.videoCodecActiveIndex;
        String[] strArr2 = exportSettings.videoCodecs != null ? exportSettings.videoCodecs : new String[]{"<empty>"};
        this.videoCodec.setAdapter((SpinnerAdapter) new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, strArr2));
        if (i >= 0 && i < strArr2.length) {
            this.videoCodec.setSelection(i, false);
        } else {
            this.videoCodec.setSelection(0, false);
        }
        this.videoCodec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.7
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        int i2 = exportSettings.profileActiveIndex;
        this.spinnerProfile.setAdapter((SpinnerAdapter) new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, exportSettings.getProfileNames(this.spinnerProfile.getResources().getString(R.string.vis_export_choose_preset))));
        this.spinnerProfile.setSelection(i2, false);
        this.spinnerProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.8
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j) {
                int i4 = i3 - 1;
                if (i4 >= 0) {
                    VisExportDialog.this.onExportSettingsProfileChanged(exportSettings.getProfile(i4), strArr);
                }
            }
        });
        this.chkForceCompatibleWh.setChecked(exportSettings.videoForceCompatibleWH);
        this.chkBlurQuality.setChecked(exportSettings.useHighQualityBlur);
        this.chkExportAudio.setChecked(exportSettings.exportAudio);
        onExportSettingsProfileChanged(exportSettings.profileActive, strArr);
        setTimeStartMs(exportSettings.startMSec);
        setTimeEndMs(exportSettings.endMSec);
    }

    private void setTimeStartMs(int i) {
        int iMin = Math.min(this.loadedExportSettings.endMSec, Math.max(0, i));
        this.timeStartMs = iMin;
        this.btnStartTime.setText(Utils.getDurationStringHMSS(iMin / 1000));
    }

    private void setTimeEndMs(int i) {
        int iMin = Math.min(this.loadedExportSettings.endMSec, Math.max(0, i));
        this.timeEndMs = iMin;
        this.btnEndTime.setText(Utils.getDurationStringHMSS(iMin / 1000));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExportSettingsProfileChanged(ExportSettingsProfile exportSettingsProfile, String[] strArr) {
        if (this.layoutContent == null) {
            return;
        }
        int i = exportSettingsProfile.videoCodecProfile;
        if (strArr == null) {
            strArr = new String[]{"<empty>"};
        }
        this.videoCodecProfile.setAdapter((SpinnerAdapter) new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, strArr));
        if (i >= 0 && i < strArr.length) {
            this.videoCodecProfile.setSelection(i, false);
        } else {
            this.videoCodecProfile.setSelection(0, false);
        }
        this.videoCodecProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.VisualizerExporter.VisExportDialog.9
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.editTxtWidth.setText("" + exportSettingsProfile.width);
        this.editTxtHeight.setText("" + exportSettingsProfile.height);
        this.editTxtFramerate.setText("" + exportSettingsProfile.frameRate);
        this.editTxtBitrate.setText("" + exportSettingsProfile.videoBitrateMbps);
        this.chkAudioStereo.setChecked(exportSettingsProfile.audioChannels > 1);
        this.editAudioBitrate.setText("" + exportSettingsProfile.audioBitrateKbps);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ExportSettings getCurrentExportSettings() {
        ExportSettings exportSettings = new ExportSettings();
        exportSettings.profileActive = new ExportSettingsProfile(0, Utils.strToIntSafe(this.editTxtWidth.getText().toString()), Utils.strToIntSafe(this.editTxtHeight.getText().toString()), Utils.strToIntSafe(this.editTxtFramerate.getText().toString()), Utils.strToFloatSafe(this.editTxtBitrate.getText().toString()), this.chkAudioStereo.isChecked() ? 2 : 1, Utils.strToFloatSafe(this.editAudioBitrate.getText().toString()), this.videoCodecProfile.getSelectedItemPosition());
        exportSettings.videoCodecs = this.loadedExportSettings.videoCodecs;
        exportSettings.videoMimeTypes = this.loadedExportSettings.videoMimeTypes;
        exportSettings.videoCodecActiveIndex = this.videoCodec.getSelectedItemPosition();
        exportSettings.destinationDir = this.btnDestDir.getText().toString();
        exportSettings.filename = this.editTxtFilename.getText().toString();
        exportSettings.videoForceCompatibleWH = this.chkForceCompatibleWh.isChecked();
        exportSettings.exportAudio = this.chkExportAudio.isChecked();
        exportSettings.startMSec = this.timeStartMs;
        exportSettings.endMSec = this.timeEndMs;
        exportSettings.useHighQualityBlur = this.chkBlurQuality.isChecked();
        exportSettings.hideAppLogo = this.chkHideAppLogo.isChecked();
        return exportSettings;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalDestDirUpdate(String str) {
        if (isViewCreated()) {
            this.btnDestDir.setText(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalUpdateExportingProgressStatus(ExportingProgressStatus exportingProgressStatus) {
        if (isViewCreated()) {
            if (exportingProgressStatus.active) {
                if (this.layoutContentExporting.getVisibility() != 0) {
                    this.layoutContentExporting.setVisibility(0);
                    this.layoutContent.setVisibility(8);
                }
                if (exportingProgressStatus.isLoadingResources) {
                    this.txtRender.setVisibility(0);
                    this.txtRender.setText(R.string.vis_export_status_loading);
                } else {
                    this.txtRender.setVisibility(8);
                }
                this.progressRender.getProgressDrawable().setColorFilter(UtilsUI.getAttrColor(this.progressRender, R.attr.colorAccent), PorterDuff.Mode.SRC_IN);
                this.progressRender.setMax(exportingProgressStatus.progressMax);
                this.progressRender.setProgress(exportingProgressStatus.progressValue);
                float f = ((float) exportingProgressStatus.progressValue) / ((float) exportingProgressStatus.progressMax);
                TextView textView = this.txtRenderValue;
                float f2 = f * 100.0f;
                textView.setText(textView.getResources().getQuantityString(R.plurals.vis_progress_x, (int) f2, Float.valueOf(f2)));
                return;
            }
            this.layoutContent.setVisibility(0);
            this.layoutContentExporting.setVisibility(8);
        }
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        onCancelAction.invoke();
    }

    @Override // android.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            onActivityResult.invoke(this, Integer.valueOf(i), intent);
        }
    }

    public static class ExportSettingsProfile {
        public float audioBitrateKbps;
        public int audioChannels;
        private int decorLevel;
        public int frameRate;
        public int height;
        public String name;
        public float videoBitrateMbps;
        public int videoCodecProfile;
        public int width;

        public ExportSettingsProfile(int i, int i2, int i3, int i4, float f, boolean z, int i5) {
            this.decorLevel = i;
            this.width = i2;
            this.height = i3;
            this.frameRate = i4;
            this.videoBitrateMbps = f;
            if (z) {
                this.audioChannels = 2;
                this.audioBitrateKbps = 384.0f;
            } else {
                this.audioChannels = 2;
                this.audioBitrateKbps = 256.0f;
            }
            this.videoCodecProfile = i5;
            this.name = makeName();
        }

        public ExportSettingsProfile(int i, int i2, int i3, int i4, float f, int i5, float f2, int i6) {
            this.decorLevel = i;
            this.width = i2;
            this.height = i3;
            this.frameRate = i4;
            this.videoBitrateMbps = f;
            this.audioChannels = i5;
            this.audioBitrateKbps = f2;
            this.videoCodecProfile = i6;
            this.name = makeName();
        }

        public String serialize() {
            return this.decorLevel + ";" + UtilsSerialize.replaceCharsInString(';', '_', this.name) + ";" + this.width + ";" + this.height + ";" + this.frameRate + ";" + this.videoBitrateMbps + ";" + this.audioChannels + ";" + this.audioBitrateKbps + ";" + this.videoCodecProfile;
        }

        public void deserialize(String str) {
            String[] strArrDeserializeIterable = UtilsSerialize.deserializeIterable(";", str);
            if (strArrDeserializeIterable.length != 9) {
                return;
            }
            this.decorLevel = Utils.strToIntSafe(strArrDeserializeIterable[0], this.decorLevel);
            this.name = Utils.strToStrSafe(strArrDeserializeIterable[1], this.name);
            this.width = Utils.strToIntSafe(strArrDeserializeIterable[2], this.width);
            this.height = Utils.strToIntSafe(strArrDeserializeIterable[3], this.height);
            this.frameRate = Utils.strToIntSafe(strArrDeserializeIterable[4], this.frameRate);
            this.videoBitrateMbps = Utils.strToFloatSafe(strArrDeserializeIterable[5], this.videoBitrateMbps);
            this.audioChannels = Utils.strToIntSafe(strArrDeserializeIterable[6], this.audioChannels);
            this.audioBitrateKbps = Utils.strToFloatSafe(strArrDeserializeIterable[7], this.audioBitrateKbps);
            this.videoCodecProfile = Utils.strToIntSafe(strArrDeserializeIterable[8], this.videoCodecProfile);
        }

        String makeName() {
            String str;
            int i = this.decorLevel;
            if (i == 1) {
                str = " ";
            } else if (i == 2) {
                str = "  ";
            } else if (i == 3) {
                str = "   ";
            } else {
                str = i == 4 ? "    " : "";
            }
            return String.format(Locale.US, "%s%dp @%d %.1fMbps%s %.1fKbps %s", str, Integer.valueOf(this.height), Integer.valueOf(this.frameRate), Float.valueOf(this.videoBitrateMbps), "", Float.valueOf(this.audioBitrateKbps), this.videoCodecProfile == 0 ? "L" : "H");
        }

        /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
        public ExportSettingsProfile m8clone() {
            try {
                super.clone();
            } catch (Exception unused) {
            }
            return new ExportSettingsProfile(this.decorLevel, this.width, this.height, this.frameRate, this.videoBitrateMbps, this.audioChannels, this.audioBitrateKbps, this.videoCodecProfile);
        }

        public static ExportSettingsProfile makeMaxProfile(ExportSettingsProfile[] exportSettingsProfileArr) {
            if (exportSettingsProfileArr == null || exportSettingsProfileArr.length <= 0) {
                return new ExportSettingsProfile(0, 0, 0, 0, 0.0f, 0, 0.0f, 0);
            }
            ExportSettingsProfile exportSettingsProfileM8clone = exportSettingsProfileArr[0].m8clone();
            for (int i = 0; i < exportSettingsProfileArr.length; i++) {
                exportSettingsProfileM8clone.width = Math.max(exportSettingsProfileM8clone.width, exportSettingsProfileArr[i].width);
                exportSettingsProfileM8clone.height = Math.max(exportSettingsProfileM8clone.height, exportSettingsProfileArr[i].height);
                exportSettingsProfileM8clone.frameRate = Math.max(exportSettingsProfileM8clone.frameRate, exportSettingsProfileArr[i].frameRate);
                exportSettingsProfileM8clone.videoBitrateMbps = Math.max(exportSettingsProfileM8clone.videoBitrateMbps, exportSettingsProfileArr[i].videoBitrateMbps);
                exportSettingsProfileM8clone.audioChannels = Math.max(exportSettingsProfileM8clone.audioChannels, exportSettingsProfileArr[i].audioChannels);
                exportSettingsProfileM8clone.audioBitrateKbps = Math.max(exportSettingsProfileM8clone.audioBitrateKbps, exportSettingsProfileArr[i].audioBitrateKbps);
                exportSettingsProfileM8clone.videoCodecProfile = Math.max(exportSettingsProfileM8clone.videoCodecProfile, exportSettingsProfileArr[i].videoCodecProfile);
            }
            exportSettingsProfileM8clone.name = exportSettingsProfileM8clone.makeName();
            return exportSettingsProfileM8clone;
        }

        public void forceMaxProfile(ExportSettingsProfile exportSettingsProfile) {
            this.width = Math.min(this.width, exportSettingsProfile.width);
            this.height = Math.min(this.height, exportSettingsProfile.height);
            this.frameRate = Math.min(this.frameRate, exportSettingsProfile.frameRate);
            this.videoBitrateMbps = Math.min(this.videoBitrateMbps, exportSettingsProfile.videoBitrateMbps);
            this.audioChannels = Math.min(this.audioChannels, exportSettingsProfile.audioChannels);
            this.audioBitrateKbps = Math.min(this.audioBitrateKbps, exportSettingsProfile.audioBitrateKbps);
            this.videoCodecProfile = Math.min(this.videoCodecProfile, exportSettingsProfile.videoCodecProfile);
            this.name = makeName();
        }
    }

    public static class ExportSettings {
        public String destinationDir;
        public int endMSec;
        public boolean exportAudio;
        public String filename;
        public boolean hideAppLogo;
        public ExportSettingsProfile profileActive;
        public int profileActiveIndex;
        public ExportSettingsProfile[] profiles;
        public int startMSec;
        public boolean useHighQualityBlur;
        public int videoCodecActiveIndex;
        public String[] videoCodecProfileNames;
        public String[] videoCodecs;
        public boolean videoForceCompatibleWH;
        public String[] videoMimeTypes;

        public ExportSettingsProfile getProfile(int i) {
            if (i < 0) {
                return null;
            }
            ExportSettingsProfile[] exportSettingsProfileArr = this.profiles;
            if (i >= exportSettingsProfileArr.length) {
                return null;
            }
            return exportSettingsProfileArr[i];
        }

        public ExportSettingsProfile getProfileClone(int i) {
            ExportSettingsProfile profile = getProfile(i);
            if (profile != null) {
                return profile.m8clone();
            }
            return null;
        }

        public String getVideoCodec(int i) {
            String[] strArr = this.videoCodecs;
            if (strArr != null && i >= 0 && i < strArr.length) {
                return strArr[i];
            }
            return null;
        }

        public String getVideoMimeTypeCodec(int i) {
            String[] strArr = this.videoMimeTypes;
            if (strArr != null && i >= 0 && i < strArr.length) {
                return strArr[i];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String[] getProfileNames(String str) {
            int i = 1;
            int i2 = 0;
            String[] strArr = new String[this.profiles.length + (str != null ? 1 : 0)];
            if (str != null) {
                strArr[0] = str;
            } else {
                i = 0;
            }
            while (true) {
                ExportSettingsProfile[] exportSettingsProfileArr = this.profiles;
                if (i2 >= exportSettingsProfileArr.length) {
                    return strArr;
                }
                strArr[i2 + i] = exportSettingsProfileArr[i2].name;
                i2++;
            }
        }

        public String serialize() {
            return this.profileActiveIndex + ";" + this.videoCodecActiveIndex + ";" + UtilsSerialize.replaceCharsInString(';', '_', this.destinationDir) + ";" + UtilsSerialize.replaceCharsInString(';', '_', this.filename) + ";" + this.exportAudio + ";" + this.startMSec + ";" + this.endMSec + ";" + this.useHighQualityBlur + ";" + this.videoForceCompatibleWH;
        }

        public void deserialize(String str) {
            String[] strArrDeserializeIterable = UtilsSerialize.deserializeIterable(";", str);
            if (strArrDeserializeIterable.length != 9) {
                return;
            }
            this.profileActiveIndex = Utils.strToIntSafe(strArrDeserializeIterable[0], this.profileActiveIndex);
            this.videoCodecActiveIndex = Utils.strToIntSafe(strArrDeserializeIterable[1], this.videoCodecActiveIndex);
            this.destinationDir = Utils.strToStrSafe(strArrDeserializeIterable[2], this.destinationDir);
            this.exportAudio = Utils.strToBoolSafe(strArrDeserializeIterable[4], this.exportAudio);
            this.useHighQualityBlur = Utils.strToBoolSafe(strArrDeserializeIterable[7], this.useHighQualityBlur);
            this.videoForceCompatibleWH = Utils.strToBoolSafe(strArrDeserializeIterable[8], this.videoForceCompatibleWH);
        }
    }
}
