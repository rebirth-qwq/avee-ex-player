package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class DepthDisplaceEffectElement extends DummyElement {
    public static final String typeName = "DepthDisplace";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_depth_displace);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_depthStrength;\nuniform float u_offsetX;\nuniform float u_offsetY;\nuniform float u_scale;\nuniform float u_tolerance;\nuniform float u_warpStrength;\nuniform float u_warpRadius;\nuniform float u_warpSmooth;\n\nfloat hash(vec2 p) {\n    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);\n}\n\nfloat smoothNoise(vec2 p) {\n    vec2 i = floor(p);\n    vec2 f = fract(p);\n    f = f * f * (3.0 - 2.0 * f);\n    float a = hash(i);\n    float b = hash(i + vec2(1.0, 0.0));\n    float c = hash(i + vec2(0.0, 1.0));\n    float d = hash(i + vec2(1.0, 1.0));\n    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);\n}\n\nfloat fbm(vec2 p) {\n    float value = 0.0;\n    float amp = 0.5;\n    float freq = 1.0;\n    for (int i = 0; i < 3; i++) {\n        value += amp * smoothNoise(p * freq);\n        amp *= 0.5;\n        freq *= 2.0;\n    }\n    return value;\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec4 texColor = texture2D(u_texture, uv);\n    float luma = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n    float tolerance = u_tolerance;\n    float depthMap = smoothstep(tolerance, 1.0, luma);\n    float fixedPoint = depthMap;\n    vec2 offset = vec2(u_offsetX, u_offsetY) * fixedPoint * u_depthStrength;\n    vec2 displacedUV = uv + offset;\n    float warpStrength = u_warpStrength;\n    if (warpStrength > 0.001) {\n        float warpRadius = max(u_warpRadius, 0.001);\n        float warpSmooth = u_warpSmooth * 4.0 + 0.5;\n        float moveFactor = 1.0 - fixedPoint;\n        vec2 warpOffset = vec2(0.0);\n        float totalWeight = 0.0;\n        float samples = 16.0;\n        for (int i = 0; i < 32; i++) {\n            if (float(i) >= samples) break;\n            float fi = float(i);\n            float angle = fi / samples * 6.28318;\n            float radius = warpRadius * (0.2 + 0.8 * fi / samples);\n            vec2 sampleUV = uv + vec2(cos(angle), sin(angle)) * radius;\n            sampleUV = clamp(sampleUV, 0.0, 1.0);\n            vec4 sampleColor = texture2D(u_texture, sampleUV);\n            float sampleLuma = dot(sampleColor.rgb, vec3(0.299, 0.587, 0.114));\n            float sampleDepth = smoothstep(tolerance, 1.0, sampleLuma);\n            float sampleMove = 1.0 - sampleDepth;\n            float dist = length(sampleUV - uv);\n            float weight = exp(-pow(dist / warpRadius, warpSmooth));\n            vec2 dir = normalize(sampleUV - uv + 0.001);\n            warpOffset += dir * sampleMove * weight;\n            totalWeight += weight;\n        }\n        if (totalWeight > 0.001) {\n            warpOffset /= totalWeight;\n        }\n        warpOffset *= moveFactor * warpStrength * 0.04;\n        displacedUV += warpOffset;\n    }\n    displacedUV = (displacedUV - 0.5) * u_scale + 0.5;\n    if (displacedUV.x < 0.0 || displacedUV.x > 1.0 || displacedUV.y < 0.0 || displacedUV.y > 1.0) {\n        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n    } else {\n        gl_FragColor = texture2D(u_texture, displacedUV);\n    }\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("depthStrength", 0.4f, -1.0f, 1.0f, "Depth");
        addValueWithPropertiesCat("offsetX", 0.02f, -1.0f, 1.0f, "Depth");
        addValueWithPropertiesCat("offsetY", 0.0f, -1.0f, 1.0f, "Depth");
        addValueWithPropertiesCat("scale", 1.0f, 0.1f, 3.0f, "Depth");
        addValueWithPropertiesCat("tolerance", 0.3f, 0.0f, 1.0f, "Depth");
        addValueWithPropertiesCat("warpStrength", 0.0f, 0.0f, 2.0f, "Warp");
        addValueWithPropertiesCat("warpRadius", 0.1f, 0.01f, 0.5f, "Warp");
        addValueWithPropertiesCat("warpSmooth", 0.5f, 0.0f, 1.0f, "Warp");
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
