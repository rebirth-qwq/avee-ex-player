package com.daaw.avee.vendor.blur;

import android.graphics.Bitmap;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes2.dex */
public class FastBlur {
    public static Bitmap fastBlur(Bitmap bitmap, int i) {
        int i2 = i;
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), true);
        if (i2 < 1) {
            return null;
        }
        int width = bitmapCopy.getWidth();
        int height = bitmapCopy.getHeight();
        int i3 = width * height;
        int[] iArr = new int[i3];
        bitmapCopy.getPixels(iArr, 0, width, 0, 0, width, height);
        int i4 = width - 1;
        int i5 = height - 1;
        int i6 = i2 + i2 + 1;
        int[] iArr2 = new int[i3];
        int[] iArr3 = new int[i3];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[Math.max(width, height)];
        int i7 = (i6 + 1) >> 1;
        int i8 = i7 * i7;
        int i9 = i8 * 256;
        int[] iArr6 = new int[i9];
        for (int i10 = 0; i10 < i9; i10++) {
            iArr6[i10] = i10 / i8;
        }
        int[][] iArr7 = (int[][]) Array.newInstance((Class<?>) int.class, i6, 3);
        int i11 = i2 + 1;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        while (i12 < height) {
            Bitmap bitmap2 = bitmapCopy;
            int i15 = height;
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            int i21 = 0;
            int i22 = 0;
            int i23 = 0;
            int i24 = -i2;
            int i25 = 0;
            while (i24 <= i2) {
                int i26 = i5;
                int[] iArr8 = iArr5;
                int i27 = iArr[i13 + Math.min(i4, Math.max(i24, 0))];
                int[] iArr9 = iArr7[i24 + i2];
                iArr9[0] = (i27 & 16711680) >> 16;
                iArr9[1] = (i27 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr9[2] = i27 & 255;
                int iAbs = i11 - Math.abs(i24);
                i25 += iArr9[0] * iAbs;
                i16 += iArr9[1] * iAbs;
                i17 += iArr9[2] * iAbs;
                if (i24 > 0) {
                    i21 += iArr9[0];
                    i22 += iArr9[1];
                    i23 += iArr9[2];
                } else {
                    i18 += iArr9[0];
                    i19 += iArr9[1];
                    i20 += iArr9[2];
                }
                i24++;
                i5 = i26;
                iArr5 = iArr8;
            }
            int i28 = i5;
            int[] iArr10 = iArr5;
            int i29 = i2;
            int i30 = i25;
            int i31 = 0;
            while (i31 < width) {
                iArr2[i13] = iArr6[i30];
                iArr3[i13] = iArr6[i16];
                iArr4[i13] = iArr6[i17];
                int i32 = i30 - i18;
                int i33 = i16 - i19;
                int i34 = i17 - i20;
                int[] iArr11 = iArr7[((i29 - i2) + i6) % i6];
                int i35 = i18 - iArr11[0];
                int i36 = i19 - iArr11[1];
                int i37 = i20 - iArr11[2];
                if (i12 == 0) {
                    iArr10[i31] = Math.min(i31 + i2 + 1, i4);
                }
                int i38 = iArr[i14 + iArr10[i31]];
                iArr11[0] = (i38 & 16711680) >> 16;
                iArr11[1] = (i38 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr11[2] = i38 & 255;
                int i39 = i21 + iArr11[0];
                int i40 = i22 + iArr11[1];
                int i41 = i23 + iArr11[2];
                i30 = i32 + i39;
                i16 = i33 + i40;
                i17 = i34 + i41;
                i29 = (i29 + 1) % i6;
                int[] iArr12 = iArr7[i29 % i6];
                i18 = i35 + iArr12[0];
                i19 = i36 + iArr12[1];
                i20 = i37 + iArr12[2];
                i21 = i39 - iArr12[0];
                i22 = i40 - iArr12[1];
                i23 = i41 - iArr12[2];
                i13++;
                i31++;
            }
            i14 += width;
            i12++;
            bitmapCopy = bitmap2;
            height = i15;
            i5 = i28;
            iArr5 = iArr10;
        }
        Bitmap bitmap3 = bitmapCopy;
        int i42 = i5;
        int[] iArr13 = iArr5;
        int i43 = height;
        int[] iArr14 = iArr6;
        int i44 = 0;
        while (i44 < width) {
            int i45 = -i2;
            int i46 = i6;
            int[] iArr15 = iArr;
            int i47 = 0;
            int i48 = 0;
            int i49 = 0;
            int i50 = 0;
            int i51 = 0;
            int i52 = 0;
            int i53 = 0;
            int i54 = i45;
            int i55 = i45 * width;
            int i56 = 0;
            int i57 = 0;
            while (i54 <= i2) {
                int i58 = width;
                int iMax = Math.max(0, i55) + i44;
                int[] iArr16 = iArr7[i54 + i2];
                iArr16[0] = iArr2[iMax];
                iArr16[1] = iArr3[iMax];
                iArr16[2] = iArr4[iMax];
                int iAbs2 = i11 - Math.abs(i54);
                i56 += iArr2[iMax] * iAbs2;
                i57 += iArr3[iMax] * iAbs2;
                i47 += iArr4[iMax] * iAbs2;
                if (i54 > 0) {
                    i51 += iArr16[0];
                    i52 += iArr16[1];
                    i53 += iArr16[2];
                } else {
                    i48 += iArr16[0];
                    i49 += iArr16[1];
                    i50 += iArr16[2];
                }
                int i59 = i42;
                if (i54 < i59) {
                    i55 += i58;
                }
                i54++;
                i42 = i59;
                width = i58;
            }
            int i60 = width;
            int i61 = i42;
            int i62 = i2;
            int i63 = i44;
            int i64 = i57;
            int i65 = i43;
            int i66 = i56;
            int i67 = 0;
            while (i67 < i65) {
                iArr15[i63] = (iArr15[i63] & ViewCompat.MEASURED_STATE_MASK) | (iArr14[i66] << 16) | (iArr14[i64] << 8) | iArr14[i47];
                int i68 = i66 - i48;
                int i69 = i64 - i49;
                int i70 = i47 - i50;
                int[] iArr17 = iArr7[((i62 - i2) + i46) % i46];
                int i71 = i48 - iArr17[0];
                int i72 = i49 - iArr17[1];
                int i73 = i50 - iArr17[2];
                if (i44 == 0) {
                    iArr13[i67] = Math.min(i67 + i11, i61) * i60;
                }
                int i74 = iArr13[i67] + i44;
                iArr17[0] = iArr2[i74];
                iArr17[1] = iArr3[i74];
                iArr17[2] = iArr4[i74];
                int i75 = i51 + iArr17[0];
                int i76 = i52 + iArr17[1];
                int i77 = i53 + iArr17[2];
                i66 = i68 + i75;
                i64 = i69 + i76;
                i47 = i70 + i77;
                i62 = (i62 + 1) % i46;
                int[] iArr18 = iArr7[i62];
                i48 = i71 + iArr18[0];
                i49 = i72 + iArr18[1];
                i50 = i73 + iArr18[2];
                i51 = i75 - iArr18[0];
                i52 = i76 - iArr18[1];
                i53 = i77 - iArr18[2];
                i63 += i60;
                i67++;
                i2 = i;
            }
            i44++;
            i2 = i;
            i42 = i61;
            i43 = i65;
            i6 = i46;
            iArr = iArr15;
            width = i60;
        }
        int i78 = width;
        bitmap3.setPixels(iArr, 0, i78, 0, 0, i78, i43);
        return bitmap3;
    }
}
