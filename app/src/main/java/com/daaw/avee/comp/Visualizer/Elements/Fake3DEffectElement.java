package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class Fake3DEffectElement extends DummyElement {
    public static final String typeName = "Fake3D";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_fake_3d);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\n\nuniform sampler2D u_texture;\nuniform float u_rotateX;\nuniform float u_rotateY;\nuniform float u_perspective;\nuniform float u_scaleX;\nuniform float u_scaleY;\nuniform float u_offsetX;\nuniform float u_offsetY;\nuniform float u_aspect;\n\nvarying vec2 vTexCoord;\n\nvoid main() {\n    float aspect = u_aspect > 0.0 ? u_aspect : 1.0;\n    vec2 uv = vTexCoord - 0.5;\n    uv.x *= aspect;\n    float cosX = cos(u_rotateX);\n    float sinX = sin(u_rotateX);\n    float cosY = cos(u_rotateY);\n    float sinY = sin(u_rotateY);\n    vec3 pos = vec3(uv.x, uv.y, 0.0);\n    mat3 rotY = mat3(cosY, 0.0, sinY, 0.0, 1.0, 0.0, -sinY, 0.0, cosY);\n    mat3 rotX = mat3(1.0, 0.0, 0.0, 0.0, cosX, -sinX, 0.0, sinX, cosX);\n    vec3 rotated = rotX * (rotY * pos);\n    float perspective = 1.0 / (1.0 + rotated.z * u_perspective);\n    vec2 finalUV = rotated.xy * perspective;\n    finalUV.x /= aspect;\n    finalUV += 0.5;\n    finalUV += vec2(u_offsetX, u_offsetY);\n    finalUV = (finalUV - 0.5) * vec2(u_scaleX, u_scaleY) + 0.5;\n    if (finalUV.x < 0.0 || finalUV.x > 1.0 || finalUV.y < 0.0 || finalUV.y > 1.0) {\n        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n        return;\n    }\n    gl_FragColor = texture2D(u_texture, finalUV);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("rotateX", 0.2f, -1.0f, 1.0f, "Rotation");
        addValueWithPropertiesCat("rotateY", 0.0f, -1.0f, 1.0f, "Rotation");
        addValueWithPropertiesCat("perspective", 1.0f, -1.0f, 1.0f, "Perspective");
        addValueWithPropertiesCat("scaleX", 1.3f, 0.1f, 3.0f, "Transform");
        addValueWithPropertiesCat("scaleY", 1.5f, 0.1f, 3.0f, "Transform");
        addValueWithPropertiesCat("offsetX", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("offsetY", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("aspect", 1.0f, 0.1f, 3.0f, "Transform");
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
