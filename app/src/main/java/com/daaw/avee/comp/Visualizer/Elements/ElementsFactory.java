package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.Visualizer.Elements.Base.Element;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ElementsFactory {
    private static Map<String, Class<? extends Element>> elementTypes = new LinkedHashMap();
    public static String typeNameNone = "None";

    static {
        rebuildElementTypes();
    }

    private static void rebuildElementTypes() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(typeNameNone, null);
        linkedHashMap.put(RootElement.typeName, RootElement.class);
        linkedHashMap.put(Composition.typeName, Composition.class);
        linkedHashMap.put(TextElement.typeName, TextElement.class);
        linkedHashMap.put("Bars", SegmentElement.class);
        linkedHashMap.put(ParticlesElement.typeName, ParticlesElement.class);
        linkedHashMap.put(ImageElement.typeName, ImageElement.class);
        linkedHashMap.put(AudioDataProviderElement.typeName, AudioDataProviderElement.class);
        linkedHashMap.put("BlurEffect", BlurStackEffectElement.class);
        linkedHashMap.put(RgbSplitEffectElement.typeName, RgbSplitEffectElement.class);
        linkedHashMap.put(MotionBlurEffectElement.typeName, MotionBlurEffectElement.class);
        linkedHashMap.put(MirrorEffectElement.typeName, MirrorEffectElement.class);
        linkedHashMap.put(LogoMark2.typeName, LogoMark2.class);
        linkedHashMap.put(DummyElement.typeName, DummyElement.class);
        linkedHashMap.put(ColorCorrectionElement.typeName, ColorCorrectionElement.class);
        linkedHashMap.put(TwirlEffectElement.typeName, TwirlEffectElement.class);
        linkedHashMap.put(LiquifyEffectElement.typeName, LiquifyEffectElement.class);
        linkedHashMap.put(Kaleidoscope.typeName, Kaleidoscope.class);
        linkedHashMap.put(FOVElement.typeName, FOVElement.class);
        linkedHashMap.put(Box3DElement.typeName, Box3DElement.class);
        linkedHashMap.put(ZoomBlurEffectElement.typeName, ZoomBlurEffectElement.class);
        linkedHashMap.put(PixelEffectElement.typeName, PixelEffectElement.class);
        linkedHashMap.put(EdgeEffectElement.typeName, EdgeEffectElement.class);
        linkedHashMap.put(GodraysEffectElement.typeName, GodraysEffectElement.class);
        linkedHashMap.put(SpherifyEffectElement.typeName, SpherifyEffectElement.class);
        linkedHashMap.put(CurveEffectElement.typeName, CurveEffectElement.class);
        linkedHashMap.put(BulgePinchEffectElement.typeName, BulgePinchEffectElement.class);
        linkedHashMap.put(LyricsElement.typeName, LyricsElement.class);
        linkedHashMap.put(VectorBlurEffectElement.typeName, VectorBlurEffectElement.class);
        linkedHashMap.put(WaterRippleEffectElement.typeName, WaterRippleEffectElement.class);
        linkedHashMap.put(DepthDisplaceEffectElement.typeName, DepthDisplaceEffectElement.class);
        linkedHashMap.put(GlowEffectElement.typeName, GlowEffectElement.class);
        linkedHashMap.put(Fake3DEffectElement.typeName, Fake3DEffectElement.class);
        linkedHashMap.put(PixelStretchEffectElement.typeName, PixelStretchEffectElement.class);
        {
            linkedHashMap.put(AfterImageEffectElement.typeName, AfterImageEffectElement.class);
            linkedHashMap.put(CausticEffectElement.typeName, CausticEffectElement.class);
            linkedHashMap.put(CCPowerPinEffectElement.typeName, CCPowerPinEffectElement.class);
            linkedHashMap.put(DissolveEffectElement.typeName, DissolveEffectElement.class);
            linkedHashMap.put(FractalNoiseEffectElement.typeName, FractalNoiseEffectElement.class);
            linkedHashMap.put(GaussianBlurEffectElement.typeName, GaussianBlurEffectElement.class);
            linkedHashMap.put(GlitchEffectElement.typeName, GlitchEffectElement.class);
            linkedHashMap.put(HalftoneEffectElement.typeName, HalftoneEffectElement.class);
            linkedHashMap.put(HeatDistortionEffectElement.typeName, HeatDistortionEffectElement.class);
            linkedHashMap.put(MagnifierEffectElement.typeName, MagnifierEffectElement.class);
            linkedHashMap.put(RoughenEdgesEffectElement.typeName, RoughenEdgesEffectElement.class);
            linkedHashMap.put(SpiralEffectElement.typeName, SpiralEffectElement.class);
            linkedHashMap.put(WaveDistortionEffectElement.typeName, WaveDistortionEffectElement.class);
            linkedHashMap.put(GradientEffectElement.typeName, GradientEffectElement.class);
            linkedHashMap.put(DropShadowEffectElement.typeName, DropShadowEffectElement.class);
            linkedHashMap.put(RippleDistortionEffectElement.typeName, RippleDistortionEffectElement.class);
            linkedHashMap.put(RainDropEffectElement.typeName, RainDropEffectElement.class);
            linkedHashMap.put(DarkFilterEffectElement.typeName, DarkFilterEffectElement.class);
            linkedHashMap.put(OldTVEffectElement.typeName, OldTVEffectElement.class);
            linkedHashMap.put(RadialBlurEffectElement.typeName, RadialBlurEffectElement.class);
            linkedHashMap.put(RandomLinesEffectElement.typeName, RandomLinesEffectElement.class);
            linkedHashMap.put(CircleizeEffectElement.typeName, CircleizeEffectElement.class);
            linkedHashMap.put(HeatWaveEffectElement.typeName, HeatWaveEffectElement.class);
            linkedHashMap.put(FailedSpherifyEffectElement.typeName, FailedSpherifyEffectElement.class);
        }
        elementTypes = linkedHashMap;
    }


    public static Element create(String str, String str2, Element element) {
        Class<? extends Element> cls = elementTypes.get(str);
        if (cls != null && !cls.isInstance(element)) {
            try {
                return cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception e) {
                tlog.d("Failed to create element for typeName: " + str);
            }
        }
        tlog.d("Unknown typeName: " + str);
        return element;
    }

    public static String getTypeName(Element element) {
        if (element == null) {
            return typeNameNone;
        }
        for (Map.Entry<String, Class<? extends Element>> entry : elementTypes.entrySet()) {
            Class<? extends Element> value = entry.getValue();
            if (value != null && value.isInstance(element)) {
                return entry.getKey();
            }
        }
        tlog.d("Unknown instance type");
        return "unk";
    }

    public static Set getAddableTypeNames() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (String str : elementTypes.keySet()) {
            if (!str.equals(typeNameNone) && isTypeAddable(str)) {
                linkedHashSet.add(str);
            }
        }
        return linkedHashSet;
    }

    private static boolean isTypeAddable(String str) {
        for (String str2 : new String[]{RootElement.typeName}) {
            if (str2 != null && str.equals(str2)) {
                return false;
            }
        }
        return true;
    }


    public static String[] getAddableTypeNamesArray() {
        rebuildElementTypes();
        return (String[]) getAddableTypeNames().toArray(new String[0]);
    }
}
