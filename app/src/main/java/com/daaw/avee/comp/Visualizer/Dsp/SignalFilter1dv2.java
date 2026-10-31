package com.daaw.avee.comp.Visualizer.Dsp;

import com.daaw.avee.Common.ISimpleListDouble;

/* JADX INFO: loaded from: classes.dex */
public class SignalFilter1dv2 {
    private int Radius;
    private float divider;
    private int[] kernelIndex;
    private float[] kernelWeight;
    private float strength;
    private int width;

    public static SignalFilter1dv2 CreateAllPass(int i) {
        double d = i;
        Double.isNaN(d);
        int iCeil = (int) Math.ceil(d * 2.57d);
        int i2 = iCeil + iCeil + 1;
        float[] fArr = new float[i2];
        int i3 = -iCeil;
        float f = 0.0f;
        while (i3 < iCeil + 1) {
            float f2 = i3 == 0 ? 1.0f : 0.0f;
            fArr[i3 + iCeil] = f2;
            f += f2;
            i3++;
        }
        return new SignalFilter1dv2(fArr, f, i2, i);
    }

    public static SignalFilter1dv2 CreateGlaussian(int i) {
        double d = i;
        Double.isNaN(d);
        int iCeil = (int) Math.ceil(2.57d * d);
        int i2 = iCeil + iCeil + 1;
        float[] fArr = new float[i2];
        float f = 1.0f;
        if (i2 == 1) {
            fArr[0] = 1.0f;
        } else {
            float f2 = 0.0f;
            for (int i3 = 0 - iCeil; i3 < 0 + iCeil + 1; i3++) {
                int i4 = i3 + 0;
                float fExp = (float) Math.exp((double) (((-(float) (i4 * i4))) / ((float) ((i * 2) * i))));
                Double.isNaN(d);
                Double.isNaN(d);
                float f3 = fExp / ((float) ((6.283185307179586d * d) * d));
                fArr[i3 + iCeil] = f3;
                f2 += f3;
            }
            f = f2;
        }
        return new SignalFilter1dv2(fArr, f, i2, i);
    }

    public SignalFilter1dv2(float[] fArr, float f, int i, int i2) {
        int i3 = 0;
        this.kernelIndex = new int[0];
        this.kernelWeight = new float[0];
        this.divider = 1.0f;
        this.Radius = i2;
        int length = fArr.length;
        this.width = length;
        this.kernelWeight = fArr;
        this.divider = f;
        this.kernelIndex = new int[fArr.length];
        int i4 = (length - 1) / 2;
        while (true) {
            int[] iArr = this.kernelIndex;
            if (i3 >= iArr.length) {
                return;
            }
            iArr[i3] = (i3 % length) - i4;
            i3++;
        }
    }

    public SignalFilter1dv2() {
        this.kernelIndex = new int[0];
        this.kernelWeight = new float[0];
        this.divider = 1.0f;
    }

    public int getRadius() {
        int[] iArr = this.kernelIndex;
        if (iArr == null || iArr.length < 2) {
            return 0;
        }
        return this.Radius;
    }

    public float getStrength() {
        return this.strength;
    }

    public void Subtract2(SignalFilter1dv2 signalFilter1dv2) {
        int i = (this.width - signalFilter1dv2.width) / 2;
        for (int i2 = 0; i2 < this.kernelIndex.length; i2++) {
            int i3 = i2 - i;
            if (i3 >= 0 && i3 < signalFilter1dv2.width) {
                float[] fArr = this.kernelWeight;
                fArr[i2] = fArr[i2] - (signalFilter1dv2.kernelWeight[i3] * (this.divider / signalFilter1dv2.divider));
            }
        }
    }

    public void CalcDivider() {
        float fAbs = 0.0f;
        for (int i = 0; i < this.kernelIndex.length; i++) {
            fAbs += Math.abs(this.kernelWeight[i]);
        }
        this.divider = fAbs;
    }

