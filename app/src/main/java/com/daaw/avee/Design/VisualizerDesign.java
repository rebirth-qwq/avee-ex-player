package com.daaw.avee.Design;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.widget.TextView;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.Events.WeakDelegateR2;
import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEvent5;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Events.WeakEventR2;
import com.daaw.avee.Common.Events.WeakEventR5;
import com.daaw.avee.Common.Interpolate;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.VAsyncTask;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.ContextData;
import com.daaw.avee.EventsGlobal.EventsGlobalTextNotifier;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.SettingsActivity;
import com.daaw.avee.comp.AlbumArt.AlbumArtFactory;
import com.daaw.avee.comp.AlbumArt.AlbumArtRequest;
import com.daaw.avee.comp.AlbumArt.AlbumArtRequest2;
import com.daaw.avee.comp.AlbumArt.IAlbumArtCore;
import com.daaw.avee.comp.AlbumArt.ImageLoadedListener;
import com.daaw.avee.comp.AlbumArt.ImageResult;
import com.daaw.avee.comp.AlbumArt.LocalBitmapLoader;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.IItemIdentifier;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Common.MeterOutputData;
import com.daaw.avee.comp.Common.TrackDurationInfo;
import com.daaw.avee.comp.Common.VisualizerThemeInfo;
import com.daaw.avee.comp.MediaControlsUI.MediaControlsUI;
import com.daaw.avee.comp.VisualUI.Fragment2;
import com.daaw.avee.comp.VisualUI.VisualizerChooseDialog;
import com.daaw.avee.comp.Visualizer.CustomScene;
import com.daaw.avee.comp.Visualizer.DependencyResourceCounter;
import com.daaw.avee.comp.Visualizer.DependencyResourceCounterDuymmy;
import com.daaw.avee.comp.Visualizer.Elements.Base.LocalFontLoader;
import com.daaw.avee.comp.Visualizer.Elements.ElementsFactory;
import com.daaw.avee.comp.Visualizer.Elements.RootElement;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.GraphicPreview.RenderStatePreview;
import com.daaw.avee.comp.Visualizer.VisualizerViewCore;
import com.daaw.avee.comp.VisualizerCutomization.CustomizeVisDialog;
import com.daaw.avee.comp.VisualizerExporter.EncodingUtils;
import com.daaw.avee.comp.playback.AudioFrameData;
import com.daaw.avee.comp.playback.EventsPlaybackService;
import com.daaw.avee.comp.playback.MediaPlaybackService;
import com.daaw.avee.comp.playback.PlayingMediaInfo;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class VisualizerDesign {
    private static final float BEAT_TRIGGER_RANDOM_HOLD_DURATION = 0.1f;
    public static WeakDelegateR2<AudioFrameData, AudioFrameData.Request2, AudioFrameData> getVisualizationData = new WeakDelegateR2<>();
    private boolean useHighQualityBlur;
    private volatile WeakReference<SurfaceHolder> surfaceHolder = new WeakReference<>(null);
    private WeakReference<VisualizerViewCore> visualizerSurfaceView = new WeakReference<>(null);
    private volatile float videoWidthHeightRatio = 1.0f;
    private boolean uiNeedShowVisual = true;
    private List<Object> listenerRefHolder = new LinkedList();
    private Handler threadHandler = new Handler();
    private WeakReference<TextView> currentPickingTxtValue = new WeakReference<>(null);
    private List<String> currentPickingCustomizationData = null;
    private String currentPickingPropertyName = null;
    final Random randomGenerator = new Random();
    private Handler mainThreadHandler = new Handler();
    private Vec2f shakePoint = new Vec2f(0.0f, 0.0f);
    private Vec2f shakePointSmooth = new Vec2f(0.0f, 0.0f);
    private float totalFrameTimeWhenPlaying = 0.0f;
    private int selectedSceneElementId = -1;
    private final HandheldMotion handheldMotionSmooth = new HandheldMotion(HandheldMotion.Jarles_Presets_MoreMovement_Smoothest);
    private final HandheldMotion handheldMotionLotOfShake = new HandheldMotion(HandheldMotion.Jarles_Presets_LotsOshake);
    HashMap<Integer, Bitmap> cachedPreviewThumbs = new HashMap<>();
    RenderStatePreview renderStatePreview = new RenderStatePreview();
    private float beatTriggerRandomValue = 0.0f;
    private boolean beatTriggerRandomTriggered = false;
    private float beatTriggerRandomHoldTime = 0.0f;
    private boolean beatTriggerAnimActive = false;
    private HashMap<String, RandomCharCache> randomCharCache = new HashMap<>();

    public VisualizerDesign() {
        this.useHighQualityBlur = false;
        Context appContext = PlayerCore.s().getAppContext();
        this.useHighQualityBlur = appContext != null && AppPreferences.createOrGetInstance().preferencesGetBoolSafe(appContext, "pref_highQualityBlur", false);
        SettingsActivity.onPrefHighQualityBlurChanged.subscribeWeak(new WeakEvent1.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.1
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(Boolean bool) {
                VisualizerDesign.this.useHighQualityBlur = bool.booleanValue();
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onDisplayMetaDataStateChanged.subscribeWeak(new WeakEvent4.Handler<PlaylistSong, IItemIdentifier, PlaylistSong.Data, PlayingMediaInfo>() { // from class: com.daaw.avee.Design.VisualizerDesign.2
            @Override // com.daaw.avee.Common.Events.WeakEvent4.Handler
            public void invoke(PlaylistSong playlistSong, IItemIdentifier iItemIdentifier, PlaylistSong.Data data, PlayingMediaInfo playingMediaInfo) {
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onRequestVideoScalingMode.subscribeWeak(new WeakEventR.Handler<Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Integer invoke() {
                return Integer.valueOf(VisualizerDesign.this.getPlayerbackVideoScalingMode());
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onRequestVideoSurfaceHolder.subscribeWeak(new WeakEventR.Handler<SurfaceHolder>() { // from class: com.daaw.avee.Design.VisualizerDesign.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public SurfaceHolder invoke() {
                return (SurfaceHolder) VisualizerDesign.this.surfaceHolder.get();
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onVideoSizeChanged.subscribeWeak(new WeakEvent3.Handler<Integer, Integer, Float>() { // from class: com.daaw.avee.Design.VisualizerDesign.5
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Integer num, Integer num2, Float f) {
                VisualizerDesign.this.videoWidthHeightRatio = f.floatValue();
                VisualizerDesign.this.threadHandler.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Fragment2 fragment2Instance = MainActivity.getFragment2Instance();
                        if (fragment2Instance != null) {
                            fragment2Instance.setVideoSizeTh(VisualizerDesign.this.getSurfaceVideoSize(VisualizerDesign.this.videoWidthHeightRatio));
                        }
                    }
                });
            }
        }, this.listenerRefHolder);
        Fragment2.onSurfaceCreated.subscribeWeak(new WeakEvent1.Handler<VisualizerViewCore>() { // from class: com.daaw.avee.Design.VisualizerDesign.6
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(VisualizerViewCore visualizerViewCore) {
                VisualizerDesign.this.visualizerSurfaceView = new WeakReference(visualizerViewCore);
            }
        }, this.listenerRefHolder);
        AppDesign.onRequestsSoundVisualizationDataRealTime.subscribeWeak(new WeakEventR2.Handler<AudioFrameData, Integer, AudioFrameData>() { // from class: com.daaw.avee.Design.VisualizerDesign.7
            @Override // com.daaw.avee.Common.Events.WeakEventR2.Handler
            public AudioFrameData invoke(AudioFrameData audioFrameData, Integer num) {
                return VisualizerDesign.this.getSoundVisualizationData(num.intValue(), audioFrameData);
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onFrameStart.subscribeWeak(new WeakEvent2.Handler<Float, Float>() { // from class: com.daaw.avee.Design.VisualizerDesign.8
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(Float f, Float f2) {
                if (VisualizerDesign.this.isPlaying()) {
                    VisualizerDesign.this.totalFrameTimeWhenPlaying += f.floatValue();
                }
                if (VisualizerDesign.this.shakePointSmooth.distance(VisualizerDesign.this.shakePoint) < 0.1f) {
                    float fNextFloat = VisualizerDesign.this.randomGenerator.nextFloat() * 2.0f * 3.1415927f;
                    float fSqrt = (float) Math.sqrt(VisualizerDesign.this.randomGenerator.nextFloat());
                    VisualizerDesign.this.shakePoint.x = ((float) Math.cos(fNextFloat)) * fSqrt;
                    VisualizerDesign.this.shakePoint.y = ((float) Math.sin(fNextFloat)) * fSqrt;
                }
                Interpolate.Lerp(VisualizerDesign.this.shakePointSmooth, VisualizerDesign.this.shakePointSmooth, VisualizerDesign.this.shakePoint, 0.8999999761581421d);
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestMeasureText.subscribeWeak(new WeakEventR2.Handler<String, VisualizerViewCore, String>() { // from class: com.daaw.avee.Design.VisualizerDesign.9
            @Override // com.daaw.avee.Common.Events.WeakEventR2.Handler
            public String invoke(String str, VisualizerViewCore visualizerViewCore) {
                return str == null ? "" : VisualizerDesign.this.formatMeasureString(str, visualizerViewCore);
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestMeasureVec2f.subscribeWeak(new WeakEventR5.Handler<String, Vec2f, Vec2f, MeterOutputData, IRenderState, Vec2f>() { // from class: com.daaw.avee.Design.VisualizerDesign.10
            @Override // com.daaw.avee.Common.Events.WeakEventR5.Handler
            public Vec2f invoke(String str, Vec2f vec2f, Vec2f vec2f2, MeterOutputData meterOutputData, IRenderState iRenderState) {
                float frameTimeF = iRenderState.getFrameTimeF();
                float totalFrameTimeF = iRenderState.getTotalFrameTimeF();
                if (str == null) {
                    return new Vec2f(vec2f.x, vec2f.y);
                }
                if (str.length() < 1) {
                    return new Vec2f(vec2f.x, vec2f.y);
                }
                if (vec2f2 == null) {
                    vec2f2 = new Vec2f(0.0f, 0.0f);
                }
                if (MeasureDefs.Nothing.equals(str)) {
                    return new Vec2f(0.0f, 0.0f);
                }
                if (MeasureDefs.Constant.equals(str)) {
                    return new Vec2f(vec2f.x, vec2f.y);
                }
                if (MeasureDefs.isPlaying.equals(str)) {
                    return VisualizerDesign.this.isPlaying() ? new Vec2f(1.0f, 1.0f) : new Vec2f(0.0f, 0.0f);
                }
                if (str.equals(MeasureDefs.beat)) {
                    return new Vec2f(meterOutputData.frameDataRmsValue * vec2f.x, meterOutputData.frameDataRmsValue * vec2f.y);
                }
                if (str.equals(MeasureDefs.beatCamShakeMore)) {
                    float fMax = Math.max(Math.min(VisualizerDesign.this.isPlaying() ? meterOutputData.frameDataRmsValue * 2.5f : 0.0f, 1.0f), 0.0f);
                    float f = fMax * fMax;
                    Vec2f pos = VisualizerDesign.this.handheldMotionLotOfShake.getPos(((fMax * (totalFrameTimeF * 0.5f)) * vec2f.y) % 1.0f);
                    pos.x = (pos.x - 0.5f) * vec2f.x * f * 0.06f;
                    pos.y = f * (pos.y - 0.5f) * vec2f.x * 0.06f;
                    Interpolate.Lerp(pos, vec2f2, pos, 0.800000011920929d);
                    return pos;
                }
                if (str.equals(MeasureDefs.beatCamShakeLess)) {
                    float fMax2 = Math.max(Math.min(VisualizerDesign.this.isPlaying() ? meterOutputData.frameDataRmsValue * 2.5f : 0.0f, 1.0f), 0.0f);
                    float f2 = fMax2 * fMax2;
                    Vec2f pos2 = VisualizerDesign.this.handheldMotionSmooth.getPos(((fMax2 * (totalFrameTimeF * 0.5f)) * vec2f.y) % 1.0f);
                    pos2.x = (pos2.x - 0.5f) * vec2f.x * f2 * 0.06f;
                    pos2.y = f2 * (pos2.y - 0.5f) * vec2f.x * 0.06f;
                    Interpolate.Lerp(pos2, vec2f2, pos2, 0.800000011920929d);
                    return pos2;
                }
                if (str.equals(MeasureDefs.totalTime)) {
                    return new Vec2f((vec2f.x * totalFrameTimeF) % 1.0f, (vec2f.y * totalFrameTimeF) % 1.0f);
                }
                if (str.equals(MeasureDefs.TotalTimeAndBack)) {
                    return new Vec2f(vec2f.x * ((float) Math.sin(totalFrameTimeF)), vec2f.y * ((float) Math.cos(totalFrameTimeF)));
                }
                if (str.equals(MeasureDefs.totalTimeBackward)) {
                    return new Vec2f(1.0f - ((vec2f.x * totalFrameTimeF) % 1.0f), 1.0f - ((vec2f.y * totalFrameTimeF) % 1.0f));
                }
                if (str.equals(MeasureDefs.totalTimeWhenPlaying)) {
                    return new Vec2f((VisualizerDesign.this.totalFrameTimeWhenPlaying * vec2f.x) % 1.0f, (VisualizerDesign.this.totalFrameTimeWhenPlaying * vec2f.y) % 1.0f);
                }
                if (str.equals(MeasureDefs.totalTimeAndBeat)) {
                    float f3 = vec2f2.x;
                    if (VisualizerDesign.this.isPlaying()) {
                        f3 = f3 + (vec2f.x * frameTimeF * 1.0f) + (meterOutputData.frameDataRmsValue * vec2f.y * 0.1f);
                    }
                    return new Vec2f(f3, f3);
                }
                if (str.equals(MeasureDefs.infiniteMotion)) {
                    return new Vec2f(vec2f.x * totalFrameTimeF, vec2f.y * totalFrameTimeF);
                }
                if (str.equals(MeasureDefs.infiniteMotionBackward)) {
                    return new Vec2f(1.0f - (vec2f.x * totalFrameTimeF), 1.0f - (vec2f.y * totalFrameTimeF));
                }
                if (str.equals(MeasureDefs.infiniteMotionWhenPlaying)) {
                    return new Vec2f(VisualizerDesign.this.totalFrameTimeWhenPlaying * vec2f.x, VisualizerDesign.this.totalFrameTimeWhenPlaying * vec2f.y);
                }
                if (str.equals(MeasureDefs.constantCamShakeMore)) {
                    Vec2f pos3 = VisualizerDesign.this.handheldMotionLotOfShake.getPos(((totalFrameTimeF * 0.5f) * vec2f.y) % 1.0f);
                    pos3.x = (pos3.x - 0.5f) * vec2f.x * 0.06f;
                    pos3.y = (pos3.y - 0.5f) * vec2f.x * 0.06f;
                    Interpolate.Lerp(pos3, vec2f2, pos3, 0.10000000149011612d);
                    return pos3;
                }
                if (str.equals(MeasureDefs.constantCamShakeLess)) {
                    Vec2f pos4 = VisualizerDesign.this.handheldMotionSmooth.getPos(((totalFrameTimeF * 0.5f) * vec2f.y) % 1.0f);
                    pos4.x = (pos4.x - 0.5f) * vec2f.x * 0.06f;
                    pos4.y = (pos4.y - 0.5f) * vec2f.x * 0.06f;
                    Interpolate.Lerp(pos4, vec2f2, pos4, 0.10000000149011612d);
                    return pos4;
                }
                if (str.equals(MeasureDefs.beatRandomShake)) {
                    float fMax3 = Math.max(Math.min(VisualizerDesign.this.isPlaying() ? meterOutputData.frameDataRmsValue * 2.5f : 0.0f, 1.0f), 0.0f);
                    float f4 = fMax3 * fMax3 * fMax3;
                    return new Vec2f(VisualizerDesign.this.shakePointSmooth.x * f4 * vec2f.x * 0.025f, f4 * VisualizerDesign.this.shakePointSmooth.y * vec2f.x * 0.025f);
                }
                if (str.equals(MeasureDefs.trackPosition)) {
                    long trackPositionForRendering = AppDesign.getTrackPositionForRendering();
                    long trackDurationForRendering = AppDesign.getTrackDurationForRendering();
                    float fMax4 = trackDurationForRendering > 0 ? Math.max(Math.min(((float) trackPositionForRendering) / ((float) trackDurationForRendering), 1.0f), 0.0f) : 0.0f;
                    return new Vec2f(vec2f.x * fMax4, fMax4 * vec2f.y);
                }
                if (str.equals(MeasureDefs.constantCamShakeRotMore)) {
                    double rot = VisualizerDesign.this.handheldMotionLotOfShake.getRot(((totalFrameTimeF * 0.5f) * vec2f.y) % 1.0f);
                    Double.isNaN(rot);
                    float fLerp = Interpolate.Lerp(vec2f2.x, ((float) (rot / 6.283185307179586d)) * vec2f.x * 0.5f, 0.10000000149011612d);
                    return new Vec2f(fLerp, fLerp);
                }
                if (str.equals(MeasureDefs.constantCamShakeRotLess)) {
                    double rot2 = VisualizerDesign.this.handheldMotionSmooth.getRot(((totalFrameTimeF * 0.5f) * vec2f.y) % 1.0f);
                    Double.isNaN(rot2);
                    float fLerp2 = Interpolate.Lerp(vec2f2.x, ((float) (rot2 / 6.283185307179586d)) * vec2f.x * 0.5f, 0.10000000149011612d);
                    return new Vec2f(fLerp2, fLerp2);
                }
                if (str.equals(MeasureDefs.beatCamShakeRotMore)) {
                    float fMax5 = Math.max(Math.min(VisualizerDesign.this.isPlaying() ? meterOutputData.frameDataRmsValue * 2.5f : 0.0f, 1.0f), 0.0f);
                    double rot3 = VisualizerDesign.this.handheldMotionLotOfShake.getRot(((totalFrameTimeF * 0.5f) * vec2f.y) % 1.0f);
                    Double.isNaN(rot3);
                    float fLerp3 = Interpolate.Lerp(vec2f2.x, fMax5 * ((float) (rot3 / 6.283185307179586d)) * fMax5 * vec2f.x * 0.5f, 0.800000011920929d);
                    return new Vec2f(fLerp3, fLerp3);
                }
                if (str.equals(MeasureDefs.beatCamShakeRotLess)) {
                    float fMax6 = Math.max(Math.min(VisualizerDesign.this.isPlaying() ? meterOutputData.frameDataRmsValue * 2.5f : 0.0f, 1.0f), 0.0f);
                    double rot4 = VisualizerDesign.this.handheldMotionSmooth.getRot(((totalFrameTimeF * 0.5f) * vec2f.y) % 1.0f);
                    Double.isNaN(rot4);
                    float fLerp4 = Interpolate.Lerp(vec2f2.x, fMax6 * ((float) (rot4 / 6.283185307179586d)) * fMax6 * vec2f.x * 0.5f, 0.800000011920929d);
                    return new Vec2f(fLerp4, fLerp4);
                }
                if (str.equals(MeasureDefs.peakBarIndex)) {
                    float fLerp5 = Interpolate.Lerp(meterOutputData.peakBarIndex, vec2f2.x, vec2f.y);
                    return new Vec2f(vec2f.x * fLerp5, fLerp5 * vec2f.x);
                }
                if (str.equals(MeasureDefs.beatTriggerRandom)) {
                    return VisualizerDesign.this.processBeatTriggerRandom(vec2f, vec2f2, meterOutputData, frameTimeF);
                }
                if (str.equals(MeasureDefs.beatTriggerAnim)) {
                    return VisualizerDesign.this.processBeatTriggerAnim(vec2f, vec2f2, meterOutputData, frameTimeF);
                }
                if (str.equals(MeasureDefs.beatPeak)) {
                    return VisualizerDesign.this.processBeatPeak(vec2f, meterOutputData);
                }
                if (str.equals(MeasureDefs.trackSetPosition)) {
                    vec2f2 = VisualizerDesign.this.processTrackSetPosition(vec2f, vec2f2);
                }
                return vec2f2;
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestsAlbumArtPath.subscribeWeak(new WeakEventR.Handler<AlbumArtRequest>() { // from class: com.daaw.avee.Design.VisualizerDesign.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public AlbumArtRequest invoke() {
                PlaylistSong.Data data = PlaybackControlsDesign.fieldsongData;
                if (data == PlaylistSong.emptyData) {
                    return null;
                }
                return new AlbumArtRequest(data.dataSource, data.getAlbumArtPath0Str(), data.getAlbumArtPath1Str(), data.getAlbumArtGenerateStr());
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestAlbumArtPathAndBitmap.subscribeWeak(new WeakEvent2.Handler<ImageLoadedListener, AlbumArtRequest2>() { // from class: com.daaw.avee.Design.VisualizerDesign.12
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(final ImageLoadedListener imageLoadedListener, AlbumArtRequest2 albumArtRequest2) {
                ImageLoadedListener imageLoadedListener2 = new ImageLoadedListener() { // from class: com.daaw.avee.Design.VisualizerDesign.12.1
                    Object object1;

                    @Override // com.daaw.avee.comp.AlbumArt.ImageLoadedListener
                    public void onBitmapLoaded(final ImageResult imageResult, final String str, final String str2, final String str3) {
                        VisualizerViewCore visualizerViewCore = (VisualizerViewCore) VisualizerDesign.this.visualizerSurfaceView.get();
                        if (visualizerViewCore != null) {
                            final ImageLoadedListener imageLoadedListener3 = imageLoadedListener;
                            visualizerViewCore.queueEvent(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.12.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    imageLoadedListener3.onBitmapLoaded(imageResult, str, str2, str3);
                                }
                            });
                        }
                    }

                    @Override // com.daaw.avee.comp.AlbumArt.ImageLoadedListener
                    public void setUserObject1(Object obj) {
                        this.object1 = obj;
                    }
                };
                imageLoadedListener.setUserObject1(imageLoadedListener2);
                int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId);
                IAlbumArtCore albumArtFactory = AlbumArtFactory.getInstance();
                if (albumArtFactory != null) {
                    albumArtFactory.loadAlbumArtLarge(false, albumArtRequest2.videoThumbDataSource, albumArtRequest2.path0, albumArtRequest2.path1, albumArtRequest2.genStr, albumArtRequest2.fitCenter, imageLoadedListener2, albumArtRequest2.targetBoundsWidth, albumArtRequest2.targetBoundsHeight, albumArtRequest2.genBoundsWidth, albumArtRequest2.genBoundsHeight, albumArtRequest2.genHint, albumArtRequest2.genHint2, false, albumArtRequest2.imageColorKeyParams, Integer.valueOf(i));
                }
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestConvertDpToPx.subscribeWeak(new WeakEventR1.Handler<Integer, Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.13
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public Integer invoke(Integer num) {
                return Integer.valueOf((int) TypedValue.applyDimension(1, Integer.valueOf(num.intValue() / 2).intValue(), PlayerCore.s().getAppContext().getResources().getDisplayMetrics()));
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onRequestSkinThemePresetList.subscribeWeak(new WeakEventR1.Handler<List<VisualizerThemeInfo>, Tuple2<Integer, List<Integer>>>() { // from class: com.daaw.avee.Design.VisualizerDesign.14
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public Tuple2<Integer, List<Integer>> invoke(List<VisualizerThemeInfo> list) {
                List<Tuple2<VisualizerThemeInfo, VisualizerThemes.IVisualizerFactory2>> themesList = VisualizerThemes.s().getThemesList();
                ArrayList arrayList = new ArrayList(themesList.size());
                Iterator<Tuple2<VisualizerThemeInfo, VisualizerThemes.IVisualizerFactory2>> it = themesList.iterator();
                while (it.hasNext()) {
                    arrayList.add(Integer.valueOf(it.next().obj1.id));
                }
                List<Integer> prefThemeCustomizationIndexes = AppPreferences.createOrGetInstance().getPrefThemeCustomizationIndexes();
                ArrayList<Integer> arrayList2 = new ArrayList(themesList.size() + prefThemeCustomizationIndexes.size());
                Iterator<Tuple2<VisualizerThemeInfo, VisualizerThemes.IVisualizerFactory2>> it2 = themesList.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(Integer.valueOf(it2.next().obj1.id));
                }
                for (Integer num : prefThemeCustomizationIndexes) {
                    if (!arrayList.contains(num)) {
                        arrayList2.add(num);
                    }
                }
                for (Integer num2 : arrayList2) {
                    list.add(new VisualizerThemeInfo(num2.intValue(), VisualizerThemes.s().isTemplateIndex(num2.intValue())));
                }
                return new Tuple2<>(Integer.valueOf(AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId)), arrayList2);
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onRequestVisTemplatePreviewImage.subscribeWeak(new WeakEventR1.Handler<List<VisualizerThemeInfo>, VAsyncTask<List<Bitmap>>>() { // from class: com.daaw.avee.Design.VisualizerDesign.15
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public VAsyncTask<List<Bitmap>> invoke(final List<VisualizerThemeInfo> list) {
                return new VAsyncTask<>(new VAsyncTask.TaskListener<List<Bitmap>>() { // from class: com.daaw.avee.Design.VisualizerDesign.15.1
                    @Override // com.daaw.avee.Common.VAsyncTask.TaskListener
                    public List<Bitmap> onExecuteInBg(VAsyncTask<List<Bitmap>> vAsyncTask) {
                        ArrayList arrayList = new ArrayList(list.size());
                        int[] iArr = new int[1];
                        HashMap<Integer, Bitmap> map = new HashMap<>(list.size());
                        RootElement themeObjectForLoading2 = VisualizerThemes.s().getThemeObjectForLoading2(0, false, "");
                        for (int i = 0; i < list.size(); i++) {
                            int i2 = ((VisualizerThemeInfo) list.get(i)).id;
                            CustomScene prefThemeCustomizationDataOrTemplateData = VisualizerDesign.getPrefThemeCustomizationDataOrTemplateData(i2, iArr);
                            Bitmap bitmap = VisualizerDesign.this.cachedPreviewThumbs.get(Integer.valueOf(iArr[0]));
                            if (bitmap == null || iArr[0] == 0) {
                                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(PsExtractor.AUDIO_STREAM, PsExtractor.AUDIO_STREAM, Bitmap.Config.ARGB_8888);
                                VisualizerDesign.this.loadRootElementWithCustomSecene(null, themeObjectForLoading2, i2, true, prefThemeCustomizationDataOrTemplateData);
                                if (themeObjectForLoading2 != null) {
                                    VisualizerDesign.this.renderStatePreview.onFrameStart();
                                    VisualizerDesign.this.renderStatePreview.setDestBitmap(bitmapCreateBitmap);
                                    for (int i3 = 0; i3 < 1; i3++) {
                                        themeObjectForLoading2.onEarlyUpdate(VisualizerDesign.this.renderStatePreview, null, null);
                                    }
                                    themeObjectForLoading2.onRenderPreview(VisualizerDesign.this.renderStatePreview, null);
                                }
                                bitmap = bitmapCreateBitmap;
                            }
                            map.put(Integer.valueOf(iArr[0]), bitmap);
                            arrayList.add(bitmap);
                        }
                        VisualizerDesign.this.cachedPreviewThumbs = map;
                        return arrayList;
                    }
                });
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onSkinThemePresetSelected.subscribeWeak(new WeakEvent1.Handler<VisualizerThemeInfo>() { // from class: com.daaw.avee.Design.VisualizerDesign.16
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(VisualizerThemeInfo visualizerThemeInfo) {
                VisualizerDesign.selectThemePreset(visualizerThemeInfo.id);
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onRemoveVisTemplateAction.subscribeWeak(new WeakEvent1.Handler<VisualizerThemeInfo>() { // from class: com.daaw.avee.Design.VisualizerDesign.17
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(VisualizerThemeInfo visualizerThemeInfo) {
                if (visualizerThemeInfo != null) {
                    EventsGlobalTextNotifier.onTextMsg.invoke(PlayerCore.s().getAppContext().getResources().getString(R.string.hold_to_remove_vis));
                }
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onRemoveVisTemplateAction_Long.subscribeWeak(new WeakEvent1.Handler<VisualizerThemeInfo>() { // from class: com.daaw.avee.Design.VisualizerDesign.18
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(VisualizerThemeInfo visualizerThemeInfo) {
                if (visualizerThemeInfo != null) {
                    AppPreferences appPreferencesCreateOrGetInstance = AppPreferences.createOrGetInstance();
                    List<Integer> prefThemeCustomizationIndexes = appPreferencesCreateOrGetInstance.getPrefThemeCustomizationIndexes();
                    prefThemeCustomizationIndexes.remove(Integer.valueOf(visualizerThemeInfo.id));
                    appPreferencesCreateOrGetInstance.getInt(AppPreferences.PREF_Int_visualizerThemeId);
                    appPreferencesCreateOrGetInstance.setPrefThemeCustomizationIndexes(prefThemeCustomizationIndexes);
                }
            }
        }, this.listenerRefHolder);
        VisualizerChooseDialog.onDuplicateVisTemplateAction.subscribeWeak(new WeakEvent2.Handler<VisualizerThemeInfo, List<Integer>>() { // from class: com.daaw.avee.Design.VisualizerDesign.19
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(VisualizerThemeInfo visualizerThemeInfo, List<Integer> list) {
                if (visualizerThemeInfo != null) {
                    VisualizerDesign.addNewVisualizer(VisualizerDesign.getPrefThemeCustomizationDataOrTemplateData(visualizerThemeInfo.id, null), list, visualizerThemeInfo.id);
                }
            }
        }, this.listenerRefHolder);
        AppPreferences.onStringPreferenceChanged.subscribeWeak(new WeakEvent3.Handler<Integer, String, Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.20
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Integer num, String str, Boolean bool) {
                VisualizerViewCore visualizerViewCore;
                if (num.equals(Integer.valueOf(AppPreferences.PREF_String_themeCustomizationIndexes))) {
                    VisualizerChooseDialog visualizerChooseDialog = VisualizerChooseDialog.getInstance();
                    if (visualizerChooseDialog != null) {
                        visualizerChooseDialog.requestUpdateVisualizerTemplates();
                        return;
                    }
                    return;
                }
                if (num.intValue() >= AppPreferences.PREF_String_vThemeCustomization16start && num.intValue() <= AppPreferences.PREF_String_vThemeCustomization116end && AppPreferences.themeCustomizationIdentifierToPref(AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId)) == num.intValue() && (visualizerViewCore = (VisualizerViewCore) VisualizerDesign.this.visualizerSurfaceView.get()) != null) {
                    visualizerViewCore.requestRefreshThemeElementsUiTh();
                }
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestSelectedSkinThemePreset.subscribeWeak(new WeakEventR.Handler<CustomScene>() { // from class: com.daaw.avee.Design.VisualizerDesign.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public CustomScene invoke() {
                return VisualizerDesign.getPrefThemeCustomizationDataOrTemplateData(AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId), null);
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onThemeChanged.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.22
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                VisualizerViewCore visualizerViewCore = (VisualizerViewCore) VisualizerDesign.this.visualizerSurfaceView.get();
                if (visualizerViewCore != null) {
                    VisualizerDesign.updateChangesToVisDialogGlTh(visualizerViewCore, false, -1);
                }
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestFont.subscribeWeak(new WeakEventR2.Handler<String, Integer, Typeface>() { // from class: com.daaw.avee.Design.VisualizerDesign.23
            @Override // com.daaw.avee.Common.Events.WeakEventR2.Handler
            public Typeface invoke(String str, Integer num) {
                return VisualizerDesign.this.onRequestFontInternal(str, num.intValue());
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestTrackDurationInfo.subscribeWeak(new WeakEventR1.Handler<Context, TrackDurationInfo>() { // from class: com.daaw.avee.Design.VisualizerDesign.24
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public TrackDurationInfo invoke(Context context) {
                TrackDurationInfo trackDurationInfo = new TrackDurationInfo();
                trackDurationInfo.trackPositionMs = AppDesign.getTrackPositionForRendering();
                trackDurationInfo.durationMs = AppDesign.getTrackDurationForRendering();
                trackDurationInfo.progress = Math.min(trackDurationInfo.trackPositionMs / trackDurationInfo.durationMs, 1.0f);
                trackDurationInfo.progress = Math.max(trackDurationInfo.progress, 0.0f);
                return trackDurationInfo;
            }
        }, this.listenerRefHolder);
        AppDesign.onRequestRenderQualityEdgesValueRealTime.subscribeWeak(new WeakEventR1.Handler<Context, Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.25
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public Integer invoke(Context context) {
                return Integer.valueOf(AppPreferences.createOrGetInstance().preferencesGetBoolSafe(context, "pref_alwaysQualityEdges", false) ? 1 : 0);
            }
        }, this.listenerRefHolder);
        AppDesign.onRequestAudioProcessMultiThreadValueRealTime.subscribeWeak(new WeakEventR1.Handler<Context, Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.26
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public Integer invoke(Context context) {
                return Integer.valueOf(AppPreferences.createOrGetInstance().preferencesGetBoolSafe(context, "pref_enableNewestFeatures", true) ? 1 : 0);
            }
        }, this.listenerRefHolder);
        Fragment2.onRequestShowVideoContentState.subscribeWeak(new WeakEventR.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Boolean invoke() {
                return Boolean.valueOf(AppPreferences.createOrGetInstance().getBool(AppPreferences.PREF_Bool_visualPreferShowVideoContent));
            }
        }, this.listenerRefHolder);
        MediaControlsUI.onToggleVideoScalingMode.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.28
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                String string;
                int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_videoScalingMode);
                int i2 = 3;
                if (i == 1) {
                    i2 = 2;
                } else if (i != 2) {
                    i2 = 1;
                }
                AppPreferences.createOrGetInstance().setInt(AppPreferences.PREF_Int_videoScalingMode, i2);
                Context appContext2 = PlayerCore.s().getAppContext();
                if (appContext2 != null) {
                    if (i2 == 1) {
                        string = appContext2.getResources().getString(R.string.video_scaling_fit);
                    } else if (i2 == 2) {
                        string = appContext2.getResources().getString(R.string.video_scaling_crop);
                    } else {
                        string = appContext2.getResources().getString(R.string.video_scaling_stretch);
                    }
                    EventsGlobalTextNotifier.onTextMsg.invoke(string);
                }
            }
        }, this.listenerRefHolder);
        Fragment2.onRequestVideoScalingMode.subscribeWeak(new WeakEventR.Handler<Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Integer invoke() {
                return Integer.valueOf(AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_videoScalingMode));
            }
        }, this.listenerRefHolder);
        MediaControlsUI.onRequestVideoScalingMode.subscribeWeak(new WeakEventR.Handler<Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Integer invoke() {
                return Integer.valueOf(AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_videoScalingMode));
            }
        }, this.listenerRefHolder);
        Fragment2.onRequestVideoWidthHeightRatio.subscribeWeak(new WeakEventR.Handler<Float>() { // from class: com.daaw.avee.Design.VisualizerDesign.31
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Float invoke() {
                VisualizerDesign visualizerDesign = VisualizerDesign.this;
                return Float.valueOf(visualizerDesign.getSurfaceVideoSize(visualizerDesign.videoWidthHeightRatio));
            }
        }, this.listenerRefHolder);
        MediaControlsUI.onToggleVisualPreferShowContent.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.32
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                AppPreferences.createOrGetInstance().setInt(AppPreferences.PREF_Int_visualizerThemeId, AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId), true);
                AppPreferences.createOrGetInstance().toggleBool(AppPreferences.PREF_Bool_visualPreferShowVideoContent);
                Context appContext2 = PlayerCore.s().getAppContext();
                if (appContext2 != null) {
                    if (AppPreferences.createOrGetInstance().getBool(AppPreferences.PREF_Bool_visualPreferShowVideoContent)) {
                        EventsGlobalTextNotifier.onTextMsg.invoke(appContext2.getResources().getString(R.string.switched_to_video));
                    } else {
                        EventsGlobalTextNotifier.onTextMsg.invoke(appContext2.getResources().getString(R.string.switched_to_visualizer));
                    }
                }
            }
        }, this.listenerRefHolder);
        Fragment2.onVideoSurfaceHolderCreated.subscribeWeak(new WeakEvent1.Handler<SurfaceHolder>() { // from class: com.daaw.avee.Design.VisualizerDesign.33
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(SurfaceHolder surfaceHolder) {
                VisualizerDesign.this.surfaceHolder = new WeakReference(surfaceHolder);
                EventsPlaybackService.Receive.setVideoSurfaceHolder.invoke(surfaceHolder);
            }
        }, this.listenerRefHolder);
        Fragment2.onVideoSurfaceHolderDestroyed.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.34
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                VisualizerDesign.this.surfaceHolder = new WeakReference(null);
            }
        }, this.listenerRefHolder);
        MainActivity.onViewPagerPageSelected.subscribeWeak(new WeakEvent2.Handler<Integer, Activity>() { // from class: com.daaw.avee.Design.VisualizerDesign.35
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(Integer num, Activity activity) {
                if (num.intValue() == 2) {
                    VisualizerDesign.this.uiNeedShowVisual = true;
                    boolean bool = AppPreferences.createOrGetInstance().getBool(AppPreferences.PREF_Bool_visualPreferShowVideoContent);
                    Fragment2 fragment2Instance = MainActivity.getFragment2Instance();
                    if (fragment2Instance != null) {
                        fragment2Instance.updateSurfaceVisibility(true, bool);
                        return;
                    }
                    return;
                }
                VisualizerDesign.this.uiNeedShowVisual = false;
                boolean bool2 = AppPreferences.createOrGetInstance().getBool(AppPreferences.PREF_Bool_visualPreferShowVideoContent);
                Fragment2 fragment2Instance2 = MainActivity.getFragment2Instance();
                if (fragment2Instance2 != null) {
                    fragment2Instance2.updateSurfaceVisibility(false, bool2);
                }
            }
        }, this.listenerRefHolder);
        Fragment2.onUIComponentNeedChanged.subscribeWeak(new WeakEvent1.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.36
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(Boolean bool) {
                VisualizerDesign.this.uiNeedShowVisual = bool.booleanValue();
            }
        }, this.listenerRefHolder);
        Fragment2.onRequestUIComponentNeedChangedValue.subscribeWeak(new WeakEventR.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.37
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Boolean invoke() {
                return Boolean.valueOf(VisualizerDesign.this.uiNeedShowVisual);
            }
        }, this.listenerRefHolder);
        Fragment2.onVideoElementInteracted.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.38
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                MainActivity mainActivity = MainActivity.getInstance();
                if (mainActivity != null && mainActivity.currentFragmentPage == 2) {
                    mainActivity.toggleShowControls(mainActivity.currentFragmentPage);
                }
            }
        }, this.listenerRefHolder);
        Fragment2.onCustomizeAction.subscribeWeak(new C0049AnonymousClass38(), this.listenerRefHolder);
        CustomizeVisDialog.onDialogShowed.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.39
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                MainActivity mainActivity = MainActivity.getInstance();
                if (mainActivity != null && mainActivity.currentFragmentPage == 2) {
                    mainActivity.showControls(false, mainActivity.currentFragmentPage);
                }
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onDialogDismissed.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.40
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                MainActivity mainActivity = MainActivity.getInstance();
                if (mainActivity != null && mainActivity.currentFragmentPage == 2) {
                    mainActivity.showControls(true, mainActivity.currentFragmentPage);
                }
                VisualizerDesign.this.selectedSceneElementChanged(-1, false);
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onSelectedSceneElementChanged.subscribeWeak(new WeakEvent3.Handler<Integer, Integer, Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.41
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Integer num, Integer num2, Boolean bool) {
                VisualizerDesign.this.selectedSceneElementChanged(num2, bool.booleanValue());
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onResetAction.subscribeWeak(new WeakEvent3.Handler<ContextData, Integer, CustomScene>() { // from class: com.daaw.avee.Design.VisualizerDesign.42
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(ContextData contextData, Integer num, CustomScene customScene) {
                CustomScene themeObject2;
                if (VisualizerThemes.s().isTemplateIndex(num.intValue())) {
                    themeObject2 = VisualizerThemes.s().getThemeObject2(num.intValue());
                } else {
                    themeObject2 = VisualizerThemes.s().getThemeObject2(customScene.getBasedOnTemplate());
                }
                if (themeObject2 == null) {
                    tlog.w("Can't reset visualizer basedOnTemplate not valid");
                } else {
                    AppPreferences.createOrGetInstance().savePrefThemeCustomizationData(num.intValue(), themeObject2, true);
                }
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onCustomizationChanged.subscribeWeak(new WeakEvent4.Handler<Integer, CustomScene, Integer, WeakEvent2<Integer, CustomScene>>() { // from class: com.daaw.avee.Design.VisualizerDesign.43
            @Override // com.daaw.avee.Common.Events.WeakEvent4.Handler
            public void invoke(Integer num, CustomScene customScene, Integer num2, final WeakEvent2<Integer, CustomScene> weakEvent2) {
                VisualizerViewCore visualizerViewCore = (VisualizerViewCore) VisualizerDesign.this.visualizerSurfaceView.get();
                if (visualizerViewCore != null) {
                    visualizerViewCore.setThemeCustomizationDataUiTh(customScene, new Action1<VisualizerViewCore>() { // from class: com.daaw.avee.Design.VisualizerDesign.43.1
                        @Override // com.daaw.avee.Common.Action1
                        public void onInvoke(VisualizerViewCore visualizerViewCore2) {
                            if (weakEvent2 != null) {
                                VisualizerDesign.updateChangesToVisDialogGlTh(visualizerViewCore2, false, -1);
                            }
                        }
                    });
                }
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onFinishedCustomizing.subscribeWeak(new WeakEvent3.Handler<Integer, CustomScene, Integer>() { // from class: com.daaw.avee.Design.VisualizerDesign.44
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Integer num, CustomScene customScene, Integer num2) {
                AppPreferences.createOrGetInstance().savePrefThemeCustomizationData(num.intValue(), customScene, false);
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onImagePropertyPicking.subscribeWeak(new WeakEvent5.Handler<Fragment, TextView, List<String>, String, String>() { // from class: com.daaw.avee.Design.VisualizerDesign.45
            @Override // com.daaw.avee.Common.Events.WeakEvent5.Handler
            public void invoke(Fragment fragment, TextView textView, List<String> list, String str, String str2) {
                if (fragment.getActivity() != null && !fragment.getActivity().isFinishing()) {
                    VisualizerDesign.this.currentPickingTxtValue = new WeakReference(textView);
                    VisualizerDesign.this.currentPickingCustomizationData = list;
                    VisualizerDesign.this.currentPickingPropertyName = str;
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        fragment.startActivityForResult(Intent.createChooser(intent, fragment.getResources().getString(R.string.vis_customization_pick_image_title)), 43);
                        return;
                    }
                    Intent intent2 = new Intent("android.intent.action.OPEN_DOCUMENT");
                    intent2.addCategory("android.intent.category.OPENABLE");
                    intent2.setType("image/*");
                    intent2.setFlags(1);
                    fragment.startActivityForResult(intent2, 44);
                }
            }
        }, this.listenerRefHolder);
        CustomizeVisDialog.onActivityResult.subscribeWeak(new WeakEvent3.Handler<CustomizeVisDialog, Integer, Intent>() { // from class: com.daaw.avee.Design.VisualizerDesign.46
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(CustomizeVisDialog customizeVisDialog, Integer num, Intent intent) {
                try {
                    if (num.intValue() == 43 || num.intValue() == 44) {
                        int i = customizeVisDialog.rootIdentifier;
                        if (intent == null) {
                            tlog.w("Data is null");
                            return;
                        }
                        Uri data = intent.getData();
                        if (data == null) {
                            tlog.w("originalUri is null");
                            return;
                        }
                        File fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(i);
                        Activity activity = customizeVisDialog.getActivity();
                        if (activity == null) {
                            tlog.w("activity is null");
                            return;
                        }
                        InputStream inputStreamOpenInputStream = activity.getContentResolver().openInputStream(data);
                        String path = data.getPath();
                        if (path == null) {
                            path = "/picked_image";
                        }
                        File fileSaveFileToLocal = AppPreferences.SaveFileToLocal(fileGetVisualizerFolderFromIdentifier, inputStreamOpenInputStream, UtilsFileSys.extractFilenameAdditionalSeperator(path, LocalBitmapLoader.Separator));
                        if (fileSaveFileToLocal == null) {
                            tlog.w("Copy to local folder failed");
                            return;
                        }
                        if (inputStreamOpenInputStream != null) {
                            try {
                                inputStreamOpenInputStream.close();
                            } catch (IOException e) {
                            }
                        }
                        VisualizerDesign.this.CustomizeVisDialogOnActivityResult(customizeVisDialog, i, num.intValue(), null, LocalBitmapLoader.Prefix + fileSaveFileToLocal.getName(), 0);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }, this.listenerRefHolder);
        AppPreferences.onIntPreferenceChanged.subscribeWeak(new WeakEvent3.Handler<Integer, Integer, Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.47
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Integer num, Integer num2, Boolean bool) {
                if (num.intValue() == AppPreferences.PREF_Int_visualizerThemeId && bool.booleanValue()) {
                    int iIntValue = num2.intValue();
                    VisualizerDesign visualizerDesign = VisualizerDesign.this;
                    visualizerDesign.loadRootElement((VisualizerViewCore) visualizerDesign.visualizerSurfaceView.get(), iIntValue, false, null);
                } else {
                    if (num.intValue() == AppPreferences.PREF_Int_videoScalingMode) {
                        EventsPlaybackService.Receive.setVideoScalingMode.invoke(Integer.valueOf(VisualizerDesign.this.getPlayerbackVideoScalingMode()));
                        Fragment2 fragment2Instance = MainActivity.getFragment2Instance();
                        if (fragment2Instance != null) {
                            VisualizerDesign visualizerDesign2 = VisualizerDesign.this;
                            fragment2Instance.setVideoSize(visualizerDesign2.getSurfaceVideoSize(visualizerDesign2.videoWidthHeightRatio));
                            fragment2Instance.updateVideoScaleMode(num2.intValue());
                        }
                        MediaControlsUI mediaControlsUI = MediaControlsUI.getInstance();
                        if (mediaControlsUI != null) {
                            mediaControlsUI.updateVideoScaleMode(num2.intValue());
                            return;
                        }
                        return;
                    }
                    num.intValue();
                    int i = AppPreferences.PREF_Int_audioVisOffset;
                }
            }
        }, this.listenerRefHolder);
        AppPreferences.onBoolPreferenceChanged.subscribeWeak(new WeakEvent2.Handler<Integer, Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.48
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(Integer num, Boolean bool) {
                Fragment2 fragment2Instance;
                if (num.intValue() == AppPreferences.PREF_Bool_visualPreferShowVideoContent && (fragment2Instance = MainActivity.getFragment2Instance()) != null) {
                    fragment2Instance.setShowVideoContentState(bool.booleanValue());
                }
            }
        }, this.listenerRefHolder);
        MainActivity.onFullscreenChanged.subscribeWeak(new WeakEvent1.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerDesign.49
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(Boolean bool) {
                Fragment2 fragment2Instance = MainActivity.getFragment2Instance();
                if (fragment2Instance != null) {
                    fragment2Instance.animateShow(!bool.booleanValue());
                }
            }
        }, this.listenerRefHolder);
        MainActivity.onToolbarAction.subscribeWeak(new WeakEventR2.Handler() { // from class: com.daaw.avee.Design.VisualizerDesign.50
            @Override // com.daaw.avee.Common.Events.WeakEventR2.Handler
            public final Object invoke(Object obj, Object obj2) {
                return VisualizerDesign.lambda$new$0((ContextData) obj, (Integer) obj2);
            }
        }, this.listenerRefHolder);
    }

    private static class RandomCharCache {
        String currentChars;
        int increaseIndex;
        List<String> increaseList;
        boolean isIncrease;
        long lastUpdateTime;

        RandomCharCache(String str, long j) {
            this.currentChars = str;
            this.lastUpdateTime = j;
            this.isIncrease = false;
            this.increaseIndex = 0;
            this.increaseList = null;
        }

        RandomCharCache(String str, long j, List<String> list) {
            this.currentChars = str;
            this.lastUpdateTime = j;
            this.isIncrease = true;
            this.increaseIndex = 0;
            this.increaseList = list;
        }
    }

    class C0049AnonymousClass38 implements WeakEvent1.Handler<ContextData> {
        C0049AnonymousClass38() {
        }

        @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
        public void invoke(final ContextData contextData) {
            final DependencyResourceCounter dependencyResourceCounter = new DependencyResourceCounter();
            final VisualizerViewCore visualizerViewCore = (VisualizerViewCore) VisualizerDesign.this.visualizerSurfaceView.get();
            if (visualizerViewCore != null) {
                visualizerViewCore.queueEvent(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.C0049AnonymousClass38.1
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
                final boolean z = visualizerViewCore.getTag() != null && visualizerViewCore.getTag().equals("initialized");
                visualizerViewCore.queueEvent(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.C0049AnonymousClass38.2
                    @Override // java.lang.Runnable
                    public void run() {
                        final int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_visualizerThemeId);
                        final CustomScene customSceneCreate = CustomScene.Create();
                        visualizerViewCore.readThemeCustomizationData(customSceneCreate, dependencyResourceCounter);
                        VisualizerViewCore visualizerViewCore2 = visualizerViewCore;
                        final DependencyResourceCounter dependencyResourceCounter2 = dependencyResourceCounter;
                        final ContextData contextData2 = contextData;
                        if (z) {
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        visualizerViewCore2.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.C0049AnonymousClass38.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                FragmentManager fragmentManager;
                                try {
                                    VisualizerDesign.this.CleanLocalFolderResources(i, dependencyResourceCounter2.GetDependencyResourceNamesList());
                                } catch (Exception e2) {
                                    tlog.w("CleanLocalFolderResources failed: " + e2.getMessage());
                                }
                                Activity activity = contextData2.getActivity();
                                if (activity != null && !activity.isFinishing() && (fragmentManager = contextData2.getFragmentManager()) != null && !CustomizeVisDialog.isShowing(fragmentManager)) {
                                    CustomizeVisDialog.createAndShowCustomizeVisDialog(contextData2, Integer.valueOf(i), customSceneCreate, ElementsFactory.getAddableTypeNamesArray());
                                }
                            }
                        });
                    }
                });
                visualizerViewCore.setTag("initialized");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Typeface onRequestFontInternal(String str, int i) {
        Typeface typefaceCreateFromAsset;
        String name;
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext == null) {
            return Typeface.DEFAULT;
        }
        if (str == null || str.isEmpty() || EncodingUtils.DEFAULT_CODEC_NAME.equals(str)) {
            return Typeface.DEFAULT;
        }
        if ("default_bold".equals(str)) {
            return Typeface.DEFAULT_BOLD;
        }
        if (str.startsWith("internal_")) {
            try {
                if ("internal_0".equals(str)) {
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/greatvibes-regular.otf");
                } else if ("internal_1".equals(str)) {
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/notomono-regular.ttf");
                } else if ("internal_2".equals(str)) {
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/orbitron-medium.otf");
                } else if ("internal_3".equals(str)) {
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext.getAssets(), "fonts/orkney-light.otf");
                } else {
                    typefaceCreateFromAsset = Typeface.DEFAULT;
                }
                return typefaceCreateFromAsset;
            } catch (Exception e) {
                return Typeface.DEFAULT;
            }
        }
        if (str.startsWith(LocalFontLoader.Prefix)) {
            name = str.substring(5);
        } else if (!str.startsWith("/")) {
            name = null;
        } else {
            File file = new File(str);
            if (file.exists()) {
                try {
                    return Typeface.createFromFile(file);
                } catch (Exception e2) {
                    name = file.getName();
                }
            } else {
                name = new File(str).getName();
            }
        }
        if (name != null && !name.isEmpty()) {
            File fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(i);
            File file2 = new File(fileGetVisualizerFolderFromIdentifier, name);
            if (file2.exists()) {
                try {
                    return Typeface.createFromFile(file2);
                } catch (Exception e3) {
                }
            }
            File file3 = new File(new File(appContext.getFilesDir(), "custom_fonts"), name);
            if (file3.exists()) {
                try {
                    if (!fileGetVisualizerFolderFromIdentifier.exists()) {
                        fileGetVisualizerFolderFromIdentifier.mkdirs();
                    }
                    File file4 = new File(fileGetVisualizerFolderFromIdentifier, name);
                    if (!file4.exists()) {
                        UtilsFileSys.CopyFile(file3, file4);
                    }
                    if (!file4.exists()) {
                        file4 = file3;
                    }
                    return Typeface.createFromFile(file4);
                } catch (Exception e4) {
                    try {
                        return Typeface.createFromFile(file3);
                    } catch (Exception e5) {
                        return Typeface.DEFAULT;
                    }
                }
            }
        }
        return Typeface.DEFAULT;
    }

    static Boolean lambda$new$0(ContextData contextData, Integer num) {
        int iIntValue = num.intValue();
        if (iIntValue == 1) {
            VisualizerChooseDialog.createAndShowDialog(contextData.getActivity());
            return true;
        }
        if (iIntValue == 2) {
            Fragment2.onCustomizeAction.invoke(contextData);
            return true;
        }
        if (iIntValue == 3) {
            Fragment2.onExportAction.invoke(contextData);
            return true;
        }
        return true;
    }

    public void CustomizeVisDialogOnActivityResult(CustomizeVisDialog customizeVisDialog, int i, int i2, Uri uri, String str, int i3) {
        if (i2 == 43) {
            if (this.currentPickingCustomizationData != null && this.currentPickingPropertyName != null) {
                customizeVisDialog.onImagePropertyValuePicked(this.currentPickingTxtValue.get(), this.currentPickingCustomizationData, this.currentPickingPropertyName, str);
                return;
            } else {
                tlog.w("PICK_IMAGE_PROPERTY_REQUEST_CODE invalid data");
                return;
            }
        }
        if (i2 == 44 && Build.VERSION.SDK_INT >= 19) {
            if (this.currentPickingCustomizationData != null && this.currentPickingPropertyName != null) {
                if ((i3 & 64) != 0 && uri != null) {
                    tlog.w("offered FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
                    try {
                        customizeVisDialog.getActivity().getContentResolver().takePersistableUriPermission(uri, 1);
                    } catch (Exception e) {
                        EventsGlobalTextNotifier.onTextMsg.invoke("Error TakePersistableUriPermission " + e.getMessage());
                    }
                }
                try {
                    customizeVisDialog.onImagePropertyValuePicked(this.currentPickingTxtValue.get(), this.currentPickingCustomizationData, this.currentPickingPropertyName, str);
                    return;
                } catch (Exception e2) {
                    EventsGlobalTextNotifier.onTextMsg.invoke("Error OnImagePropertyValuePicked " + e2.getMessage());
                    return;
                }
            }
            tlog.w("PICK_IMAGE_PROPERTY_REQUEST_CODE_KITKAT invalid data");
        }
    }

    public Vec2f processBeatTriggerRandom(Vec2f vec2f, Vec2f vec2f2, MeterOutputData meterOutputData, float f) {
        float f2 = vec2f.x;
        float f3 = vec2f.y;
        if (isPlaying() && meterOutputData.frameDataRmsValue > f2 && !this.beatTriggerRandomTriggered) {
            this.beatTriggerRandomValue = ((this.randomGenerator.nextFloat() * 2.0f) * f3) - f3;
            this.beatTriggerRandomTriggered = true;
            this.beatTriggerRandomHoldTime = 0.0f;
        }
        if (this.beatTriggerRandomTriggered) {
            this.beatTriggerRandomHoldTime += f;
            if (this.beatTriggerRandomHoldTime >= 0.1f) {
                this.beatTriggerRandomTriggered = false;
            }
        }
        float fLerp = Interpolate.Lerp(vec2f2.x, this.beatTriggerRandomTriggered ? this.beatTriggerRandomValue : 0.0f, this.beatTriggerRandomTriggered ? 0.15f : 0.05f);
        return new Vec2f(fLerp, fLerp);
    }

    public Vec2f processBeatTriggerAnim(Vec2f vec2f, Vec2f vec2f2, MeterOutputData meterOutputData, float f) {
        float fLerp;
        float f2 = vec2f.x;
        float f3 = vec2f.y;
        boolean zIsPlaying = isPlaying();
        float f4 = meterOutputData.frameDataRmsValue;
        float f5 = vec2f2.x;
        float f6 = vec2f2.y;
        if (zIsPlaying && f4 > f2 && f6 < 1.9999999f) {
            this.beatTriggerAnimActive = true;
            f6 = 0.0f;
            f5 = 0.0f;
        }
        if (this.beatTriggerAnimActive) {
            f6 += f3 * f;
            if (f6 < 0.5f) {
                fLerp = f6 * 2.0f;
            } else if (f6 < 1.0f) {
                fLerp = 2.0f - (f6 * 2.0f);
            } else {
                this.beatTriggerAnimActive = false;
                f6 = 1.0f;
                fLerp = 0.0f;
            }
        } else {
            fLerp = Interpolate.Lerp(f5, 0.0f, 0.05d);
        }
        return new Vec2f(Math.max(0.0f, Math.min(1.0f, fLerp)), Math.min(1.0f, f6));
    }

    public Vec2f processTrackSetPosition(Vec2f vec2f, Vec2f vec2f2) {
        float fMax = Math.max(0.0f, Math.min(1.0f, vec2f.y));
        long trackDurationForRendering = AppDesign.getTrackDurationForRendering();
        if (trackDurationForRendering <= 0) {
            return new Vec2f(0.0f, 0.0f);
        }
        float f = trackDurationForRendering / 1000.0f;
        long trackPositionForRendering = AppDesign.getTrackPositionForRendering() - ((long) (Math.max(0.0f, Math.min(((vec2f.x + 1.0f) / 2.0f) * f, f)) * 1000.0f));
        if (trackPositionForRendering < 0) {
            return new Vec2f(0.0f, 0.0f);
        }
        if (trackPositionForRendering >= 300) {
            return new Vec2f(fMax, fMax);
        }
        float f2 = fMax * (trackPositionForRendering / 300.0f);
        return new Vec2f(f2, f2);
    }

    public Vec2f processBeatPeak(Vec2f vec2f, MeterOutputData meterOutputData) {
        float f = vec2f.x;
        float fMax = Math.max(0.0f, Math.min(1.0f, vec2f.y));
        if (f <= 0.0f) {
            return new Vec2f(0.0f, 0.0f);
        }
        float fMin = Math.min(Math.min(meterOutputData.frameDataRmsValue / f, 1.0f) * fMax, 1.0f);
        return new Vec2f(fMin, fMin);
    }

    public Vec2f processTrackSetEndPosition(Vec2f vec2f) {
        long trackDurationForRendering = AppDesign.getTrackDurationForRendering();
        if (trackDurationForRendering <= 0) {
            return new Vec2f(0.0f, 0.0f);
        }
        float f = trackDurationForRendering / 1000.0f;
        float fMax = Math.max(0.0f, Math.min(((vec2f.x + 1.0f) / 2.0f) * f, f));
        long trackPositionForRendering = AppDesign.getTrackPositionForRendering();
        long j = trackDurationForRendering - ((long) (fMax * 1000.0f));
        float fMax2 = Math.max(0.0f, Math.min(1.0f, vec2f.y));
        long j2 = trackPositionForRendering - j;
        if (j2 < 0) {
            return new Vec2f(0.0f, 0.0f);
        }
        if (j2 >= 300) {
            return new Vec2f(fMax2, fMax2);
        }
        float f2 = (j2 / 300.0f) * fMax2;
        return new Vec2f(f2, f2);
    }

    public static void selectThemePreset(int i) {
        AppPreferences.setCurrentThemeId(i);
        AppPreferences.createOrGetInstance().setInt(AppPreferences.PREF_Int_visualizerThemeId, i, true);
        AppPreferences.createOrGetInstance().setBool(AppPreferences.PREF_Bool_visualPreferShowVideoContent, false);
    }

    public void selectedSceneElementChanged(Integer num, boolean z) {
        if (z || this.selectedSceneElementId != num.intValue()) {
            this.selectedSceneElementId = num.intValue();
            VisualizerViewCore visualizerViewCore = this.visualizerSurfaceView.get();
            if (visualizerViewCore != null) {
                visualizerViewCore.setTriggerLogoTh(3, num.intValue());
            }
        }
    }

    public int getPlayerbackVideoScalingMode() {
        int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_videoScalingMode);
        return (i == 1 || i != 2) ? 1 : 2;
    }

    public float getSurfaceVideoSize(float f) {
        int i = AppPreferences.createOrGetInstance().getInt(AppPreferences.PREF_Int_videoScalingMode);
        if (i != 1) {
            if (i == 2 || i == 3) {
                return 0.0f;
            }
            return f;
        }
        return f;
    }

    private void applyThemeCustomizationData(VisualizerViewCore visualizerViewCore, int i, RootElement rootElement, CustomScene customScene, boolean z) {
        if (customScene == null) {
            if (visualizerViewCore != null) {
                visualizerViewCore.setThemeElementsUiTh(rootElement);
                return;
            }
            return;
        }
        if (rootElement.getVersionId().equals(customScene.getVersionId())) {
            rootElement.setThemeCustomizationData(customScene);
            if (visualizerViewCore != null) {
                visualizerViewCore.setThemeElementsUiTh(rootElement);
                return;
            }
            return;
        }
        tlog.w("visualizer customization, version mismatch");
        if (z) {
            rootElement.setThemeCustomizationData(customScene);
            if (visualizerViewCore != null) {
                visualizerViewCore.setThemeElementsUiTh(rootElement);
                return;
            }
            return;
        }
        customScene.setVersionId(rootElement.getVersionId());
        rootElement.setThemeCustomizationData(customScene);
        if (visualizerViewCore != null) {
            visualizerViewCore.setThemeElementsUiTh(rootElement);
        }
        AppPreferences.createOrGetInstance().savePrefThemeCustomizationData(i, customScene, false);
        Context activityContext = PlayerCore.s().getActivityContext();
        if (activityContext != null) {
            new AlertDialog.Builder(activityContext).setMessage(R.string.vis_customization_dialog_ver_mismatch).show();
        }
    }

    public static void applyThemeCustomizationData2(VisualizerViewCore visualizerViewCore, RootElement rootElement, int i, String str, CustomScene customScene, boolean z) {
        if (customScene == null) {
            if (visualizerViewCore != null) {
                visualizerViewCore.setThemeElementsUiTh((CustomScene) null);
                return;
            }
            return;
        }
        if (str.equals(customScene.getVersionId())) {
            if (visualizerViewCore != null) {
                visualizerViewCore.setThemeElementsUiTh(customScene);
            }
            if (rootElement != null) {
                rootElement.setThemeCustomizationData(customScene);
                return;
            }
            return;
        }
        tlog.w("visualizer customization, version mismatch");
        if (z) {
            if (visualizerViewCore != null) {
                visualizerViewCore.setThemeElementsUiTh(customScene);
            }
            if (rootElement != null) {
                rootElement.setThemeCustomizationData(customScene);
                return;
            }
            return;
        }
        customScene.setVersionId(str);
        AppPreferences.createOrGetInstance().savePrefThemeCustomizationData(i, customScene, false);
        Context activityContext = PlayerCore.s().getActivityContext();
        if (activityContext != null) {
            new AlertDialog.Builder(activityContext).setMessage(R.string.vis_customization_dialog_ver_mismatch).show();
        }
        if (visualizerViewCore != null) {
            visualizerViewCore.setThemeElementsUiTh(customScene);
        }
        if (rootElement != null) {
            rootElement.setThemeCustomizationData(customScene);
        }
    }

    public AudioFrameData getSoundVisualizationData(int i, AudioFrameData audioFrameData) {
        AppPreferences appPreferencesCreateOrGetInstance = AppPreferences.createOrGetInstance();
        AudioFrameData.Request2 request2 = new AudioFrameData.Request2();
        request2.useGlobalSession = appPreferencesCreateOrGetInstance.getBool(AppPreferences.PREF_Bool_visualizerUseGlobalSession);
        request2.overridePositionUs = -1L;
        request2.audioChannelCount = 1;
        request2.audioOffsetMs = appPreferencesCreateOrGetInstance.getInt(AppPreferences.PREF_Int_audioVisOffset);
        request2.samplesCountUs = 0L;
        request2.samplesCount = i;
        AudioFrameData audioFrameDataInvoke = getVisualizationData.invoke(audioFrameData, request2, null);
        return audioFrameDataInvoke != null ? audioFrameDataInvoke : new AudioFrameData();
    }

    boolean getUseHighQualityBlur() {
        return this.useHighQualityBlur;
    }

    boolean isPlaying() {
        return VisualizerExporterDesign.isExporting() || PlaybackDesign.isPlaying;
    }

    public static void updateChangesToVisDialogGlTh(final VisualizerViewCore visualizerViewCore, final boolean z, final int i) {
        visualizerViewCore.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.51
            @Override // java.lang.Runnable
            public void run() {
                VisualizerDesign.lambda$updateChangesToVisDialogGlTh$3(visualizerViewCore, z, i);
            }
        });
    }

    static void lambda$updateChangesToVisDialogGlTh$3(final VisualizerViewCore visualizerViewCore, final boolean z, final int i) {
        final CustomizeVisDialog customizeVisDialog = CustomizeVisDialog.getInstance();
        if (customizeVisDialog != null) {
            final CustomScene customSceneCreate = CustomScene.Create();
            visualizerViewCore.readThemeCustomizationDataUiTh(customSceneCreate, new DependencyResourceCounterDuymmy(), new Action1() { // from class: com.daaw.avee.Design.VisualizerDesign.52
                @Override // com.daaw.avee.Common.Action1
                public final void onInvoke(Object obj) {
                    VisualizerViewCore visualizerViewCore2 = visualizerViewCore;
                    final CustomizeVisDialog customizeVisDialog2 = customizeVisDialog;
                    final CustomScene customScene = customSceneCreate;
                    final boolean z2 = z;
                    final int i2 = i;
                    visualizerViewCore2.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerDesign.52.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            customizeVisDialog2.onVisualizerCustomizationChanged(customScene, z2, i2);
                        }
                    });
                }
            });
        }
    }

    void updateChangesToVisDialogGlTh(CustomScene customScene, boolean z, int i) {
        CustomizeVisDialog customizeVisDialog = CustomizeVisDialog.getInstance();
        if (customizeVisDialog != null) {
            customizeVisDialog.onVisualizerCustomizationChanged(customScene, z, i);
        }
    }

    private List<String> parseIncreaseExpression(String str) {
        boolean z;
        int i;
        boolean z2;
        ArrayList arrayList = new ArrayList();
        if (!str.startsWith("<") || !str.endsWith(">")) {
            return null;
        }
        String strSubstring = str.substring(1, str.length() - 1);
        if (strSubstring.toUpperCase().endsWith(",DOWN")) {
            strSubstring = strSubstring.substring(0, strSubstring.length() - 5).trim();
            z = true;
        } else {
            z = false;
        }
        int iLastIndexOf = strSubstring.lastIndexOf(44);
        if (iLastIndexOf > 0) {
            try {
                i = Integer.parseInt(strSubstring.substring(iLastIndexOf + 1).trim());
                if (i < 1) {
                    i = 1;
                }
                strSubstring = strSubstring.substring(0, iLastIndexOf).trim();
            } catch (NumberFormatException e) {
                i = 1;
            }
        } else {
            i = 1;
        }
        int iIndexOf = strSubstring.indexOf(45);
        if (iIndexOf < 0) {
            return null;
        }
        String strTrim = strSubstring.substring(0, iIndexOf).trim();
        String strTrim2 = strSubstring.substring(iIndexOf + 1).trim();
        char[] charArray = strTrim.toCharArray();
        int length = charArray.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                z2 = true;
                break;
            }
            if (!Character.isDigit(charArray[i2])) {
                z2 = false;
                break;
            }
            i2++;
        }
        if (z2) {
            for (char c : strTrim2.toCharArray()) {
                if (!Character.isDigit(c)) {
                    z2 = false;
                    break;
                }
            }
        }
        if (z2) {
            int i3 = Integer.parseInt(strTrim);
            int i4 = Integer.parseInt(strTrim2);
            int iMax = Math.max(strTrim.length(), strTrim2.length());
            if (z || i3 > i4) {
                while (i3 >= i4) {
                    arrayList.add(String.format("%0" + iMax + "d", Integer.valueOf(i3)));
                    i3 -= i;
                }
            } else {
                while (i3 <= i4) {
                    arrayList.add(String.format("%0" + iMax + "d", Integer.valueOf(i3)));
                    i3 += i;
                }
            }
        } else {
            if (strTrim.length() != 1 || strTrim2.length() != 1) {
                return null;
            }
            char cCharAt = strTrim.charAt(0);
            char cCharAt2 = strTrim2.charAt(0);
            if (z || cCharAt > cCharAt2) {
                while (cCharAt >= cCharAt2) {
                    arrayList.add(String.valueOf(cCharAt));
                    cCharAt = (char) (cCharAt - i);
                }
            } else {
                while (cCharAt <= cCharAt2) {
                    arrayList.add(String.valueOf(cCharAt));
                    cCharAt = (char) (cCharAt + i);
                }
            }
        }
        return arrayList;
    }

    private String generateRandomString(String str, int i) {
        String str2;
        int i2 = 0;
        if (i <= 0) {
            return null;
        }
        if (str.startsWith("[") && str.endsWith("]")) {
            String[] strArrSplit = str.substring(1, str.length() - 1).split(",");
            ArrayList arrayList = new ArrayList();
            for (String str3 : strArrSplit) {
                String strTrim = str3.trim();
                if (!strTrim.isEmpty()) {
                    if (strTrim.startsWith("<") && strTrim.endsWith(">")) {
                        List<String> increaseExpression = parseIncreaseExpression(strTrim);
                        if (increaseExpression != null) {
                            arrayList.addAll(increaseExpression);
                        }
                    } else {
                        arrayList.add(strTrim);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            while (i2 < i) {
                sb.append((String) arrayList.get(this.randomGenerator.nextInt(arrayList.size())));
                i2++;
            }
            return sb.toString();
        }
        if (str.startsWith("<") && str.endsWith(">")) {
            List<String> increaseExpression2 = parseIncreaseExpression(str);
            if (increaseExpression2 == null || increaseExpression2.isEmpty()) {
                return null;
            }
            StringBuilder sb2 = new StringBuilder();
            while (i2 < i) {
                sb2.append(increaseExpression2.get(this.randomGenerator.nextInt(increaseExpression2.size())));
                i2++;
            }
            return sb2.toString();
        }
        switch (str) {
            case "#":
                str2 = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
                break;
            case "*":
                str2 = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
                break;
            case "09":
                str2 = "0123456789";
                break;
            case "AZ":
                str2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;
            case "a9":
                str2 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                break;
            case "aZ":
                str2 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;
            case "az":
                str2 = "abcdefghijklmnopqrstuvwxyz";
                break;
            default:
                return null;
        }
        StringBuilder sb3 = new StringBuilder();
        while (i2 < i) {
            sb3.append(str2.charAt(this.randomGenerator.nextInt(str2.length())));
            i2++;
        }
        return sb3.toString();
    }

    private String getMeasurePlaceholderValue(String str, VisualizerViewCore visualizerViewCore) {
        int i;
        String strValueOf;
        int i2;
        String strSubstring;
        int i3;
        String strGenerateRandomString;
        String strSubstring2;
        int i4;
        RandomCharCache randomCharCache;
        RandomCharCache randomCharCache2;
        if (MeasureDefs.textTimeCurrent.equals(str)) {
            return Utils.getDurationStringHMSS((int) (AppDesign.getTrackPositionForRendering() / 1000));
        }
        if (MeasureDefs.textTimeRemaining.equals(str)) {
            return Utils.getDurationStringHMSS((int) (Math.max(0L, AppDesign.getTrackDurationForRendering() - AppDesign.getTrackPositionForRendering()) / 1000));
        }
        if (MeasureDefs.textTimeLength.equals(str)) {
            return Utils.getDurationStringHMSS((int) (AppDesign.getTrackDurationForRendering() / 1000));
        }
        if (MeasureDefs.textArtist.equals(str)) {
            PlaylistSong.Data data = PlaybackControlsDesign.fieldsongData;
            return data.isArtistKnown() ? data.artistName : "";
        }
        if (MeasureDefs.textArtistOrTitle.equals(str)) {
            PlaylistSong.Data data2 = PlaybackControlsDesign.fieldsongData;
            return data2.isArtistKnown() ? data2.artistName : PlaybackControlsDesign.fieldsongData.trackName;
        }
        if (MeasureDefs.textArtistAndTitle.equals(str)) {
            PlaylistSong.Data data3 = PlaybackControlsDesign.fieldsongData;
            if (data3.isArtistKnown()) {
                return String.valueOf(String.valueOf(String.valueOf(data3.artistName))) + "\n" + data3.trackName;
            }
            return "\n" + data3.trackName;
        }
        if (MeasureDefs.textMarkedArtistAndTitle.equals(str)) {
            PlaylistSong.Data data4 = PlaybackControlsDesign.fieldsongData;
            if (data4.isArtistKnown()) {
                return "Avee Player\n" + data4.artistName + " - " + data4.trackName;
            }
            return "Avee Player\n" + data4.trackName;
        }
        if (MeasureDefs.textTitle.equals(str)) {
            return PlaybackControlsDesign.fieldsongData.trackName;
        }
        if (MeasureDefs.textAlbum.equals(str)) {
            return PlaybackControlsDesign.fieldsongData.albumName;
        }
        if (MeasureDefs.textFps.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameStats().getFps()).toString();
        }
        if (MeasureDefs.textFrameTime.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameTimeMs()).toString();
        }
        if (MeasureDefs.textStatFlush.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameStats().getStatFlushCount()).toString();
        }
        if (MeasureDefs.textWaitResLoad.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameStats().getFrameResourcesLoadingCount()).toString();
        }
        if (MeasureDefs.textResLoad.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameStats().getStatResourceLoadCount()).toString();
        }
        if (MeasureDefs.textGradualResLoad.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameStats().getStatGradualResourceLoadCount()).toString();
        }
        if (MeasureDefs.textRTSwitch.equals(str)) {
            return new StringBuilder().append(visualizerViewCore.getFrameStats().getStatRTSwitchCount()).toString();
        }
        if ("{FPS}".equals(str)) {
            return String.valueOf(visualizerViewCore.getFrameStats().getFps());
        }
        if ("{Progress}".equals(str) || (str.startsWith("{Progress:") && str.endsWith("}"))) {
            long trackPositionForRendering = AppDesign.getTrackPositionForRendering();
            long trackDurationForRendering = AppDesign.getTrackDurationForRendering();
            if (trackDurationForRendering <= 0) {
                return "0%";
            }
            double d = ((double) trackPositionForRendering) / ((double) trackDurationForRendering);
            String str2 = null;
            String strSubstring3 = str.substring(10, str.length() - 1);
            if (strSubstring3.isEmpty()) {
                i = 100;
            } else {
                String[] strArrSplit = strSubstring3.split(",");
                i = 100;
                for (String str3 : strArrSplit) {
                    String strTrim = str3.trim();
                    if (!strTrim.isEmpty()) {
                        if (strTrim.startsWith("n")) {
                            try {
                                i = Integer.parseInt(strTrim.substring(1));
                                if (i != 1 && i != 10 && i != 100 && i != 1000 && i != 10000) {
                                    i = 100;
                                }
                            } catch (NumberFormatException e) {
                                i = 100;
                            }
                        } else if (strTrim.matches("x+")) {
                            str2 = strTrim;
                        }
                    }
                }
            }
            int iRound = (int) Math.round(((double) i) * d);
            if (str2 != null && !str2.isEmpty()) {
                int length = str2.length();
                strValueOf = String.format("%0" + length + "d", Integer.valueOf(iRound));
                if (iRound >= Math.pow(10.0d, length)) {
                    strValueOf = String.valueOf(iRound);
                }
            } else {
                strValueOf = String.valueOf(iRound);
            }
            if (i == 100) {
                return String.valueOf(strValueOf) + "%";
            }
            if (i == 1000) {
                return String.valueOf(strValueOf) + "‰";
            }
            return i == 10000 ? String.valueOf(strValueOf) + "‱" : strValueOf;
        }
        if (str.startsWith("{Increase:[") && str.endsWith("}")) {
            String strSubstring4 = str.substring(11, str.length() - 1);
            int iLastIndexOf = strSubstring4.lastIndexOf(92);
            if (iLastIndexOf > 0) {
                strSubstring2 = strSubstring4.substring(0, iLastIndexOf);
                try {
                    int i5 = Integer.parseInt(strSubstring4.substring(iLastIndexOf + 1));
                    i4 = i5 < 0 ? 0 : i5;
                } catch (NumberFormatException e2) {
                    i4 = 0;
                }
            } else {
                strSubstring2 = strSubstring4;
                i4 = 0;
            }
            ArrayList arrayList = new ArrayList();
            String strSubstring5 = strSubstring2;
            while (strSubstring5.length() > 0) {
                int iIndexOf = strSubstring5.indexOf(60);
                int iIndexOf2 = strSubstring5.indexOf(62);
                if (iIndexOf < 0 || iIndexOf2 <= iIndexOf) {
                    break;
                }
                List<String> increaseExpression = parseIncreaseExpression(strSubstring5.substring(iIndexOf, iIndexOf2 + 1));
                if (increaseExpression != null) {
                    arrayList.addAll(increaseExpression);
                }
                strSubstring5 = strSubstring5.substring(iIndexOf2 + 1);
            }
            if (arrayList.isEmpty() && strSubstring2.startsWith("[") && strSubstring2.endsWith("]")) {
                String[] strArrSplit2 = strSubstring2.substring(1, strSubstring2.length() - 1).split(",");
                for (String str4 : strArrSplit2) {
                    String strTrim2 = str4.trim();
                    if (!strTrim2.isEmpty()) {
                        arrayList.add(strTrim2);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return str;
            }
            if (i4 == 0) {
                RandomCharCache randomCharCache3 = this.randomCharCache.get(str);
                if (randomCharCache3 == null || !randomCharCache3.isIncrease) {
                    randomCharCache2 = new RandomCharCache((String) arrayList.get(0), System.currentTimeMillis(), arrayList);
                    this.randomCharCache.put(str, randomCharCache2);
                } else {
                    randomCharCache2 = randomCharCache3;
                }
                int i6 = randomCharCache2.increaseIndex;
                String str5 = (String) arrayList.get(i6 % arrayList.size());
                randomCharCache2.increaseIndex = (i6 + 1) % arrayList.size();
                randomCharCache2.currentChars = str5;
                randomCharCache2.lastUpdateTime = System.currentTimeMillis();
                return str5;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            RandomCharCache randomCharCache4 = this.randomCharCache.get(str);
            if (randomCharCache4 == null || !randomCharCache4.isIncrease) {
                randomCharCache = new RandomCharCache((String) arrayList.get(0), jCurrentTimeMillis, arrayList);
                this.randomCharCache.put(str, randomCharCache);
            } else {
                randomCharCache = randomCharCache4;
            }
            if (jCurrentTimeMillis - randomCharCache.lastUpdateTime >= i4) {
                int size = (randomCharCache.increaseIndex + 1) % arrayList.size();
                randomCharCache.increaseIndex = size;
                randomCharCache.currentChars = (String) arrayList.get(size);
                randomCharCache.lastUpdateTime = jCurrentTimeMillis;
            }
            return randomCharCache.currentChars;
        }
        if (!str.startsWith("{?") || !str.endsWith("}")) {
            return "{kskbl}".equals(str) ? "zdjd" : str;
        }
        String strSubstring6 = str.substring(2, str.length() - 1);
        int iIndexOf3 = strSubstring6.indexOf(92);
        if (iIndexOf3 > 0) {
            strSubstring = strSubstring6.substring(0, iIndexOf3);
            try {
                int i7 = Integer.parseInt(strSubstring6.substring(iIndexOf3 + 1));
                i2 = i7 < 0 ? 0 : i7;
            } catch (NumberFormatException e3) {
                i2 = 0;
            }
        } else {
            i2 = 0;
            strSubstring = strSubstring6;
        }
        int iIndexOf4 = strSubstring.indexOf(40);
        if (iIndexOf4 <= 0 || !strSubstring.endsWith(")")) {
            i3 = 1;
        } else {
            String strSubstring7 = strSubstring.substring(0, iIndexOf4);
            try {
                int i8 = Integer.parseInt(strSubstring.substring(iIndexOf4 + 1, strSubstring.length() - 1));
                if (i8 < 1) {
                    i8 = 1;
                }
                i3 = i8 <= 100 ? i8 : 100;
                strSubstring = strSubstring7;
            } catch (NumberFormatException e4) {
                i3 = 1;
            }
        }
        String strGenerateRandomString2 = generateRandomString(strSubstring, i3);
        if (strGenerateRandomString2 == null) {
            return str;
        }
        if (i2 == 0) {
            return strGenerateRandomString2;
        }
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        RandomCharCache randomCharCache5 = this.randomCharCache.get(str);
        if (randomCharCache5 == null || randomCharCache5.isIncrease) {
            randomCharCache5 = new RandomCharCache(strGenerateRandomString2, jCurrentTimeMillis2);
            this.randomCharCache.put(str, randomCharCache5);
        }
        if (jCurrentTimeMillis2 - randomCharCache5.lastUpdateTime >= i2 && (strGenerateRandomString = generateRandomString(strSubstring, i3)) != null) {
            randomCharCache5.currentChars = strGenerateRandomString;
            randomCharCache5.lastUpdateTime = jCurrentTimeMillis2;
        }
        return randomCharCache5.currentChars;
    }

    public String formatMeasureString(String str, VisualizerViewCore visualizerViewCore) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(32);
        boolean z = false;
        StringBuilder sb2 = null;
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '{') {
                if (!z) {
                    z = true;
                    sb2 = new StringBuilder(16);
                    sb2.append(cCharAt);
                }
            } else if (cCharAt == '}') {
                if (z) {
                    sb2.append(cCharAt);
                    String measurePlaceholderValue = getMeasurePlaceholderValue(sb2.toString(), visualizerViewCore);
                    if (measurePlaceholderValue == null) {
                        measurePlaceholderValue = "";
                    }
                    sb.append(measurePlaceholderValue);
                    z = false;
                    sb2 = null;
                }
            } else if (z) {
                sb2.append(cCharAt);
            } else {
                sb.append(cCharAt);
            }
        }
        if (z && sb2 != null) {
            sb.append(sb2.toString());
        }
        return sb.toString();
    }

    public void loadRootElement(VisualizerViewCore visualizerViewCore, int i, boolean z, int[] iArr) {
        AppPreferences.setCurrentThemeId(i);
        loadRootElementWithCustomSecene(visualizerViewCore, null, i, z, getPrefThemeCustomizationDataOrTemplateData(i, iArr));
    }

    public void loadRootElementWithCustomSecene(VisualizerViewCore visualizerViewCore, RootElement rootElement, int i, boolean z, CustomScene customScene) {
        applyThemeCustomizationData2(visualizerViewCore, rootElement, i, "10", customScene, z);
    }

    public static CustomScene getPrefThemeCustomizationDataOrTemplateData(int i, int[] iArr) {
        CustomScene prefThemeCustomizationData = AppPreferences.createOrGetInstance().getPrefThemeCustomizationData(i, iArr);
        if (prefThemeCustomizationData == null) {
            CustomScene themeObject2 = VisualizerThemes.s().getThemeObject2(i);
            if (themeObject2 == null) {
                return null;
            }
            if (iArr != null) {
                iArr[0] = AppPreferences.generateVisualizerCustomizationHash(themeObject2.serialize());
                return themeObject2;
            }
            return themeObject2;
        }
        return prefThemeCustomizationData;
    }

    public static int addNewVisualizer(CustomScene customScene, List<Integer> list, int i) {
        AppPreferences appPreferencesCreateOrGetInstance = AppPreferences.createOrGetInstance();
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext != null) {
            appPreferencesCreateOrGetInstance.load(appContext);
        }
        List<Integer> prefThemeCustomizationIndexes = appPreferencesCreateOrGetInstance.getPrefThemeCustomizationIndexes();
        if (prefThemeCustomizationIndexes != null) {
            list = prefThemeCustomizationIndexes;
        }
        if (customScene == null) {
            return -1;
        }
        int i2 = 0;
        while (true) {
            if (!VisualizerThemes.s().isTemplateIndex(i2) && !list.contains(Integer.valueOf(i2))) {
                break;
            }
            i2++;
        }
        if (AppPreferences.themeCustomizationIdentifierToPref(i2) < 0) {
            Context appContext2 = PlayerCore.s().getAppContext();
            if (appContext2 != null) {
                EventsGlobalTextNotifier.onTextMsg.invoke(appContext2.getResources().getString(R.string.vis_error_duplicating));
            }
            return -1;
        }
        list.add(Integer.valueOf(i2));
        if (customScene != null && customScene.getBasedOnTemplate() < 0 && i > 0) {
            customScene.setBasedOnTemplate(i);
        }
        if (customScene != null) {
            appPreferencesCreateOrGetInstance.savePrefThemeCustomizationData(i2, customScene, false);
        }
        appPreferencesCreateOrGetInstance.setPrefThemeCustomizationIndexes(list);
        return i2;
    }

    public void CleanLocalFolderResources(int i, List<String> list) {
        File fileGetVisualizerFolderFromIdentifier;
        File[] fileArrListFiles;
        int i2;
        int i3 = 0;
        if (i >= 0 && (fileGetVisualizerFolderFromIdentifier = AppPreferences.GetVisualizerFolderFromIdentifier(i)) != null && fileGetVisualizerFolderFromIdentifier.exists() && (fileArrListFiles = fileGetVisualizerFolderFromIdentifier.listFiles()) != null && fileArrListFiles.length != 0) {
            ArrayList<File> arrayList = new ArrayList();
            for (File file : fileArrListFiles) {
                if (file != null) {
                    arrayList.add(file);
                }
            }
            int i4 = 0;
            int i5 = -1;
            int i6 = -1;
            while (i4 < arrayList.size()) {
                File file2 = (File) arrayList.get(i4);
                if (file2 != null) {
                    String name = file2.getName();
                    i2 = VisualizerSharingDesign.PackedContent_SceneFileName.equals(name) ? i4 : i6;
                    if (VisualizerSharingDesign.StringContent_FileExtWithDotAlt.equals(UtilsFileSys.extractFilenameExtWithDot(name))) {
                        i5 = i4;
                    }
                } else {
                    i2 = i6;
                }
                i4++;
                i6 = i2;
            }
            if (i6 == -1) {
                i6 = i5;
            }
            if (i6 >= 0 && i6 < arrayList.size()) {
                arrayList.set(i6, null);
            }
            if (list != null && !list.isEmpty()) {
                for (int i7 = 0; i7 < arrayList.size(); i7++) {
                    File file3 = (File) arrayList.get(i7);
                    if (file3 != null) {
                        for (String str : list) {
                            if (str != null && CompareResourceName(file3.getName(), str)) {
                                arrayList.set(i7, null);
                                break;
                            }
                        }
                    }
                }
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            for (File file4 : arrayList) {
                if (file4 != null && file4.exists()) {
                    try {
                        if (!file4.delete()) {
                            tlog.w("Failed to delete: " + file4.getName());
                        }
                        i3++;
                        if (i3 % 5 == 0 && System.currentTimeMillis() - jCurrentTimeMillis > 200) {
                            tlog.w("CleanLocalFolderResources timeout, skipping remaining");
                            return;
                        }
                    } catch (Exception e) {
                        tlog.w("Error deleting: " + file4.getName());
                    }
                }
            }
        }
    }

    boolean CompareResourceName(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        return str.equals(UtilsFileSys.extractFilenameAdditionalSeperator(str2, LocalBitmapLoader.Separator));
    }
}
