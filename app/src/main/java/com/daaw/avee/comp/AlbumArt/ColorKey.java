package com.daaw.avee.comp.AlbumArt;

import android.graphics.Bitmap;
import com.daaw.avee.Common.Ref;
import com.daaw.avee.comp.Visualizer.Graphic.GraphicsUtils;

/* JADX INFO: loaded from: classes.dex */
public class ColorKey {
    static int XYtoI(int i, int i2, int i3) {
        return i + (i2 * i3);
    }

    static int ItoX(int i, int i2) {
        return i % i2;
    }

    static int ItoY(int i, int i2) {
        return i / i2;
    }

    static int compareColorRGB(int[] iArr, int[] iArr2) {
        return Math.max(Math.max(Math.abs(iArr[0] - iArr2[0]), Math.abs(iArr[1] - iArr2[1])), Math.abs(iArr[2] - iArr2[2]));
    }

    static int pickBgColor(int[] iArr) {
        int[] iArr2 = new int[4];
        int[] iArr3 = {0, 0, 0, 0};
        for (int i : iArr) {
            GraphicsUtils.intColorToInt4Color(iArr2, 0, i);
            iArr3[0] = iArr3[0] + iArr2[0];
            iArr3[1] = iArr3[1] + iArr2[1];
            iArr3[2] = iArr3[2] + iArr2[2];
            iArr3[3] = iArr3[3] + iArr2[3];
        }
        iArr3[0] = iArr3[0] / iArr.length;
        iArr3[1] = iArr3[1] / iArr.length;
        iArr3[2] = iArr3[2] / iArr.length;
        iArr3[3] = iArr3[3] / iArr.length;
        int i2 = -16711936;
        int i3 = 999999;
        for (int i4 : iArr) {
            GraphicsUtils.intColorToInt4Color(iArr2, 0, i4);
            int iCompareColorRGB = compareColorRGB(iArr2, iArr3);
            if (iCompareColorRGB < i3) {
                i2 = i4;
                i3 = iCompareColorRGB;
            }
        }
        return i2;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [T, int[]] */
    /* JADX WARN: Type inference failed for: r0v57, types: [T, android.graphics.Bitmap] */
    /* JADX WARN: Type inference failed for: r0v9, types: [T, android.graphics.Bitmap] */
    public static Bitmap processBitmapColorKey(Bitmap bitmap, ImageColorKeyParams imageColorKeyParams, Ref<Bitmap> ref, Ref<int[]> ref2) {
        int i;
        if (imageColorKeyParams == null) {
            return bitmap;
        }
        int width = bitmap.getWidth() * bitmap.getHeight();
        if (ref2.obj == null || ref2.obj.length < width) {
            try {
                ref2.obj = new int[width];
            } catch (Exception unused) {
                return bitmap;
            }
        }
        if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
            if (ref.obj == null || ref.obj.getWidth() != bitmap.getWidth() || ref.obj.getHeight() != bitmap.getHeight()) {
                if (ref.obj != null) {
                    ref.obj.recycle();
                }
                ref.obj = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            }
            bitmap.getPixels(ref2.obj, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        } else {
            try {
                ref.obj = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ref.obj.getPixels(ref2.obj, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        int iPickBgColor = imageColorKeyParams.colorKey;
        float[] fArr = new float[4];
        float[] fArr2 = new float[4];
        int[] iArr = new int[4];
        int[] iArr2 = new int[4];
        int[] iArr3 = new int[4];
        if (imageColorKeyParams.autoDetect) {
            int width2 = bitmap.getWidth();
            int height = bitmap.getHeight();
            int i2 = width2 - 1;
            int i3 = height - 1;
            int i4 = width2 / 2;
            int i5 = height / 2;
            i = 0;
            iPickBgColor = pickBgColor(new int[]{ref2.obj[XYtoI(0, 0, width2)], ref2.obj[XYtoI(i2, 0, width2)], ref2.obj[XYtoI(i2, i3, width2)], ref2.obj[XYtoI(0, i3, width2)], ref2.obj[XYtoI(i4, 0, width2)], ref2.obj[XYtoI(i2, i5, width2)], ref2.obj[XYtoI(i4, i3, width2)], ref2.obj[XYtoI(0, i5, width2)]});
        } else {
            i = 0;
        }
        GraphicsUtils.intColorToHlsa1(fArr, iPickBgColor);
        GraphicsUtils.intColorToInt4Color(iArr, i, iPickBgColor);
        iArr2[i] = 128 - iArr[i];
        iArr2[1] = 128 - iArr[1];
        iArr2[2] = 128 - iArr[2];
        iArr2[3] = 255 - iArr[3];
        int i6 = 0;
        while (i6 < width) {
            int i7 = ref2.obj[i6];
            GraphicsUtils.intColorToInt4Color(iArr3, 0, i7);
            GraphicsUtils.intColorToHlsa1(fArr2, i7);
            float fMin = Math.min(Math.abs(fArr2[0] - fArr[0]) * 5.0f, 1.0f);
            Math.min(Math.abs(fArr2[1] - fArr[1]) * 1.0f, 1.0f);
            float fMin2 = Math.min(1.0f, (((int) (Math.max(Math.min(Math.abs(fArr2[2] - fArr[2]) * 2.0f, 1.0f), fMin) * 255.0f)) / 255.0f) * 1.0f);
            float f = 1.0f - fMin2;
            int[] iArr4 = iArr2;
            float fPow = 1.0f - ((float) Math.pow(1.0f - ((float) Math.pow(fMin2, imageColorKeyParams.transparencyStrength * 2.0f)), imageColorKeyParams.opacityStrength * 2.0f));
            iArr3[0] = iArr3[0] + ((int) (iArr4[0] * f));
            iArr3[1] = iArr3[1] + ((int) (iArr4[1] * f));
            iArr3[2] = iArr3[2] + ((int) (iArr4[2] * f));
            iArr3[3] = (int) (255.0f * fPow);
            iArr3[0] = (int) (iArr3[0] * fPow);
            iArr3[1] = (int) (iArr3[1] * fPow);
            iArr3[2] = (int) (iArr3[2] * fPow);
            if (iArr3[0] < 0) {
                iArr3[0] = 0;
            }
            if (iArr3[1] < 0) {
                iArr3[1] = 0;
            }
            if (iArr3[2] < 0) {
                iArr3[2] = 0;
            }
            if (iArr3[3] < 0) {
                iArr3[3] = 0;
            }
            if (iArr3[0] > 255) {
                iArr3[0] = 255;
            }
            if (iArr3[1] > 255) {
                iArr3[1] = 255;
            }
            if (iArr3[2] > 255) {
                iArr3[2] = 255;
            }
            if (iArr3[3] > 255) {
                iArr3[3] = 255;
            }
            ref2.obj[i6] = GraphicsUtils.Int4ColorToIntColor(iArr3);
            i6++;
            iArr2 = iArr4;
        }
        ref.obj.setPixels(ref2.obj, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return ref.obj;
    }
}
