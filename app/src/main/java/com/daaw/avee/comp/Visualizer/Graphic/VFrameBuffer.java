package com.daaw.avee.comp.Visualizer.Graphic;

import android.opengl.GLES20;
import com.daaw.avee.Common.DbgVar;
import com.daaw.avee.Common.Func.Func;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import com.daaw.avee.vendor.compat.LWJGLException;

/* JADX INFO: loaded from: classes.dex */
public class VFrameBuffer extends FrameBuffer {
    private DbgVar dbgVar;

    public static VFrameBuffer createSafe(int i, int i2, int i3, int i4, boolean z) {
        VTexture vTextureCheckIfValid = new VTexture(i, i2, i3, i4, z).checkIfValid();
        if (vTextureCheckIfValid == null) {
            return null;
        }
        try {
            return new VFrameBuffer(vTextureCheckIfValid, true);
        } catch (Exception e) {
            tlog.w("exception " + e.getMessage());
            return null;
        }
    }

    private VFrameBuffer(VTexture vTexture, boolean z) throws LWJGLException {
        super(vTexture, z);
        this.dbgVar = new DbgVar();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.dbgVar.assertDisposed(new Func() { // from class: com.daaw.avee.comp.Visualizer.Graphic.-$$Lambda$VFrameBuffer$6_WHNd5Ftg2ZVa_dVeW1f_u5N8M
            @Override // com.daaw.avee.Common.Func.Func
            public final Object onInvoke() {
                return lambda$finalize$0$VFrameBuffer();
            }
        });
    }

    public /* synthetic */ Boolean lambda$finalize$0$VFrameBuffer() {
        return Boolean.valueOf(!isValid());
    }

    public boolean isValid() {
        return this.id != 0;
    }

    public VFrameBuffer checkIfValid() {
        if (isValid()) {
            return this;
        }
        dispose();
        tlog.w("FrameBuffer is not valid");
        return null;
    }

    @Override // com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer
    public void begin() {
        if (isValid()) {
            GLES20.glViewport(0, 0, getWidth(), getHeight());
            GLES20.glBindFramebuffer(36160, this.id);
        }
    }

    @Override // com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer
    public void end() {
        if (isValid()) {
            GLES20.glBindFramebuffer(36160, 0);
        }
    }
}
