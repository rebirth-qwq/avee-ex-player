package com.daaw.avee.comp.VisualizerCutomization;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.Func.Func;
import com.daaw.avee.Common.MultiList;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.Design.AppDesign;
import com.daaw.avee.EventsGlobal.EventsGlobalTextNotifier;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.AlbumArt.InternalBitmapLoader;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Common.View.SimpleViewProgressBar;
import com.daaw.avee.comp.Common.VisualizerThemeInfo;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.AnchorMode;
import com.daaw.avee.comp.Visualizer.Elements.Base.LocalFontLoader;
import com.daaw.avee.comp.Visualizer.Elements.Composition;
import com.daaw.avee.comp.Visualizer.Elements.DissolveEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.Fake3DEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GlitchEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GlowEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.GradientEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.HalftoneEffectElement;
import com.daaw.avee.comp.Visualizer.Elements.ImageElement;
import com.daaw.avee.comp.Visualizer.Elements.LyricsElement;
import com.daaw.avee.comp.Visualizer.Elements.Particles.AreaRect;
import com.daaw.avee.comp.Visualizer.Elements.Particles.VortexForceField;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioSpectrumData;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioSpectrumDataPhaseS;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioWaveformData;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentPathCircle;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentPathSided;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentRendererRoundBar;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentRendererSharpBar;
import com.daaw.avee.comp.Visualizer.Elements.TextElement;
import com.daaw.avee.comp.Visualizer.Graphic.BlendMode;
import com.daaw.avee.comp.Visualizer.Graphic.GraphicsUtils;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.AlphaSlider;
import com.flask.colorpicker.slider.LightnessSlider;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class CustomizeVisView1 {
    private static final Map<String, String> DISPLAY_NAME_MAP = new HashMap();
    private static HashMap<String, Integer> sResIdMap;
    private CustomPropertiesList currentElementCustom;
    private CustomizeVisDialog customizeVisDialog;
    private ViewGroup linearLayoutRootContent;
    private Spinner spinnerVersion;
    private AdapterView.OnItemSelectedListener spinnerVersionOnItemClickListener;
    private TextView txtElementTitle;
    private View view1RootView;
    int spinnerVersionInitalSelectedPosition = -1;
    private Map<String, Boolean> propertiesExpandViews = new HashMap();
    private Map<String, Action1<Object>> propertiesUpdateValueCb = new HashMap();

    static {
        DISPLAY_NAME_MAP.put("1layer Color", String.valueOf(R.string.prop_1layer_color));
        DISPLAY_NAME_MAP.put("1layer Scale", String.valueOf(R.string.prop_1layer_scale));
        DISPLAY_NAME_MAP.put("2layer Color", String.valueOf(R.string.prop_2layer_color));
        DISPLAY_NAME_MAP.put("2layer Scale", String.valueOf(R.string.prop_2layer_scale));
        DISPLAY_NAME_MAP.put("3layer Color", String.valueOf(R.string.prop_3layer_color));
        DISPLAY_NAME_MAP.put("3layer Scale", String.valueOf(R.string.prop_3layer_scale));
        DISPLAY_NAME_MAP.put("A (Amount)", String.valueOf(R.string.prop_a_amount));
        DISPLAY_NAME_MAP.put("A (Speed)", String.valueOf(R.string.prop_a_speed));
        DISPLAY_NAME_MAP.put("A (Threshold)", String.valueOf(R.string.prop_a_threshold));
        DISPLAY_NAME_MAP.put("A (Trigger Time)", String.valueOf(R.string.prop_a_trigger_time));
        DISPLAY_NAME_MAP.put("A (X Amount)", String.valueOf(R.string.prop_a_x_amount));
        DISPLAY_NAME_MAP.put("A (X Speed)", String.valueOf(R.string.prop_a_x_speed));
        DISPLAY_NAME_MAP.put("AfterImageEffect", String.valueOf(R.string.prop_afterimage_effect));
        DISPLAY_NAME_MAP.put("Align Position", String.valueOf(R.string.prop_align_position));
        DISPLAY_NAME_MAP.put("Alignment Position", String.valueOf(R.string.prop_alignment_position));
        DISPLAY_NAME_MAP.put("Amount", String.valueOf(R.string.prop_amount));
        DISPLAY_NAME_MAP.put("Amplitude", String.valueOf(R.string.prop_amplitude));
        DISPLAY_NAME_MAP.put("Analog", String.valueOf(R.string.prop_analog));
        DISPLAY_NAME_MAP.put("Anchor X", String.valueOf(R.string.prop_anchor_x));
        DISPLAY_NAME_MAP.put("Anchor Y", String.valueOf(R.string.prop_anchor_y));
        DISPLAY_NAME_MAP.put("Angle", String.valueOf(R.string.prop_angle));
        DISPLAY_NAME_MAP.put("Animation Speed", String.valueOf(R.string.prop_animation_speed));
        DISPLAY_NAME_MAP.put("Appearance", String.valueOf(R.string.prop_appearance));
        DISPLAY_NAME_MAP.put("Aspect", String.valueOf(R.string.prop_aspect));
        DISPLAY_NAME_MAP.put("Aspect Ratio", String.valueOf(R.string.prop_aspect_ratio));
        DISPLAY_NAME_MAP.put("Audio Capture", String.valueOf(R.string.prop_audio_capture));
        DISPLAY_NAME_MAP.put("Audio Duration", String.valueOf(R.string.prop_audio_duration));
        DISPLAY_NAME_MAP.put("Audio Duration Ms", String.valueOf(R.string.prop_audio_duration_ms));
        DISPLAY_NAME_MAP.put("Audio Provider", String.valueOf(R.string.prop_audio_provider));
        DISPLAY_NAME_MAP.put("Audio Provider Index", String.valueOf(R.string.prop_audio_provider_index));
        DISPLAY_NAME_MAP.put("Auto Detect Color Key", String.valueOf(R.string.prop_auto_detect_color_key));
        DISPLAY_NAME_MAP.put("AWeight", String.valueOf(R.string.prop_aweight));
        DISPLAY_NAME_MAP.put("B (Amount)", String.valueOf(R.string.prop_b_amount));
        DISPLAY_NAME_MAP.put("B (Beat Amount)", String.valueOf(R.string.prop_b_beat_amount));
        DISPLAY_NAME_MAP.put("B (Range)", String.valueOf(R.string.prop_b_range));
        DISPLAY_NAME_MAP.put("B (Scale)", String.valueOf(R.string.prop_b_scale));
        DISPLAY_NAME_MAP.put("B (Speed)", String.valueOf(R.string.prop_b_speed));
        DISPLAY_NAME_MAP.put("B (Y Amount)", String.valueOf(R.string.prop_b_y_amount));
        DISPLAY_NAME_MAP.put("B (Y Speed)", String.valueOf(R.string.prop_b_y_speed));
        DISPLAY_NAME_MAP.put("Bad Block", String.valueOf(R.string.prop_bad_block));
        DISPLAY_NAME_MAP.put("Bad Block Intensity", String.valueOf(R.string.prop_bad_block_intensity));
        DISPLAY_NAME_MAP.put("Bad Block Size", String.valueOf(R.string.prop_bad_block_size));
        DISPLAY_NAME_MAP.put("Bad Pixel", String.valueOf(R.string.prop_bad_pixel));
        DISPLAY_NAME_MAP.put("Bad Pixel Density", String.valueOf(R.string.prop_bad_pixel_density));
        DISPLAY_NAME_MAP.put("Bad Pixel Intensity", String.valueOf(R.string.prop_bad_pixel_intensity));
        DISPLAY_NAME_MAP.put("Bar Height Mult", String.valueOf(R.string.prop_bar_height_mult));
        DISPLAY_NAME_MAP.put("Bar Height Multiplier", String.valueOf(R.string.prop_bar_height_multiplier));
        DISPLAY_NAME_MAP.put("Bar Width", String.valueOf(R.string.prop_bar_width));
        DISPLAY_NAME_MAP.put("Bar Width Affected By Shape", String.valueOf(R.string.prop_bar_width_affected_by_shape));
        DISPLAY_NAME_MAP.put("Bars", String.valueOf(R.string.prop_bars));
        DISPLAY_NAME_MAP.put(MeasureDefs.beat, String.valueOf(R.string.prop_beat));
        DISPLAY_NAME_MAP.put("Beat Range", String.valueOf(R.string.prop_beat_range));
        DISPLAY_NAME_MAP.put("Beat Range Bar First", String.valueOf(R.string.prop_beat_range_bar_first));
        DISPLAY_NAME_MAP.put("Beat Range Bar Last", String.valueOf(R.string.prop_beat_range_bar_last));
        DISPLAY_NAME_MAP.put("Beat Range Value Higher", String.valueOf(R.string.prop_beat_range_value_higher));
        DISPLAY_NAME_MAP.put("Beat Range Value Lower", String.valueOf(R.string.prop_beat_range_value_lower));
        DISPLAY_NAME_MAP.put("Beat Smooth", String.valueOf(R.string.prop_beat_smooth));
        DISPLAY_NAME_MAP.put("BeatAmount", String.valueOf(R.string.prop_beatamount));
        DISPLAY_NAME_MAP.put("Behavior", String.valueOf(R.string.prop_behavior));
        DISPLAY_NAME_MAP.put("Behaviour", String.valueOf(R.string.prop_behaviour));
        DISPLAY_NAME_MAP.put("Bend", String.valueOf(R.string.prop_bend));
        DISPLAY_NAME_MAP.put("Bend Glitch Intensity", String.valueOf(R.string.prop_bend_glitch_intensity));
        DISPLAY_NAME_MAP.put("Black", String.valueOf(R.string.prop_black));
        DISPLAY_NAME_MAP.put("Bl X", String.valueOf(R.string.prop_bl_x));
        DISPLAY_NAME_MAP.put("Bl Y", String.valueOf(R.string.prop_bl_y));
        DISPLAY_NAME_MAP.put("Blend", String.valueOf(R.string.prop_blend));
        DISPLAY_NAME_MAP.put("Blend Mode", String.valueOf(R.string.prop_blend_mode));
        DISPLAY_NAME_MAP.put("Blend Mode Content", String.valueOf(R.string.prop_blend_mode_content));
        DISPLAY_NAME_MAP.put("Blend Original", String.valueOf(R.string.prop_blend_original));
        DISPLAY_NAME_MAP.put("Blobs", String.valueOf(R.string.prop_blobs));
        DISPLAY_NAME_MAP.put("Blur", String.valueOf(R.string.prop_blur));
        DISPLAY_NAME_MAP.put("Blur Amount Multiplier", String.valueOf(R.string.prop_blur_amount_multiplier));
        DISPLAY_NAME_MAP.put("Blur Divider", String.valueOf(R.string.prop_blur_divider));
        DISPLAY_NAME_MAP.put("Blur Enabled", String.valueOf(R.string.prop_blur_enabled));
        DISPLAY_NAME_MAP.put("Blur Multiplier", String.valueOf(R.string.prop_blur_multiplier));
        DISPLAY_NAME_MAP.put("Blur Radius", String.valueOf(R.string.prop_blur_radius));
        DISPLAY_NAME_MAP.put("Blurred Border", String.valueOf(R.string.prop_blurred_border));
        DISPLAY_NAME_MAP.put("Br X", String.valueOf(R.string.prop_br_x));
        DISPLAY_NAME_MAP.put("Br Y", String.valueOf(R.string.prop_br_y));
        DISPLAY_NAME_MAP.put("Brightness", String.valueOf(R.string.prop_brightness));
        DISPLAY_NAME_MAP.put("Cam Dist", String.valueOf(R.string.prop_cam_dist));
        DISPLAY_NAME_MAP.put("Cam Pos X", String.valueOf(R.string.prop_cam_pos_x));
        DISPLAY_NAME_MAP.put("Cam Pos Y", String.valueOf(R.string.prop_cam_pos_y));
        DISPLAY_NAME_MAP.put("Cam Pos Z", String.valueOf(R.string.prop_cam_pos_z));
        DISPLAY_NAME_MAP.put("Cam Rot X", String.valueOf(R.string.prop_cam_rot_x));
        DISPLAY_NAME_MAP.put("Cam Rot Y", String.valueOf(R.string.prop_cam_rot_y));
        DISPLAY_NAME_MAP.put("Cam Rot Z", String.valueOf(R.string.prop_cam_rot_z));
        DISPLAY_NAME_MAP.put("Cam Target X", String.valueOf(R.string.prop_cam_target_x));
        DISPLAY_NAME_MAP.put("Cam Target Y", String.valueOf(R.string.prop_cam_target_y));
        DISPLAY_NAME_MAP.put("Cam Target Z", String.valueOf(R.string.prop_cam_target_z));
        DISPLAY_NAME_MAP.put("Camera", String.valueOf(R.string.prop_camera));
        DISPLAY_NAME_MAP.put("Camera Distance", String.valueOf(R.string.prop_camera_distance));
        DISPLAY_NAME_MAP.put("Camera Fov", String.valueOf(R.string.prop_camera_fov));
        DISPLAY_NAME_MAP.put("Camera Pos X", String.valueOf(R.string.prop_camera_pos_x));
        DISPLAY_NAME_MAP.put("Camera Pos Y", String.valueOf(R.string.prop_camera_pos_y));
        DISPLAY_NAME_MAP.put("Camera Pos Z", String.valueOf(R.string.prop_camera_pos_z));
        DISPLAY_NAME_MAP.put("Camera Target X", String.valueOf(R.string.prop_camera_target_x));
        DISPLAY_NAME_MAP.put("Camera Target Y", String.valueOf(R.string.prop_camera_target_y));
        DISPLAY_NAME_MAP.put("Camera Target Z", String.valueOf(R.string.prop_camera_target_z));
        DISPLAY_NAME_MAP.put("CausticEffect", String.valueOf(R.string.prop_caustic_effect));
        DISPLAY_NAME_MAP.put("Center", String.valueOf(R.string.prop_center));
        DISPLAY_NAME_MAP.put("Center X", String.valueOf(R.string.prop_center_x));
        DISPLAY_NAME_MAP.put("Center Y", String.valueOf(R.string.prop_center_y));
        DISPLAY_NAME_MAP.put("Channel", String.valueOf(R.string.prop_channel));
        DISPLAY_NAME_MAP.put("Chromatic", String.valueOf(R.string.prop_chromatic));
        DISPLAY_NAME_MAP.put("ChromaticAberration", String.valueOf(R.string.prop_chromatic_aberration));
        DISPLAY_NAME_MAP.put("ChromaticWave", String.valueOf(R.string.prop_chromatic_wave));
        DISPLAY_NAME_MAP.put("Collapse", String.valueOf(R.string.prop_collapse));
        DISPLAY_NAME_MAP.put("Collapse Intensity", String.valueOf(R.string.prop_collapse_intensity));
        DISPLAY_NAME_MAP.put("Color", String.valueOf(R.string.prop_color));
        DISPLAY_NAME_MAP.put("Color B", String.valueOf(R.string.prop_color_b));
        DISPLAY_NAME_MAP.put("Color Banding", String.valueOf(R.string.prop_color_banding));
        DISPLAY_NAME_MAP.put("Color Banding Intensity", String.valueOf(R.string.prop_color_banding_intensity));
        DISPLAY_NAME_MAP.put("Color Bleed", String.valueOf(R.string.prop_color_bleed));
        DISPLAY_NAME_MAP.put("Color Bleed Direction", String.valueOf(R.string.prop_color_bleed_direction));
        DISPLAY_NAME_MAP.put("Color Bleed Intensity", String.valueOf(R.string.prop_color_bleed_intensity));
        DISPLAY_NAME_MAP.put("Color Blend Offset", String.valueOf(R.string.prop_color_blend_offset));
        DISPLAY_NAME_MAP.put("Color Edge", String.valueOf(R.string.prop_color_edge));
        DISPLAY_NAME_MAP.put("Color Fragment", String.valueOf(R.string.prop_color_fragment));
        DISPLAY_NAME_MAP.put("Color Fragment Intensity", String.valueOf(R.string.prop_color_fragment_intensity));
        DISPLAY_NAME_MAP.put("Color From", String.valueOf(R.string.prop_color_from));
        DISPLAY_NAME_MAP.put("Color G", String.valueOf(R.string.prop_color_g));
        DISPLAY_NAME_MAP.put("Color Invert", String.valueOf(R.string.prop_color_invert));
        DISPLAY_NAME_MAP.put("Color Invert Intensity", String.valueOf(R.string.prop_color_invert_intensity));
        DISPLAY_NAME_MAP.put("Color Key", String.valueOf(R.string.prop_color_key));
        DISPLAY_NAME_MAP.put("Color Key Enabled", String.valueOf(R.string.prop_color_key_enabled));
        DISPLAY_NAME_MAP.put("Color Loss B", String.valueOf(R.string.prop_color_loss_b));
        DISPLAY_NAME_MAP.put("Color Loss BIntensity", String.valueOf(R.string.prop_color_loss_bintensity));
        DISPLAY_NAME_MAP.put("Color Loss G", String.valueOf(R.string.prop_color_loss_g));
        DISPLAY_NAME_MAP.put("Color Loss GIntensity", String.valueOf(R.string.prop_color_loss_gintensity));
        DISPLAY_NAME_MAP.put("Color Loss R", String.valueOf(R.string.prop_color_loss_r));
        DISPLAY_NAME_MAP.put("Color Loss RIntensity", String.valueOf(R.string.prop_color_loss_rintensity));
        DISPLAY_NAME_MAP.put("Color Noise", String.valueOf(R.string.prop_color_noise));
        DISPLAY_NAME_MAP.put("Color Noise Density", String.valueOf(R.string.prop_color_noise_density));
        DISPLAY_NAME_MAP.put("Color Noise Intensity", String.valueOf(R.string.prop_color_noise_intensity));
        DISPLAY_NAME_MAP.put("Color Overflow", String.valueOf(R.string.prop_color_overflow));
        DISPLAY_NAME_MAP.put("Color Overflow Intensity", String.valueOf(R.string.prop_color_overflow_intensity));
        DISPLAY_NAME_MAP.put("Color R", String.valueOf(R.string.prop_color_r));
        DISPLAY_NAME_MAP.put("Color Shift Intensity", String.valueOf(R.string.prop_color_shift_intensity));
        DISPLAY_NAME_MAP.put("Color Solarize", String.valueOf(R.string.prop_color_solarize));
        DISPLAY_NAME_MAP.put("Color Solarize Intensity", String.valueOf(R.string.prop_color_solarize_intensity));
        DISPLAY_NAME_MAP.put("Color Solarize Threshold", String.valueOf(R.string.prop_color_solarize_threshold));
        DISPLAY_NAME_MAP.put("Color Stripe", String.valueOf(R.string.prop_color_stripe));
        DISPLAY_NAME_MAP.put("Color Stripe Evolution", String.valueOf(R.string.prop_color_stripe_evolution));
        DISPLAY_NAME_MAP.put("Color Strip Intensity", String.valueOf(R.string.prop_color_strip_intensity));
        DISPLAY_NAME_MAP.put("Color To", String.valueOf(R.string.prop_color_to));
        DISPLAY_NAME_MAP.put("Color Trail", String.valueOf(R.string.prop_color_trail));
        DISPLAY_NAME_MAP.put("Color Trail Intensity", String.valueOf(R.string.prop_color_trail_intensity));
        DISPLAY_NAME_MAP.put("Color Wave", String.valueOf(R.string.prop_color_wave));
        DISPLAY_NAME_MAP.put("Color Wave Evolution", String.valueOf(R.string.prop_color_wave_evolution));
        DISPLAY_NAME_MAP.put("Color Wave Intensity", String.valueOf(R.string.prop_color_wave_intensity));
        DISPLAY_NAME_MAP.put("Colorize", String.valueOf(R.string.prop_colorize));
        DISPLAY_NAME_MAP.put("Complexity", String.valueOf(R.string.prop_complexity));
        DISPLAY_NAME_MAP.put("Contrast", String.valueOf(R.string.prop_contrast));
        DISPLAY_NAME_MAP.put("Control", String.valueOf(R.string.prop_control));
        DISPLAY_NAME_MAP.put("Control Cam Rot", String.valueOf(R.string.prop_control_cam_rot));
        DISPLAY_NAME_MAP.put("Control Cube Rot", String.valueOf(R.string.prop_control_cube_rot));
        DISPLAY_NAME_MAP.put("Core", String.valueOf(R.string.prop_core));
        DISPLAY_NAME_MAP.put("Corner Radius", String.valueOf(R.string.prop_corner_radius));
        DISPLAY_NAME_MAP.put("Count Limit", String.valueOf(R.string.prop_count_limit));
        DISPLAY_NAME_MAP.put("CRT Burn", String.valueOf(R.string.prop_crt_burn));
        DISPLAY_NAME_MAP.put("CRT Burn Intensity", String.valueOf(R.string.prop_crt_burn_intensity));
        DISPLAY_NAME_MAP.put("CRT Flicker", String.valueOf(R.string.prop_crt_flicker));
        DISPLAY_NAME_MAP.put("CRT Flicker Intensity", String.valueOf(R.string.prop_crt_flicker_intensity));
        DISPLAY_NAME_MAP.put("CRT Scanline", String.valueOf(R.string.prop_crt_scanline));
        DISPLAY_NAME_MAP.put("CRT Scanline Intensity", String.valueOf(R.string.prop_crt_scanline_intensity));
        DISPLAY_NAME_MAP.put("Crt Burn Intensity", String.valueOf(R.string.prop_crt_burn_intensity_alt));
        DISPLAY_NAME_MAP.put("Crt Flicker Intensity", String.valueOf(R.string.prop_crt_flicker_intensity_alt));
        DISPLAY_NAME_MAP.put("Crt Scanline Intensity", String.valueOf(R.string.prop_crt_scanline_intensity_alt));
        DISPLAY_NAME_MAP.put("Cube", String.valueOf(R.string.prop_cube));
        DISPLAY_NAME_MAP.put("Cube Rot X", String.valueOf(R.string.prop_cube_rot_x));
        DISPLAY_NAME_MAP.put("Cube Rot Y", String.valueOf(R.string.prop_cube_rot_y));
        DISPLAY_NAME_MAP.put("Cube Rot Z", String.valueOf(R.string.prop_cube_rot_z));
        DISPLAY_NAME_MAP.put("Cube Texture Scale", String.valueOf(R.string.prop_cube_texture_scale));
        DISPLAY_NAME_MAP.put("Cube Trans X", String.valueOf(R.string.prop_cube_trans_x));
        DISPLAY_NAME_MAP.put("Cube Trans Y", String.valueOf(R.string.prop_cube_trans_y));
        DISPLAY_NAME_MAP.put("Cube Trans Z", String.valueOf(R.string.prop_cube_trans_z));
        DISPLAY_NAME_MAP.put("Curvature", String.valueOf(R.string.prop_curvature));
        DISPLAY_NAME_MAP.put("Custom Image", String.valueOf(R.string.prop_custom_image));
        DISPLAY_NAME_MAP.put("Cyan", String.valueOf(R.string.prop_cyan));
        DISPLAY_NAME_MAP.put("Data", String.valueOf(R.string.prop_data));
        DISPLAY_NAME_MAP.put("Data Confuse", String.valueOf(R.string.prop_data_confuse));
        DISPLAY_NAME_MAP.put("Data Confuse Intensity", String.valueOf(R.string.prop_data_confuse_intensity));
        DISPLAY_NAME_MAP.put("Data Corruption", String.valueOf(R.string.prop_data_corruption));
        DISPLAY_NAME_MAP.put("Data Corruption Intensity", String.valueOf(R.string.prop_data_corruption_intensity));
        DISPLAY_NAME_MAP.put("Data Corruption Prob", String.valueOf(R.string.prop_data_corruption_prob));
        DISPLAY_NAME_MAP.put("Data Corruption Probability", String.valueOf(R.string.prop_data_corruption_probability));
        DISPLAY_NAME_MAP.put("Data Corruption Size", String.valueOf(R.string.prop_data_corruption_size));
        DISPLAY_NAME_MAP.put("Data Fragment", String.valueOf(R.string.prop_data_fragment));
        DISPLAY_NAME_MAP.put("Data Fragment Intensity", String.valueOf(R.string.prop_data_fragment_intensity));
        DISPLAY_NAME_MAP.put("Data Fragment Size", String.valueOf(R.string.prop_data_fragment_size));
        DISPLAY_NAME_MAP.put("Data Garbled", String.valueOf(R.string.prop_data_garbled));
        DISPLAY_NAME_MAP.put("Data Garbled Intensity", String.valueOf(R.string.prop_data_garbled_intensity));
        DISPLAY_NAME_MAP.put("Data Jump", String.valueOf(R.string.prop_data_jump));
        DISPLAY_NAME_MAP.put("Data Jump Intensity", String.valueOf(R.string.prop_data_jump_intensity));
        DISPLAY_NAME_MAP.put("Data Loop", String.valueOf(R.string.prop_data_loop));
        DISPLAY_NAME_MAP.put("Data Loop Intensity", String.valueOf(R.string.prop_data_loop_intensity));
        DISPLAY_NAME_MAP.put("Data Loop Size", String.valueOf(R.string.prop_data_loop_size));
        DISPLAY_NAME_MAP.put("Data Loss", String.valueOf(R.string.prop_data_loss));
        DISPLAY_NAME_MAP.put("Data Loss Intensity", String.valueOf(R.string.prop_data_loss_intensity));
        DISPLAY_NAME_MAP.put("Data Loss Prob", String.valueOf(R.string.prop_data_loss_prob));
        DISPLAY_NAME_MAP.put("Data Loss Probability", String.valueOf(R.string.prop_data_loss_probability));
        DISPLAY_NAME_MAP.put("Data Overflow", String.valueOf(R.string.prop_data_overflow));
        DISPLAY_NAME_MAP.put("Data Overflow Intensity", String.valueOf(R.string.prop_data_overflow_intensity));
        DISPLAY_NAME_MAP.put("Data Repeat", String.valueOf(R.string.prop_data_repeat));
        DISPLAY_NAME_MAP.put("Data Repeat Intensity", String.valueOf(R.string.prop_data_repeat_intensity));
        DISPLAY_NAME_MAP.put("Data Shift", String.valueOf(R.string.prop_data_shift));
        DISPLAY_NAME_MAP.put("Data Shift Density", String.valueOf(R.string.prop_data_shift_density));
        DISPLAY_NAME_MAP.put("Data Shift Intensity", String.valueOf(R.string.prop_data_shift_intensity));
        DISPLAY_NAME_MAP.put("Data Truncate", String.valueOf(R.string.prop_data_truncate));
        DISPLAY_NAME_MAP.put("Data Truncate Intensity", String.valueOf(R.string.prop_data_truncate_intensity));
        DISPLAY_NAME_MAP.put("Decay", String.valueOf(R.string.prop_decay));
        DISPLAY_NAME_MAP.put("Density", String.valueOf(R.string.prop_density));
        DISPLAY_NAME_MAP.put("Depth", String.valueOf(R.string.prop_depth));
        DISPLAY_NAME_MAP.put("Depth Strength", String.valueOf(R.string.prop_depth_strength));
        DISPLAY_NAME_MAP.put("DepthDisplaceEffect", String.valueOf(R.string.prop_depth_displace_effect));
        DISPLAY_NAME_MAP.put("Direction", String.valueOf(R.string.prop_direction));
        DISPLAY_NAME_MAP.put(DissolveEffectElement.typeName, String.valueOf(R.string.prop_dissolve));
        DISPLAY_NAME_MAP.put("Dissolve Effect", String.valueOf(R.string.prop_dissolve_effect));
        DISPLAY_NAME_MAP.put("Dissolve Intensity", String.valueOf(R.string.prop_dissolve_intensity));
        DISPLAY_NAME_MAP.put("Dissolve Evolution", String.valueOf(R.string.prop_dissolve_evolution));
        DISPLAY_NAME_MAP.put("Distortion", String.valueOf(R.string.prop_distortion));
        DISPLAY_NAME_MAP.put("Edge", String.valueOf(R.string.prop_edge));
        DISPLAY_NAME_MAP.put("Edge B", String.valueOf(R.string.prop_edge_b));
        DISPLAY_NAME_MAP.put("Edge Block", String.valueOf(R.string.prop_edge_block));
        DISPLAY_NAME_MAP.put("Edge Block Intensity", String.valueOf(R.string.prop_edge_block_intensity));
        DISPLAY_NAME_MAP.put("Edge Blur", String.valueOf(R.string.prop_edge_blur));
        DISPLAY_NAME_MAP.put("Edge Blur Intensity", String.valueOf(R.string.prop_edge_blur_intensity));
        DISPLAY_NAME_MAP.put("Edge Dissolve", String.valueOf(R.string.prop_edge_dissolve));
        DISPLAY_NAME_MAP.put("Edge Dissolve Intensity", String.valueOf(R.string.prop_edge_dissolve_intensity));
        DISPLAY_NAME_MAP.put("Edge Distort", String.valueOf(R.string.prop_edge_distort));
        DISPLAY_NAME_MAP.put("Edge Flicker", String.valueOf(R.string.prop_edge_flicker));
        DISPLAY_NAME_MAP.put("Edge Flicker Intensity", String.valueOf(R.string.prop_edge_flicker_intensity));
        DISPLAY_NAME_MAP.put("Edge Fringe", String.valueOf(R.string.prop_edge_fringe));
        DISPLAY_NAME_MAP.put("Edge Fringe Intensity", String.valueOf(R.string.prop_edge_fringe_intensity));
        DISPLAY_NAME_MAP.put("Edge G", String.valueOf(R.string.prop_edge_g));
        DISPLAY_NAME_MAP.put("Edge Glow", String.valueOf(R.string.prop_edge_glow));
        DISPLAY_NAME_MAP.put("Edge Glow Color B", String.valueOf(R.string.prop_edge_glow_color_b));
        DISPLAY_NAME_MAP.put("Edge Glow Color G", String.valueOf(R.string.prop_edge_glow_color_g));
        DISPLAY_NAME_MAP.put("Edge Glow Color R", String.valueOf(R.string.prop_edge_glow_color_r));
        DISPLAY_NAME_MAP.put("Edge Glow Intensity", String.valueOf(R.string.prop_edge_glow_intensity));
        DISPLAY_NAME_MAP.put("Edge Noise", String.valueOf(R.string.prop_edge_noise));
        DISPLAY_NAME_MAP.put("Edge Noise Intensity", String.valueOf(R.string.prop_edge_noise_intensity));
        DISPLAY_NAME_MAP.put("Edge R", String.valueOf(R.string.prop_edge_r));
        DISPLAY_NAME_MAP.put("Edge Shadow", String.valueOf(R.string.prop_edge_shadow));
        DISPLAY_NAME_MAP.put("Edge Shadow Intensity", String.valueOf(R.string.prop_edge_shadow_intensity));
        DISPLAY_NAME_MAP.put("Edge Softness", String.valueOf(R.string.prop_edge_softness));
        DISPLAY_NAME_MAP.put("Edge Tear", String.valueOf(R.string.prop_edge_tear));
        DISPLAY_NAME_MAP.put("Edge Tear Intensity", String.valueOf(R.string.prop_edge_tear_intensity));
        DISPLAY_NAME_MAP.put("Edge Warp", String.valueOf(R.string.prop_edge_warp));
        DISPLAY_NAME_MAP.put("Edge Warp Intensity", String.valueOf(R.string.prop_edge_warp_intensity));
        DISPLAY_NAME_MAP.put("Edge Width", String.valueOf(R.string.prop_edge_width));
        DISPLAY_NAME_MAP.put("Element Desc", String.valueOf(R.string.prop_element_desc));
        DISPLAY_NAME_MAP.put("Element Description", String.valueOf(R.string.prop_element_description));
        DISPLAY_NAME_MAP.put("End Size", String.valueOf(R.string.prop_end_size));
        DISPLAY_NAME_MAP.put("End Size Random", String.valueOf(R.string.prop_end_size_random));
        DISPLAY_NAME_MAP.put("End Size ZFactor", String.valueOf(R.string.prop_end_size_zfactor));
        DISPLAY_NAME_MAP.put("Enviroment", String.valueOf(R.string.prop_enviroment));
        DISPLAY_NAME_MAP.put("Evolution", String.valueOf(R.string.prop_evolution));
        DISPLAY_NAME_MAP.put("Evolution Phase", String.valueOf(R.string.prop_evolution_phase));
        DISPLAY_NAME_MAP.put("Fade In And Out", String.valueOf(R.string.prop_fade_in_and_out));
        DISPLAY_NAME_MAP.put("Fade In And Out Time", String.valueOf(R.string.prop_fade_in_and_out_time));
        DISPLAY_NAME_MAP.put("Fade In Size", String.valueOf(R.string.prop_fade_in_size));
        DISPLAY_NAME_MAP.put("Fade Radius", String.valueOf(R.string.prop_fade_radius));
        DISPLAY_NAME_MAP.put("Fade Width", String.valueOf(R.string.prop_fade_width));
        DISPLAY_NAME_MAP.put(Fake3DEffectElement.typeName, String.valueOf(R.string.prop_fake3d));
        DISPLAY_NAME_MAP.put("Falloff", String.valueOf(R.string.prop_falloff));
        DISPLAY_NAME_MAP.put("Falloff Power", String.valueOf(R.string.prop_falloff_power));
        DISPLAY_NAME_MAP.put("Fft Size", String.valueOf(R.string.prop_fft_size));
        DISPLAY_NAME_MAP.put("Field", String.valueOf(R.string.prop_field));
        DISPLAY_NAME_MAP.put("Film Flicker", String.valueOf(R.string.prop_film_flicker));
        DISPLAY_NAME_MAP.put("Film Flicker Intensity", String.valueOf(R.string.prop_film_flicker_intensity));
        DISPLAY_NAME_MAP.put("Film Grain", String.valueOf(R.string.prop_film_grain));
        DISPLAY_NAME_MAP.put("Film Grain Intensity", String.valueOf(R.string.prop_film_grain_intensity));
        DISPLAY_NAME_MAP.put("Film Scratch", String.valueOf(R.string.prop_film_scratch));
        DISPLAY_NAME_MAP.put("Film Scratch Density", String.valueOf(R.string.prop_film_scratch_density));
        DISPLAY_NAME_MAP.put("Film Scratch Intensity", String.valueOf(R.string.prop_film_scratch_intensity));
        DISPLAY_NAME_MAP.put("Filter Radius", String.valueOf(R.string.prop_filter_radius));
        DISPLAY_NAME_MAP.put("Filter Strength", String.valueOf(R.string.prop_filter_strength));
        DISPLAY_NAME_MAP.put("Fisheye", String.valueOf(R.string.prop_fisheye));
        DISPLAY_NAME_MAP.put("Fisheye Intensity", String.valueOf(R.string.prop_fisheye_intensity));
        DISPLAY_NAME_MAP.put("Fixed Height", String.valueOf(R.string.prop_fixed_height));
        DISPLAY_NAME_MAP.put("Flash", String.valueOf(R.string.prop_flash));
        DISPLAY_NAME_MAP.put("Flash Evolution", String.valueOf(R.string.prop_flash_evolution));
        DISPLAY_NAME_MAP.put("Flash Intensity", String.valueOf(R.string.prop_flash_intensity));
        DISPLAY_NAME_MAP.put("Flicker", String.valueOf(R.string.prop_flicker));
        DISPLAY_NAME_MAP.put("Flip Every Other", String.valueOf(R.string.prop_flip_every_other));
        DISPLAY_NAME_MAP.put("Flip Glitch", String.valueOf(R.string.prop_flip_glitch));
        DISPLAY_NAME_MAP.put("Flip Glitch Intensity", String.valueOf(R.string.prop_flip_glitch_intensity));
        DISPLAY_NAME_MAP.put("Flip Input", String.valueOf(R.string.prop_flip_input));
        DISPLAY_NAME_MAP.put("Flip Mirror", String.valueOf(R.string.prop_flip_mirror));
        DISPLAY_NAME_MAP.put("Flow", String.valueOf(R.string.prop_flow));
        DISPLAY_NAME_MAP.put("Font Size", String.valueOf(R.string.prop_font_size));
        DISPLAY_NAME_MAP.put("Force Field", String.valueOf(R.string.prop_force_field));
        DISPLAY_NAME_MAP.put("Fractal", String.valueOf(R.string.prop_fractal));
        DISPLAY_NAME_MAP.put("FractalNoiseEffect", String.valueOf(R.string.prop_fractal_noise_effect));
        DISPLAY_NAME_MAP.put("Frame Jitter", String.valueOf(R.string.prop_frame_jitter));
        DISPLAY_NAME_MAP.put("Frame Jitter Distance", String.valueOf(R.string.prop_frame_jitter_distance));
        DISPLAY_NAME_MAP.put("Frame Jitter Intensity", String.valueOf(R.string.prop_frame_jitter_intensity));
        DISPLAY_NAME_MAP.put("Frame Jitter Probability", String.valueOf(R.string.prop_frame_jitter_probability));
        DISPLAY_NAME_MAP.put("Fray", String.valueOf(R.string.prop_fray));
        DISPLAY_NAME_MAP.put("Fray Evolution", String.valueOf(R.string.prop_fray_evolution));
        DISPLAY_NAME_MAP.put("Freeze", String.valueOf(R.string.prop_freeze));
        DISPLAY_NAME_MAP.put("Freeze Duration", String.valueOf(R.string.prop_freeze_duration));
        DISPLAY_NAME_MAP.put("Freeze Intensity", String.valueOf(R.string.prop_freeze_intensity));
        DISPLAY_NAME_MAP.put("Freeze Probability", String.valueOf(R.string.prop_freeze_probability));
        DISPLAY_NAME_MAP.put("Freq Shift", String.valueOf(R.string.prop_freq_shift));
        DISPLAY_NAME_MAP.put("Frequency", String.valueOf(R.string.prop_frequency));
        DISPLAY_NAME_MAP.put("Gain", String.valueOf(R.string.prop_gain));
        DISPLAY_NAME_MAP.put("Gap", String.valueOf(R.string.prop_gap));
        DISPLAY_NAME_MAP.put("GaussianBlurEffect", String.valueOf(R.string.prop_gaussian_blur_effect));
        DISPLAY_NAME_MAP.put("General", String.valueOf(R.string.prop_general));
        DISPLAY_NAME_MAP.put("Generated Album Art", String.valueOf(R.string.prop_generated_album_art));
        DISPLAY_NAME_MAP.put("Generated Album Art Color", String.valueOf(R.string.prop_generated_album_art_color));
        DISPLAY_NAME_MAP.put("Generated Album Art Hint Force Gen", String.valueOf(R.string.prop_generated_album_art_hint_force_gen));
        DISPLAY_NAME_MAP.put("Generated Album Art Hint No Text", String.valueOf(R.string.prop_generated_album_art_hint_no_text));
        DISPLAY_NAME_MAP.put("Generated Album Art Hint Shift Hue", String.valueOf(R.string.prop_generated_album_art_hint_shift_hue));
        DISPLAY_NAME_MAP.put("Geometry", String.valueOf(R.string.prop_geometry));
        DISPLAY_NAME_MAP.put(GlitchEffectElement.typeName, String.valueOf(R.string.prop_glitch));
        DISPLAY_NAME_MAP.put("Glitch Propagation", String.valueOf(R.string.prop_glitch_propagation));
        DISPLAY_NAME_MAP.put("Glitch Propagation Evo", String.valueOf(R.string.prop_glitch_propagation_evo));
        DISPLAY_NAME_MAP.put("Glitch Propagation Evolution", String.valueOf(R.string.prop_glitch_propagation_evolution));
        DISPLAY_NAME_MAP.put("Glitch Propagation Intensity", String.valueOf(R.string.prop_glitch_propagation_intensity));
        DISPLAY_NAME_MAP.put("Glitch Propagation Radius", String.valueOf(R.string.prop_glitch_propagation_radius));
        DISPLAY_NAME_MAP.put("GlitchPropagationEffect", String.valueOf(R.string.prop_glitch_propagation_effect));
        DISPLAY_NAME_MAP.put(GlowEffectElement.typeName, String.valueOf(R.string.prop_glow));
        DISPLAY_NAME_MAP.put(GradientEffectElement.typeName, String.valueOf(R.string.prop_gradient));
        DISPLAY_NAME_MAP.put("Gradient Length", String.valueOf(R.string.prop_gradient_length));
        DISPLAY_NAME_MAP.put("Gravity", String.valueOf(R.string.prop_gravity));
        DISPLAY_NAME_MAP.put(HalftoneEffectElement.typeName, String.valueOf(R.string.prop_halftone));
        DISPLAY_NAME_MAP.put("HalftoneEffect", String.valueOf(R.string.prop_halftone_effect));
        DISPLAY_NAME_MAP.put("HeatDistortionEffect", String.valueOf(R.string.prop_heat_distortion_effect));
        DISPLAY_NAME_MAP.put("Height Scale", String.valueOf(R.string.prop_height_scale));
        DISPLAY_NAME_MAP.put("High Quality Audio Capture", String.valueOf(R.string.prop_high_quality_audio_capture));
        DISPLAY_NAME_MAP.put("Higher Hz", String.valueOf(R.string.prop_higher_hz));
        DISPLAY_NAME_MAP.put("Highlights", String.valueOf(R.string.prop_highlights));
        DISPLAY_NAME_MAP.put("Hue Amount", String.valueOf(R.string.prop_hue_amount));
        DISPLAY_NAME_MAP.put("Hz Linear Factor", String.valueOf(R.string.prop_hz_linear_factor));
        DISPLAY_NAME_MAP.put("Ignore Dark Colors", String.valueOf(R.string.prop_ignore_dark_colors));
        DISPLAY_NAME_MAP.put(ImageElement.typeName, String.valueOf(R.string.prop_image));
        DISPLAY_NAME_MAP.put("Inner Radius", String.valueOf(R.string.prop_inner_radius));
        DISPLAY_NAME_MAP.put("Input Size", String.valueOf(R.string.prop_input_size));
        DISPLAY_NAME_MAP.put("Intensity", String.valueOf(R.string.prop_intensity));
        DISPLAY_NAME_MAP.put("Interference", String.valueOf(R.string.prop_interference));
        DISPLAY_NAME_MAP.put("Interference Density", String.valueOf(R.string.prop_interference_density));
        DISPLAY_NAME_MAP.put("Interference Evolution", String.valueOf(R.string.prop_interference_evolution));
        DISPLAY_NAME_MAP.put("Interference Intensity", String.valueOf(R.string.prop_interference_intensity));
        DISPLAY_NAME_MAP.put("Interval", String.valueOf(R.string.prop_interval));
        DISPLAY_NAME_MAP.put("Intro Blend Mode", String.valueOf(R.string.prop_intro_blend_mode));
        DISPLAY_NAME_MAP.put("Intro Fade Color", String.valueOf(R.string.prop_intro_fade_color));
        DISPLAY_NAME_MAP.put("Intro Fade Duration", String.valueOf(R.string.prop_intro_fade_duration));
        DISPLAY_NAME_MAP.put("Intro Outro", String.valueOf(R.string.prop_intro_outro));
        DISPLAY_NAME_MAP.put("Invert", String.valueOf(R.string.prop_invert));
        DISPLAY_NAME_MAP.put("Jitter", String.valueOf(R.string.prop_jitter));
        DISPLAY_NAME_MAP.put("Jitter Amount", String.valueOf(R.string.prop_jitter_amount));
        DISPLAY_NAME_MAP.put("Jitter Falloff", String.valueOf(R.string.prop_jitter_falloff));
        DISPLAY_NAME_MAP.put("Jump", String.valueOf(R.string.prop_jump));
        DISPLAY_NAME_MAP.put("Jump Distance", String.valueOf(R.string.prop_jump_distance));
        DISPLAY_NAME_MAP.put("Jump Intensity", String.valueOf(R.string.prop_jump_intensity));
        DISPLAY_NAME_MAP.put("Keep Aspect Ratio", String.valueOf(R.string.prop_keep_aspect_ratio));
        DISPLAY_NAME_MAP.put("Keep Aspect Ratio And Crop", String.valueOf(R.string.prop_keep_aspect_ratio_and_crop));
        DISPLAY_NAME_MAP.put("Keep Aspect Ratio And Crop To Fit", String.valueOf(R.string.prop_keep_aspect_ratio_and_crop_to_fit));
        DISPLAY_NAME_MAP.put("Lacunarity", String.valueOf(R.string.prop_lacunarity));
        DISPLAY_NAME_MAP.put("Length", String.valueOf(R.string.prop_length));
        DISPLAY_NAME_MAP.put("Lifetime", String.valueOf(R.string.prop_lifetime));
        DISPLAY_NAME_MAP.put("Lower Freq", String.valueOf(R.string.prop_lower_freq));
        DISPLAY_NAME_MAP.put("Lower Freq Magnitude", String.valueOf(R.string.prop_lower_freq_magnitude));
        DISPLAY_NAME_MAP.put("Lower Hz", String.valueOf(R.string.prop_lower_hz));
        DISPLAY_NAME_MAP.put("Magenta", String.valueOf(R.string.prop_magenta));
        DISPLAY_NAME_MAP.put("MagnifierEffect", String.valueOf(R.string.prop_magnifier_effect));
        DISPLAY_NAME_MAP.put("Mask", String.valueOf(R.string.prop_mask));
        DISPLAY_NAME_MAP.put("Mask Image", String.valueOf(R.string.prop_mask_image));
        DISPLAY_NAME_MAP.put("Mask Mode", String.valueOf(R.string.prop_mask_mode));
        DISPLAY_NAME_MAP.put("Max Height Scale", String.valueOf(R.string.prop_max_height_scale));
        DISPLAY_NAME_MAP.put("Measure Animation Speed", String.valueOf(R.string.prop_measure_animation_speed));
        DISPLAY_NAME_MAP.put("Measure Color Blend", String.valueOf(R.string.prop_measure_color_blend));
        DISPLAY_NAME_MAP.put("Measure Overall Speed", String.valueOf(R.string.prop_measure_overall_speed));
        DISPLAY_NAME_MAP.put("Measure Pos", String.valueOf(R.string.prop_measure_pos));
        DISPLAY_NAME_MAP.put("Measure Rot", String.valueOf(R.string.prop_measure_rot));
        DISPLAY_NAME_MAP.put("Measure Scale", String.valueOf(R.string.prop_measure_scale));
        DISPLAY_NAME_MAP.put("Measure What", String.valueOf(R.string.prop_measure_what));
        DISPLAY_NAME_MAP.put("Melt", String.valueOf(R.string.prop_melt));
        DISPLAY_NAME_MAP.put("Melt Evolution", String.valueOf(R.string.prop_melt_evolution));
        DISPLAY_NAME_MAP.put("Melt Intensity", String.valueOf(R.string.prop_melt_intensity));
        DISPLAY_NAME_MAP.put("Min Height", String.valueOf(R.string.prop_min_height));
        DISPLAY_NAME_MAP.put("Min Height Scale", String.valueOf(R.string.prop_min_height_scale));
        DISPLAY_NAME_MAP.put("Mirro Effect", String.valueOf(R.string.prop_mirro_effect));
        DISPLAY_NAME_MAP.put("Mirror", String.valueOf(R.string.prop_mirror));
        DISPLAY_NAME_MAP.put("Mirror Mode", String.valueOf(R.string.prop_mirror_mode));
        DISPLAY_NAME_MAP.put("Mirror Samples", String.valueOf(R.string.prop_mirror_samples));
        DISPLAY_NAME_MAP.put("Mirror X", String.valueOf(R.string.prop_mirror_x));
        DISPLAY_NAME_MAP.put("Mirror Y", String.valueOf(R.string.prop_mirror_y));
        DISPLAY_NAME_MAP.put("Moire Pattern", String.valueOf(R.string.prop_moire_pattern));
        DISPLAY_NAME_MAP.put("Moire Pattern Evolution", String.valueOf(R.string.prop_moire_pattern_evolution));
        DISPLAY_NAME_MAP.put("Moire Pattern Intensity", String.valueOf(R.string.prop_moire_pattern_intensity));
        DISPLAY_NAME_MAP.put("Moire Pattern Scale", String.valueOf(R.string.prop_moire_pattern_scale));
        DISPLAY_NAME_MAP.put("Motion", String.valueOf(R.string.prop_motion));
        DISPLAY_NAME_MAP.put("Motion Blur", String.valueOf(R.string.prop_motion_blur));
        DISPLAY_NAME_MAP.put("Motion Source", String.valueOf(R.string.prop_motion_source));
        DISPLAY_NAME_MAP.put("Multiplier", String.valueOf(R.string.prop_multiplier));
        DISPLAY_NAME_MAP.put("Near Camera Fade", String.valueOf(R.string.prop_near_camera_fade));
        DISPLAY_NAME_MAP.put("Near Camera Fade Out", String.valueOf(R.string.prop_near_camera_fade_out));
        DISPLAY_NAME_MAP.put("Near Camera Fade Out Distance", String.valueOf(R.string.prop_near_camera_fade_out_distance));
        DISPLAY_NAME_MAP.put("Noise Scale", String.valueOf(R.string.prop_noise_scale));
        DISPLAY_NAME_MAP.put("Octaves", String.valueOf(R.string.prop_octaves));
        DISPLAY_NAME_MAP.put("Offset", String.valueOf(R.string.prop_offset));
        DISPLAY_NAME_MAP.put("Offset Start", String.valueOf(R.string.prop_offset_start));
        DISPLAY_NAME_MAP.put("Offset X", String.valueOf(R.string.prop_offset_x));
        DISPLAY_NAME_MAP.put("Offset Y", String.valueOf(R.string.prop_offset_y));
        DISPLAY_NAME_MAP.put("Opacity", String.valueOf(R.string.prop_opacity));
        DISPLAY_NAME_MAP.put("Opacity Strength", String.valueOf(R.string.prop_opacity_strength));
        DISPLAY_NAME_MAP.put("Optics", String.valueOf(R.string.prop_optics));
        DISPLAY_NAME_MAP.put("Outer Radius", String.valueOf(R.string.prop_outer_radius));
        DISPLAY_NAME_MAP.put("Output", String.valueOf(R.string.prop_output));
        DISPLAY_NAME_MAP.put("Output Multiplier", String.valueOf(R.string.prop_output_multiplier));
        DISPLAY_NAME_MAP.put("Outro Fade Color", String.valueOf(R.string.prop_outro_fade_color));
        DISPLAY_NAME_MAP.put("Outro Fade Duration", String.valueOf(R.string.prop_outro_fade_duration));
        DISPLAY_NAME_MAP.put("Overall", String.valueOf(R.string.prop_overall));
        DISPLAY_NAME_MAP.put("Overall Speed", String.valueOf(R.string.prop_overall_speed));
        DISPLAY_NAME_MAP.put("Particle Scale", String.valueOf(R.string.prop_particle_scale));
        DISPLAY_NAME_MAP.put("Passes", String.valueOf(R.string.prop_passes));
        DISPLAY_NAME_MAP.put("Performance", String.valueOf(R.string.prop_performance));
        DISPLAY_NAME_MAP.put("Perspective", String.valueOf(R.string.prop_perspective));
        DISPLAY_NAME_MAP.put("Perspective Depth", String.valueOf(R.string.prop_perspective_depth));
        DISPLAY_NAME_MAP.put("Perspective Fov", String.valueOf(R.string.prop_perspective_fov));
        DISPLAY_NAME_MAP.put("Phase", String.valueOf(R.string.prop_phase));
        DISPLAY_NAME_MAP.put("Phase Offset", String.valueOf(R.string.prop_phase_offset));
        DISPLAY_NAME_MAP.put("Pixel", String.valueOf(R.string.prop_pixel));
        DISPLAY_NAME_MAP.put("Pixel Dissolve", String.valueOf(R.string.prop_pixel_dissolve));
        DISPLAY_NAME_MAP.put("Pixel Dissolve Intensity", String.valueOf(R.string.prop_pixel_dissolve_intensity));
        DISPLAY_NAME_MAP.put("Pixel Drift", String.valueOf(R.string.prop_pixel_drift));
        DISPLAY_NAME_MAP.put("Pixel Drift Intensity", String.valueOf(R.string.prop_pixel_drift_intensity));
        DISPLAY_NAME_MAP.put("Pixel Explode", String.valueOf(R.string.prop_pixel_explode));
        DISPLAY_NAME_MAP.put("Pixel Explode Intensity", String.valueOf(R.string.prop_pixel_explode_intensity));
        DISPLAY_NAME_MAP.put("Pixel Explode Radius", String.valueOf(R.string.prop_pixel_explode_radius));
        DISPLAY_NAME_MAP.put("Pixel Flicker", String.valueOf(R.string.prop_pixel_flicker));
        DISPLAY_NAME_MAP.put("Pixel Flicker Intensity", String.valueOf(R.string.prop_pixel_flicker_intensity));
        DISPLAY_NAME_MAP.put("Pixel Incomplete", String.valueOf(R.string.prop_pixel_incomplete));
        DISPLAY_NAME_MAP.put("Pixel Incomplete Intensity", String.valueOf(R.string.prop_pixel_incomplete_intensity));
        DISPLAY_NAME_MAP.put("Pixel Stack", String.valueOf(R.string.prop_pixel_stack));
        DISPLAY_NAME_MAP.put("Pixel Stack Intensity", String.valueOf(R.string.prop_pixel_stack_intensity));
        DISPLAY_NAME_MAP.put("Pixel Stretch", String.valueOf(R.string.prop_pixel_stretch));
        DISPLAY_NAME_MAP.put("Pixel Stretch Intensity", String.valueOf(R.string.prop_pixel_stretch_intensity));
        DISPLAY_NAME_MAP.put("Pixel Wobble", String.valueOf(R.string.prop_pixel_wobble));
        DISPLAY_NAME_MAP.put("Pixel Wobble Direction", String.valueOf(R.string.prop_pixel_wobble_direction));
        DISPLAY_NAME_MAP.put("Pixel Wobble Evolution", String.valueOf(R.string.prop_pixel_wobble_evolution));
        DISPLAY_NAME_MAP.put("Pixel Wobble Intensity", String.valueOf(R.string.prop_pixel_wobble_intensity));
        DISPLAY_NAME_MAP.put("PixelStretchEffect", String.valueOf(R.string.prop_pixel_stretch_effect));
        DISPLAY_NAME_MAP.put("Plane Infinite", String.valueOf(R.string.prop_plane_infinite));
        DISPLAY_NAME_MAP.put("Plane Texture Scale", String.valueOf(R.string.prop_plane_texture_scale));
        DISPLAY_NAME_MAP.put("Position", String.valueOf(R.string.prop_position));
        DISPLAY_NAME_MAP.put("Post FX", String.valueOf(R.string.prop_post_fx));
        DISPLAY_NAME_MAP.put("Pre Smooth", String.valueOf(R.string.prop_pre_smooth));
        DISPLAY_NAME_MAP.put("Quality", String.valueOf(R.string.prop_quality));
        DISPLAY_NAME_MAP.put("Radial", String.valueOf(R.string.prop_radial));
        DISPLAY_NAME_MAP.put("Radius", String.valueOf(R.string.prop_radius));
        DISPLAY_NAME_MAP.put("Radius X", String.valueOf(R.string.prop_radius_x));
        DISPLAY_NAME_MAP.put("Radius Y", String.valueOf(R.string.prop_radius_y));
        DISPLAY_NAME_MAP.put("Ray Center X", String.valueOf(R.string.prop_ray_center_x));
        DISPLAY_NAME_MAP.put("Ray Center Y", String.valueOf(R.string.prop_ray_center_y));
        DISPLAY_NAME_MAP.put("Ray Opacity", String.valueOf(R.string.prop_ray_opacity));
        DISPLAY_NAME_MAP.put("Reaction Accumulated", String.valueOf(R.string.prop_reaction_accumulated));
        DISPLAY_NAME_MAP.put("Reaction Accumulated Delay", String.valueOf(R.string.prop_reaction_accumulated_delay));
        DISPLAY_NAME_MAP.put("Reaction Delay", String.valueOf(R.string.prop_reaction_delay));
        DISPLAY_NAME_MAP.put("Reflection", String.valueOf(R.string.prop_reflection));
        DISPLAY_NAME_MAP.put("Relative Motion", String.valueOf(R.string.prop_relative_motion));
        DISPLAY_NAME_MAP.put("Relative Motion Mode", String.valueOf(R.string.prop_relative_motion_mode));
        DISPLAY_NAME_MAP.put("Render Input On Top", String.valueOf(R.string.prop_render_input_on_top));
        DISPLAY_NAME_MAP.put("Repeat Edge", String.valueOf(R.string.prop_repeat_edge));
        DISPLAY_NAME_MAP.put("Repeat Samples", String.valueOf(R.string.prop_repeat_samples));
        DISPLAY_NAME_MAP.put("Repeat Texture", String.valueOf(R.string.prop_repeat_texture));
        DISPLAY_NAME_MAP.put("Reverse", String.valueOf(R.string.prop_reverse));
        DISPLAY_NAME_MAP.put("Rewind", String.valueOf(R.string.prop_rewind));
        DISPLAY_NAME_MAP.put("Rewind Intensity", String.valueOf(R.string.prop_rewind_intensity));
        DISPLAY_NAME_MAP.put("Rotate", String.valueOf(R.string.prop_rotate));
        DISPLAY_NAME_MAP.put("Rotate Glitch", String.valueOf(R.string.prop_rotate_glitch));
        DISPLAY_NAME_MAP.put("Rotate Glitch Intensity", String.valueOf(R.string.prop_rotate_glitch_intensity));
        DISPLAY_NAME_MAP.put("Rotate X", String.valueOf(R.string.prop_rotate_x));
        DISPLAY_NAME_MAP.put("Rotate Y", String.valueOf(R.string.prop_rotate_y));
        DISPLAY_NAME_MAP.put("Rotation", String.valueOf(R.string.prop_rotation));
        DISPLAY_NAME_MAP.put("Rotation X", String.valueOf(R.string.prop_rotation_x));
        DISPLAY_NAME_MAP.put("Rotation Y", String.valueOf(R.string.prop_rotation_y));
        DISPLAY_NAME_MAP.put("RoughenEdgesEffect", String.valueOf(R.string.prop_roughen_edges_effect));
        DISPLAY_NAME_MAP.put("Roughness", String.valueOf(R.string.prop_roughness));
        DISPLAY_NAME_MAP.put("Sample Count", String.valueOf(R.string.prop_sample_count));
        DISPLAY_NAME_MAP.put("Sample Out Count", String.valueOf(R.string.prop_sample_out_count));
        DISPLAY_NAME_MAP.put("Sample Out Count Power", String.valueOf(R.string.prop_sample_out_count_power));
        DISPLAY_NAME_MAP.put("Sample Provider", String.valueOf(R.string.prop_sample_provider));
        DISPLAY_NAME_MAP.put("Samples", String.valueOf(R.string.prop_samples));
        DISPLAY_NAME_MAP.put("Saturation", String.valueOf(R.string.prop_saturation));
        DISPLAY_NAME_MAP.put("Scale", String.valueOf(R.string.prop_scale));
        DISPLAY_NAME_MAP.put("Scale Is Uniform", String.valueOf(R.string.prop_scale_is_uniform));
        DISPLAY_NAME_MAP.put("Scale Power", String.valueOf(R.string.prop_scale_power));
        DISPLAY_NAME_MAP.put("Scale X", String.valueOf(R.string.prop_scale_x));
        DISPLAY_NAME_MAP.put("Scale Y", String.valueOf(R.string.prop_scale_y));
        DISPLAY_NAME_MAP.put("Scale Z", String.valueOf(R.string.prop_scale_z));
        DISPLAY_NAME_MAP.put("Scatter", String.valueOf(R.string.prop_scatter));
        DISPLAY_NAME_MAP.put("Scatter Amount", String.valueOf(R.string.prop_scatter_amount));
        DISPLAY_NAME_MAP.put("Segment 1", String.valueOf(R.string.prop_segment_1));
        DISPLAY_NAME_MAP.put("Segment 2", String.valueOf(R.string.prop_segment_2));
        DISPLAY_NAME_MAP.put("Segments", String.valueOf(R.string.prop_segments));
        DISPLAY_NAME_MAP.put("Shader", String.valueOf(R.string.prop_shader));
        DISPLAY_NAME_MAP.put("Shader Fragment", String.valueOf(R.string.prop_shader_fragment));
        DISPLAY_NAME_MAP.put("Shader Vertex", String.valueOf(R.string.prop_shader_vertex));
        DISPLAY_NAME_MAP.put("Shadow", String.valueOf(R.string.prop_shadow));
        DISPLAY_NAME_MAP.put("Shadows", String.valueOf(R.string.prop_shadows));
        DISPLAY_NAME_MAP.put("Shake", String.valueOf(R.string.prop_shake));
        DISPLAY_NAME_MAP.put("Shake Direction", String.valueOf(R.string.prop_shake_direction));
        DISPLAY_NAME_MAP.put("Shake Evolution", String.valueOf(R.string.prop_shake_evolution));
        DISPLAY_NAME_MAP.put("Shake Intensity", String.valueOf(R.string.prop_shake_intensity));
        DISPLAY_NAME_MAP.put("Shape", String.valueOf(R.string.prop_shape));
        DISPLAY_NAME_MAP.put("Shape Path", String.valueOf(R.string.prop_shape_path));
        DISPLAY_NAME_MAP.put("Sharpness", String.valueOf(R.string.prop_sharpness));
        DISPLAY_NAME_MAP.put("Shift B", String.valueOf(R.string.prop_shift_b));
        DISPLAY_NAME_MAP.put("Shift G", String.valueOf(R.string.prop_shift_g));
        DISPLAY_NAME_MAP.put("Shift Glitch", String.valueOf(R.string.prop_shift_glitch));
        DISPLAY_NAME_MAP.put("Shift Glitch Intensity", String.valueOf(R.string.prop_shift_glitch_intensity));
        DISPLAY_NAME_MAP.put("Shift R", String.valueOf(R.string.prop_shift_r));
        DISPLAY_NAME_MAP.put("Show Cube", String.valueOf(R.string.prop_show_cube));
        DISPLAY_NAME_MAP.put("Show Plane", String.valueOf(R.string.prop_show_plane));
        DISPLAY_NAME_MAP.put("Show Unblurred", String.valueOf(R.string.prop_show_unblurred));
        DISPLAY_NAME_MAP.put("Show Unblurred Content", String.valueOf(R.string.prop_show_unblurred_content));
        DISPLAY_NAME_MAP.put("Show Unblurred Content Under", String.valueOf(R.string.prop_show_unblurred_content_under));
        DISPLAY_NAME_MAP.put("Sides", String.valueOf(R.string.prop_sides));
        DISPLAY_NAME_MAP.put("Side Sine Wave Freq", String.valueOf(R.string.prop_side_sine_wave_freq));
        DISPLAY_NAME_MAP.put("Side Sine Wave Freq Random", String.valueOf(R.string.prop_side_sine_wave_freq_random));
        DISPLAY_NAME_MAP.put("Side Sine Wave Mag", String.valueOf(R.string.prop_side_sine_wave_mag));
        DISPLAY_NAME_MAP.put("Side Sine Wave Mag Random", String.valueOf(R.string.prop_side_sine_wave_mag_random));
        DISPLAY_NAME_MAP.put("Signal", String.valueOf(R.string.prop_signal));
        DISPLAY_NAME_MAP.put("Signal Decode", String.valueOf(R.string.prop_signal_decode));
        DISPLAY_NAME_MAP.put("Signal Decode Intensity", String.valueOf(R.string.prop_signal_decode_intensity));
        DISPLAY_NAME_MAP.put("Signal Distort", String.valueOf(R.string.prop_signal_distort));
        DISPLAY_NAME_MAP.put("Signal Distort Intensity", String.valueOf(R.string.prop_signal_distort_intensity));
        DISPLAY_NAME_MAP.put("Signal Drift", String.valueOf(R.string.prop_signal_drift));
        DISPLAY_NAME_MAP.put("Signal Drift Intensity", String.valueOf(R.string.prop_signal_drift_intensity));
        DISPLAY_NAME_MAP.put("Signal Echo", String.valueOf(R.string.prop_signal_echo));
        DISPLAY_NAME_MAP.put("Signal Echo Intensity", String.valueOf(R.string.prop_signal_echo_intensity));
        DISPLAY_NAME_MAP.put("Signal Fade", String.valueOf(R.string.prop_signal_fade));
        DISPLAY_NAME_MAP.put("Signal Fade Intensity", String.valueOf(R.string.prop_signal_fade_intensity));
        DISPLAY_NAME_MAP.put("Signal Flicker", String.valueOf(R.string.prop_signal_flicker));
        DISPLAY_NAME_MAP.put("Signal Flicker Evo", String.valueOf(R.string.prop_signal_flicker_evo));
        DISPLAY_NAME_MAP.put("Signal Flicker Evolution", String.valueOf(R.string.prop_signal_flicker_evolution));
        DISPLAY_NAME_MAP.put("Signal Flicker Intensity", String.valueOf(R.string.prop_signal_flicker_intensity));
        DISPLAY_NAME_MAP.put("Signal Lock", String.valueOf(R.string.prop_signal_lock));
        DISPLAY_NAME_MAP.put("Signal Lock Intensity", String.valueOf(R.string.prop_signal_lock_intensity));
        DISPLAY_NAME_MAP.put("Signal Lock Prob", String.valueOf(R.string.prop_signal_lock_prob));
        DISPLAY_NAME_MAP.put("Signal Lock Probability", String.valueOf(R.string.prop_signal_lock_probability));
        DISPLAY_NAME_MAP.put("Signal Loss", String.valueOf(R.string.prop_signal_loss));
        DISPLAY_NAME_MAP.put("Signal Loss Intensity", String.valueOf(R.string.prop_signal_loss_intensity));
        DISPLAY_NAME_MAP.put("Signal Loss Prob", String.valueOf(R.string.prop_signal_loss_prob));
        DISPLAY_NAME_MAP.put("Signal Loss Probability", String.valueOf(R.string.prop_signal_loss_probability));
        DISPLAY_NAME_MAP.put("Signal Loss Width", String.valueOf(R.string.prop_signal_loss_width));
        DISPLAY_NAME_MAP.put("Signal Overlap", String.valueOf(R.string.prop_signal_overlap));
        DISPLAY_NAME_MAP.put("Signal Overlap Intensity", String.valueOf(R.string.prop_signal_overlap_intensity));
        DISPLAY_NAME_MAP.put("Signal Shift", String.valueOf(R.string.prop_signal_shift));
        DISPLAY_NAME_MAP.put("Signal Shift Intensity", String.valueOf(R.string.prop_signal_shift_intensity));
        DISPLAY_NAME_MAP.put("Signal Spike", String.valueOf(R.string.prop_signal_spike));
        DISPLAY_NAME_MAP.put("Signal Spike Evo", String.valueOf(R.string.prop_signal_spike_evo));
        DISPLAY_NAME_MAP.put("Signal Spike Evolution", String.valueOf(R.string.prop_signal_spike_evolution));
        DISPLAY_NAME_MAP.put("Signal Spike Intensity", String.valueOf(R.string.prop_signal_spike_intensity));
        DISPLAY_NAME_MAP.put("Signal Spike Width", String.valueOf(R.string.prop_signal_spike_width));
        DISPLAY_NAME_MAP.put("Size", String.valueOf(R.string.prop_size));
        DISPLAY_NAME_MAP.put("Size Aspect", String.valueOf(R.string.prop_size_aspect));
        DISPLAY_NAME_MAP.put("Size Aspect Ratio", String.valueOf(R.string.prop_size_aspect_ratio));
        DISPLAY_NAME_MAP.put("Slow Down", String.valueOf(R.string.prop_slow_down));
        DISPLAY_NAME_MAP.put("Slow Down Intensity", String.valueOf(R.string.prop_slow_down_intensity));
        DISPLAY_NAME_MAP.put("Smooth", String.valueOf(R.string.prop_smooth));
        DISPLAY_NAME_MAP.put("Softness", String.valueOf(R.string.prop_softness));
        DISPLAY_NAME_MAP.put("Source Composition", String.valueOf(R.string.prop_source_composition));
        DISPLAY_NAME_MAP.put("Source Composition Index", String.valueOf(R.string.prop_source_composition_index));
        DISPLAY_NAME_MAP.put("Spawn", String.valueOf(R.string.prop_spawn));
        DISPLAY_NAME_MAP.put("Spawn Area", String.valueOf(R.string.prop_spawn_area));
        DISPLAY_NAME_MAP.put("Spawn Time", String.valueOf(R.string.prop_spawn_time));
        DISPLAY_NAME_MAP.put(SegmentAudioSpectrumData.typeName, String.valueOf(R.string.prop_spectrum));
        DISPLAY_NAME_MAP.put("Spectrum Hz", String.valueOf(R.string.prop_spectrum_hz));
        DISPLAY_NAME_MAP.put("Speed", String.valueOf(R.string.prop_speed));
        DISPLAY_NAME_MAP.put("Speed Random", String.valueOf(R.string.prop_speed_random));
        DISPLAY_NAME_MAP.put("Speed Up Intensity", String.valueOf(R.string.prop_speed_up_intensity));
        DISPLAY_NAME_MAP.put("SpiralEffect", String.valueOf(R.string.prop_spiral_effect));
        DISPLAY_NAME_MAP.put("Split Amount", String.valueOf(R.string.prop_split_amount));
        DISPLAY_NAME_MAP.put("Split Color 0", String.valueOf(R.string.prop_split_color_0));
        DISPLAY_NAME_MAP.put("Split Color 1", String.valueOf(R.string.prop_split_color_1));
        DISPLAY_NAME_MAP.put("Split Color 2", String.valueOf(R.string.prop_split_color_2));
        DISPLAY_NAME_MAP.put("Split Effect", String.valueOf(R.string.prop_split_effect));
        DISPLAY_NAME_MAP.put("Split Multiplier", String.valueOf(R.string.prop_split_multiplier));
        DISPLAY_NAME_MAP.put("Split Textures", String.valueOf(R.string.prop_split_textures));
        DISPLAY_NAME_MAP.put("Squeeze", String.valueOf(R.string.prop_squeeze));
        DISPLAY_NAME_MAP.put("Squeeze Center", String.valueOf(R.string.prop_squeeze_center));
        DISPLAY_NAME_MAP.put("Squeeze Intensity", String.valueOf(R.string.prop_squeeze_intensity));
        DISPLAY_NAME_MAP.put("Star And End Gap", String.valueOf(R.string.prop_star_and_end_gap));
        DISPLAY_NAME_MAP.put("Start Size", String.valueOf(R.string.prop_start_size));
        DISPLAY_NAME_MAP.put("Static Interference", String.valueOf(R.string.prop_static_interference));
        DISPLAY_NAME_MAP.put("Static Interference Intensity", String.valueOf(R.string.prop_static_interference_intensity));
        DISPLAY_NAME_MAP.put("Stretch", String.valueOf(R.string.prop_stretch));
        DISPLAY_NAME_MAP.put("Stretch Intensity", String.valueOf(R.string.prop_stretch_intensity));
        DISPLAY_NAME_MAP.put("Strength", String.valueOf(R.string.prop_strength));
        DISPLAY_NAME_MAP.put("Strength Intensity", String.valueOf(R.string.prop_strength_intensity));
        DISPLAY_NAME_MAP.put("Swipe", String.valueOf(R.string.prop_swipe));
        DISPLAY_NAME_MAP.put("Swirl Intensity", String.valueOf(R.string.prop_swirl_intensity));
        DISPLAY_NAME_MAP.put("Swirl Radius", String.valueOf(R.string.prop_swirl_radius));
        DISPLAY_NAME_MAP.put("Tag", String.valueOf(R.string.prop_tag));
        DISPLAY_NAME_MAP.put("Tape Noise", String.valueOf(R.string.prop_tape_noise));
        DISPLAY_NAME_MAP.put("Tape Noise Intensity", String.valueOf(R.string.prop_tape_noise_intensity));
        DISPLAY_NAME_MAP.put("Tape Streak", String.valueOf(R.string.prop_tape_streak));
        DISPLAY_NAME_MAP.put("Tape Streak Intensity", String.valueOf(R.string.prop_tape_streak_intensity));
        DISPLAY_NAME_MAP.put("Target Image", String.valueOf(R.string.prop_target_image));
        DISPLAY_NAME_MAP.put("Tear Diagonal", String.valueOf(R.string.prop_tear_diagonal));
        DISPLAY_NAME_MAP.put("Tear Diagonal Density", String.valueOf(R.string.prop_tear_diagonal_density));
        DISPLAY_NAME_MAP.put("Tear Diagonal Intensity", String.valueOf(R.string.prop_tear_diagonal_intensity));
        DISPLAY_NAME_MAP.put("Tear Diagonal Probability", String.valueOf(R.string.prop_tear_diagonal_probability));
        DISPLAY_NAME_MAP.put("Tear H", String.valueOf(R.string.prop_tear_h));
        DISPLAY_NAME_MAP.put("Tear H Density", String.valueOf(R.string.prop_tear_h_density));
        DISPLAY_NAME_MAP.put("Tear H Intensity", String.valueOf(R.string.prop_tear_h_intensity));
        DISPLAY_NAME_MAP.put("Tear H Probability", String.valueOf(R.string.prop_tear_h_probability));
        DISPLAY_NAME_MAP.put("Tear HIntensity", String.valueOf(R.string.prop_tear_hintensity));
        DISPLAY_NAME_MAP.put("Tear HProbability", String.valueOf(R.string.prop_tear_hprobability));
        DISPLAY_NAME_MAP.put("Tear HDensity", String.valueOf(R.string.prop_tear_hdensity));
        DISPLAY_NAME_MAP.put("Tear V", String.valueOf(R.string.prop_tear_v));
        DISPLAY_NAME_MAP.put("Tear V Density", String.valueOf(R.string.prop_tear_v_density));
        DISPLAY_NAME_MAP.put("Tear V Intensity", String.valueOf(R.string.prop_tear_v_intensity));
        DISPLAY_NAME_MAP.put("Tear V Probability", String.valueOf(R.string.prop_tear_v_probability));
        DISPLAY_NAME_MAP.put("Tear VIntensity", String.valueOf(R.string.prop_tear_vintensity));
        DISPLAY_NAME_MAP.put("Tear VProbability", String.valueOf(R.string.prop_tear_vprobability));
        DISPLAY_NAME_MAP.put("Tear VDensity", String.valueOf(R.string.prop_tear_vdensity));
        DISPLAY_NAME_MAP.put(TextElement.typeName, String.valueOf(R.string.prop_text));
        DISPLAY_NAME_MAP.put("Text Align", String.valueOf(R.string.prop_text_align));
        DISPLAY_NAME_MAP.put("Texture Aspect", String.valueOf(R.string.prop_texture_aspect));
        DISPLAY_NAME_MAP.put("Threshold", String.valueOf(R.string.prop_threshold));
        DISPLAY_NAME_MAP.put("Timing", String.valueOf(R.string.prop_timing));
        DISPLAY_NAME_MAP.put("Tl X", String.valueOf(R.string.prop_tl_x));
        DISPLAY_NAME_MAP.put("Tl Y", String.valueOf(R.string.prop_tl_y));
        DISPLAY_NAME_MAP.put("Tr X", String.valueOf(R.string.prop_tr_x));
        DISPLAY_NAME_MAP.put("Tr Y", String.valueOf(R.string.prop_tr_y));
        DISPLAY_NAME_MAP.put("Trail Length", String.valueOf(R.string.prop_trail_length));
        DISPLAY_NAME_MAP.put("Transform", String.valueOf(R.string.prop_transform));
        DISPLAY_NAME_MAP.put("Transparency", String.valueOf(R.string.prop_transparency));
        DISPLAY_NAME_MAP.put("Transparency Strength", String.valueOf(R.string.prop_transparency_strength));
        DISPLAY_NAME_MAP.put("Tri Curve", String.valueOf(R.string.prop_tri_curve));
        DISPLAY_NAME_MAP.put("Tri Round", String.valueOf(R.string.prop_tri_round));
        DISPLAY_NAME_MAP.put("Turbulence", String.valueOf(R.string.prop_turbulence));
        DISPLAY_NAME_MAP.put("Twist", String.valueOf(R.string.prop_twist));
        DISPLAY_NAME_MAP.put("Twist Power", String.valueOf(R.string.prop_twist_power));
        DISPLAY_NAME_MAP.put("Type Face", String.valueOf(R.string.prop_type_face));
        DISPLAY_NAME_MAP.put("Use Random Vector Instead", String.valueOf(R.string.prop_use_random_vector_instead));
        DISPLAY_NAME_MAP.put("Value 1", String.valueOf(R.string.prop_value_1));
        DISPLAY_NAME_MAP.put("Value 2", String.valueOf(R.string.prop_value_2));
        DISPLAY_NAME_MAP.put("Value 3", String.valueOf(R.string.prop_value_3));
        DISPLAY_NAME_MAP.put("Value 4", String.valueOf(R.string.prop_value_4));
        DISPLAY_NAME_MAP.put("Value 5", String.valueOf(R.string.prop_value_5));
        DISPLAY_NAME_MAP.put("Value 6", String.valueOf(R.string.prop_value_6));
        DISPLAY_NAME_MAP.put("Variables", String.valueOf(R.string.prop_variables));
        DISPLAY_NAME_MAP.put("Vector Angle", String.valueOf(R.string.prop_vector_angle));
        DISPLAY_NAME_MAP.put("Vector Angle Z", String.valueOf(R.string.prop_vector_angle_z));
        DISPLAY_NAME_MAP.put("Vector Blur", String.valueOf(R.string.prop_vector_blur));
        DISPLAY_NAME_MAP.put("Vector Relative", String.valueOf(R.string.prop_vector_relative));
        DISPLAY_NAME_MAP.put("Vector Relative To Center", String.valueOf(R.string.prop_vector_relative_to_center));
        DISPLAY_NAME_MAP.put("VectorBlurEffect", String.valueOf(R.string.prop_vector_blur_effect));
        DISPLAY_NAME_MAP.put("Velocity Angle", String.valueOf(R.string.prop_velocity_angle));
        DISPLAY_NAME_MAP.put("Vertical Orientation", String.valueOf(R.string.prop_vertical_orientation));
        DISPLAY_NAME_MAP.put("Vhs Tracking", String.valueOf(R.string.prop_vhs_tracking));
        DISPLAY_NAME_MAP.put("Vhs Tracking Intensity", String.valueOf(R.string.prop_vhs_tracking_intensity));
        DISPLAY_NAME_MAP.put("Vibrance", String.valueOf(R.string.prop_vibrance));
        DISPLAY_NAME_MAP.put("Visible", String.valueOf(R.string.prop_visible));
        DISPLAY_NAME_MAP.put("Visible (Avee水印)", String.valueOf(R.string.prop_visible_watermark));
        DISPLAY_NAME_MAP.put("Warp", String.valueOf(R.string.prop_warp));
        DISPLAY_NAME_MAP.put("Warp Speed", String.valueOf(R.string.prop_warp_speed));
        DISPLAY_NAME_MAP.put("WaterRippleEffect", String.valueOf(R.string.prop_water_ripple_effect));
        DISPLAY_NAME_MAP.put("Wave", String.valueOf(R.string.prop_wave));
        DISPLAY_NAME_MAP.put("Wave Amplitude", String.valueOf(R.string.prop_wave_amplitude));
        DISPLAY_NAME_MAP.put("Wave Behaviour", String.valueOf(R.string.prop_wave_behaviour));
        DISPLAY_NAME_MAP.put("Wave Distort", String.valueOf(R.string.prop_wave_distort));
        DISPLAY_NAME_MAP.put("Wave Distort Evo", String.valueOf(R.string.prop_wave_distort_evo));
        DISPLAY_NAME_MAP.put("Wave Distort Evolution", String.valueOf(R.string.prop_wave_distort_evolution));
        DISPLAY_NAME_MAP.put("Wave Distort Freq", String.valueOf(R.string.prop_wave_distort_freq));
        DISPLAY_NAME_MAP.put("Wave Distort Frequency", String.valueOf(R.string.prop_wave_distort_frequency));
        DISPLAY_NAME_MAP.put("Wave Distort Intensity", String.valueOf(R.string.prop_wave_distort_intensity));
        DISPLAY_NAME_MAP.put("Wave Distortion", String.valueOf(R.string.prop_wave_distortion));
        DISPLAY_NAME_MAP.put("Wave DistortionEffect", String.valueOf(R.string.prop_wave_distortion_effect));
        DISPLAY_NAME_MAP.put("Wave Frequency", String.valueOf(R.string.prop_wave_frequency));
        DISPLAY_NAME_MAP.put(SegmentAudioWaveformData.typeName, String.valueOf(R.string.prop_waveform));
        DISPLAY_NAME_MAP.put("Weight", String.valueOf(R.string.prop_weight));
        DISPLAY_NAME_MAP.put("Wobble", String.valueOf(R.string.prop_wobble));
        DISPLAY_NAME_MAP.put("Yellow", String.valueOf(R.string.prop_yellow));
        DISPLAY_NAME_MAP.put("Zoom", String.valueOf(R.string.prop_zoom));
        DISPLAY_NAME_MAP.put("Zoom Glitch", String.valueOf(R.string.prop_zoom_glitch));
        DISPLAY_NAME_MAP.put("Zoom Glitch Intensity", String.valueOf(R.string.prop_zoom_glitch_intensity));
        DISPLAY_NAME_MAP.put("Cam Distance", String.valueOf(R.string.prop_cam_distance));
        DISPLAY_NAME_MAP.put("Color Stripe Intensity", String.valueOf(R.string.prop_color_stripe_intensity));
        DISPLAY_NAME_MAP.put("Char Source", String.valueOf(R.string.prop_char_source));
        DISPLAY_NAME_MAP.put("Custom Char", String.valueOf(R.string.prop_custom_char));
        DISPLAY_NAME_MAP.put("Frame Smooth", String.valueOf(R.string.prop_frame_smooth));
        DISPLAY_NAME_MAP.put("Filter Smooth", String.valueOf(R.string.prop_filter_smooth));
        DISPLAY_NAME_MAP.put("Pos Blur Amount", String.valueOf(R.string.prop_pos_blur_amount));
        DISPLAY_NAME_MAP.put("Scale Blur Amount", String.valueOf(R.string.prop_scale_blur_amount));
        DISPLAY_NAME_MAP.put("Tolerance", String.valueOf(R.string.prop_tolerance));
        DISPLAY_NAME_MAP.put("Warp Strength", String.valueOf(R.string.prop_warp_strength));
        DISPLAY_NAME_MAP.put("Warp Radius", String.valueOf(R.string.prop_warp_radius));
        DISPLAY_NAME_MAP.put("Warp Smooth", String.valueOf(R.string.prop_warp_smooth));
        DISPLAY_NAME_MAP.put("Blend Offset", String.valueOf(R.string.prop_blend_offset));
        DISPLAY_NAME_MAP.put("Color From", String.valueOf(R.string.prop_color_from_kskbl));
        DISPLAY_NAME_MAP.put("Mid Color", String.valueOf(R.string.prop_mid_color_kskbl));
        DISPLAY_NAME_MAP.put("Color To", String.valueOf(R.string.prop_color_to_kskbl));
        DISPLAY_NAME_MAP.put("Repeat", String.valueOf(R.string.prop_repeat));
        DISPLAY_NAME_MAP.put("Type", String.valueOf(R.string.prop_type));
        DISPLAY_NAME_MAP.put("Red From", String.valueOf(R.string.prop_red_from));
        DISPLAY_NAME_MAP.put("Green From", String.valueOf(R.string.prop_green_from));
        DISPLAY_NAME_MAP.put("Blue From", String.valueOf(R.string.prop_blue_from));
        DISPLAY_NAME_MAP.put("Alpha From", String.valueOf(R.string.prop_alpha_from));
        DISPLAY_NAME_MAP.put("Red Mid", String.valueOf(R.string.prop_red_mid));
        DISPLAY_NAME_MAP.put("Green Mid", String.valueOf(R.string.prop_green_mid));
        DISPLAY_NAME_MAP.put("Blue Mid", String.valueOf(R.string.prop_blue_mid));
        DISPLAY_NAME_MAP.put("Alpha Mid", String.valueOf(R.string.prop_alpha_mid));
        DISPLAY_NAME_MAP.put("Use Mid", String.valueOf(R.string.prop_use_mid));
        DISPLAY_NAME_MAP.put("Mid Pos", String.valueOf(R.string.prop_mid_pos));
        DISPLAY_NAME_MAP.put("Red To", String.valueOf(R.string.prop_red_to));
        DISPLAY_NAME_MAP.put("Green To", String.valueOf(R.string.prop_green_to));
        DISPLAY_NAME_MAP.put("Blue To", String.valueOf(R.string.prop_blue_to));
        DISPLAY_NAME_MAP.put("Alpha To", String.valueOf(R.string.prop_alpha_to));
        DISPLAY_NAME_MAP.put("Gradient Type", String.valueOf(R.string.prop_gradient_type));
        DISPLAY_NAME_MAP.put("Stretch X", String.valueOf(R.string.prop_stretch_x));
        DISPLAY_NAME_MAP.put("Stretch Y", String.valueOf(R.string.prop_stretch_y));
        DISPLAY_NAME_MAP.put("Repeat Mode", String.valueOf(R.string.prop_repeat_mode));
        DISPLAY_NAME_MAP.put("Repeat Offset", String.valueOf(R.string.prop_repeat_offset));
        DISPLAY_NAME_MAP.put("Shadow R", String.valueOf(R.string.prop_shadow_r));
        DISPLAY_NAME_MAP.put("Shadow G", String.valueOf(R.string.prop_shadow_g));
        DISPLAY_NAME_MAP.put("Shadow B", String.valueOf(R.string.prop_shadow_b));
        DISPLAY_NAME_MAP.put("Shadow A", String.valueOf(R.string.prop_shadow_a));
        DISPLAY_NAME_MAP.put("Distance", String.valueOf(R.string.prop_distance));
        DISPLAY_NAME_MAP.put("Mix", String.valueOf(R.string.prop_mix));
        DISPLAY_NAME_MAP.put("Red", String.valueOf(R.string.prop_red));
        DISPLAY_NAME_MAP.put("Green", String.valueOf(R.string.prop_green));
        DISPLAY_NAME_MAP.put("Blue", String.valueOf(R.string.prop_blue));
        DISPLAY_NAME_MAP.put("Rain", String.valueOf(R.string.prop_rain));
        DISPLAY_NAME_MAP.put("Key", String.valueOf(R.string.prop_key));
        DISPLAY_NAME_MAP.put("Edge Clean", String.valueOf(R.string.prop_edge_clean));
        DISPLAY_NAME_MAP.put("Sharpen", String.valueOf(R.string.prop_sharpen));
        DISPLAY_NAME_MAP.put("Sharpen Step", String.valueOf(R.string.prop_sharpen_step));
        DISPLAY_NAME_MAP.put("Edge Smooth", String.valueOf(R.string.prop_edge_smooth));
        DISPLAY_NAME_MAP.put("Scan", String.valueOf(R.string.prop_scan));
        DISPLAY_NAME_MAP.put("Scan Density", String.valueOf(R.string.prop_scan_density));
        DISPLAY_NAME_MAP.put("Scan Height", String.valueOf(R.string.prop_scan_height));
        DISPLAY_NAME_MAP.put("Scan Bg Alpha", String.valueOf(R.string.prop_scan_bg_alpha));
        DISPLAY_NAME_MAP.put("Scan Opacity", String.valueOf(R.string.prop_scan_opacity));
        DISPLAY_NAME_MAP.put("Wave Strength", String.valueOf(R.string.prop_wave_strength));
        DISPLAY_NAME_MAP.put("Wave Density", String.valueOf(R.string.prop_wave_dencity));
        DISPLAY_NAME_MAP.put("Wave Speed", String.valueOf(R.string.prop_wave_speed));
        DISPLAY_NAME_MAP.put("Wave Height", String.valueOf(R.string.prop_wave_height));
        DISPLAY_NAME_MAP.put("Wave Stretch", String.valueOf(R.string.prop_wave_stretch));
        DISPLAY_NAME_MAP.put("Wave Sharpness", String.valueOf(R.string.prop_wave_sharpness));
        DISPLAY_NAME_MAP.put("Lines", String.valueOf(R.string.prop_lines));
        DISPLAY_NAME_MAP.put("Count", String.valueOf(R.string.prop_count));
        DISPLAY_NAME_MAP.put("Width", String.valueOf(R.string.prop_width));
        DISPLAY_NAME_MAP.put("Length Random", String.valueOf(R.string.prop_length_random));
        DISPLAY_NAME_MAP.put("Width Random", String.valueOf(R.string.prop_width_random));
        DISPLAY_NAME_MAP.put("Fade Edges", String.valueOf(R.string.prop_fade_edges));
        DISPLAY_NAME_MAP.put("Spread", String.valueOf(R.string.prop_spread));
        DISPLAY_NAME_MAP.put("Angle Spread", String.valueOf(R.string.prop_angle_spread));
        DISPLAY_NAME_MAP.put("Levels", String.valueOf(R.string.prop_levels));
        DISPLAY_NAME_MAP.put("Hue", String.valueOf(R.string.prop_hue));
        DISPLAY_NAME_MAP.put("Red Gain", String.valueOf(R.string.prop_red_gain));
        DISPLAY_NAME_MAP.put("Green Gain", String.valueOf(R.string.prop_green_gain));
        DISPLAY_NAME_MAP.put("Blue Gain", String.valueOf(R.string.prop_blue_gain));
        DISPLAY_NAME_MAP.put("Red Lift", String.valueOf(R.string.prop_red_lift));
        DISPLAY_NAME_MAP.put("Green Lift", String.valueOf(R.string.prop_green_lift));
        DISPLAY_NAME_MAP.put("Blue Lift", String.valueOf(R.string.prop_blue_lift));
        DISPLAY_NAME_MAP.put("Gamma", String.valueOf(R.string.prop_gamma));
        DISPLAY_NAME_MAP.put("Red Low", String.valueOf(R.string.prop_red_low));
        DISPLAY_NAME_MAP.put("Green Low", String.valueOf(R.string.prop_green_low));
        DISPLAY_NAME_MAP.put("Blue Low", String.valueOf(R.string.prop_blue_low));
        DISPLAY_NAME_MAP.put("Red High", String.valueOf(R.string.prop_red_high));
        DISPLAY_NAME_MAP.put("Green High", String.valueOf(R.string.prop_green_high));
        DISPLAY_NAME_MAP.put("Blue High", String.valueOf(R.string.prop_blue_high));
        DISPLAY_NAME_MAP.put("Animation", String.valueOf(R.string.prop_animation));
        DISPLAY_NAME_MAP.put("Index", String.valueOf(R.string.prop_index));
        DISPLAY_NAME_MAP.put("Line Index", String.valueOf(R.string.prop_line_index));
        DISPLAY_NAME_MAP.put("Time Offset", String.valueOf(R.string.prop_time_offset));
        DISPLAY_NAME_MAP.put("Anim Type", String.valueOf(R.string.prop_anim_type));
        DISPLAY_NAME_MAP.put("Curve In", String.valueOf(R.string.prop_curve_in));
        DISPLAY_NAME_MAP.put("Curve Out", String.valueOf(R.string.prop_curve_out));
        DISPLAY_NAME_MAP.put("Fade In Duration", String.valueOf(R.string.prop_fade_in_duration));
        DISPLAY_NAME_MAP.put("Fade Out Duration", String.valueOf(R.string.prop_fade_out_duration));
        DISPLAY_NAME_MAP.put("Move Start Y", String.valueOf(R.string.prop_move_start_y));
        DISPLAY_NAME_MAP.put("Move End Y", String.valueOf(R.string.prop_move_end_y));
        DISPLAY_NAME_MAP.put("Move Duration Start", String.valueOf(R.string.prop_move_duration_start));
        DISPLAY_NAME_MAP.put("Move Duration End", String.valueOf(R.string.prop_move_duration_end));
        DISPLAY_NAME_MAP.put("Line Count", String.valueOf(R.string.prop_line_count));
        DISPLAY_NAME_MAP.put("Highlight Line Index", String.valueOf(R.string.prop_highlight_line_index));
        DISPLAY_NAME_MAP.put("Scroll Curve", String.valueOf(R.string.prop_scroll_curve));
        DISPLAY_NAME_MAP.put("Scroll Speed", String.valueOf(R.string.prop_scroll_speed));
        DISPLAY_NAME_MAP.put("Line Spacing", String.valueOf(R.string.prop_line_spacing));
        DISPLAY_NAME_MAP.put("Scroll Direction", String.valueOf(R.string.prop_scroll_direction));
        DISPLAY_NAME_MAP.put("Highlight Transition", String.valueOf(R.string.prop_highlight_transition));
        DISPLAY_NAME_MAP.put("Transition Duration", String.valueOf(R.string.prop_transition_duration));
        DISPLAY_NAME_MAP.put("Normal Color", String.valueOf(R.string.prop_normal_color));
        DISPLAY_NAME_MAP.put("Highlight Color", String.valueOf(R.string.prop_highlight_color));
        DISPLAY_NAME_MAP.put("Normal Alpha", String.valueOf(R.string.prop_normal_alpha));
        DISPLAY_NAME_MAP.put("Highlight Alpha", String.valueOf(R.string.prop_highlight_alpha));
        DISPLAY_NAME_MAP.put(SegmentPathCircle.typeName, String.valueOf(R.string.prop_circle));
        DISPLAY_NAME_MAP.put("Fill", String.valueOf(R.string.prop_fill));
        DISPLAY_NAME_MAP.put("Scale Compensation", String.valueOf(R.string.prop_scale_compensation));
        DISPLAY_NAME_MAP.put("Sphere", String.valueOf(R.string.prop_sphere));
        DISPLAY_NAME_MAP.put("Fade", String.valueOf(R.string.prop_fade));
        DISPLAY_NAME_MAP.put("Fade In Curve", String.valueOf(R.string.prop_fade_in_curve));
        DISPLAY_NAME_MAP.put("Fade Out Curve", String.valueOf(R.string.prop_fade_out_curve));
        DISPLAY_NAME_MAP.put("Move", String.valueOf(R.string.prop_move));
        DISPLAY_NAME_MAP.put("Move In Curve", String.valueOf(R.string.prop_move_in_curve));
        DISPLAY_NAME_MAP.put("Move Out Curve", String.valueOf(R.string.prop_move_out_curve));
        DISPLAY_NAME_MAP.put("Scroll", String.valueOf(R.string.prop_scroll));
        DISPLAY_NAME_MAP.put(LyricsElement.typeName, String.valueOf(R.string.prop_lyrics));
        DISPLAY_NAME_MAP.put("Normal", String.valueOf(R.string.prop_normal));
        DISPLAY_NAME_MAP.put("Highlight", String.valueOf(R.string.prop_highlight));
        DISPLAY_NAME_MAP.put("Normal Scale", String.valueOf(R.string.prop_normal_scale));
        DISPLAY_NAME_MAP.put("Fade Duration", String.valueOf(R.string.prop_fade_duration));
        DISPLAY_NAME_MAP.put("Scale In Duration", String.valueOf(R.string.prop_scale_in_duration));
        DISPLAY_NAME_MAP.put("Scale Out Duration", String.valueOf(R.string.prop_scale_out_duration));
        DISPLAY_NAME_MAP.put("Scale In Curve", String.valueOf(R.string.prop_scale_in_curve));
        DISPLAY_NAME_MAP.put("Scale Out Curve", String.valueOf(R.string.prop_scale_out_curve));
        DISPLAY_NAME_MAP.put("Highlight Scale", String.valueOf(R.string.prop_highlight_scale));
        DISPLAY_NAME_MAP.put("Highlight YOffset", String.valueOf(R.string.prop_highlight_y_offset));
        DISPLAY_NAME_MAP.put("Invert Mode", String.valueOf(R.string.saved_to_template_suffix));
        DISPLAY_NAME_MAP.put("Luma Low", String.valueOf(R.string.prop_luma_low));
        DISPLAY_NAME_MAP.put("Luma High", String.valueOf(R.string.prop_luma_high));
        DISPLAY_NAME_MAP.put("Luma Out Low", String.valueOf(R.string.prop_luma_out_low));
        DISPLAY_NAME_MAP.put("Luma Out High", String.valueOf(R.string.prop_luma_out_high));
        DISPLAY_NAME_MAP.put("Luma Levels", String.valueOf(R.string.prop_luma_levels));
        DISPLAY_NAME_MAP.put("Channel Levels", String.valueOf(R.string.prop_channel_levels));
        sResIdMap = new HashMap<>();
        sResIdMap.put(BlendMode.typeName0, Integer.valueOf(R.string.blend_alpha));
        sResIdMap.put(BlendMode.typeName5, Integer.valueOf(R.string.blend_add_alpha));
        sResIdMap.put(BlendMode.typeName4, Integer.valueOf(R.string.blend_pre_mul_alpha));
        sResIdMap.put(BlendMode.typeName1, Integer.valueOf(R.string.blend_screen));
        sResIdMap.put(BlendMode.typeName2, Integer.valueOf(R.string.blend_add));
        sResIdMap.put(BlendMode.typeName3, Integer.valueOf(R.string.blend_over));
        sResIdMap.put(AnchorMode.typeName0, Integer.valueOf(R.string.position_unset));
        sResIdMap.put("start", Integer.valueOf(R.string.position_start));
        sResIdMap.put("center", Integer.valueOf(R.string.position_center));
        sResIdMap.put("end", Integer.valueOf(R.string.position_end));
        sResIdMap.put(MeasureDefs.Nothing, Integer.valueOf(R.string.anim_nothing));
        sResIdMap.put(MeasureDefs.beat, Integer.valueOf(R.string.anim_beat));
        sResIdMap.put(MeasureDefs.beatCamShakeMore, Integer.valueOf(R.string.anim_beat_cam_shake_more));
        sResIdMap.put(MeasureDefs.beatCamShakeLess, Integer.valueOf(R.string.anim_beat_cam_shake_less));
        sResIdMap.put(MeasureDefs.beatRandomShake, Integer.valueOf(R.string.anim_beat_random_shake));
        sResIdMap.put(MeasureDefs.beatCamShakeRotMore, Integer.valueOf(R.string.anim_beat_cam_shake_rot_more));
        sResIdMap.put(MeasureDefs.beatCamShakeRotLess, Integer.valueOf(R.string.anim_beat_cam_shake_rot_less));
        sResIdMap.put(MeasureDefs.beatTriggerAnim, Integer.valueOf(R.string.anim_beat_trigger_anim));
        sResIdMap.put(MeasureDefs.beatTriggerRandom, Integer.valueOf(R.string.anim_beat_trigger_random));
        sResIdMap.put(MeasureDefs.constantCamShakeMore, Integer.valueOf(R.string.anim_constant_shake_more));
        sResIdMap.put(MeasureDefs.constantCamShakeLess, Integer.valueOf(R.string.anim_constant_shake));
        sResIdMap.put(MeasureDefs.constantCamShakeRotMore, Integer.valueOf(R.string.anim_constant_shake_rot_more));
        sResIdMap.put(MeasureDefs.constantCamShakeRotLess, Integer.valueOf(R.string.anim_constant_shake_rot_less));
        sResIdMap.put(MeasureDefs.trackPosition, Integer.valueOf(R.string.anim_track_position));
        sResIdMap.put(MeasureDefs.totalTime, Integer.valueOf(R.string.anim_total_time));
        sResIdMap.put(MeasureDefs.totalTimeBackward, Integer.valueOf(R.string.anim_total_time_backward));
        sResIdMap.put(MeasureDefs.totalTimeWhenPlaying, Integer.valueOf(R.string.anim_total_time_when_playing));
        sResIdMap.put(MeasureDefs.totalTimeAndBeat, Integer.valueOf(R.string.anim_total_time_and_beat));
        sResIdMap.put(MeasureDefs.TotalTimeAndBack, Integer.valueOf(R.string.anim_total_time_and_back));
        sResIdMap.put(MeasureDefs.trackSetPosition, Integer.valueOf(R.string.anim_track_set_position));
        sResIdMap.put(MeasureDefs.infiniteMotion, Integer.valueOf(R.string.anim_infinite_motion));
        sResIdMap.put(MeasureDefs.infiniteMotionBackward, Integer.valueOf(R.string.anim_infinite_motion_backward));
        sResIdMap.put(MeasureDefs.infiniteMotionWhenPlaying, Integer.valueOf(R.string.anim_infinite_motion_when_playing));
        sResIdMap.put("Flip Every Other", Integer.valueOf(R.string.anim_flip_every_other));
        sResIdMap.put("Sides", Integer.valueOf(R.string.shape_sides));
        sResIdMap.put("None", Integer.valueOf(R.string.shape_none));
        sResIdMap.put("Line", Integer.valueOf(R.string.shape_line));
        sResIdMap.put(SegmentRendererSharpBar.typeName, Integer.valueOf(R.string.shape_sharp_bars));
        sResIdMap.put(SegmentRendererRoundBar.typeName, Integer.valueOf(R.string.shape_round_bars));
        sResIdMap.put(VortexForceField.typeName, Integer.valueOf(R.string.shape_vortex));
        sResIdMap.put("Transparency", Integer.valueOf(R.string.transparency_mode_transparency));
        sResIdMap.put("TransparencyAndBlacks", Integer.valueOf(R.string.transparency_mode_transparency_and_blacks));
        sResIdMap.put("TransparencyAndWhites", Integer.valueOf(R.string.transparency_mode_transparency_and_whites));
        sResIdMap.put("InvertedTransparency", Integer.valueOf(R.string.transparency_mode_inverted_transparency));
        sResIdMap.put(SegmentAudioSpectrumDataPhaseS.typeName, Integer.valueOf(R.string.spectrum_type_spectrum2));
        sResIdMap.put(SegmentAudioSpectrumData.typeName, Integer.valueOf(R.string.spectrum_type_spectrum));
        sResIdMap.put(SegmentAudioWaveformData.typeName, Integer.valueOf(R.string.spectrum_type_waveform));
        sResIdMap.put("Horizontal", Integer.valueOf(R.string.direction_horizontal));
        sResIdMap.put("Vertical", Integer.valueOf(R.string.direction_vertical));
        sResIdMap.put("HorizontalAndVertical", Integer.valueOf(R.string.direction_horizontal_and_vertical));
        sResIdMap.put("HorizontalLine", Integer.valueOf(R.string.direction_horizontal_line));
        sResIdMap.put(SegmentPathCircle.typeName, Integer.valueOf(R.string.shape_circle));
        sResIdMap.put(SegmentPathSided.typeName, Integer.valueOf(R.string.shape_sided_polygon));
        sResIdMap.put("Letter", Integer.valueOf(R.string.shape_letter));
        sResIdMap.put("EffectTransform", Integer.valueOf(R.string.control_effect_transform));
        sResIdMap.put("Manual", Integer.valueOf(R.string.control_manual));
        sResIdMap.put("Bars", Integer.valueOf(R.string.bar_bars));
        sResIdMap.put(AreaRect.typeName, Integer.valueOf(R.string.bar_rectangle));
        sResIdMap.put("Center", Integer.valueOf(R.string.align_center));
        sResIdMap.put("Left", Integer.valueOf(R.string.align_left));
        sResIdMap.put("Right", Integer.valueOf(R.string.align_right));
        sResIdMap.put("Basic", Integer.valueOf(R.string.spinner_basic));
        sResIdMap.put("Fade", Integer.valueOf(R.string.spinner_fade));
        sResIdMap.put("Move", Integer.valueOf(R.string.spinner_move));
        sResIdMap.put("Scroll", Integer.valueOf(R.string.spinner_scroll));
        sResIdMap.put("Linear", Integer.valueOf(R.string.spinner_linear));
        sResIdMap.put("EaseIn", Integer.valueOf(R.string.spinner_ease_in));
        sResIdMap.put("EaseInQuad", Integer.valueOf(R.string.spinner_ease_in_quad));
        sResIdMap.put("EaseInCubic", Integer.valueOf(R.string.spinner_ease_in_cubic));
        sResIdMap.put("EaseOut", Integer.valueOf(R.string.spinner_ease_out));
        sResIdMap.put("EaseOutQuad", Integer.valueOf(R.string.spinner_ease_out_quad));
        sResIdMap.put("EaseOutCubic", Integer.valueOf(R.string.spinner_ease_out_cubic));
        sResIdMap.put("EaseInOut", Integer.valueOf(R.string.spinner_ease_in_out));
        sResIdMap.put("EaseInOutQuad", Integer.valueOf(R.string.spinner_ease_in_out_quad));
        sResIdMap.put("EaseInOutCubic", Integer.valueOf(R.string.spinner_ease_in_out_cubic));
        sResIdMap.put("Circular", Integer.valueOf(R.string.spinner_circular));
        sResIdMap.put("Back", Integer.valueOf(R.string.spinner_back));
        sResIdMap.put("Bounce", Integer.valueOf(R.string.spinner_bounce));
        sResIdMap.put("Elastic", Integer.valueOf(R.string.spinner_elastic));
        sResIdMap.put("Sine", Integer.valueOf(R.string.spinner_sine));
        sResIdMap.put("Auto", Integer.valueOf(R.string.spinner_auto));
        sResIdMap.put("Artist", Integer.valueOf(R.string.spinner_artist));
        sResIdMap.put("Title", Integer.valueOf(R.string.spinner_title));
        sResIdMap.put("Custom", Integer.valueOf(R.string.spinner_custom));
        sResIdMap.put(MeasureDefs.beatPeak, Integer.valueOf(R.string.spinner_beat_peak));
        sResIdMap.put("Instant", Integer.valueOf(R.string.spinner_instant));
        sResIdMap.put("Up", Integer.valueOf(R.string.spinner_up));
        sResIdMap.put("Down", Integer.valueOf(R.string.spinner_down));
        sResIdMap.put("Scale", Integer.valueOf(R.string.spinner_scale));
        sResIdMap.put("FadeAndScale", Integer.valueOf(R.string.spinner_fade_and_scale));
    }

    private static int argb(int i, int i2) {
        return (i << 24) | (16777215 & i2);
    }

    public CustomizeVisView1(CustomizeVisDialog customizeVisDialog) {
        this.customizeVisDialog = customizeVisDialog;
    }

    public static String getDisplayNameFromRes(Context context, String str) {
        String str2;
        if (context != null && str != null && (str2 = DISPLAY_NAME_MAP.get(str)) != null) {
            try {
                return context.getString(Integer.parseInt(str2));
            } catch (Exception e) {
                return str;
            }
        }
        return str;
    }

    private static String formatPropertyDisplayName(String str) {
        return formatPropertyDisplayName(str, null);
    }

    private static String formatPropertyDisplayNameWoPrefix(String str, String str2) {
        return formatPropertyDisplayName(Utils.stringSplitInTwoGetSecond(str, 95, str));
    }

    public static String formatPropertyDisplayName(String str, String str2) {
        boolean zIsDigit;
        StringBuilder sb = new StringBuilder();
        if (str.length() > 0) {
            char upperCase = Character.toUpperCase(str.charAt(0));
            sb.append(upperCase);
            zIsDigit = Character.isDigit(upperCase);
        } else {
            zIsDigit = false;
        }
        boolean z = zIsDigit;
        for (int i = 1; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            boolean z2 = Character.isUpperCase(cCharAt) || Character.isDigit(cCharAt);
            if (z && z2) {
                sb.append(' ');
            }
            sb.append(cCharAt);
            z = !z2;
        }
        if (str2 != null && str2.length() > 0) {
            sb.append(" (");
            sb.append(str2);
            sb.append(')');
        }
        return sb.toString();
    }

    Activity getActivity() {
        return this.customizeVisDialog.getActivity();
    }

    boolean isViewCreated() {
        return this.txtElementTitle != null;
    }

    public void setCollapsed(boolean z) {
        this.view1RootView.setVisibility(z ? 8 : 0);
    }

    public void onCreatedView1(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view1RootView = viewGroup.findViewById(R.id.view1RootView);
        this.linearLayoutRootContent = (ViewGroup) viewGroup.findViewById(R.id.linearLayoutContent);
        this.txtElementTitle = (TextView) viewGroup.findViewById(R.id.txtElementTitle);
        ((ImageButton) viewGroup.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CustomizeVisView1.this.customizeVisDialog.goToView(0);
            }
        });
        ((ImageButton) viewGroup.findViewById(R.id.btnInfo)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CustomizeVisView1.this.currentElementCustom != null) {
                    CustomizeVisView1.this.customizeVisDialog.InvokeOnElementWikiInfoAction(CustomizeVisView1.this.currentElementCustom.getTypeName(), CustomizeVisView1.this.currentElementCustom.getVersionId());
                }
            }
        });
        ImageButton imageButton = (ImageButton) viewGroup.findViewById(R.id.btnResetElement);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EventsGlobalTextNotifier.onTextMsg.invoke(view.getResources().getString(R.string.vis_customization_hold_to_reset_element));
            }
        });
        imageButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.4
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (CustomizeVisView1.this.currentElementCustom != null) {
                    CustomizeVisView1.this.customizeVisDialog.InvokeOnActionResetElement(CustomizeVisView1.this.currentElementCustom, null);
                }
                return true;
            }
        });
        this.spinnerVersion = (Spinner) viewGroup.findViewById(R.id.spinnerVersion);
        this.spinnerVersionOnItemClickListener = new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.5
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                CustomizeVisView1.this.customizeVisDialog.ApplySpinnerTheme(adapterView);
                if (CustomizeVisView1.this.currentElementCustom != null) {
                    String customizationName = CustomizeVisView1.this.currentElementCustom.getCustomizationName();
                    String versionId = CustomizeVisView1.this.currentElementCustom.getVersionId();
                    Tuple2<String[], String> tuple2InvokeOnRequestElementAvailableVersions = CustomizeVisView1.this.customizeVisDialog.InvokeOnRequestElementAvailableVersions(customizationName, versionId);
                    String[] strArr = tuple2InvokeOnRequestElementAvailableVersions.obj1;
                    String str = tuple2InvokeOnRequestElementAvailableVersions.obj2;
                    if (i >= 0 && i < strArr.length) {
                        str = strArr[i];
                    }
                    if (!VisualizerThemeInfo.CompareElementVersions(versionId, str)) {
                        CustomizeVisView1.this.currentElementCustom.setVersionId(str);
                        CustomizeVisView1.this.customizeVisDialog.onChildPropertyChanged();
                    }
                }
            }
        };
        this.propertiesUpdateValueCb.clear();
    }

    void parsePropertyData(CustomPropertiesList customPropertiesList) {
        String versionId;
        if (this.customizeVisDialog.isView1Created() && isViewCreated()) {
            LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.customizeVisDialog.getActivity());
            this.currentElementCustom = customPropertiesList;
            if (customPropertiesList == null) {
                this.txtElementTitle.setText(R.string.nothing_to_show);
                this.linearLayoutRootContent.removeAllViews();
                this.propertiesUpdateValueCb.clear();
                return;
            }
            this.txtElementTitle.setText(customPropertiesList.getCustomizationName());
            this.spinnerVersionInitalSelectedPosition = -2;
            this.spinnerVersion.setOnItemSelectedListener(null);
            CustomPropertiesList customPropertiesList2 = this.currentElementCustom;
            String customizationName = "";
            if (customPropertiesList2 != null) {
                customizationName = customPropertiesList2.getCustomizationName();
                versionId = this.currentElementCustom.getVersionId();
            } else {
                versionId = "";
            }
            Tuple2<String[], String> tuple2InvokeOnRequestElementAvailableVersions = this.customizeVisDialog.InvokeOnRequestElementAvailableVersions(customizationName, versionId);
            if (tuple2InvokeOnRequestElementAvailableVersions.obj1.length > 1) {
                this.spinnerVersion.setVisibility(0);
                int iStringToIndex = Utils.StringToIndex(tuple2InvokeOnRequestElementAvailableVersions.obj1, versionId);
                int length = tuple2InvokeOnRequestElementAvailableVersions.obj1.length;
                String[] strArr = new String[length];
                for (int i = 0; i < length; i++) {
                    strArr[i] = this.customizeVisDialog.getResources().getString(R.string.vis_customization_ver_x, tuple2InvokeOnRequestElementAvailableVersions.obj1[i]);
                }
                this.spinnerVersion.setAdapter((SpinnerAdapter) new ArrayAdapter(this.customizeVisDialog.getActivity(), android.R.layout.simple_spinner_item, strArr));
                this.spinnerVersionInitalSelectedPosition = iStringToIndex;
                if (iStringToIndex >= 0) {
                    this.spinnerVersion.setSelection(iStringToIndex, false);
                }
                this.customizeVisDialog.ApplySpinnerTheme(this.spinnerVersion);
                this.spinnerVersion.setOnItemSelectedListener(this.spinnerVersionOnItemClickListener);
            } else {
                this.spinnerVersion.setVisibility(4);
            }
            this.linearLayoutRootContent.removeAllViews();
            this.propertiesUpdateValueCb.clear();
            Map<String, MultiList<String, Integer>> mapGetAllPropertiesGroupedSortedByOrder = customPropertiesList.GetAllPropertiesGroupedSortedByOrder();
            for (final String str : mapGetAllPropertiesGroupedSortedByOrder.keySet()) {
                MultiList<String, Integer> multiList = mapGetAllPropertiesGroupedSortedByOrder.get(str);
                View viewInflate = layoutInflaterFrom.inflate(R.layout.dialog_customize_vis_element_group, (ViewGroup) null);
                final ViewGroup viewGroup = (ViewGroup) viewInflate.findViewById(R.id.linearLayoutContent);
                TextView textView = (TextView) viewInflate.findViewById(R.id.groupTitle);
                final View viewFindViewById = viewInflate.findViewById(R.id.btnExpand);
                ViewGroup viewGroup2 = (ViewGroup) viewInflate.findViewById(R.id.groupHeader);
                if (parseDataRecursive(this.customizeVisDialog, layoutInflaterFrom, customPropertiesList, viewGroup, multiList.iterator1()) > 0) {
                    String displayNameFromRes = getDisplayNameFromRes(this.customizeVisDialog.getActivity(), formatPropertyDisplayNameWoPrefix(str, null));
                    if (displayNameFromRes == null || displayNameFromRes.equals(str)) {
                        displayNameFromRes = formatPropertyDisplayNameWoPrefix(str, null);
                    }
                    textView.setText(displayNameFromRes);
                    this.linearLayoutRootContent.addView(viewInflate);
                    viewGroup2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.6
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            if (viewGroup.getVisibility() == 0) {
                                viewFindViewById.animate().rotation(0.0f);
                                viewGroup.setVisibility(8);
                                CustomizeVisView1.this.propertiesExpandViews.put(str, false);
                            } else {
                                viewFindViewById.animate().rotation(-90.0f);
                                viewGroup.setVisibility(0);
                                CustomizeVisView1.this.propertiesExpandViews.put(str, true);
                            }
                        }
                    });
                    Boolean bool = this.propertiesExpandViews.get(str);
                    if (bool != null && !bool.booleanValue()) {
                        viewFindViewById.setRotation(0.0f);
                        viewGroup.setVisibility(8);
                    } else if (bool != null && bool.booleanValue()) {
                        viewFindViewById.setRotation(-90.0f);
                        viewGroup.setVisibility(0);
                    }
                }
            }
        }
    }

    public static boolean createPropertyView(CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, CustomPropertiesList customPropertiesList, ViewGroup viewGroup, String str, boolean z, Func<Action1<Object>> func) {
        String propertyType = customPropertiesList.getPropertyType(str);
        String propertyHint = customPropertiesList.getPropertyHint(str);
        String[] propertyTypeParts = CustomPropertiesList.getPropertyTypeParts(propertyType);
        String displayNameFromRes = getDisplayNameFromRes(customizeVisDialog.getActivity(), formatPropertyDisplayName(str, propertyHint));
        if (displayNameFromRes == null || displayNameFromRes.equals(formatPropertyDisplayName(str, propertyHint))) {
            displayNameFromRes = formatPropertyDisplayName(str, propertyHint);
        }
        if (propertyTypeParts[0].equals("i") && propertyTypeParts.length >= 3) {
            createPropertyViewInt(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, Utils.strToIntSafe(propertyTypeParts[1], 0), Utils.strToIntSafe(propertyTypeParts[2], 100));
        } else if (propertyTypeParts[0].equals("pb")) {
            createPropertyViewBool(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("b")) {
            createPropertyViewBool(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("crgb")) {
            createPropertyViewRGBA(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, false, false, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("crgba")) {
            createPropertyViewRGBA(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, true, false, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("crgb_hl")) {
            createPropertyViewRGBA(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, false, false, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("chsl4f")) {
            createPropertyViewRGBA(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, false, true, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("chsla4f")) {
            createPropertyViewRGBA(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, true, true, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("f") && propertyTypeParts.length >= 3) {
            float fStrToFloatSafe = Utils.strToFloatSafe(propertyTypeParts[1], 0.0f);
            float fStrToFloatSafe2 = Utils.strToFloatSafe(propertyTypeParts[2], 100.0f);
            createPropertyViewFloat(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, fStrToFloatSafe, fStrToFloatSafe2, (fStrToFloatSafe2 - fStrToFloatSafe) / 400.0f);
        } else if (propertyTypeParts[0].equals("f2") && propertyTypeParts.length >= 3) {
            float fStrToFloatSafe3 = Utils.strToFloatSafe(propertyTypeParts[1], 0.0f);
            float fStrToFloatSafe4 = Utils.strToFloatSafe(propertyTypeParts[2], 100.0f);
            createPropertyViewVec2f(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, fStrToFloatSafe3, fStrToFloatSafe4, (fStrToFloatSafe4 - fStrToFloatSafe3) / 400.0f);
        } else if (propertyTypeParts[0].equals("mvarf") && propertyTypeParts.length >= 3) {
            float fStrToFloatSafe5 = Utils.strToFloatSafe(propertyTypeParts[1], 0.0f);
            float fStrToFloatSafe6 = Utils.strToFloatSafe(propertyTypeParts[2], 100.0f);
            CustomizeVisView1MVar.createPropertyViewMVarFloat(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, false, displayNameFromRes, str, fStrToFloatSafe5, fStrToFloatSafe6, (fStrToFloatSafe6 - fStrToFloatSafe5) / 400.0f);
        } else if (propertyTypeParts[0].equals("mvarf2") && propertyTypeParts.length >= 3) {
            float fStrToFloatSafe7 = Utils.strToFloatSafe(propertyTypeParts[1], 0.0f);
            float fStrToFloatSafe8 = Utils.strToFloatSafe(propertyTypeParts[2], 100.0f);
            CustomizeVisView1MVar.createPropertyViewMVarFloat(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, true, displayNameFromRes, str, fStrToFloatSafe7, fStrToFloatSafe8, (fStrToFloatSafe8 - fStrToFloatSafe7) / 400.0f);
        } else if (propertyTypeParts[0].equals("txt")) {
            createPropertyViewText(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("ptxt")) {
            createPropertyViewText(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str);
        } else if (propertyTypeParts[0].equals("img")) {
            int length = propertyTypeParts.length - 1;
            String[] strArr = new String[length];
            System.arraycopy(propertyTypeParts, 1, strArr, 0, length);
            createPropertyViewImage(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, strArr);
        } else if (propertyTypeParts[0].equals("pimg")) {
            int length2 = propertyTypeParts.length - 1;
            String[] strArr2 = new String[length2];
            System.arraycopy(propertyTypeParts, 1, strArr2, 0, length2);
            createPropertyViewImage(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, strArr2);
        } else if (propertyTypeParts[0].equals("asset")) {
            int length3 = propertyTypeParts.length - 1;
            String[] strArr3 = new String[length3];
            System.arraycopy(propertyTypeParts, 1, strArr3, 0, length3);
            createPropertyViewAsset(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, strArr3);
        } else if (propertyTypeParts[0].equals("passet")) {
            int length4 = propertyTypeParts.length - 1;
            String[] strArr4 = new String[length4];
            System.arraycopy(propertyTypeParts, 1, strArr4, 0, length4);
            createPropertyViewAsset(customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, strArr4);
        } else if (propertyTypeParts[0].equals("sel")) {
            int length5 = propertyTypeParts.length - 1;
            String[] strArr5 = new String[length5];
            System.arraycopy(propertyTypeParts, 1, strArr5, 0, length5);
            createChildView(true, customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, strArr5);
        } else {
            if (!propertyTypeParts[0].equals("_child")) {
                return false;
            }
            int length6 = propertyTypeParts.length - 1;
            String[] strArr6 = new String[length6];
            System.arraycopy(propertyTypeParts, 1, strArr6, 0, length6);
            createChildView(false, customizeVisDialog, layoutInflater, customPropertiesList, z, func, viewGroup, displayNameFromRes, str, strArr6);
        }
        return true;
    }

    private static int parseDataRecursive(CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, CustomPropertiesList customPropertiesList, ViewGroup viewGroup, Iterator<String> it) {
        Func<Action1<Object>> func = new Func<Action1<Object>>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Func.Func
            public Action1<Object> onInvoke() {
                return null;
            }
        };
        int i = 0;
        while (it.hasNext()) {
            int i2 = i + 1;
            i = !createPropertyView(customizeVisDialog, layoutInflater, customPropertiesList, viewGroup, it.next(), false, func) ? i2 - 1 : i2;
        }
        return i;
    }

    private static void createChildView(final boolean z, final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, boolean z2, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2, final String[] strArr) {
        final CustomPropertiesList child;
        String typeName;
        ViewGroup viewGroup2;
        View viewInflate;
        int i;
        if (z) {
            child = null;
            typeName = customPropertiesList.getPropertySelectString(str2, "");
            viewGroup2 = null;
            viewInflate = layoutInflater.inflate(R.layout.dialog_customize_vis_element_sel, (ViewGroup) null);
        } else {
            child = customPropertiesList.getChild(str2);
            typeName = child.getTypeName();
            View viewInflate2 = layoutInflater.inflate(R.layout.dialog_customize_vis_element_child, (ViewGroup) null);
            viewGroup2 = (ViewGroup) viewInflate2.findViewById(R.id.linearLayoutContent);
            viewInflate = viewInflate2;
        }
        TextView textView = (TextView) viewInflate.findViewById(R.id.title);
        textView.setText(str);
        final boolean[] zArr = new boolean[1];
        Spinner spinner = (Spinner) viewInflate.findViewById(R.id.spinnerType);
        int i2 = 0;
        while (true) {
            if (i2 >= strArr.length) {
                i = -1;
                break;
            } else {
                if (typeName.equals(strArr[i2])) {
                    i = i2;
                    break;
                }
                i2++;
            }
        }
        String[] strArr2 = new String[strArr.length];
        Activity activity = customizeVisDialog.getActivity();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= strArr.length) {
                break;
            }
            String str3 = strArr[i4];
            Integer num = sResIdMap.get(str3);
            if (num != null && activity != null) {
                try {
                    strArr2[i4] = activity.getString(num.intValue());
                } catch (Exception e) {
                    strArr2[i4] = str3;
                }
            } else {
                strArr2[i4] = str3;
            }
            i3 = i4 + 1;
        }
        spinner.setAdapter((SpinnerAdapter) new ArrayAdapter(customizeVisDialog.getActivity(), android.R.layout.simple_spinner_item, strArr2));
        if (i >= 0) {
            spinner.setSelection(i, false);
        }
        customizeVisDialog.ApplySpinnerTheme(spinner);
        zArr[0] = true;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.8
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i5, long j) {
                customizeVisDialog.ApplySpinnerTheme(adapterView);
                if (zArr[0] && i5 >= 0 && i5 < strArr.length) {
                    if (z) {
                        if (!customPropertiesList.getPropertySelectString(str2, "").equals(strArr[i5])) {
                            customPropertiesList.putPropertySelectStringValue(str2, strArr[i5]);
                            customizeVisDialog.onChildPropertyChanged();
                            Action1 action1 = (Action1) func.onInvoke();
                            if (action1 != null) {
                                action1.onInvoke(this);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (!child.getTypeName().equals(strArr[i5])) {
                        child.putChildTypeValue(strArr[i5]);
                        customizeVisDialog.onChildPropertyChanged();
                    }
                }
            }
        });
        if (strArr.length > 0) {
            spinner.setVisibility(0);
        } else {
            spinner.setVisibility(8);
            textView.setTextColor(textView.getResources().getColor(R.color.white_alpha_25));
        }
        if (z) {
            viewGroup.addView(viewInflate);
            return;
        }
        int dataRecursive = parseDataRecursive(customizeVisDialog, layoutInflater, child, viewGroup2, child.GetAllPropertiesSortedByOrder());
        if (strArr.length > 0 || dataRecursive > 0) {
            viewGroup.addView(viewInflate);
        }
        if (dataRecursive <= 0) {
            viewGroup2.setVisibility(8);
        }
    }

    private static void createPropertyCommon(CustomizeVisDialog customizeVisDialog, CustomPropertiesList customPropertiesList, boolean z, View view, String str) {
        createPropertyCommon(customizeVisDialog, customPropertiesList, z, view, str, null);
    }

    protected static Action1<Object> createPropertyCommon(final CustomizeVisDialog customizeVisDialog, final CustomPropertiesList customPropertiesList, boolean z, final View view, final String str, final Action1<Object> action1) {
        if (!z) {
            customizeVisDialog.view1AddPropertyUpdateValueCb(customPropertiesList, str, action1);
        }
        if (z) {
            View viewFindViewById = view.findViewById(R.id.elementDetailContent);
            if (viewFindViewById != null) {
                viewFindViewById.setVisibility(0);
            }
        } else {
            view.findViewById(R.id.elementRoot).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.9
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    customizeVisDialog.onPropertySelected(customPropertiesList, str, view, action1);
                }
            });
        }
        if (action1 != null) {
            action1.onInvoke(null);
        }
        return action1;
    }

    private static void createPropertyViewBool(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2) {
        View viewInflate;
        viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_toggle : R.layout.dialog_customize_vis_element_toggle, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        final CheckBox checkBox = (CheckBox) viewInflate.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(null);
        createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.10
            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                checkBox.setChecked(customPropertiesList.getPropertyBool(str2, false));
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.11
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z3) {
                customPropertiesList.putPropertyBoolValue(str2, z3);
                customizeVisDialog.onPropertyChanged();
                Action1 action1 = (Action1) func.onInvoke();
                if (action1 != null) {
                    action1.onInvoke(this);
                }
            }
        });
        viewGroup.addView(viewInflate);
    }

    private static void createPropertyViewInt(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, final boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2, final int i, final int i2) {
        customPropertiesList.getPropertyInt(str2, i);
        View viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_bar : R.layout.dialog_customize_vis_element_bar, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txtValue);
        final View viewFindViewById = viewInflate.findViewById(R.id.lineBarBg);
        final View viewFindViewById2 = viewInflate.findViewById(R.id.lineBarValue);
        final SeekBar[] seekBarArr = new SeekBar[1];
        final EditText[] editTextArr = new EditText[1];
        if (z) {
            seekBarArr[0] = (SeekBar) viewInflate.findViewById(R.id.seekBar);
            editTextArr[0] = (EditText) viewInflate.findViewById(R.id.editTxt);
            seekBarArr[0].setMax(i2 - i);
        }
        final Action1<Object> action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.12
            boolean updating = false;

            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                if (!this.updating) {
                    this.updating = true;
                    int propertyInt = customPropertiesList.getPropertyInt(str2, i);
                    textView.setText(new StringBuilder().append(propertyInt).toString());
                    View view = viewFindViewById;
                    View view2 = viewFindViewById2;
                    int i3 = i2;
                    int i4 = i;
                    SimpleViewProgressBar.SetProgress(view, view2, i3 - i4, propertyInt - i4);
                    if (z) {
                        if (!seekBarArr[0].equals(obj)) {
                            seekBarArr[0].setProgress(propertyInt - i);
                        }
                        if (!editTextArr[0].equals(obj)) {
                            editTextArr[0].setText(new StringBuilder().append(propertyInt).toString());
                        }
                    }
                    this.updating = false;
                }
            }
        });
        if (z) {
            seekBarArr[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.13
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar, int i3, boolean z2) {
                    if (z2) {
                        customPropertiesList.putPropertyIntValue(str2, i + i3);
                        customizeVisDialog.onPropertyChanged();
                        action1CreatePropertyCommon.onInvoke(seekBarArr[0]);
                        Action1 action1 = (Action1) func.onInvoke();
                        if (action1 != null) {
                            action1.onInvoke(this);
                        }
                    }
                }
            });
            editTextArr[0].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.14
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    customPropertiesList.putPropertyIntValue(str2, Utils.strToIntSafe(editable.toString(), i + (i2 / 2)));
                    customizeVisDialog.onPropertyChanged();
                    action1CreatePropertyCommon.onInvoke(editTextArr[0]);
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(null);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    private static void createPropertyViewRGBA(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, final boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, final boolean z2, final boolean z3, String str, final String str2) {
        final Action1<Object> action1CreatePropertyCommon;
        View viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_crgba : R.layout.dialog_customize_vis_element_crgba, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txtValue);
        final View viewFindViewById = viewInflate.findViewById(R.id.solidValue);
        final ColorPickerView[] colorPickerViewArr = new ColorPickerView[1];
        LightnessSlider[] lightnessSliderArr = new LightnessSlider[1];
        final AlphaSlider[] alphaSliderArr = new AlphaSlider[1];
        if (z) {
            colorPickerViewArr[0] = (ColorPickerView) viewInflate.findViewById(R.id.colorPicker);
            lightnessSliderArr[0] = (LightnessSlider) viewInflate.findViewById(R.id.lightnessSlider);
            alphaSliderArr[0] = (AlphaSlider) viewInflate.findViewById(R.id.alphaSlider);
            colorPickerViewArr[0].setLightnessSlider(lightnessSliderArr[0]);
            colorPickerViewArr[0].setAlphaSlider(alphaSliderArr[0]);
        }
        if (z3) {
            action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.15
                @Override // com.daaw.avee.Common.Action1
                public final void onInvoke(Object obj) {
                    CustomizeVisView1.lambda$createPropertyViewRGBA$0(customPropertiesList, str2, z, z2, alphaSliderArr, colorPickerViewArr, textView, viewFindViewById, obj);
                }
            });
        } else {
            action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.16
                @Override // com.daaw.avee.Common.Action1
                public final void onInvoke(Object obj) {
                    CustomizeVisView1.lambda$createPropertyViewRGBA$1(customPropertiesList, str2, z, z2, alphaSliderArr, colorPickerViewArr, textView, viewFindViewById, obj);
                }
            });
        }
        if (z) {
            colorPickerViewArr[0].addOnColorChangedListener(new OnColorChangedListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.17
                @Override // com.flask.colorpicker.OnColorChangedListener
                public final void onColorChanged(int i) {
                    CustomizeVisView1.lambda$createPropertyViewRGBA$2(z3, customPropertiesList, str2, customizeVisDialog, action1CreatePropertyCommon, colorPickerViewArr, func, i);
                }
            });
            colorPickerViewArr[0].addOnColorSelectedListener(new OnColorSelectedListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.18
                @Override // com.flask.colorpicker.OnColorSelectedListener
                public final void onColorSelected(int i) {
                    CustomizeVisView1.lambda$createPropertyViewRGBA$3(z3, customPropertiesList, str2, customizeVisDialog, action1CreatePropertyCommon, colorPickerViewArr, func, i);
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    static void lambda$createPropertyViewRGBA$0(CustomPropertiesList customPropertiesList, String str, boolean z, boolean z2, AlphaSlider[] alphaSliderArr, ColorPickerView[] colorPickerViewArr, TextView textView, View view, Object obj) {
        int iHlsa1ColorToInt = GraphicsUtils.hlsa1ColorToInt(customPropertiesList.getPropertyArray4f(str, new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
        if (z) {
            if (z2) {
                alphaSliderArr[0].setVisibility(0);
            } else {
                alphaSliderArr[0].setVisibility(8);
            }
            if (!colorPickerViewArr[0].equals(obj)) {
                colorPickerViewArr[0].setColor(iHlsa1ColorToInt, true);
            }
        }
        textView.setText(Utils.intToHexString(iHlsa1ColorToInt));
        view.setBackgroundColor(iHlsa1ColorToInt);
    }

    static void lambda$createPropertyViewRGBA$1(CustomPropertiesList customPropertiesList, String str, boolean z, boolean z2, AlphaSlider[] alphaSliderArr, ColorPickerView[] colorPickerViewArr, TextView textView, View view, Object obj) {
        int propertyInt = customPropertiesList.getPropertyInt(str, -1);
        if (z) {
            if (z2) {
                alphaSliderArr[0].setVisibility(0);
            } else {
                alphaSliderArr[0].setVisibility(8);
            }
            if (!colorPickerViewArr[0].equals(obj)) {
                colorPickerViewArr[0].setColor(propertyInt, true);
            }
        }
        textView.setText(Utils.intToHexString(propertyInt));
        view.setBackgroundColor(propertyInt);
    }

    static void lambda$createPropertyViewRGBA$2(boolean z, CustomPropertiesList customPropertiesList, String str, CustomizeVisDialog customizeVisDialog, Action1 action1, ColorPickerView[] colorPickerViewArr, Func func, int i) {
        if (z) {
            float[] fArr = new float[4];
            GraphicsUtils.intColorToHlsa1(fArr, i);
            customPropertiesList.putPropertyArray4fValue(str, fArr);
        } else {
            customPropertiesList.putPropertyIntValue(str, i);
        }
        customizeVisDialog.onPropertyChanged();
        action1.onInvoke(colorPickerViewArr[0]);
        Action1 action2 = (Action1) func.onInvoke();
        if (action2 != null) {
            action2.onInvoke(null);
        }
    }

    static void lambda$createPropertyViewRGBA$3(boolean z, CustomPropertiesList customPropertiesList, String str, CustomizeVisDialog customizeVisDialog, Action1 action1, ColorPickerView[] colorPickerViewArr, Func func, int i) {
        if (z) {
            float[] fArr = new float[4];
            GraphicsUtils.intColorToHlsa1(fArr, i);
            customPropertiesList.putPropertyArray4fValue(str, fArr);
        } else {
            customPropertiesList.putPropertyIntValue(str, i);
        }
        customizeVisDialog.onPropertyChanged();
        action1.onInvoke(colorPickerViewArr[0]);
        Action1 action2 = (Action1) func.onInvoke();
        if (action2 != null) {
            action2.onInvoke(null);
        }
    }

    private static void createPropertyViewFloat(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, final boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2, final float f, final float f2, final float f3) {
        View viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_bar : R.layout.dialog_customize_vis_element_bar, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txtValue);
        final View viewFindViewById = viewInflate.findViewById(R.id.lineBarBg);
        final View viewFindViewById2 = viewInflate.findViewById(R.id.lineBarValue);
        final SeekBar[] seekBarArr = new SeekBar[1];
        final EditText[] editTextArr = new EditText[1];
        if (z) {
            seekBarArr[0] = (SeekBar) viewInflate.findViewById(R.id.seekBar);
            editTextArr[0] = (EditText) viewInflate.findViewById(R.id.editTxt);
            seekBarArr[0].setMax((int) ((f2 - f) / f3));
        }
        final Action1<Object> action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.19
            boolean updating = false;

            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                if (!this.updating) {
                    this.updating = true;
                    float propertyFloat = customPropertiesList.getPropertyFloat(str2, f);
                    textView.setText(String.format(Locale.US, "%.3f", Float.valueOf(propertyFloat)));
                    View view = viewFindViewById;
                    View view2 = viewFindViewById2;
                    float f4 = f2;
                    float f5 = f;
                    float f6 = f3;
                    SimpleViewProgressBar.SetProgress(view, view2, (f4 - f5) / f6, (propertyFloat - f5) / f6);
                    if (z) {
                        if (!seekBarArr[0].equals(obj)) {
                            seekBarArr[0].setProgress((int) ((propertyFloat - f) / f3));
                        }
                        if (!editTextArr[0].equals(obj)) {
                            if (str2 != null && str2.contains("Trigger")) {
                                float trackDurationForRendering = AppDesign.getTrackDurationForRendering() / 1000.0f;
                                if (trackDurationForRendering > 0.0f) {
                                    editTextArr[0].setText(String.format(Locale.US, "%.1f", Float.valueOf(((propertyFloat + 1.0f) / 2.0f) * trackDurationForRendering)));
                                } else {
                                    editTextArr[0].setText(String.format(Locale.US, "%.3f", Float.valueOf(propertyFloat)));
                                }
                            } else {
                                editTextArr[0].setText(String.format(Locale.US, "%.3f", Float.valueOf(propertyFloat)));
                            }
                        }
                    }
                    this.updating = false;
                }
            }
        });
        if (z) {
            seekBarArr[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.20
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar, int i, boolean z2) {
                    if (z2) {
                        customPropertiesList.putPropertyFloatValue(str2, (i * f3) + f);
                        customizeVisDialog.onPropertyChanged();
                        action1CreatePropertyCommon.onInvoke(seekBarArr[0]);
                        Action1 action1 = (Action1) func.onInvoke();
                        if (action1 != null) {
                            action1.onInvoke(null);
                        }
                    }
                }
            });
            editTextArr[0].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.21
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    float fStrToFloatSafe = Utils.strToFloatSafe(editable.toString(), 0.0f);
                    if (str2 != null && str2.contains("Trigger")) {
                        float trackDurationForRendering = AppDesign.getTrackDurationForRendering() / 1000.0f;
                        if (trackDurationForRendering > 0.0f) {
                            fStrToFloatSafe = Math.max(-1.0f, Math.min(1.0f, ((fStrToFloatSafe / trackDurationForRendering) * 2.0f) - 1.0f));
                        }
                    }
                    customPropertiesList.putPropertyFloatValue(str2, fStrToFloatSafe);
                    customizeVisDialog.onPropertyChanged();
                    action1CreatePropertyCommon.onInvoke(editTextArr[0]);
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(null);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    private static void createPropertyViewVec2f(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, final boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2, final float f, final float f2, final float f3) {
        customPropertiesList.getPropertyVec2f(str2, new Vec2f(f, f));
        View viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_bar_xy : R.layout.dialog_customize_vis_element_bar_xy, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txtValue);
        final View viewFindViewById = viewInflate.findViewById(R.id.lineBarBgX);
        final View viewFindViewById2 = viewInflate.findViewById(R.id.lineBarValueX);
        final View viewFindViewById3 = viewInflate.findViewById(R.id.lineBarBgY);
        final View viewFindViewById4 = viewInflate.findViewById(R.id.lineBarValueY);
        final SeekBar[] seekBarArr = new SeekBar[1];
        final SeekBar[] seekBarArr2 = new SeekBar[1];
        final EditText[] editTextArr = new EditText[1];
        final EditText[] editTextArr2 = new EditText[1];
        if (z) {
            seekBarArr[0] = (SeekBar) viewInflate.findViewById(R.id.seekBarX);
            seekBarArr2[0] = (SeekBar) viewInflate.findViewById(R.id.seekBarY);
            editTextArr[0] = (EditText) viewInflate.findViewById(R.id.editTxtX);
            editTextArr2[0] = (EditText) viewInflate.findViewById(R.id.editTxtY);
            int i = (int) ((f2 - f) / f3);
            seekBarArr[0].setMax(i);
            seekBarArr2[0].setMax(i);
        }
        final Action1<Object> action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.22
            boolean updating = false;

            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                if (!this.updating) {
                    this.updating = true;
                    CustomPropertiesList customPropertiesList2 = customPropertiesList;
                    String str3 = str2;
                    float f4 = f;
                    Vec2f propertyVec2f = customPropertiesList2.getPropertyVec2f(str3, new Vec2f(f4, f4));
                    textView.setText(String.format(Locale.US, "%.3f   %.3f", Float.valueOf(propertyVec2f.x), Float.valueOf(propertyVec2f.y)));
                    SimpleViewProgressBar.SetProgress(viewFindViewById, viewFindViewById2, (f2 - f) / f3, (propertyVec2f.x - f) / f3);
                    SimpleViewProgressBar.SetProgress(viewFindViewById3, viewFindViewById4, (f2 - f) / f3, (propertyVec2f.y - f) / f3);
                    if (z) {
                        if (!seekBarArr[0].equals(obj)) {
                            seekBarArr[0].setProgress((int) ((propertyVec2f.x - f) / f3));
                        }
                        if (!seekBarArr2[0].equals(obj)) {
                            seekBarArr2[0].setProgress((int) ((propertyVec2f.y - f) / f3));
                        }
                        if (!editTextArr[0].equals(obj)) {
                            editTextArr[0].setText(String.format(Locale.US, "%.3f", Float.valueOf(propertyVec2f.x)));
                        }
                        if (!editTextArr2[0].equals(obj)) {
                            editTextArr2[0].setText(String.format(Locale.US, "%.3f", Float.valueOf(propertyVec2f.y)));
                        }
                    }
                    this.updating = false;
                }
            }
        });
        if (z) {
            seekBarArr[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.23
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar, int i2, boolean z2) {
                    if (z2) {
                        CustomPropertiesList customPropertiesList2 = customPropertiesList;
                        String str3 = str2;
                        float f4 = f;
                        customPropertiesList.putPropertyVec2fValue(str2, new Vec2f((i2 * f3) + f, customPropertiesList2.getPropertyVec2f(str3, new Vec2f(f4, f4)).y));
                        customizeVisDialog.onPropertyChanged();
                        action1CreatePropertyCommon.onInvoke(seekBarArr[0]);
                        Action1 action1 = (Action1) func.onInvoke();
                        if (action1 != null) {
                            action1.onInvoke(null);
                        }
                    }
                }
            });
            seekBarArr2[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.24
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar, int i2, boolean z2) {
                    if (z2) {
                        CustomPropertiesList customPropertiesList2 = customPropertiesList;
                        String str3 = str2;
                        float f4 = f;
                        customPropertiesList.putPropertyVec2fValue(str2, new Vec2f(customPropertiesList2.getPropertyVec2f(str3, new Vec2f(f4, f4)).x, (i2 * f3) + f));
                        customizeVisDialog.onPropertyChanged();
                        action1CreatePropertyCommon.onInvoke(seekBarArr2[0]);
                        Action1 action1 = (Action1) func.onInvoke();
                        if (action1 != null) {
                            action1.onInvoke(null);
                        }
                    }
                }
            });
            editTextArr[0].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.25
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    float fStrToFloatSafe = Utils.strToFloatSafe(editable.toString(), f + (f2 * 0.5f));
                    CustomPropertiesList customPropertiesList2 = customPropertiesList;
                    String str3 = str2;
                    float f4 = f;
                    customPropertiesList.putPropertyVec2fValue(str2, new Vec2f(fStrToFloatSafe, customPropertiesList2.getPropertyVec2f(str3, new Vec2f(f4, f4)).y));
                    customizeVisDialog.onPropertyChanged();
                    action1CreatePropertyCommon.onInvoke(editTextArr[0]);
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(null);
                    }
                }
            });
            editTextArr2[0].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.26
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    float fStrToFloatSafe = Utils.strToFloatSafe(editable.toString(), f + (f2 * 0.5f));
                    CustomPropertiesList customPropertiesList2 = customPropertiesList;
                    String str3 = str2;
                    float f4 = f;
                    customPropertiesList.putPropertyVec2fValue(str2, new Vec2f(customPropertiesList2.getPropertyVec2f(str3, new Vec2f(f4, f4)).x, fStrToFloatSafe));
                    customizeVisDialog.onPropertyChanged();
                    action1CreatePropertyCommon.onInvoke(editTextArr2[0]);
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(null);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    private static void createPropertyViewText(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2) {
        View viewInflate;
        String propertyString = customPropertiesList.getPropertyString(str2, "");
        viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_txt : R.layout.dialog_customize_vis_element_txt, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txtValue);
        createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.27
            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                textView.setText(customPropertiesList.getPropertyString(str2, ""));
            }
        });
        if (z) {
            EditText editText = (EditText) viewInflate.findViewById(R.id.editTxt);
            editText.setText(propertyString);
            editText.addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.28
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    String string = editable.toString();
                    textView.setText(string);
                    customPropertiesList.putPropertyStringValue(str2, string);
                    customizeVisDialog.onPropertyChanged();
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(this);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    private static void createPropertyViewImage(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, final boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2, String[] strArr) {
        View viewInflate;
        int i;
        int entryCount = customizeVisDialog.customizationScene != null ? customizeVisDialog.customizationScene.getEntryCount() : 0;
        final String propertyString = customPropertiesList.getPropertyString(str2, "");
        viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_image : R.layout.dialog_customize_vis_element_image, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        View viewFindViewById = viewInflate.findViewById(R.id.elemValue);
        final ImageButton imageButton = (ImageButton) viewFindViewById.findViewById(R.id.btn0);
        final TextView textView = (TextView) viewFindViewById.findViewById(R.id.txt0);
        imageButton.setClickable(false);
        final EditText[] editTextArr = new EditText[1];
        if (z) {
            editTextArr[0] = (EditText) viewInflate.findViewById(R.id.editTxt);
        }
        final Action1<Object> action1CreatePropertyCommon = createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.29
            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                String propertyString2 = customPropertiesList.getPropertyString(str2, "");
                int resIdFromPath = InternalBitmapLoader.getResIdFromPath(propertyString2);
                if (resIdFromPath <= 0) {
                    int indexFromStringPath = Composition.getIndexFromStringPath(propertyString2);
                    if (indexFromStringPath >= 0) {
                        textView.setVisibility(0);
                        textView.setText(new StringBuilder().append(indexFromStringPath).toString());
                        imageButton.setImageResource(R.drawable.ic_visual1);
                    } else if (propertyString2 == null || propertyString2.length() <= 0) {
                        textView.setVisibility(8);
                        imageButton.setImageResource(0);
                    } else {
                        textView.setVisibility(8);
                        imageButton.setImageResource(R.drawable.ic_visual3);
                    }
                } else {
                    textView.setVisibility(8);
                    imageButton.setImageResource(resIdFromPath);
                }
                if (z && !editTextArr[0].equals(obj)) {
                    editTextArr[0].setText(propertyString2);
                }
            }
        });
        if (z) {
            final TextView textView2 = null;
            LinearLayout linearLayout = (LinearLayout) viewInflate.findViewById(R.id.layoutContent);
            Button button = (Button) viewInflate.findViewById(R.id.btnValue);
            final Button button2 = (Button) viewInflate.findViewById(R.id.btnResetValue);
            int i2 = R.id.btn0;
            boolean z3 = false;
            int i3 = 0;
            while (true) {
                int length = strArr.length;
                i = R.layout.dialog_customize_vis_element_asset_elem;
                if (i3 >= length) {
                    break;
                }
                final String str3 = strArr[i3];
                int resIdFromPath = InternalBitmapLoader.getResIdFromPath(str3);
                if (resIdFromPath > 0) {
                    View viewInflate2 = layoutInflater.inflate(R.layout.dialog_customize_vis_element_asset_elem, (ViewGroup) null);
                    final ImageButton imageButton2 = (ImageButton) viewInflate2.findViewById(i2);
                    imageButton2.setImageResource(resIdFromPath);
                    imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.30
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            customPropertiesList.putPropertyStringValue(str2, str3);
                            customizeVisDialog.onPropertyChanged();
                            action1CreatePropertyCommon.onInvoke(imageButton2);
                            Action1 action1 = (Action1) func.onInvoke();
                            if (action1 != null) {
                                action1.onInvoke(this);
                            }
                        }
                    });
                    linearLayout.addView(viewInflate2);
                } else if (Composition.getIndexFromStringPath(str3) >= 0) {
                    z3 = true;
                }
                i2 = R.id.btn0;
                i3++;
            }
            if (z3) {
                int i4 = 1;
                ViewGroup viewGroup2 = null;
                while (i4 < entryCount) {
                    final String strMakeIndexStringPath = Composition.makeIndexStringPath(i4);
                    View viewInflate3 = layoutInflater.inflate(i, viewGroup2);
                    TextView textView3 = (TextView) viewInflate3.findViewById(R.id.txt0);
                    textView3.setVisibility(0);
                    textView3.setText(new StringBuilder().append(i4).toString());
                    final ImageButton imageButton3 = (ImageButton) viewInflate3.findViewById(R.id.btn0);
                    imageButton3.setImageResource(R.drawable.ic_visual1);
                    imageButton3.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.31
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            customPropertiesList.putPropertyStringValue(str2, strMakeIndexStringPath);
                            customizeVisDialog.onPropertyChanged();
                            action1CreatePropertyCommon.onInvoke(imageButton3);
                            Action1 action1 = (Action1) func.onInvoke();
                            if (action1 != null) {
                                action1.onInvoke(this);
                            }
                        }
                    });
                    linearLayout.addView(viewInflate3);
                    viewGroup2 = null;
                    i4++;
                    i = R.layout.dialog_customize_vis_element_asset_elem;
                }
            }
            final TextView textView4 = null;
            button.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.32
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CustomizeVisDialog.onImagePropertyPicking.invoke(customizeVisDialog, textView4, customPropertiesList.getHierarchyPath(), str2, propertyString);
                }
            });
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.33
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    TextView textView5 = textView2;
                    if (textView5 != null) {
                        textView5.setText("");
                    }
                    customPropertiesList.putPropertyStringValue(str2, "");
                    customizeVisDialog.onPropertyChanged();
                    action1CreatePropertyCommon.onInvoke(button2);
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(this);
                    }
                }
            });
            editTextArr[0].addTextChangedListener(new TextWatcher() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.34
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    customPropertiesList.putPropertyStringValue(str2, editable.toString());
                    customizeVisDialog.onPropertyChanged();
                    action1CreatePropertyCommon.onInvoke(editTextArr[0]);
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(null);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    private static boolean isFontFile(File file) {
        String lowerCase = file.getName().toLowerCase();
        return lowerCase.endsWith(".ttf") || lowerCase.endsWith(".otf") || lowerCase.endsWith(".ttc") || lowerCase.endsWith(".otc");
    }

    private static void createPropertyViewAsset(final CustomizeVisDialog customizeVisDialog, LayoutInflater layoutInflater, final CustomPropertiesList customPropertiesList, boolean z, final Func<Action1<Object>> func, ViewGroup viewGroup, String str, final String str2, final String[] strArr) {
        View viewInflate;
        File[] fileArrListFiles;
        customPropertiesList.getPropertyString(str2, "");
        viewInflate = layoutInflater.inflate(z ? R.layout.dialog_customize_vis_elementdetail_image : R.layout.dialog_customize_vis_element_image, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.title)).setText(str);
        View viewFindViewById = viewInflate.findViewById(R.id.elemValue);
        final ImageButton imageButton = (ImageButton) viewFindViewById.findViewById(R.id.btn0);
        final TextView textView = (TextView) viewFindViewById.findViewById(R.id.txt0);
        imageButton.setClickable(false);
        createPropertyCommon(customizeVisDialog, customPropertiesList, z, viewInflate, str2, new Action1<Object>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.35
            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Object obj) {
                int iStrToIntSafe;
                String propertyString = customPropertiesList.getPropertyString(str2, "");
                int i = 0;
                while (true) {
                    String[] strArr2 = strArr;
                    if (i >= strArr2.length) {
                        iStrToIntSafe = -1;
                        break;
                    }
                    String[] strArr3 = new String[2];
                    Utils.stringSplitInTwo(strArr2[i], 58, strArr3);
                    if (propertyString.equals(strArr3[0])) {
                        iStrToIntSafe = Utils.strToIntSafe(strArr3[1], 0);
                        break;
                    }
                    i++;
                }
                if (iStrToIntSafe <= 0) {
                    if (propertyString != null && propertyString.startsWith(LocalFontLoader.Prefix)) {
                        File file = new File(new File(customizeVisDialog.getActivity().getFilesDir(), "custom_fonts"), propertyString.substring(5));
                        if (file.exists()) {
                            textView.setVisibility(8);
                            Bitmap bitmapGenerateFontPreviewBitmap = CustomizeVisView1.generateFontPreviewBitmap(file, (int) (customizeVisDialog.getResources().getDisplayMetrics().density * 48.0f));
                            if (bitmapGenerateFontPreviewBitmap != null) {
                                imageButton.setImageBitmap(bitmapGenerateFontPreviewBitmap);
                                return;
                            } else {
                                imageButton.setImageResource(R.drawable.ic_visual3);
                                return;
                            }
                        }
                    }
                    if (propertyString != null && propertyString.startsWith("/")) {
                        File file2 = new File(propertyString);
                        if (file2.exists()) {
                            textView.setVisibility(8);
                            Bitmap bitmapGenerateFontPreviewBitmap2 = CustomizeVisView1.generateFontPreviewBitmap(file2, (int) (customizeVisDialog.getResources().getDisplayMetrics().density * 48.0f));
                            if (bitmapGenerateFontPreviewBitmap2 != null) {
                                imageButton.setImageBitmap(bitmapGenerateFontPreviewBitmap2);
                                return;
                            } else {
                                imageButton.setImageResource(R.drawable.ic_visual3);
                                return;
                            }
                        }
                    }
                    textView.setVisibility(8);
                    imageButton.setImageResource(0);
                    return;
                }
                textView.setVisibility(8);
                imageButton.setImageResource(iStrToIntSafe);
            }
        });
        if (z) {
            LinearLayout linearLayout = (LinearLayout) viewInflate.findViewById(R.id.layoutContent);
            Button button = (Button) viewInflate.findViewById(R.id.btnValue);
            Button button2 = (Button) viewInflate.findViewById(R.id.btnResetValue);
            button.setVisibility(8);
            linearLayout.removeAllViews();
            int i = 0;
            int i2 = R.id.btn0;
            for (String str3 : strArr) {
                final String[] strArr2 = new String[2];
                Utils.stringSplitInTwo(str3, 58, strArr2);
                final int iStrToIntSafe = Utils.strToIntSafe(strArr2[1], i);
                View viewInflate2 = layoutInflater.inflate(R.layout.dialog_customize_vis_element_asset_elem, (ViewGroup) null);
                ImageButton imageButton2 = (ImageButton) viewInflate2.findViewById(i2);
                imageButton2.setImageResource(iStrToIntSafe);
                imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.36
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        customPropertiesList.putPropertyStringValue(str2, strArr2[0]);
                        customizeVisDialog.onPropertyChanged();
                        imageButton.setImageResource(iStrToIntSafe);
                        textView.setVisibility(8);
                        customizeVisDialog.refreshPropertyView();
                        Action1 action1 = (Action1) func.onInvoke();
                        if (action1 != null) {
                            action1.onInvoke(this);
                        }
                    }
                });
                linearLayout.addView(viewInflate2);
                i = 0;
                i2 = R.id.btn0;
            }
            if (str2.equals("typeFace")) {
                File file = new File(customizeVisDialog.getActivity().getFilesDir(), "custom_fonts");
                if (file.exists() && file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
                    Arrays.sort(fileArrListFiles, new Comparator<File>() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.37
                        @Override // java.util.Comparator
                        public int compare(File file2, File file3) {
                            return file2.getName().compareToIgnoreCase(file3.getName());
                        }
                    });
                    for (File file2 : fileArrListFiles) {
                        if (file2.isFile() && isFontFile(file2)) {
                            addCustomFontItem(linearLayout, layoutInflater, customizeVisDialog, customPropertiesList, str2, func, file2);
                        }
                    }
                }
                ImageButton imageButton3 = new ImageButton(customizeVisDialog.getActivity());
                imageButton3.setImageResource(R.drawable.add_font);
                imageButton3.setBackgroundResource(R.drawable.border_item_background_light);
                imageButton3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                int i3 = (int) (48.0f * customizeVisDialog.getResources().getDisplayMetrics().density);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i3, i3);
                layoutParams.setMargins(4, 4, 4, 4);
                imageButton3.setLayoutParams(layoutParams);
                imageButton3.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.38
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
                        intent.addCategory("android.intent.category.OPENABLE");
                        intent.setType("*/*");
                        intent.addFlags(1);
                        CustomizeVisDialog.pendingFontCustomData = customPropertiesList;
                        CustomizeVisDialog.pendingFontPropertyName = str2;
                        customizeVisDialog.startActivityForResult(intent, 1001);
                    }
                });
                linearLayout.addView(imageButton3);
            }
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.39
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    customPropertiesList.putPropertyStringValue(str2, "");
                    customizeVisDialog.onPropertyChanged();
                    imageButton.setImageResource(0);
                    textView.setVisibility(8);
                    customizeVisDialog.refreshPropertyView();
                    Action1 action1 = (Action1) func.onInvoke();
                    if (action1 != null) {
                        action1.onInvoke(this);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
    }

    private static void addCustomFontItem(final LinearLayout linearLayout, LayoutInflater layoutInflater, final CustomizeVisDialog customizeVisDialog, final CustomPropertiesList customPropertiesList, final String str, final Func<Action1<Object>> func, final File file) {
        final View viewInflate = layoutInflater.inflate(R.layout.dialog_customize_vis_element_asset_elem, (ViewGroup) null);
        final ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.btn0);
        final TextView textView = (TextView) viewInflate.findViewById(R.id.txt0);
        Bitmap bitmapGenerateFontPreviewBitmap = generateFontPreviewBitmap(file, (int) (48.0f * customizeVisDialog.getResources().getDisplayMetrics().density));
        if (bitmapGenerateFontPreviewBitmap != null) {
            imageButton.setImageBitmap(bitmapGenerateFontPreviewBitmap);
        } else {
            imageButton.setImageResource(R.drawable.ic_visual3);
        }
        textView.setVisibility(8);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.40
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                File fileGetVisualizerFolderFromIdentifier;
                String name = file.getName();
                String str2 = LocalFontLoader.Prefix + name;
                int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId);
                if (i >= 0 && (fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(i)) != null) {
                    if (!fileGetVisualizerFolderFromIdentifier.exists()) {
                        fileGetVisualizerFolderFromIdentifier.mkdirs();
                    }
                    File file2 = new File(fileGetVisualizerFolderFromIdentifier, name);
                    if (!file2.exists()) {
                        try {
                            UtilsFileSys.CopyFile(file, file2);
                        } catch (Exception e) {
                        }
                    }
                }
                customPropertiesList.putPropertyStringValue(str, str2);
                Bitmap bitmapGenerateFontPreviewBitmap2 = CustomizeVisView1.generateFontPreviewBitmap(file, (int) (48.0f * customizeVisDialog.getResources().getDisplayMetrics().density));
                if (bitmapGenerateFontPreviewBitmap2 != null) {
                    imageButton.setImageBitmap(bitmapGenerateFontPreviewBitmap2);
                } else {
                    imageButton.setImageResource(R.drawable.ic_visual3);
                }
                textView.setVisibility(8);
                customizeVisDialog.onPropertyChanged();
                customizeVisDialog.forceFinishCustomizing();
                Action1 action1 = (Action1) func.onInvoke();
                if (action1 != null) {
                    action1.onInvoke(null);
                }
            }
        });
        imageButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.41
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                MainActivity mainActivity = MainActivity.getInstance();
                String name = file.getName();
                int iLastIndexOf = name.lastIndexOf(46);
                if (iLastIndexOf > 0) {
                    name = name.substring(0, iLastIndexOf);
                }
                String string = mainActivity.getString(R.string.delete_font_title);
                String str2 = String.format(mainActivity.getString(R.string.delete_font_message), name);
                String string2 = mainActivity.getString(R.string.delete_font_confirm);
                String string3 = mainActivity.getString(R.string.delete_font_cancel);
                final String string4 = mainActivity.getString(R.string.delete_font_success);
                AlertDialog.Builder message = new AlertDialog.Builder(customizeVisDialog.getActivity()).setTitle(string).setMessage(str2);
                final File file2 = file;
                final CustomPropertiesList customPropertiesList2 = customPropertiesList;
                final String str3 = str;
                final CustomizeVisDialog customizeVisDialog2 = customizeVisDialog;
                final LinearLayout linearLayout2 = linearLayout;
                final View view2 = viewInflate;
                final Func func2 = func;
                message.setPositiveButton(string2, new DialogInterface.OnClickListener() { // from class: com.daaw.avee.comp.VisualizerCutomization.CustomizeVisView1.41.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File fileGetVisualizerFolderFromIdentifier;
                        if (file2.exists()) {
                            file2.delete();
                        }
                        int i2 = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId);
                        if (i2 >= 0 && (fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(i2)) != null && fileGetVisualizerFolderFromIdentifier.exists()) {
                            File file3 = new File(fileGetVisualizerFolderFromIdentifier, file2.getName());
                            if (file3.exists()) {
                                file3.delete();
                            }
                        }
                        String propertyString = customPropertiesList2.getPropertyString(str3, "");
                        if (propertyString.equals(LocalFontLoader.Prefix + file2.getName()) || propertyString.equals(file2.getAbsolutePath())) {
                            customPropertiesList2.putPropertyStringValue(str3, "");
                            customizeVisDialog2.onPropertyChanged();
                            customizeVisDialog2.refreshPropertyView();
                        }
                        linearLayout2.removeView(view2);
                        Action1 action1 = (Action1) func2.onInvoke();
                        if (action1 != null) {
                            action1.onInvoke(null);
                        }
                        Toast.makeText(customizeVisDialog2.getActivity(), string4, 0).show();
                    }
                }).setNegativeButton(string3, (DialogInterface.OnClickListener) null).show();
                return true;
            }
        });
        linearLayout.addView(viewInflate);
    }

    public static Bitmap generateFontPreviewBitmap(File file, int i) {
        Typeface typefaceCreateFromFile;
        String name = file.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        if (iLastIndexOf > 0) {
            name = name.substring(0, iLastIndexOf);
        }
        String str = name.length() > 8 ? String.valueOf(String.valueOf(String.valueOf(name.substring(0, 7)))) + "…" : name;
        try {
            String lowerCase = file.getName().toLowerCase();
            if (lowerCase.endsWith(".ttf") || lowerCase.endsWith(".otf")) {
                try {
                    typefaceCreateFromFile = Typeface.createFromFile(file);
                } catch (Exception e) {
                    typefaceCreateFromFile = null;
                }
            } else {
                typefaceCreateFromFile = null;
            }
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(i * 0.5f);
            paint.setColor(-1);
            paint.setTextAlign(Paint.Align.CENTER);
            if (typefaceCreateFromFile != null) {
                paint.setTypeface(typefaceCreateFromFile);
            }
            Rect rect = new Rect();
            paint.getTextBounds(str, 0, str.length(), rect);
            int i2 = (int) (i * 0.1f);
            int iMax = Math.max(rect.width() + (i2 * 2), i);
            int iMax2 = Math.max((i2 * 2) + rect.height(), i);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMax, iMax2, Bitmap.Config.ARGB_8888);
            new Canvas(bitmapCreateBitmap).drawText(str, iMax / 2.0f, (iMax2 / 2.0f) - ((rect.bottom + rect.top) / 2.0f), paint);
            return bitmapCreateBitmap;
        } catch (Exception e2) {
            return null;
        }
    }

    public void onImagePropertyValuePicked(TextView textView, List<String> list, String str, String str2) {
        tlog.d("hierarchyPath" + list + " propertyName: " + str + " value: " + str2);
        if (this.linearLayoutRootContent != null && textView != null) {
            textView.setText(str2 != null ? str2 : "");
        }
        if (list != null && str != null) {
            CustomPropertiesList data = this.customizeVisDialog.customizationScene.getData(list);
            if (data != null) {
                if (str2 == null) {
                    str2 = "";
                }
                data.putPropertyStringValue(str, str2);
                this.customizeVisDialog.onFinishedChangingProperties();
                return;
            }
            Iterator<String> it = list.iterator();
            String str3 = "";
            while (it.hasNext()) {
                str3 = String.valueOf(String.valueOf(String.valueOf(str3))) + it.next();
            }
            tlog.w("invalid hierarchyPath: [" + str3 + "]");
        }
    }

    public void AddPropertyUpdateValueCb(CustomPropertiesList customPropertiesList, String str, Action1<Object> action1) {
        this.propertiesUpdateValueCb.put(CustomPropertiesList.MakeHierarchyPathKeyString(customPropertiesList.getHierarchyPath(), str), action1);
    }

    public Action1<Object> GetPropertyUpdateValueCb(List<String> list, String str) {
        if (list == null || str == null) {
            return null;
        }
        return this.propertiesUpdateValueCb.get(CustomPropertiesList.MakeHierarchyPathKeyString(list, str));
    }
}
