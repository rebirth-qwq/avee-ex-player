package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CurveEffectElement extends DummyElement {
    public static final String typeName = "CurveEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "// Cubiqued, https://www.shadertoy.com/view/4ttXRH\nprecision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nuniform sampler2D u_texture;\n\nvarying vec2 vTexCoord;\n\n#define PI acos(-1.)\n\nuniform float u_innerRadius;\nuniform float u_outerRadius;\nuniform float u_aspectRatio;\n\n\nvoid main()\n{   \n    vec2 uv = vTexCoord * 2.0 - 1.0;\n    uv.x *= u_aspectRatio;\n    uv.y *= -1.;\n    \n    float theta = atan( uv.y, uv.x );\n    float dist = length( uv );\n    \n    //Make center circle and edge black\n   \tfloat a = smoothstep( u_innerRadius - 0.001, u_innerRadius + 0.001, dist );\n    a *= 1.0 - smoothstep( u_outerRadius - 0.001, u_outerRadius + 0.001, dist );\n    \n    uv = vec2((theta + PI) / (PI * 2.0),\n    \t\t  (dist - u_innerRadius) / (u_outerRadius - u_innerRadius)\n             );\n   \n\tgl_FragColor = texture2D(u_texture , uv) * a;\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("innerRadius", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("outerRadius", 1.0f, 0.0f, 3.0f);
        addValueWithProperties("aspectRatio", 2.0f, 0.0f, 4.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_curve_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
