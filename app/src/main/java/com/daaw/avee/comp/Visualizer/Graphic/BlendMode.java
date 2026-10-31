package com.daaw.avee.comp.Visualizer.Graphic;

import com.daaw.avee.Common.tlog;

/* JADX INFO: loaded from: classes.dex */
public class BlendMode {
    public static final int ADD_ALPHA_BLEND = 5;
    public static final int ADD_BLEND = 2;
    public static final int ALPHA_BLEND = 0;
    public static final int DRAW_OVER = 3;
    public static final int PRE_MUL_ALPHA = 4;
    public static final int SCREEN_BLEND = 1;
    public static final String typeName0 = "Alpha";
    public static final String typeName4 = "PreMulAlpha";
    public static final String typeName1 = "Screen";
    public static final String typeName2 = "Add";
    public static final String typeName5 = "AddAlpha";
    public static final String typeName3 = "Over";
    public static final String[] useableModes = {typeName0, typeName4, typeName1, typeName2, typeName5, typeName3};

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:23:0x004a  */
    public static int create(String str, int i) {
        byte b;
        switch (str) {
            case "Screen":
                b = 1;
                break;
            case "AddAlpha":
                b = 5;
                break;
            case "PreMulAlpha":
                b = 4;
                break;
            case "Add":
                b = 2;
                break;
            case "Over":
                b = 3;
                break;
            case "Alpha":
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
        if (b == 4) {
            return 4;
        }
        if (b == 5) {
            return 5;
        }
        tlog.w("unknown typeName: " + str);
        return i;
    }

    public static String getTypeName(int i) {
        if (i == 0) {
            return typeName0;
        }
        if (i == 1) {
            return typeName1;
        }
        if (i == 2) {
            return typeName2;
        }
        if (i == 3) {
            return typeName3;
        }
        if (i == 4) {
            return typeName4;
        }
        if (i == 5) {
            return typeName5;
        }
        tlog.w("unknown instance type");
        return typeName0;
    }
}
