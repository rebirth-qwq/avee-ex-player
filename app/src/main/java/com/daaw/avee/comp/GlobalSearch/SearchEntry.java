package com.daaw.avee.comp.GlobalSearch;

import com.daaw.avee.comp.Common.IGeneralItemContainerIdentifier;
import com.daaw.avee.comp.Common.ISearchEntry;

/* JADX INFO: loaded from: classes.dex */
public class SearchEntry extends SearchEntryOptions implements ISearchEntry {
    private final int index;
    String query = "";

    SearchEntry(int i) {
        this.index = i;
    }

    @Override // com.daaw.avee.comp.Common.ISearchEntry
    public int getIndex() {
        return this.index;
    }

    @Override // com.daaw.avee.comp.Common.ISearchEntry
    public String getQuery() {
        return this.query;
    }

    @Override // com.daaw.avee.comp.Common.ISearchEntry
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override // com.daaw.avee.comp.Common.ISearchEntry
    public String getHint() {
        return this.hint;
    }

    @Override // com.daaw.avee.comp.Common.ISearchEntry
    public IGeneralItemContainerIdentifier getContainerIdentifier() {
        return this.containerIdentifier;
    }
}
