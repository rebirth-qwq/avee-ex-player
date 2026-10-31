package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class RandomLinesEffectElement extends DummyElement {
    public static final String typeName = "RandomLines";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_random_lines);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_count;\nuniform float u_length;\nuniform float u_width;\nuniform float u_lengthRandom;\nuniform float u_widthRandom;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_spread;\nuniform float u_angleSpread;\nuniform float u_colorR;\nuniform float u_colorG;\nuniform float u_colorB;\nuniform float u_opacity;\nuniform float u_speed;\nuniform float u_fadeEdges;\n\nfloat hash(vec2 p) {\n    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    int count = int(u_count);\n    float lineLength = u_length;\n    float lineWidth = u_width;\n    float lengthRand = u_lengthRandom;\n    float widthRand = u_widthRandom;\n    vec2 center = vec2(u_centerX, u_centerY);\n    float spread = u_spread;\n    float angleSpread = u_angleSpread * 3.14159;\n    vec3 lineColor = vec3(u_colorR, u_colorG, u_colorB);\n    float opacity = u_opacity;\n    float speed = u_speed;\n    float fadeEdges = u_fadeEdges;\n    float lineMask = 0.0;\n\n    for (int i = 0; i < 1000; i++) {\n        if (i >= count) break;\n        float fi = float(i);\n\n        vec2 seed = vec2(fi * 1.7 + 0.3, fi * 2.3 + 1.7);\n        float randX = hash(seed);\n        float randY = hash(seed + vec2(100.0, 50.0));\n        float randLen = hash(seed + vec2(200.0, 150.0));\n        float randWid = hash(seed + vec2(300.0, 250.0));\n        float randAng = hash(seed + vec2(400.0, 350.0));\n        float randLife = hash(seed + vec2(500.0, 450.0));\n\n        vec2 pos = vec2(\n            center.x + (randX - 0.5) * spread,\n            center.y + (randY - 0.5) * spread\n        );\n\n        float life = fract(speed * 0.2 + randLife);\n        float lifeMask = 1.0 - smoothstep(0.0, 0.3, life) * (1.0 - smoothstep(0.7, 1.0, life));\n\n        float len = lineLength + (randLen - 0.5) * lengthRand * 2.0;\n        len = max(len, 0.001);\n        float wid = lineWidth + (randWid - 0.5) * widthRand * 2.0;\n        wid = max(wid, 0.001);\n\n        float angle = randAng * angleSpread * 2.0 - angleSpread;\n        vec2 dir = vec2(cos(angle), sin(angle));\n        vec2 perp = vec2(-dir.y, dir.x);\n\n        vec2 diff = uv - pos;\n        float proj = dot(diff, dir);\n        float distPerp = abs(dot(diff, perp));\n        float halfLen = len * 0.5;\n\n        float edgeFade = 0.005 + fadeEdges * 0.05;\n        float alongMask = 1.0;\n        if (proj < -halfLen || proj > halfLen) {\n            alongMask = 0.0;\n        } else {\n            float distToEnd = min(proj + halfLen, halfLen - proj);\n            alongMask = smoothstep(0.0, edgeFade, distToEnd);\n        }\n\n        float widthMask = 1.0 - smoothstep(0.0, wid, distPerp);\n\n        float currentMask = alongMask * widthMask * lifeMask;\n        lineMask = max(lineMask, currentMask);\n    }\n\n    lineMask = clamp(lineMask, 0.0, 1.0);\n    vec3 finalColor = mix(vec3(0.0), lineColor, lineMask * opacity);\n    float finalAlpha = lineMask * opacity;\n    gl_FragColor = vec4(finalColor, finalAlpha);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("count", 50.0f, 1.0f, 500.0f, "Lines");
        addValueWithPropertiesCat("length", 2.0f, 0.01f, 2.0f, "Lines");
        addValueWithPropertiesCat("width", 0.008f, 0.001f, 0.05f, "Lines");
        addValueWithPropertiesCat("lengthRandom", 0.0f, 0.0f, 1.0f, "Lines");
        addValueWithPropertiesCat("widthRandom", 0.05f, 0.0f, 0.5f, "Lines");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Position");
        addValueWithPropertiesCat("spread", 0.9f, 0.0f, 2.0f, "Position");
        addValueWithPropertiesCat("angleSpread", 1.0f, 0.0f, 2.0f, "Position");
        addValueWithPropertiesCat("colorR", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("colorG", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("colorB", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("opacity", 0.9f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("speed", 0.3f, -5.0f, 5.0f, "Motion");
        addValueWithPropertiesCat("fadeEdges", 0.5f, 0.0f, 1.0f, "Motion");
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
