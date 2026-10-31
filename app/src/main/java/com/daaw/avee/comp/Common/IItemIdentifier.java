package com.daaw.avee.comp.Common;

/* JADX INFO: loaded from: classes.dex */
public interface IItemIdentifier {
    public static final int SLOT_ORDINARY = 0;
    public static final int SLOT_TRANSIENT = -1;

    int getQueueIndex();

    int getQueueSlot();

    void setQueueSlot(int i);
}
