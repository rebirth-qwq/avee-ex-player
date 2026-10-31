package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class HeatWaveEffectElement extends DummyElement {
    public static final String typeName = "HeatWave";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_heat_wave);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_speed;\nuniform float u_amplitude;\nuniform float u_frequency;\nuniform float u_angle;\nuniform float u_evolution;\nuniform float u_intensity;\n\nvoid main() {\n    float speed = u_speed * 2.0;\n    float amplitude = (u_amplitude + 1.0) * 0.5 * 0.05;\n    float frequency = (u_frequency + 1.0) * 0.5 * 6.0 + 2.0;\n    float angle = u_angle * 3.14159;\n    float time = u_evolution * 10.0;\n    float strength = (u_intensity + 1.0) * 0.5;\n    vec2 uv = vTexCoord;\n    float t = time * speed;\n    float ca = cos(angle);\n    float sa = sin(angle);\n    vec2 rotatedUV = vec2(uv.x * ca - uv.y * sa, uv.x * sa + uv.y * ca);\n    float wave1 = sin(rotatedUV.y * frequency * 2.0 + t * 1.2);\n    float wave2 = cos(rotatedUV.x * frequency * 1.7 + t * 0.9);\n    float wave3 = sin((rotatedUV.x + rotatedUV.y) * frequency * 1.3 + t * 1.5);\n    float combined = (wave1 + wave2 + wave3) / 3.0;\n    vec2 dirY = vec2(-sa, ca);\n    vec2 dirX = vec2(ca, sa);\n    vec2 offset = (dirY * 1.0 + dirX * 0.4) * combined * amplitude * strength;\n    vec2 distortedUV = uv + offset;\n    distortedUV = clamp(distortedUV, 0.0, 1.0);\n    gl_FragColor = texture2D(u_texture, distortedUV);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("speed", 0.5f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("amplitude", 0.5f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("frequency", 0.5f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("angle", 0.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("evolution", 0.0f, -3.0f, 3.0f, "Wave");
        addValueWithPropertiesCat("intensity", 0.5f, -1.0f, 1.0f, "Wave");
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
