package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class DropShadowEffectElement extends DummyElement {
    public static final String typeName = "DropShadow";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_drop_shadow);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_distance;\nuniform float u_angle;\nuniform float u_blurRadius;\nuniform float u_shadowR;\nuniform float u_shadowG;\nuniform float u_shadowB;\nuniform float u_shadowA;\nuniform float u_scale;\nuniform float u_softness;\nuniform float u_offsetX;\nuniform float u_offsetY;\nuniform float u_density;\nuniform float u_rotation;\nuniform float u_twist;\nuniform float u_stretchX;\nuniform float u_stretchY;\nuniform float u_falloff;\nuniform float u_samples;\nuniform float u_blendOriginal;\n\nfloat gaussianWeight(float x, float sigma) {\n    return exp(-(x * x) / (2.0 * sigma * sigma));\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec4 texColor = texture2D(u_texture, uv);\n    float scaleFactor = 1.0 + u_scale * 0.5;\n    vec2 shadowUV = (uv - 0.5) / scaleFactor + 0.5;\n    float rotAngle = u_rotation * 6.28318;\n    float cosR = cos(rotAngle);\n    float sinR = sin(rotAngle);\n    vec2 centered = shadowUV - 0.5;\n    centered = vec2(centered.x * cosR - centered.y * sinR, centered.x * sinR + centered.y * cosR);\n    centered.x = centered.x * u_stretchX;\n    centered.y = centered.y * u_stretchY;\n    shadowUV = centered + 0.5;\n    vec2 dir = vec2(cos(u_angle * 6.28318), sin(u_angle * 6.28318));\n    float density = max(u_density, 0.001);\n    float distOffset = u_distance * 0.002 * density;\n    float twistAngle = u_twist * 3.0 * length(centered);\n    float cosT = cos(twistAngle);\n    float sinT = sin(twistAngle);\n    vec2 twistedDir = vec2(dir.x * cosT - dir.y * sinT, dir.x * sinT + dir.y * cosT);\n    shadowUV = shadowUV + twistedDir * distOffset + vec2(u_offsetX, u_offsetY) * 0.002;\n    float shadowAlpha = texture2D(u_texture, shadowUV).a;\n    if (u_blurRadius > 0.0) {\n        float radius = u_blurRadius;\n        float sigma = max(radius * 0.3, 0.001);\n        int samples = int(min(u_samples, 31.0));\n        float totalWeight = 0.0;\n        float blurred = 0.0;\n        float stepSize = 0.002 * density;\n        for (int i = -15; i <= 15; i++) {\n            if (i > samples) break;\n            float fi = float(i);\n            float weight = gaussianWeight(fi, sigma);\n            vec2 sampleUV = shadowUV + twistedDir * fi * stepSize;\n            float a = texture2D(u_texture, sampleUV).a;\n            blurred += a * weight;\n            totalWeight += weight;\n        }\n        blurred = blurred / max(totalWeight, 0.001);\n        shadowAlpha = blurred;\n    }\n    float softnessFactor = 1.0 - u_softness * 0.6;\n    shadowAlpha = pow(shadowAlpha, softnessFactor);\n    float falloffFactor = 1.0 - u_falloff * 0.8;\n    shadowAlpha = shadowAlpha * (1.0 - u_falloff * 0.5) + pow(shadowAlpha, 1.0 / max(falloffFactor, 0.001)) * u_falloff * 0.5;\n    shadowAlpha = clamp(shadowAlpha * u_shadowA, 0.0, 1.0);\n    vec4 shadowColor = vec4(u_shadowR, u_shadowG, u_shadowB, shadowAlpha);\n    float blendFactor = u_blendOriginal;\n    float outAlpha = max(texColor.a, shadowAlpha);\n    vec3 outColor = mix(shadowColor.rgb, texColor.rgb, texColor.a * blendFactor + (1.0 - blendFactor));\n    gl_FragColor = vec4(outColor, outAlpha);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("distance", 10.0f, 0.0f, 50.0f, "Shadow");
        addValueWithPropertiesCat("angle", 0.25f, -1.0f, 1.0f, "Shadow");
        addValueWithPropertiesCat("blurRadius", 8.0f, 0.0f, 30.0f, "Shadow");
        addValueWithPropertiesCat("scale", 0.0f, -0.5f, 1.0f, "Shadow");
        addValueWithPropertiesCat("softness", 0.5f, 0.0f, 1.0f, "Shadow");
        addValueWithPropertiesCat("density", 1.0f, 0.1f, 3.0f, "Shadow");
        addValueWithPropertiesCat("falloff", 0.0f, 0.0f, 1.0f, "Shadow");
        addValueWithPropertiesCat("samples", 15.0f, 2.0f, 31.0f, "Shadow");
        addValueWithPropertiesCat("shadowR", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("shadowG", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("shadowB", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("shadowA", 0.6f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("offsetX", 0.0f, -100.0f, 100.0f, "Offset");
        addValueWithPropertiesCat("offsetY", 0.0f, -100.0f, 100.0f, "Offset");
        addValueWithPropertiesCat("rotation", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("twist", 0.0f, -2.0f, 2.0f, "Transform");
        addValueWithPropertiesCat("stretchX", 1.0f, 0.1f, 3.0f, "Transform");
        addValueWithPropertiesCat("stretchY", 1.0f, 0.1f, 3.0f, "Transform");
        addValueWithPropertiesCat("blendOriginal", 1.0f, 0.0f, 1.0f, "Blend");
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
