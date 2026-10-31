package com.daaw.avee.comp.Visualizer.Elements;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.daaw.avee.Common.Action3;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.Vec2i;
import com.daaw.avee.Common.Vec3f;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.Element;
import com.daaw.avee.comp.Visualizer.Graphic.CharSet;
import com.daaw.avee.comp.Visualizer.Graphic.GraphicsUtils;
import com.daaw.avee.comp.Visualizer.Graphic.RenderPassData;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.SpriteFont;
import com.daaw.avee.comp.Visualizer.Graphic.VMatrix;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderProgram;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider;
import com.google.android.exoplayer2.util.MimeTypes;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class TextElement extends Element {
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final String typeName = "Text";
    protected int color1;
    private String customFontPath;
    protected SpriteFont font1;
    protected int fontSize;
    protected int maxfontSizePixelsLimit;
    Action3<RenderState, VShaderProgram, RenderPassData> shaderOnBindAction;
    private String text;
    protected int textAlign;
    public final VMatrix vpMatTmp;
    public static final String[] fontTypefaces = {"internal_0:2131230895", "internal_1:2131230896", "internal_2:2131230897", "internal_3:2131230898"};
    public static final String[] alignModes = {"Center", "Left", "Right"};

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    public void onRenderTextElement(RenderState renderState, RectF rectF) {
    }

    public TextElement() {
        super(0, 0.5f, 0.5f);
        this.textAlign = 0;
        this.text = MeasureDefs.textArtistOrTitle;
        this.fontSize = 30;
        this.color1 = -1;
        this.customFontPath = "";
        this.maxfontSizePixelsLimit = 500;
        this.vpMatTmp = new VMatrix();
        this.shaderOnBindAction = new Action3<RenderState, VShaderProgram, RenderPassData>() { // from class: com.daaw.avee.comp.Visualizer.Elements.TextElement.1
            @Override // com.daaw.avee.Common.Action3
            public void onInvoke(RenderState renderState, VShaderProgram vShaderProgram, RenderPassData renderPassData) {
                vShaderProgram.setUniformMatrix("u_projView", false, TextElement.this.vpMatTmp.getObj());
            }
        };
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setFontSize(int i) {
        if (this.fontSize != i) {
            this.fontSize = Math.min(500, i);
            markNeedReCreateGLResources();
        }
    }

    public void setColor(int i) {
        this.color1 = i;
    }

    public void setColorAlpha(int i) {
        this.color1 = GraphicsUtils.setIntColorAlpha(this.color1, i);
    }

    public String getCustomFontPath() {
        return this.customFontPath;
    }

    public void setCustomFontPath(String str) {
        if (!Utils.compareNullStrings(this.customFontPath, str)) {
            this.customFontPath = str;
            markNeedReCreateGLResources();
        }
    }

    public void limitColor(float f, float f2) {
        float[] fArr = {1.0f, 1.0f, 1.0f, 1.0f};
        GraphicsUtils.intColorToHlsa360(fArr, this.color1);
        fArr[2] = Math.max(f2, fArr[2]);
        fArr[3] = Math.max(f, fArr[3]);
        setColor(GraphicsUtils.hlsa360ColorToInt(fArr));
    }

    public void limitFontSize(int i) {
        this.fontSize = Math.max(i, this.fontSize);
    }

    public void setTextAlign(int i) {
        this.textAlign = i;
    }

    public int getTextAlign() {
        return this.textAlign;
    }

    protected int getFontSize() {
        return this.fontSize;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
        super.onApplyCustomizationAdditional(customPropertiesList);
        setText(customPropertiesList.getPropertyString(MimeTypes.BASE_TYPE_TEXT, this.text));
        setColor(customPropertiesList.getPropertyInt("color", -1));
        setFontSize(customPropertiesList.getPropertyInt("fontSize", 30));
        this.customFontPath = customPropertiesList.getPropertyString("typeFace", "");
        setTextAlign(customPropertiesList.getPropertySelectStringIndex("textAlign", alignModes, 0));
    }

    protected void onApplyCustomization0(CustomPropertiesList customPropertiesList) {
        setCustomFontPath(customPropertiesList.getPropertyString("typeFace", getCustomFontPath()));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        super.onReadCustomization(customPropertiesList, iDependencyResourceCounter);
        super.onReadCustomizationAdditional(customPropertiesList);
        customPropertiesList.setCustomizationName(MainActivity.getInstance().getString(R.string.element_title_bar_name_text));
        customPropertiesList.putPropertyStringAsTxtPr(MimeTypes.BASE_TYPE_TEXT, this.text, "appearance");
        customPropertiesList.putPropertyIntAsCRGBA("color", this.color1, "appearance");
        customPropertiesList.putPropertyIntAsRange("fontSize", this.fontSize, "appearance", 8, 100);
        customPropertiesList.putPropertyStringAsAssetPr("typeFace", getCustomFontPath(), "appearance", fontTypefaces);
        customPropertiesList.putSelectString("textAlign", alignModes[this.textAlign], "appearance", alignModes);
        String customFontPath = getCustomFontPath();
        if (customFontPath != null && !customFontPath.isEmpty() && iDependencyResourceCounter != null) {
            iDependencyResourceCounter.PutDependencyResourceName(customFontPath);
        }
    }

    protected void onReadCustomization0(CustomPropertiesList customPropertiesList) {
        customPropertiesList.putPropertyStringAsAssetPr("typeFace", getCustomFontPath(), "appearance", fontTypefaces);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onDestroyGLResources(RenderState renderState) {
        super.onDestroyGLResources(renderState);
        if (this.font1 != null) {
            this.font1.dispose();
            this.font1 = null;
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public boolean onCreateGLResources(RenderState renderState) {
        int iMin = Math.min((int) renderState.getRes().getMeter().measureScreenSpace(this.fontSize * 0.14f * 0.01f), this.maxfontSizePixelsLimit);
        Typeface typefaceResolveTypeface = resolveTypeface(this.customFontPath, renderState);
        if (this.font1 != null) {
            this.font1.dispose();
            this.font1 = null;
        }
        this.font1 = new SpriteFont(typefaceResolveTypeface, iMin, CharSet.createAscii32to126());
        return super.onCreateGLResources(renderState);
    }

    /* JADX WARN: Code duplicated, block: B:29:0x0078  */
    private Typeface resolveTypeface(String str, RenderState renderState) {
        Typeface typefaceCreateFromAsset;
        if (str == null || str.isEmpty()) {
            return Typeface.DEFAULT;
        }
        if ("default_bold".equals(str)) {
            return Typeface.DEFAULT_BOLD;
        }
        if (str.startsWith("internal_")) {
            Context appContext = PlayerCore.s().getAppContext();
            if (appContext != null) {
                try {
                    if ("internal_0".equals(str)) {
                        typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/greatvibes-regular.otf");
                    } else if ("internal_1".equals(str)) {
                        typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/notomono-regular.ttf");
                    } else if ("internal_2".equals(str)) {
                        typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/orbitron-medium.otf");
                    } else if ("internal_3".equals(str)) {
                        typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/orkney-light.otf");
                    } else {
                        typefaceCreateFromAsset = Typeface.DEFAULT;
                    }
                } catch (Exception e) {
                    return Typeface.DEFAULT;
                }
            } else {
                typefaceCreateFromAsset = Typeface.DEFAULT;
            }
            return typefaceCreateFromAsset;
        }
        InternalVisualizationDataProvider internalVisualizationDataProvider = renderState.res.visualizationData;
        if (internalVisualizationDataProvider != null) {
            try {
                Typeface typefaceOnRequestFont = internalVisualizationDataProvider.onRequestFont(str, AppPreferences.getCurrentThemeId());
                if (typefaceOnRequestFont != null && typefaceOnRequestFont != Typeface.DEFAULT) {
                    return typefaceOnRequestFont;
                }
            } catch (Exception e2) {
            }
        }
        if (str.startsWith("/")) {
            try {
                return Typeface.createFromFile(str);
            } catch (Exception e3) {
                return Typeface.DEFAULT;
            }
        }
        return Typeface.DEFAULT;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRender(RenderState renderState, FrameBuffer frameBuffer) {
        super.onRender(renderState, frameBuffer);
        if (this.font1 != null && this.font1.isValid()) {
            String strMeasureText = renderState.res.meter.measureText(this.text);
            Vec2i vec2iMeasureText = renderState.res.getFontRenderer().measureText(this.font1, strMeasureText);
            float f = vec2iMeasureText.x;
            RectF rectFMeasureDrawRect = measureDrawRect(renderState.res.getMeter(), vec2iMeasureText);
            createRotationAndVpMat(renderState, this.vpMatTmp, rectFMeasureDrawRect.left + (rectFMeasureDrawRect.width() * 0.5f), rectFMeasureDrawRect.top - (rectFMeasureDrawRect.height() * 0.5f), measureDrawRot(renderState.getRes().getMeter()));
            Vec2f drawOffset = getDrawOffset(rectFMeasureDrawRect, f);
            renderState.res.getFontRenderer().drawText(renderState, this.font1, new Vec3f(rectFMeasureDrawRect.left + drawOffset.x, drawOffset.y + rectFMeasureDrawRect.top, 0.0f), strMeasureText, this.color1, 0, false, 0, 0, 0, 0, new RenderPassData(getBlendMode(), this.font1.getEntryTexture(), renderState.res.getBufferRenderer_atlasBufferVPMat(), this.shaderOnBindAction));
            onRenderTextElement(renderState, rectFMeasureDrawRect);
        }
    }

    public Vec2f getDrawOffset(RectF rectF, float f) {
        float f2;
        switch (this.textAlign) {
            case 1:
                f2 = (-f) / 2.0f;
                break;
            case 2:
                f2 = f / 2.0f;
                break;
            default:
                f2 = 0.0f;
                break;
        }
        return new Vec2f(f2, 0.0f);
    }

    public Vec2f getDrawOffset(RectF rectF) {
        return new Vec2f(0.0f, 0.0f);
    }
}
