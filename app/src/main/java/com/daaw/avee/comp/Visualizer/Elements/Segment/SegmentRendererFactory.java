package com.daaw.avee.comp.Visualizer.Elements.Segment;

import com.daaw.avee.Common.tlog;

/* JADX INFO: loaded from: classes.dex */
public class SegmentRendererFactory {
    public static final String typeNameNone = "None";
    public static final String[] typeNames = {"None", "Bars", "Line", SegmentRendererSharpBar.typeName, SegmentRendererRoundBar.typeName};

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static ISegmentRenderer create(String str, ISegmentRenderer iSegmentRenderer) {
        if (getTypeName(iSegmentRenderer).equals(str)) {
            return iSegmentRenderer;
        }
        byte b = -1;
        switch (str.hashCode()) {
            case 2063072:
                if (str.equals("Bars")) {
                    b = 1;
                }
                break;
            case 2368532:
                if (str.equals("Line")) {
                    b = 2;
                }
                break;
            case 2433880:
                if (str.equals("None")) {
                    b = 0;
                }
                break;
            case 431547818:
                if (str.equals(SegmentRendererSharpBar.typeName)) {
                    b = 3;
                }
                break;
            case 1998378318:
                if (str.equals(SegmentRendererRoundBar.typeName)) {
                    b = 4;
                }
                break;
        }
        if (b == 0) {
            return null;
        }
        if (b == 1) {
            return new SegmentRendererBar();
        }
        if (b == 2) {
            return new SegmentRendererLine();
        }
        if (b == 3) {
            return new SegmentRendererSharpBar();
        }
        if (b == 4) {
            return new SegmentRendererRoundBar();
        }
        tlog.w("unknown typeName: " + str);
        return iSegmentRenderer;
    }

    public static String getTypeName(ISegmentRenderer iSegmentRenderer) {
        if (iSegmentRenderer == null) {
            return "None";
        }
        if (iSegmentRenderer instanceof SegmentRendererBar) {
            return "Bars";
        }
        if (iSegmentRenderer instanceof SegmentRendererLine) {
            return "Line";
        }
        if (iSegmentRenderer instanceof SegmentRendererSharpBar) {
            return SegmentRendererSharpBar.typeName;
        }
        if (iSegmentRenderer instanceof SegmentRendererRoundBar) {
            return SegmentRendererRoundBar.typeName;
        }
        tlog.w("unknown instance type");
        return "unk";
    }
}
