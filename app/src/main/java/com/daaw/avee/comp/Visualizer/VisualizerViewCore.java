package com.daaw.avee.comp.Visualizer;

import android.content.Context;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Events.WeakEventR2;
import com.daaw.avee.Common.Events.WeakEventR5;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.comp.AlbumArt.AlbumArtRequest;
import com.daaw.avee.comp.AlbumArt.AlbumArtRequest2;
import com.daaw.avee.comp.AlbumArt.ImageLoadedListener;
import com.daaw.avee.comp.Common.MeterOutputData;
import com.daaw.avee.comp.Common.TrackDurationInfo;
import com.daaw.avee.comp.Visualizer.Elements.RootElement;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RendererCore;
import com.daaw.avee.comp.playback.AudioFrameData;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import com.daaw.avee.vendor.glutil.graphics.Texture;

/* JADX INFO: loaded from: classes.dex */
public class VisualizerViewCore extends GLSurfaceView {
    private int EGLContextClientVersion;
    private int dbgFrameCounter;
    InternalVisualizationDataProvider internalDataProvider;
    private RendererCore renderer;
    public static WeakEventR1<Integer, AudioFrameData> onRequestsSoundVisualizationData = new WeakEventR1<>();
    public static WeakEventR2<String, VisualizerViewCore, String> onRequestMeasureText = new WeakEventR2<>();
    public static WeakEventR5<String, Vec2f, Vec2f, MeterOutputData, IRenderState, Vec2f> onRequestMeasureVec2f = new WeakEventR5<>();
    public static WeakEventR<AlbumArtRequest> onRequestsAlbumArtPath = new WeakEventR<>();
    public static WeakEvent2<ImageLoadedListener, AlbumArtRequest2> onRequestAlbumArtPathAndBitmap = new WeakEvent2<>();
    public static WeakEventR<CustomScene> onRequestSelectedSkinThemePreset = new WeakEventR<>();
    public static WeakEvent onThemeChanged = new WeakEvent();
    public static WeakEvent2<Float, Float> onFrameStart = new WeakEvent2<>();
    public static WeakEventR<FrameCaptureDesc> onRequestFrameCaptureMode = new WeakEventR<>();
    public static WeakEvent4<int[], Integer, Integer, int[]> onFrameCaptured = new WeakEvent4<>();
    public static WeakEventR<Boolean> onRequestUseHighQualityBlur = new WeakEventR<>();
    public static WeakEventR<Integer> onRequestOverrideAudioFrameSamplesInCount = new WeakEventR<>();
    public static WeakEvent3<Texture, Boolean, String> onOffScreenContentRendered = new WeakEvent3<>();
    public static WeakEventR1<Integer, Integer> onRequestConvertDpToPx = new WeakEventR1<>();
    public static WeakEventR1<Context, Integer> onRequestRenderQualityEdgesValue = new WeakEventR1<>();
    public static WeakEventR1<Context, Integer> onRequestAudioProcessMultiThreadValue = new WeakEventR1<>();
    public static WeakEventR1<Context, TrackDurationInfo> onRequestTrackDurationInfo = new WeakEventR1<>();
    public static WeakEventR2<String, Integer, Typeface> onRequestFont = new WeakEventR2<>();

