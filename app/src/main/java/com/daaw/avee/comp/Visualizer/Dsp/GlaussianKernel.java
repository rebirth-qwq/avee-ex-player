package com.daaw.avee.comp.Visualizer.Dsp;

import com.daaw.avee.Common.Vec2i;
import com.daaw.avee.Common.tlog;
import junit.framework.Assert;

/* JADX INFO: loaded from: classes.dex */
public class GlaussianKernel {
    public int Radius;
    float divider;
    float[] kernel;
    Vec2i[] kernelIndex;
    int width;

    static float wBlackman(float f, float f2) {
        double d = f2 - f;
        Double.isNaN(d);
        double d2 = 2.0f * f2;
        Double.isNaN(d2);
        double dCos = 0.42d - (Math.cos((6.283185307179586d * d) / d2) * 0.5d);
        Double.isNaN(d);
        Double.isNaN(d2);
        return (float) (dCos + (Math.cos((d * 12.566370614359172d) / d2) * 0.08d));
    }

    static float sinc(float f) {
        if (f == 0.0f) {
            return 1.0f;
        }
        double d = f;
        Double.isNaN(d);
        double dSin = Math.sin(d * 3.141592653589793d);
        Double.isNaN(d);
        return (float) ((dSin / d) * 3.141592653589793d);
    }

    static float windowed_sinc(float f, float f2, int i) {
        return sinc2(f, 1.0f / ((f2 * 0.25f) * i));
    }

    static float sinc2(float f, float f2) {
        if (f == 0.0f) {
            return f2 * 2.0f;
        }
        double d = f2;
        Double.isNaN(d);
        double d2 = f;
        Double.isNaN(d2);
        double dSin = Math.sin(d * 6.283185307179586d * d2);
        Double.isNaN(d2);
        return (float) (dSin / (d2 * 3.141592653589793d));
    }

    public static GlaussianKernel CreateSinc(int i, int i2) {
        int i3 = i + i + 1;
        float[] fArr = new float[i3 * i3];
        int i4 = -i;
        float f = 0.0f;
        int i5 = i4;
        while (true) {
            int i6 = i + 1;
            if (i5 >= i6) {
                return new GlaussianKernel(fArr, f, i3, i);
            }
            for (int i7 = i4; i7 < i6; i7++) {
                float fWindowed_sinc = windowed_sinc((float) Math.sqrt((i7 * i7) + (i5 * i5)), i, i2);
                fArr[((i5 + i) * i3) + i7 + i] = fWindowed_sinc;
                f += fWindowed_sinc;
            }
            i5++;
        }
    }

    public static GlaussianKernel CreateGlaussian(int i) {
        double d = i;
        Double.isNaN(d);
        int iCeil = (int) Math.ceil(2.57d * d);
        int i2 = iCeil + iCeil + 1;
        int i3 = i2 * i2;
        float[] fArr = new float[i3];
        float f = 1.0f;
        if (i3 != 1) {
            int i4 = 0 - iCeil;
            f = 0.0f;
            int i5 = i4;
            while (true) {
                int i6 = 0 + iCeil + 1;
                if (i5 >= i6) {
                    break;
                }
                for (int i7 = i4; i7 < i6; i7++) {
                    int i8 = i7 + 0;
                    int i9 = i5 + 0;
                    float fExp = (float) Math.exp((double) (((-(float) ((i8 * i8) + (i9 * i9)))) / ((float) ((i * 2) * i))));
                    Double.isNaN(d);
                    Double.isNaN(d);
                    float f2 = fExp / ((float) ((6.283185307179586d * d) * d));
                    fArr[((i5 + iCeil) * i2) + i7 + iCeil] = f2;
                    f += f2;
                }
                i5++;
            }
        } else {
            fArr[0] = 1.0f;
        }
        return new GlaussianKernel(fArr, f, i2, i);
    }

    public static GlaussianKernel CreateAllPass(int i) {
        double d = i;
        Double.isNaN(d);
        int iCeil = (int) Math.ceil(d * 2.57d);
        int i2 = iCeil + iCeil + 1;
        float[] fArr = new float[i2 * i2];
        int i3 = -iCeil;
        int i4 = i3;
        float f = 0.0f;
        while (true) {
            int i5 = iCeil + 1;
            if (i4 >= i5) {
                return new GlaussianKernel(fArr, f, i2, i);
            }
            int i6 = i3;
            while (i6 < i5) {
                float f2 = (i4 == 0 && i6 == 0) ? 1.0f : 0.0f;
                fArr[((i4 + iCeil) * i2) + i6 + iCeil] = f2;
                f += f2;
                i6++;
            }
            i4++;
        }
    }

    public GlaussianKernel(float[] fArr, float f, int i, int i2) {
        this.Radius = i2;
        this.width = i;
        this.kernel = fArr;
        this.divider = f;
        this.kernelIndex = new Vec2i[fArr.length];
        int i3 = (i - 1) / 2;
        int i4 = (i - 1) / 2;
        int i5 = 0;
        while (true) {
            Vec2i[] vec2iArr = this.kernelIndex;
            if (i5 >= vec2iArr.length) {
                return;
            }
            vec2iArr[i5] = new Vec2i((i5 % i) - i3, (i5 / i) - i4);
            i5++;
        }
    }

    public void Subtract(GlaussianKernel glaussianKernel) {
        if (this.kernelIndex.length != glaussianKernel.kernelIndex.length) {
            tlog.w("kernelIndex.length dont match");
            return;
        }
        for (int i = 0; i < this.kernelIndex.length; i++) {
            float[] fArr = this.kernel;
            fArr[i] = fArr[i] - (glaussianKernel.kernel[i] * (this.divider / glaussianKernel.divider));
        }
    }

