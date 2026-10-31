package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class Kaleidoscope extends DummyElement {
    public static final String typeName = "Kaleidoscope";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\nuniform float u_segments;\nuniform float u_offset;\n\nconst float PI =3.14159265359;\nconst float TAU = 2.0 * PI;\n\nvoid main() {\n   if (u_segments == 0.0) {\n      gl_FragColor = texture2D(u_texture, vTexCoord);\n   } else { \n      vec2 centered = vTexCoord - 0.5;\n      // to pol\n      float r = length(centered);\n      float theta = atan(centered.y, centered.x);\n      theta = mod(theta, TAU / u_segments);\n      theta = abs(theta - PI / u_segments);\n      // back to cartesi\n      vec2 newCoords = r * vec2(cos(theta), sin(theta)) + 0.5;\n      gl_FragColor = texture2D(u_texture, mod(newCoords - u_offset, 1.0));\n   }\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("segments", 3.0f, 1.0f, 10.0f);
        addValueWithProperties("offset", 0.0f, -1.0f, 1.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_kaleidoscope);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
