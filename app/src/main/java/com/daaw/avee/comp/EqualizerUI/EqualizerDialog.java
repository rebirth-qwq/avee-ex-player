package com.daaw.avee.comp.EqualizerUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.Common.VerticalSeekBar;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.ContextData;
import com.triggertrap.seekarc.SeekArc;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class EqualizerDialog extends DialogFragment {
    private VerticalSeekBar[] bandSeekBars;
    private TextView[] bandTexts;
    private float bassValue;
    private ViewGroup linearLayoutBars;
    private ScrollView scrollView1;
    private HorizontalScrollView scrollView2;
    private SeekArc seekArcBass;
    private SeekArc seekArcTreble;
    private SeekBar seekBarVirtualizer;
    private Spinner spinnerPresets;
    private SwitchCompat switchEnable;
    private TextView textBass;
    private TextView textTreble;
    private float trebleValue;
    public static WeakEvent1<EqualizerUIDesc> onReceiveEqualizerDescChanged = new WeakEvent1<>();
    public static WeakEvent2<EqualizerUISettings, EqualizerUIDesc> onSubmitEqualizerSettings = new WeakEvent2<>();
    public static WeakEventR<EqualizerUIDesc> onRequestEqualizerDesc = new WeakEventR<>();
    private static int seekArcMax = 30;
    private List<Object> listenerRefHolder = new LinkedList();
    private boolean spinnerPresetsEventFromUser = false;
    private EqualizerUIDesc equalizerUIDesc = null;
    private int bandBarMax = 30;
    private EQPreset currentBands = EQPreset.clone(EQPreset.empty);
    private boolean preventSettingsUpdate = false;
    View.OnTouchListener seekBarTouch = new View.OnTouchListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.1
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                EqualizerDialog.this.scrollView1.requestDisallowInterceptTouchEvent(true);
                EqualizerDialog.this.scrollView2.requestDisallowInterceptTouchEvent(true);
            }
            return view.onTouchEvent(motionEvent);
        }
    };

    public static EqualizerDialog createAndShowEqualizerDialog(ContextData contextData) {
        EqualizerDialog equalizerDialog = new EqualizerDialog();
        UtilsUI.showDialogSafe(equalizerDialog, "EqualizerDialog", contextData);
        return equalizerDialog;
    }

    boolean isViewCreated() {
        return this.switchEnable != null;
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        updateEqualizerDesc(onRequestEqualizerDesc.invoke(null));
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewInflate = View.inflate(getActivity(), R.layout.dialog_equalizer, null);
        builder.setView(viewInflate);
        SwitchCompat switchCompat = (SwitchCompat) viewInflate.findViewById(R.id.switchEnable);
        this.switchEnable = switchCompat;
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                EqualizerDialog.this.onSettingsChanged();
            }
        });
        this.spinnerPresets = (Spinner) viewInflate.findViewById(R.id.spinnerPresets);
        this.scrollView1 = (ScrollView) viewInflate.findViewById(R.id.scrollView1);
        this.scrollView2 = (HorizontalScrollView) viewInflate.findViewById(R.id.scrollView2);
        this.linearLayoutBars = (ViewGroup) viewInflate.findViewById(R.id.linearLayoutBars);
        this.textBass = (TextView) viewInflate.findViewById(R.id.textBass);
        SeekArc seekArc = (SeekArc) viewInflate.findViewById(R.id.seekArcBass);
        this.seekArcBass = seekArc;
        seekArc.setOnTouchListener(this.seekBarTouch);
        this.seekArcBass.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.3
            @Override // com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
            public void onStartTrackingTouch(SeekArc seekArc2) {
            }

            @Override // com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
            public void onStopTrackingTouch(SeekArc seekArc2) {
            }

            @Override // com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
            public void onProgressChanged(SeekArc seekArc2, int i, boolean z) {
                if (z) {
                    EqualizerDialog.this.spinnerPresets.setSelection(0, false);
                    EqualizerDialog.this.bassValue = (i - (EqualizerDialog.seekArcMax / 2)) / (EqualizerDialog.seekArcMax / 2.0f);
                    EqualizerDialog equalizerDialog = EqualizerDialog.this;
                    equalizerDialog.onBassTrebleChanged(equalizerDialog.bassValue, EqualizerDialog.this.trebleValue, true, true);
                    EqualizerDialog.this.onSettingsChanged();
                }
            }
        });
        this.textTreble = (TextView) viewInflate.findViewById(R.id.textTreble);
        SeekArc seekArc2 = (SeekArc) viewInflate.findViewById(R.id.seekArcTreble);
        this.seekArcTreble = seekArc2;
        seekArc2.setOnTouchListener(this.seekBarTouch);
        this.seekArcTreble.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.4
            @Override // com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
            public void onStartTrackingTouch(SeekArc seekArc3) {
            }

            @Override // com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
            public void onStopTrackingTouch(SeekArc seekArc3) {
            }

            @Override // com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
            public void onProgressChanged(SeekArc seekArc3, int i, boolean z) {
                if (z) {
                    EqualizerDialog.this.spinnerPresets.setSelection(0, false);
                    EqualizerDialog.this.trebleValue = (i - (EqualizerDialog.seekArcMax / 2)) / (EqualizerDialog.seekArcMax / 2.0f);
                    EqualizerDialog equalizerDialog = EqualizerDialog.this;
                    equalizerDialog.onBassTrebleChanged(equalizerDialog.bassValue, EqualizerDialog.this.trebleValue, true, true);
                    EqualizerDialog.this.onSettingsChanged();
                }
            }
        });
        SeekBar seekBar = (SeekBar) viewInflate.findViewById(R.id.seekBarVirtualizer);
        this.seekBarVirtualizer = seekBar;
        seekBar.setMax(100);
        this.seekBarVirtualizer.setOnTouchListener(this.seekBarTouch);
        this.seekBarVirtualizer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                EqualizerDialog.this.onVirtualizerValueChanged(i / 100.0f, true);
                EqualizerDialog.this.onSettingsChanged();
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.setCancelable(true);
        alertDialogCreate.setCanceledOnTouchOutside(true);
        return alertDialogCreate;
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        onReceiveEqualizerDescChanged.subscribeWeak(new WeakEvent1.Handler<EqualizerUIDesc>() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.6
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(EqualizerUIDesc equalizerUIDesc) {
                EqualizerDialog.this.updateEqualizerDesc(equalizerUIDesc);
            }
        }, this.listenerRefHolder);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        onReceiveEqualizerDescChanged.unSubscribeWeak(this.listenerRefHolder);
        this.listenerRefHolder.clear();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEqualizerDesc(EqualizerUIDesc equalizerUIDesc) {
        if (getActivity() != null && isViewCreated()) {
            this.preventSettingsUpdate = true;
            if (equalizerUIDesc == null) {
                equalizerUIDesc = EqualizerUIDesc.empty;
            }
            this.equalizerUIDesc = equalizerUIDesc;
            this.switchEnable.setChecked(equalizerUIDesc.enabled);
            this.currentBands = EQPreset.clone(this.equalizerUIDesc.currentBands);
            int length = this.equalizerUIDesc.currentBands.points.length;
            int i = this.bandBarMax;
            if (i % 2 != 0) {
                this.bandBarMax = i + 1;
            }
            this.spinnerPresetsEventFromUser = false;
            int length2 = this.equalizerUIDesc.presets.length + 1;
            String[] strArr = new String[length2];
            strArr[0] = getResources().getString(R.string.audio_eqcustom);
            int i2 = 0;
            while (i2 < this.equalizerUIDesc.presets.length) {
                int i3 = i2 + 1;
                strArr[i3] = this.equalizerUIDesc.presets[i2].name;
                i2 = i3;
            }
            this.spinnerPresets.setAdapter((SpinnerAdapter) new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, strArr));
            int i4 = this.equalizerUIDesc.currentPreset >= 0 ? this.equalizerUIDesc.currentPreset + 1 : 0;
            if (i4 < 0 || i4 >= length2) {
                i4 = 0;
            }
            this.spinnerPresets.setSelection(i4, false);
            this.spinnerPresets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.7
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> adapterView, View view, int i5, long j) {
                    int i6 = i5 - 1;
                    if (i6 >= 0 && i6 < EqualizerDialog.this.equalizerUIDesc.presets.length) {
                        Equalization.getEqBandsPresetsConvert(EqualizerDialog.this.equalizerUIDesc.presets[i6], EqualizerDialog.this.currentBands);
                        EqualizerDialog.this.onBassTrebleChanged(0.0f, 0.0f, false, true);
                    }
                    EqualizerDialog.this.onSettingsChanged();
                }
            });
            this.spinnerPresetsEventFromUser = true;
            this.bandSeekBars = new VerticalSeekBar[length];
            this.bandTexts = new TextView[length];
            this.linearLayoutBars.removeAllViews();
            for (int i5 = 0; i5 < length; i5++) {
                final int iBandIndex = i5;
                View viewInflate = View.inflate(getActivity(), R.layout.dialog_equalizer_bar, null);
                this.bandSeekBars[i5] = (VerticalSeekBar) viewInflate.findViewById(R.id.seekBarVolume);
                this.bandTexts[i5] = (TextView) viewInflate.findViewById(R.id.txtBarTop);
                ((TextView) viewInflate.findViewById(R.id.txtBarBottom)).setText(formatFreqHz(this.equalizerUIDesc.currentBands.points[i5].freq));
                this.bandSeekBars[i5].setMax(this.bandBarMax);
                this.bandSeekBars[i5].setOnTouchListener(this.seekBarTouch);
                this.bandSeekBars[i5].setOnSeekBarChangeFromUserListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.EqualizerUI.EqualizerDialog.8
                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar, int i6, boolean z) {
                        if (z) {
                            EqualizerDialog.this.spinnerPresets.setSelection(0, false);
                            EqualizerDialog equalizerDialog = EqualizerDialog.this;
                            equalizerDialog.setBandProgress(iBandIndex, i6 - (equalizerDialog.bandBarMax / 2), true);
                            EqualizerDialog equalizerDialog2 = EqualizerDialog.this;
                            equalizerDialog2.setEqPresetFromBandBars(equalizerDialog2.currentBands);
                            EqualizerDialog.this.onBassTrebleChanged(0.0f, 0.0f, false, false);
                            EqualizerDialog.this.onSettingsChanged();
                        }
                    }
                });
                this.linearLayoutBars.addView(viewInflate);
            }
            onBassTrebleChanged(this.equalizerUIDesc.bassBoostValue, this.equalizerUIDesc.trebleBoostValue, false, true);
            onVirtualizerValueChanged(this.equalizerUIDesc.virtualizerStrength, false);
            this.preventSettingsUpdate = false;
        }
    }

    private void setBandProgress(float[] fArr) {
        if (fArr.length != this.bandSeekBars.length) {
            return;
        }
        for (int i = 0; i < fArr.length; i++) {
            setBandProgress(i, Math.round((this.bandBarMax / 2) * fArr[i]), false);
        }
    }

    private void setBandProgress(EQPreset eQPreset) {
        if (eQPreset.points.length != this.bandSeekBars.length) {
            return;
        }
        for (int i = 0; i < eQPreset.points.length; i++) {
            setBandProgress(i, Math.round((this.bandBarMax / 2) * eQPreset.points[i].value), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBandProgress(int i, int i2, boolean z) {
        int i3 = this.bandBarMax;
        int iEnsureRange = Utils.ensureRange(i2, -(i3 / 2), i3 / 2);
        if (!z) {
            this.bandSeekBars[i].setProgress((this.bandBarMax / 2) + iEnsureRange);
            this.bandSeekBars[i].updateThumb();
        }
        this.bandTexts[i].setText("" + iEnsureRange);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBassTrebleChanged(float f, float f2, boolean z, boolean z2) {
        this.bassValue = f;
        this.trebleValue = f2;
        if (!z) {
            this.seekArcBass.setProgress(Math.round((seekArcMax / 2) * f) + (seekArcMax / 2));
            this.seekArcTreble.setProgress(Math.round((seekArcMax / 2) * f2) + (seekArcMax / 2));
        }
        int iRound = Math.round((seekArcMax / 2) * f);
        TextView textView = this.textBass;
        textView.setText(textView.getResources().getQuantityString(R.plurals.audio_bass_x, iRound, Integer.valueOf(iRound)));
        int iRound2 = Math.round((seekArcMax / 2) * f2);
        TextView textView2 = this.textTreble;
        textView2.setText(textView2.getResources().getQuantityString(R.plurals.audio_treble_x, iRound2, Integer.valueOf(iRound2)));
        float[] fArr = new float[this.currentBands.points.length];
        int length = this.currentBands.points.length;
        float[] fArr2 = new float[length];
        for (int i = 0; i < length; i++) {
            fArr2[i] = this.currentBands.points[i].freq;
        }
        if (z2) {
            Equalization.getEqBandsBassTrebleControl(this.currentBands, this.equalizerUIDesc.bassBoost, this.equalizerUIDesc.trebleBoost, f, f2, fArr, fArr2);
            setBandProgress(fArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSettingsChanged() {
        VerticalSeekBar[] verticalSeekBarArr;
        EqualizerUIDesc equalizerUIDesc;
        if (this.preventSettingsUpdate || (verticalSeekBarArr = this.bandSeekBars) == null || (equalizerUIDesc = this.equalizerUIDesc) == null) {
            return;
        }
        if (verticalSeekBarArr.length != equalizerUIDesc.currentBands.points.length) {
            tlog.w("equalizerUIDesc bands count doesnt match ");
            return;
        }
        EqualizerUISettings equalizerUISettings = new EqualizerUISettings();
        equalizerUISettings.enabled = this.switchEnable.isChecked();
        equalizerUISettings.presetIndex = this.spinnerPresets.getSelectedItemPosition() - 1;
        equalizerUISettings.bandsFinal = new EQPreset("Default", this.bandSeekBars.length);
        equalizerUISettings.bassValue = this.bassValue;
        equalizerUISettings.trebleValue = this.trebleValue;
        equalizerUISettings.currentBands = this.currentBands;
        equalizerUISettings.virtualizerStrength = ((float) this.seekBarVirtualizer.getProgress()) / ((float) this.seekBarVirtualizer.getMax());
        setEqPresetFromBandBars(equalizerUISettings.bandsFinal);
        onSubmitEqualizerSettings.invoke(equalizerUISettings, this.equalizerUIDesc);
    }

    void setEqPresetFromBandBars(EQPreset eQPreset) {
        VerticalSeekBar[] verticalSeekBarArr = this.bandSeekBars;
        int max = (verticalSeekBarArr.length > 0 ? verticalSeekBarArr[0].getMax() : 2) / 2;
        int length = eQPreset.points.length;
        VerticalSeekBar[] verticalSeekBarArr2 = this.bandSeekBars;
        if (length != verticalSeekBarArr2.length) {
            eQPreset.resize(verticalSeekBarArr2.length);
        }
        for (int i = 0; i < this.bandSeekBars.length; i++) {
            eQPreset.points[i] = new EQPreset.Point(this.equalizerUIDesc.currentBands.points[i].freq, ((float) (this.bandSeekBars[i].getProgress() - max)) / ((float) max));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVirtualizerValueChanged(float f, boolean z) {
        if (z) {
            return;
        }
        this.seekBarVirtualizer.setProgress((int) (f * 100.0f));
    }

    static String formatFreqHz(int i) {
        if (i < 1000) {
            return String.format(Locale.US, "%.1fHz", Float.valueOf(i * 0.001f));
        }
        if (i >= 1000000) {
            return String.format(Locale.US, "%.1fkHz", Float.valueOf(i * 1.0E-6f));
        }
        return "" + (i / 1000) + "Hz";
    }

    static String formatFreqHz(float f) {
        return formatFreqHz((int) (f * 1000.0f));
    }
}
