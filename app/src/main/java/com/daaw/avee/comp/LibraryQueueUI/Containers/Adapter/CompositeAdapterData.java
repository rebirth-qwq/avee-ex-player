package com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.daaw.avee.comp.LibraryQueueUI.ViewHolders.BaseViewHolder;
import com.daaw.avee.comp.LibraryQueueUI.ViewHolders.ViewHolderFactory;
import java.lang.ref.WeakReference;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CompositeAdapterData implements ViewAdapter.IAdapterDataProvider {
    private WeakReference<IAdapter> adapter = new WeakReference<>(null);
    private ViewAdapter.IAdapterDataProvider[] adapterData;
    private IAdapter[] adapterWrap;
    private IContainerData[] containerData;
    private SectionDesc[] sectionDesc;

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public int dataPositionToPosition(int i) {
        return 0;
    }

    public CompositeAdapterData(SectionDesc[] sectionDescArr, ViewAdapter.IAdapterDataProvider[] iAdapterDataProviderArr, IContainerData[] iContainerDataArr) {
        this.containerData = iContainerDataArr;
        this.sectionDesc = sectionDescArr;
        this.adapterData = iAdapterDataProviderArr;
        this.adapterWrap = new IAdapter[iAdapterDataProviderArr.length];
        for (int i = 0; i < iAdapterDataProviderArr.length; i++) {
            this.adapterWrap[i] = new AdapterWrap(i);
            iAdapterDataProviderArr[i].onAdapterInitialized(this.adapterWrap[i]);
        }
    }

    protected IAdapter getAdapter() {
        return this.adapter.get();
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onAdapterInitialized(IAdapter iAdapter) {
        this.adapter = new WeakReference<>(iAdapter);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onAdapterDispose() {
        for (ViewAdapter.IAdapterDataProvider iAdapterDataProvider : this.adapterData) {
            iAdapterDataProvider.onAdapterDispose();
        }
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public int getItemViewType(int i) {
        Tuple2<Integer, Integer> tuple2PositionToDataPosition = positionToDataPosition(i);
        if (tuple2PositionToDataPosition.obj1.intValue() < 0) {
            return this.sectionDesc[tuple2PositionToDataPosition.obj2.intValue()].getViewType();
        }
        return this.adapterData[tuple2PositionToDataPosition.obj2.intValue()].getItemViewType(tuple2PositionToDataPosition.obj1.intValue());
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public int getItemCount() {
        int itemCount = 0;
        for (int i = 0; i < this.adapterData.length; i++) {
            itemCount = this.containerData[i].getSectionOpened() ? itemCount + this.adapterData[i].getItemCount() + 1 : itemCount + 1;
        }
        return itemCount;
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public long getItemId(int i) {
        Tuple2<Integer, Integer> tuple2PositionToDataPosition = positionToDataPosition(i);
        if (tuple2PositionToDataPosition.obj1.intValue() < 0) {
            return -1L;
        }
        return this.adapterData[tuple2PositionToDataPosition.obj2.intValue()].getItemId(tuple2PositionToDataPosition.obj1.intValue());
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return ViewHolderFactory.newInstance(viewGroup.getContext(), viewGroup, i, this.containerData[0]);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Tuple2<Integer, Integer> tuple2PositionToDataPosition = positionToDataPosition(i);
        if (tuple2PositionToDataPosition.obj1.intValue() < 0) {
            ((BaseViewHolder) viewHolder).onBind(this.containerData[tuple2PositionToDataPosition.obj2.intValue()], tuple2PositionToDataPosition.obj2.intValue());
        } else {
            this.adapterData[tuple2PositionToDataPosition.obj2.intValue()].onBindViewHolder(viewHolder, tuple2PositionToDataPosition.obj1.intValue());
        }
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onItemsMoved(int i, int i2, List<Integer> list) {
        Tuple2<Integer, Integer> tuple2PositionToDataPosition = positionToDataPosition(i);
        Tuple2<Integer, Integer> tuple2PositionToDataPosition2 = positionToDataPosition(i2);
        if (!tuple2PositionToDataPosition.obj2.equals(tuple2PositionToDataPosition.obj1)) {
            tlog.w("Assert posFrom.obj2 != posFrom.obj1");
            this.adapterData[tuple2PositionToDataPosition2.obj2.intValue()].onItemsMoved(tuple2PositionToDataPosition.obj1.intValue(), -1, list);
        } else {
            this.adapterData[tuple2PositionToDataPosition2.obj2.intValue()].onItemsMoved(tuple2PositionToDataPosition.obj1.intValue(), tuple2PositionToDataPosition2.obj1.intValue(), list);
        }
    }

    public int dataPositionToPosition(int i, int i2) {
        int iMin = Math.min(i2, this.adapterData.length);
        int itemCount = 0;
        for (int i3 = 0; i3 < iMin; i3++) {
            itemCount = this.containerData[i3].getSectionOpened() ? itemCount + this.adapterData[i3].getItemCount() + 1 : itemCount + 1;
        }
        return i + itemCount + 1;
    }

    public Tuple2<Integer, Integer> positionToDataPosition(int i) {
        int i2 = 0;
        int itemCount = 0;
        int i3 = 0;
        while (i2 < this.adapterData.length) {
            itemCount = this.containerData[i2].getSectionOpened() ? itemCount + this.adapterData[i2].getItemCount() + 1 : itemCount + 1;
            if (i < itemCount) {
                return new Tuple2<>(Integer.valueOf((i - i3) - 1), Integer.valueOf(i2));
            }
            i2++;
            i3 = itemCount;
        }
        return new Tuple2<>(-1, -1);
    }

    public static class SectionDesc {
        private final int viewType;

        public SectionDesc(int i) {
            this.viewType = i;
        }

        public int getViewType() {
            return this.viewType;
        }
    }

    class AdapterWrap implements IAdapter {
        int sectionIndex;

        public AdapterWrap(int i) {
            this.sectionIndex = i;
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyDataSetChanged() {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyDataSetChanged();
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemChanged(int i) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemChanged(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex));
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemRangeChanged(int i, int i2) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemRangeChanged(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex), i2);
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemInserted(int i) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemInserted(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex));
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemMoved(int i, int i2) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemMoved(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex), CompositeAdapterData.this.dataPositionToPosition(i2, this.sectionIndex));
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemRangeInserted(int i, int i2) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemRangeInserted(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex), i2);
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemRemoved(int i) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemRemoved(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex));
            }
        }

        @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter
        public void myNotifyItemRangeRemoved(int i, int i2) {
            IAdapter adapter = CompositeAdapterData.this.getAdapter();
            if (adapter != null) {
                adapter.myNotifyItemRangeRemoved(CompositeAdapterData.this.dataPositionToPosition(i, this.sectionIndex), i2);
            }
        }
    }
}
