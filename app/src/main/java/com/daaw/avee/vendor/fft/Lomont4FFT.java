package com.daaw.avee.vendor.fft;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Lomont4FFT extends FFTBase {
    List<Double> forwardCos;
    List<Double> forwardSin;
    List<Double> inverseCos;
    List<Double> inverseSin;

    @Override // com.daaw.avee.vendor.fft.FFTBase
    public void Initialize(int i) {
        this.forwardCos = new ArrayList();
        this.forwardSin = new ArrayList();
        this.inverseCos = new ArrayList();
        this.inverseSin = new ArrayList();
        int i2 = 1;
        int i3 = 1;
        while (i > i3) {
            int i4 = i3 * 2;
            double d = i3;
            Double.isNaN(d);
            double d2 = 3.141592653589793d / d;
            double dCos = Math.cos(d2);
            double dSin = Math.sin(d2);
            int i5 = 0;
            double d3 = 1.0d;
            double d4 = 0.0d;
            while (i5 < i4) {
                this.forwardCos.add(Double.valueOf(d3));
                this.forwardSin.add(Double.valueOf(d4));
                double d5 = (d3 * dCos) - (d4 * dSin);
                d4 = (d4 * dCos) + (d3 * dSin);
                i5 += 2;
                d3 = d5;
            }
            i3 = i4;
        }
        while (i > i2) {
            int i6 = i2 * 2;
            double d6 = i2;
            Double.isNaN(d6);
            double d7 = (3.141592653589793d / d6) * (-1.0d);
            double dCos2 = Math.cos(d7);
            double dSin2 = Math.sin(d7);
            int i7 = 0;
            double d8 = 1.0d;
            double d9 = 0.0d;
            while (i7 < i6) {
                this.inverseCos.add(Double.valueOf(d8));
                this.inverseSin.add(Double.valueOf(d9));
                double d10 = (d8 * dCos2) - (d9 * dSin2);
                d9 = (d9 * dCos2) + (d8 * dSin2);
                i7 += 2;
                d8 = d10;
            }
            i2 = i6;
        }
    }

    @Override // com.daaw.avee.vendor.fft.FFTBase
    public void FFT(double[] dArr, boolean z) {
        if (dArr.length % 2 != 0) {
            throw new IllegalArgumentException("Array length must be even for FFT. Current length: " + dArr.length);
        }
        int length = dArr.length / 2;
        int i = length / 2;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i2 + 2;
            double d = dArr[i4];
            int i5 = i3 + length;
            dArr[i4] = dArr[i5];
            dArr[i5] = d;
            int i6 = i2 + 3;
            double d2 = dArr[i6];
            int i7 = length + 1 + i3;
            dArr[i6] = dArr[i7];
            dArr[i7] = d2;
            if (i2 > i3) {
                double d3 = dArr[i2];
                dArr[i2] = dArr[i3];
                dArr[i3] = d3;
                int i8 = i2 + 1;
                double d4 = dArr[i8];
                int i9 = i3 + 1;
                dArr[i8] = dArr[i9];
                dArr[i9] = d4;
                int i10 = length + 2;
                int i11 = i2 + i10;
                double d5 = dArr[i11];
                int i12 = i10 + i3;
                dArr[i11] = dArr[i12];
                dArr[i12] = d5;
                int i13 = length + 3;
                int i14 = i2 + i13;
                double d6 = dArr[i14];
                int i15 = i13 + i3;
                dArr[i14] = dArr[i15];
                dArr[i15] = d6;
            }
            i3 += 4;
            if (i3 >= length) {
                break;
            }
            int i16 = i;
            int i17 = i2;
            while (i17 >= i16) {
                i17 -= i16;
                i16 /= 2;
            }
            i2 = i17 + i16;
        }
        List<Double> list = z ? this.forwardCos : this.inverseCos;
        List<Double> list2 = z ? this.forwardSin : this.inverseSin;
        int i18 = 1;
        int i19 = 0;
        while (length > i18) {
            int i20 = i18 * 2;
            int i21 = 0;
            int i22 = i19;
            while (i21 < i20) {
                double dDoubleValue = list.get(i22).doubleValue();
                int i23 = i22 + 1;
                double dDoubleValue2 = list2.get(i22).doubleValue();
                for (int i24 = i21; i24 < length * 2; i24 += i20 * 2) {
                    int i25 = i24 + i20;
                    int i26 = i25 + 1;
                    double d7 = (dArr[i25] * dDoubleValue) - (dArr[i26] * dDoubleValue2);
                    double d8 = (dArr[i25] * dDoubleValue2) + (dArr[i26] * dDoubleValue);
                    dArr[i25] = dArr[i24] - d7;
                    int i27 = i24 + 1;
                    dArr[i26] = dArr[i27] - d8;
                    dArr[i24] = dArr[i24] + d7;
                    dArr[i27] = dArr[i27] + d8;
                }
                i21 += 2;
                i22 = i23;
            }
            i19 = i22;
            i18 = i20;
        }
        if (!z) {
            double d9 = 1 / length;
            for (int i28 = 0; i28 < length * 2; i28++) {
                double d10 = dArr[i28];
                Double.isNaN(d9);
                dArr[i28] = d10 * d9;
            }
        }
    }

    @Override // com.daaw.avee.vendor.fft.FFTBase
    public void RealFFT(double[] dArr, boolean z) {
        double d;
        int length = dArr.length;
        if (length % 2 != 0) {
            int i = length - 1;
            double[] dArr2 = new double[i];
            System.arraycopy(dArr, 0, dArr2, 0, i);
            dArr = dArr2;
        }
        if (z) {
            FFT(dArr, z);
            d = 1.0d;
        } else {
            d = -1.0d;
        }
        int length2 = dArr.length;
        double d2 = length2;
        Double.isNaN(d2);
        double d3 = (3.141592653589793d / d2) * 2.0d * d;
        double dCos = Math.cos(d3);
        double dSin = Math.sin(d3);
        int i2 = 1;
        double d4 = dSin;
        double d5 = dCos;
        while (i2 <= length2 / 4) {
            int i3 = ((length2 / 2) - i2) * 2;
            double d6 = dArr[i3];
            int i4 = i3 + 1;
            double d7 = dArr[i4];
            int i5 = i2 * 2;
            double d8 = dArr[i5];
            int i6 = i5 + 1;
            double d9 = dArr[i6];
            double d10 = d8 + d6;
            double d11 = d9 - d7;
            double d12 = d8 - d6;
            double d13 = d7 + d9;
            double d14 = d13 * d4;
            double d15 = d12 * d5;
            double d16 = ((d12 * d4) + (d13 * d5)) * d;
            dArr[i5] = (d10 + d16) * 0.5d;
            dArr[i6] = (d11 - ((d15 - d14) * d)) * 0.5d;
            dArr[i3] = (d10 - d16) * 0.5d;
            dArr[i4] = (((((-1.0d) * d15) + d14) * d) - d11) * 0.5d;
            double d17 = (d5 * dCos) - (d4 * dSin);
            d4 = (d4 * dCos) + (d5 * dSin);
            i2++;
            d5 = d17;
        }
        if (z) {
            double d18 = dArr[0];
            dArr[0] = dArr[0] + dArr[1];
            dArr[1] = d18 - dArr[1];
        } else {
            double d19 = dArr[0];
            dArr[0] = (dArr[1] + d19) * 0.5d;
            dArr[1] = (d19 - dArr[1]) * 0.5d;
            FFT(dArr, false);
        }
    }
}
