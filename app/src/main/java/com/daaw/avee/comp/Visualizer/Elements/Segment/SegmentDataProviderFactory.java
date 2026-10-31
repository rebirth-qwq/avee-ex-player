package com.daaw.avee.comp.Visualizer.Elements.Segment;

import com.daaw.avee.Common.tlog;

/* JADX INFO: loaded from: classes.dex */
public class SegmentDataProviderFactory {
    public static final String typeNameNone = "None";
    public static final String[] typeNames = {SegmentAudioWaveformData.typeName, SegmentAudioSpectrumData.typeName, SegmentAudioSpectrumDataPhaseS.typeName};

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static ISegmentDataProvider create(String str, ISegmentDataProvider iSegmentDataProvider) {
        if (getTypeName(iSegmentDataProvider, "None").equals(str)) {
            return iSegmentDataProvider;
        }
        byte b = -1;
        switch (str.hashCode()) {
            case -2067891215:
                if (str.equals(SegmentAudioSpectrumData.typeName)) {
                    b = 2;
                }
                break;
            case 2433880:
                if (str.equals("None")) {
                    b = 0;
                }
                break;
            case 319881825:
                if (str.equals(SegmentAudioSpectrumDataPhaseS.typeName)) {
                    b = 3;
                }
                break;
            case 668852061:
                if (str.equals(SegmentAudioWaveformData.typeName)) {
                    b = 1;
                }
                break;
        }
        if (b == 0) {
            return null;
        }
        if (b == 1) {
            return new SegmentAudioWaveformData();
        }
        if (b == 2) {
            return new SegmentAudioSpectrumData();
        }
        if (b == 3) {
            return new SegmentAudioSpectrumDataPhaseS();
        }
        tlog.w("unknown typeName: " + str);
        return iSegmentDataProvider == null ? new SegmentAudioSpectrumDataPhaseS() : iSegmentDataProvider;
    }

    public static String getTypeName(ISegmentDataProvider iSegmentDataProvider, String str) {
        if (iSegmentDataProvider == null) {
            return str;
        }
        if (iSegmentDataProvider instanceof SegmentAudioWaveformData) {
            return SegmentAudioWaveformData.typeName;
        }
        if (iSegmentDataProvider instanceof SegmentAudioSpectrumData) {
            return SegmentAudioSpectrumData.typeName;
        }
        if (iSegmentDataProvider instanceof SegmentAudioSpectrumDataPhaseS) {
            return SegmentAudioSpectrumDataPhaseS.typeName;
        }
        tlog.w("unknown instance type");
        return str;
    }
}
