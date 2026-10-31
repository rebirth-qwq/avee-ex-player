package com.daaw.avee.vendor.grafika.gles;

import android.graphics.Bitmap;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public class EglSurfaceBase {
    protected static final String TAG = "Grafika";
    protected EglCore mEglCore;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private int mWidth = -1;
    private int mHeight = -1;

    protected EglSurfaceBase(EglCore eglCore) {
        this.mEglCore = eglCore;
    }

    public void createWindowSurface(Object obj) {
        if (this.mEGLSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.mEGLSurface = this.mEglCore.createWindowSurface(obj);
    }

    public void createOffscreenSurface(int i, int i2) {
        if (this.mEGLSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.mEGLSurface = this.mEglCore.createOffscreenSurface(i, i2);
        this.mWidth = i;
        this.mHeight = i2;
    }

    public int getWidth() {
        int i = this.mWidth;
        return i < 0 ? this.mEglCore.querySurface(this.mEGLSurface, 12375) : i;
    }

    public int getHeight() {
        int i = this.mHeight;
        return i < 0 ? this.mEglCore.querySurface(this.mEGLSurface, 12374) : i;
    }

    public void releaseEglSurface() {
        this.mEglCore.releaseSurface(this.mEGLSurface);
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mHeight = -1;
        this.mWidth = -1;
    }

    public void makeCurrent() {
        this.mEglCore.makeCurrent(this.mEGLSurface);
    }

    public void makeCurrentReadFrom(EglSurfaceBase eglSurfaceBase) {
        this.mEglCore.makeCurrent(this.mEGLSurface, eglSurfaceBase.mEGLSurface);
    }

    public boolean swapBuffers() {
        boolean zSwapBuffers = this.mEglCore.swapBuffers(this.mEGLSurface);
        if (!zSwapBuffers) {
            Log.d("Grafika", "WARNING: swapBuffers() failed");
        }
        return zSwapBuffers;
    }

    public void setPresentationTime(long j) {
        this.mEglCore.setPresentationTime(this.mEGLSurface, j);
    }

    public void saveFrame(File file) throws Throwable {
        if (!this.mEglCore.isCurrent(this.mEGLSurface)) {
            throw new RuntimeException("Expected EGL context/surface is not current");
        }
        String string = file.toString();
        int width = getWidth();
        int height = getHeight();
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(width * height * 4);
        byteBufferAllocateDirect.order(ByteOrder.LITTLE_ENDIAN);
        GLES20.glReadPixels(0, 0, width, height, 6408, 5121, byteBufferAllocateDirect);
        GlUtil.checkGlError("glReadPixels");
        byteBufferAllocateDirect.rewind();
        BufferedOutputStream bufferedOutputStream = null;
        try {
            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(string));
            try {
                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmapCreateBitmap.copyPixelsFromBuffer(byteBufferAllocateDirect);
                bitmapCreateBitmap.compress(Bitmap.CompressFormat.PNG, 90, bufferedOutputStream2);
                bitmapCreateBitmap.recycle();
                bufferedOutputStream2.close();
                Log.d("Grafika", "Saved " + width + "x" + height + " frame as '" + string + "'");
            } catch (Throwable th) {
                bufferedOutputStream = bufferedOutputStream2;
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
        }
    }
}
