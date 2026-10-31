package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class ZoomBlurEffectElement extends DummyElement {
    public static final String typeName = "ZoomBlurEffect";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision mediump float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\n// shader author(s): PixiJS + Cubiq\nuniform float u_strength;\nuniform float u_innerRadius;\nuniform float u_radius;\nuniform float u_passes;\nuniform float u_gradientLength;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_aspectRatio;\n\n\n\n// uniform vec2 resolution;\n\n// #define u_centerX 0.5\n// #define u_centerY 0.5\n\n#define resolution vec2(u_aspectRatio,1.)\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\n\nfloat rand(vec2 co, float seed) {\n  const float a = 12.9898, b = 78.233, c = 43758.5453;\n  float dt = dot(co + seed, vec2(a, b));\n  float sn = mod(dt, 3.14159);\n  return fract(sin(sn) * c + seed);\n}\n\nvoid main()\n{\n  // Define your variables \n  vec2 iCenter = (vec2(u_centerX, u_centerY)+1.)/2.; // Example center position\n  float iStrength = u_strength; // Example u_strength\n  float iInnerRadius = u_innerRadius; // Example inner u_radius\n//   float iRadius = 10000.; // Example u_radius\n  float iRadius = u_radius/0.9; // Example \n  float iMaxKernelSize = u_passes*100.; // Example max kernel size\n\n  //vec2 vTexCoord = gl_FragCoord / resolution.xy;\n  // Convert uCenter, uu_InnerRadius, and uu_Radius to screen space\n  iCenter *= resolution.xy;\n  iInnerRadius *= min(resolution.x, resolution.y);\n  iRadius *= min(resolution.x, resolution.y);\n  \n\n   \n  float minGradient = iInnerRadius * 0.3 * u_gradientLength*5.;\n  float u_innerRadius = (iInnerRadius + minGradient * 0.5) / resolution.x;\n\n  float gradient = iRadius * 0.3;\n  float u_radius = (iRadius - gradient * 0.5) / resolution.x;\n\n\n  float countLimit = iMaxKernelSize;\n\n  vec2 dir = vec2(iCenter.xy / resolution.xy - vTexCoord.xy);\n  float dist = length(vec2(dir.x, dir.y * resolution.y / resolution.x));\n\n  float u_strength = iStrength;\n\n  float delta = 0.0;\n  float gap;\n  if (dist < u_innerRadius) {\n      delta = u_innerRadius - dist;\n      gap = minGradient;\n  } else if (u_radius >= 0.0 && dist > u_radius) {\n      delta = dist - u_radius;\n      gap = gradient;\n  }\n\n  if (delta > 0.0) {\n      float normalCount = gap / resolution.x;\n      delta = (normalCount - delta) / normalCount;\n      countLimit *= delta;\n      u_strength *= delta;\n      if (countLimit < 1.0)\n      {\n          gl_FragColor = texture2D(u_texture, vTexCoord.xy);\n          return;\n      }\n  }\n\n  // randomize the lookup values to hide the fixed number of samples\n  float offset = rand(vTexCoord.xy, 0.0);\n\n  float total = 0.0;\n  vec4 color = vec4(0.0);\n\n  dir *= u_strength;\n\n  for (float t = 0.0; t < iMaxKernelSize; t++) {\n      float percent = (t + offset) / iMaxKernelSize;\n      float weight = 4.0 * (percent - percent * percent);\n      vec2 p = vTexCoord.xy + dir * percent;\n      vec4 mysample = texture2D(u_texture, p);\n\n      // switch to pre-multiplied alpha to correctly blur transparent images\n      // sample.rgb *= sample.a;\n\n      color += mysample * weight;\n      total += weight;\n\n      if (t > countLimit){\n          break;\n      }\n  }\n\n  color /= total;\n  // switch back from pre-multiplied alpha\n  // color.rgb /= color.a + 0.00001;\n\n  gl_FragColor = color;\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithProperties("strength", 0.5f, 0.0f, 1.0f);
        addValueWithProperties("innerRadius", 0.3f, 0.0f, 1.0f);
        addValueWithProperties("radius", 1.0f, 0.0f, 1.0f);
        addValueWithProperties("passes", 0.5f, 0.0f, 1.0f);
        addValueWithProperties("gradientLength", 0.5f, 0.0f, 1.0f);
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
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_zoom_blur_effect);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}
