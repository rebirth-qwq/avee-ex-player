package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class OldTVEffectElement extends DummyElement {
    public static final String typeName = "OldTV";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_old_tv);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_waveStrength;\nuniform float u_waveDensity;\nuniform float u_waveSpeed;\nuniform float u_waveHeight;\nuniform float u_waveStretch;\nuniform float u_waveSharpness;\nuniform float u_scanDensity;\nuniform float u_scanHeight;\nuniform float u_scanSpeed;\nuniform float u_scanOpacity;\nuniform float u_scanBgAlpha;\n\nvoid main() {\n    float waveStrength = u_waveStrength;\n    float waveDensity = u_waveDensity;\n    float waveSpeed = u_waveSpeed;\n    float waveHeight = max(u_waveHeight, 0.001);\n    float waveStretch = u_waveStretch;\n    float waveSharpness = u_waveSharpness * 2.0 + 0.5;\n    float t = fract(vTexCoord.y * waveDensity + waveSpeed);\n    float distToLine = abs(t - 0.5);\n    float factor = 0.0;\n    if (t > 0.5) {\n        float raw = 1.0 - distToLine / waveHeight;\n        raw = clamp(raw, 0.0, 1.0);\n        factor = pow(raw, waveSharpness);\n    }\n    float stretch = 1.0 + factor * waveStrength * waveStretch;\n    vec2 uv = vTexCoord;\n    uv.x = 0.5 + (vTexCoord.x - 0.5) / stretch;\n    uv = clamp(uv, 0.0, 1.0);\n    vec4 color = texture2D(u_texture, uv);\n    float scanDensity = u_scanDensity;\n    float scanHeight = max(u_scanHeight, 0.001);\n    float scanSpeed = u_scanSpeed;\n    float scanOpacity = u_scanOpacity;\n    float scanBgAlpha = u_scanBgAlpha;\n    float phase = vTexCoord.y * scanDensity + scanSpeed;\n    float scanT = fract(phase);\n    float mask = step(scanT, scanHeight);\n    float bgFactor = 1.0 - scanBgAlpha;\n    float lineFactor = scanOpacity * mask;\n    vec3 finalColor = color.rgb * bgFactor * (1.0 - lineFactor);\n    float finalAlpha = bgFactor * (1.0 - lineFactor) + lineFactor;\n    gl_FragColor = vec4(finalColor, finalAlpha);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("waveStrength", 0.5f, 0.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("waveDensity", 4.5f, 0.0f, 25.0f, "Wave");
        addValueWithPropertiesCat("waveSpeed", 5.0f, -20.0f, 20.0f, "Wave");
        addValueWithPropertiesCat("waveHeight", 0.15f, 0.01f, 0.5f, "Wave");
        addValueWithPropertiesCat("waveStretch", 0.5f, 0.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("waveSharpness", 0.5f, 0.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("scanDensity", 40.0f, 10.0f, 100.0f, "Scan");
        addValueWithPropertiesCat("scanHeight", 0.15f, 0.01f, 0.5f, "Scan");
        addValueWithPropertiesCat("scanSpeed", 10.0f, -20.0f, 20.0f, "Scan");
        addValueWithPropertiesCat("scanOpacity", 0.4f, 0.0f, 1.0f, "Scan");
        addValueWithPropertiesCat("scanBgAlpha", 0.0f, 0.0f, 1.0f, "Blend");
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
