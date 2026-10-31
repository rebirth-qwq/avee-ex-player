package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CausticEffectElement extends DummyElement {
    public static final String typeName = "Caustic";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_caustic);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\n\nuniform sampler2D u_texture;\nuniform float u_strength;\nuniform float u_density;\nuniform float u_evolution;\nuniform float u_warp;\nuniform float u_sharpness;\nuniform float u_fadeRadius;\n\nvarying vec2 vTexCoord;\n\nvoid main() {\n    float strength = (u_strength + 1.0) * 0.5;\n    float density = (u_density + 1.0) * 100.0;\n    float speed = u_evolution * 10.0;\n    float warp = (u_warp + 1.0) * 0.5;\n    float contrast = (u_sharpness + 1.0) * 4.95 + 0.1;\n    float radialWidth = (u_fadeRadius + 1.0) * 0.25;\n    vec2 uv = vTexCoord;\n    uv.x += sin(uv.y * density + speed) * warp * 0.1;\n    uv.y += cos(uv.x * density + speed * 1.3) * warp * 0.1;\n    float xWave = sin(uv.x * density + speed * 2.0) * 0.5 + 0.5;\n    float yWave = cos(uv.y * density + speed * 1.7) * 0.5 + 0.5;\n    float caustic = xWave * yWave;\n    caustic = pow(caustic, contrast);\n    vec2 center = vec2(0.5, 0.5);\n    float dist = length(uv - center);\n    float maxDist = 0.7071;\n    float normDist = dist / maxDist;\n    float radialGradient = 1.0 - smoothstep(radialWidth, 1.0, normDist);\n    float offsetAmount = (caustic - 0.5) * strength * 0.1;\n    vec2 grad = vec2(cos(uv.x * density), sin(uv.y * density));\n    vec2 sampleUv = vTexCoord + grad * offsetAmount;\n    sampleUv = clamp(sampleUv, 0.0, 1.0);\n    vec4 originalColor = texture2D(u_texture, vTexCoord);\n    vec4 distortedColor = texture2D(u_texture, sampleUv);\n    float finalAlpha = originalColor.a * radialGradient * caustic;\n    gl_FragColor = vec4(distortedColor.rgb, finalAlpha);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", -0.6f, -1.0f, 1.0f, "Distortion");
        addValueWithPropertiesCat("warp", -0.75f, -1.0f, 1.0f, "Distortion");
        addValueWithPropertiesCat("density", 1.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("sharpness", -1.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("evolution", 0.2f, -3.0f, 3.0f, "Evolution");
        addValueWithPropertiesCat("fadeRadius", 0.0f, -1.0f, 1.0f, "Mask");
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
