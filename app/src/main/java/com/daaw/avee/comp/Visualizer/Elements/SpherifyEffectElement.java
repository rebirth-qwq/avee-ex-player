package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class SpherifyEffectElement extends DummyElement {
    public static final String typeName = "Spherify";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_spherify);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nfloat focalLength = 50.0;\nfloat sphereRotationX = 0.0;\nfloat sphereRotationY = 0.0;\n\nvarying vec2 vTexCoord;\n\nuniform sampler2D u_texture;\nuniform float u_radius;\nuniform float u_rotationX;\nuniform float u_rotationY;\nuniform float u_aspectRatio;\nuniform float u_textureAspect;\nuniform float u_camDistance;\n\nconst int MAX_STEPS = 500;\nconst float MAX_TRACE_DISTANCE = 1000.0;\nconst float MIN_HIT_DISTANCE = 0.001;\n\n#define PI 3.1415\n#define TWO_PI (PI*2.0)\n\nvec3 rotateY(vec3 p, float angle) {\n    float c = cos(angle);\n    float s = sin(angle);\n    return vec3(p.x * c + p.z * s, p.y, -p.x * s + p.z * c);\n}\n\nvec3 rotateX(vec3 p, float angle) {\n    float c = cos(angle);\n    float s = sin(angle);\n    return vec3(p.x, p.y * c - p.z * s, p.y * s + p.z * c);\n}\n\nvec3 rotatePointAbout(vec3 p, vec3 o, float rx, float ry) {\n    vec3 r = p - o;\n    r = rotateX(r, rx);\n    r = rotateY(r, ry);\n    return r + o;\n}\n\nfloat sdSphere(vec3 p, vec3 o, float r) {\n    return length(p - o) - r;\n}\n\nvec4 rayMarch(vec3 ro, vec3 rd) {\n    float distanceTraveled = 0.0;\n    for (int i = 0; i < MAX_STEPS; ++i) {\n        vec3 sphereOrigin = vec3(0.0, 0.0, -1000.0 * u_camDistance);\n        float sphereRadius = u_radius * 4.0;\n        vec3 currentPosition = ro + rd * distanceTraveled;\n        vec3 relativePosition = rotatePointAbout(currentPosition, sphereOrigin, sphereRotationX, sphereRotationY);\n        float distanceToClosest = sdSphere(relativePosition, sphereOrigin, sphereRadius);\n        if (distanceToClosest < MIN_HIT_DISTANCE) {\n            relativePosition -= sphereOrigin;\n            float phi = atan(relativePosition.z, relativePosition.x);\n            float theta = atan(relativePosition.y, sqrt(relativePosition.z * relativePosition.z + relativePosition.x * relativePosition.x));\n            vec2 uv = vec2((phi + PI) / TWO_PI, (theta + PI / 2.0) / PI);\n            vec4 color = texture2D(u_texture, uv * vec2(u_textureAspect * 2.0, 1.0));\n            return color;\n        }\n        if (distanceTraveled > MAX_TRACE_DISTANCE) {\n            break;\n        }\n        distanceTraveled += distanceToClosest;\n    }\n    return vec4(0.0);\n}\n\nvoid main() {\n    vec2 xy = vTexCoord - 0.5;\n    xy *= vec2(u_aspectRatio, 1.0);\n    sphereRotationX = u_rotationX * TWO_PI;\n    sphereRotationY = u_rotationY * TWO_PI;\n    vec3 rayOrigin = vec3(0.0, 0.0, 0.0);\n    vec3 rayDirection = normalize(vec3(xy, -focalLength));\n    gl_FragColor = rayMarch(rayOrigin, rayDirection);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("radius", 1.0f, 0.0f, 1.0f);
        addValueWithProperties("rotationX", 1.0f, 0.0f, 1.0f);
        addValueWithProperties("rotationY", 0.0f, 0.0f, 1.0f);
        addValueWithProperties("camDistance", 1.0f, 0.0f, 1.0f);
        addValueWithProperties("textureAspect", 0.5f, 0.0f, 2.0f);
        addValueWithProperties("aspectRatio", 2.0f, 0.0f, 4.0f);
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
