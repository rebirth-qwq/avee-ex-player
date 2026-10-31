package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class HeatDistortionEffectElement extends DummyElement {
    public static final String typeName = "HeatDistortion";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_heat_distortion);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_strength;\nuniform float u_frequency;\nuniform float u_evolution;\nuniform float u_phase;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_decay;\nuniform float u_turbulence;\nuniform float u_scaleX;\nuniform float u_scaleY;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec2 center = vec2(u_centerX, u_centerY);\n    vec2 delta = uv - center;\n    float dist = length(delta);\n    float time = u_evolution * 2.0 + u_phase;\n    float wave1 = sin(uv.x * u_frequency * 30.0 * u_scaleX + time) * u_strength * 0.02;\n    float wave2 = cos(uv.y * u_frequency * 30.0 * u_scaleY + time * 0.8) * u_strength * 0.02;\n    float wave3 = sin((uv.x + uv.y) * u_frequency * 20.0 * u_turbulence + time * 1.3) * u_strength * 0.01 * u_turbulence;\n    float attenuation = exp(-dist * u_decay * 2.0);\n    wave1 *= (1.0 - attenuation * 0.8);\n    wave2 *= (1.0 - attenuation * 0.8);\n    wave3 *= (1.0 - attenuation * 0.9);\n    vec2 offsetUv = uv + vec2(wave1, wave2) + vec2(wave3, -wave3) * 0.3;\n    offsetUv = clamp(offsetUv, 0.0, 1.0);\n    gl_FragColor = texture2D(u_texture, offsetUv);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", 0.5f, 0.0f, 2.0f, "Distortion");
        addValueWithPropertiesCat("frequency", 1.0f, 0.1f, 5.0f, "Distortion");
        addValueWithPropertiesCat("evolution", 0.5f, -3.0f, 3.0f, "Distortion");
        addValueWithPropertiesCat("phase", 0.0f, -6.28f, 6.28f, "Distortion");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("decay", 0.5f, 0.0f, 3.0f, "Distortion");
        addValueWithPropertiesCat("turbulence", 0.5f, 0.0f, 2.0f, "Distortion");
        addValueWithPropertiesCat("scaleX", 1.0f, 0.1f, 3.0f, "Scale");
        addValueWithPropertiesCat("scaleY", 1.0f, 0.1f, 3.0f, "Scale");
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
