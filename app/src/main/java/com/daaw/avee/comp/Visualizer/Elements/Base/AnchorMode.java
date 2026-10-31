package com.daaw.avee.comp.Visualizer.Elements.Base;

import com.daaw.avee.Common.tlog;

/* JADX INFO: loaded from: classes.dex */
public class AnchorMode {
    public static final int Anchor_Center = 2;
    public static final int Anchor_End = 3;
    public static final int Anchor_Na = 0;
    public static final int Anchor_Start = 1;
    public static final String typeName1 = "start";
    public static final String typeName2 = "center";
    public static final String typeName3 = "end";
    public static final String typeName0 = "unset";
    public static final String[] modes = {typeName0, "start", "center", "end"};

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:17:0x0034  */
    public static int create(String str, int i) {
        byte b;
        switch (str) {
            case "center":
                b = 2;
                break;
            case "end":
                b = 3;
                break;
            case "start":
                b = 1;
                break;
            case "unset":
                b = 0;
                break;
            default:
                b = -1;
                break;
        }
        if (b == 0) {
            return 0;
        }
        if (b == 1) {
            return 1;
        }
        if (b == 2) {
            return 2;
        }
        if (b == 3) {
            return 3;
        }
        tlog.w("unknown typeName: " + str);
        return i;
    }

    public static String getTypeName(int i, int i2) {
        if (i == 0) {
            return typeName0;
        }
        if (i == 1) {
            return "start";
        }
        if (i == 2) {
            return "center";
        }
        if (i == 3) {
            return "end";
        }
        if (i2 == 0) {
            return typeName0;
        }
        if (i2 == 1) {
            return "start";
        }
        if (i2 == 2) {
            return "center";
        }
        if (i2 == 3) {
            return "end";
        }
        tlog.w("unknown instance type");
        return typeName0;
    }
}
