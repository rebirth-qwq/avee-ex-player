package com.daaw.avee.comp.Visualizer.Graphic;

import com.daaw.avee.Common.Action3;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.vendor.glutil.graphics.ITexture;

/* JADX INFO: loaded from: classes.dex */
public class RenderPassData {
    private IAtlasTexture atlasTex;
    public int currentBlendMode;
    public VShaderBinder currentShader;
    public Action3<RenderState, VShaderProgram, RenderPassData> currentShaderBindCb;
    private ITexture[] currentTexture;
    private int texWrapMode;
    private boolean[] textureIsFlipped;

    public RenderPassData(int i, IAtlasTexture iAtlasTexture, VShaderBinder vShaderBinder, Action3<RenderState, VShaderProgram, RenderPassData> action3) {
        this(i, iAtlasTexture, vShaderBinder, action3, 1);
    }

    public RenderPassData(int i, IAtlasTexture iAtlasTexture, VShaderBinder vShaderBinder, Action3<RenderState, VShaderProgram, RenderPassData> action3, int i2) {
        this.currentTexture = new ITexture[4];
        this.textureIsFlipped = new boolean[4];
        for (int i3 = 0; i3 < this.currentTexture.length; i3++) {
            this.currentTexture[i3] = null;
        }
        for (int i4 = 0; i4 < this.textureIsFlipped.length; i4++) {
            this.textureIsFlipped[i4] = false;
        }
        this.currentBlendMode = i;
        if (iAtlasTexture != null) {
            this.currentTexture[0] = iAtlasTexture.getTexture2D();
            this.textureIsFlipped[0] = iAtlasTexture.isFlipped();
        }
        this.currentShader = vShaderBinder;
        this.currentShaderBindCb = action3;
        this.texWrapMode = i2;
        this.atlasTex = iAtlasTexture == null ? AtlasTexture.errorTexture : iAtlasTexture;
    }

    public RenderPassData(int i, IAtlasTexture[] iAtlasTextureArr, VShaderBinder vShaderBinder, Action3<RenderState, VShaderProgram, RenderPassData> action3) {
        this(i, iAtlasTextureArr, vShaderBinder, action3, 1);
    }

    public RenderPassData(int i, IAtlasTexture[] iAtlasTextureArr, VShaderBinder vShaderBinder, Action3<RenderState, VShaderProgram, RenderPassData> action3, int i2) {
        this.currentTexture = new ITexture[4];
        this.textureIsFlipped = new boolean[4];
        for (int i3 = 0; i3 < this.currentTexture.length; i3++) {
            this.currentTexture[i3] = null;
        }
        for (int i4 = 0; i4 < this.textureIsFlipped.length; i4++) {
            this.textureIsFlipped[i4] = false;
        }
        this.currentBlendMode = i;
        if (iAtlasTextureArr != null) {
            int iMin = Math.min(iAtlasTextureArr.length, this.currentTexture.length);
            for (int i5 = 0; i5 < iMin; i5++) {
                if (iAtlasTextureArr[i5] != null) {
                    this.currentTexture[i5] = iAtlasTextureArr[i5].getTexture2D();
                    this.textureIsFlipped[i5] = iAtlasTextureArr[i5].isFlipped();
                }
            }
        }
        this.currentShader = vShaderBinder;
        this.currentShaderBindCb = action3;
        this.texWrapMode = i2;
        this.atlasTex = (iAtlasTextureArr == null || iAtlasTextureArr.length <= 0 || iAtlasTextureArr[0] == null) ? AtlasTexture.errorTexture : iAtlasTextureArr[0];
    }

    public RenderPassData() {
        this.currentTexture = new ITexture[4];
        this.textureIsFlipped = new boolean[4];
        setToEmpty();
    }

    public void assignFrom(RenderPassData renderPassData) {
        this.currentBlendMode = renderPassData.currentBlendMode;
        this.currentShader = renderPassData.currentShader;
        this.currentShaderBindCb = renderPassData.currentShaderBindCb;
        int i = 0;
        while (i < this.currentTexture.length) {
            this.currentTexture[i] = i < renderPassData.currentTexture.length ? renderPassData.currentTexture[i] : null;
            i++;
        }
        this.texWrapMode = renderPassData.texWrapMode;
        int i2 = 0;
        while (i2 < this.textureIsFlipped.length) {
            this.textureIsFlipped[i2] = i2 < renderPassData.textureIsFlipped.length && renderPassData.textureIsFlipped[i2];
            i2++;
        }
        this.atlasTex = renderPassData.atlasTex;
    }

    public boolean compare(RenderPassData renderPassData) {
        if (renderPassData.currentBlendMode != this.currentBlendMode || renderPassData.texWrapMode != this.texWrapMode || !Utils.compareNullEqual(renderPassData.currentShader, this.currentShader) || !Utils.compareNullEqual(renderPassData.currentShaderBindCb, this.currentShaderBindCb)) {
            return false;
        }
        int i = 0;
        while (i < this.currentTexture.length) {
            if (!Utils.compareNullEqual(this.currentTexture[i], i < renderPassData.currentTexture.length ? renderPassData.currentTexture[i] : null)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public void setToEmpty() {
        this.currentShaderBindCb = null;
        this.currentShader = null;
        this.currentBlendMode = 0;
        for (int i = 0; i < this.currentTexture.length; i++) {
            this.currentTexture[i] = null;
        }
        for (int i2 = 0; i2 < this.textureIsFlipped.length; i2++) {
            this.textureIsFlipped[i2] = false;
        }
        this.atlasTex = AtlasTexture.errorTexture;
        this.texWrapMode = 2;
    }

    public int getTextureCount() {
        return this.currentTexture.length;
    }

    public ITexture getTexture(int i) {
        if (i < 0 || i >= this.currentTexture.length) {
            return null;
        }
        return this.currentTexture[i];
    }

    public boolean getTextureIsFlipped(int i) {
        return i >= 0 && i < this.textureIsFlipped.length && this.textureIsFlipped[i];
    }

    public float translateU(float f) {
        return this.atlasTex.translateU(f);
    }

    public float translateV(float f) {
        return this.atlasTex.translateV(f);
    }

    public float translateW(float f) {
        return this.atlasTex.translateW(f);
    }

    public float translateW() {
        return this.atlasTex.translateW();
    }

    public IAtlasTexture getTexturePrimary() {
        return this.atlasTex;
    }

    public int getTextureWrapMode() {
        return this.texWrapMode;
    }
}
