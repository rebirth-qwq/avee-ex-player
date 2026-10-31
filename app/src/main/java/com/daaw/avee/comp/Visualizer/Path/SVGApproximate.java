package com.daaw.avee.comp.Visualizer.Path;

import android.util.Log;
import com.daaw.avee.Common.Vec4f;
import com.daaw.avee.vendor.svg.ParserHelper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SVGApproximate {
    static final String TAG = "SVGApproximate";

    public interface IPath {
        void close();

        void cubicTo(float f, float f2, float f3, float f4, float f5, float f6);

        void lineTo(float f, float f2);

        void moveTo(float f, float f2);

        void rLineTo(float f, float f2);

        void rMoveTo(float f, float f2);
    }

    private static void drawArc(IPath iPath, float f, float f2, float f3, float f4, float f5, float f6, float f7, int i, int i2) {
    }

    public static class PathCreator implements IPath {
        List<SegmentContour> segments = new ArrayList();
        List<Vec4f> currentSegment = new ArrayList();
        boolean posset = false;
        float cx = 0.0f;
        float cy = 0.0f;

        @Override // com.daaw.avee.comp.Visualizer.Path.SVGApproximate.IPath
        public void cubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
        }

        public List<SegmentContour> getSegments() {
            return this.segments;
        }

        @Override // com.daaw.avee.comp.Visualizer.Path.SVGApproximate.IPath
        public void rMoveTo(float f, float f2) {
            this.cx += f;
            this.cy += f2;
            this.posset = true;
        }

        @Override // com.daaw.avee.comp.Visualizer.Path.SVGApproximate.IPath
        public void moveTo(float f, float f2) {
            this.cx = f;
            this.cy = f2;
            this.posset = true;
        }

        @Override // com.daaw.avee.comp.Visualizer.Path.SVGApproximate.IPath
        public void close() {
            if (this.currentSegment.size() > 0) {
                this.segments.add(new SegmentContour(this.currentSegment));
            }
            this.currentSegment.clear();
            this.posset = false;
        }

        @Override // com.daaw.avee.comp.Visualizer.Path.SVGApproximate.IPath
        public void rLineTo(float f, float f2) {
            if (!this.posset) {
                this.cx = 0.0f;
                this.cy = 0.0f;
                this.posset = true;
            }
            List<Vec4f> list = this.currentSegment;
            float f3 = this.cx;
            float f4 = this.cy;
            list.add(new Vec4f(f3, f4, f3 + f, f4 + f2));
            this.cx += f;
            this.cy += f2;
        }

        @Override // com.daaw.avee.comp.Visualizer.Path.SVGApproximate.IPath
        public void lineTo(float f, float f2) {
            if (!this.posset) {
                this.cx = 0.0f;
                this.cy = 0.0f;
                this.posset = true;
            }
            this.currentSegment.add(new Vec4f(this.cx, this.cy, f, f2));
            this.cx = f;
            this.cy = f2;
        }
    }

    public static List<SegmentContour> parsePathApproximate(String str) {
        PathCreator pathCreator = new PathCreator();
        doPath(str, pathCreator);
        return pathCreator.getSegments();
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0033  */
    /* JADX WARN: Code duplicated, block: B:18:0x0047 A[FALL_THROUGH] */
    /* JADX WARN: Failed to find 'out' block for switch in B:21:0x0054. Please report as an issue. */
    private static IPath doPath(String str, IPath iPath) {
        char c = 0;
        float fNextFloat;
        float fNextFloat2;
        boolean z;
        int length = str.length();
        ParserHelper parserHelper = new ParserHelper(str, 0);
        parserHelper.skipWhitespace();
        char c2 = 0;
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        while (parserHelper.pos < length) {
            char cCharAt = str.charAt(parserHelper.pos);
            if (cCharAt != '+' && cCharAt != '-') {
                switch (cCharAt) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        if (c2 != 'm') {
                            break;
                        }
                        c = c2;
                        c2 = (char) (c2 - 1);
                    default:
                        parserHelper.advance();
                        c2 = cCharAt;
                        c = c2;
                        break;
                }
            } else if (c2 != 'm' || c2 == 'M') {
                c = c2;
                c2 = (char) (c2 - 1);
            } else {
                if ("lhvcsqta".indexOf(Character.toLowerCase(c2)) < 0) {
                    parserHelper.advance();
                    c2 = cCharAt;
                }
                c = c2;
            }
            switch (c2) {
                case 'A':
                case 'a':
                    float fNextFloat3 = parserHelper.nextFloat();
                    float fNextFloat4 = parserHelper.nextFloat();
                    float fNextFloat5 = parserHelper.nextFloat();
                    int iNextFlag = parserHelper.nextFlag();
                    int iNextFlag2 = parserHelper.nextFlag();
                    float fNextFloat6 = parserHelper.nextFloat();
                    float fNextFloat7 = parserHelper.nextFloat();
                    if (c2 == 'a') {
                        fNextFloat6 += f;
                        fNextFloat7 += f2;
                    }
                    float f7 = fNextFloat6;
                    float f8 = fNextFloat7;
                    drawArc(iPath, f, f2, f7, f8, fNextFloat3, fNextFloat4, fNextFloat5, iNextFlag, iNextFlag2);
                    f = f7;
                    f2 = f8;
                    z = false;
                    break;
                case 'C':
                case 'c':
                    float fNextFloat8 = parserHelper.nextFloat();
                    float fNextFloat9 = parserHelper.nextFloat();
                    float fNextFloat10 = parserHelper.nextFloat();
                    float fNextFloat11 = parserHelper.nextFloat();
                    float fNextFloat12 = parserHelper.nextFloat();
                    float fNextFloat13 = parserHelper.nextFloat();
                    if (c2 == 'c') {
                        fNextFloat8 += f;
                        fNextFloat10 += f;
                        fNextFloat12 += f;
                        fNextFloat9 += f2;
                        fNextFloat11 += f2;
                        fNextFloat13 += f2;
                    }
                    float f9 = fNextFloat10;
                    float f10 = fNextFloat11;
                    iPath.cubicTo(fNextFloat8, fNextFloat9, f9, f10, fNextFloat12, fNextFloat13);
                    f = fNextFloat12;
                    f2 = fNextFloat13;
                    f6 = f10;
                    f5 = f9;
                    z = true;
                    break;
                case 'H':
                case 'h':
                    float fNextFloat14 = parserHelper.nextFloat();
                    if (c2 == 'h') {
                        iPath.rLineTo(fNextFloat14, 0.0f);
                        f += fNextFloat14;
                    } else {
                        iPath.lineTo(fNextFloat14, f2);
                        f = fNextFloat14;
                    }
                    z = false;
                    break;
                case 'L':
                case 'T':
                case 'l':
                case 't':
                    fNextFloat = parserHelper.nextFloat();
                    fNextFloat2 = parserHelper.nextFloat();
                    if (c2 == 'l') {
                        iPath.rLineTo(fNextFloat, fNextFloat2);
                        f += fNextFloat;
                        f2 += fNextFloat2;
                    } else {
                        iPath.lineTo(fNextFloat, fNextFloat2);
                        f = fNextFloat;
                        f2 = fNextFloat2;
                    }
                    z = false;
                    break;
                case 'M':
                case 'm':
                    fNextFloat = parserHelper.nextFloat();
                    fNextFloat2 = parserHelper.nextFloat();
                    if (c2 == 'm') {
                        f4 += fNextFloat;
                        f3 += fNextFloat2;
                        iPath.rMoveTo(fNextFloat, fNextFloat2);
                        f += fNextFloat;
                        f2 += fNextFloat2;
                    } else {
                        iPath.moveTo(fNextFloat, fNextFloat2);
                        f = fNextFloat;
                        f4 = f;
                        f2 = fNextFloat2;
                        f3 = f2;
                    }
                    z = false;
                    break;
                case 'Q':
                case 'S':
                case 'q':
                case 's':
                    float fNextFloat15 = parserHelper.nextFloat();
                    float fNextFloat16 = parserHelper.nextFloat();
                    float fNextFloat17 = parserHelper.nextFloat();
                    float fNextFloat18 = parserHelper.nextFloat();
                    if (Character.isLowerCase(c2)) {
                        fNextFloat15 += f;
                        fNextFloat17 += f;
                        fNextFloat16 += f2;
                        fNextFloat18 += f2;
                    }
                    float f11 = fNextFloat15;
                    float f12 = fNextFloat16;
                    float f13 = fNextFloat17;
                    float f14 = fNextFloat18;
                    iPath.cubicTo((f * 2.0f) - f5, (f2 * 2.0f) - f6, f11, f12, f13, f14);
                    f5 = f11;
                    f6 = f12;
                    f = f13;
                    f2 = f14;
                    z = true;
                    break;
                case 'V':
                case 'v':
                    float fNextFloat19 = parserHelper.nextFloat();
                    if (c2 == 'v') {
                        iPath.rLineTo(0.0f, fNextFloat19);
                        f2 += fNextFloat19;
                    } else {
                        iPath.lineTo(f, fNextFloat19);
                        f2 = fNextFloat19;
                    }
                    z = false;
                    break;
                case 'Z':
                case 'z':
                    iPath.close();
                    iPath.moveTo(f4, f3);
                    f2 = f3;
                    f6 = f2;
                    f = f4;
                    f5 = f;
                    z = true;
                    break;
                default:
                    Log.w(TAG, "Invalid path command: " + c2);
                    parserHelper.advance();
                    z = false;
                    break;
            }
            if (!z) {
                f5 = f;
                f6 = f2;
            }
            parserHelper.skipWhitespace();
            c2 = c;
        }
        return iPath;
    }
}
