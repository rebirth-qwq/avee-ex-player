package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class PixelEffectElement extends DummyElement {
    public static final String typeName = "PixelEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\n// Cubiq - Pixel shader\n\nuniform sampler2D u_texture;\nuniform float u_amount;\nuniform float u_aspectRatio;\nuniform vec2 resolution;\nvarying vec2 vTexCoord;\n  \n  void main() {\n    vec2 resolution = vec2(u_aspectRatio* 2., 1.);\n    vec2 coordinates = vTexCoord;\n    float amount = u_amount/10.;\n    vec2 pixelSize = vec2(amount/resolution.x, amount/resolution.y);\n    vec2 position = (pixelSize.x == 0.) ? vTexCoord : floor(coordinates/pixelSize)*pixelSize;\n\n    vec4 finalColor = texture2D(u_texture, position);\n    gl_FragColor = finalColor;\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("amount", 0.5f, 0.0f, 1.0f);
        addValueWithProperties("aspectRatio", 0.5f, 0.0f, 2.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_pixel_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
