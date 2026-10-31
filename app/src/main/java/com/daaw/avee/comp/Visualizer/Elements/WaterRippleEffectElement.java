package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class WaterRippleEffectElement extends DummyElement {
    public static final String typeName = "WaterRipple";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_water_ripple);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_amplitude;\nuniform float u_frequency;\nuniform float u_evolution;\nuniform float u_phase;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_decay;\nuniform float u_radius;\nuniform float u_distortion;\nuniform float u_scaleX;\nuniform float u_scaleY;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec2 center = vec2(u_centerX, u_centerY);\n    vec2 delta = uv - center;\n    delta.x *= u_scaleX;\n    delta.y *= u_scaleY;\n    float dist = length(delta);\n    float ripple = 0.0;\n    for (int i = 0; i < 3; i++) {\n        float t = float(i) / 3.0;\n        float r = dist + t * u_radius * 0.3;\n        float wave = sin(r * u_frequency * 40.0 - u_evolution * 3.0 + u_phase + t * 2.0) * u_amplitude * 0.04;\n        wave *= exp(-r * u_decay * 2.0);\n        wave *= (1.0 - t * 0.5);\n        ripple += wave;\n    }\n    ripple /= 2.0;\n    ripple *= (1.0 + u_distortion * 0.5);\n    vec2 waveOffset = normalize(delta + 0.001) * ripple;\n    vec2 newUv = uv + waveOffset;\n    newUv = clamp(newUv, 0.0, 1.0);\n    gl_FragColor = texture2D(u_texture, newUv);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("amplitude", 0.5f, 0.0f, 2.0f, "Wave");
        addValueWithPropertiesCat("frequency", 1.0f, 0.1f, 5.0f, "Wave");
        addValueWithPropertiesCat("evolution", 0.5f, -3.0f, 3.0f, "Wave");
        addValueWithPropertiesCat("phase", 0.0f, -6.28f, 6.28f, "Wave");
        addValueWithPropertiesCat("decay", 0.5f, 0.0f, 3.0f, "Wave");
        addValueWithPropertiesCat("radius", 0.5f, 0.0f, 2.0f, "Wave");
        addValueWithPropertiesCat("distortion", 0.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Position");
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
