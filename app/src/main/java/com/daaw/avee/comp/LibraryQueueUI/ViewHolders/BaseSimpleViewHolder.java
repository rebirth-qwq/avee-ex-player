package com.daaw.avee.comp.LibraryQueueUI.ViewHolders;

import android.view.View;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData;

public abstract class BaseSimpleViewHolder extends BaseViewHolder {

    public BaseSimpleViewHolder(View view, int i, boolean z) {
        super(view);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.ViewHolders.BaseViewHolder
    public void onBind(IContainerData iContainerData, int i) {
    }
}
