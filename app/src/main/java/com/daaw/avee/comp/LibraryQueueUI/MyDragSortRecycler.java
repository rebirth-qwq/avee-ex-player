package com.daaw.avee.comp.LibraryQueueUI;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daaw.avee.vendor.dragsort.DragSortRecycler;

/* JADX INFO: loaded from: classes.dex */
public class MyDragSortRecycler extends DragSortRecycler {
    @Override // com.daaw.avee.vendor.dragsort.DragSortRecycler, androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
    }
}
