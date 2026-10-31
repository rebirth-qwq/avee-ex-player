package com.daaw.avee.comp.Visualizer.Elements.Particles;

import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.Visualizer.Elements.Base.IArea;
import com.daaw.avee.comp.Visualizer.Elements.Particles.Remove.AreaHorizontalLineRandDir;
import com.daaw.avee.comp.Visualizer.Elements.Particles.Remove.AreaRect3d;
import com.daaw.avee.comp.Visualizer.Elements.Particles.Remove.AreaVerticalLine;

/* JADX INFO: loaded from: classes.dex */
public class AreaFactory {
    public static final String typeNameNone = "None";
    public static final String[] typeNames = {"HorizontalLine", AreaRect.typeName};
    public static final String[] typeNamesAndNone = {"None", "HorizontalLine", AreaRect.typeName};

    public static IArea create(String str, IArea iArea) {
        if (getTypeName(iArea).equals(str)) {
            return iArea;
        }
        switch (str) {
            case "None":
                return null;
            case "HorizontalLine":
                return new AreaHorizontalLine();
            case "HorizontalLineRandom":
                return new AreaHorizontalLineRandDir();
            case "Line":
                return new AreaVerticalLine();
            case "VerticalLine":
                return new AreaVerticalLine();
            case "Rectangle":
                return new AreaRect();
            case "Rectangle3D":
                return new AreaRect3d();
            default:
                tlog.w("unknown typeName: " + str);
                return iArea;
        }
    }

    public static String getTypeName(IArea iArea) {
        if (iArea == null) {
            return "None";
        }
        if (iArea instanceof AreaHorizontalLine) {
            return "HorizontalLine";
        }
        if (iArea instanceof AreaHorizontalLineRandDir) {
            return AreaHorizontalLineRandDir.typeName;
        }
        if (iArea instanceof AreaVerticalLine) {
            return "Line";
        }
        if (iArea instanceof AreaRect) {
            return AreaRect.typeName;
        }
        if (iArea instanceof AreaRect3d) {
            return AreaRect3d.typeName;
        }
        tlog.w("unknown instance type");
        return "unk";
    }
}