    public VisualizerViewCore(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.EGLContextClientVersion = 2;
        this.dbgFrameCounter = 0;
        this.internalDataProvider = new InternalVisualizationDataProvider() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.1
            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public AudioFrameData onRequestSoundVisualizationData(int i) {
                return VisualizerViewCore.onRequestsSoundVisualizationData.invoke(Integer.valueOf(i), null);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public String onRequestsMeasureText(String str) {
                return VisualizerViewCore.onRequestMeasureText.invoke(str, VisualizerViewCore.this, str);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public Vec2f onRequestMeasureVec2f(String str, Vec2f vec2f, Vec2f vec2f2, MeterOutputData meterOutputData, IRenderState iRenderState) {
                return VisualizerViewCore.onRequestMeasureVec2f.invoke(str, vec2f, vec2f2, meterOutputData, iRenderState, vec2f2);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public AlbumArtRequest onRequestsAlbumArtPath() {
                return VisualizerViewCore.onRequestsAlbumArtPath.invoke(null);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public void onRequestAlbumArtPathAndBitmap(ImageLoadedListener imageLoadedListener, AlbumArtRequest2 albumArtRequest2) {
                VisualizerViewCore.onRequestAlbumArtPathAndBitmap.invoke(imageLoadedListener, albumArtRequest2);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public boolean onRequestUseHighQualityBlur() {
                return VisualizerViewCore.onRequestUseHighQualityBlur.invoke(false).booleanValue();
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public int onRequestOverrideAudioFrameSamplesInCount() {
                return VisualizerViewCore.onRequestOverrideAudioFrameSamplesInCount.invoke(0).intValue();
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public void onOffScreenContentRendered(Texture texture, boolean z, String str) {
                VisualizerViewCore.onOffScreenContentRendered.invoke(texture, Boolean.valueOf(z), str);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public int onRequestConvertDpToPx(int i) {
                return VisualizerViewCore.onRequestConvertDpToPx.invoke(Integer.valueOf(i), Integer.valueOf(i)).intValue();
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public int onRequestAntiAliasingValue() {
                return VisualizerViewCore.onRequestRenderQualityEdgesValue.invoke(VisualizerViewCore.this.getContext(), 0).intValue();
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public int onRequestAudioProcessMultiThreadValue() {
                return VisualizerViewCore.onRequestAudioProcessMultiThreadValue.invoke(VisualizerViewCore.this.getContext(), 3).intValue();
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public void onThemeChanged() {
                VisualizerViewCore.onThemeChanged.invoke();
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public TrackDurationInfo onRequestTrackDurationInfo() {
                return VisualizerViewCore.onRequestTrackDurationInfo.invoke(VisualizerViewCore.this.getContext(), null);
            }

            @Override // com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider
            public Typeface onRequestFont(String str, int i) {
                return VisualizerViewCore.onRequestFont.invoke(str, Integer.valueOf(i), null);
            }
        };
        setEGLContextClientVersion(this.EGLContextClientVersion);
        setEGLConfigChooser(true);
        RendererCore rendererCore = new RendererCore(context, context.getResources(), this.internalDataProvider);
        this.renderer = rendererCore;
        setRenderer(rendererCore);
    }

    public FrameStats getFrameStats() {
        return this.renderer.getFrameStats();
    }

    public int getFrameTimeMs() {
        return this.renderer.getFrameTimeMs();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setThemeElements(RootElement rootElement) {
        this.renderer.setThemeElements(rootElement);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setThemeElements(CustomScene customScene) {
        this.renderer.setThemeElements(customScene);
    }

    public void setThemeElementsUiTh(final CustomScene customScene) {
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.2
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.setThemeElements(customScene);
            }
        });
    }

    public void setThemeElementsUiTh(final RootElement rootElement) {
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.3
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.setThemeElements(rootElement);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestRefreshThemeElements() {
        this.renderer.requestRefreshThemeElements();
    }

    public void requestRefreshThemeElementsUiTh() {
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.4
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.requestRefreshThemeElements();
            }
        });
    }

    public void setThemeCustomizationData(CustomScene customScene) {
        UtilsUI.AssertIsNotUiThread();
        this.renderer.setThemeCustomizationData(customScene);
    }

    public void setThemeCustomizationDataUiTh(final CustomScene customScene, final Action1<VisualizerViewCore> action1) {
        // 保持原版语义：序列化在 GL 线程执行（不在 UI 线程做重活，避免快速连切模板时 UI 卡死）。
        // 并发安全由 CustomScene 内部的锁保证（_syncLock），不再依赖"UI 线程预序列化"。
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.5
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.setThemeCustomizationData(CustomScene.CreateFromString(customScene.serialize()));
                Action1 action2 = action1;
                if (action2 != null) {
                    action2.onInvoke(VisualizerViewCore.this);
                }
            }
        });
    }

    public void readThemeCustomizationData(CustomScene customScene, IDependencyResourceCounter iDependencyResourceCounter) {
        this.renderer.readThemeCustomizationData(customScene, iDependencyResourceCounter);
    }

    public void readThemeCustomizationDataUiTh(final CustomScene customScene, final IDependencyResourceCounter iDependencyResourceCounter, final Action1<VisualizerViewCore> action1) {
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.6
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.renderer.readThemeCustomizationData(customScene, iDependencyResourceCounter);
                if (action1 != null) {
                    action1.onInvoke(VisualizerViewCore.this);
                }
            }
        });
    }

    private void resetVisualizer() {
        this.renderer.resetVisualizer();
    }

    public void resetVisualizerTh() {
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.7
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.renderer.resetVisualizer();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTriggerLogo(int i, int i2) {
        this.renderer.setTriggerLogo(i, i2);
    }

    public void setTriggerLogoTh(final int i, final int i2) {
        queueEvent(new Runnable() { // from class: com.daaw.avee.comp.Visualizer.VisualizerViewCore.8
            @Override // java.lang.Runnable
            public void run() {
                VisualizerViewCore.this.setTriggerLogo(i, i2);
            }
        });
    }

    private abstract class BaseConfigChooser implements GLSurfaceView.EGLConfigChooser {
        protected int[] configSpec;

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] iArr) {
            this.configSpec = filterConfigSpec(iArr);
        }

        @Override // android.opengl.GLSurfaceView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] iArr = new int[1];
            if (!egl10.eglChooseConfig(eGLDisplay, this.configSpec, null, 0, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int i = iArr[0];
            if (i <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (!egl10.eglChooseConfig(eGLDisplay, this.configSpec, eGLConfigArr, i, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig eGLConfigChooseConfig = chooseConfig(egl10, eGLDisplay, eGLConfigArr);
            if (eGLConfigChooseConfig != null) {
                return eGLConfigChooseConfig;
            }
            throw new IllegalArgumentException("No config chosen");
        }

        private int[] filterConfigSpec(int[] iArr) {
            if (VisualizerViewCore.this.EGLContextClientVersion == 2 || VisualizerViewCore.this.EGLContextClientVersion == 3) {
                if (VisualizerViewCore.this.EGLContextClientVersion == 2 && Build.VERSION.SDK_INT >= 17) {
                    int length = iArr.length;
                    int[] iArr2 = new int[length + 2];
                    int i = length - 1;
                    System.arraycopy(iArr, 0, iArr2, 0, i);
                    iArr2[i] = 12352;
                    iArr2[length] = 4;
                    iArr2[length + 1] = 12344;
                    return iArr2;
                }
                if (Build.VERSION.SDK_INT >= 18) {
                    int length2 = iArr.length;
                    int[] iArr3 = new int[length2 + 2];
                    int i2 = length2 - 1;
                    System.arraycopy(iArr, 0, iArr3, 0, i2);
                    iArr3[i2] = 12352;
                    iArr3[length2] = 64;
                    iArr3[length2 + 1] = 12344;
                    return iArr3;
                }
                return iArr;
            }
            return iArr;
        }
    }

    private class MyEGLConfigChooser extends BaseConfigChooser {
        protected int alphaSize;
        protected int blueSize;
        protected int depthSize;
        protected int greenSize;
        protected int maxSamples;
        protected int minSamples;
        protected int redSize;
        protected int sampleBuffers;
        protected int stencilSize;
        private int[] value;

        public MyEGLConfigChooser(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.value = new int[1];
            this.redSize = i;
            this.greenSize = i2;
            this.blueSize = i3;
            this.alphaSize = i4;
            this.depthSize = i5;
            this.stencilSize = i6;
            this.sampleBuffers = i7;
            this.minSamples = i8;
            this.maxSamples = i9;
        }

        @Override // com.daaw.avee.comp.Visualizer.VisualizerViewCore.BaseConfigChooser
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            ArrayList<EGLConfig> arrayList = new ArrayList();
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int iFindConfigAttrib = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int iFindConfigAttrib2 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (iFindConfigAttrib >= this.depthSize && iFindConfigAttrib2 >= this.stencilSize) {
                    int iFindConfigAttrib3 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int iFindConfigAttrib4 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int iFindConfigAttrib5 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int iFindConfigAttrib6 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (iFindConfigAttrib3 == this.redSize && iFindConfigAttrib4 == this.greenSize && iFindConfigAttrib5 == this.blueSize && iFindConfigAttrib6 >= this.alphaSize) {
                        arrayList.add(eGLConfig);
                    }
                }
            }
            int i = this.maxSamples;
            EGLConfig eGLConfig2 = null;
            int i2 = i;
            for (EGLConfig eGLConfig3 : arrayList) {
                int iFindConfigAttrib7 = findConfigAttrib(egl10, eGLDisplay, eGLConfig3, 12338, 0);
                int iFindConfigAttrib8 = findConfigAttrib(egl10, eGLDisplay, eGLConfig3, 12337, 0);
                if (iFindConfigAttrib7 == this.sampleBuffers && iFindConfigAttrib8 > 0 && iFindConfigAttrib8 <= i2) {
                    eGLConfig2 = eGLConfig3;
                    i2 = iFindConfigAttrib8;
                }
            }
            if (eGLConfig2 != null) {
                return eGLConfig2;
            }
            if (arrayList.size() > 0) {
                return (EGLConfig) arrayList.get(0);
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.value) ? this.value[0] : i2;
        }
    }
}
