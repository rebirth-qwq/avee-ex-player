package com.daaw.avee.comp.Visualizer.Elements.Segment;

import com.daaw.avee.Common.ISimpleListDouble;
import com.daaw.avee.Common.ISimpleListFloat;
import com.daaw.avee.Common.ISimpleListShort;
import com.daaw.avee.Common.Signal;
import com.daaw.avee.Common.SimpleListConstantShort;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Dsp.DspCurves;
import com.daaw.avee.comp.Visualizer.Dsp.DspWindows;
import com.daaw.avee.comp.Visualizer.Dsp.PhaseSpectrum.PhaseSpectrum;
import com.daaw.avee.comp.Visualizer.Dsp.RangeBox;
import com.daaw.avee.comp.Visualizer.Dsp.SignalFilter1dv2;
import com.daaw.avee.comp.Visualizer.Elements.Base.FrameValuesAccumulator;
import com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable;
import com.daaw.avee.comp.Visualizer.Elements.Base.IFrameDataProvider;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.VFrameBuffer;
import com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider;
import com.daaw.avee.comp.playback.AudioFrameData;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;

/* JADX INFO: loaded from: classes.dex */
public class SegmentAudioSpectrumDataPhaseS implements ISegmentDataProvider, IFrameDataProvider {
    static final boolean debug = false;
    public static final String typeName = "Spectrum2";
    FrameValuesAccumulator frameValuesAccumulator = new FrameValuesAccumulator();
    AudioProcess audioProcess = new AudioProcess();
    final BlockingQueue<ProcessingData> processingQueue = new ArrayBlockingQueue(1);
    final BlockingQueue<float[]> processingResultQueue = new ArrayBlockingQueue(2);
    Thread audioProcessTh = null;

