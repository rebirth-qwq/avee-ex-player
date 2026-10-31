package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class FailedSpherifyEffectElement extends DummyElement {
    public static final String typeName = "FailedSpherify";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_Failed_Spherify);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_radius;\nuniform float u_rotationX;\nuniform float u_rotationY;\nuniform float u_aspectRatio;\nuniform float u_textureAspect;\nuniform float u_depth;\n\nconst float PI = 3.14159265359;\nconst float TWO_PI = 6.28318530718;\n\nvec3 rotateY(vec3 p, float angle) {\n    float c = cos(angle);\n    float s = sin(angle);\n    return vec3(p.x * c + p.z * s, p.y, -p.x * s + p.z * c);\n}\n\nvec3 rotateX(vec3 p, float angle) {\n    float c = cos(angle);\n    float s = sin(angle);\n    return vec3(p.x, p.y * c - p.z * s, p.y * s + p.z * c);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord - 0.5;\n    uv.x *= u_aspectRatio;\n    float dist = length(uv);\n    float radius = u_radius * 1.5;\n    if (dist > radius) {\n        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n        return;\n    }\n    float theta = atan(uv.y, uv.x);\n    float phi = asin(clamp(dist / radius, -1.0, 1.0));\n    vec3 hitPoint = vec3(\n        radius * cos(phi) * cos(theta),\n        radius * sin(phi),\n        radius * cos(phi) * sin(theta)\n    );\n    float rotX = u_rotationX * TWO_PI;\n    float rotY = u_rotationY * TWO_PI;\n    hitPoint = rotateX(hitPoint, rotX);\n    hitPoint = rotateY(hitPoint, rotY);\n    float hitRadius = length(hitPoint);\n    if (hitRadius < 0.0001) {\n        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n        return;\n    }\n    float phi2 = atan(hitPoint.z, hitPoint.x);\n    float theta2 = asin(clamp(hitPoint.y / hitRadius, -1.0, 1.0));\n    vec2 texCoord = vec2(\n        (phi2 + PI) / TWO_PI,\n        (theta2 + PI / 2.0) / PI\n    );\n    texCoord.x = texCoord.x * u_textureAspect * 2.0;\n    texCoord.x = fract(texCoord.x);\n    texCoord.y = clamp(texCoord.y, 0.0, 1.0);\n    vec4 color = texture2D(u_texture, texCoord);\n    vec3 normal = normalize(hitPoint);\n    vec3 lightDir = normalize(vec3(0.5, 1.0, 0.5));\n    float diff = max(0.0, dot(normal, lightDir));\n    float ambient = 0.3;\n    float lighting = ambient + (1.0 - ambient) * diff;\n    vec3 viewDir = normalize(vec3(0.0, 0.0, -1.0));\n    float rim = 1.0 - max(0.0, dot(normal, viewDir));\n    rim = pow(rim, 2.0) * 0.3;\n    gl_FragColor = vec4(color.rgb * (lighting + rim), color.a);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("radius", 0.6f, 0.0f, 1.0f, "Sphere");
        addValueWithPropertiesCat("rotationX", 0.5f, 0.0f, 1.0f, "Sphere");
        addValueWithPropertiesCat("rotationY", 0.5f, 0.0f, 1.0f, "Sphere");
        addValueWithPropertiesCat("depth", 0.5f, 0.0f, 1.0f, "Sphere");
        addValueWithPropertiesCat("textureAspect", 0.5f, 0.0f, 2.0f, "Sphere");
        addValueWithPropertiesCat("aspectRatio", 2.0f, 0.0f, 4.0f, "Sphere");
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
