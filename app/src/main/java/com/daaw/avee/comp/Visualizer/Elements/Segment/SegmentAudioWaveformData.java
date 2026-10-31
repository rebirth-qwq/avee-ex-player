package com.daaw.avee.comp.Visualizer.Elements.Segment;

import com.daaw.avee.Common.ISimpleListDouble;
import com.daaw.avee.Common.ISimpleListFloat;
import com.daaw.avee.Common.ISimpleListShort;
import com.daaw.avee.Common.SimpleListConstantShort;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Dsp.DspWindows;
import com.daaw.avee.comp.Visualizer.Dsp.SignalFilter1dv2;
import com.daaw.avee.comp.Visualizer.Elements.Base.IFrameDataProvider;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider;
import com.daaw.avee.comp.playback.AudioFrameData;

/* JADX INFO: loaded from: classes.dex */
public class SegmentAudioWaveformData implements ISegmentDataProvider, IFrameDataProvider {
    public static final String typeName = "Waveform";
    private float[] barVals2 = new float[2];
    private float rms = 0.0f;
    private float beat = 0.0f;
    private float[] pcmRangeTmp = new float[2];
    private int sampleInCountPower = 11;
    private int sampleOutCountPower = 8;
    private final float rangeTarget = 300.0f;
    private final float rangeSmoothAttackFactor = 0.9f;
    private final float rangeSmoothDecayFactor = 0.01f;
    private float rangeHiSmooth = 1000.0f;
    private float rangeLoSmooth = 0.0f;
    private float rangeSmoothMul = 1.0f;
    private float frameSmooth = 0.0f;
    private float[] smoothBuffer = new float[0];
    private float filterSmooth = 0.0f;
    private SignalFilter1dv2 waveformFilter = SignalFilter1dv2.createHighPass(1, 0.5f);
    ISimpleListFloat barValsList = new ISimpleListFloat() { // from class: com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioWaveformData.1
        @Override // com.daaw.avee.Common.ISimpleListFloat
        public int size() {
            return SegmentAudioWaveformData.this.barVals2.length;
        }

        @Override // com.daaw.avee.Common.ISimpleListFloat
        public float get(int i) {
            return SegmentAudioWaveformData.this.barVals2[i];
        }
    };

    private float[] applyFrameSmooth(float[] fArr, float f) {
        if (this.frameSmooth > 0.0f && fArr.length != 0) {
            if (this.smoothBuffer.length != fArr.length) {
                this.smoothBuffer = new float[fArr.length];
                System.arraycopy(fArr, 0, this.smoothBuffer, 0, fArr.length);
                return fArr;
            }
            float fMin = Math.min(20.0f * f * this.frameSmooth, 1.0f);
            float[] fArr2 = new float[fArr.length];
            for (int i = 0; i < fArr.length; i++) {
                float f2 = fArr[i] - this.smoothBuffer[i];
                float[] fArr3 = this.smoothBuffer;
                fArr3[i] = (f2 * fMin) + fArr3[i];
                fArr2[i] = this.smoothBuffer[i];
            }
            return fArr2;
        }
        return fArr;
    }

    private float[] applyFilterSmooth(final float[] fArr) {
        int i = (int) this.filterSmooth;
        if (i > 0 && fArr.length != 0) {
            SignalFilter1dv2 signalFilter1dv2CreateHighPass = SignalFilter1dv2.createHighPass(i <= 5 ? i : 5, 0.5f);
            ISimpleListDouble iSimpleListDouble = new ISimpleListDouble() { // from class: com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioWaveformData.2
                @Override // com.daaw.avee.Common.ISimpleListDouble
                public int size() {
                    return fArr.length;
                }

                @Override // com.daaw.avee.Common.ISimpleListDouble
                public double get(int i2) {
                    if (i2 < 0) {
                        return fArr[0];
                    }
                    return i2 >= fArr.length ? fArr[fArr.length - 1] : fArr[i2];
                }
            };
            float[] fArr2 = new float[fArr.length];
            for (int i2 = 0; i2 < fArr.length; i2++) {
                fArr2[i2] = (float) signalFilter1dv2CreateHighPass.getSoftedClamped(i2, iSimpleListDouble);
            }
            return fArr2;
        }
        return fArr;
    }

    public void setSampleInCountPower(int i) {
        this.sampleInCountPower = i;
    }

    public void setSampleOutCountPower(int i) {
        this.sampleOutCountPower = i;
    }

    public void setFrameSmooth(float f) {
        this.frameSmooth = Math.max(0.0f, Math.min(1.0f, f));
    }