    public void Invert2() {
        for (int i = 0; i < this.kernelIndex.length; i++) {
            float[] fArr = this.kernelWeight;
            fArr[i] = fArr[i] * (-1.0f);
        }
    }

    public void Convolution(float[] fArr, float[] fArr2, int i, int i2) {
        int i3 = (this.width - 1) / 2;
        for (int i4 = 0; i4 < i2; i4++) {
            for (int i5 = 0; i5 < i; i5++) {
                float f = 0.0f;
                int i6 = 0;
                // 原版字节码（SignalFilter1dv2.Convolution）内层是 aget→aget→mul-float 每轮【覆写】v3，
                // 全方法不含 add-float，即原版不做累加。此处曾被误改为 f +=（累加全部 tap），
                // 属“原版不存在的问题”，会使 EQ/滤波输出偏离原版，现按 smali 还原为 f = 。
                while (i6 < this.kernelIndex.length) {
                    int[] iArr = this.kernelIndex;
                    f = fArr[Math.min(i - 1, Math.max(0, iArr[i6] + i5))] * this.kernelWeight[i6];
                    i6++;
                }
                fArr2[(i4 * i) + i5] = f / this.divider;
            }
        }
    }

    public double getSoftedClamped(int i, ISimpleListDouble iSimpleListDouble) {
        float f = 0.0f;
        int i2 = 0;
        while (true) {
            int[] iArr = this.kernelIndex;
            if (i2 < iArr.length) {
                int i3 = iArr[i2] + i;
                if (i3 >= 0 && i3 < iSimpleListDouble.size()) {
                    double d = f;
                    double d2 = iSimpleListDouble.get(i3);
                    double d3 = this.kernelWeight[i2];
                    Double.isNaN(d3);
                    Double.isNaN(d);
                    f = (float) (d + (d2 * d3));
                }
                i2++;
            } else {
                return f / this.divider;
            }
        }
    }

    public static SignalFilter1dv2 createHighPass(int i, float f) {
        int i2 = 0;
        if (i < 1) {
            return new SignalFilter1dv2(new float[]{1.0f}, 1.0f, 1, 0);
        }
        SignalFilter1dv2 signalFilter1dv2CreateAllPass = CreateAllPass(i);
        SignalFilter1dv2 signalFilter1dv2CreateAllPass2 = CreateAllPass(i);
        signalFilter1dv2CreateAllPass2.Subtract2(CreateGlaussian(i));
        signalFilter1dv2CreateAllPass2.strength = f;
        float f2 = 1.0f - f;
        while (true) {
            float[] fArr = signalFilter1dv2CreateAllPass2.kernelWeight;
            if (i2 < fArr.length) {
                fArr[i2] = (fArr[i2] * f) + (signalFilter1dv2CreateAllPass.kernelWeight[i2] * f2);
                i2++;
            } else {
                signalFilter1dv2CreateAllPass2.CalcDivider();
                return signalFilter1dv2CreateAllPass2;
            }
        }
    }

    public SignalFilter1dv2 _createHighPass(int i, float f) {
        this.strength = f;
        this.Radius = i;
        int i2 = 0;
        if (i < 1) {
            this.kernelIndex = new int[]{0};
            this.kernelWeight = new float[]{1.0f};
            this.divider = 1.0f;
            return this;
        }
        int[] iArr = new int[i + 1 + i];
        this.kernelIndex = iArr;
        this.kernelWeight = new float[iArr.length];
        this.divider = 0.0f;
        while (true) {
            int[] iArr2 = this.kernelIndex;
            if (i2 < iArr2.length) {
                iArr2[i2] = i2 - i;
                this.kernelWeight[i2] = -f;
                this.divider += f;
                i2++;
            } else {
                float length = (iArr2.length - 1) * 1.0f;
                this.kernelWeight[i] = length;
                this.divider += length;
                return this;
            }
        }
    }
}
