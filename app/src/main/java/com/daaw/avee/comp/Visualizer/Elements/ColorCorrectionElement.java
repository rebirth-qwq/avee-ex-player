package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class ColorCorrectionElement extends DummyElement {
    public static final String typeName = "ColorCorrection";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_brightness;\nuniform float u_contrast;\nuniform float u_hue;\nuniform float u_saturation;\nuniform float u_vibrance;\nuniform float u_redGain;\nuniform float u_greenGain;\nuniform float u_blueGain;\nuniform float u_redLift;\nuniform float u_greenLift;\nuniform float u_blueLift;\nuniform float u_redLow;\nuniform float u_greenLow;\nuniform float u_blueLow;\nuniform float u_redHigh;\nuniform float u_greenHigh;\nuniform float u_blueHigh;\nuniform float u_lumaLow;\nuniform float u_lumaHigh;\nuniform float u_lumaOutLow;\nuniform float u_lumaOutHigh;\nuniform float u_gamma;\nuniform float u_shadows;\nuniform float u_highlights;\nuniform float u_invert;\nuniform float u_sharpen;\nuniform float u_invertMode;\n\nvec3 rgb2hsv(vec3 c) {\n    vec4 K = vec4(0.0, -1.0/3.0, 2.0/3.0, -1.0);\n    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n    float d = q.x - min(q.w, q.y);\n    float e = 1.0e-10;\n    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\n\nvec3 hsv2rgb(vec3 c) {\n    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);\n    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\n\nfloat gammaCorrect(float v, float g) {\n    return pow(max(v, 0.0), 1.0 / max(g, 0.001));\n}\n\nfloat liftGain(float v, float lift, float gain) {\n    return v * gain + lift;\n}\n\nfloat colorMap(float v, float low, float high) {\n    return clamp((v - low) / max(high - low, 0.001), 0.0, 1.0);\n}\n\nfloat lumaMap(float v, float inLow, float inHigh, float outLow, float outHigh) {\n    float t = clamp((v - inLow) / max(inHigh - inLow, 0.001), 0.0, 1.0);\n    return outLow + t * (outHigh - outLow);\n}\n\nvoid main() {\n    vec4 pixel = texture2D(u_texture, vTexCoord);\n    vec3 color = pixel.rgb;\n\n    if (pixel.a == 0.0) {\n        gl_FragColor = pixel;\n        return;\n    }\n\n    float sharpen = u_sharpen;\n    if (sharpen > 0.001) {\n        float step = 0.002;\n        vec3 cU = texture2D(u_texture, vTexCoord + vec2(0.0, step)).rgb;\n        vec3 cD = texture2D(u_texture, vTexCoord - vec2(0.0, step)).rgb;\n        vec3 cL = texture2D(u_texture, vTexCoord - vec2(step, 0.0)).rgb;\n        vec3 cR = texture2D(u_texture, vTexCoord + vec2(step, 0.0)).rgb;\n        vec3 diff = 4.0 * color - cU - cD - cL - cR;\n        color = color + sharpen * diff;\n        color = clamp(color, 0.0, 1.0);\n    }\n\n    color.r = liftGain(color.r, u_redLift, u_redGain);\n    color.g = liftGain(color.g, u_greenLift, u_greenGain);\n    color.b = liftGain(color.b, u_blueLift, u_blueGain);\n\n    float luma = dot(color, vec3(0.299, 0.587, 0.114));\n    float mappedLuma = lumaMap(luma, u_lumaLow, u_lumaHigh, u_lumaOutLow, u_lumaOutHigh);\n    float lumaScale = mappedLuma / max(luma, 0.001);\n    color = color * lumaScale;\n    color = clamp(color, 0.0, 1.0);\n\n    vec3 hsv = rgb2hsv(color);\n    hsv.x += u_hue;\n    hsv.y *= u_saturation;\n    hsv.z += u_brightness - 1.0;\n    color = hsv2rgb(clamp(hsv, 0.0, 1.0));\n\n    color = clamp((color - 0.5) * u_contrast + 0.5, 0.0, 1.0);\n\n    color.r = gammaCorrect(color.r, u_gamma);\n    color.g = gammaCorrect(color.g, u_gamma);\n    color.b = gammaCorrect(color.b, u_gamma);\n\n    color.r = colorMap(color.r, u_redLow, u_redHigh);\n    color.g = colorMap(color.g, u_greenLow, u_greenHigh);\n    color.b = colorMap(color.b, u_blueLow, u_blueHigh);\n\n    luma = dot(color, vec3(0.299, 0.587, 0.114));\n    float shadow = clamp((pow(luma, 1.0 / (u_shadows + 1.0)) - luma), 0.0, 1.0);\n    float highlight = clamp((1.0 - pow(1.0 - luma, 1.0 / (2.0 - u_highlights)) - luma), -1.0, 0.0);\n    color = luma + shadow + highlight > 0.001 ? (luma + shadow + highlight) * (color / vec3(luma + 0.001)) : vec3(0.0);\n\n    float avg = dot(color, vec3(0.3333));\n    float maxC = max(color.r, max(color.g, color.b));\n    float vibrAmt = (maxC - avg) * (-u_vibrance * 0.5);\n    color = mix(color, vec3(maxC), clamp(vibrAmt, 0.0, 1.0));\n\n    if (u_invert > 0.5) {\n        float invertMode = u_invertMode;\n        if (invertMode < 0.5) {\n            color = 1.0 - color;\n        } else {\n            vec3 hsvColor = rgb2hsv(color);\n            float hueRot = hsvColor.x + 0.5;\n            if (hueRot > 1.0) hueRot = hueRot - 1.0;\n            hsvColor.x = hueRot;\n            color = hsv2rgb(hsvColor);\n        }\n    }\n\n    gl_FragColor = vec4(clamp(color, 0.0, 1.0), pixel.a);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("brightness", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("contrast", 1.0f, 0.0f, 3.0f, "Color");
        addValueWithPropertiesCat("hue", 0.0f, -1.0f, 1.0f, "Color");
        addValueWithPropertiesCat("saturation", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("vibrance", 0.0f, -2.0f, 2.0f, "Color");
        addValueWithPropertiesCat("redGain", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("greenGain", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("blueGain", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("redLift", 0.0f, -0.5f, 0.5f, "Color");
        addValueWithPropertiesCat("greenLift", 0.0f, -0.5f, 0.5f, "Color");
        addValueWithPropertiesCat("blueLift", 0.0f, -0.5f, 0.5f, "Color");
        addValueWithPropertiesCat("gamma", 1.0f, 0.1f, 3.0f, "Color");
        addValueWithPropertiesCat("shadows", 0.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("highlights", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("invert", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("invertMode", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("sharpen", 0.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("lumaLow", 0.0f, 0.0f, 1.0f, "LumaLevels");
        addValueWithPropertiesCat("lumaHigh", 1.0f, 0.0f, 1.0f, "LumaLevels");
        addValueWithPropertiesCat("lumaOutLow", 0.0f, 0.0f, 1.0f, "LumaLevels");
        addValueWithPropertiesCat("lumaOutHigh", 1.0f, 0.0f, 1.0f, "LumaLevels");
        addValueWithPropertiesCat("redLow", 0.0f, 0.0f, 1.0f, "ChannelLevels");
        addValueWithPropertiesCat("greenLow", 0.0f, 0.0f, 1.0f, "ChannelLevels");
        addValueWithPropertiesCat("blueLow", 0.0f, 0.0f, 1.0f, "ChannelLevels");
        addValueWithPropertiesCat("redHigh", 1.0f, 0.0f, 1.0f, "ChannelLevels");
        addValueWithPropertiesCat("greenHigh", 1.0f, 0.0f, 1.0f, "ChannelLevels");
        addValueWithPropertiesCat("blueHigh", 1.0f, 0.0f, 1.0f, "ChannelLevels");
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_color_correction);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
