package com.daaw.avee.comp.Visualizer.Elements.Segment;

import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;

/* JADX INFO: loaded from: classes.dex */
public class SegmentRendererRoundBar extends SegmentRendererBase {
    public static final String typeName = "RoundBars";
    private ISegmentRenderer.DrawBatchDesc lastDesc;
    private ISegmentRenderer.DrawBatchDesc nextDesc;
    private static Vec2f texCoordX0Y03 = new Vec2f(0.0f, 0.5f);
    private static Vec2f texCoordX1Y03 = new Vec2f(1.0f, 0.5f);
    private static Vec2f texCoordX0Y06 = new Vec2f(0.0f, 0.5f);
    private static Vec2f texCoordX1Y06 = new Vec2f(1.0f, 0.5f);
    private float barWidth = 0.5f;
    private boolean mirror = false;
    protected boolean barWidthAffectedByNormal = false;

    public SegmentRendererRoundBar setBarWidth(float f) {
        this.barWidth = f;
        return this;
    }

    public SegmentRendererRoundBar setMirror(boolean z) {
        this.mirror = z;
        return this;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentRenderer
    public void drawSegment(RenderState renderState, ISegmentRenderer.DrawDesc drawDesc, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        drawDesc.color1 = getBarColorBase(drawDesc.valueIndex, drawDesc.valuesCount, f);
        float fRound = Math.round((drawDesc.drawSegmentWidth * 0.5f) / (drawDesc.valuesCount + 1)) * this.barWidth;
        float f6 = drawDesc.drawPointX;
        float f7 = drawDesc.drawPointY;
        float f8 = drawDesc.segmentHeightVal * (-2.0f) * drawDesc.drawScale;
        float fSignum = Math.signum(f8);
        float f9 = f8 * this.barHeightMultiplier;
        if (this.mirror) {
            f6 -= drawDesc.drawVecX * f9;
            f7 -= drawDesc.drawVecY * f9;
            double d = f9;
            Double.isNaN(d);
            f9 = (float) (d * 2.0d);
        }
        float fCcw90X = (Vec2f.ccw90X(drawDesc.drawVecX, drawDesc.drawVecY) * fRound) + f6;
        float fCcw90Y = (Vec2f.ccw90Y(drawDesc.drawVecX, drawDesc.drawVecY) * fRound) + f7;
        float fCw90X = (Vec2f.cw90X(drawDesc.drawVecX, drawDesc.drawVecY) * fRound) + f6;
        float fCw90Y = (Vec2f.cw90Y(drawDesc.drawVecX, drawDesc.drawVecY) * fRound) + f7;
        float f10 = (drawDesc.drawVecX * f9) + fCcw90X;
        float f11 = (drawDesc.drawVecY * f9) + fCcw90Y;
        float f12 = (drawDesc.drawVecX * f9) + fCw90X;
        float f13 = (drawDesc.drawVecY * f9) + fCw90Y;
        if (this.useFixedLineHeight) {
            float f14 = f10 + (drawDesc.drawVecX * fSignum * this.fixedLineHeight);
            f3 = f14;
            f4 = f11 + (drawDesc.drawVecY * fSignum * this.fixedLineHeight);
            f5 = f12 + (drawDesc.drawVecX * fSignum * this.fixedLineHeight);
            f2 = f13 + (drawDesc.drawVecY * fSignum * this.fixedLineHeight);
        } else {
            f2 = fCw90Y;
            f3 = fCcw90X;
            f4 = fCcw90Y;
            f5 = fCw90X;
        }
        float f15 = -fRound;
        float f16 = (drawDesc.drawVecX * 1.0f * f15) + f10;
        float f17 = (drawDesc.drawVecY * 1.0f * f15) + f11;
        float f18 = (drawDesc.drawVecX * 1.0f * f15) + f12;
        float f19 = (drawDesc.drawVecY * 1.0f * f15) + f13;
        float f20 = (drawDesc.drawVecX * 1.0f * fRound) + f3;
        float f21 = (drawDesc.drawVecY * 1.0f * fRound) + f4;
        float f22 = (drawDesc.drawVecX * 1.0f * fRound) + f5;
        float f23 = (drawDesc.drawVecY * 1.0f * fRound) + f2;
        if (Math.abs(fRound) > 0.01f) {
            renderState.res.getBufferRenderer().drawRectangle(renderState, f16, f17, f18, f19, f10, f11, f12, f13, 0.0f, drawDesc.color1, Vec2f.zero(), texCoordX1Y03, renderState.res.getAtlasTexRoundSegment(), drawDesc.blendMode);
            renderState.res.getBufferRenderer().drawRectangle(renderState, f10, f11, f12, f13, f3, f4, f5, f2, 0.0f, drawDesc.color1, texCoordX0Y03, texCoordX1Y06, renderState.res.getAtlasTexRoundSegment(), drawDesc.blendMode);
            renderState.res.getBufferRenderer().drawRectangle(renderState, f3, f4, f5, f2, f20, f21, f22, f23, 0.0f, drawDesc.color1, texCoordX0Y06, Vec2f.one(), renderState.res.getAtlasTexRoundSegment(), drawDesc.blendMode);
            return;
        }
        renderState.res.getBufferRenderer().drawRectangle(renderState, f10, f11, f12, f13, f3, f4, f5, f2, 0.0f, drawDesc.color1, Vec2f.zero(), Vec2f.one(), renderState.res.getAtlasTexWhite(), drawDesc.blendMode);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentRenderer
    public void drawSegmentBatch(IRenderState iRenderState, ISegmentRenderer.DrawBatchDesc[] drawBatchDescArr, float f, float f2, int i, float f3) {
        float fCcw90X;
        float fCcw90Y;
        float fCw90Y;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        ISegmentRenderer.DrawBatchDesc[] drawBatchDescArr2 = drawBatchDescArr;
        int length = drawBatchDescArr2.length;
        float fRound = Math.round((f * 0.5f) / (length + 1)) * this.barWidth;
        Vec2f vec2f = new Vec2f(0.0f, 0.0f);
        Vec2f vec2f2 = new Vec2f(0.0f, 0.0f);
        int i2 = 0;
        while (i2 < drawBatchDescArr2.length) {
            ISegmentRenderer.DrawBatchDesc drawBatchDesc = drawBatchDescArr2[i2];
            this.lastDesc = drawBatchDesc.valueIndexLastToConnect < 0 ? drawBatchDesc : drawBatchDescArr2[drawBatchDesc.valueIndexLastToConnect];
            this.nextDesc = drawBatchDesc.valueIndexNextToConnect < 0 ? drawBatchDesc : drawBatchDescArr2[drawBatchDesc.valueIndexNextToConnect];
            int barColorBase = getBarColorBase(i2, length, f3);
            float f16 = drawBatchDesc.drawPointX;
            float f17 = drawBatchDesc.drawPointY;
            float f18 = drawBatchDesc.segmentHeightVal * (-2.0f) * f2;
            float fSignum = Math.signum(f18);
            float f19 = f18 * this.barHeightMultiplier;
            if (this.mirror) {
                f16 -= drawBatchDesc.drawVecX * f19;
                f17 -= drawBatchDesc.drawVecY * f19;
                double d = f19;
                Double.isNaN(d);
                f19 = (float) (d * 2.0d);
            }
            if (this.barWidthAffectedByNormal) {
                vec2f.x = drawBatchDesc.drawVecX + this.lastDesc.drawVecX;
                vec2f.y = drawBatchDesc.drawVecY + this.lastDesc.drawVecY;
                vec2f2.x = drawBatchDesc.drawVecX + this.nextDesc.drawVecX;
                vec2f2.y = drawBatchDesc.drawVecY + this.nextDesc.drawVecY;
                vec2f2.normalizeSafe();
                vec2f.normalizeSafe();
                fCcw90X = (Vec2f.ccw90X(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f16;
                float fCcw90Y2 = (Vec2f.ccw90Y(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f17;
                float fCw90X = (Vec2f.cw90X(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f16;
                fCw90Y = (Vec2f.cw90Y(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f17;
                float f20 = (vec2f.x * f19) + fCcw90X;
                float f21 = (vec2f.y * f19) + fCcw90Y2;
                float f22 = (vec2f2.x * f19) + fCw90X;
                float f23 = (vec2f2.y * f19) + fCw90Y;
                float f24 = -(Vec2f.length(f20 - f22, f21 - f23) * 0.5f);
                f6 = (vec2f.x * f24) + f20;
                f11 = (vec2f.y * f24) + f21;
                float f25 = (vec2f2.x * f24) + f22;
                float f26 = (vec2f2.y * f24) + f23;
                f10 = f23;
                fCcw90Y = fCcw90Y2;
                f9 = f22;
                f12 = fCw90X;
                f7 = f25;
                f5 = f21;
                f8 = f26;
                f4 = f20;
            } else {
                fCcw90X = (Vec2f.ccw90X(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f16;
                fCcw90Y = (Vec2f.ccw90Y(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f17;
                float fCw90X2 = (Vec2f.cw90X(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f16;
                fCw90Y = (Vec2f.cw90Y(drawBatchDesc.drawVecX, drawBatchDesc.drawVecY) * fRound) + f17;
                f4 = (drawBatchDesc.drawVecX * f19) + fCcw90X;
                f5 = (drawBatchDesc.drawVecY * f19) + fCcw90Y;
                float f27 = (drawBatchDesc.drawVecX * f19) + fCw90X2;
                float f28 = (drawBatchDesc.drawVecY * f19) + fCw90Y;
                float f29 = -fRound;
                f6 = (drawBatchDesc.drawVecX * f29) + f4;
                float f30 = (drawBatchDesc.drawVecY * f29) + f5;
                f7 = (drawBatchDesc.drawVecX * f29) + f27;
                f8 = (drawBatchDesc.drawVecY * f29) + f28;
                f9 = f27;
                f10 = f28;
                f11 = f30;
                f12 = fCw90X2;
            }
            if (this.useFixedLineHeight) {
                f15 = (drawBatchDesc.drawVecX * fSignum * this.fixedLineHeight) + f4;
                fCcw90Y = (drawBatchDesc.drawVecY * fSignum * this.fixedLineHeight) + f5;
                f13 = f9 + (drawBatchDesc.drawVecX * fSignum * this.fixedLineHeight);
                f14 = f10 + (drawBatchDesc.drawVecY * fSignum * this.fixedLineHeight);
            } else {
                f13 = f12;
                f14 = fCw90Y;
                f15 = fCcw90X;
            }
            float f31 = (drawBatchDesc.drawVecX * fRound) + f15;
            float f32 = (drawBatchDesc.drawVecY * fRound) + fCcw90Y;
            float f33 = (drawBatchDesc.drawVecX * fRound) + f13;
            float f34 = (drawBatchDesc.drawVecY * fRound) + f14;
            if (Math.abs(fRound) > 0.01f) {
                iRenderState.getBufferRenderer().drawRectangle(iRenderState, f6, f11, f7, f8, f4, f5, f9, f10, 0.0f, barColorBase, Vec2f.zero(), texCoordX1Y03, iRenderState.getRes().getAtlasTexRoundSegment(), i);
                iRenderState.getBufferRenderer().drawRectangle(iRenderState, f4, f5, f9, f10, f15, fCcw90Y, f13, f14, 0.0f, barColorBase, texCoordX0Y03, texCoordX1Y06, iRenderState.getRes().getAtlasTexRoundSegment(), i);
                iRenderState.getBufferRenderer().drawRectangle(iRenderState, f15, fCcw90Y, f13, f14, f31, f32, f33, f34, 0.0f, barColorBase, texCoordX0Y06, Vec2f.one(), iRenderState.getRes().getAtlasTexRoundSegment(), i);
            } else {
                iRenderState.getBufferRenderer().drawRectangle(iRenderState, f4, f5, f9, f10, f15, fCcw90Y, f13, f14, 0.0f, barColorBase, Vec2f.zero(), Vec2f.one(), iRenderState.getRes().getAtlasTexWhite(), i);
            }
            i2++;
            drawBatchDescArr2 = drawBatchDescArr;
            vec2f = vec2f;
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentRendererBase, com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
        this.barWidth = customPropertiesList.getPropertyFloat("barWidth", 0.5f);
        this.barWidthAffectedByNormal = customPropertiesList.getPropertyBool("barWidthAffectedByShape", false);
        this.mirror = customPropertiesList.getPropertyBool("mirror", false);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentRendererBase, com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onReadCustomization(CustomPropertiesList customPropertiesList) {
        super.onReadCustomization(customPropertiesList);
        customPropertiesList.putPropertyFloatAsRange("barWidth", this.barWidth, "misc", 0.0f, 2.0f);
        customPropertiesList.putPropertyBool("barWidthAffectedByShape", this.barWidthAffectedByNormal, "b");
        customPropertiesList.putPropertyBool("mirror", this.mirror, "misc");
    }
}
