package com.daaw.avee.comp.Visualizer;

import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.Common.MeterOutputData;
import com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Meter {
    private float framePeakBarIndex;
    private IRenderState renderState;
    private MeterOutputData meterOutputData = new MeterOutputData();
    private List<WeakReference<ISegmentDataProvider>> audioDataProviderWeak = new ArrayList();

    public float measureLocalSpaceX(float f, boolean z, float f2, float f3) {
        return (!z || f3 >= f2) ? f * f2 : f * f3;
    }

    public float measureLocalSpaceY(float f, boolean z, float f2, float f3) {
        return (!z || f2 >= f3) ? f * f3 : f * f2;
    }

    public Meter(IRenderState iRenderState) {
        this.renderState = iRenderState;
    }

    public float measureScreenSpace(float f) {
        int screenWidth;
        if (this.renderState.getScreenHeight() < this.renderState.getScreenWidth()) {
            screenWidth = this.renderState.getScreenHeight();
        } else {
            screenWidth = this.renderState.getScreenWidth();
        }
        return f * screenWidth;
    }

    public float measureScreenSpaceX(float f, boolean z) {
        int screenWidth;
        if (z && this.renderState.getScreenHeight() < this.renderState.getScreenWidth()) {
            screenWidth = this.renderState.getScreenHeight();
        } else {
            screenWidth = this.renderState.getScreenWidth();
        }
        return f * screenWidth;
    }

    public float measureScreenSpaceY(float f, boolean z) {
        int screenHeight;
        if (z && this.renderState.getScreenWidth() < this.renderState.getScreenHeight()) {
            screenHeight = this.renderState.getScreenWidth();
        } else {
            screenHeight = this.renderState.getScreenHeight();
        }
        return f * screenHeight;
    }

    public float measureScreenSpaceAnchorX(int i) {
        float f;
        int screenWidth;
        if (i == 1) {
            return this.renderState.getScreenWidth() * 0.0f;
        }
        if (i == 2) {
            f = 0.5f;
            screenWidth = this.renderState.getScreenWidth();
        } else {
            if (i != 3) {
                return 0.0f;
            }
            f = 1.0f;
            screenWidth = this.renderState.getScreenWidth();
        }
        return screenWidth * f;
    }

    public float measureScreenSpaceAnchorY(int i) {
        float f;
        int screenHeight;
        if (i == 1) {
            return this.renderState.getScreenHeight() * 0.0f;
        }
        if (i == 2) {
            f = 0.5f;
            screenHeight = this.renderState.getScreenHeight();
        } else {
            if (i != 3) {
                return 0.0f;
            }
            f = 1.0f;
            screenHeight = this.renderState.getScreenHeight();
        }
        return screenHeight * f;
    }

    public float measureScreenScaleX(float f, boolean z) {
        int screenWidth;
        if (z && this.renderState.getScreenHeight() < this.renderState.getScreenWidth()) {
            screenWidth = this.renderState.getScreenHeight();
        } else {
            screenWidth = this.renderState.getScreenWidth();
        }
        return f * screenWidth;
    }

    public float measureScreenScaleY(float f, boolean z) {
        int screenHeight;
        if (z && this.renderState.getScreenWidth() < this.renderState.getScreenHeight()) {
            screenHeight = this.renderState.getScreenWidth();
        } else {
            screenHeight = this.renderState.getScreenHeight();
        }
        return f * screenHeight;
    }

    public float measureScreenScale1D(float f) {
        return measureScreenScaleX(f, true);
    }

    public String measureText(String str) {
        String strOnRequestsMeasureText = this.renderState.getRes().getVisualizationData().onRequestsMeasureText(str);
        if (strOnRequestsMeasureText != null) {
            return strOnRequestsMeasureText;
        }
        tlog.w("result null, " + str);
        return str;
    }

    public Vec2f measureVec2f(String str) {
        return measureVec2f(str, new Vec2f(1.0f, 1.0f), null);
    }

    public Vec2f measureVec2f(String str, Vec2f vec2f, Vec2f vec2f2) {
        Vec2f vec2fOnRequestMeasureVec2f = this.renderState.getRes().getVisualizationData().onRequestMeasureVec2f(str, vec2f, vec2f2, this.meterOutputData, this.renderState);
        if (vec2fOnRequestMeasureVec2f != null) {
            return vec2fOnRequestMeasureVec2f;
        }
        tlog.w("result null, " + str);
        return new Vec2f(0.0f, 0.0f);
    }

    public void onFrameStart() {
        this.audioDataProviderWeak.clear();
    }

    public void setFrameDataRmsValue(float f) {
        this.meterOutputData.frameDataRmsValue = f;
    }

    public void addAudioDataProviderToFrame(ISegmentDataProvider iSegmentDataProvider) {
        this.audioDataProviderWeak.add(new WeakReference<>(iSegmentDataProvider));
    }

    public ISegmentDataProvider getAudioDataProvider(int i) {
        if (i < 0 || i >= this.audioDataProviderWeak.size()) {
            return null;
        }
        return this.audioDataProviderWeak.get(i).get();
    }

    public void setFramePeakBarIndex(float f) {
        this.meterOutputData.peakBarIndex = f;
    }
}
