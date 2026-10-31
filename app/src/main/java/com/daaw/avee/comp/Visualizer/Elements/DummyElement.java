package com.daaw.avee.comp.Visualizer.Elements;

import android.content.res.Resources;
import android.graphics.RectF;
import android.opengl.GLES20;
import com.daaw.avee.Common.Action3;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.Element;
import com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader;
import com.daaw.avee.comp.Visualizer.Elements.Base.MVariableFloat;
import com.daaw.avee.comp.Visualizer.Graphic.IAtlasTexture;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderPassData;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.VMatrix;
import com.daaw.avee.comp.Visualizer.Graphic.VShaderProgram;
import com.daaw.avee.comp.Visualizer.ICompositionDependencies;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.comp.Visualizer.Meter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class DummyElement extends Element {
    public static final String[] internalImages = {"composition:0"};
    public static final String typeName = "DummyElement";
    private boolean flipMirror;
    private VShaderProgram loadedShader;
    private int mirrorMode;
    private boolean reloadShader;
    public String shaderFrag;
    private Action3<RenderState, VShaderProgram, RenderPassData> shaderOnBindAction;
    public String shaderVert;
    ElementImageLoader targetImageLoader;
    public LinkedHashMap<String, MVariableFloat> u_values;
    public HashMap<String, String> valueCategories;
    public HashMap<String, float[]> valueProperties;
    public final VMatrix vpMatTmp;

    public void addValueWithProperties(String str, float f, float f2, float f3) {
        this.u_values.put(str, MVariableFloat.CreateConstantFloat(f));
        this.valueProperties.put(str, new float[]{f, f2, f3});
    }

    public void addValueWithPropertiesCat(String str, float f, float f2, float f3, String str2) {
        this.u_values.put(str, MVariableFloat.CreateConstantFloat(f));
        this.valueProperties.put(str, new float[]{f, f2, f3});
        HashMap<String, String> map = this.valueCategories;
        if (str2 == null) {
            str2 = "variables";
        }
        map.put(str, str2);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_dummy_element);
    }

    public void initCustomShader() {
        Resources resources = PlayerCore.s().getAppContext().getResources();
        this.shaderVert = UtilsFileSys.readResource(resources, R.raw.buffer_fisheye_vert);
        this.shaderFrag = UtilsFileSys.readResource(resources, R.raw.buffer_fisheye_frag);
    }

    public void initCustomValues() {
        addValueWithProperties("value1", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("value2", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("value3", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("value4", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("value5", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("value6", 0.0f, -1.0f, 1.0f);
    }

    public boolean isShaderEditable() {
        return true;
    }

    public DummyElement() {
        super(4, 1.0f, 1.0f);
        this.u_values = new LinkedHashMap<>();
        this.valueProperties = new HashMap<>();
        this.valueCategories = new HashMap<>();
        this.vpMatTmp = new VMatrix();
        this.mirrorMode = 0;
        this.flipMirror = false;
        initCustomShader();
        initCustomValues();
        this.shaderOnBindAction = new Action3<RenderState, VShaderProgram, RenderPassData>() { // from class: com.daaw.avee.comp.Visualizer.Elements.DummyElement.1
            @Override // com.daaw.avee.Common.Action3
            public void onInvoke(RenderState renderState, VShaderProgram vShaderProgram, RenderPassData renderPassData) {
                vShaderProgram.setUniformMatrix("u_projView", false, DummyElement.this.vpMatTmp.getObj());
                Meter meter = renderState.getRes().getMeter();
                LinkedHashMap<String, MVariableFloat> linkedHashMap = DummyElement.this.u_values;
                for (String str : linkedHashMap.keySet()) {
                    vShaderProgram.setUniformf("u_".concat(str), linkedHashMap.get(str).getValueAsFloat(meter));
                }
            }
        };
        setBlendMode(4);
        setScale(1.0f, 1.0f);
        this.targetImageLoader = new ElementImageLoader(new ElementImageLoader.IOnNeedReCreateGLResources() { // from class: com.daaw.avee.comp.Visualizer.Elements.DummyElement.2
            @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ElementImageLoader.IOnNeedReCreateGLResources
            public void markNeedReCreateGLResources() {
                DummyElement.this.markNeedReCreateGLResourcesDontOverride();
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

    public int getMirrorMode() {
        return this.mirrorMode;
    }

    public boolean getFlipMirror() {
        return this.flipMirror;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
        super.onApplyCustomizationAdditional(customPropertiesList);
        setTargetImage(customPropertiesList.getPropertyString("TargetImage", "composition:1"));
        this.shaderVert = customPropertiesList.getPropertyString("shaderVertex", this.shaderVert);
        this.shaderFrag = customPropertiesList.getPropertyString("shaderFragment", this.shaderFrag);
        LinkedHashMap<String, MVariableFloat> linkedHashMap = this.u_values;
        for (String str : linkedHashMap.keySet()) {
            linkedHashMap.put(str, customPropertiesList.getPropertyMVariableFloat(str, linkedHashMap.get(str)));
        }
        this.reloadShader = true;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        super.onReadCustomization(customPropertiesList, iDependencyResourceCounter);
        super.onReadCustomizationAdditional(customPropertiesList);
        customPropertiesList.setCustomizationName(getCustomizationDisplayName());
        iDependencyResourceCounter.PutDependencyResourceName(getTargetImage());
        customPropertiesList.putPropertyStringAsImage("TargetImage", getTargetImage(), "1_appearance", internalImages);
        if (isShaderEditable()) {
            customPropertiesList.putPropertyStringAsTxtPr("shaderFragment", this.shaderFrag, "shader");
            customPropertiesList.putPropertyStringAsTxtPr("shaderVertex", this.shaderVert, "shader");
        }
        HashMap<String, float[]> map = this.valueProperties;
        LinkedHashMap<String, MVariableFloat> linkedHashMap = this.u_values;
        for (String str : linkedHashMap.keySet()) {
            float[] fArr = map.get(str);
            float f = fArr[1];
            float f2 = fArr[2];
            String str2 = "variables";
            String str3 = this.valueCategories.get(str);
            if (str3 != null) {
                str2 = str3;
            }
            customPropertiesList.putPropertyMVariableFloat(str, linkedHashMap.get(str), str2, f, f2);
        }
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
        IAtlasTexture texture = this.targetImageLoader.getTexture(renderState);
        if (texture == null) {
            super.onRender(renderState, frameBuffer);
            return;
        }
        onRenderCheckResources(renderState);
        RectF rectFMeasureDrawRect = measureDrawRect(renderState.getRes().getMeter());
        createRotationAndVpMat(renderState, this.vpMatTmp, rectFMeasureDrawRect.centerX(), rectFMeasureDrawRect.centerY(), measureDrawRot(renderState.getRes().getMeter()));
        setupFrameBuffer(renderState);
        super.onRender(renderState, frameBuffer);
        RenderState.RenderResources renderResources = renderState.res;
        if (this.loadedShader == null || this.reloadShader) {
            this.reloadShader = false;
            this.loadedShader = renderResources.safeDisposeShader(this.loadedShader);
            this.loadedShader = renderResources.loadShaderFromString(this.shaderVert, this.shaderFrag);
            tlog.d("shaderReload");
        }
        VShaderProgram vShaderProgram = this.loadedShader;
        renderState.drawFullscreenQuad(rectFMeasureDrawRect.left, rectFMeasureDrawRect.top, -1, Vec2f.zero(), Vec2f.one(), new RenderPassData(getBlendMode(), texture, vShaderProgram != null ? renderResources.createCustomShaderBinder(vShaderProgram) : null, this.shaderOnBindAction));
    }

    private void setupFrameBuffer(RenderState renderState) {
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        renderState.setTextureWrapping(1);
    }
}