    public void setSampleInCountPower(int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    class AudioProcess implements ICustomizable {
        private static final int KEEP_ALIVE_TIME = 1;
        ISimpleListDouble fftResultAccess;
        PhaseSpectrum[] fftTransform;
        private float hiFreq;
        private float loFreq;
        ThreadPoolExecutor mDecodeThreadPool;
        private int dataCountInMs = 90;
        private boolean highQualityAudioCapture = false;
        private double[] fftMag = new double[1];
        private float[] fftMagSmooth = new float[1];
        private float[] pcmRangeTmp = new float[2];
        private int starBar = 0;
        private int endBar = 0;
        private float freqShift = 0.0f;
        private int barsCount = 64;
        private float[] barSmoothValues = new float[1];
        private float[] barValues = new float[1];
        private float[] barFreq = new float[1];
        private float[] barFreqNext = new float[1];
        private float[] beatBarValue = new float[1];
        private float beatBarValueSmooth = 0.0f;
        private boolean mirrorSampleValues = false;
        private int repeatSamples = 1;
        private float smoothFactor = 1.0f;
        private final float rangeTarget = 300.0f;
        private final float rangeSmoothAttackFactor = 0.9f;
        private final float rangeSmoothDecayFactor = 0.01f;
        private float beatSmoothFactor = 0.4f;
        private int rmsTargetBar = 4;
        private float windowedRms = 0.0f;
        private float rangeHiSmooth = 1000.0f;
        private float rangeLoSmooth = 0.0f;
        private float rangeSmoothMul = 1.0f;
        private int[] fftSoftnessIndexes = new int[0];
        private float[] fftSoftnessWeight = new float[0];
        private float fftSoftnessDivider = 1.0f;
        private int fftSoftnessWidth = 2;
        private float freqLinfactor = 0.0f;
        private float aweightFft = 0.2f;
        private float outputMultiplier = 1.0f;
        private boolean frequencyBinSliding = true;
        float[] fftWindowMultipliers = new float[0];
        private float peakBarSmoothFactor = 0.5f;
        private float peakBarIndex01 = 0.0f;
        private float peakBarIndex01Smooth = 0.0f;
        RangeBox beatRange = new RangeBox(0.5f);
        SignalFilter1dv2 fftFilter = SignalFilter1dv2.createHighPass(1, 1.0f);
        double[] fftResultDouble = new double[0];
        int fftTransformInitializeSize = 0;
        double[] binMagnitudes = new double[0];
        final Object lockObj = new Object();
        private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        private final BlockingQueue<Runnable> mDecodeWorkQueue = new LinkedBlockingQueue();
        private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        int lastSampleRate = 44100;

        public void SpectrumProcessTh(double[] dArr, int i, int i2) {
        }

        class FftSimpleListDouble implements ISimpleListDouble {
            AudioProcess parent;

            public FftSimpleListDouble(AudioProcess audioProcess) {
                this.parent = audioProcess;
            }

            @Override // com.daaw.avee.Common.ISimpleListDouble
            public int size() {
                return this.parent.binMagnitudes.length;
            }

            @Override // com.daaw.avee.Common.ISimpleListDouble
            public double get(int i) {
                return this.parent.binMagnitudes[i];
            }
        }

        public AudioProcess() {
            int iMax = Math.max(1, this.NUMBER_OF_CORES - 3);
            this.fftTransform = new PhaseSpectrum[iMax];
            int i = 0;
            while (true) {
                PhaseSpectrum[] phaseSpectrumArr = this.fftTransform;
                if (i >= phaseSpectrumArr.length) {
                    this.fftResultAccess = new FftSimpleListDouble(this);
                    setSampleOutCount(64, 0.06f, 0);
                    setFftSoftness(1);
                    setBeatRangeCorners(0.0f, 0.2f, 0.7f, 35.0f);
                    setFftFilter(3, 1.0f);
                    this.mDecodeThreadPool = new ThreadPoolExecutor(iMax, iMax, 1L, this.KEEP_ALIVE_TIME_UNIT, this.mDecodeWorkQueue);
                    return;
                }
                phaseSpectrumArr[i] = new PhaseSpectrum();
                i++;
            }
        }

        private void setSampleInCountAsMilliseconds(int i) {
            this.dataCountInMs = i;
        }

        private void setHighQualityAudioCapture(boolean z) {
            this.highQualityAudioCapture = z;
        }

        public void setSampleOutCount(int i, float f, int i2) {
            setSampleOutCount(i, 20.0f, 18000.0f, false, 1, f, i2, i2, 0.0f);
        }

        private void setSampleOutCount(int i, boolean z, int i2) {
            setSampleOutCount(i, 20.0f, 18000.0f, z, i2, 0.06f, 0, 0, 0.0f);
        }

        private void setSampleOutCount(int i, float f, float f2, boolean z, int i2, float f3) {
            setSampleOutCount(i, f, f2, z, i2, f3, 0, 0, 0.0f);
        }

        public void setSampleOutCount(int i, float f, float f2, boolean z, int i2, float f3, int i3, int i4, float f4) {
            synchronized (this.lockObj) {
                this.loFreq = Math.max(f, 0.1f);
                this.hiFreq = Math.max(f2, 0.1f);
                this.barsCount = Math.max(1, i);
                this.mirrorSampleValues = z;
                this.repeatSamples = Math.max(i2, 1);
                this.freqLinfactor = f3;
                this.barFreq = new float[this.barsCount];
                this.barFreqNext = new float[this.barsCount];
                this.starBar = i3;
                this.endBar = i4;
                this.freqShift = f4;
                this.binMagnitudes = new double[this.barsCount];
                updateBandsFreq(this.barsCount, this.loFreq, f2, z, f3, i3, i4, f4);
                setBeatBart(6);
                for (int i5 = 0; i5 < this.fftTransform.length; i5++) {
                    this.fftTransform[i5].Initialize(this.barFreq, 44100);
                }
            }
        }

        public void setSmoothFactor(float f) {
            synchronized (this.lockObj) {
                this.smoothFactor = f;
            }
        }

        public void setBeatBart(int i) {
            synchronized (this.lockObj) {
                this.rmsTargetBar = i;
            }
        }

        public void setDataMode(int i) {
            synchronized (this.lockObj) {
            }
        }

        public void setSmooth(float f) {
            synchronized (this.lockObj) {
                this.smoothFactor = Math.max(f, 0.01f);
            }
        }

        public void setSteadySmooth(float f) {
            synchronized (this.lockObj) {
            }
        }

        public void setFftSoftness(int i) {
            synchronized (this.lockObj) {
                if (i < 1) {
                    i = 1;
                }
                int[] iArr = new int[i + 1 + i];
                this.fftSoftnessIndexes = iArr;
                this.fftSoftnessWeight = new float[iArr.length];
                this.fftSoftnessDivider = iArr.length;
                for (int i2 = 0; i2 < this.fftSoftnessIndexes.length; i2++) {
                    int i3 = i2 - i;
                    this.fftSoftnessIndexes[i2] = i3;
                    this.fftSoftnessWeight[i2] = DspWindows.hammingWindow(((this.fftSoftnessIndexes.length + 2) / 2) + i3, this.fftSoftnessIndexes.length + 2);
                }
            }
        }

        public void setFftFilter(int i, float f) {
            synchronized (this.lockObj) {
                this.fftFilter = SignalFilter1dv2.createHighPass(i, f);
            }
        }

        public void setFrequencyBinSliding(boolean z) {
            synchronized (this.lockObj) {
                this.frequencyBinSliding = z;
            }
        }

        public void setBeatSmooth(float f) {
            synchronized (this.lockObj) {
                this.beatSmoothFactor = f;
            }
        }

        public void setBeatRangeCorners(float f, float f2, float f3, float f4) {
            synchronized (this.lockObj) {
                this.beatRange.setCorners(f, f2, f3, f4);
            }
        }

        public float[] processTh(float f, InternalVisualizationDataProvider internalVisualizationDataProvider) {
            ISimpleListShort simpleListConstantShort;
            float f2;
            boolean z;
            int size;
            int i;
            int i2;
            int i3;
            float[] fArr;
            synchronized (this.lockObj) {
                int i4 = (int) (this.lastSampleRate * 0.001f * this.dataCountInMs);
                int iOnRequestOverrideAudioFrameSamplesInCount = internalVisualizationDataProvider.onRequestOverrideAudioFrameSamplesInCount();
                if (iOnRequestOverrideAudioFrameSamplesInCount > 0) {
                    i4 = iOnRequestOverrideAudioFrameSamplesInCount;
                }
                AudioFrameData audioFrameDataOnRequestSoundVisualizationData = internalVisualizationDataProvider.onRequestSoundVisualizationData(i4);
                char c = 0;
                if (audioFrameDataOnRequestSoundVisualizationData != null) {
                    if (this.highQualityAudioCapture) {
                        simpleListConstantShort = audioFrameDataOnRequestSoundVisualizationData.pcmAsMonoForFft();
                    } else {
                        simpleListConstantShort = audioFrameDataOnRequestSoundVisualizationData.pcmAsMonoForFft_DoubleDataRate();
                    }
                    size = simpleListConstantShort.size();
                    boolean zIsDoubleDataRate = audioFrameDataOnRequestSoundVisualizationData.IsDoubleDataRate();
                    i = audioFrameDataOnRequestSoundVisualizationData.sampleRate;
                    f2 = audioFrameDataOnRequestSoundVisualizationData.minPcmValue;
                    this.pcmRangeTmp[0] = 0.0f;
                    this.pcmRangeTmp[1] = audioFrameDataOnRequestSoundVisualizationData.maxPcmValue;
                    z = zIsDoubleDataRate;
                } else {
                    simpleListConstantShort = new SimpleListConstantShort((short) 0, 4);
                    f2 = -1000.0f;
                    this.pcmRangeTmp[0] = 0.0f;
                    this.pcmRangeTmp[1] = 1000.0f;
                    z = false;
                    size = 4;
                    i = 44100;
                }
                if (size < 4) {
                    simpleListConstantShort = new SimpleListConstantShort((short) 0, 4);
                    i3 = 4;
                    i2 = 44100;
                } else {
                    i2 = i;
                    i3 = size;
                }
                this.lastSampleRate = i2;
                if (this.barValues.length != this.barsCount) {
                    this.barValues = new float[this.barsCount];
                }
                if (this.barSmoothValues.length != this.barValues.length) {
                    this.barSmoothValues = new float[this.barValues.length];
                    for (int i5 = 0; i5 < this.barSmoothValues.length; i5++) {
                        this.barSmoothValues[i5] = 0.0f;
                    }
                }
                if (i3 != this.fftWindowMultipliers.length) {
                    this.fftWindowMultipliers = new float[i3];
                    for (int i6 = 0; i6 < i3; i6++) {
                        this.fftWindowMultipliers[i6] = DspWindows.hannWindow(i6, i3) * 0.00390625f;
                    }
                }
                updateRange(this.pcmRangeTmp);
                if (this.fftResultDouble.length != i3) {
                    this.fftResultDouble = new double[i3];
                }
                int i7 = 0;
                float fSqrt = 0.0f;
                while (i7 < this.fftResultDouble.length) {
                    float f3 = simpleListConstantShort.get(i7) * this.fftWindowMultipliers[i7] * this.rangeSmoothMul;
                    fSqrt += f3 * f3;
                    this.fftResultDouble[i7] = f3;
                    i7++;
                }
                if ((internalVisualizationDataProvider.onRequestAudioProcessMultiThreadValue() & 2) != 0) {
                    int length = this.fftTransform.length;
                    Signal[] signalArr = new Signal[length];
                    int i8 = 0;
                    while (i8 < this.fftTransform.length) {
                        signalArr[i8] = new Signal();
                        if (this.fftTransform[i8].sampleRate != i2) {
                            this.fftTransform[i8].Initialize(this.barFreq, i2);
                        }
                        final int i9 = this.fftTransform[c].binCount;
                        final int length2 = i9 / this.fftTransform.length;
                        final int i10 = i8;
                        final Signal[] signalArr2 = signalArr;
                        final float f4 = f2;
                        int i11 = length;
                        this.mDecodeThreadPool.execute(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.Elements.Segment.-$$Lambda$SegmentAudioSpectrumDataPhaseS$AudioProcess$GIkafdM5fP4FF5WYHEVXrL3TrdE
                            @Override // java.lang.Runnable
                            public final void run() {
                                lambda$processTh$0$SegmentAudioSpectrumDataPhaseS$AudioProcess(length2, i10, i9, f4, signalArr2);
                            }
                        });
                        i8 = i10 + 1;
                        length = i11;
                        signalArr = signalArr2;
                        c = 0;
                    }
                    Signal[] signalArr3 = signalArr;
                    int i12 = length;
                    for (int i13 = 0; i13 < i12; i13++) {
                        try {
                            signalArr3[i13].waitForSignal();
                        } catch (InterruptedException unused) {
                        }
                    }
                } else {
                    for (int i14 = 0; i14 < this.fftTransform.length; i14++) {
                        if (this.fftTransform[i14].sampleRate != i2) {
                            this.fftTransform[i14].Initialize(this.barFreq, i2);
                        }
                    }
                    this.fftTransform[0].Process(this.fftResultDouble, 0, this.fftTransform[0].binCount, this.binMagnitudes, f2, this.pcmRangeTmp[1]);
                }
                if (i3 > 0) {
                    fSqrt = (float) Math.sqrt(fSqrt / i3);
                }
                this.windowedRms = fSqrt;
                fillBarValues2(this.barValues, this.outputMultiplier, this.mirrorSampleValues, this.fftResultAccess, i2, new float[2], this.beatBarValue, z);
                this.beatRange.reset(this.barSmoothValues.length);
                Assert.assertEquals("barValues == barSmoothValues", this.barValues.length, this.barSmoothValues.length);
                this.peakBarIndex01 = 0.0f;
                float f5 = -10000.0f;
                for (int i15 = 0; i15 < this.barValues.length; i15++) {
                    if (this.barValues[i15] > f5) {
                        f5 = this.barValues[i15];
                        this.peakBarIndex01 = i15;
                    }
                    float f6 = this.barValues[i15] - this.barSmoothValues[i15];
                    float[] fArr2 = this.barSmoothValues;
                    fArr2[i15] = fArr2[i15] + (f6 * 20.0f * f * this.smoothFactor);
                    this.barSmoothValues[i15] = Math.max(this.barSmoothValues[i15], 0.0f);
                    this.beatRange.addValue(i15, this.barSmoothValues[i15]);
                }
                this.beatBarValue[0] = this.beatRange.getValueNormal();
                this.beatBarValueSmooth = (this.beatBarValueSmooth * (1.0f - this.beatSmoothFactor)) + (this.beatBarValue[0] * this.beatSmoothFactor);
                float length3 = this.peakBarIndex01 / this.barValues.length;
                this.peakBarIndex01 = length3;
                this.peakBarIndex01Smooth = (this.peakBarIndex01Smooth * (1.0f - this.peakBarSmoothFactor)) + (length3 * this.peakBarSmoothFactor);
                fArr = this.barSmoothValues;
            }
            return fArr;
        }

        public /* synthetic */ void lambda$processTh$0$SegmentAudioSpectrumDataPhaseS$AudioProcess(int i, int i2, int i3, float f, Signal[] signalArr) {
            int i4 = i * i2;
            if (i2 != this.fftTransform.length - 1) {
                i3 = (i2 + 1) * i;
            }
            this.fftTransform[i2].Process(this.fftResultDouble, i4, i3, this.binMagnitudes, f, this.pcmRangeTmp[1]);
            signalArr[i2].setSignal();
        }

        private void updateRange(float[] fArr) {
            this.rangeLoSmooth = (this.rangeLoSmooth * 0.100000024f) + (fArr[0] * 0.9f);
            float f = fArr[1];
            float f2 = this.rangeHiSmooth;
            if (f > f2) {
                this.rangeHiSmooth = (f2 * 0.100000024f) + (fArr[1] * 0.9f);
            } else {
                this.rangeHiSmooth = (f2 * 0.99f) + (fArr[1] * 0.01f);
            }
            float f3 = this.rangeHiSmooth - this.rangeLoSmooth;
            this.rangeSmoothMul = f3;
            if (f3 < 1.0f) {
                this.rangeSmoothMul = 1.0f;
            }
            this.rangeSmoothMul = 300.0f / this.rangeSmoothMul;
        }

        private void fillBarValues2(float[] fArr, float f, boolean z, ISimpleListDouble iSimpleListDouble, int i, float[] fArr2, float[] fArr3, boolean z2) {
            int i2 = 0;
            if (fArr.length < 4) {
                while (i2 < fArr.length) {
                    fArr[i2] = 0.0f;
                    i2++;
                }
                return;
            }
            fArr2[0] = 9999990.0f;
            fArr2[1] = -9999990.0f;
            if (this.fftMag.length != iSimpleListDouble.size()) {
                this.fftMag = new double[iSimpleListDouble.size()];
            }
            Assert.assertTrue(fArr.length == this.fftMag.length);
            float f2 = this.aweightFft;
            float f3 = 1.0f - f2;
            while (i2 < this.fftMag.length) {
                fArr[i2] = Math.max((float) this.fftFilter.getSoftedClamped(i2, iSimpleListDouble), 0.0f) * ((((float) DspCurves.aweight(Math.min(2600.0d, this.barFreq[i2]))) * 0.1f * f2) + f3) * f;
                i2++;
            }
        }

        private void updateBandsFreq(int i, float f, float f2, boolean z, float f3, int i2, int i3, float f4) {
            int iMax = Math.max(0, i2);
            int iMax2 = Math.max(0, i3);
            boolean z2 = z && i > 1;
            int i4 = z2 ? i / 2 : i;
            int i5 = ((z2 ? i / 2 : i) - iMax) - iMax2;
            int iMax3 = Math.max(i5 / this.repeatSamples, 1);
            for (int i6 = 0; i6 < i; i6++) {
                this.barFreq[i6] = 0.0f;
            }
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            float f5 = Float.MAX_VALUE;
            float f6 = 0.0f;
            while (i8 < i5) {
                int i10 = ((i8 - ((int) (i5 * f4))) + i5) % iMax3;
                int i11 = i8 + iMax;
                int i12 = i4;
                int i13 = iMax;
                int i14 = i7;
                int i15 = i8;
                int i16 = i9;
                int i17 = iMax3;
                int i18 = i5;
                this.barFreq[i11] = (float) logspace(f, f2, i10, i17);
                float f7 = f + ((f2 - f) * i10 * (1.0f / i17));
                float[] fArr = this.barFreq;
                fArr[i11] = (fArr[i11] * (1.0f - f3)) + (f7 * f3);
                if (f5 > fArr[i11]) {
                    f5 = fArr[i11];
                    i9 = i11;
                } else {
                    i9 = i16;
                }
                float[] fArr2 = this.barFreq;
                if (f6 < fArr2[i11]) {
                    f6 = fArr2[i11];
                    i7 = i11;
                } else {
                    i7 = i14;
                }
                i8 = i15 + 1;
                i4 = i12;
                iMax3 = i17;
                i5 = i18;
                iMax = i13;
            }
            int i19 = i7;
            int i20 = i9;
            int i21 = iMax;
            int i22 = i4;
            int i23 = i5;
            int i24 = 0;
            while (i24 < i) {
                int i25 = i21;
                float f8 = (i24 <= i25 || i24 == i20) ? this.barFreq[i24] : this.barFreq[((i24 - 1) + i) % i];
                int i26 = i19;
                float f9 = (i24 >= (i23 - iMax2) - 1 || i24 == i26) ? this.barFreq[i24] : this.barFreq[(i24 + 1) % i];
                this.barFreqNext[i24] = Math.min(f8, f9);
                if (f8 < 0.0f) {
                    if (f9 < 0.0f) {
                        this.barFreqNext[i24] = 0.0f;
                    } else {
                        this.barFreqNext[i24] = f9;
                    }
                } else if (f8 < 0.0f) {
                    this.barFreqNext[i24] = 0.0f;
                } else {
                    this.barFreqNext[i24] = f8;
                }
                i24++;
                i21 = i25;
                i19 = i26;
            }
            for (int i27 = 0; i27 < i; i27++) {
                float[] fArr3 = this.barFreq;
                if (fArr3[i27] < 0.0f) {
                    fArr3[i27] = 0.0f;
                }
                float[] fArr4 = this.barFreqNext;
                if (fArr4[i27] < 0.0f) {
                    fArr4[i27] = 0.0f;
                }
            }
            int i28 = i - 1;
            int iMin = 1;
            for (int i29 = i28; i29 >= i22; i29--) {
                float[] fArr5 = this.barFreq;
                fArr5[i29] = fArr5[iMin];
                float[] fArr6 = this.barFreqNext;
                fArr6[i29] = fArr6[iMin];
                iMin = Math.min(iMin + 1, i28);
            }
        }

        double logspace(double d, double d2, int i, int i2) {
            double d3 = i;
            double d4 = i2 - 1;
            Double.isNaN(d3);
            Double.isNaN(d4);
            return d * Math.pow(d2 / d, d3 / d4);
        }

        public void setAweightFft(float f) {
            synchronized (this.lockObj) {
                this.aweightFft = f;
            }
        }

        public void setOutputMultiplier(float f) {
            synchronized (this.lockObj) {
                this.outputMultiplier = f;
            }
        }

        @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
        public void onApplyCustomization(CustomPropertiesList customPropertiesList) {
            synchronized (this.lockObj) {
                setSampleInCountAsMilliseconds(customPropertiesList.getPropertyInt("audioDurationMs", 60));
                setHighQualityAudioCapture(customPropertiesList.getPropertyBool("highQualityAudioCapture", false));
                setSampleOutCount(customPropertiesList.getPropertyInt("sampleOutCount", 128), customPropertiesList.getPropertyFloat("lowerHz", 0.1f), customPropertiesList.getPropertyFloat("higherHz", 18000.0f), customPropertiesList.getPropertyBool("mirrorSamples", false), customPropertiesList.getPropertyInt("repeatSamples", 1), customPropertiesList.getPropertyFloat("hzLinearFactor", 0.06f), customPropertiesList.getPropertyInt("starAndEndGap", 0), customPropertiesList.getPropertyInt("starAndEndGap", 0), customPropertiesList.getPropertyFloat("freqShift", 0.0f));
                setSmoothFactor(customPropertiesList.getPropertyFloat("smooth", 1.0f));
                setFftFilter(customPropertiesList.getPropertyInt("filterRadius", 4), customPropertiesList.getPropertyFloat("filterStrength", 1.0f));
                setBeatSmooth(customPropertiesList.getPropertyFloat("beatSmooth", 0.4f));
                setBeatRangeCorners(customPropertiesList.getPropertyFloat("beatRangeBarFirst", 0.0f), customPropertiesList.getPropertyFloat("beatRangeBarLast", 0.2f), customPropertiesList.getPropertyFloat("beatRangeValueLower", 0.7f), customPropertiesList.getPropertyFloat("beatRangeValueHigher", 35.0f));
                setAweightFft(customPropertiesList.getPropertyFloat("lowerFreqMagnitude", 0.25f));
                setOutputMultiplier(customPropertiesList.getPropertyFloat("outputMultiplier", 1.0f));
            }
        }

        @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
        public void onReadCustomization(CustomPropertiesList customPropertiesList) {
            synchronized (this.lockObj) {
                customPropertiesList.putPropertyIntAsRange("audioDurationMs", this.dataCountInMs, "1_AudioCapture", 20, 300);
                customPropertiesList.putPropertyBool("highQualityAudioCapture", this.highQualityAudioCapture, "1_AudioCapture");
                customPropertiesList.putPropertyIntAsRange("sampleOutCount", this.barsCount, "2_spectrum", 0, 512);
                customPropertiesList.putPropertyFloatAsRange("lowerHz", this.loFreq, "2_spectrumHz", 0.0f, 300.0f);
                customPropertiesList.putPropertyFloatAsRange("higherHz", this.hiFreq, "2_spectrumHz", 300.0f, 18000.0f);
                customPropertiesList.putPropertyFloatAsRange("hzLinearFactor", this.freqLinfactor, "2_spectrumHz", 0.0f, 1.0f);
                customPropertiesList.putPropertyFloatAsRange("freqShift", this.freqShift, "2_spectrumHz", 0.0f, 1.0f);
                customPropertiesList.putPropertyBool("mirrorSamples", this.mirrorSampleValues, "2_spectrum");
                customPropertiesList.putPropertyIntAsRange("repeatSamples", this.repeatSamples, "2_spectrum", 1, 6);
                customPropertiesList.putPropertyIntAsRange("starAndEndGap", this.starBar, "2_spectrum", 0, 30);
                customPropertiesList.putPropertyFloatAsRange("smooth", this.smoothFactor, "2_spectrum", 0.1f, 1.0f);
                customPropertiesList.putPropertyIntAsRange("filterRadius", this.fftFilter.getRadius(), "2_spectrum", 0, 20);
                customPropertiesList.putPropertyFloatAsRange("filterStrength", this.fftFilter.getStrength(), "2_spectrum", 0.1f, 2.0f);
                customPropertiesList.putPropertyFloatAsRange("beatSmooth", this.beatSmoothFactor, "beat", 0.1f, 1.0f);
                customPropertiesList.putPropertyFloatAsRange("beatRangeBarFirst", this.beatRange.getX0(), "beat", 0.0f, 1.0f);
                customPropertiesList.putPropertyFloatAsRange("beatRangeBarLast", this.beatRange.getX1(), "beat", 0.0f, 1.0f);
                customPropertiesList.putPropertyFloatAsRange("beatRangeValueLower", this.beatRange.getY0(), "beat", 0.0f, 50.0f);
                customPropertiesList.putPropertyFloatAsRange("beatRangeValueHigher", this.beatRange.getY1(), "beat", 0.0f, 100.0f);
                customPropertiesList.putPropertyFloatAsRange("lowerFreqMagnitude", this.aweightFft, "2_spectrum", 0.0f, 1.0f);
                customPropertiesList.putPropertyFloatAsRange("outputMultiplier", this.outputMultiplier, "2_spectrum", 0.1f, 3.0f);
            }
        }

        public float getBeatBarValueSmooth() {
            float f;
            synchronized (this.lockObj) {
                f = this.beatBarValueSmooth;
            }
            return f;
        }

        public float getPeakBarIndexSmooth() {
            float f;
            synchronized (this.lockObj) {
                f = this.peakBarIndex01Smooth;
            }
            return f;
        }

        public void onRender(RenderState renderState, VFrameBuffer vFrameBuffer) {
            synchronized (this.lockObj) {
                if (this.fftTransform[0] != null) {
                    this.fftTransform[0].onRender(renderState, vFrameBuffer);
                }
            }
        }
    }

