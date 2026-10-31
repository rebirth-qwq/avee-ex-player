package com.daaw.avee.comp.VisualizerCutomization;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daaw.avee.Common.Func.Func;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.ContextData;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.Visualizer.CustomComposition;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.CustomScene;
import com.daaw.avee.comp.Visualizer.Elements.AfterImageEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.AudioDataProviderElement;
import com.daaw.avee.comp.Visualizer.Elements.Box3DElement;
import com.daaw.avee.comp.Visualizer.Elements.BulgePinchEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.CCPowerPinEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.CausticEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.CircleizeEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.ColorCorrectionElement;
import com.daaw.avee.comp.Visualizer.Elements.Composition;
import com.daaw.avee.comp.Visualizer.Elements.CurveEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.DarkFilterEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.DepthDisplaceEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.DissolveEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.DropShadowEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.DummyElement;
import com.daaw.avee.comp.Visualizer.Elements.EdgeEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.ElementsFactory;
import com.daaw.avee.comp.Visualizer.Elements.FOVElement;
import com.daaw.avee.comp.Visualizer.Elements.FailedSpherifyEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.Fake3DEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.FractalNoiseEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GaussianBlurEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GlitchEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GlowEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GodraysEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GradientEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.HalftoneEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.HeatDistortionEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.HeatWaveEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.ImageElement;
import com.daaw.avee.comp.Visualizer.Elements.Kaleidoscope;
import com.daaw.avee.comp.Visualizer.Elements.LiquifyEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.LogoMark2;
import com.daaw.avee.comp.Visualizer.Elements.LyricsElement;
import com.daaw.avee.comp.Visualizer.Elements.MagnifierEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.MirrorEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.MotionBlurEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.OldTVEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.ParticlesElement;
import com.daaw.avee.comp.Visualizer.Elements.PixelEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.PixelStretchEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.RadialBlurEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.RainDropEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.RandomLinesEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.RgbSplitEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.RippleDistortionEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.RoughenEdgesEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.SpherifyEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.SpiralEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.TextElement;
import com.daaw.avee.comp.Visualizer.Elements.TwirlEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.VectorBlurEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.WaterRippleEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.WaveDistortionEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.ZoomBlurEffectElement;
import com.daaw.avee.vendor.dragsort.DragSortRecycler;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CustomizeVisView0 {
    View actionsElements;
    ImageButton btnExpandAdd;
    ImageButton btnExpandReset;
    private CustomizeVisDialog customizeVisDialog;
    View expandAddElements;
    View expandReset;
    GridLayout gridAddElements;
    CustomSimpleAdapter mAdapter;
    CustomSimpleSectionedAdapter mSectionedAdapter;
    RecyclerView recyclerViewItems;
    ScrollView scrollView1;
    private static final Map<String, Integer> ELEMENT_NAME_MAP = new HashMap();
    private static final Map<String, Integer> ELEMENT_LIST_NAME_MAP = new HashMap();
    private final List<CustomizeVisDialog.ThreeEntry> treeElements = new ArrayList();
    public int selectedElementInTree = -1;

    static {
        ELEMENT_NAME_MAP.put(Composition.typeName, Integer.valueOf(R.string.element_composition));
        ELEMENT_NAME_MAP.put(ParticlesElement.typeName, Integer.valueOf(R.string.element_particles));
        ELEMENT_NAME_MAP.put(AudioDataProviderElement.typeName, Integer.valueOf(R.string.element_audio_source));
        ELEMENT_NAME_MAP.put("BlurEffect", Integer.valueOf(R.string.element_blur));
        ELEMENT_NAME_MAP.put(RgbSplitEffectElement.typeName, Integer.valueOf(R.string.element_rgb_split));
        ELEMENT_NAME_MAP.put(MotionBlurEffectElement.typeName, Integer.valueOf(R.string.element_motion_blur));
        ELEMENT_NAME_MAP.put(MirrorEffectElement.typeName, Integer.valueOf(R.string.element_mirror));
        ELEMENT_NAME_MAP.put(LogoMark2.typeName, Integer.valueOf(R.string.element_watermark));
        ELEMENT_NAME_MAP.put(DummyElement.typeName, Integer.valueOf(R.string.element_custom));
        ELEMENT_NAME_MAP.put(ColorCorrectionElement.typeName, Integer.valueOf(R.string.element_color_correction));
        ELEMENT_NAME_MAP.put(TwirlEffectElement.typeName, Integer.valueOf(R.string.element_twirl));
        ELEMENT_NAME_MAP.put(LiquifyEffectElement.typeName, Integer.valueOf(R.string.element_liquify));
        ELEMENT_NAME_MAP.put(Kaleidoscope.typeName, Integer.valueOf(R.string.element_kaleidoscope));
        ELEMENT_NAME_MAP.put(FOVElement.typeName, Integer.valueOf(R.string.element_fov));
        ELEMENT_NAME_MAP.put(ZoomBlurEffectElement.typeName, Integer.valueOf(R.string.element_zoom_blur));
        ELEMENT_NAME_MAP.put(PixelEffectElement.typeName, Integer.valueOf(R.string.element_pixelate));
        ELEMENT_NAME_MAP.put(Box3DElement.typeName, Integer.valueOf(R.string.element_cube));
        ELEMENT_NAME_MAP.put(EdgeEffectElement.typeName, Integer.valueOf(R.string.element_edge_detection));
        ELEMENT_NAME_MAP.put(GodraysEffectElement.typeName, Integer.valueOf(R.string.element_godrays));
        ELEMENT_NAME_MAP.put(SpherifyEffectElement.typeName, Integer.valueOf(R.string.element_spherify));
        ELEMENT_NAME_MAP.put(CurveEffectElement.typeName, Integer.valueOf(R.string.element_polar_coordinates));
        ELEMENT_NAME_MAP.put(BulgePinchEffectElement.typeName, Integer.valueOf(R.string.element_bulge_pinch));
        ELEMENT_NAME_MAP.put(ImageElement.typeName, Integer.valueOf(R.string.element_image));
        ELEMENT_NAME_MAP.put(TextElement.typeName, Integer.valueOf(R.string.element_text));
        ELEMENT_NAME_MAP.put("Bars", Integer.valueOf(R.string.element_bars));
        ELEMENT_NAME_MAP.put(AfterImageEffectElement.typeName, Integer.valueOf(R.string.element_afterimage));
        ELEMENT_NAME_MAP.put(CausticEffectElement.typeName, Integer.valueOf(R.string.element_caustic));
        ELEMENT_NAME_MAP.put(CCPowerPinEffectElement.typeName, Integer.valueOf(R.string.element_corner_pin));
        ELEMENT_NAME_MAP.put(DepthDisplaceEffectElement.typeName, Integer.valueOf(R.string.element_depth_displace));
        ELEMENT_NAME_MAP.put(DissolveEffectElement.typeName, Integer.valueOf(R.string.element_dissolve));
        ELEMENT_NAME_MAP.put(Fake3DEffectElement.typeName, Integer.valueOf(R.string.element_3d_transform));
        ELEMENT_NAME_MAP.put(FractalNoiseEffectElement.typeName, Integer.valueOf(R.string.element_fractal_noise));
        ELEMENT_NAME_MAP.put(GaussianBlurEffectElement.typeName, Integer.valueOf(R.string.element_gaussian_blur));
        ELEMENT_NAME_MAP.put(GlitchEffectElement.typeName, Integer.valueOf(R.string.element_glitch));
        ELEMENT_NAME_MAP.put(GlowEffectElement.typeName, Integer.valueOf(R.string.element_glow));
        ELEMENT_NAME_MAP.put(HalftoneEffectElement.typeName, Integer.valueOf(R.string.element_halftone));
        ELEMENT_NAME_MAP.put(HeatDistortionEffectElement.typeName, Integer.valueOf(R.string.element_heat_distortion));
        ELEMENT_NAME_MAP.put(MagnifierEffectElement.typeName, Integer.valueOf(R.string.element_magnifier));
        ELEMENT_NAME_MAP.put(PixelStretchEffectElement.typeName, Integer.valueOf(R.string.element_pixel_stretch));
        ELEMENT_NAME_MAP.put(RoughenEdgesEffectElement.typeName, Integer.valueOf(R.string.element_roughen_edges));
        ELEMENT_NAME_MAP.put(SpiralEffectElement.typeName, Integer.valueOf(R.string.element_spiral));
        ELEMENT_NAME_MAP.put(VectorBlurEffectElement.typeName, Integer.valueOf(R.string.element_vector_blur));
        ELEMENT_NAME_MAP.put(WaterRippleEffectElement.typeName, Integer.valueOf(R.string.element_water_ripple));
        ELEMENT_NAME_MAP.put(WaveDistortionEffectElement.typeName, Integer.valueOf(R.string.element_wave_distortion));
        ELEMENT_NAME_MAP.put(GradientEffectElement.typeName, Integer.valueOf(R.string.element_gradient));
        ELEMENT_NAME_MAP.put(DropShadowEffectElement.typeName, Integer.valueOf(R.string.element_drop_shadow));
        ELEMENT_NAME_MAP.put(RippleDistortionEffectElement.typeName, Integer.valueOf(R.string.element_ripple_distortion));
        ELEMENT_NAME_MAP.put(RainDropEffectElement.typeName, Integer.valueOf(R.string.element_rain_drop));
        ELEMENT_NAME_MAP.put(DarkFilterEffectElement.typeName, Integer.valueOf(R.string.element_dark_key));
        ELEMENT_NAME_MAP.put(RandomLinesEffectElement.typeName, Integer.valueOf(R.string.element_random_lines));
        ELEMENT_NAME_MAP.put(OldTVEffectElement.typeName, Integer.valueOf(R.string.element_old_tv));
        ELEMENT_NAME_MAP.put(RadialBlurEffectElement.typeName, Integer.valueOf(R.string.element_radial_blur));
        ELEMENT_NAME_MAP.put(LyricsElement.typeName, Integer.valueOf(R.string.element_lyrics));
        ELEMENT_NAME_MAP.put(CircleizeEffectElement.typeName, Integer.valueOf(R.string.element_circleize));
        ELEMENT_NAME_MAP.put(HeatWaveEffectElement.typeName, Integer.valueOf(R.string.element_heat_wave));
        ELEMENT_NAME_MAP.put(FailedSpherifyEffectElement.typeName, Integer.valueOf(R.string.element_Failed_Spherify));
        ELEMENT_LIST_NAME_MAP.put(ParticlesElement.typeName, Integer.valueOf(R.string.element_list_name_particles));
        ELEMENT_LIST_NAME_MAP.put(AudioDataProviderElement.typeName, Integer.valueOf(R.string.element_list_name_audio_source));
        ELEMENT_LIST_NAME_MAP.put("BlurEffect", Integer.valueOf(R.string.element_list_name_blur));
        ELEMENT_LIST_NAME_MAP.put(RgbSplitEffectElement.typeName, Integer.valueOf(R.string.element_list_name_rgb_split));
        ELEMENT_LIST_NAME_MAP.put(MotionBlurEffectElement.typeName, Integer.valueOf(R.string.element_list_name_motion_blur));
        ELEMENT_LIST_NAME_MAP.put(MirrorEffectElement.typeName, Integer.valueOf(R.string.element_list_name_mirror));
        ELEMENT_LIST_NAME_MAP.put(LogoMark2.typeName, Integer.valueOf(R.string.element_list_name_watermark));
        ELEMENT_LIST_NAME_MAP.put(DummyElement.typeName, Integer.valueOf(R.string.element_list_name_custom));
        ELEMENT_LIST_NAME_MAP.put(ColorCorrectionElement.typeName, Integer.valueOf(R.string.element_list_name_color_correction));
        ELEMENT_LIST_NAME_MAP.put(TwirlEffectElement.typeName, Integer.valueOf(R.string.element_list_name_twirl));
        ELEMENT_LIST_NAME_MAP.put(LiquifyEffectElement.typeName, Integer.valueOf(R.string.element_list_name_liquify));
        ELEMENT_LIST_NAME_MAP.put(Kaleidoscope.typeName, Integer.valueOf(R.string.element_list_name_kaleidoscope));
        ELEMENT_LIST_NAME_MAP.put(FOVElement.typeName, Integer.valueOf(R.string.element_list_name_fov));
        ELEMENT_LIST_NAME_MAP.put(ZoomBlurEffectElement.typeName, Integer.valueOf(R.string.element_list_name_zoom_blur));
        ELEMENT_LIST_NAME_MAP.put(PixelEffectElement.typeName, Integer.valueOf(R.string.element_list_name_pixelate));
        ELEMENT_LIST_NAME_MAP.put(Box3DElement.typeName, Integer.valueOf(R.string.element_list_name_cube));
        ELEMENT_LIST_NAME_MAP.put(EdgeEffectElement.typeName, Integer.valueOf(R.string.element_list_name_edge_detection));
        ELEMENT_LIST_NAME_MAP.put(GodraysEffectElement.typeName, Integer.valueOf(R.string.element_list_name_godrays));
        ELEMENT_LIST_NAME_MAP.put(SpherifyEffectElement.typeName, Integer.valueOf(R.string.element_list_name_spherify));
        ELEMENT_LIST_NAME_MAP.put(CurveEffectElement.typeName, Integer.valueOf(R.string.element_list_name_polar_coordinates));
        ELEMENT_LIST_NAME_MAP.put(BulgePinchEffectElement.typeName, Integer.valueOf(R.string.element_list_name_bulge_pinch));
        ELEMENT_LIST_NAME_MAP.put(ImageElement.typeName, Integer.valueOf(R.string.element_list_name_image));
        ELEMENT_LIST_NAME_MAP.put(TextElement.typeName, Integer.valueOf(R.string.element_list_name_text));
        ELEMENT_LIST_NAME_MAP.put("Bars", Integer.valueOf(R.string.element_list_name_bars));
        ELEMENT_LIST_NAME_MAP.put(AfterImageEffectElement.typeName, Integer.valueOf(R.string.element_list_name_afterimage));
        ELEMENT_LIST_NAME_MAP.put(CausticEffectElement.typeName, Integer.valueOf(R.string.element_list_name_caustic));
        ELEMENT_LIST_NAME_MAP.put(CCPowerPinEffectElement.typeName, Integer.valueOf(R.string.element_list_name_corner_pin));
        ELEMENT_LIST_NAME_MAP.put(DepthDisplaceEffectElement.typeName, Integer.valueOf(R.string.element_list_name_depth_displace));
        ELEMENT_LIST_NAME_MAP.put(DissolveEffectElement.typeName, Integer.valueOf(R.string.element_list_name_dissolve));
        ELEMENT_LIST_NAME_MAP.put(Fake3DEffectElement.typeName, Integer.valueOf(R.string.element_list_name_3d_transform));
        ELEMENT_LIST_NAME_MAP.put(FractalNoiseEffectElement.typeName, Integer.valueOf(R.string.element_list_name_fractal_noise));
        ELEMENT_LIST_NAME_MAP.put(GaussianBlurEffectElement.typeName, Integer.valueOf(R.string.element_list_name_gaussian_blur));
        ELEMENT_LIST_NAME_MAP.put(GlitchEffectElement.typeName, Integer.valueOf(R.string.element_list_name_glitch));
        ELEMENT_LIST_NAME_MAP.put(GlowEffectElement.typeName, Integer.valueOf(R.string.element_list_name_glow));
        ELEMENT_LIST_NAME_MAP.put(HalftoneEffectElement.typeName, Integer.valueOf(R.string.element_list_name_halftone));
        ELEMENT_LIST_NAME_MAP.put(HeatDistortionEffectElement.typeName, Integer.valueOf(R.string.element_list_name_heat_distortion));
        ELEMENT_LIST_NAME_MAP.put(MagnifierEffectElement.typeName, Integer.valueOf(R.string.element_list_name_magnifier));
        ELEMENT_LIST_NAME_MAP.put(PixelStretchEffectElement.typeName, Integer.valueOf(R.string.element_list_name_pixel_stretch));
        ELEMENT_LIST_NAME_MAP.put(RoughenEdgesEffectElement.typeName, Integer.valueOf(R.string.element_list_name_roughen_edges));
        ELEMENT_LIST_NAME_MAP.put(SpiralEffectElement.typeName, Integer.valueOf(R.string.element_list_name_spiral));
        ELEMENT_LIST_NAME_MAP.put(VectorBlurEffectElement.typeName, Integer.valueOf(R.string.element_list_name_vector_blur));
        ELEMENT_LIST_NAME_MAP.put(WaterRippleEffectElement.typeName, Integer.valueOf(R.string.element_list_name_water_ripple));
        ELEMENT_LIST_NAME_MAP.put(WaveDistortionEffectElement.typeName, Integer.valueOf(R.string.element_list_name_wave_distortion));
        ELEMENT_LIST_NAME_MAP.put(GradientEffectElement.typeName, Integer.valueOf(R.string.element_list_name_gradient));
        ELEMENT_LIST_NAME_MAP.put(DropShadowEffectElement.typeName, Integer.valueOf(R.string.element_list_name_drop_shadow));
        ELEMENT_LIST_NAME_MAP.put(RippleDistortionEffectElement.typeName, Integer.valueOf(R.string.element_list_name_ripple_distortion));
        ELEMENT_LIST_NAME_MAP.put(RainDropEffectElement.typeName, Integer.valueOf(R.string.element_list_name_rain_drop));
        ELEMENT_LIST_NAME_MAP.put(DarkFilterEffectElement.typeName, Integer.valueOf(R.string.element_list_name_dark_key));
        ELEMENT_LIST_NAME_MAP.put(RandomLinesEffectElement.typeName, Integer.valueOf(R.string.element_list_name_random_lines));
        ELEMENT_LIST_NAME_MAP.put(OldTVEffectElement.typeName, Integer.valueOf(R.string.element_list_name_old_tv));
        ELEMENT_LIST_NAME_MAP.put(RadialBlurEffectElement.typeName, Integer.valueOf(R.string.element_list_name_radial_blur));
        ELEMENT_LIST_NAME_MAP.put(LyricsElement.typeName, Integer.valueOf(R.string.element_list_name_lyrics));
        ELEMENT_LIST_NAME_MAP.put(CircleizeEffectElement.typeName, Integer.valueOf(R.string.element_list_name_circleize));
        ELEMENT_LIST_NAME_MAP.put(HeatWaveEffectElement.typeName, Integer.valueOf(R.string.element_list_name_heat_wave));
        ELEMENT_LIST_NAME_MAP.put(FailedSpherifyEffectElement.typeName, Integer.valueOf(R.string.element_list_name_Failed_Spherify));
    }

    public CustomizeVisView0(CustomizeVisDialog customizeVisDialog) {
        this.customizeVisDialog = customizeVisDialog;
    }

    boolean isViewCreated() {
        return this.btnExpandAdd != null;
    }

    public void onCreatedView0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, final Activity activity, final String[] strArr) {
        View viewInflate;
        this.actionsElements = viewGroup.findViewById(R.id.actionsElements);
        this.expandAddElements = viewGroup.findViewById(R.id.expandAddElements);
        this.expandReset = viewGroup.findViewById(R.id.expandReset);
        ImageButton imageButton = (ImageButton) viewGroup.findViewById(R.id.btnExpandAdd);
        this.btnExpandAdd = imageButton;
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CustomizeVisView0.this.closeReset();
                CustomizeVisView0.this.toggleAddElements();
            }
        });
        ImageButton imageButton2 = (ImageButton) viewGroup.findViewById(R.id.btnExpandReset);
        this.btnExpandReset = imageButton2;
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CustomizeVisView0.this.closeAddElements();
                CustomizeVisView0.this.toggleReset();
            }
        });
        ((Button) viewGroup.findViewById(R.id.btnReset)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CustomizeVisDialog.onResetAction.invoke(new ContextData(activity), Integer.valueOf(CustomizeVisView0.this.customizeVisDialog.rootIdentifier), CustomizeVisView0.this.customizeVisDialog.customizationScene);
            }
        });
        GridLayout gridLayout = (GridLayout) this.expandAddElements;
        this.gridAddElements = gridLayout;
        gridLayout.removeAllViews();
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int iIntValue = ((Integer) view.getTag()).intValue();
                String[] strArr2 = strArr;
                if (iIntValue < strArr2.length) {
                    CustomizeVisDialog.onAddElementAction.invoke(new ContextData(view), Integer.valueOf(CustomizeVisView0.this.customizeVisDialog.rootIdentifier), Integer.valueOf(CustomizeVisView0.this.selectedElementInTree), CustomizeVisView0.this.customizeVisDialog.customizationScene, strArr2[iIntValue]);
                }
            }
        };
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            viewInflate = View.inflate(activity, R.layout.dialog_custom_vis_add_element, null);
            View view = viewInflate;
            Button button = (Button) view.findViewById(R.id.btnElement);
            button.setOnClickListener(onClickListener);
            button.setText(getDisplayName(str));
            button.setTag(Integer.valueOf(i));
            this.gridAddElements.addView(view);
        }
        this.scrollView1 = (ScrollView) viewGroup.findViewById(R.id.scrollView1);
        RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerViewItems);
        this.recyclerViewItems = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        CustomSimpleAdapter customSimpleAdapter = new CustomSimpleAdapter(activity, null, this.scrollView1);
        this.mAdapter = customSimpleAdapter;
        CustomSimpleSectionedAdapter customSimpleSectionedAdapter = new CustomSimpleSectionedAdapter(activity, R.layout.custom_section, 0, customSimpleAdapter, new Func<Boolean>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Func.Func
            public Boolean onInvoke() {
                return Boolean.valueOf(CustomizeVisView0.this.mAdapter.getCollapsed());
            }
        });
        this.mSectionedAdapter = customSimpleSectionedAdapter;
        this.recyclerViewItems.setAdapter(customSimpleSectionedAdapter);
        final DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.btnItemMore);
        dragSortRecycler.setFloatingAlpha(0.4f);
        dragSortRecycler.setFloatingBgColor(activity.getResources().getColor(R.color.white_alpha_1));
        dragSortRecycler.setAutoScrollSpeed(0.3f);
        dragSortRecycler.setAutoScrollWindow(0.1f);
        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.6
            @Override // com.daaw.avee.vendor.dragsort.DragSortRecycler.OnItemMovedListener
            public void onItemMoved(int i2, int i3) {
                CustomizeVisDialog.ThreeEntry threeEntry;
                int iIndexOf;
                int iMin = Math.min(CustomizeVisView0.this.treeElements.size() - 1, Math.max(i3, 1));
                if (i2 != iMin) {
                    if (CustomizeVisView0.this.selectedElementInTree >= 0 && CustomizeVisView0.this.selectedElementInTree < CustomizeVisView0.this.treeElements.size()) {
                        threeEntry = (CustomizeVisDialog.ThreeEntry) CustomizeVisView0.this.treeElements.get(CustomizeVisView0.this.selectedElementInTree);
                    } else {
                        threeEntry = null;
                    }
                    CustomizeVisDialog.ThreeEntry threeEntry2 = (CustomizeVisDialog.ThreeEntry) CustomizeVisView0.this.treeElements.get(i2);
                    CustomizeVisView0.this.treeElements.remove(i2);
                    CustomizeVisView0.this.treeElements.add(iMin, threeEntry2);
                    CustomizeVisView0 customizeVisView0 = CustomizeVisView0.this;
                    if (threeEntry != null) {
                        iIndexOf = customizeVisView0.treeElements.indexOf(threeEntry);
                    } else {
                        iIndexOf = -1;
                    }
                    customizeVisView0.selectedElementInTree = iIndexOf;
                    CustomizeVisView0 customizeVisView1 = CustomizeVisView0.this;
                    customizeVisView1.updateCompositionFromTreeElements(customizeVisView1.treeElements, true);
                }
            }
        });
        this.recyclerViewItems.addItemDecoration(dragSortRecycler);
        this.recyclerViewItems.addOnItemTouchListener(dragSortRecycler);
        this.recyclerViewItems.addOnScrollListener(dragSortRecycler.getScrollListener());
        this.mAdapter.setOnDraggingListener(new CustomSimpleAdapter.IOnDraggingListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.7
            @Override // com.daaw.avee.comp.VisualizerCutomization.CustomSimpleAdapter.IOnDraggingListener
            public void onStartDragging(View view2) {
                dragSortRecycler.StartDragging(view2);
            }
        });
        this.mAdapter.setOnItemClickListener(new CustomSimpleAdapter.IOnItemClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.8
            @Override // com.daaw.avee.comp.VisualizerCutomization.CustomSimpleAdapter.IOnItemClickListener
            public boolean onItemClick(int i2, View view2) {
                CustomizeVisView0.this.customizeVisDialog.goToView(1);
                CustomizeVisView0.this.parsePropertyDataByIndex(i2, true);
                CustomizeVisView0.this.mSectionedAdapter.setSelectedItem(i2, view2);
                return true;
            }
        });
        this.mSectionedAdapter.setOnItemClickListener(new CustomSimpleAdapter.IOnItemClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.9
            @Override // com.daaw.avee.comp.VisualizerCutomization.CustomSimpleAdapter.IOnItemClickListener
            public boolean onItemClick(int i2, View view2) {
                CustomizeVisView0.this.customizeVisDialog.goToView(1);
                CustomizeVisView0.this.parsePropertyDataByIndex(i2, true);
                CustomizeVisView0.this.mSectionedAdapter.setSelectedItem(i2, view2);
                return true;
            }
        });
    }

    private String getDisplayName(String str) {
        Integer num;
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null && (num = ELEMENT_NAME_MAP.get(str)) != null) {
            return mainActivity.getString(num.intValue());
        }
        return str;
    }

    private String getListDisplayName(String str) {
        Integer num;
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null && (num = ELEMENT_LIST_NAME_MAP.get(str)) != null) {
            return mainActivity.getString(num.intValue());
        }
        return str;
    }

    public void rebuildAddableElements() {
        View viewInflate;
        if (this.gridAddElements != null) {
            this.gridAddElements.removeAllViews();
            final String[] addableTypeNamesArray = ElementsFactory.getAddableTypeNamesArray();
            Activity activity = (Activity) this.gridAddElements.getContext();
            if (activity != null) {
                View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        int iIntValue = ((Integer) view.getTag()).intValue();
                        if (iIntValue < addableTypeNamesArray.length) {
                            CustomizeVisDialog.onAddElementAction.invoke(new ContextData(view), Integer.valueOf(CustomizeVisView0.this.customizeVisDialog.rootIdentifier), Integer.valueOf(CustomizeVisView0.this.selectedElementInTree), CustomizeVisView0.this.customizeVisDialog.customizationScene, addableTypeNamesArray[iIntValue]);
                        }
                    }
                };
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 < addableTypeNamesArray.length) {
                        String str = addableTypeNamesArray[i2];
                        viewInflate = View.inflate(activity, R.layout.dialog_custom_vis_add_element, null);
                        View view = viewInflate;
                        Button button = (Button) view.findViewById(R.id.btnElement);
                        button.setOnClickListener(onClickListener);
                        button.setText(getDisplayName(str));
                        button.setTag(Integer.valueOf(i2));
                        this.gridAddElements.addView(view);
                        i = i2 + 1;
                    } else {
                        this.gridAddElements.requestLayout();
                        this.gridAddElements.invalidate();
                        return;
                    }
                }
            }
        }
    }

    public boolean isCollapsed() {
        View view = this.actionsElements;
        boolean z = view == null || view.getVisibility() == 8;
        CustomSimpleAdapter customSimpleAdapter = this.mAdapter;
        return z && (customSimpleAdapter == null || customSimpleAdapter.getCollapsed());
    }

    public void setCollapsed(boolean z) {
        View view = this.actionsElements;
        if (view != null) {
            view.setVisibility(z ? 8 : 0);
        }
        CustomSimpleAdapter customSimpleAdapter = this.mAdapter;
        if (customSimpleAdapter != null) {
            customSimpleAdapter.setCollapsed(z);
        }
        if (z) {
            closeReset();
            closeAddElements();
        }
    }

    void parseCustomizationData(CustomScene customScene) {
        String quantityString;
        if (this.customizeVisDialog.isView1Created() && isViewCreated()) {
            int entryCount = customScene.getEntryCount();
            this.treeElements.clear();
            for (int i = 0; i < entryCount; i++) {
                CustomComposition entry = customScene.getEntry(i);
                if (entry == null) {
                    tlog.w("CustomPropertiesList is null");
                } else {
                    if (i == 0) {
                        quantityString = this.customizeVisDialog.getString(R.string.vis_customization_composition_final);
                    } else {
                        quantityString = this.customizeVisDialog.getResources().getQuantityString(R.plurals.vis_customization_composition_x, i, Integer.valueOf(i));
                    }
                    this.treeElements.add(new CustomizeVisDialog.ThreeEntry(quantityString, entry, entry.getPropertyInt("_id", -1), true));
                    parseCompositionData(entry, this.treeElements);
                }
            }
            setTreeData(this.selectedElementInTree);
        }
    }

    private void parseCompositionData(CustomComposition customComposition, List<CustomizeVisDialog.ThreeEntry> list) {
        int entryCount = customComposition.getEntryCount();
        for (int i = 0; i < entryCount; i++) {
            CustomPropertiesList entry = customComposition.getEntry(i);
            if (entry == null) {
                tlog.w("CustomPropertiesList is null");
            } else {
                list.add(new CustomizeVisDialog.ThreeEntry(getListDisplayName(entry.getTypeName()), entry, entry.getPropertyInt("_id", -1)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleAddElements() {
        if (this.expandAddElements.getVisibility() == 0) {
            closeAddElements();
        } else {
            openAddElements();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeAddElements() {
        this.btnExpandAdd.animate().rotation(0.0f).setDuration(this.customizeVisDialog.shortAnimTime);
        this.expandAddElements.animate().alpha(0.0f).setDuration(this.customizeVisDialog.shortAnimTime).withEndAction(new Runnable() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.11
            @Override // java.lang.Runnable
            public void run() {
                CustomizeVisView0.this.expandAddElements.setVisibility(8);
            }
        });
    }

    private void openAddElements() {
        this.btnExpandAdd.animate().rotation(-45.0f).setDuration(this.customizeVisDialog.shortAnimTime);
        this.expandAddElements.setVisibility(0);
        this.expandAddElements.animate().alpha(1.0f).setDuration(this.customizeVisDialog.shortAnimTime);
        this.scrollView1.post(new Runnable() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.12
            @Override // java.lang.Runnable
            public void run() {
                CustomizeVisView0.this.scrollView1.fullScroll(TsExtractor.TS_STREAM_TYPE_HDMV_DTS);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleReset() {
        if (this.expandReset.getVisibility() == 0) {
            closeReset();
        } else {
            openReset();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeReset() {
        this.btnExpandReset.animate().rotation(0.0f).setDuration(this.customizeVisDialog.shortAnimTime);
        this.expandReset.animate().alpha(0.0f).setDuration(this.customizeVisDialog.shortAnimTime).withEndAction(new Runnable() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.13
            @Override // java.lang.Runnable
            public void run() {
                CustomizeVisView0.this.expandReset.setVisibility(8);
            }
        });
    }

    private void openReset() {
        this.btnExpandReset.animate().rotation(-45.0f).setDuration(this.customizeVisDialog.shortAnimTime);
        this.expandReset.setVisibility(0);
        this.expandReset.animate().alpha(1.0f).setDuration(this.customizeVisDialog.shortAnimTime);
        this.scrollView1.post(new Runnable() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView0.14
            @Override // java.lang.Runnable
            public void run() {
                CustomizeVisView0.this.scrollView1.fullScroll(TsExtractor.TS_STREAM_TYPE_HDMV_DTS);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCompositionFromTreeElements(List<CustomizeVisDialog.ThreeEntry> list, boolean z) {
        rebuildSceneFromTreeData(this.customizeVisDialog.customizationScene, list);
        setTreeData(this.selectedElementInTree);
        this.customizeVisDialog.onCompositionChanged(z);
    }

    private void updateCompositionFromScene(CustomScene customScene, boolean z) {
        parseCustomizationData(customScene);
        this.customizeVisDialog.onCompositionChanged(z);
    }

    void rebuildSceneFromTreeData(CustomScene customScene, List<CustomizeVisDialog.ThreeEntry> list) {
        int i;
        customScene.removeAllEntries();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        CustomComposition customComposition = null;
        while (i4 < list.size()) {
            CustomizeVisDialog.ThreeEntry threeEntry = list.get(i4);
            CustomComposition composition = threeEntry.getComposition();
            CustomPropertiesList element = threeEntry.getElement();
            if (composition != null) {
                composition.removeAllEntries();
                customScene.setEntry(i2, composition);
                customComposition = composition;
                i = i2 + 1;
                i3 = 0;
            } else if (element == null || customComposition == null) {
                i = i2;
            } else {
                customComposition.setEntry(i3, element);
                i3++;
                i = i2;
            }
            i4++;
            i2 = i;
        }
    }

    void updateSelectedItemThreeView(int i) {
        if (i >= 0 && i < this.treeElements.size()) {
            CustomizeVisDialog.ThreeEntry threeEntry = this.treeElements.get(i);
            this.mSectionedAdapter.baseAdapterSetDataAt(i, new Tuple2<>(threeEntry.name, threeEntry.getSubName()));
        }
    }

    void setTreeData(int i) {
        setTreeData(this.treeElements, i);
    }

    void setTreeData(List<CustomizeVisDialog.ThreeEntry> list, int i) {
        if (this.mAdapter != null) {
            ArrayList arrayList = new ArrayList(list.size());
            ArrayList arrayList2 = new ArrayList(list.size());
            for (int i2 = 0; i2 < list.size(); i2++) {
                CustomizeVisDialog.ThreeEntry threeEntry = list.get(i2);
                if (threeEntry.isSection()) {
                    arrayList2.add(new CustomSimpleSectionedAdapter.Section(arrayList.size(), threeEntry.name));
                } else {
                    arrayList.add(new Tuple2(threeEntry.name, threeEntry.getSubName()));
                }
            }
            this.mAdapter.setAllData(arrayList);
            this.mSectionedAdapter.setSections(arrayList2);
            this.mSectionedAdapter.setSelectedItem(i);
            this.customizeVisDialog.onSelectedElementChanged(this.selectedElementInTree, false);
        }
    }

    public void removeTreeItem(int i) {
        if (this.customizeVisDialog.isView1Created() && isViewCreated()) {
            if (i < this.treeElements.size()) {
                this.treeElements.remove(i);
                updateCompositionFromTreeElements(this.treeElements, false);
            }
            int i2 = this.selectedElementInTree;
            if (i == i2) {
                parsePropertyDataByIndex(-1);
            } else {
                parsePropertyDataByIndex(i2);
            }
        }
    }

    public CustomizeVisDialog.ThreeEntry getThreeItem(int i) {
        if (i < 0 || i >= this.treeElements.size()) {
            return null;
        }
        return this.treeElements.get(i);
    }

    void parsePropertyDataByIndex(int i) {
        parsePropertyDataByIndex(i, false);
    }

    void parsePropertyDataByIndex(int i, boolean z) {
        this.selectedElementInTree = i;
        this.customizeVisDialog.onSelectedElementChanged(i, z);
        CustomSimpleSectionedAdapter customSimpleSectionedAdapter = this.mSectionedAdapter;
        if (customSimpleSectionedAdapter != null) {
            customSimpleSectionedAdapter.setSelectedItem(i);
        }
        if (i >= 0 && i < this.treeElements.size()) {
            CustomizeVisDialog.ThreeEntry threeEntry = this.treeElements.get(i);
            CustomPropertiesList element = threeEntry.getElement();
            CustomComposition composition = threeEntry.getComposition();
            if (element != null) {
                this.customizeVisDialog.parsePropertyData(element);
                return;
            } else {
                this.customizeVisDialog.parsePropertyData(composition);
                return;
            }
        }
        this.customizeVisDialog.parsePropertyData(null);
    }

    public void selectAndScrollToLast() {
        if (this.customizeVisDialog.isView1Created() && isViewCreated() && this.treeElements.size() > 0) {
            this.recyclerViewItems.scrollToPosition(this.treeElements.size() - 1);
            parsePropertyDataByIndex(this.treeElements.size() - 1);
        }
    }

    public void selectAndScrollToLast(int i) {
        if (this.customizeVisDialog.isView1Created() && isViewCreated()) {
            int i2 = -1;
            int i3 = -1;
            for (int i4 = 0; i4 < this.treeElements.size(); i4++) {
                if (this.treeElements.get(i4).getComposition() != null) {
                    i2++;
                }
                if (i2 > i) {
                    break;
                }
                i3 = i4;
            }
            if (i3 > 0 && this.treeElements.size() > i3) {
                this.recyclerViewItems.scrollToPosition(i3);
                parsePropertyDataByIndex(i3);
            }
        }
    }
}
