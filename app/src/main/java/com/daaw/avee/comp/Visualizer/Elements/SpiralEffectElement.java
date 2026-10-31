package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class SpiralEffectElement extends DummyElement {
    public static final String typeName = "Spiral";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_spiral);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\n\nuniform sampler2D u_texture;\nuniform float u_strength;\nuniform float u_density;\nuniform float u_twist;\nuniform float u_evolution;\nuniform float u_fadeWidth;\nuniform float u_sharpness;\n\nvarying vec2 vTexCoord;\n\nfloat atan2_cont(vec2 p) {\n    float a = atan(p.y, p.x);\n    float s = sign(p.y);\n    return a + 3.14159 * (1.0 - s) * step(0.0, -p.x);\n}\n\nvoid main() {\n    float strength = (u_strength + 1.0) * 0.5;\n    float density = (u_density + 1.0) * 50.0;\n    float twist = u_twist * 8.0;\n    float speed = u_evolution * 10.0;\n    float radialWidth = (u_fadeWidth + 1.0) * 0.25;\n    float sharpness = (u_sharpness + 1.0) * 4.95 + 0.1;\n    vec2 center = vec2(0.5, 0.5);\n    vec2 offset = vTexCoord - center;\n    float dist = length(offset);\n    float angle = atan2_cont(offset);\n    float phase = dist * density + angle * twist + speed * 5.0;\n    float wave = sin(phase) * 0.5 + 0.5;\n    wave = pow(wave, sharpness);\n    float maxDist = 0.7071;\n    float normDist = dist / maxDist;\n    float radialGradient;\n    if (normDist < radialWidth) {\n        radialGradient = 1.0;\n    } else {\n        radialGradient = 1.0 - (normDist - radialWidth) / (1.0 - radialWidth);\n    }\n    radialGradient = clamp(radialGradient, 0.0, 1.0);\n    vec2 dir = normalize(offset + 1e-6);\n    float offsetAmount = (wave - 0.5) * strength * 0.15;\n    vec2 uv = vTexCoord + dir * offsetAmount;\n    uv = clamp(uv, 0.0, 1.0);\n    vec4 originalColor = texture2D(u_texture, vTexCoord);\n    vec4 distortedColor = texture2D(u_texture, uv);\n    float finalAlpha = originalColor.a * radialGradient * wave;\n    gl_FragColor = vec4(distortedColor.rgb, finalAlpha);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", 0.0f, -1.0f, 1.0f, "Distortion");
        addValueWithPropertiesCat("twist", 0.0f, -1.0f, 1.0f, "Distortion");
        addValueWithPropertiesCat("density", 0.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("sharpness", 0.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("evolution", -0.1f, -3.0f, 3.0f, "Evolution");
        addValueWithPropertiesCat("fadeWidth", 0.0f, -1.0f, 1.0f, "Mask");
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
