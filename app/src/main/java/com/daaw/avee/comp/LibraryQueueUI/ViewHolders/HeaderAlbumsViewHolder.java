package com.daaw.avee.comp.LibraryQueueUI.ViewHolders;

import android.content.Context;
import android.view.ViewGroup;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class HeaderAlbumsViewHolder extends BaseHeaderViewHolder {
    public HeaderAlbumsViewHolder(Context context, ViewGroup viewGroup, IContainerData iContainerData) {
        super(UtilsUI.getInflaterFromContext(context).inflate(R.layout.header_albums_item, viewGroup, false), iContainerData);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.ViewHolders.BaseHeaderViewHolder, com.daaw.avee.comp.LibraryQueueUI.ViewHolders.BaseSimpleViewHolder, com.daaw.avee.comp.LibraryQueueUI.ViewHolders.BaseViewHolder
    public void onBind(IContainerData iContainerData, int i) {
        super.onBind(iContainerData, i);
    }
}
