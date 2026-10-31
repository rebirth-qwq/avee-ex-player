package com.daaw.avee.comp.Visualizer.Elements.Particles;

import com.daaw.avee.Common.tlog;

/* JADX INFO: loaded from: classes.dex */
public class ForceFieldFactory {
    public static final String typeNameNone = "None";
    public static final String[] typeNamesAndNone = {"None", VortexForceField.typeName};

    public static IForceField create(String str, IForceField iForceField) {
        if (getTypeName(iForceField).equals(str)) {
            return iForceField;
        }
        byte b = -1;
        int iHashCode = str.hashCode();
        if (iHashCode != -1726838578) {
            if (iHashCode == 2433880 && str.equals("None")) {
                b = 0;
            }
        } else if (str.equals(VortexForceField.typeName)) {
            b = 1;
        }
        if (b == 0) {
            return null;
        }
        if (b == 1) {
            return new VortexForceField();
        }
        tlog.w("unknown typeName: " + str);
        return iForceField;
    }

    public static String getTypeName(IForceField iForceField) {
        if (iForceField == null) {
            return "None";
        }
        if (iForceField instanceof VortexForceField) {
            return VortexForceField.typeName;
        }
        tlog.w("unknown instance type");
        return "unk";
    }
}
