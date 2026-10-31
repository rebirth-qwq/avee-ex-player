package com.daaw.avee.comp.VisualizerCutomization;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.Func.Func;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Common.View.SimpleViewProgressBar;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.MVariableFloat;
import com.daaw.avee.comp.Visualizer.Elements.Base.MeasuredVar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CustomizeVisView1MVar extends CustomizeVisView1 {

    private static class StringMapper {
        private static final Map<String, Integer> translationMap = new HashMap();
        private static final Map<String, String> cache = new ConcurrentHashMap();

        private StringMapper() {
        }

        static {
            translationMap.put(MeasureDefs.Constant, Integer.valueOf(R.string.prop_levels));
            translationMap.put(MeasureDefs.beat, Integer.valueOf(R.string.anim_beat));
            translationMap.put(MeasureDefs.beatRandomShake, Integer.valueOf(R.string.anim_beat_random_shake));
            translationMap.put(MeasureDefs.trackPosition, Integer.valueOf(R.string.anim_track_position));
            translationMap.put(MeasureDefs.totalTime, Integer.valueOf(R.string.anim_total_time));
            translationMap.put(MeasureDefs.totalTimeBackward, Integer.valueOf(R.string.anim_total_time_backward));
            translationMap.put(MeasureDefs.totalTimeWhenPlaying, Integer.valueOf(R.string.anim_total_time_when_playing));
            translationMap.put(MeasureDefs.totalTimeAndBeat, Integer.valueOf(R.string.anim_total_time_and_beat));
            translationMap.put(MeasureDefs.beatCamShakeRotMore, Integer.valueOf(R.string.anim_beat_cam_shake_rot_more));
            translationMap.put(MeasureDefs.beatCamShakeRotLess, Integer.valueOf(R.string.anim_beat_cam_shake_rot_less));
            translationMap.put(MeasureDefs.constantCamShakeRotMore, Integer.valueOf(R.string.anim_constant_shake_rot_more));
            translationMap.put(MeasureDefs.constantCamShakeRotLess, Integer.valueOf(R.string.anim_constant_shake_rot_less));
            translationMap.put(MeasureDefs.beatTriggerAnim, Integer.valueOf(R.string.anim_beat_trigger_anim));
            translationMap.put(MeasureDefs.TotalTimeAndBack, Integer.valueOf(R.string.anim_total_time_and_back));
            translationMap.put(MeasureDefs.constantCamShakeMore, Integer.valueOf(R.string.anim_constant_shake_more));
            translationMap.put(MeasureDefs.beatCamShakeLess, Integer.valueOf(R.string.anim_beat_cam_shake_less));
            translationMap.put(MeasureDefs.beatCamShakeMore, Integer.valueOf(R.string.anim_beat_cam_shake_more));
            translationMap.put(MeasureDefs.constantCamShakeLess, Integer.valueOf(R.string.anim_constant_shake));
            translationMap.put(MeasureDefs.beatTriggerRandom, Integer.valueOf(R.string.anim_beat_trigger_random));
            translationMap.put(MeasureDefs.trackSetPosition, Integer.valueOf(R.string.anim_track_set_position));
            translationMap.put(MeasureDefs.infiniteMotion, Integer.valueOf(R.string.anim_infinite_motion));
            translationMap.put(MeasureDefs.infiniteMotionBackward, Integer.valueOf(R.string.anim_infinite_motion_backward));
            translationMap.put(MeasureDefs.infiniteMotionWhenPlaying, Integer.valueOf(R.string.anim_infinite_motion_when_playing));
            translationMap.put(MeasureDefs.beatPeak, Integer.valueOf(R.string.spinner_beat_peak));
            translationMap.put("X Amount", Integer.valueOf(R.string.param_x_amount));
            translationMap.put("Y Amount", Integer.valueOf(R.string.param_y_amount));
            translationMap.put("X Speed", Integer.valueOf(R.string.param_x_speed));
            translationMap.put("Y Speed", Integer.valueOf(R.string.param_y_speed));
            translationMap.put("Amount", Integer.valueOf(R.string.param_amount));
            translationMap.put("Speed", Integer.valueOf(R.string.param_speed));
            translationMap.put("Beat Amount", Integer.valueOf(R.string.param_beat_amount));
            translationMap.put("Smoothness", Integer.valueOf(R.string.param_smoothness));
            translationMap.put("Threshold", Integer.valueOf(R.string.param_threshold));
            translationMap.put("Range", Integer.valueOf(R.string.param_range));
            translationMap.put("Scale", Integer.valueOf(R.string.param_scale));
            translationMap.put("Trigger Time", Integer.valueOf(R.string.param_trigger_time));
        }

        public static String get(CustomizeVisDialog customizeVisDialog, String str) {
            if (str != null && customizeVisDialog != null) {
                if (cache.containsKey(str)) {
                    return cache.get(str);
                }
                Integer num = translationMap.get(str);
                if (num != null) {
                    try {
                        String string = customizeVisDialog.getString(num.intValue());
                        cache.put(str, string);
                        return string;
                    } catch (Exception e) {
                        return str;
                    }
                }
                return str;
            }
            return str;
        }

        public static String[] mapArray(CustomizeVisDialog customizeVisDialog, String[] strArr) {
            if (strArr != null && customizeVisDialog != null) {
                String[] strArr2 = new String[strArr.length];
                for (int i = 0; i < strArr.length; i++) {
                    strArr2[i] = get(customizeVisDialog, strArr[i]);
                }
                return strArr2;
            }
            return strArr;
        }
    }

    public CustomizeVisView1MVar(CustomizeVisDialog customizeVisDialog) {
        super(customizeVisDialog);
    }

    public static void createPropertyViewMVarFloat(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, final boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, final boolean z2, String str, final String str2, final float f, final float f2, final float f3) {
        MVariableFloat mVariableFloat;
        final String[] strArr;
        final MVariableFloat mVariableFloat2 = new MVariableFloat(new MeasuredVar(MeasureDefs.Constant, f, f));
        String[] strArr2 = z2 ? MeasureDefs.measures2dMVar : MeasureDefs.measures1dMVar;
        MVariableFloat propertyMVariableFloat = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
        if (propertyMVariableFloat.getMeasuredVarCount() == 0) {
            MVariableFloat mVariableFloat3 = new MVariableFloat(mVariableFloat2);
            customPropertiesList.putPropertyMVariableFloatValue(str2, mVariableFloat3);
            mVariableFloat = mVariableFloat3;
        } else {
            mVariableFloat = propertyMVariableFloat;
        }
        String str3 = StringMapper.get(customizeVisDialog, str);
        View viewInflate = View.inflate(customizeVisDialog.getActivity(), z ? R.layout.dialog_customize_vis_elementdetail_var_bar : R.layout.dialog_customize_vis_element_var_bar, null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str3);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txtValue);
        final View viewFindViewById = viewInflate.findViewById(R.id.lineBarBg);
        final View viewFindViewById2 = viewInflate.findViewById(R.id.lineBarValue);
        final int measuredVarCount = mVariableFloat.getMeasuredVarCount();
        final SeekBar[] seekBarArr = new SeekBar[measuredVarCount];
        final SeekBar[] seekBarArr2 = new SeekBar[measuredVarCount];
        final EditText[] editTextArr = new EditText[measuredVarCount];
        final EditText[] editTextArr2 = new EditText[measuredVarCount];
        final TextView[] textViewArr = new TextView[measuredVarCount];
        final TextView[] textViewArr2 = new TextView[measuredVarCount];
        final Spinner[] spinnerArr = new Spinner[measuredVarCount];
        if (z) {
            Button button = (Button) viewInflate.findViewById(R.id.btnAdd);
            Button button2 = (Button) viewInflate.findViewById(R.id.btnRemove);
            button.setText(customizeVisDialog.getString(R.string.button_new));
            button2.setText(customizeVisDialog.getString(R.string.button_delete));
            ViewGroup viewGroup2 = (ViewGroup) viewInflate.findViewById(R.id.varContainer);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                    float f4 = f;
                    customPropertiesList.putPropertyMVariableFloatValue(str2, new MVariableFloat(propertyMVariableFloat2, new MeasuredVar(MeasureDefs.Constant, f4, f4)));
                    customizeVisDialog.onChildPropertyChanged();
                }
            });
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                    if (propertyMVariableFloat2.getMeasuredVarCount() > 1) {
                        customPropertiesList.putPropertyMVariableFloatValue(str2, new MVariableFloat(propertyMVariableFloat2, 0, propertyMVariableFloat2.getMeasuredVarCount() - 1));
                        customizeVisDialog.onChildPropertyChanged();
                    }
                }
            });
            strArr = strArr2;
            for (int i = 0; i < measuredVarCount; i++) {
                MeasuredVar measuredVar = mVariableFloat.getMeasuredVar(i);
                View viewInflate2 = View.inflate(customizeVisDialog.getActivity(), R.layout.dialog_customize_vis_element_var_bar_elem, null);
                seekBarArr[i] = (SeekBar) viewInflate2.findViewById(R.id.seekBar);
                seekBarArr2[i] = (SeekBar) viewInflate2.findViewById(R.id.seekBar2);
                editTextArr[i] = (EditText) viewInflate2.findViewById(R.id.editTxt);
                editTextArr2[i] = (EditText) viewInflate2.findViewById(R.id.editTxt2);
                textViewArr[i] = (TextView) viewInflate2.findViewById(R.id.hintA);
                textViewArr2[i] = (TextView) viewInflate2.findViewById(R.id.hintB);
                spinnerArr[i] = (Spinner) viewInflate2.findViewById(R.id.spinnerType);
                viewGroup2.addView(viewInflate2);
                int i2 = (int) ((f2 - f) / f3);
                seekBarArr[i].setMax(i2);
                seekBarArr2[i].setMax(i2);
                spinnerArr[i].setAdapter((SpinnerAdapter) new ArrayAdapter(customizeVisDialog.getActivity(), android.R.layout.simple_spinner_item, StringMapper.mapArray(customizeVisDialog, strArr)));
                int iStringToIndex = Utils.StringToIndex(strArr, measuredVar.getMeasure());
                if (iStringToIndex >= 0) {
                    spinnerArr[i].setSelection(iStringToIndex, false);
                }
                customizeVisDialog.ApplySpinnerTheme(spinnerArr[i]);
            }
        } else {
            strArr = strArr2;
        }
        final boolean[] zArr = new boolean[1];
        final Action1<Object> action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.3
            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                boolean[] zArr2 = zArr;
                if (!zArr2[0]) {
                    zArr2[0] = true;
                    MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                    if (z2) {
                        textView.setText(propertyMVariableFloat2.getDisplayString());
                    } else {
                        textView.setText(propertyMVariableFloat2.getDisplayString1d());
                    }
                    SimpleViewProgressBar.SetProgress(viewFindViewById, viewFindViewById2, (f2 - f) / f3, (propertyMVariableFloat2.getDisplayValueX() - f) / f3);
                    if (z) {
                        for (int i3 = 0; i3 < measuredVarCount; i3++) {
                            final int i3f = i3;
                            MeasuredVar measuredVar2 = propertyMVariableFloat2.getMeasuredVar(i3f);
                            MeasureDefs.MeasureDef measureDefByName = MeasureDefs.getMeasureDefByName(measuredVar2.getMeasure());
                            boolean z3 = measureDefByName == null || z2 || measureDefByName.isBUsedFor1d;
                            if (z3) {
                                Vec2f measureArgVec2f = measuredVar2.getMeasureArgVec2f();
                                if (!seekBarArr[i3f].equals(obj)) {
                                    seekBarArr[i3f].setProgress((int) ((measureArgVec2f.x - f) / f3));
                                }
                                if (!seekBarArr2[i3f].equals(obj)) {
                                    seekBarArr2[i3f].setProgress((int) ((measureArgVec2f.y - f) / f3));
                                }
                                if (!editTextArr[i3f].equals(obj)) {
                                    editTextArr[i3f].setText(String.format(Locale.US, "%.3f", Float.valueOf(measureArgVec2f.x)));
                                }
                                if (!editTextArr2[i3f].equals(obj)) {
                                    editTextArr2[i3f].setText(String.format(Locale.US, "%.3f", Float.valueOf(measureArgVec2f.y)));
                                }
                            } else {
                                float measureArgF = measuredVar2.getMeasureArgF();
                                if (!seekBarArr[i3f].equals(obj)) {
                                    seekBarArr[i3f].setProgress((int) ((measureArgF - f) / f3));
                                }
                                if (!editTextArr[i3f].equals(obj)) {
                                    editTextArr[i3f].setText(String.format(Locale.US, "%.3f", Float.valueOf(measureArgF)));
                                }
                            }
                            if (!spinnerArr[i3f].equals(obj)) {
                                spinnerArr[i3f].setSelection(Utils.StringToIndex(strArr, measuredVar2.getMeasure()), false);
                            }
                            if (measureDefByName != null) {
                                textViewArr[i3f].setText(StringMapper.get(customizeVisDialog, measureDefByName.argAHint));
                                textViewArr2[i3f].setText(StringMapper.get(customizeVisDialog, measureDefByName.argBHint));
                            } else {
                                textViewArr[i3f].setText("");
                                textViewArr2[i3f].setText("");
                            }
                            seekBarArr2[i3f].setVisibility(z3 ? 0 : 8);
                            editTextArr2[i3f].setVisibility(z3 ? 0 : 8);
                            textViewArr2[i3f].setVisibility(z3 ? 0 : 8);
                        }
                    }
                    zArr[0] = false;
                }
            }
        });
        if (z) {
            for (int i3 = 0; i3 < measuredVarCount; i3++) {
                final int i3f = i3;
                seekBarArr[i3f].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.4
                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar, int i4, boolean z3) {
                        if (!zArr[0] && z3) {
                            float f4 = (i4 * f3) + f;
                            MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                            if (i3f < propertyMVariableFloat2.getMeasuredVarCount()) {
                                MeasuredVar measuredVar2 = propertyMVariableFloat2.getMeasuredVar(i3f);
                                propertyMVariableFloat2.setMeasuredVar(i3f, new MeasuredVar(measuredVar2.getMeasure(), f4, measuredVar2.getMeasureArgVec2f().y));
                                customPropertiesList.putPropertyMVariableFloatValue(str2, propertyMVariableFloat2);
                                customizeVisDialog.onPropertyChanged();
                                action1CreatePropertyCommon.onInvoke(seekBarArr[i3f]);
                                Action1 action1 = (Action1) func.onInvoke();
                                if (action1 != null) {
                                    action1.onInvoke(null);
                                }
                            }
                        }
                    }
                });
                seekBarArr2[i3f].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.5
                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar, int i4, boolean z3) {
                        if (!zArr[0] && z3) {
                            float f4 = (i4 * f3) + f;
                            MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                            if (i3f < propertyMVariableFloat2.getMeasuredVarCount()) {
                                MeasuredVar measuredVar2 = propertyMVariableFloat2.getMeasuredVar(i3f);
                                propertyMVariableFloat2.setMeasuredVar(i3f, new MeasuredVar(measuredVar2.getMeasure(), measuredVar2.getMeasureArgVec2f().x, f4));
                                customPropertiesList.putPropertyMVariableFloatValue(str2, propertyMVariableFloat2);
                                customizeVisDialog.onPropertyChanged();
                                action1CreatePropertyCommon.onInvoke(seekBarArr2[i3f]);
                                Action1 action1 = (Action1) func.onInvoke();
                                if (action1 != null) {
                                    action1.onInvoke(null);
                                }
                            }
                        }
                    }
                });
                editTextArr[i3f].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.6
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    }

                    public int hashCode() {
                        return super.hashCode();
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (!zArr[0]) {
                            float fStrToFloatSafe = Utils.strToFloatSafe(editable.toString(), f + (f2 * 0.5f));
                            MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                            if (i3f < propertyMVariableFloat2.getMeasuredVarCount()) {
                                MeasuredVar measuredVar2 = propertyMVariableFloat2.getMeasuredVar(i3f);
                                propertyMVariableFloat2.setMeasuredVar(i3f, new MeasuredVar(measuredVar2.getMeasure(), fStrToFloatSafe, measuredVar2.getMeasureArgVec2f().y));
                                customPropertiesList.putPropertyMVariableFloatValue(str2, propertyMVariableFloat2);
                                customizeVisDialog.onPropertyChanged();
                                action1CreatePropertyCommon.onInvoke(editTextArr[i3f]);
                                Action1 action1 = (Action1) func.onInvoke();
                                if (action1 != null) {
                                    action1.onInvoke(null);
                                }
                            }
                        }
                    }
                });
                editTextArr2[i3f].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.7
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    }

                    public int hashCode() {
                        return super.hashCode();
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (!zArr[0]) {
                            float fStrToFloatSafe = Utils.strToFloatSafe(editable.toString(), f + (f2 * 0.5f));
                            MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                            if (i3f < propertyMVariableFloat2.getMeasuredVarCount()) {
                                MeasuredVar measuredVar2 = propertyMVariableFloat2.getMeasuredVar(i3f);
                                propertyMVariableFloat2.setMeasuredVar(i3f, new MeasuredVar(measuredVar2.getMeasure(), measuredVar2.getMeasureArgVec2f().x, fStrToFloatSafe));
                                customPropertiesList.putPropertyMVariableFloatValue(str2, propertyMVariableFloat2);
                                customizeVisDialog.onPropertyChanged();
                                action1CreatePropertyCommon.onInvoke(editTextArr2[i3f]);
                                Action1 action1 = (Action1) func.onInvoke();
                                if (action1 != null) {
                                    action1.onInvoke(null);
                                }
                            }
                        }
                    }
                });
                final String[] strArr3 = strArr;
                final int i4 = i3f;
                spinnerArr[i3f].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1MVar.8
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i5, long j) {
                        String str4;
                        customizeVisDialog.ApplySpinnerTheme(adapterView);
                        if (i5 >= 0 && i5 < strArr3.length) {
                            str4 = strArr3[i5];
                        } else {
                            str4 = MeasureDefs.Constant;
                        }
                        MVariableFloat propertyMVariableFloat2 = customPropertiesList.getPropertyMVariableFloat(str2, new MVariableFloat(mVariableFloat2));
                        if (i4 < propertyMVariableFloat2.getMeasuredVarCount()) {
                            propertyMVariableFloat2.setMeasuredVar(i4, new MeasuredVar(str4, propertyMVariableFloat2.getMeasuredVar(i4).getMeasureArgVec2f()));
                            customPropertiesList.putPropertyMVariableFloatValue(str2, propertyMVariableFloat2);
                            customizeVisDialog.onPropertyChanged();
                            action1CreatePropertyCommon.onInvoke(spinnerArr[i4]);
                            Action1 action1 = (Action1) func.onInvoke();
                            if (action1 != null) {
                                action1.onInvoke(null);
                            }
                        }
                    }
                });
            }
        }
        viewGroup.addView(viewInflate);
    }
}
