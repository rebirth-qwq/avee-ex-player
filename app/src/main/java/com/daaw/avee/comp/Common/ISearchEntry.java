package com.daaw.avee.comp.Common;

/* JADX INFO: loaded from: classes.dex */
public interface ISearchEntry {
    IGeneralItemContainerIdentifier getContainerIdentifier();

    String getHint();

    int getIndex();

    String getQuery();

    boolean isEnabled();
}
