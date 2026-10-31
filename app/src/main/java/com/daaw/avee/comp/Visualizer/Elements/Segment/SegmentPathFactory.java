package com.daaw.avee.comp.Visualizer.Elements.Segment;

import com.daaw.avee.Common.tlog;

/* JADX INFO: loaded from: classes.dex */
public class SegmentPathFactory {
    public static final String typeNameNone = "None";
    public static final String[] typeNames = {"HorizontalLine", SegmentPathCircle.typeName, SegmentPathSided.typeName, "Letter"};
    public static final String[] typeNamesCelebration = {"HorizontalLine", SegmentPathCircle.typeName, SegmentPathSided.typeName, "Letter", SegmentPathYear.typeName};
    public static final String[] typeNamesClosedAndNone = {"None", SegmentPathCircle.typeName, SegmentPathSided.typeName};

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static ISegmentPath create(String str, ISegmentPath iSegmentPath) {
        if (getTypeName(iSegmentPath).equals(str)) {
            return iSegmentPath;
        }
        byte b = -1;
        switch (str.hashCode()) {
            case -2022305722:
                if (str.equals("Letter")) {
                    b = 4;
                }
                break;
            case 2433880:
                if (str.equals("None")) {
                    b = 0;
                }
                break;
            case 2751581:
                if (str.equals(SegmentPathYear.typeName)) {
                    b = 5;
                }
                break;
            case 1536319149:
                if (str.equals(SegmentPathSided.typeName)) {
                    b = 3;
                }
                break;
            case 1802881624:
                if (str.equals("HorizontalLine")) {
                    b = 1;
                }
                break;
            case 2018617584:
                if (str.equals(SegmentPathCircle.typeName)) {
                    b = 2;
                }
                break;
        }
        if (b == 0) {
            return null;
        }
        if (b == 1) {
            return new SegmentPathHorizontalLine();
        }
        if (b == 2) {
            return new SegmentPathCircle();
        }
        if (b == 3) {
            return new SegmentPathSided();
        }
        if (b == 4) {
            return new SegmentPathLetter();
        }
        if (b == 5) {
            return new SegmentPathYear();
        }
        tlog.w("unknown typeName: " + str);
        return iSegmentPath;
    }

    public static String getTypeName(ISegmentPath iSegmentPath) {
        if (iSegmentPath == null) {
            return "None";
        }
        if (iSegmentPath instanceof SegmentPathHorizontalLine) {
            return "HorizontalLine";
        }
        if (iSegmentPath instanceof SegmentPathCircle) {
            return SegmentPathCircle.typeName;
        }
        if (iSegmentPath instanceof SegmentPathSided) {
            return SegmentPathSided.typeName;
        }
        if (iSegmentPath instanceof SegmentPathLetter) {
            return "Letter";
        }
        if (iSegmentPath instanceof SegmentPathYear) {
            return SegmentPathYear.typeName;
        }
        tlog.w("unknown instance type");
        return "unk";
    }
}
