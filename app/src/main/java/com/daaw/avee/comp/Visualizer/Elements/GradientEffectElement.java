package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class GradientEffectElement extends DummyElement {
    public static final String typeName = "Gradient";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_gradient);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_redFrom;\nuniform float u_greenFrom;\nuniform float u_blueFrom;\nuniform float u_alphaFrom;\nuniform float u_redTo;\nuniform float u_greenTo;\nuniform float u_blueTo;\nuniform float u_alphaTo;\nuniform float u_redMid;\nuniform float u_greenMid;\nuniform float u_blueMid;\nuniform float u_alphaMid;\nuniform float u_useMid;\nuniform float u_midPos;\nuniform float u_gradientType;\nuniform float u_angle;\nuniform float u_scale;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_stretchX;\nuniform float u_stretchY;\nuniform float u_repeatMode;\nuniform float u_repeatOffset;\nuniform float u_softness;\nuniform float u_saturation;\nuniform float u_brightness;\nuniform float u_contrast;\nuniform float u_blendOffset;\nuniform float u_opacity;\nuniform float u_invert;\n\nvec2 rotate2D(vec2 p, float a) {\n    float c = cos(a);\n    float s = sin(a);\n    return vec2(p.x * c - p.y * s, p.x * s + p.y * c);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec4 texColor = texture2D(u_texture, uv);\n    vec2 pos = uv - vec2(u_centerX, u_centerY);\n    pos = rotate2D(pos, u_angle * 6.28318);\n    pos.x = pos.x / max(u_stretchX, 0.001);\n    pos.y = pos.y / max(u_stretchY, 0.001);\n    float t = 0.0;\n    float type = u_gradientType;\n    if (type < 0.5) {\n        t = pos.x / u_scale + 0.5;\n    } else if (type < 1.5) {\n        t = length(pos) / u_scale;\n    } else if (type < 2.5) {\n        t = atan(pos.y, pos.x) / 6.28318 + 0.5;\n    } else if (type < 3.5) {\n        t = (abs(pos.x) + abs(pos.y)) / u_scale;\n    } else if (type < 4.5) {\n        t = max(abs(pos.x), abs(pos.y)) / u_scale;\n    } else if (type < 5.5) {\n        t = min(abs(pos.x), abs(pos.y)) * 2.0 / u_scale;\n    } else if (type < 6.5) {\n        t = sin(pos.x * 6.28318 / u_scale) * 0.5 + 0.5;\n    } else if (type < 7.5) {\n        float d = length(pos);\n        t = sin(d * 12.566 / u_scale) * 0.5 + 0.5;\n    } else {\n        t = abs(sin(pos.x * 6.28318 / u_scale));\n    }\n    float repeat = u_repeatMode;\n    if (repeat < 0.5) {\n    } else if (repeat < 1.5) {\n        t = fract(t + u_repeatOffset);\n    } else if (repeat < 2.5) {\n        t = abs(fract(t * 0.5 + 0.5 + u_repeatOffset * 0.5) - 0.5) * 2.0;\n    } else {\n        t = abs(mod(t + u_repeatOffset, 2.0) - 1.0);\n    }\n    if (u_invert > 0.5) {\n        t = 1.0 - t;\n    }\n    t = clamp(t, 0.0, 1.0);\n    if (u_softness > 0.0) {\n        float s = u_softness * 0.5;\n        t = smoothstep(s, 1.0 - s, t);\n    }\n    vec4 colorFrom = vec4(u_redFrom, u_greenFrom, u_blueFrom, u_alphaFrom);\n    vec4 colorMid = vec4(u_redMid, u_greenMid, u_blueMid, u_alphaMid);\n    vec4 colorTo = vec4(u_redTo, u_greenTo, u_blueTo, u_alphaTo);\n    vec4 gradColor = colorFrom;\n    if (u_useMid > 0.5) {\n        if (t < u_midPos) {\n            gradColor = mix(colorFrom, colorMid, t / max(u_midPos, 0.001));\n        } else {\n            gradColor = mix(colorMid, colorTo, (t - u_midPos) / max(1.0 - u_midPos, 0.001));\n        }\n    } else {\n        gradColor = mix(colorFrom, colorTo, t);\n    }\n    vec3 rgb = gradColor.rgb;\n    float gray = dot(rgb, vec3(0.299, 0.587, 0.114));\n    rgb = mix(vec3(gray), rgb, u_saturation);\n    rgb *= u_brightness;\n    rgb = clamp((rgb - 0.5) * u_contrast + 0.5, 0.0, 1.0);\n    float alpha = gradColor.a * u_opacity;\n    vec4 finalGrad = vec4(rgb, alpha);\n    vec4 finalColor = mix(texColor, finalGrad, u_blendOffset);\n    gl_FragColor = finalColor;\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("redFrom", 1.0f, 0.0f, 1.0f, "ColorFrom");
        addValueWithPropertiesCat("greenFrom", 0.0f, 0.0f, 1.0f, "ColorFrom");
        addValueWithPropertiesCat("blueFrom", 0.0f, 0.0f, 1.0f, "ColorFrom");
        addValueWithPropertiesCat("alphaFrom", 1.0f, 0.0f, 1.0f, "ColorFrom");
        addValueWithPropertiesCat("redMid", 0.0f, 0.0f, 1.0f, "MidColor");
        addValueWithPropertiesCat("greenMid", 1.0f, 0.0f, 1.0f, "MidColor");
        addValueWithPropertiesCat("blueMid", 0.0f, 0.0f, 1.0f, "MidColor");
        addValueWithPropertiesCat("alphaMid", 1.0f, 0.0f, 1.0f, "MidColor");
        addValueWithPropertiesCat("useMid", 0.0f, 0.0f, 1.0f, "MidColor");
        addValueWithPropertiesCat("midPos", 0.5f, 0.0f, 1.0f, "MidColor");
        addValueWithPropertiesCat("redTo", 0.0f, 0.0f, 1.0f, "ColorTo");
        addValueWithPropertiesCat("greenTo", 0.0f, 0.0f, 1.0f, "ColorTo");
        addValueWithPropertiesCat("blueTo", 1.0f, 0.0f, 1.0f, "ColorTo");
        addValueWithPropertiesCat("alphaTo", 1.0f, 0.0f, 1.0f, "ColorTo");
        addValueWithPropertiesCat("gradientType", 0.0f, 0.0f, 8.0f, "Type");
        addValueWithPropertiesCat("angle", 0.0f, -1.0f, 1.0f, "Transform");
        addValueWithPropertiesCat("scale", 1.0f, 0.05f, 5.0f, "Transform");
        addValueWithPropertiesCat("centerX", 0.5f, -2.0f, 2.0f, "Transform");
        addValueWithPropertiesCat("centerY", 0.5f, -2.0f, 2.0f, "Transform");
        addValueWithPropertiesCat("stretchX", 1.0f, 0.05f, 5.0f, "Transform");
        addValueWithPropertiesCat("stretchY", 1.0f, 0.05f, 5.0f, "Transform");
        addValueWithPropertiesCat("repeatMode", 0.0f, 0.0f, 3.0f, "Repeat");
        addValueWithPropertiesCat("repeatOffset", 0.0f, 0.0f, 1.0f, "Repeat");
        addValueWithPropertiesCat("softness", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("saturation", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("brightness", 1.0f, 0.0f, 2.0f, "Color");
        addValueWithPropertiesCat("contrast", 1.0f, 0.0f, 3.0f, "Color");
        addValueWithPropertiesCat("opacity", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("invert", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("blendOffset", 0.5f, 0.0f, 1.0f, "Blend");
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
