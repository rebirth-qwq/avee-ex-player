package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class DarkFilterEffectElement extends DummyElement {
    public static final String typeName = "DarkFilter";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_dark_key);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_strength;\nuniform float u_threshold;\nuniform float u_edgeSoftness;\nuniform float u_edgeClean;\nuniform float u_sharpen;\nuniform float u_sharpenStep;\nuniform float u_edgeSmooth;\n\nvoid main() {\n    float strength = u_strength;\n    float threshold = u_threshold;\n    float edgeSoftness = max(u_edgeSoftness, 0.001);\n    float edgeClean = u_edgeClean;\n    float sharpen = u_sharpen;\n    float sharpenStep = 0.002 * (1.0 + u_sharpenStep * 2.0);\n    sharpenStep = clamp(sharpenStep, 0.0001, 0.008);\n    float edgeSmooth = u_edgeSmooth;\n    vec4 col = texture2D(u_texture, vTexCoord);\n    vec4 colSharp = col;\n    if (sharpen > 0.01) {\n        vec4 cu = texture2D(u_texture, vTexCoord + vec2(0.0, sharpenStep));\n        vec4 cd = texture2D(u_texture, vTexCoord - vec2(0.0, sharpenStep));\n        vec4 cl = texture2D(u_texture, vTexCoord - vec2(sharpenStep, 0.0));\n        vec4 cr = texture2D(u_texture, vTexCoord + vec2(sharpenStep, 0.0));\n        vec4 diff = 4.0 * col - cu - cd - cl - cr;\n        colSharp = col + sharpen * diff;\n        colSharp = clamp(colSharp, 0.0, 1.0);\n    }\n    float luma = dot(colSharp.rgb, vec3(0.299, 0.587, 0.114));\n    float transition = edgeSoftness * (0.5 + 0.5 * (1.0 - edgeClean));\n    float blackFactor = 1.0 - smoothstep(threshold - transition, threshold + transition, luma);\n    float alpha = 1.0 - blackFactor * strength;\n    if (edgeSmooth > 0.01) {\n        float lu = dot(texture2D(u_texture, vTexCoord + vec2(0.0, sharpenStep)).rgb, vec3(0.299, 0.587, 0.114));\n        float ld = dot(texture2D(u_texture, vTexCoord - vec2(0.0, sharpenStep)).rgb, vec3(0.299, 0.587, 0.114));\n        float ll = dot(texture2D(u_texture, vTexCoord - vec2(sharpenStep, 0.0)).rgb, vec3(0.299, 0.587, 0.114));\n        float lr = dot(texture2D(u_texture, vTexCoord + vec2(sharpenStep, 0.0)).rgb, vec3(0.299, 0.587, 0.114));\n        float au = 1.0 - (1.0 - smoothstep(threshold - transition, threshold + transition, lu)) * strength;\n        float ad = 1.0 - (1.0 - smoothstep(threshold - transition, threshold + transition, ld)) * strength;\n        float al = 1.0 - (1.0 - smoothstep(threshold - transition, threshold + transition, ll)) * strength;\n        float ar = 1.0 - (1.0 - smoothstep(threshold - transition, threshold + transition, lr)) * strength;\n        float avgAlpha = (alpha + au + ad + al + ar) / 5.0;\n        alpha = mix(alpha, avgAlpha, edgeSmooth);\n    }\n    gl_FragColor = vec4(colSharp.rgb, alpha);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", 0.9f, 0.0f, 1.0f, "Key");
        addValueWithPropertiesCat("threshold", 0.3f, 0.0f, 1.0f, "Key");
        addValueWithPropertiesCat("edgeSoftness", 0.1f, 0.0f, 0.5f, "Key");
        addValueWithPropertiesCat("edgeClean", 0.5f, 0.0f, 1.0f, "Key");
        addValueWithPropertiesCat("sharpen", 0.0f, 0.0f, 1.0f, "Key");
        addValueWithPropertiesCat("sharpenStep", 0.0f, -0.5f, 0.5f, "Key");
        addValueWithPropertiesCat("edgeSmooth", 0.0f, 0.0f, 1.0f, "Key");
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
