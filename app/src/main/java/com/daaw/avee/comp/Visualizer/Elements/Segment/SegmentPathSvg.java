package com.daaw.avee.comp.Visualizer.Elements.Segment;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.Common.MeasureDefs;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.MeasuredVar;
import com.daaw.avee.comp.Visualizer.Graphic.CharSet;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Path.SVGApproximate;
import com.daaw.avee.comp.Visualizer.Path.SegmentContour;
import com.daaw.avee.vendor.svg.SVGParser;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SegmentPathSvg implements ISegmentPath {
    public static final int SOURCE_ARTIST = 1;
    public static final int SOURCE_AUTO = 0;
    public static final int SOURCE_CUSTOM = 3;
    public static final int SOURCE_TITLE = 2;
    public static final String[] sourceModes = {"Auto", "Artist", "Title", "Custom"};
    boolean svgApproximate;
    String[] svgPaths;
    float totalPathLength;
    float radius = 1.0f;
    private MeasuredVar measureText = new MeasuredVar(MeasureDefs.textArtistOrTitle, 1.0f, 1.0f);
    RectF pathBounds = new RectF();
    Vec2f vec = new Vec2f(0.0f, 0.0f);
    CharSet charSet = CharSet.createAscii48to90();
    CharPathEntry[] charPaths = new CharPathEntry[0];
    int currentCharIndex = 0;
    private int charSource = 0;
    private String customChar = "";

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentPath
    public int getPreferredPointCount(RectF rectF) {
        return 2;
    }

    class CharPathEntry {
        public float length;
        public Vec2f[] points;
        public Vec2f[] tans;
        public List<Integer> indexContourGaps = new ArrayList();
        public List<Integer> indexContourLastIndex = new ArrayList();
        public Vec2f min = new Vec2f(10000.0f, 10000.0f);
        public Vec2f max = new Vec2f(-10000.0f, -10000.0f);

        public CharPathEntry(int i) {
            this.points = new Vec2f[i];
            this.tans = new Vec2f[i];
        }

        public void finishInit() {
            this.indexContourLastIndex = new ArrayList(this.indexContourGaps.size());
            for (int i = 0; i < this.indexContourGaps.size(); i++) {
                this.indexContourLastIndex.add(Integer.valueOf(getLastIndexOfContourGap(this.indexContourGaps.get(i).intValue())));
            }
            Vec2f[] vec2fArr = this.tans;
            if (vec2fArr.length > 0) {
                if (Float.isNaN(vec2fArr[0].x) || this.tans[0].length() < 1.0E-5f) {
                    int lastIndexOfContour = getLastIndexOfContour(0);
                    Vec2f[] vec2fArr2 = this.tans;
                    vec2fArr2[0].x = vec2fArr2[lastIndexOfContour].x;
                    Vec2f[] vec2fArr3 = this.tans;
                    vec2fArr3[0].y = vec2fArr3[lastIndexOfContour].y;
                }
            }
        }

        public float getLength() {
            return this.length;
        }

        boolean isGapToPreviousPoint(int i) {
            return this.indexContourGaps.contains(Integer.valueOf(i));
        }

        int getLastIndexOfContourGap(int i) {
            int length;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.indexContourGaps.size() - 1) {
                    if (this.indexContourGaps.get(i3).intValue() == i) {
                        length = this.indexContourGaps.get(i3 + 1).intValue();
                        break;
                    }
                    i2 = i3 + 1;
                } else {
                    length = this.points.length;
                    break;
                }
            }
            return length - 1;
        }

        int getLastIndexOfContour(int i) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.indexContourGaps.size()) {
                    if (this.indexContourGaps.get(i3).intValue() != i) {
                        i2 = i3 + 1;
                    } else {
                        return this.indexContourLastIndex.get(i3).intValue();
                    }
                } else {
                    return ((this.points.length + i) - 1) % this.points.length;
                }
            }
        }
    }

    public SegmentPathSvg(String[] strArr, boolean z) {
        this.svgPaths = strArr;
        this.svgApproximate = z;
    }

    private void createPaths(CharSet charSet, String[] strArr, int i) {
        if (this.svgApproximate) {
            createPaths_approximate(charSet, strArr, i);
        } else {
            createPaths_pathMeasure(charSet, strArr, i);
        }
    }

    private void createPaths_pathMeasure(CharSet charSet, String[] strArr, int i) {
        if (charSet.count() != strArr.length) {
            tlog.w("charSet count don't match");
        }
        this.charPaths = new CharPathEntry[Math.min(charSet.count(), strArr.length)];
        PathMeasure pathMeasure = new PathMeasure();
        ArrayList arrayList = new ArrayList();
        float[] fArr = {0.0f, 0.0f};
        float[] fArr2 = {0.0f, 0.0f};
        for (int i2 = 0; i2 < this.charPaths.length; i2++) {
            float f = 0.0f;
            this.totalPathLength = 0.0f;
            arrayList.clear();
            Path path = SVGParser.parsePath(strArr[i2]);
            pathMeasure.setPath(path, false);
            do {
                float length = pathMeasure.getLength();
                arrayList.add(Float.valueOf(length));
                this.totalPathLength = length + this.totalPathLength;
            } while (pathMeasure.nextContour());
            CharPathEntry charPathEntry = new CharPathEntry(i);
            pathMeasure.setPath(path, false);
            float f2 = this.totalPathLength / i;
            charPathEntry.indexContourGaps.add(0);
            int i3 = 0;
            while (i3 < i) {
                float f3 = i3 * f2;
                float length2 = pathMeasure.getLength() + f;
                if (f3 > length2) {
                    if (!pathMeasure.nextContour()) {
                        tlog.w("could get all points on path");
                        break;
                    } else {
                        charPathEntry.indexContourGaps.add(Integer.valueOf(i3));
                        f = length2;
                    }
                } else {
                    pathMeasure.getPosTan(f3 - f, fArr, fArr2);
                    charPathEntry.length = this.totalPathLength;
                    charPathEntry.points[i3] = new Vec2f(fArr[0], fArr[1]);
                    charPathEntry.tans[i3] = new Vec2f(fArr2[0], fArr2[1]);
                    charPathEntry.tans[i3].normalize();
                    if (charPathEntry.min.x > charPathEntry.points[i3].x) {
                        charPathEntry.min.x = charPathEntry.points[i3].x;
                    }
                    if (charPathEntry.min.y > charPathEntry.points[i3].y) {
                        charPathEntry.min.y = charPathEntry.points[i3].y;
                    }
                    if (charPathEntry.max.x < charPathEntry.points[i3].x) {
                        charPathEntry.max.x = charPathEntry.points[i3].x;
                    }
                    if (charPathEntry.max.y < charPathEntry.points[i3].y) {
                        charPathEntry.max.y = charPathEntry.points[i3].y;
                    }
                    i3++;
                }
            }
            charPathEntry.finishInit();
            this.charPaths[i2] = charPathEntry;
        }
    }

    private void createPaths_approximate(CharSet charSet, String[] strArr, int i) {
        if (charSet.count() != strArr.length) {
            tlog.w("charSet count don't match");
        }
        this.charPaths = new CharPathEntry[Math.min(charSet.count(), strArr.length)];
        ArrayList arrayList = new ArrayList();
        float[] fArr = {0.0f, 0.0f};
        float[] fArr2 = {0.0f, 0.0f};
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < this.charPaths.length) {
                float f = 0.0f;
                this.totalPathLength = 0.0f;
                arrayList.clear();
                List<SegmentContour> pathApproximate = SVGApproximate.parsePathApproximate(strArr[i4]);
                if (pathApproximate.size() <= 0) {
                    CharPathEntry charPathEntry = new CharPathEntry(i2);
                    charPathEntry.finishInit();
                    this.charPaths[i4] = charPathEntry;
                } else {
                    int i5 = 0;
                    do {
                        int i6 = i5;
                        float length = pathApproximate.get(i6).getLength();
                        arrayList.add(Float.valueOf(length));
                        this.totalPathLength = length + this.totalPathLength;
                        i5 = i6 + 1;
                    } while (i5 < pathApproximate.size());
                    CharPathEntry charPathEntry2 = new CharPathEntry(i);
                    float f2 = this.totalPathLength / i;
                    charPathEntry2.indexContourGaps.add(0);
                    int i7 = 0;
                    int i8 = 0;
                    while (i7 < i) {
                        float f3 = i7 * f2;
                        float length2 = f + pathApproximate.get(i8).getLength();
                        if (f3 > length2) {
                            int i9 = i8 + 1;
                            if (i9 >= pathApproximate.size()) {
                                tlog.w("could get all points on path");
                                break;
                            } else {
                                charPathEntry2.indexContourGaps.add(Integer.valueOf(i7));
                                i8 = i9;
                                f = length2;
                            }
                        } else {
                            pathApproximate.get(i8).getPosTan(f3 - f, fArr, fArr2);
                            charPathEntry2.length = this.totalPathLength;
                            charPathEntry2.points[i7] = new Vec2f(fArr[0], fArr[1]);
                            charPathEntry2.tans[i7] = new Vec2f(fArr2[0], fArr2[1]);
                            charPathEntry2.tans[i7].normalize();
                            if (charPathEntry2.min.x > charPathEntry2.points[i7].x) {
                                charPathEntry2.min.x = charPathEntry2.points[i7].x;
                            }
                            if (charPathEntry2.min.y > charPathEntry2.points[i7].y) {
                                charPathEntry2.min.y = charPathEntry2.points[i7].y;
                            }
                            if (charPathEntry2.max.x < charPathEntry2.points[i7].x) {
                                charPathEntry2.max.x = charPathEntry2.points[i7].x;
                            }
                            if (charPathEntry2.max.y < charPathEntry2.points[i7].y) {
                                charPathEntry2.max.y = charPathEntry2.points[i7].y;
                            }
                            i7++;
                        }
                    }
                    charPathEntry2.finishInit();
                    this.charPaths[i4] = charPathEntry2;
                }
                i3 = i4 + 1;
                i2 = 0;
            } else {
                return;
            }
        }
    }

    public SegmentPathSvg setRadius(float f) {
        this.radius = f;
        return this;
    }

    private char getTargetChar(IRenderState iRenderState) {
        String strMeasureText;
        switch (this.charSource) {
            case 1:
                strMeasureText = iRenderState.getRes().getMeter().measureText(MeasureDefs.textArtist);
                break;
            case 2:
                strMeasureText = iRenderState.getRes().getMeter().measureText(MeasureDefs.textTitle);
                break;
            case 3:
                strMeasureText = this.customChar;
                break;
            default:
                strMeasureText = this.measureText.measureText(iRenderState.getRes().getMeter());
                break;
        }
        if (strMeasureText == null || strMeasureText.length() < 1) {
            return 'X';
        }
        char cCharAt = strMeasureText.charAt(0);
        if (cCharAt >= '0' && cCharAt <= '9') {
            return cCharAt;
        }
        if (cCharAt < 'A' || cCharAt > 'Z') {
            return 'X';
        }
        return cCharAt;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentPath
    public void process(IRenderState iRenderState) {
        this.currentCharIndex = this.charSet.getIndexByChar(getTargetChar(iRenderState));
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentPath
    public int getPointOnPath(boolean z, int i, int i2, RectF rectF, float f, PointF pointF, PointF pointF2) {
        CharPathEntry charPathEntry;
        float fHeight;
        CharPathEntry[] charPathEntryArr = this.charPaths;
        if (charPathEntryArr.length == 0 || charPathEntryArr[0].points.length != i2) {
            createPaths(this.charSet, this.svgPaths, i2);
        }
        int i3 = this.currentCharIndex;
        CharPathEntry[] charPathEntryArr2 = this.charPaths;
        if (i3 < charPathEntryArr2.length) {
            charPathEntry = charPathEntryArr2[i3];
        } else {
            charPathEntry = charPathEntryArr2[0];
        }
        if (charPathEntry == null || charPathEntry.points.length == 0) {
            pointF2.x = 1.0f;
            pointF2.y = 0.0f;
            pointF.x = rectF.centerX();
            pointF.y = rectF.centerY();
            return -1;
        }
        Vec2f vec2f = charPathEntry.points[i];
        Vec2f vec2f2 = charPathEntry.tans[i];
        this.pathBounds.left = charPathEntry.min.x;
        this.pathBounds.top = charPathEntry.min.y;
        this.pathBounds.right = charPathEntry.max.x;
        this.pathBounds.bottom = charPathEntry.max.y;
        this.vec.x = vec2f2.y;
        this.vec.y = -vec2f2.x;
        if (this.pathBounds.width() > this.pathBounds.height()) {
            pointF.x = (vec2f.x - this.pathBounds.centerX()) / this.pathBounds.width();
            pointF.y = (vec2f.y - this.pathBounds.centerY()) / this.pathBounds.width();
        } else {
            pointF.x = (vec2f.x - this.pathBounds.centerX()) / this.pathBounds.height();
            pointF.y = (vec2f.y - this.pathBounds.centerY()) / this.pathBounds.height();
        }
        if (rectF.width() < rectF.height()) {
            fHeight = rectF.width();
        } else {
            fHeight = rectF.height();
        }
        float f2 = fHeight * 0.5f * this.radius;
        pointF.x = rectF.centerX() + (pointF.x * f2);
        pointF.y = (f2 * pointF.y) + rectF.centerY();
        pointF2.x = -this.vec.x;
        pointF2.y = -this.vec.y;
        return charPathEntry.getLastIndexOfContour(i);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentPath
    public float getPathLength(RectF rectF, int i) {
        float fHeight;
        float fHeight2;
        CharPathEntry[] charPathEntryArr = this.charPaths;
        if (charPathEntryArr.length == 0 || charPathEntryArr[0].points.length != i) {
            createPaths(this.charSet, this.svgPaths, i);
        }
        int i2 = this.currentCharIndex;
        CharPathEntry[] charPathEntryArr2 = this.charPaths;
        CharPathEntry charPathEntry = i2 < charPathEntryArr2.length ? charPathEntryArr2[i2] : null;
        if (charPathEntry == null) {
            return 1.0f;
        }
        this.pathBounds.left = charPathEntry.min.x;
        this.pathBounds.top = charPathEntry.min.y;
        this.pathBounds.right = charPathEntry.max.x;
        this.pathBounds.bottom = charPathEntry.max.y;
        if (rectF.width() < rectF.height()) {
            fHeight = rectF.width();
        } else {
            fHeight = rectF.height();
        }
        if (this.pathBounds.width() > this.pathBounds.height()) {
            fHeight2 = this.pathBounds.width();
        } else {
            fHeight2 = this.pathBounds.height();
        }
        return fHeight * (charPathEntry.getLength() / fHeight2) * 0.5f * this.radius;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        this.radius = customPropertiesList.getPropertyFloat("radius", this.radius);
        this.charSource = customPropertiesList.getPropertySelectStringIndex("charSource", sourceModes, 0);
        String propertyString = customPropertiesList.getPropertyString("customChar", "");
        if (propertyString != null && propertyString.length() > 0) {
            char upperCase = Character.toUpperCase(propertyString.charAt(0));
            if ((upperCase >= '0' && upperCase <= '9') || (upperCase >= 'A' && upperCase <= 'Z')) {
                this.customChar = String.valueOf(upperCase);
                return;
            } else {
                this.customChar = "";
                return;
            }
        }
        this.customChar = "";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.ICustomizable
    public void onReadCustomization(CustomPropertiesList customPropertiesList) {
        customPropertiesList.putPropertyFloatAsRange("radius", this.radius, "misc", 0.1f, 3.0f);
        customPropertiesList.putSelectString("charSource", sourceModes[this.charSource], "misc", sourceModes);
        customPropertiesList.putPropertyStringAsTxt("customChar", (this.customChar == null || this.customChar.length() <= 0) ? "" : this.customChar, "misc");
    }
}
