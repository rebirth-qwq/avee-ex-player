package com.daaw.avee.comp.LibraryQueueUI.Containers.Base;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.daaw.avee.Common.Func.Func;
import com.daaw.avee.Common.Func.Func1;
import com.daaw.avee.Common.IVAsyncTask;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.IAdapter;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.daaw.avee.comp.LibraryQueueUI.ViewHolders.ViewHolderFactory;
import java.lang.ref.WeakReference;
import java.util.List;
import junit.framework.Assert;

/* JADX INFO: loaded from: classes.dex */
public abstract class ListContainerBase<T, V extends List<T>> extends ContainerBase implements ViewAdapter.IAdapterDataProvider {
    private WeakReference<IAdapter> associatedAdapter;
    private Func1<ContainerBase, V> getContent;
    private V list;
    protected Func<V> listFactory;

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public int dataPositionToPosition(int i) {
        return i;
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public long getItemId(int i) {
        return i;
    }

    public abstract int getItemViewType(int i);

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onAdapterDispose() {
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onItemsMoved(int i, int i2, List<Integer> list) {
    }

    protected ListContainerBase(Context context, Func1<ContainerBase, V> func1, Func<V> func, String str, IVAsyncTask<String> iVAsyncTask, int i, int i2) {
        super(context, str, iVAsyncTask, i, i2);
        this.associatedAdapter = new WeakReference<>(null);
        this.getContent = func1;
        this.listFactory = func;
        this.list = func1.onInvoke(this);
    }

    protected IAdapter getAssociatedAdapter() {
        return this.associatedAdapter.get();
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onAdapterInitialized(IAdapter iAdapter) {
        this.associatedAdapter = new WeakReference<>(iAdapter);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData
    public int getItemCount() {
        return this.list.size();
    }

    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return ViewHolderFactory.newInstance(viewGroup.getContext(), viewGroup, i, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public V getList() {
        return this.list;
    }

    protected void setDataAndNotifyDataSetChanged(V v, String str) {
        if (checkItemIdent(str, true)) {
            return;
        }
        Assert.assertNotNull(v);
        this.list = v;
        IAdapter associatedAdapter = getAssociatedAdapter();
        if (associatedAdapter != null) {
            associatedAdapter.myNotifyDataSetChanged();
        }
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData
    public boolean RefreshContent() {
        if (checkItemIdent(getContent(true).obj2, false)) {
            return true;
        }
        Tuple2<V, String> content = getContent(false);
        V v = content.obj1;
        if (v == null) {
            setDataAndNotifyDataSetChanged(this.listFactory.onInvoke(), null);
        } else {
            setDataAndNotifyDataSetChanged(v, content.obj2);
        }
        return true;
    }

    protected Tuple2<V, String> getContent(boolean z) {
        V vOnInvoke;
        V vOnInvoke2;
        if (z || (vOnInvoke2 = this.getContent.onInvoke(this)) == null) {
            vOnInvoke = null;
        } else {
            vOnInvoke = this.listFactory.onInvoke();
            vOnInvoke.addAll(vOnInvoke2);
        }
        return new Tuple2<>(vOnInvoke, "");
    }
}
