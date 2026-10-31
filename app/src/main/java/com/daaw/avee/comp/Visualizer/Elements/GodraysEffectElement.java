package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class GodraysEffectElement extends DummyElement {
    public static final String typeName = "GodraysEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "// Godrays Effect\n// Thanks to the Vizzy Team: vizzy.io \nprecision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;  \n    \nuniform sampler2D u_texture;\n\n// uniform float u_time;\nuniform float u_decay;\nuniform float u_density;\nuniform float u_weight;\nuniform float u_rayCenterX;\nuniform float u_rayCenterY;\nuniform float u_rayOpacity;\nuniform float u_saturation;\n\nuniform float u_ignoreDark;\nuniform float u_renderInputOnTop;\n\nuniform float u_sampleCount;\n\n#define ignoreDark floatToBool(u_ignoreDark)\n#define renderInputOnTop floatToBool(u_renderInputOnTop)\n#define nsamples floatToInt(u_sampleCount)\n\nvarying vec2 vTexCoord;\n\nint floatToInt(float f) { return int(f); }\n\nbool floatToBool(float f) { return f > 0.5; }\n\n\n// Algorithm found in https://medium.com/community-play-3d/god-rays-whats-that-5a67f26aeac2 // https://www.shadertoy.com/view/ls2Xzd\n\n// https://github.com/jamieowen/glsl-blend\nfloat blendScreen(float base, float blend) {\n    return 1.0-((1.0-base)*(1.0-blend));\n}\n    \nvec4 blendScreen(vec4 base, vec4 blend) {\n    return vec4(blendScreen(base.r,blend.r),blendScreen(base.g,blend.g),blendScreen(base.b,blend.b),blendScreen(base.a,blend.a));\n}\n    \nvec4 blendScreen(vec4 base, vec4 blend, float opacity) {\n    return (blendScreen(base, blend) * opacity + base * (1.0 - opacity));\n}\n\nvec3 blendScreen(vec3 base, vec3 blend) {\n    return vec3(blendScreen(base.r,blend.r),blendScreen(base.g,blend.g),blendScreen(base.b,blend.b));\n}\n    \nvec3 blendScreen(vec3 base, vec3 blend, float opacity) {\n    return (blendScreen(base, blend) * opacity + base * (1.0 - opacity));\n}\n    \nvec4 saturation(vec4 col, float adjustment)\n{\n    const vec3 W = vec3(0.2125, 0.7154, 0.0721);\n    vec3 color = col.rgb;\n    vec3 intensityRGB = vec3(dot(color, W));\n    vec4 intensityRGBA = vec4(intensityRGB, col.a);\n    return mix(intensityRGBA, col, adjustment);\n}\n    \nvec4 crepuscular_rays(vec2 texCoords, vec2 pos) {\n    //float u_decay = 0.92;\n    //float u_density = 1.0;\n    //float u_weight = 0.58767;\n    /// NUM_SAMPLES will describe the rays quality, you can play with\n    //const int nsamples =50;\n\n    vec2 tc = vTexCoord;\n    \n    vec2 deltaTexCoord = tc - pos.xy;\n    \n    deltaTexCoord *= (1.0 / float(nsamples) * u_density);\n    \n    float illuminationDecay = 1.0;\n  \n    vec4 color = texture2D(u_texture, tc.xy);\n  \n    tc += deltaTexCoord * fract( sin(dot(texCoords.xy+fract(0.), vec2(12.9898, 78.233)))* 43758.5453 );\n    \n    for (int i = 0; i < nsamples; i++)\n    {\n        tc -= deltaTexCoord;\n        vec4 sampl = texture2D(u_texture, tc.xy);\n\n        sampl *= illuminationDecay * u_weight;\n        \n        if(!ignoreDark)\n          color = blendScreen(color, sampl, u_rayOpacity);\n        else {\n          color = vec4(blendScreen(color.rgb, sampl.rgb, u_rayOpacity), color.a);\n        }\n        illuminationDecay *= u_decay;\n    }\n\n    color = saturation(color, u_saturation);\n\n    if(renderInputOnTop) {\n      color.rgb *= vec3(1.0 - texture2D(u_texture, vTexCoord).a);\n      color.rgb += texture2D(u_texture, vTexCoord).rgb;\n    }\n\n    return color;\n}\n\nvoid main(){\n  vec2 uv = vTexCoord;\n  vec2 pos = vec2((u_rayCenterX * -1. + 0.5), (u_rayCenterY * -1. + 0.5));\n  gl_FragColor = crepuscular_rays(uv, pos.xy);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("decay", 0.9f, 0.0f, 1.0f);
        addValueWithProperties("density", 1.0f, 0.0f, 1.0f);
        addValueWithProperties("weight", 0.2f, 0.0f, 1.0f);
        addValueWithProperties("rayOpacity", 1.0f, 0.0f, 2.0f);
        addValueWithProperties("saturation", 1.0f, 0.0f, 2.0f);
        addValueWithProperties("ignoreDarkColors", 0.0f, 0.0f, 1.0f);
        addValueWithProperties("renderInputOnTop", 0.0f, 0.0f, 1.0f);
        addValueWithProperties("rayCenterX", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("rayCenterY", 0.0f, -1.0f, 1.0f);
        addValueWithProperties("sampleCount", 50.0f, 0.0f, 100.0f);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_godrays_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
