package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class FOVElement extends DummyElement {
    public static final String typeName = "FOV";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_scale;\nuniform float u_curvature;\nuniform float u_aspectRatio;\nuniform float u_repeatTexture;\nuniform float u_centerX;\nuniform float u_centerY;\n\n#define REPEAT_TEXTURE !floatToBool(u_repeatTexture)\n\nbool floatToBool(float f) { return f > 0.5; }\n\nvec2 GLCoord2TextureCoord(vec2 glCoord) {\n    return glCoord * vec2(1.0, -1.0) / 2.0 + vec2(0.5);\n}\n\nvoid main() {\n    vec2 center = vec2(u_centerX, u_centerY);\n    vec2 vPosition = (vTexCoord - 0.5);\n    vPosition.y *= -1.0;\n\n    float b = 1280.0 / 720.0;\n    float scale = u_scale;\n\n    float _F = u_curvature * 4.0;\n    float L = length(vec3(vPosition.xy / scale, _F));\n\n    vec2 vMapping = vPosition.xy * _F / L;\n    vMapping = vMapping * vec2(u_aspectRatio, 1.0);\n\n    vMapping = GLCoord2TextureCoord(vMapping / scale);\n    vMapping += (center - 0.5) * 0.3;\n\n    vec4 image2 = texture2D(u_texture, vMapping);\n    \n    if (REPEAT_TEXTURE &&\n        (vMapping.x > 0.99 || vMapping.x < 0.01 || vMapping.y > 0.99 || vMapping.y < 0.01)) {\n        image2 = vec4(0.0);\n    }\n    \n    gl_FragColor = image2;\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("curvature", 0.5f, 0.0f, 1.0f);
        addValueWithProperties("scale", 0.5f, 0.0f, 1.0f);
        addValueWithProperties("aspectRatio", 1.0f, 0.0f, 2.0f);
        addValueWithProperties("repeatTexture", 1.0f, 0.0f, 1.0f);
        addValueWithProperties("centerX", 0.5f, -1.0f, 1.0f);
        addValueWithProperties("centerY", 0.5f, -1.0f, 1.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_fov);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
