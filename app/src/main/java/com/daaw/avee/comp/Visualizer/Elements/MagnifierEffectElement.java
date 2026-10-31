package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class MagnifierEffectElement extends DummyElement {
    public static final String typeName = "Magnifier";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_magnifier);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_shape;\nuniform float u_zoom;\nuniform float u_scale;\nuniform float u_aspect;\nuniform float u_radius;\nuniform float u_cornerRadius;\nuniform float u_triRound;\nuniform float u_triCurve;\nuniform float u_edgeWidth;\nuniform float u_edgeSoftness;\nuniform float u_edgeGlow;\nuniform float u_edgeDistort;\nuniform float u_chromatic;\nuniform float u_reflection;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_rotation;\n\nfloat sdCircle(vec2 p, float r) { return length(p) - r; }\nfloat sdEllipse(vec2 p, vec2 r) { float k1 = length(p / r); return (k1 - 1.0) * min(r.x, r.y); }\nfloat sdRoundedBox(vec2 p, vec2 b, float r) { vec2 q = abs(p) - b + r; return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r; }\nfloat sdTriangleBase(vec2 p, float R) { float r = R * 0.8660254; const float k = 1.7320508; p.x = abs(p.x); float d1 = (p.x + k * p.y) * 0.5; float d2 = (-p.x + k * p.y) * 0.5; float d3 = p.y + r; return max(max(d1, d2), -d3) - r; }\nfloat sdRoundTriangle(vec2 p, float R, float round) { float d = sdTriangleBase(p, R + round); return d - round; }\nfloat sdCurvedTriangle(vec2 p, float R, float round, float curve) { float d_tri = sdRoundTriangle(p, R, round); float d_circ = length(p) - R; return mix(d_tri, d_circ, curve); }\n\nvoid main() {\n    vec2 uv = vTexCoord - vec2(u_centerX, u_centerY);\n    vec2 p = uv;\n    float rot = u_rotation;\n    float c = cos(rot);\n    float s = sin(rot);\n    p = vec2(p.x * c - p.y * s, p.x * s + p.y * c);\n    float d = 0.0;\n    float shapeScale = 1.0;\n    float radius = u_radius * u_scale;\n    float aspect = u_aspect;\n    \n    if (u_shape < 0.5) {\n        d = sdEllipse(p, vec2(radius, radius / aspect));\n        shapeScale = radius;\n    } else if (u_shape < 1.5) {\n        vec2 rectSize = vec2(radius * aspect, radius);\n        d = sdRoundedBox(p, rectSize, u_cornerRadius);\n        shapeScale = radius;\n    } else {\n        if (radius <= 0.0) { d = 1e10; } else {\n            vec2 triScale = vec2(1.0, 1.0 / aspect);\n            vec2 tp = p * triScale;\n            d = sdCurvedTriangle(tp, radius, u_triRound, u_triCurve) / length(triScale);\n        }\n        shapeScale = radius;\n    }\n    \n    float edgeWidth = u_edgeWidth * 0.05 + 0.005;\n    float edgeSoftness = u_edgeSoftness * 0.02 + 0.001;\n    float innerMask = 1.0 - smoothstep(-edgeWidth, edgeSoftness, d);\n    float edgeFactor = smoothstep(-edgeWidth - edgeSoftness, -edgeWidth * 0.5, d) * smoothstep(edgeSoftness, -edgeWidth * 0.5, d);\n    float invZoom = 1.0 / u_zoom;\n    vec2 zoomedUV = uv * invZoom;\n    vec2 normalDir = vec2(0.0);\n    if (length(p) > 0.001) { normalDir = normalize(p); }\n    float distortAmount = edgeFactor * u_edgeDistort;\n    zoomedUV += normalDir * distortAmount * 0.08 * shapeScale;\n    vec2 finalUV = mix(uv, zoomedUV, innerMask);\n    vec2 texUV = finalUV + vec2(u_centerX, u_centerY);\n    vec2 clampedUV = clamp(texUV, 0.001, 0.999);\n    float caStrength = (1.0 - smoothstep(-0.1, 0.0, d)) * u_chromatic * 0.03;\n    vec2 caOffset = normalDir * caStrength;\n    float rChannel = texture2D(u_texture, clampedUV + caOffset).r;\n    float gChannel = texture2D(u_texture, clampedUV).g;\n    float bChannel = texture2D(u_texture, clampedUV - caOffset).b;\n    vec4 finalColor = vec4(rChannel, gChannel, bChannel, 1.0);\n    float outerMask = smoothstep(0.0, edgeSoftness, d);\n    vec4 originalColor = texture2D(u_texture, vTexCoord);\n    finalColor = mix(finalColor, originalColor, outerMask);\n    float glowEdge = smoothstep(-edgeWidth - edgeSoftness, -edgeWidth * 0.3, d) * smoothstep(edgeSoftness * 0.5, -edgeWidth * 0.3, d);\n    vec3 glowColor = vec3(0.6, 0.75, 1.0) * glowEdge * u_edgeGlow * 0.6;\n    finalColor.rgb += glowColor;\n    float fresnel = 1.0 - abs(dot(normalDir, vec2(0.0, 1.0)));\n    float reflection = fresnel * innerMask * u_reflection * 0.3;\n    float radialDist = length(p) / (shapeScale + 0.001);\n    float highlight = pow(max(0.0, 1.0 - radialDist), 4.0);\n    highlight *= u_reflection * 0.2;\n    finalColor.rgb += vec3(1.0, 1.0, 1.0) * (reflection + highlight);\n    float vignette = 1.0 - (1.0 - innerMask) * 0.25;\n    finalColor.rgb *= vignette;\n    gl_FragColor = finalColor;\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("shape", 0.0f, 0.0f, 2.0f, "Shape");
        addValueWithPropertiesCat("zoom", 2.0f, 0.2f, 5.0f, "Zoom");
        addValueWithPropertiesCat("scale", 1.0f, 0.1f, 2.0f, "Shape");
        addValueWithPropertiesCat("aspect", 1.0f, 0.1f, 3.0f, "Shape");
        addValueWithPropertiesCat("radius", 0.3f, 0.05f, 0.8f, "Shape");
        addValueWithPropertiesCat("cornerRadius", 0.05f, 0.0f, 0.2f, "Shape");
        addValueWithPropertiesCat("triRound", 0.02f, 0.0f, 0.3f, "Shape");
        addValueWithPropertiesCat("triCurve", 0.0f, 0.0f, 1.0f, "Shape");
        addValueWithPropertiesCat("edgeWidth", 0.05f, 0.05f, 5.0f, "Edge");
        addValueWithPropertiesCat("edgeSoftness", 0.5f, 0.0f, 2.0f, "Edge");
        addValueWithPropertiesCat("edgeGlow", 0.4f, 0.0f, 1.0f, "Optics");
        addValueWithPropertiesCat("edgeDistort", 0.2f, 0.0f, 0.5f, "Optics");
        addValueWithPropertiesCat("chromatic", 0.03f, 0.0f, 0.1f, "Optics");
        addValueWithPropertiesCat("reflection", 0.2f, 0.0f, 0.5f, "Optics");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("rotation", 0.0f, 0.0f, 6.28318f, "Position");
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
