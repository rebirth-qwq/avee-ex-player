package com.daaw.avee.comp.Visualizer.Elements;

import android.graphics.Bitmap;
import android.graphics.RectF;
import com.daaw.avee.Common.Action3;
import com.daaw.avee.Common.Func.Func2;
import com.daaw.avee.Common.Ref;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.AlbumArt.ImageResult;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader;
import com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement;
import com.daaw.avee.comp.Visualizer.Elements.Base.ImageMask;
import com.daaw.avee.comp.Visualizer.Elements.Base.MVariableFloat;
import com.daaw.avee.comp.Visualizer.Elements.Base.MeasuredVar;
import com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentPath;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentPathCircle;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentPathFactory;
import com.daaw.avee.comp.Visualizer.Graphic.GraphicsUtils;
import com.daaw.avee.comp.Visualizer.Graphic.IAtlasTexture;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderPassData;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.VMatrix;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderBinder;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderProgram;
import com.daaw.avee.comp.Visualizer.ICompositionDependencies;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.comp.Visualizer.Meter;
import com.daaw.avee.vendor.blur.FastBlur;
import com.daaw.avee.vendor.glutil.graphics.ITexture;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class ImageElement extends ImageBaseElement {
    public static final String typeName = "Image";
    private ElementImageLoader maskImageLoader;
    private float[] hslaColorFrom = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] hslaColorTo = {1.0f, 1.0f, 1.0f, 1.0f};
    private MeasuredVar measureColor = MeasuredVar.AsNothing();
    private MVariableFloat saturationVar = MVariableFloat.CreateConstantFloat(1.0f);
    private boolean blurEnabled = false;
    private int blurDivider = 5;
    private int pixelRadius = 7;
    private boolean blurredBorder = false;
    private boolean keepAspectRatio = true;
    private ISegmentPath segmentPath = null;
    float[] colorHslaTmp = new float[4];
    float[] colorF4Tmp = new float[4];
    private final VMatrix vpMatTmp = new VMatrix();
    private int maskMode = 0;
    Action3<RenderState, VShaderProgram, RenderPassData> shaderOnBindAction = new Action3<RenderState, VShaderProgram, RenderPassData>() { // from class: com.daaw.avee.comp.Visualizer.Elements.ImageElement.4
        @Override // com.daaw.avee.Common.Action3
        public void onInvoke(RenderState renderState, VShaderProgram vShaderProgram, RenderPassData renderPassData) {
            vShaderProgram.setUniformMatrix("u_projView", false, ImageElement.this.vpMatTmp.getObj());
        }
    };
    private Action3<RenderState, VShaderProgram, RenderPassData> renderBlurShaderOnBindAction = new Action3<RenderState, VShaderProgram, RenderPassData>() { // from class: com.daaw.avee.comp.Visualizer.Elements.ImageElement.5
        @Override // com.daaw.avee.Common.Action3
        public void onInvoke(RenderState renderState, VShaderProgram vShaderProgram, RenderPassData renderPassData) {
            vShaderProgram.setUniformMatrix("u_projView", false, ImageElement.this.vpMatTmp.getObj());
            vShaderProgram.setUniformf("Color2", 0.0f, 0.0f, 0.0f, 1.0f);
            vShaderProgram.setUniformf("saturation", ImageElement.this.saturationVar.getValueAsFloat(renderState.getRes().getMeter()));
            if (ImageElement.this.maskMode != 1) {
                if (ImageElement.this.maskMode != 2) {
                    if (ImageElement.this.maskMode == 3) {
                        vShaderProgram.setUniformf("maskadd", 1.0f);
                        vShaderProgram.setUniformf("maskmul", -1.0f);
                        vShaderProgram.setUniformf("mask_l_add", 1.0f);
                        vShaderProgram.setUniformf("mask_l_mul", 0.0f);
                    } else {
                        vShaderProgram.setUniformf("maskadd", 0.0f);
                        vShaderProgram.setUniformf("maskmul", 1.0f);
                        vShaderProgram.setUniformf("mask_l_add", 1.0f);
                        vShaderProgram.setUniformf("mask_l_mul", 0.0f);
                    }
                } else {
                    vShaderProgram.setUniformf("maskadd", 0.0f);
                    vShaderProgram.setUniformf("maskmul", 1.0f);
                    vShaderProgram.setUniformf("mask_l_add", 1.0f);
                    vShaderProgram.setUniformf("mask_l_mul", -1.0f);
                }
            } else {
                vShaderProgram.setUniformf("maskadd", 0.0f);
                vShaderProgram.setUniformf("maskmul", 1.0f);
                vShaderProgram.setUniformf("mask_l_add", 0.0f);
                vShaderProgram.setUniformf("mask_l_mul", 1.0f);
            }
            if (renderPassData.getTextureIsFlipped(0) == renderPassData.getTextureIsFlipped(1)) {
                vShaderProgram.setUniformf("tex2_y_add", 0.0f);
                vShaderProgram.setUniformf("tex2_y_mul", 1.0f);
            } else {
                vShaderProgram.setUniformf("tex2_y_add", 1.0f);
                vShaderProgram.setUniformf("tex2_y_mul", -1.0f);
            }
        }
    };

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement
    protected boolean onAlbumArtCreateGLResources(RenderState renderState, ImageResult imageResult, Ref<Tuple2<ITexture[], Object>> ref, int i) {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement
    protected void onAlbumArtRender(RenderState renderState) {
    }

    public ImageElement() {
        initialize(new Func2<RenderState, Bitmap, Bitmap>() { // from class: com.daaw.avee.comp.Visualizer.Elements.ImageElement.1
            @Override // com.daaw.avee.Common.Func.Func2
            public Bitmap Invoke(RenderState renderState, Bitmap bitmap) {
                bitmap.getConfig();
                if (!ImageElement.this.blurEnabled) {
                    return bitmap;
                }
                int iMin = Math.min(1 << ImageElement.this.blurDivider, 512);
                int width = (int) (iMin * (bitmap.getWidth() / bitmap.getHeight()));
                try {
                    return FastBlur.fastBlur((width <= 0 || iMin <= 0) ? bitmap : Bitmap.createScaledBitmap(bitmap, width, iMin, true), ImageElement.this.pixelRadius);
                } catch (Exception e) {
                    tlog.w("Art blurring failed: " + e.getMessage());
                    return bitmap;
                } catch (OutOfMemoryError unused) {
                    tlog.w("Art blurring failed: OutOfMemoryError");
                    return bitmap;
                }
            }
        }, new Func2<RenderState, Bitmap, Bitmap>() { // from class: com.daaw.avee.comp.Visualizer.Elements.ImageElement.2
            @Override // com.daaw.avee.Common.Func.Func2
            public Bitmap Invoke(RenderState renderState, Bitmap bitmap) {
                if (!ImageElement.this.blurredBorder || !ImageElement.this.keepAspectRatio) {
                    return null;
                }
                Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, true);
                try {
                    return FastBlur.fastBlur(bitmapCreateScaledBitmap, 7);
                } catch (Exception e) {
                    tlog.w("Art blurring failed: " + e.getMessage());
                    return bitmapCreateScaledBitmap;
                } catch (OutOfMemoryError unused) {
                    tlog.w("Art blurring failed: OutOfMemoryError");
                    return bitmapCreateScaledBitmap;
                }
            }
        });
        this.maskImageLoader = new ElementImageLoader(new ElementImageLoader.IOnNeedReCreateGLResources() { // from class: com.daaw.avee.comp.Visualizer.Elements.ImageElement.3
            @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader.IOnNeedReCreateGLResources
            public void markNeedReCreateGLResources() {
                ImageElement.this.markNeedReCreateGLResourcesDontOverride();
            }
        }, null, null, null);
    }

    public void setColor(int i) {
        GraphicsUtils.intColorToHlsa1(this.hslaColorFrom, i);
    }

    public void setColorTo(int i) {
        GraphicsUtils.intColorToHlsa1(this.hslaColorTo, i);
    }

    public void setMeasureColor(MeasuredVar measuredVar) {
        this.measureColor = measuredVar;
    }

    public void setBlurAmount(boolean z) {
        setBlurAmount(z, 5, 7);
    }

    public void setBlurAmount(boolean z, int i, int i2) {
        if (this.blurEnabled == z && this.blurDivider == i && this.pixelRadius == i2) {
            return;
        }
        this.blurEnabled = z;
        this.blurDivider = i;
        this.pixelRadius = i2;
        markNeedReCreateGLResources();
    }

    public void setCircleShape(boolean z) {
        if (z) {
            this.segmentPath = new SegmentPathCircle();
        } else {
            this.segmentPath = null;
        }
    }

    public void setSegmentPath(ISegmentPath iSegmentPath) {
        this.segmentPath = iSegmentPath;
    }

    public void setKeepAspectRatio(boolean z) {
        if (this.keepAspectRatio == z) {
            return;
        }
        this.keepAspectRatio = z;
        markNeedReCreateGLResources();
    }

    public void setBlurredBorder(boolean z) {
        if (this.blurredBorder == z) {
            return;
        }
        this.blurredBorder = z;
        markNeedReCreateGLResources();
    }

    public void setMaskCustomImage(String str) {
        this.maskImageLoader.setCustomImage(str);
    }

    public void setMaskMode(int i) {
        this.maskMode = i;
    }

    public int getMaskMode() {
        return this.maskMode;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
        setKeepAspectRatio(customPropertiesList.getPropertyBool("keepAspectRatio", true));
        setBlurredBorder(customPropertiesList.getPropertyBool("blurredBorder", false));
        this.hslaColorFrom = customPropertiesList.getPropertyArray4f("Color", new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        this.hslaColorTo = customPropertiesList.getPropertyArray4f("ColorTo", new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        this.measureColor.onApplyCustomization(customPropertiesList.getChild("MeasureColorBlend"), MeasureDefs.Nothing, 0.5f, 0.5f);
        this.saturationVar = customPropertiesList.getPropertyMVariableFloat("saturation", MVariableFloat.CreateConstantFloat(1.0f));
        setBlurAmount(customPropertiesList.getPropertyBool("blurEnabled", false), customPropertiesList.getPropertyInt("blurDivider", 5), customPropertiesList.getPropertyInt("blurRadius", 7));
        CustomPropertiesList child = customPropertiesList.getChild("Shape");
        ISegmentPath iSegmentPathCreate = SegmentPathFactory.create(child.getTypeName("None"), this.segmentPath);
        this.segmentPath = iSegmentPathCreate;
        if (iSegmentPathCreate != null) {
            iSegmentPathCreate.onApplyCustomization(child);
        }
        this.maskImageLoader.setCustomImage(customPropertiesList.getPropertyString("MaskImage", null));
        setMaskMode(customPropertiesList.getPropertySelectStringIndex("maskMode", ImageMask.maskModes, 0));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        super.onReadCustomization(customPropertiesList, iDependencyResourceCounter);
        customPropertiesList.putPropertyBool("keepAspectRatio", this.keepAspectRatio, "1_image");
        customPropertiesList.putPropertyBool("blurredBorder", this.blurredBorder, "1_image");
        customPropertiesList.setCustomizationName(MainActivity.getInstance().getString(R.string.element_title_bar_name_image));
        customPropertiesList.putPropertyArray4fAsCHSLA("Color", this.hslaColorFrom, "2_color");
        customPropertiesList.putPropertyArray4fAsCHSLA("ColorTo", this.hslaColorTo, "2_color");
        this.measureColor.onReadCustomization1D(customPropertiesList.putEmptyChild("MeasureColorBlend", "", "2_color", new String[0]));
        customPropertiesList.putPropertyMVariableFloat("saturation", this.saturationVar, "2_color", 0.0f, 1.0f);
        customPropertiesList.putPropertyBool("blurEnabled", this.blurEnabled, "blur");
        customPropertiesList.putPropertyIntAsRange("blurDivider", this.blurDivider, "blur", 0, 10);
        customPropertiesList.putPropertyIntAsRange("blurRadius", this.pixelRadius, "blur", 1, 25);
        CustomPropertiesList customPropertiesListPutEmptyChild = customPropertiesList.putEmptyChild("Shape", SegmentPathFactory.getTypeName(this.segmentPath), "1_image", SegmentPathFactory.typeNamesClosedAndNone);
        ISegmentPath iSegmentPath = this.segmentPath;
        if (iSegmentPath != null) {
            iSegmentPath.onReadCustomization(customPropertiesListPutEmptyChild);
        }
        iDependencyResourceCounter.PutDependencyResourceName(this.maskImageLoader.getCustomImagePath());
        customPropertiesList.putPropertyStringAsImage("MaskImage", this.maskImageLoader.getCustomImagePath(), "1_image", ElementImageLoader.internalMaskImages);
        customPropertiesList.putSelectString("maskMode", Utils.IndexToString(ImageMask.maskModes, getMaskMode(), ImageMask.maskModes[0]), "1_image", ImageMask.maskModes);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onDestroyGLResources(RenderState renderState) {
        super.onDestroyGLResources(renderState);
        ElementImageLoader elementImageLoader = this.maskImageLoader;
        if (elementImageLoader != null) {
            elementImageLoader.onDestroyGLResources(renderState);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void markNeedReCreateGLResources() {
        super.markNeedReCreateGLResources();
        ElementImageLoader elementImageLoader = this.maskImageLoader;
        if (elementImageLoader != null) {
            elementImageLoader.markNeedReCreateGLResources();
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected boolean onCreateGLResources(RenderState renderState) {
        super.onCreateGLResources(renderState);
        this.maskImageLoader.onCreateGLResources(renderState, measureDrawRectMaxScaleValues(renderState.res.meter), 0);
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onCreateGradualGLResources(RenderState renderState, int i) {
        super.onCreateGradualGLResources(renderState, i);
        this.maskImageLoader.onCreateGradualGLResources(renderState, i);
    }

    private float[] getCurrentColor(Meter meter) {
        Utils.lerpHsla(this.hslaColorFrom, this.hslaColorTo, this.colorHslaTmp, Math.max(Math.min(this.measureColor.measure(meter, 0.0f), 1.0f), 0.0f));
        GraphicsUtils.hlsa1ColorToF4Color(this.colorHslaTmp, this.colorF4Tmp);
        return this.colorF4Tmp;
    }

    private int getCurrentColorInt(Meter meter) {
        Utils.lerpHsla(this.hslaColorFrom, this.hslaColorTo, this.colorHslaTmp, Math.max(Math.min(this.measureColor.measure(meter, 0.0f), 1.0f), 0.0f));
        return GraphicsUtils.hlsa1ColorToInt(this.colorHslaTmp);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onEarlyUpdate(IRenderState iRenderState, FrameBuffer frameBuffer, ICompositionDependencies iCompositionDependencies) {
        super.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
        this.maskImageLoader.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x00a8 A[PHI: r3 r10
      0x00a8: PHI (r3v21 float) = (r3v13 float), (r3v3 float) binds: [B:32:0x00f3, B:10:0x00a5] A[DONT_GENERATE, DONT_INLINE]
      0x00a8: PHI (r10v16 float) = (r10v9 float), (r10v1 float) binds: [B:32:0x00f3, B:10:0x00a5] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ImageBaseElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRender(RenderState renderState, FrameBuffer frameBuffer) {
        float fHeight;
        RenderPassData renderPassData;
        int i;
        RectF rectF;
        float fCenterX;
        float fCenterY;
        super.onRender(renderState, frameBuffer);
        this.maskImageLoader.onRender(renderState, frameBuffer);
        IAtlasTexture texture = this.elementImageLoader.getTexture(renderState);
        IAtlasTexture textureSecondary = this.elementImageLoader.getTextureSecondary(renderState);
        if (texture == null) {
            return;
        }
        IAtlasTexture subZ = texture.getSubZ(this.measureAnimationSpeed.measure(renderState.res.meter) * 2.0f);
        RectF rectFMeasureDrawRect = measureDrawRect(renderState.getRes().getMeter());
        float fMeasureDrawRot = measureDrawRot(renderState.getRes().getMeter());
        int currentColorInt = getCurrentColorInt(renderState.getRes().getMeter());
        createRotationAndVpMat(renderState, this.vpMatTmp, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), fMeasureDrawRot);
        VShaderBinder bufferRenderer_atlasBufferFxLight = renderState.res.getBufferRenderer_atlasBufferFxLight();
        RenderPassData renderPassData2 = new RenderPassData(getBlendMode(), new IAtlasTexture[]{subZ, this.maskImageLoader.getTexture(renderState)}, bufferRenderer_atlasBufferFxLight, this.renderBlurShaderOnBindAction, 1);
        if (this.segmentPath == null) {
            float fWidth = rectFMeasureDrawRect.width();
            float fHeight2 = rectFMeasureDrawRect.height();
            if (this.keepAspectRatio || getKeepAspectRatioAndCropToFit()) {
                float fWidth2 = rectFMeasureDrawRect.width() / rectFMeasureDrawRect.height();
                // 原版字节码（ImageElement.onRender）此处为 int-to-float + div-float；
                // jadx 丢失强转使贴图宽高比变成整数除法（宽<高时恒为 0，等比缩放/裁切完全失效）。
                float width = subZ == null ? 1.0f : ((float) subZ.getWidth()) / ((float) subZ.getHeight());
                boolean z = width > fWidth2;
                if (getKeepAspectRatioAndCropToFit()) {
                    if (!z) {
                        fWidth = rectFMeasureDrawRect.width();
                        fHeight2 = fWidth / width;
                    } else {
                        fHeight = rectFMeasureDrawRect.height();
                        float f = width * fHeight;
                        fHeight2 = fHeight;
                        fWidth = f;
                    }
                } else if (z) {
                    fWidth = rectFMeasureDrawRect.width();
                    fHeight2 = fWidth / width;
                } else {
                    fHeight = rectFMeasureDrawRect.height();
                    float f2 = width * fHeight;
                    fHeight2 = fHeight;
                    fWidth = f2;
                }
                if (textureSecondary != null) {
                    renderPassData = new RenderPassData(getBlendMode(), new IAtlasTexture[]{textureSecondary, this.maskImageLoader.getTexture(renderState)}, bufferRenderer_atlasBufferFxLight, this.renderBlurShaderOnBindAction);
                } else {
                    renderPassData = null;
                }
            } else {
                renderPassData = null;
            }
            float f3 = fHeight2;
            if (getKeepAspectRatioAndCropToFit()) {
                fCenterX = rectFMeasureDrawRect.centerX() - (fWidth * 0.5f);
                fCenterY = rectFMeasureDrawRect.centerY() - (f3 * 0.5f);
                float f4 = ((fWidth / fWidth) - 1.0f) * 0.5f;
                float f5 = ((f3 / f3) - 1.0f) * 0.5f;
                i = currentColorInt;
                rectF = rectFMeasureDrawRect;
                renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, fCenterX, fCenterY, 0.0f, fWidth, f3, i, new Vec2f(0.0f - f4, 0.0f - f5), new Vec2f(f4 + 1.0f, f5 + 1.0f), renderPassData2, false);
            } else {
                i = currentColorInt;
                rectF = rectFMeasureDrawRect;
                fCenterX = rectF.centerX() - (fWidth * 0.5f);
                fCenterY = rectF.centerY() - (0.5f * f3);
                renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, fCenterX, fCenterY, 0.0f, fWidth, f3, i, Vec2f.zero(), Vec2f.one(), renderPassData2, false);
            }
            if (renderPassData != null) {
                renderRectangleBooleanOp(renderState, i, rectF, new RectF(fCenterX, fCenterY, fWidth + fCenterX, f3 + fCenterY), renderPassData);
                return;
            }
            return;
        }
        renderCircle(renderState, rectFMeasureDrawRect, currentColorInt, renderPassData2);
    }

    void renderRectangleBooleanOp(RenderState renderState, int i, RectF rectF, RectF rectF2, RenderPassData renderPassData) {
        float f;
        float fWidth = rectF.width();
        float fHeight = rectF.height();
        RectF rectF3 = new RectF();
        rectF3.set(rectF.left, rectF.top, rectF.right, rectF2.top);
        if (rectF3.height() > 0.0f) {
            renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, rectF3.left, rectF3.top, 0.0f, rectF3.width(), rectF3.height(), i, new Vec2f(0.0f, rectF2.bottom / fHeight), Vec2f.one(), renderPassData, false);
        }
        rectF3.set(rectF.left, rectF2.bottom, rectF.right, rectF.bottom);
        if (rectF3.height() > 0.0f) {
            renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, rectF3.left, rectF3.top, 0.0f, rectF3.width(), rectF3.height(), i, Vec2f.zero(), new Vec2f(1.0f, rectF2.top / fHeight), renderPassData, false);
        }
        rectF3.set(rectF.left, rectF2.top, rectF2.left, rectF2.bottom);
        if (rectF3.width() > 0.0f) {
            f = 0.0f;
            renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, rectF3.left, rectF3.top, 0.0f, rectF3.width(), rectF3.height(), i, Vec2f.zero(), new Vec2f(rectF2.left / fWidth, 1.0f), renderPassData, false);
        } else {
            f = 0.0f;
        }
        rectF3.set(rectF2.right, rectF2.top, rectF.right, rectF2.bottom);
        if (rectF3.width() > f) {
            renderState.res.getBufferRenderer().drawRectangleWHBottom(renderState, rectF3.left, rectF3.top, 0.0f, rectF3.width(), rectF3.height(), i, new Vec2f(rectF2.right / fWidth, 0.0f), Vec2f.one(), renderPassData, false);
        }
    }

    private void renderCircle(RenderState renderState, RectF rectF, int i, RenderPassData renderPassData) {
        float fWidth;
        float fHeight;
        float f;
        float fWidth2 = rectF.width() / rectF.height();
        float width = ((float) renderPassData.getTexturePrimary().getWidth()) / ((float) renderPassData.getTexturePrimary().getHeight());
        if (width > fWidth2) {
            fWidth = rectF.width();
            fHeight = fWidth / width;
        } else if (width < fWidth2) {
            fHeight = rectF.height();
            fWidth = width * fHeight;
        } else {
            fWidth = rectF.width();
            fHeight = rectF.height();
        }
        float f2 = 1.0f;
        if (fWidth > fHeight) {
            f2 = 1.0f / width;
            f = 1.0f;
        } else {
            f = width * 1.0f;
        }
        float f3 = f2 * 0.5f;
        float f4 = f * 0.5f;
        int i2 = (fWidth > fHeight ? 1 : (fWidth == fHeight ? 0 : -1));
        renderState.res.getBufferRenderer().renderPath(renderState, rectF, i, new Vec2f(0.5f - f3, f4 + 0.5f), new Vec2f(f3 + 0.5f, 0.5f - f4), renderPassData, this.segmentPath);
    }
}
