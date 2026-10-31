package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CCPowerPinEffectElement extends DummyElement {
    public static final String typeName = "CornerPin";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_corner_pin);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_tlX;\nuniform float u_tlY;\nuniform float u_trX;\nuniform float u_trY;\nuniform float u_blX;\nuniform float u_blY;\nuniform float u_brX;\nuniform float u_brY;\nuniform float u_blendOriginal;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec2 tl = vec2(u_tlX, -u_tlY);\n    vec2 tr = vec2(u_trX, -u_trY);\n    vec2 bl = vec2(u_blX, -u_blY);\n    vec2 br = vec2(u_brX, -u_brY);\n    vec2 baseTL = vec2(0.0, 0.0);\n    vec2 baseTR = vec2(1.0, 0.0);\n    vec2 baseBL = vec2(0.0, 1.0);\n    vec2 baseBR = vec2(1.0, 1.0);\n    vec2 cornerTL = baseTL + tl * 0.5;\n    vec2 cornerTR = baseTR + tr * 0.5;\n    vec2 cornerBL = baseBL + bl * 0.5;\n    vec2 cornerBR = baseBR + br * 0.5;\n    vec2 top = mix(cornerTL, cornerTR, uv.x);\n    vec2 bottom = mix(cornerBL, cornerBR, uv.x);\n    vec2 sampleUV = mix(top, bottom, uv.y);\n    if (sampleUV.x < 0.0 || sampleUV.x > 1.0 || sampleUV.y < 0.0 || sampleUV.y > 1.0) {\n        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n        return;\n    }\n    vec4 color = texture2D(u_texture, sampleUV);\n    vec4 original = texture2D(u_texture, uv);\n    color = mix(color, original, u_blendOriginal);\n    gl_FragColor = clamp(color, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("tlX", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("tlY", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("trX", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("trY", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("blX", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("blY", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("brX", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("brY", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("blendOriginal", 0.0f, 0.0f, 1.0f, "Blend");
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