    private class ProcessingData {
        public float frameTimeF;
        public InternalVisualizationDataProvider visualisationData;

        public ProcessingData(float f, InternalVisualizationDataProvider internalVisualizationDataProvider) {
            this.frameTimeF = f;
            this.visualisationData = internalVisualizationDataProvider;
        }
    }

    private void MaybeStartAudioProcessThread() {
        Thread thread = this.audioProcessTh;
        if (thread == null || !thread.isAlive()) {
            tlog.d("Starting audioProcessTh");
            Thread thread2 = new Thread(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioSpectrumDataPhaseS.1
                @Override // java.lang.Runnable
                public void run() {
                    while (true) {
                        try {
                            ProcessingData processingDataPoll = SegmentAudioSpectrumDataPhaseS.this.processingQueue.poll(10000L, TimeUnit.MILLISECONDS);
                            if (processingDataPoll == null) {
                                return;
                            }
                            SegmentAudioSpectrumDataPhaseS.this.processingResultQueue.add(SegmentAudioSpectrumDataPhaseS.this.audioProcess.processTh(processingDataPoll.frameTimeF, processingDataPoll.visualisationData));
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            });
            this.audioProcessTh = thread2;
            thread2.start();
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider
    public void process(IRenderState iRenderState, InternalVisualizationDataProvider internalVisualizationDataProvider) {
        if (!iRenderState.getMultiThread()) {
            this.frameValuesAccumulator.addFrame(this.audioProcess.processTh(iRenderState.getFrameTimeF(), internalVisualizationDataProvider));
            return;
        }
        MaybeStartAudioProcessThread();
        try {
            float[] fArrPoll = this.processingResultQueue.poll(100L, TimeUnit.MILLISECONDS);
            if (fArrPoll != null) {
                this.frameValuesAccumulator.addFrame(fArrPoll);
            }
        } catch (InterruptedException unused) {
        }
        this.processingQueue.offer(new ProcessingData(iRenderState.getFrameTimeF(), internalVisualizationDataProvider));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider
    public float[] getFrameValues(int i, int i2) {
        return this.frameValuesAccumulator.getFrameValues(i, i2);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider
    public ISimpleListFloat createFrameValuesAccessorList(int i, int i2, int i3, ISimpleListFloat iSimpleListFloat) {
        return this.frameValuesAccumulator.createFrameValuesAccessorList(i, i2, i3, iSimpleListFloat);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.IFrameDataProvider
    public float getRms() {
        return this.audioProcess.getBeatBarValueSmooth();
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.IFrameDataProvider
    public float getPeakBarIndex() {
        return this.audioProcess.getPeakBarIndexSmooth();
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        this.audioProcess.onApplyCustomization(customPropertiesList);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onReadCustomization(CustomPropertiesList customPropertiesList) {
        this.audioProcess.onReadCustomization(customPropertiesList);
    }

    public void setSampleOutCount(int i, float f, int i2) {
        setSampleOutCount(i, 20.0f, 18000.0f, false, 1, f, i2, i2, 0.0f);
    }

    public void setSampleOutCount(int i, boolean z, int i2) {
        setSampleOutCount(i, 20.0f, 18000.0f, z, i2, 0.06f, 0, 0, 0.0f);
    }

    public void setSampleOutCount(int i, float f, float f2, boolean z, int i2, float f3) {
        setSampleOutCount(i, f, f2, z, i2, f3, 0, 0, 0.0f);
    }

    public void setSampleOutCount(int i, float f, float f2, boolean z, int i2, float f3, int i3, int i4, float f4) {
        this.audioProcess.setSampleOutCount(i, f, f2, z, i2, f3, i3, i4, f4);
    }

    public void setSmoothFactor(float f) {
        this.audioProcess.setSmoothFactor(f);
    }

    public void setBeatBart(int i) {
        this.audioProcess.setBeatBart(i);
    }

    public void setDataMode(int i) {
        this.audioProcess.setDataMode(i);
    }

    public void setSmooth(float f) {
        this.audioProcess.setSmooth(f);
    }

    public void setSteadySmooth(float f) {
        this.audioProcess.setSteadySmooth(f);
    }

    public void setFftSoftness(int i) {
        this.audioProcess.setFftSoftness(i);
    }

    public void setFftFilter(int i, float f) {
        this.audioProcess.setFftFilter(i, f);
    }

    public void setFrequencyBinSliding(boolean z) {
        this.audioProcess.setFrequencyBinSliding(z);
    }

    public void setBeatSmooth(float f) {
        this.audioProcess.setBeatSmooth(f);
    }

    public void setBeatRangeCorners(float f, float f2, float f3, float f4) {
        this.audioProcess.setBeatRangeCorners(f, f2, f3, f4);
    }

    public void setAweightFft(float f) {
        this.audioProcess.setAweightFft(f);
    }

    public void setOutputMultiplier(float f) {
        this.audioProcess.setOutputMultiplier(f);
    }

    public void onRender(RenderState renderState, VFrameBuffer vFrameBuffer) {
        this.audioProcess.onRender(renderState, vFrameBuffer);
    }
}
