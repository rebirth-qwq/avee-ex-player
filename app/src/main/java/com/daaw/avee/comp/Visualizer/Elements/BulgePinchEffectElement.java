package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class BulgePinchEffectElement extends DummyElement {
    public static final String typeName = "BulgePinchEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "\n// BulgePinch - Cubified xd \n// original from pixiJS : https://github.com/pixijs/filters/blob/main/filters/bulge-pinch/src/bulgePinch.frag\n\n\nprecision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\n\n\nuniform float u_radius;\nuniform float u_strength;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_aspectRatio;\n\nuniform sampler2D u_texture;\nvarying vec2 vTexCoord;\n\n\nvoid main()\n{\n    vec2 center = (vec2(u_centerX,u_centerY) + 1.0)/2.0;\n    float strength = u_strength * 2.;\n    vec2 uv = vTexCoord.xy - center;\n    float distance = length(uv* vec2(u_aspectRatio, 1.));\n    if (distance < u_radius) {\n        float percent = distance / u_radius;\n        if (strength > 0.0) {\n            uv *= mix(1.0, smoothstep(0.0, u_radius / distance, percent), strength * 0.75);\n        } else {\n            uv *= mix(1.0, pow(percent, 1.0 + strength * 0.75) * u_radius / distance, 1.0 - percent);\n        }\n    }\n    uv += center;\n    vec2 clampedCoord = clamp(uv, 0.0, 1.0);\n    vec4 color = texture2D(u_texture, clampedCoord);\n    if (uv != clampedCoord) {\n        color *= max(0.0, 1.0 - length(uv - clampedCoord));\n    }\n\n    gl_FragColor = color;\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("radius", 0.2f, 0.0f, 1.0f);
        addValueWithProperties("strength", 0.5f, -1.0f, 1.0f);
        addValueWithProperties("centerX", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("centerY", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("aspectRatio", 2.0f, 0.0f, 4.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_bulge_pinch_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
