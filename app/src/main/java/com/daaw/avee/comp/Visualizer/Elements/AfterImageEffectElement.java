package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class AfterImageEffectElement extends DummyElement {
    public static final String typeName = "AfterImage";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_after_image);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_samples;\nuniform float u_length;\nuniform float u_angle;\nuniform float u_falloffPower;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_scale;\nuniform float u_scalePower;\nuniform float u_twist;\nuniform float u_twistPower;\nuniform float u_jitterAmount;\nuniform float u_jitterFalloff;\nuniform float u_offsetStart;\nuniform float u_bend;\nuniform float u_radial;\nuniform float u_waveAmplitude;\nuniform float u_waveFrequency;\nuniform float u_scatterAmount;\n\nfloat rand(vec2 co) {\n    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);\n}\n\nfloat rand2(vec2 co) {\n    return fract(sin(dot(co, vec2(39.346, 11.135))) * 23758.5453);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    int samples = int(u_samples);\n    if (samples < 2) {\n        gl_FragColor = texture2D(u_texture, uv);\n        return;\n    }\n    const int MAX_SAMPLES = 200;\n    if (samples > MAX_SAMPLES) samples = MAX_SAMPLES;\n    vec2 center = vec2(u_centerX, u_centerY);\n    vec2 dir = vec2(cos(u_angle), sin(u_angle));\n    vec2 perpDir = vec2(-dir.y, dir.x);\n    vec4 finalColor = vec4(0.0);\n    float totalWeight = 0.0;\n    float offsetStart = u_offsetStart;\n    float invSamples = 1.0 / float(samples - 1);\n    for (int i = 0; i < MAX_SAMPLES; i++) {\n        if (i >= samples) break;\n        float t = float(i) * invSamples;\n        t = mix(offsetStart, 1.0, t);\n        if (t > 1.0) break;\n        float offsetLen = t * u_length * 0.5;\n        vec2 offsetVec = dir * offsetLen;\n        float bendAngle = u_bend * t * 2.0;\n        float cosB = cos(bendAngle);\n        float sinB = sin(bendAngle);\n        vec2 bentDir = vec2(dir.x * cosB - dir.y * sinB, dir.x * sinB + dir.y * cosB);\n        offsetVec = bentDir * offsetLen;\n        float radialOffset = u_radial * t * 0.3;\n        vec2 samplePos = center + (uv - center) * (1.0 + u_scale * pow(t, u_scalePower));\n        vec2 radialDir = normalize(samplePos - center + 0.001);\n        offsetVec += radialDir * radialOffset;\n        float twistAngle = u_twist * 3.0 * pow(t, u_twistPower);\n        float ct = cos(twistAngle);\n        float st = sin(twistAngle);\n        vec2 twistedOffset = vec2(offsetVec.x * ct - offsetVec.y * st, offsetVec.x * st + offsetVec.y * ct);\n        float scaleFactor = 1.0 + u_scale * pow(t, u_scalePower);\n        vec2 scaledUv = center + (uv - center) * scaleFactor;\n        float wave = sin(t * 6.2832 * u_waveFrequency) * u_waveAmplitude * 0.15;\n        vec2 waveOffset = perpDir * wave;\n        float jitterVal = u_jitterAmount * 0.15 * pow(t, u_jitterFalloff);\n        vec2 seed = uv + float(i) * vec2(1.7, 3.1);\n        float jitterX = rand(seed);\n        float jitterY = rand2(seed);\n        vec2 jitterOffset = (vec2(jitterX, jitterY) - 0.5) * jitterVal;\n        float scatterVal = u_scatterAmount * 0.15 * t;\n        float scatterSeed = rand(uv + float(i) * 7.3);\n        float scatterAngle = scatterSeed * 6.2832;\n        vec2 scatterDir = vec2(cos(scatterAngle), sin(scatterAngle));\n        vec2 scatterOffset = scatterDir * scatterVal;\n        vec2 sampleUv = scaledUv - twistedOffset + waveOffset + jitterOffset + scatterOffset;\n        sampleUv = clamp(sampleUv, 0.0, 1.0);\n        vec4 sampleColor = texture2D(u_texture, sampleUv);\n        float weight = pow(1.0 - t, u_falloffPower);\n        finalColor += sampleColor * weight;\n        totalWeight += weight;\n    }\n    if (totalWeight > 0.0) {\n        finalColor /= totalWeight;\n    } else {\n        finalColor = texture2D(u_texture, uv);\n    }\n    gl_FragColor = finalColor;\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("samples", 30.0f, 2.0f, 150.0f, "Samples");
        addValueWithPropertiesCat("length", 0.3f, 0.0f, 1.0f, "Samples");
        addValueWithPropertiesCat("falloffPower", 1.5f, 0.1f, 5.0f, "Samples");
        addValueWithPropertiesCat("offsetStart", 0.0f, 0.0f, 0.9f, "Samples");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("scale", 0.0f, -1.5f, 1.5f, "Shape");
        addValueWithPropertiesCat("scalePower", 1.5f, 0.1f, 4.0f, "Shape");
        addValueWithPropertiesCat("twist", 0.0f, -8.0f, 8.0f, "Shape");
        addValueWithPropertiesCat("twistPower", 1.5f, 0.1f, 4.0f, "Shape");
        addValueWithPropertiesCat("angle", 0.0f, -3.14159f, 3.14159f, "Transform");
        addValueWithPropertiesCat("bend", 0.0f, -2.0f, 2.0f, "Transform");
        addValueWithPropertiesCat("radial", 0.0f, -2.0f, 2.0f, "Transform");
        addValueWithPropertiesCat("jitterAmount", 0.0f, 0.0f, 0.4f, "Jitter");
        addValueWithPropertiesCat("jitterFalloff", 1.5f, 0.1f, 4.0f, "Jitter");
        addValueWithPropertiesCat("scatterAmount", 0.0f, 0.0f, 0.5f, "Scatter");
        addValueWithPropertiesCat("waveAmplitude", 0.0f, 0.0f, 0.8f, "Wave");
        addValueWithPropertiesCat("waveFrequency", 2.0f, 0.5f, 10.0f, "Wave");
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