    public void setFilterSmooth(float f) {
        this.filterSmooth = Math.max(0.0f, Math.min(5.0f, f));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider
    public void process(IRenderState iRenderState, InternalVisualizationDataProvider internalVisualizationDataProvider) {
        ISimpleListShort simpleListConstantShort;
        int size;
        int i = 4;
        int i2 = 0;
        int iMin = Math.min(Math.max(1 << this.sampleInCountPower, 32), 16384);
        int iMin2 = Math.min(Math.max(1 << this.sampleOutCountPower, 8), 512);
        AudioFrameData audioFrameDataOnRequestSoundVisualizationData = internalVisualizationDataProvider.onRequestSoundVisualizationData(iMin);
        if (audioFrameDataOnRequestSoundVisualizationData != null) {
            simpleListConstantShort = audioFrameDataOnRequestSoundVisualizationData.pcmAsMonoForFft_DoubleDataRate();
            size = simpleListConstantShort.size();
            float[] fArr = this.pcmRangeTmp;
            fArr[0] = 0.0f;
            fArr[1] = audioFrameDataOnRequestSoundVisualizationData.maxPcmValue;
            this.rms = audioFrameDataOnRequestSoundVisualizationData.rms;
        } else {
            simpleListConstantShort = new SimpleListConstantShort((short) 0, 4);
            float[] fArr2 = this.pcmRangeTmp;
            fArr2[0] = 0.0f;
            fArr2[1] = 1000.0f;
            this.rms = 0.0f;
            size = 4;
        }
        if (size < 4) {
            simpleListConstantShort = new SimpleListConstantShort((short) 0, 4);
        } else {
            i = size;
        }
        if (this.barVals2.length != iMin2) {
            this.barVals2 = new float[iMin2];
        }
        updateRange(this.pcmRangeTmp);
        float length = i / this.barVals2.length;
        for (int i3 = 0; i3 < this.barVals2.length; i3++) {
            this.barVals2[i3] = 0.0f;
        }
        this.beat = 0.0f;
        if (length <= 1.0f) {
            while (i2 < this.barVals2.length) {
                int iFloor = (int) Math.floor(i2 * length);
                if (iFloor >= i) {
                    iFloor = i - 1;
                }
                float fKaiserWindow = simpleListConstantShort.get(iFloor) * DspWindows.kaiserWindow((int) Math.floor(i2 * length), i, 0.8f) * 0.00390625f;
                float f = this.rangeSmoothMul;
                float f2 = fKaiserWindow * f * 100.0f;
                this.beat = Math.max(this.beat, Math.abs(f * f2));
                float[] fArr3 = this.barVals2;
                fArr3[i2] = f2 + fArr3[i2];
                fArr3[i2] = fArr3[i2] * 0.5f;
                i2++;
            }
        } else {
            for (int i4 = 0; i4 < i; i4++) {
                float fKaiserWindow2 = simpleListConstantShort.get(i4) * DspWindows.kaiserWindow(i4, i, 0.8f) * 0.00390625f;
                float f3 = this.rangeSmoothMul;
                float f4 = fKaiserWindow2 * f3 * 100.0f;
                this.beat = Math.max(this.beat, Math.abs(f3 * f4));
                int iFloor2 = (int) Math.floor(i4 / length);
                if (iFloor2 < this.barVals2.length) {
                    this.barVals2[iFloor2] = f4 + this.barVals2[iFloor2];
                }
            }
            while (i2 < this.barVals2.length) {
                this.barVals2[i2] = this.barVals2[i2] / length;
                i2++;
            }
        }
        float frameTimeF = iRenderState.getFrameTimeF();
        if (this.frameSmooth > 0.0f) {
            this.barVals2 = applyFrameSmooth(this.barVals2, frameTimeF);
        }
        if (this.filterSmooth > 0.0f) {
            this.barVals2 = applyFilterSmooth(this.barVals2);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider
    public float[] getFrameValues(int i, int i2) {
        return this.barVals2;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider
    public ISimpleListFloat createFrameValuesAccessorList(int i, int i2, int i3, ISimpleListFloat iSimpleListFloat) {
        return this.barValsList;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.IFrameDataProvider
    public float getRms() {
        return this.beat;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.IFrameDataProvider
    public float getPeakBarIndex() {
        float f = 0.0f;
        int i = 0;
        float f2 = 0.0f;
        while (true) {
            float f3 = f;
            if (i < this.barVals2.length) {
                float fAbs = Math.abs(this.barVals2[i]);
                if (fAbs > f2) {
                    f = i;
                    f2 = fAbs;
                } else {
                    f = f3;
                }
                i++;
            } else {
                return f3 / this.barVals2.length;
            }
        }
    }

    private void updateRange(float[] fArr) {
        this.rangeLoSmooth = (this.rangeLoSmooth * 0.100000024f) + (fArr[0] * 0.9f);
        float f = fArr[1];
        float f2 = this.rangeHiSmooth;
        if (f > f2) {
            this.rangeHiSmooth = (f2 * 0.100000024f) + (fArr[1] * 0.9f);
        } else {
            this.rangeHiSmooth = (0.99f * f2) + (fArr[1] * 0.01f);
        }
        float f3 = this.rangeHiSmooth - this.rangeLoSmooth;
        this.rangeSmoothMul = f3;
        if (f3 < 1.0f) {
            this.rangeSmoothMul = 1.0f;
        }
        this.rangeSmoothMul = 300.0f / this.rangeSmoothMul;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        setSampleInCountPower(customPropertiesList.getPropertyInt("inputSize", 11));
        setSampleOutCountPower(customPropertiesList.getPropertyInt("sampleOutCountPower", 8));
        setFrameSmooth(customPropertiesList.getPropertyFloat("frameSmooth", 0.0f));
        setFilterSmooth(customPropertiesList.getPropertyFloat("filterSmooth", 0.0f));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onReadCustomization(CustomPropertiesList customPropertiesList) {
        customPropertiesList.putPropertyIntAsRange("inputSize", this.sampleInCountPower, "Performance", 5, 13);
        customPropertiesList.putPropertyIntAsRange("sampleOutCountPower", this.sampleOutCountPower, typeName, 3, 9);
        customPropertiesList.putPropertyFloatAsRange("frameSmooth", this.frameSmooth, "Smooth", 0.0f, 1.0f);
        customPropertiesList.putPropertyFloatAsRange("filterSmooth", this.filterSmooth, "Smooth", 0.0f, 5.0f);
    }
}
