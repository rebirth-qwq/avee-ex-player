package com.daaw.avee.vendor.aosp;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class MyThumbnailUtils {
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    public static final int TARGET_SIZE_MINI_THUMBNAIL = 320;

    /* JADX WARN: Code duplicated, block: B:17:0x002a A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:18:0x002b  */
    /* JADX WARN: Code duplicated, block: B:20:0x002e  */
    /* JADX WARN: Code duplicated, block: B:22:0x003e  */
    /* JADX WARN: Code duplicated, block: B:23:0x0055  */
    /* JADX WARN: Code duplicated, block: B:25:0x0058  */
    /* JADX WARN: Code duplicated, block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:38:? A[RETURN, SYNTHETIC] */
    public static Bitmap createVideoThumbnail(String str, int i) throws IOException {
        Bitmap frameAtTime;
        int width;
        int height;
        int iMax;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            try {
                mediaMetadataRetriever.setDataSource(str);
                frameAtTime = mediaMetadataRetriever.extractMetadata(17) != null ? mediaMetadataRetriever.getFrameAtTime(-1L) : null;
                try {
                    mediaMetadataRetriever.release();
                } catch (RuntimeException unused) {
                }
            } catch (RuntimeException unused2) {
                frameAtTime = null;
            }
        } catch (RuntimeException unused3) {
            mediaMetadataRetriever.release();
            frameAtTime = null;
            if (frameAtTime == null) {
                return null;
            }
            if (i == 1) {
                if (i == 3) {
                    return ThumbnailUtils.extractThumbnail(frameAtTime, 96, 96, 2);
                }
                return frameAtTime;
            }
            width = frameAtTime.getWidth();
            height = frameAtTime.getHeight();
            iMax = Math.max(width, height);
            if (iMax > 512) {
                return frameAtTime;
            }
            float f = 512.0f / iMax;
            return Bitmap.createScaledBitmap(frameAtTime, Math.round(width * f), Math.round(f * height), true);
        } catch (Throwable th) {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException unused4) {
            }
            throw th;
        }
        if (frameAtTime == null) {
            return null;
        }
        if (i == 1) {
            if (i == 3) {
                return ThumbnailUtils.extractThumbnail(frameAtTime, 96, 96, 2);
            }
            return frameAtTime;
        }
        width = frameAtTime.getWidth();
        height = frameAtTime.getHeight();
        iMax = Math.max(width, height);
        if (iMax > 512) {
            return frameAtTime;
        }
        float f2 = 512.0f / iMax;
        return Bitmap.createScaledBitmap(frameAtTime, Math.round(width * f2), Math.round(f2 * height), true);
    }
}
