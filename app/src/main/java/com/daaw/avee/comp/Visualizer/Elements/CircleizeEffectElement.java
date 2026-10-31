package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentPathCircle;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CircleizeEffectElement extends DummyElement {
    public static final String typeName = "Circleize";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_circleize);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_scale;\nuniform float u_fill;\nuniform float u_brightness;\nuniform float u_blend;\nuniform float u_scaleCompensation;\n\nvoid main() {\n    float cellSize = 0.015 * pow(0.2 / 0.015, (u_scale + 1.0) * 0.5);\n    float fill = u_fill;\n    float shapeFactor = smoothstep(0.0, 1.0, fill);\n    float sizeFactor = (fill < 0.0) ? (1.0 + fill) : 1.0;\n    float brightness = u_brightness + 1.0;\n    float blend = clamp(u_blend + 1.0, 0.0, 1.0);\n    float aspectComp = 1.0 + u_scaleCompensation * 0.5;\n    vec2 cellSizeVec = vec2(cellSize * aspectComp, cellSize);\n    vec2 cellCoord = vTexCoord / cellSizeVec;\n    vec2 gridIndex = floor(cellCoord);\n    vec2 localCoord = fract(cellCoord) - 0.5;\n    vec2 dxy = localCoord * 2.0;\n    float dCircle = length(dxy);\n    float dSquare = max(abs(dxy.x), abs(dxy.y));\n    float dist = mix(dCircle, dSquare, shapeFactor);\n    float dotAlpha = smoothstep(sizeFactor, sizeFactor - 0.03, dist);\n    vec2 centerUV = (gridIndex + 0.5) * cellSizeVec;\n    vec3 originalRGB = texture2D(u_texture, centerUV).rgb;\n    vec3 effectColor = originalRGB * brightness * dotAlpha;\n    vec3 fullOriginal = texture2D(u_texture, vTexCoord).rgb;\n    vec3 finalColor = mix(fullOriginal, effectColor, blend);\n    gl_FragColor = vec4(finalColor, 1.0);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("scale", 0.5f, -1.0f, 1.0f, SegmentPathCircle.typeName);
        addValueWithPropertiesCat("fill", 0.0f, -1.0f, 1.0f, SegmentPathCircle.typeName);
        addValueWithPropertiesCat("brightness", 0.0f, -1.0f, 1.0f, SegmentPathCircle.typeName);
        addValueWithPropertiesCat("blend", 0.5f, -1.0f, 1.0f, SegmentPathCircle.typeName);
        addValueWithPropertiesCat("scaleCompensation", 0.0f, -1.0f, 1.0f, SegmentPathCircle.typeName);
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
