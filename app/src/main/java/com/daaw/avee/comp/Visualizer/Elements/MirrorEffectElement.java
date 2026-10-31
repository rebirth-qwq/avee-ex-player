package com.daaw.avee.comp.Visualizer.Elements;

import android.graphics.RectF;
import com.daaw.avee.Common.Action3;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.Element;
import com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderPassData;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.VMatrix;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderBinder;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderProgram;
import com.daaw.avee.comp.Visualizer.ICompositionDependencies;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class MirrorEffectElement extends Element {
    public static final String[] internalImages = {"composition:0"};
    public static final String[] mirrorModes = {"Horizontal", "Vertical", "HorizontalAndVertical"};
    public static final String typeName = "MirrorEffect";
    private int color2;
    private boolean flipMirror;
    private int mirrorMode;
    private Action3<RenderState, VShaderProgram, RenderPassData> shaderOnBindAction;
    ElementImageLoader targetImageLoader;
    public final VMatrix vpMatTmp;

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    public MirrorEffectElement() {
        super(4, 1.0f, 1.0f);
        this.color2 = -1;
        this.mirrorMode = 0;
        this.flipMirror = false;
        this.vpMatTmp = new VMatrix();
        this.shaderOnBindAction = new Action3<RenderState, VShaderProgram, RenderPassData>() { // from class: com.daaw.avee.comp.Visualizer.Elements.MirrorEffectElement.2
            @Override // com.daaw.avee.Common.Action3
            public void onInvoke(RenderState renderState, VShaderProgram vShaderProgram, RenderPassData renderPassData) {
                vShaderProgram.setUniformMatrix("u_projView", false, MirrorEffectElement.this.vpMatTmp.getObj());
            }
        };
        setBlendMode(4);
        setScale(1.0f, 1.0f);
        this.targetImageLoader = new ElementImageLoader(new ElementImageLoader.IOnNeedReCreateGLResources() { // from class: com.daaw.avee.comp.Visualizer.Elements.MirrorEffectElement.1
            @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader.IOnNeedReCreateGLResources
            public void markNeedReCreateGLResources() {
                MirrorEffectElement.this.markNeedReCreateGLResourcesDontOverride();
            }
        }, null, null, null);
        setTargetImage("composition:1");
    }

    public void setTargetImage(String str) {
        this.targetImageLoader.setCustomImage(str);
    }

    public String getTargetImage() {
        return this.targetImageLoader.getCustomImagePath();
    }

    public void setColor2(int i) {
        this.color2 = i;
    }

    public void setMirrorMode(int i) {
        this.mirrorMode = i;
    }

    public int getMirrorMode() {
        return this.mirrorMode;
    }

    public void setFlipMirror(boolean z) {
        this.flipMirror = z;
    }

    public boolean getFlipMirror() {
        return this.flipMirror;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
        super.onApplyCustomizationAdditional(customPropertiesList);
        setTargetImage(customPropertiesList.getPropertyString("TargetImage", "composition:1"));
        setMirrorMode(Utils.StringToIndex(mirrorModes, customPropertiesList.getPropertySelectString("mirrorMode", mirrorModes[0])));
        setFlipMirror(customPropertiesList.getPropertyBool("flipMirror", false));
        setColor2(customPropertiesList.getPropertyInt("color", -1));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        super.onReadCustomization(customPropertiesList, iDependencyResourceCounter);
        super.onReadCustomizationAdditional(customPropertiesList);
        customPropertiesList.setCustomizationName(MainActivity.getInstance().getString(R.string.element_title_bar_name_mirror_effect));
        iDependencyResourceCounter.PutDependencyResourceName(getTargetImage());
        customPropertiesList.putPropertyStringAsImage("TargetImage", getTargetImage(), "1_appearance", internalImages);
        customPropertiesList.putSelectString("mirrorMode", Utils.IndexToString(mirrorModes, getMirrorMode(), mirrorModes[0]), "1_appearance", mirrorModes);
        customPropertiesList.putPropertyBool("flipMirror", getFlipMirror(), "1_appearance");
        customPropertiesList.putPropertyIntAsCRGBA("color", this.color2, "1_appearance");
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void markNeedReCreateGLResources() {
        super.markNeedReCreateGLResources();
        ElementImageLoader elementImageLoader = this.targetImageLoader;
        if (elementImageLoader != null) {
            elementImageLoader.markNeedReCreateGLResources();
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onDestroyGLResources(RenderState renderState) {
        super.onDestroyGLResources(renderState);
        ElementImageLoader elementImageLoader = this.targetImageLoader;
        if (elementImageLoader != null) {
            elementImageLoader.onDestroyGLResources(renderState);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected boolean onCreateGLResources(RenderState renderState) {
        this.targetImageLoader.onCreateGLResources(renderState, measureDrawRectMaxScaleValues(renderState.res.meter), 0);
        return super.onCreateGLResources(renderState);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onCreateGradualGLResources(RenderState renderState, int i) {
        this.targetImageLoader.onCreateGradualGLResources(renderState, i);
        super.onCreateGradualGLResources(renderState, i);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onEarlyUpdate(IRenderState iRenderState, FrameBuffer frameBuffer, ICompositionDependencies iCompositionDependencies) {
        super.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
        this.targetImageLoader.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRender(RenderState renderState, FrameBuffer frameBuffer) {
        this.targetImageLoader.onRender(renderState, frameBuffer);
        super.onRender(renderState, frameBuffer);
        RectF rectFMeasureDrawRect = measureDrawRect(renderState.getRes().getMeter());
        createRotationAndVpMat(renderState, this.vpMatTmp, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), measureDrawRot(renderState.getRes().getMeter()));
        Vec2f vec2f = new Vec2f(0.0f, 0.0f);
        Vec2f vec2f2 = new Vec2f(1.0f, 0.0f);
        VShaderBinder bufferRenderer_atlasBufferVPMat = renderState.res.getBufferRenderer_atlasBufferVPMat();
        RenderPassData renderPassData = new RenderPassData(getBlendMode(), this.targetImageLoader.getTexture(renderState), bufferRenderer_atlasBufferVPMat, this.shaderOnBindAction);
        int i = this.mirrorMode;
        if (i == 0) {
            float fCenterX = rectFMeasureDrawRect.centerX();
            if (this.flipMirror) {
                vec2f.x = 0.0f;
                vec2f.y = 1.0f;
                vec2f2.x = 0.5f;
                vec2f2.y = 0.0f;
            } else {
                vec2f.x = 1.0f;
                vec2f.y = 1.0f;
                vec2f2.x = 0.5f;
                vec2f2.y = 0.0f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.left, rectFMeasureDrawRect.top, fCenterX, rectFMeasureDrawRect.top, fCenterX, rectFMeasureDrawRect.bottom, rectFMeasureDrawRect.left, rectFMeasureDrawRect.bottom, 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            if (this.flipMirror) {
                vec2f.x = 0.5f;
                vec2f.y = 1.0f;
                vec2f2.x = 0.0f;
                vec2f2.y = 0.0f;
            } else {
                vec2f.x = 0.5f;
                vec2f.y = 1.0f;
                vec2f2.x = 1.0f;
                vec2f2.y = 0.0f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, fCenterX, rectFMeasureDrawRect.top, rectFMeasureDrawRect.right, rectFMeasureDrawRect.top, rectFMeasureDrawRect.right, rectFMeasureDrawRect.bottom, fCenterX, rectFMeasureDrawRect.bottom, 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            return;
        }
        if (i == 1) {
            if (this.flipMirror) {
                vec2f.x = 0.0f;
                vec2f.y = 0.0f;
                vec2f2.x = 1.0f;
                vec2f2.y = 0.5f;
            } else {
                vec2f.x = 0.0f;
                vec2f.y = 1.0f;
                vec2f2.x = 1.0f;
                vec2f2.y = 0.5f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.left, rectFMeasureDrawRect.top, rectFMeasureDrawRect.right, rectFMeasureDrawRect.top, rectFMeasureDrawRect.right, rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.left, rectFMeasureDrawRect.centerY(), 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            if (this.flipMirror) {
                vec2f.x = 0.0f;
                vec2f.y = 0.5f;
                vec2f2.x = 1.0f;
                vec2f2.y = 0.0f;
            } else {
                vec2f.x = 0.0f;
                vec2f.y = 0.5f;
                vec2f2.x = 1.0f;
                vec2f2.y = 1.0f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.left, rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.right, rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.right, rectFMeasureDrawRect.bottom, rectFMeasureDrawRect.left, rectFMeasureDrawRect.bottom, 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            return;
        }
        if (i == 2) {
            if (this.flipMirror) {
                vec2f.x = 0.0f;
                vec2f.y = 1.0f;
                vec2f2.x = 0.5f;
                vec2f2.y = 0.5f;
            } else {
                vec2f.x = 1.0f;
                vec2f.y = 1.0f;
                vec2f2.x = 0.5f;
                vec2f2.y = 0.5f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.left, rectFMeasureDrawRect.top, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.top, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.left, rectFMeasureDrawRect.centerY(), 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            if (this.flipMirror) {
                vec2f.x = 0.5f;
                vec2f.y = 1.0f;
                vec2f2.x = 0.0f;
                vec2f2.y = 0.5f;
            } else {
                vec2f.x = 0.5f;
                vec2f.y = 1.0f;
                vec2f2.x = 1.0f;
                vec2f2.y = 0.5f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.top, rectFMeasureDrawRect.right, rectFMeasureDrawRect.top, rectFMeasureDrawRect.right, rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            if (this.flipMirror) {
                vec2f.x = 0.0f;
                vec2f.y = 0.5f;
                vec2f2.x = 0.5f;
                vec2f2.y = 1.0f;
            } else {
                vec2f.x = 1.0f;
                vec2f.y = 0.5f;
                vec2f2.x = 0.5f;
                vec2f2.y = 1.0f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.left, rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.bottom, rectFMeasureDrawRect.left, rectFMeasureDrawRect.bottom, 0.0f, this.color2, vec2f, vec2f2, renderPassData);
            if (this.flipMirror) {
                vec2f.x = 0.5f;
                vec2f.y = 0.5f;
                vec2f2.x = 0.0f;
                vec2f2.y = 1.0f;
            } else {
                vec2f.x = 0.5f;
                vec2f.y = 0.5f;
                vec2f2.x = 1.0f;
                vec2f2.y = 1.0f;
            }
            renderState.getBufferRenderer().drawRectangle0123(renderState, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.right, rectFMeasureDrawRect.centerY(), rectFMeasureDrawRect.right, rectFMeasureDrawRect.bottom, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.bottom, 0.0f, this.color2, vec2f, vec2f2, renderPassData);
        }
    }
}
