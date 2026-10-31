package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class HalftoneEffectElement extends DummyElement {
    public static final String typeName = "Halftone";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_halftone);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_size;\nuniform float u_angle;\nuniform float u_sharpness;\nuniform float u_intensity;\nuniform float u_cyan;\nuniform float u_magenta;\nuniform float u_yellow;\nuniform float u_black;\nuniform float u_colorize;\nuniform float u_blendOriginal;\n\nfloat safePow(float base, float exp) {\n    return pow(max(base, 0.0001), exp);\n}\n\nfloat dotDistance(vec2 uv, vec2 gridSize, float angle) {\n    float cosA = cos(angle);\n    float sinA = sin(angle);\n    vec2 rotUV = vec2(uv.x * cosA - uv.y * sinA, uv.x * sinA + uv.y * cosA);\n    vec2 grid = floor(rotUV / gridSize) * gridSize + gridSize * 0.5;\n    vec2 delta = rotUV - grid;\n    return length(delta);\n}\n\nvec4 rgbToCmyk(vec3 rgb) {\n    float k = 1.0 - max(max(rgb.r, rgb.g), rgb.b);\n    float invK = 1.0 / (1.0 - k + 0.001);\n    float c = (1.0 - rgb.r - k) * invK;\n    float m = (1.0 - rgb.g - k) * invK;\n    float y = (1.0 - rgb.b - k) * invK;\n    return vec4(clamp(c, 0.0, 1.0), clamp(m, 0.0, 1.0), clamp(y, 0.0, 1.0), k);\n}\n\nvec3 cmykToRgb(vec4 cmyk) {\n    vec3 cmy = clamp(cmyk.xyz, 0.0, 1.0);\n    float k = clamp(cmyk.w, 0.0, 1.0);\n    vec3 rgb = (1.0 - cmy) * (1.0 - k);\n    return clamp(rgb, 0.0, 1.0);\n}\n\nfloat calculateDot(float channelValue, float strength, float maxRadius, \n                   float dist, float sharpness, float dotSize) {\n    if (channelValue < 0.001 || strength < 0.001) return 0.0;\n    float effectiveValue = clamp(channelValue * strength, 0.0, 1.0);\n    float radius = maxRadius * safePow(effectiveValue, 1.0 / sharpness);\n    float smoothness = max(0.001, dotSize * 0.05);\n    return 1.0 - smoothstep(radius - smoothness, radius + smoothness, dist);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec4 color = texture2D(u_texture, uv);\n    vec4 cmyk = rgbToCmyk(color.rgb);\n    float dotSize = mix(0.008, 0.15, u_size / 20.0);\n    float maxRadius = dotSize * 0.7071;\n    float sharpness = max(0.5, u_sharpness * 2.0);\n    float baseAngle = u_angle * 6.28318;\n    float angleC = baseAngle + 0.0;\n    float angleM = baseAngle + 0.5236;\n    float angleY = baseAngle + 1.0472;\n    float angleK = baseAngle + 1.5708;\n    float cStrength = clamp(u_cyan, 0.0, 2.0);\n    float mStrength = clamp(u_magenta, 0.0, 2.0);\n    float yStrength = clamp(u_yellow, 0.0, 2.0);\n    float kStrength = clamp(u_black, 0.0, 2.0);\n    float distC = dotDistance(uv, vec2(dotSize), angleC);\n    float distM = dotDistance(uv, vec2(dotSize), angleM);\n    float distY = dotDistance(uv, vec2(dotSize), angleY);\n    float distK = dotDistance(uv, vec2(dotSize), angleK);\n    float maskC = calculateDot(cmyk.x, cStrength, maxRadius, distC, sharpness, dotSize);\n    float maskM = calculateDot(cmyk.y, mStrength, maxRadius, distM, sharpness, dotSize);\n    float maskY = calculateDot(cmyk.z, yStrength, maxRadius, distY, sharpness, dotSize);\n    float maskK = calculateDot(cmyk.w, kStrength, maxRadius, distK, sharpness, dotSize);\n    vec3 halftoneColor;\n    if (u_colorize > 0.5) {\n        vec4 cmykOut = vec4(maskC * cmyk.x * cStrength, maskM * cmyk.y * mStrength, maskY * cmyk.z * yStrength, maskK * cmyk.w * kStrength);\n        halftoneColor = cmykToRgb(cmykOut);\n    } else {\n        float gray = maskK * cmyk.w * kStrength;\n        halftoneColor = vec3(gray);\n    }\n    halftoneColor *= u_intensity;\n    vec4 result = vec4(halftoneColor, color.a);\n    result = mix(result, color, u_blendOriginal);\n    gl_FragColor = clamp(result, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("size", 5.0f, 0.5f, 20.0f, typeName);
        addValueWithPropertiesCat("angle", 0.0f, -1.0f, 1.0f, typeName);
        addValueWithPropertiesCat("sharpness", 1.0f, 0.1f, 3.0f, typeName);
        addValueWithPropertiesCat("intensity", 1.0f, 0.0f, 2.0f, typeName);
        addValueWithPropertiesCat("cyan", 1.0f, 0.0f, 2.0f, "CMYK");
        addValueWithPropertiesCat("magenta", 1.0f, 0.0f, 2.0f, "CMYK");
        addValueWithPropertiesCat("yellow", 1.0f, 0.0f, 2.0f, "CMYK");
        addValueWithPropertiesCat("black", 1.0f, 0.0f, 2.0f, "CMYK");
        addValueWithPropertiesCat("colorize", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("blendOriginal", 0.0f, 0.0f, 1.0f, "Blend");
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