    public void Subtract2(GlaussianKernel glaussianKernel) {
        int i;
        int i2 = this.width;
        int i3 = (i2 - glaussianKernel.width) / 2;
        for (int i4 = 0; i4 < this.kernelIndex.length; i4++) {
            int i5 = (i4 % i2) - i3;
            int i6 = (i4 / i2) - i3;
            if (i5 >= 0 && i5 < (i = glaussianKernel.width) && i6 >= 0 && i6 < i) {
                int i7 = (i6 * i) + i5;
                float[] fArr = this.kernel;
                fArr[i4] = fArr[i4] - (glaussianKernel.kernel[i7] * (this.divider / glaussianKernel.divider));
            }
        }
    }

    public void Add(GlaussianKernel glaussianKernel) {
        if (this.kernelIndex.length != glaussianKernel.kernelIndex.length) {
            return;
        }
        float f = 0.0f;
        for (int i = 0; i < this.kernelIndex.length; i++) {
            float[] fArr = this.kernel;
            fArr[i] = fArr[i] + glaussianKernel.kernel[i];
            f += fArr[i];
        }
        this.divider = f;
    }

    public void Invert() {
        for (int i = 0; i < this.kernelIndex.length; i++) {
            float[] fArr = this.kernel;
            fArr[i] = fArr[i] * (-1.0f);
        }
        int i2 = this.width;
        float[] fArr2 = this.kernel;
        int i3 = (i2 * ((i2 - 1) / 2)) + ((i2 - 1) / 2);
        fArr2[i3] = fArr2[i3] + 1.0f;
        float f = 0.0f;
        for (int i4 = 0; i4 < this.kernelIndex.length; i4++) {
            f += this.kernel[i4];
        }
        this.divider = f;
    }

    public void Invert2() {
        for (int i = 0; i < this.kernelIndex.length; i++) {
            float[] fArr = this.kernel;
            fArr[i] = fArr[i] * (-1.0f);
        }
    }

    public void Convolution(float[] fArr, float[] fArr2, int i, int i2) {
        int i3 = this.width;
        int i4 = (i3 - 1) / 2;
        int i5 = (i3 - 1) / 2;
        for (int i6 = 0; i6 < i2; i6++) {
            for (int i7 = 0; i7 < i; i7++) {
                float f = 0.0f;
                int i8 = 0;
                // 原版 GlaussianKernel.Convolution 的 :goto_2 循环是 aget→mul 每轮【覆写】v3，
                // 并不累加（divider 除法的语义保持与原版一致）。此处曾被误改为 f += （累加全部 tap），
                // 属于"原版不存在的问题"，会让卷积/模糊结果偏离原版，现按字节码还原为 f = 。
                while (i8 < this.kernelIndex.length) {
                    Vec2i[] vec2iArr = this.kernelIndex;
                    f = fArr[(Math.min(i2 - 1, Math.max(0, vec2iArr[i8].y + i6)) * i) + Math.min(i - 1, Math.max(0, vec2iArr[i8].x + i7))] * this.kernel[i8];
                    i8++;
                }
                fArr2[(i6 * i) + i7] = f / this.divider;
            }
        }
    }

    int getCenterKernelIndex() {
        int i = 0;
        while (true) {
            Vec2i[] vec2iArr = this.kernelIndex;
            if (i < vec2iArr.length) {
                if (vec2iArr[i].x == 0 && this.kernelIndex[i].y == 0) {
                    return i;
                }
                i++;
            } else {
                Assert.assertTrue(false);
                return 0;
            }
        }
    }

    float getCenterKernel() {
        int i = 0;
        while (true) {
            Vec2i[] vec2iArr = this.kernelIndex;
            if (i < vec2iArr.length) {
                if (vec2iArr[i].x == 0 && this.kernelIndex[i].y == 0) {
                    return this.kernel[i];
                }
                i++;
            } else {
                Assert.assertTrue(false);
                return 0.0f;
            }
        }
    }

    public void Levels(float[] fArr, float[] fArr2, int i, int i2) {
        float centerKernel = getCenterKernel();
        int centerKernelIndex = getCenterKernelIndex();
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = i - 1;
                int i6 = i2 - 1;
                float f = fArr[(Math.min(i6, Math.max(0, this.kernelIndex[centerKernelIndex].y + i3)) * i) + Math.min(i5, Math.max(0, this.kernelIndex[centerKernelIndex].x + i4))];
                float f2 = f;
                int i7 = 0;
                while (i7 < this.kernelIndex.length) {
                    Vec2i[] vec2iArr = this.kernelIndex;

                        int iMin = Math.min(i5, Math.max(0, vec2iArr[i7].x + i4));
                        int iMin2 = Math.min(i6, Math.max(0, this.kernelIndex[i7].y + i3));
                        float f3 = this.kernel[i7] * (1.0f / centerKernel);
                        float f4 = fArr[(iMin2 * i) + iMin] * f3;
                        float f5 = 1.0f - f3;
                        float f6 = (f * f5) + f4;
                        if (f > f6) {
                            f = f6;
                        }
                        float f7 = f4 + (f5 * f2);
                        if (f2 < f7) {
                            f2 = f7;
                        }
                                            i7++;
                }
                int i8 = (i3 * i) + i4;
                fArr2[i8] = (fArr[i8] + (-f)) / ((f2 - f) + 1.0E-7f);
            }
        }
    }
}
