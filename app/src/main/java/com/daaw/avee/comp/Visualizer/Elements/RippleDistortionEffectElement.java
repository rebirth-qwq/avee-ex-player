package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class RippleDistortionEffectElement extends DummyElement {
    public static final String typeName = "RippleDistortion";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_ripple_distortion);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_evolution;\nuniform float u_density;\nuniform float u_speed;\nuniform float u_amplitude;\nuniform float u_intensity;\nuniform float u_scale;\nuniform float u_red;\nuniform float u_green;\nuniform float u_blue;\nuniform float u_mix;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec2 center = vec2(0.5, 0.5);\n    vec2 dir = uv - center;\n    float dist = length(dir) * u_scale;\n    float angle = atan(dir.y, dir.x);\n    float time = u_evolution;\n    float wave1 = sin(dist * 20.0 * u_density - time * 2.0 * u_speed) * 0.5 + 0.5;\n    float wave2 = cos(dist * 15.0 * u_density + time * 1.5 * u_speed) * 0.3 + 0.3;\n    float wave3 = sin(dist * 12.0 * u_density + angle * 4.0 - time * 1.2 * u_speed) * 0.2 + 0.2;\n    float waveEffect = wave1 * 0.5 + wave2 * 0.3 + wave3 * 0.2;\n    float attenuation = 1.0 - smoothstep(0.3, 0.9, dist);\n    waveEffect *= attenuation;\n    vec2 radialOffset = normalize(dir + 0.001) * waveEffect * u_amplitude;\n    vec2 angleOffset = vec2(cos(angle + waveEffect * 5.0), sin(angle + waveEffect * 5.0)) * waveEffect * u_amplitude * 0.5;\n    vec2 distortedUV = uv + radialOffset + angleOffset;\n    distortedUV.x += sin(distortedUV.y * 30.0 + time * 3.0) * 0.002 * waveEffect;\n    distortedUV.y += cos(distortedUV.x * 30.0 + time * 3.0) * 0.002 * waveEffect;\n    vec4 color = texture2D(u_texture, distortedUV);\n    float glowFactor = waveEffect * u_intensity * attenuation;\n    vec3 waveColor = vec3(u_red, u_green, u_blue);\n    float hueShift = sin(dist * 10.0 - time * 2.0) * 0.2 + 0.8;\n    vec3 glow = waveColor * glowFactor * hueShift;\n    vec3 finalColor = mix(color.rgb, color.rgb + glow, u_mix);\n    finalColor = mix(finalColor, finalColor * (1.0 + glowFactor * 0.5), u_mix);\n    gl_FragColor = vec4(finalColor, color.a);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("evolution", 0.0f, -3.0f, 3.0f, "Timing");
        addValueWithPropertiesCat("density", 0.8f, 0.2f, 2.0f, "Wave");
        addValueWithPropertiesCat("speed", 1.0f, 0.3f, 3.0f, "Timing");
        addValueWithPropertiesCat("amplitude", 0.06f, 0.0f, 0.2f, "Wave");
        addValueWithPropertiesCat("intensity", 0.5f, 0.0f, 1.0f, "Wave");
        addValueWithPropertiesCat("scale", 1.0f, 0.3f, 2.5f, "Transform");
        addValueWithPropertiesCat("red", 0.4f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("green", 0.7f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("blue", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("mix", 0.7f, 0.0f, 1.0f, "Blend");
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
