package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class RainDropEffectElement extends DummyElement {
    public static final String typeName = "RainDrop";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_rain_drop);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_evolution;\nuniform float u_size;\nuniform float u_distortion;\nuniform float u_blur;\nuniform float u_intensity;\n\nfloat N21(vec2 p) {\n    p = fract(p * vec2(123.34, 345.45));\n    p += dot(p, p + 34.345);\n    return fract(p.x + p.y);\n}\n\nvec3 dropLayer(vec2 UV, float T) {\n    float t = abs(u_evolution) * 100.0 + T;\n    float aspect = 2.0;\n    vec2 uv = UV * u_size * aspect;\n    uv.y += t * 0.25;\n    vec2 gv = fract(uv) - 0.5;\n    vec2 id = floor(uv);\n    float n = N21(id);\n    t += n * 6.2831;\n    float w = UV.y * 10.0;\n    float x = (n - 0.5) * 0.8;\n    x += (0.4 - abs(x)) * sin(3.0 * w) * pow(abs(sin(w)), 6.0) * 0.45;\n    float y = -sin(t + sin(t + sin(t) * 0.5)) * 0.45;\n    y -= (gv.x - x) * (gv.x - x);\n    vec2 dropPos = (gv - vec2(x, y)) / aspect;\n    float drop = smoothstep(0.05, 0.03, length(dropPos));\n    vec2 trailPos = (gv - vec2(x, t * 0.25)) / aspect;\n    trailPos.y = (fract(trailPos.y * 8.0) - 0.5) / 8.0;\n    float trail = smoothstep(0.03, 0.01, length(trailPos));\n    float fogTrail = smoothstep(-0.05, 0.05, dropPos.y);\n    fogTrail *= smoothstep(0.5, y, gv.y);\n    fogTrail *= smoothstep(0.05, 0.04, abs(dropPos.x));\n    trail *= fogTrail;\n    vec2 offset = drop * dropPos + trail * trailPos;\n    return vec3(offset, fogTrail);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec3 drops = dropLayer(uv, 0.0);\n    drops += dropLayer(uv * 1.25 + 7.52, 0.0);\n    drops += dropLayer(uv * 1.35 + 1.54, 0.0);\n    drops += dropLayer(uv * 1.57 - 7.52, 0.0);\n    vec2 offset = drops.xy * u_distortion * u_intensity;\n    float blurAmount = u_blur * 0.01;\n    vec4 color = texture2D(u_texture, uv + offset);\n    if (u_blur > 0.01) {\n        vec4 blurColor = vec4(0.0);\n        float totalWeight = 0.0;\n        for (int i = -3; i <= 3; i++) {\n            for (int j = -3; j <= 3; j++) {\n                float fi = float(i);\n                float fj = float(j);\n                float weight = exp(-(fi * fi + fj * fj) / (2.0 * 2.0 * 2.0));\n                vec2 sampleUV = uv + offset + vec2(fi, fj) * blurAmount;\n                blurColor += texture2D(u_texture, sampleUV) * weight;\n                totalWeight += weight;\n            }\n        }\n        blurColor /= totalWeight;\n        float blend = smoothstep(0.0, 0.5, drops.z);\n        color = mix(color, blurColor, blend * u_blur);\n    }\n    gl_FragColor = color;\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("evolution", 0.0f, -1.0f, 1.0f, "Timing");
        addValueWithPropertiesCat("size", 4.0f, 0.0f, 15.0f, "Rain");
        addValueWithPropertiesCat("distortion", -0.5f, -5.0f, 0.0f, "Rain");
        addValueWithPropertiesCat("blur", 0.2f, 0.0f, 1.0f, "Rain");
        addValueWithPropertiesCat("intensity", 1.0f, 0.0f, 3.0f, "Rain");
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
