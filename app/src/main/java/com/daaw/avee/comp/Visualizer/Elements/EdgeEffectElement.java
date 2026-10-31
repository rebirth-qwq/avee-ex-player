package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class EdgeEffectElement extends DummyElement {
    public static final String typeName = "EdgeEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "// Cubiqued - https://www.shadertoy.com/view/td2yDm\nprecision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nuniform float u_amount;\nuniform sampler2D u_texture;\n\nvarying vec2 vTexCoord;\n\nvoid main()\n{\n    // I suggest playing around with this value\n    float h = u_amount / 200.;\n    \n    vec4 o = texture2D(u_texture, vTexCoord);\n    vec4 n = texture2D(u_texture, vTexCoord + vec2(0, h));\n    vec4 e = texture2D(u_texture, vTexCoord + vec2(h, 0));\n    vec4 s = texture2D(u_texture, vTexCoord - vec2(0, h));\n    vec4 w = texture2D(u_texture, vTexCoord - vec2(h, 0));\n    \n    vec4 dy = (n - s) * 0.5;\n    vec4 dx = (e - w) * 0.5;\n    \n    vec4 edge = sqrt(dx * dx + dy * dy);\n    vec4 angle = atan(dy, dx);\n   \n    gl_FragColor = vec4(edge * 5.0); \n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("amount", 0.5f, 0.0f, 1.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_edge_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
