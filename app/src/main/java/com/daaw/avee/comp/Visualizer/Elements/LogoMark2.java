package com.daaw.avee.comp.Visualizer.Elements;

import android.graphics.RectF;
import android.net.Uri;
import com.daaw.avee.Common.Action3;
import com.daaw.avee.Common.Easing;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Func.Func1;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.AlbumArt.AlbumArtRequest;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader;
import com.daaw.avee.comp.Visualizer.Graphic.GraphicsUtils;
import com.daaw.avee.comp.Visualizer.Graphic.IAtlasTexture;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderPassData;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderBinder;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderProgram;
import com.daaw.avee.comp.Visualizer.ICompositionDependencies;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class LogoMark2 extends TextElement {
    public static final String typeName = "AppLogo";
    private ElementImageLoader elementImageLoader;
    private AlbumArtRequest currentAlbumArtPath = new AlbumArtRequest(Uri.EMPTY, "internalres:anim128_g_m10_15", "", "");
    private int logoState = 0;
    private float frameNormal = 0.0f;
    private float[] color4f = new float[4];
    private float showingTimeAcc = 0.0f;
    private final float logoSize = 2.4f;
    private final int fontSize = 26;

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    public LogoMark2() {
        ElementImageLoader elementImageLoader = new ElementImageLoader(new ElementImageLoader.IOnNeedReCreateGLResources() { // from class: com.daaw.avee.comp.Visualizer.Elements.LogoMark2.1
            @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader.IOnNeedReCreateGLResources
            public void markNeedReCreateGLResources() {
                LogoMark2.this.markNeedReCreateGLResourcesDontOverride();
            }
        }, new Func1<IRenderState, AlbumArtRequest>() { // from class: com.daaw.avee.comp.Visualizer.Elements.LogoMark2.2
            @Override // com.daaw.avee.Common.Func.Func1
            public AlbumArtRequest onInvoke(IRenderState iRenderState) {
                return LogoMark2.this.currentAlbumArtPath;
            }
        }, null, null);
        this.elementImageLoader = elementImageLoader;
        elementImageLoader.setColorKeyEnabled(true);
        setColor(-1869574000);
        setBlendMode(4);
        setFontSize(26);
        setCustomFontPath("internal_3");
        setText(MeasureDefs.textMarkedArtistAndTitle);
        setPosition(0.0f, 1.0f);
        setLocalPosition(0.0f, 1.2f);
        setPositionUniform(true, false);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected boolean shouldRender() {
        return super.shouldRender();
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement
    public void setColor(int i) {
        super.setColor(i);
        GraphicsUtils.intColorToF4Color(this.color4f, i);
    }

    public void setColorNotUpdateOriginal(int i) {
        super.setColor(i);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement
    public void setFontSize(int i) {
        super.setFontSize(i);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void setPosition(Vec2f vec2f) {
        super.setPosition(vec2f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void setLocalPosition(Vec2f vec2f) {
        super.setLocalPosition(vec2f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization0(customPropertiesList);
        setVisible(customPropertiesList.getPropertyBool("visible", true));
        setColor(customPropertiesList.getPropertyInt("color", -1869574000));
        setPositionUniform(true, false);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        super.onReadCustomization0(customPropertiesList);
        customPropertiesList.setCustomizationName(MainActivity.getInstance().getString(R.string.element_title_bar_name_app_logo));
        customPropertiesList.putPropertyBool("visible", this.visible, "0_general");
        customPropertiesList.updatePropertyBool("visible", "pb", "Avee水印");
        customPropertiesList.putPropertyIntAsCRGBA("color", GraphicsUtils.f4ColorToIntColor(this.color4f), "0_general");
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void markNeedReCreateGLResources() {
        super.markNeedReCreateGLResources();
        ElementImageLoader elementImageLoader = this.elementImageLoader;
        if (elementImageLoader != null) {
            elementImageLoader.markNeedReCreateGLResources();
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onDestroyGLResources(RenderState renderState) {
        super.onDestroyGLResources(renderState);
        ElementImageLoader elementImageLoader = this.elementImageLoader;
        if (elementImageLoader != null) {
            elementImageLoader.onDestroyGLResources(renderState);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public boolean onCreateGLResources(RenderState renderState) {
        super.onCreateGLResources(renderState);
        this.elementImageLoader.onCreateGLResources(renderState, measureDrawRectMaxScaleValues(renderState.res.meter), 0);
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onCreateGradualGLResources(RenderState renderState, int i) {
        this.elementImageLoader.onCreateGradualGLResources(renderState, i);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onEarlyUpdate(IRenderState iRenderState, FrameBuffer frameBuffer, ICompositionDependencies iCompositionDependencies) {
        super.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
        this.elementImageLoader.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRender(RenderState renderState, FrameBuffer frameBuffer) {
        super.onRender(renderState, frameBuffer);
        this.elementImageLoader.onRender(renderState, frameBuffer);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement
    public void onRenderTextElement(RenderState renderState, RectF rectF) {
        super.onRenderTextElement(renderState, rectF);
        if (renderState.getTriggerLogo() == 1) {
            TriggerShow();
        } else if (renderState.getTriggerLogo() == 2) {
            TriggerHide();
        }
        if (this.showingTimeAcc > 3.5f) {
            TriggerHide();
        }
        int i = this.logoState;
        if (i == 0) {
            setColorNotUpdateOriginal(0);
            this.frameNormal = 0.0f;
            this.showingTimeAcc = 0.0f;
            return;
        }
        if (i == 1) {
            float frameTimeF = this.frameNormal + (renderState.getFrameTimeF() * 1.25f);
            this.frameNormal = frameTimeF;
            if (frameTimeF >= 0.999f) {
                this.frameNormal = 0.999f;
                this.logoState = 2;
            }
            Math.max(0.0f, (this.frameNormal - 0.5f) * 2.0f);
            float fEaseInOutExpo = Easing.easeInOutExpo(0.0f, this.frameNormal, 0.0f, 1.0f, 1.0f);
            float[] fArr = this.color4f;
            setColorNotUpdateOriginal(GraphicsUtils.f4ColorToIntColor(new float[]{fArr[0] * fEaseInOutExpo, fArr[1] * fEaseInOutExpo, fArr[2] * fEaseInOutExpo, fEaseInOutExpo * fArr[3]}));
        } else if (i == 2) {
            this.showingTimeAcc += renderState.getFrameTimeF();
            float[] fArr2 = this.color4f;
            setColorNotUpdateOriginal(GraphicsUtils.f4ColorToIntColor(new float[]{fArr2[0], fArr2[1], fArr2[2], fArr2[3]}));
            this.frameNormal = 0.999f;
        } else if (i == 3) {
            float frameTimeF2 = this.frameNormal - (renderState.getFrameTimeF() * 1.25f);
            this.frameNormal = frameTimeF2;
            if (frameTimeF2 <= 0.0f) {
                this.frameNormal = 0.0f;
                this.logoState = 0;
            }
            float fEaseInOutExpo2 = Easing.easeInOutExpo(0.0f, Math.max(0.0f, (this.frameNormal - 0.25f) * 1.3333334f), 0.0f, 1.0f, 1.0f);
            float[] fArr3 = this.color4f;
            setColorNotUpdateOriginal(GraphicsUtils.f4ColorToIntColor(new float[]{fArr3[0] * fEaseInOutExpo2, fArr3[1] * fEaseInOutExpo2, fArr3[2] * fEaseInOutExpo2, fEaseInOutExpo2 * fArr3[3]}));
        }
        IAtlasTexture texture = this.elementImageLoader.getTexture(renderState);
        if (texture != null) {
            IAtlasTexture subZ = texture.getSubZ(this.frameNormal);
            float fHeight = rectF.height() * 2.4f;
            renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, (rectF.height() * 0.15f * 2.4f) + rectF.left, rectF.bottom - fHeight, 0.0f, fHeight, fHeight, -1, Vec2f.zero(), Vec2f.one(), new RenderPassData(getBlendMode(), subZ, (VShaderBinder) null, (Action3<RenderState, VShaderProgram, RenderPassData>) null), false);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement
    public Vec2f getDrawOffset(RectF rectF) {
        float fEaseInOutExpo = Easing.easeInOutExpo(0.0f, this.frameNormal, 0.0f, 1.0f, 1.0f);
        float fHeight = rectF.height() * 0.0f;
        return new Vec2f((((fEaseInOutExpo * 0.5f) + 0.5f) * (fHeight + (rectF.height() * 1.05f * 2.4f) + (rectF.height() * 0.15f * 2.4f))) + (-fHeight), rectF.height() * (-0.16f) * 2.4f);
    }

    public void TriggerShow() {
        if (this.logoState != 2) {
            this.logoState = 1;
        }
    }

    public void TriggerHide() {
        if (this.logoState != 0) {
            this.logoState = 3;
        }
    }
}
