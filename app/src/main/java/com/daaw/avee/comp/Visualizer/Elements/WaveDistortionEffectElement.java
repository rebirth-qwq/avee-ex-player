package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class WaveDistortionEffectElement extends DummyElement {
    public static final String typeName = "WaveDistortion";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_wave_distortion);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\n\nuniform sampler2D u_texture;\nuniform float u_amplitude;\nuniform float u_frequency;\nuniform float u_evolution;\nuniform float u_direction;\nuniform float u_phase;\nuniform float u_multiplier;\n\nvarying vec2 vTexCoord;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    float wave1 = sin((uv.y * u_frequency + u_phase) + u_evolution * u_multiplier * 6.2831);\n    float wave2 = cos((uv.y * u_frequency * 1.7 + u_phase * 2.0) + u_evolution * u_multiplier * 6.2831);\n    float combinedWave = (wave1 + wave2) * 0.5;\n    if (u_direction < 0.5) {\n        uv.x += combinedWave * u_amplitude;\n    } else {\n        uv.y += combinedWave * u_amplitude;\n    }\n    gl_FragColor = texture2D(u_texture, uv);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("amplitude", 0.08f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("frequency", 50.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("evolution", 0.4f, -1.0f, 1.0f, "Evolution");
        addValueWithPropertiesCat("direction", 1.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("phase", 0.0f, -1.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("multiplier", 1.0f, -1.0f, 1.0f, "Evolution");
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
