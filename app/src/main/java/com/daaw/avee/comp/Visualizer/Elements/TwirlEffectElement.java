package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class TwirlEffectElement extends DummyElement {
    public static final String typeName = "TwirlEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\nuniform sampler2D u_texture;\nuniform float u_amount;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_radius;\nuniform float u_aspectRatio;\n\nvarying vec2 vTexCoord;\n\nvoid main() {\n\n     // Parameters for the effect\n    float effectRadius = u_radius;\n    float effectAngle = u_amount;\n\n    vec2 u_center = vec2(u_centerX, u_centerY);\n\n    vec2 center = (u_center + 1.0)/2.0;\n    //center = (center == vec2(0.0, 0.0)) ? vec2(0.5, 0.5) : center;\n\n    // vec2 iResolution = vec2(900.0, 1600.0);\n    // Calculate the normalized UV coordinates\n     vec2 uv = vTexCoord.xy - center;\n\n    // Calculate the length of the UV vector\n    float len = length(uv * vec2(u_aspectRatio, 1.));\n\n    // Calculate the angle for the effect\n    float angle = atan(uv.y, uv.x) + effectAngle * smoothstep(effectRadius, 0.0, len);\n\n    // Calculate the radius from the center\n    float radius = length(uv);\n    \n    vec2 newUV = vec2(radius * cos(angle), radius * sin(angle)) + center;\n\n    // Sample the texture with the modified coordinates\n    gl_FragColor = texture2D(u_texture, newUV);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("amount", 2.0f, -10.0f, 10.0f);
        addValueWithProperties("radius", 0.5f, 0.0f, 1.0f);
        addValueWithProperties("centerX", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("centerY", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("aspectRatio", 0.5f, 0.0f, 2.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_twirl_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
