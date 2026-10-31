package com.daaw.avee.comp.LibraryQueueUI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daaw.avee.comp.Common.View.PagerSlidingTabStrip;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.IGeneralItemContainerIdentifier;
import com.daaw.avee.comp.GlobalSearch.SearchEntryOptions;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.daaw.avee.comp.LibraryQueueUI.Containers.ContainerRootLocal;
import com.daaw.avee.comp.LibraryQueueUI.ViewHolders.ContentItemViewHolder;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import junit.framework.Assert;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class Fragment0 extends LibraryQueueFragmentBase {
    private View backSwipeProgress;
    private ViewAdapter libraryRootAdapter;
    private PagerSlidingTabStrip pathTabStrip;
    private RecyclerView recyclerViewItems;
    private HashMap<String, ContainerScrollData> pathContainerData = new HashMap<>();
    private String currentAbsoluteLibraryAddress = "";

    public static Fragment0 newInstance() {
        Fragment0 fragment0 = new Fragment0();
        fragment0.setArguments(new Bundle());
        return fragment0;
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        String string = AppPreferences.createOrGetInstance().getString(AppPreferences.PREF_String_currentAbsoluteLibraryAddress);
        this.currentAbsoluteLibraryAddress = string;
        if (string == null) {
            this.currentAbsoluteLibraryAddress = "";
        }
        navigateLibraryAddress(null, this.currentAbsoluteLibraryAddress);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        String string = AppPreferences.createOrGetInstance().getString(AppPreferences.PREF_String_currentAbsoluteLibraryAddress);
        this.currentAbsoluteLibraryAddress = string;
        if (string == null) {
            this.currentAbsoluteLibraryAddress = "";
        }
        View viewInflate = layoutInflater.inflate(R.layout.fragment_0, viewGroup, false);
        UtilsUI.setStatusBarDimensions(viewInflate.findViewById(R.id.viewStatusBarBg));
        onCreateView.invoke(0, viewInflate.findViewById(R.id.viewActionBarBgSkin));
        this.backSwipeProgress = viewInflate.findViewById(R.id.backSwipeProgress);
        navigateForBackwardProgress(0.0f);
        this.pathTabStrip = (PagerSlidingTabStrip) viewInflate.findViewById(R.id.tabStripPath);
        int color = getResources().getColor(R.color.action_bar_fg);
        this.pathTabStrip.setTextColor(color);
        this.pathTabStrip.setTextColorSelected(color);
        this.pathTabStrip.setDividerColor(color);
        this.pathTabStrip.setUnderlineColor(color);
        this.pathTabStrip.setIndicatorColor(color);
        this.pathTabStrip.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.LibraryQueueUI.Fragment0.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Fragment0.this.navigateLibraryAddress(null, (String) view.getTag());
            }
        });
        this.recyclerViewItems = (RecyclerView) viewInflate.findViewById(R.id.recyclerViewItems);
        this.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView recyclerView = this.recyclerViewItems;
        recyclerView.addItemDecoration(new SpacesItemDecoration(1, UtilsUI.getAttrColor(recyclerView, R.attr.containerBackgroundDark)));
        View viewFindViewById = viewInflate.findViewById(R.id.tabStripPathGroup);
        UtilsUI.disallowInterceptTouchEventRecursive(viewFindViewById, viewFindViewById.getParent());
        this.libraryRootAdapter = new ContainerRootLocal(getActivity(), 0, new WeakReference(this)).createOrGetAdapter(getActivity());
        navigateLibraryAddress(null, this.currentAbsoluteLibraryAddress);
        updateTrackInfo();
        updateSearchInfo();
        return viewInflate;
    }

    boolean isViewCreated() {
        return this.pathTabStrip != null;
    }

    public void updateTrackInfo() {
        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = this.recyclerViewItems;
        if (recyclerView == null || (adapter = recyclerView.getAdapter()) == null) {
            return;
        }
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            RecyclerView.ViewHolder viewHolderFindViewHolderForAdapterPosition = this.recyclerViewItems.findViewHolderForAdapterPosition(i);
            if (viewHolderFindViewHolderForAdapterPosition instanceof ContentItemViewHolder) {
                ((ContentItemViewHolder) viewHolderFindViewHolderForAdapterPosition).refreshSelection();
            }
        }
    }

    public void updateLibraryItems() {
        IContainerData containerData;
        if (isViewCreated()) {
            boolean zRefreshContent = false;
            ViewAdapter viewAdapter = (ViewAdapter) this.recyclerViewItems.getAdapter();
            if (viewAdapter != null && (containerData = viewAdapter.getContainerData()) != null) {
                zRefreshContent = containerData.RefreshContent(true);
            }
            if (zRefreshContent) {
                return;
            }
            navigateLibraryAddress(null, this.currentAbsoluteLibraryAddress, true);
        }
    }

    public void refreshAdapter(IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier) {
        ViewAdapter viewAdapter;
        RecyclerView recyclerView = this.recyclerViewItems;
        if (recyclerView == null || (viewAdapter = (ViewAdapter) recyclerView.getAdapter()) == null || !viewAdapter.getContainerData().containsContainerIdentifier(iGeneralItemContainerIdentifier)) {
            return;
        }
        viewAdapter.notifyDataSetChanged();
    }

    public void navigateForBackwardProgress(float f) {
        View view = this.backSwipeProgress;
        if (view == null) {
            return;
        }
        view.setPivotX(1.0f);
        this.backSwipeProgress.setScaleX(f);
    }

    public void navigateForBackwardLibraryAddress() {
        int iLastIndexOf;
        String str = this.currentAbsoluteLibraryAddress;
        if (str.charAt(str.length() - 1) == '/') {
            iLastIndexOf = str.lastIndexOf(47, str.length() - 2);
        } else {
            iLastIndexOf = str.lastIndexOf(47);
        }
        navigateLibraryAddress(null, iLastIndexOf <= 0 ? "/" : str.substring(0, iLastIndexOf));
    }

    public void navigateLibraryAddress(ViewAdapter viewAdapter, String str) {
        navigateLibraryAddress(viewAdapter, str, false);
    }

    public void navigateLibraryAddress(ViewAdapter viewAdapter, String str, boolean z) {
        RecyclerView recyclerView;
        ViewAdapter viewAdapter2;
        if (str.length() <= 0 || str.charAt(0) != '/') {
            str = "/";
        }
        if (viewAdapter == null) {
            viewAdapter = this.libraryRootAdapter;
        }
        if (z || viewAdapter == null || (recyclerView = this.recyclerViewItems) == null || (viewAdapter2 = (ViewAdapter) recyclerView.getAdapter()) == null || !viewAdapter.getContainerData().makeChildAddress(str).equals(viewAdapter2.getContainerData().getLibraryAddress())) {
            if (str.length() > 0 && str.charAt(0) == '/') {
                if (viewAdapter == null) {
                    viewAdapter = this.libraryRootAdapter;
                }
                this.pathTabStrip.myClearTabs();
                this.pathTabStrip.myAddTab(viewAdapter.getContainerData().getDisplayName(), viewAdapter.getContainerData().getDisplayIconResId(), viewAdapter.getContainerData().getLibraryAddress());
                str = str.substring(1, str.length());
            }
            ViewAdapter viewAdapterNavigateLibraryAddressRecursive = navigateLibraryAddressRecursive(viewAdapter, str);
            this.currentAbsoluteLibraryAddress = viewAdapterNavigateLibraryAddressRecursive.getContainerData().getLibraryAddress();
            AppPreferences.createOrGetInstance().setString(AppPreferences.PREF_String_currentAbsoluteLibraryAddress, this.currentAbsoluteLibraryAddress);
            setLibraryAdapter(viewAdapterNavigateLibraryAddressRecursive);
        }
    }

    public void navigateForwardLibraryAddress(ViewAdapter viewAdapter, String str) {
        if (viewAdapter == null) {
            viewAdapter = (ViewAdapter) this.recyclerViewItems.getAdapter();
        }
        if (viewAdapter == null) {
            return;
        }
        ViewAdapter viewAdapterNavigateLibraryAddressRecursive = navigateLibraryAddressRecursive(viewAdapter, str);
        this.currentAbsoluteLibraryAddress = viewAdapterNavigateLibraryAddressRecursive.getContainerData().getLibraryAddress();
        AppPreferences.createOrGetInstance().setString(AppPreferences.PREF_String_currentAbsoluteLibraryAddress, this.currentAbsoluteLibraryAddress);
        setLibraryAdapter(viewAdapterNavigateLibraryAddressRecursive);
    }

    ViewAdapter navigateLibraryAddressRecursive(ViewAdapter viewAdapter, String str) {
        String strSubstring;
        if (str == null || str.isEmpty()) {
            return viewAdapter;
        }
        int iIndexOf = str.indexOf(47);
        if (iIndexOf < 0) {
            strSubstring = "";
        } else {
            String strSubstring2 = str.substring(0, iIndexOf);
            strSubstring = str.substring(iIndexOf + 1, str.length());
            str = strSubstring2;
        }
        ViewAdapter viewAdapterCreateChildAdapter = viewAdapter.getContainerData().createChildAdapter(getActivity(), str);
        if (viewAdapterCreateChildAdapter == null) {
            return viewAdapter;
        }
        this.pathTabStrip.myAddTab(viewAdapterCreateChildAdapter.getContainerData().getDisplayName(), viewAdapterCreateChildAdapter.getContainerData().getDisplayIconResId(), viewAdapterCreateChildAdapter.getContainerData().getLibraryAddress());
        return navigateLibraryAddressRecursive(viewAdapterCreateChildAdapter, strSubstring);
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0038  */
    /* JADX WARN: Code duplicated, block: B:15:0x003e  */
    void setLibraryAdapter(ViewAdapter viewAdapter) {
        boolean z;
        ContainerScrollData containerScrollData;
        ViewAdapter viewAdapter2 = (ViewAdapter) this.recyclerViewItems.getAdapter();
        boolean z2 = false;
        if (viewAdapter2 != null) {
            String libraryAddress = viewAdapter2.getContainerData().getLibraryAddress();
            String libraryAddress2 = viewAdapter.getContainerData().getLibraryAddress();
            z = true;
            if (libraryAddress2.length() >= libraryAddress.length() || !libraryAddress.contains(libraryAddress2)) {
                if (!libraryAddress.equals(libraryAddress2)) {
                }
                if (z2) {
                    this.pathContainerData.remove(libraryAddress);
                } else {
                    ContainerScrollData containerScrollData2 = new ContainerScrollData();
                    containerScrollData2.scrollPosition = getScrollPosition();
                    this.pathContainerData.put(viewAdapter2.getContainerData().getLibraryAddress(), containerScrollData2);
                }
            } else {
                z2 = true;
            }
            z = false;
            if (z2) {
                this.pathContainerData.remove(libraryAddress);
            } else {
                ContainerScrollData containerScrollData3 = new ContainerScrollData();
                containerScrollData3.scrollPosition = getScrollPosition();
                this.pathContainerData.put(viewAdapter2.getContainerData().getLibraryAddress(), containerScrollData3);
            }
        } else {
            z = false;
        }
        if (viewAdapter2 != viewAdapter) {
            if (viewAdapter2 != null) {
                viewAdapter2.dispose();
            }
            updateSearchInfo(viewAdapter);
            this.recyclerViewItems.setAdapter(viewAdapter);
            if ((z2 || z) && (containerScrollData = this.pathContainerData.get(viewAdapter.getContainerData().getLibraryAddress())) != null) {
                setScrollPosition(containerScrollData.scrollPosition);
            }
        }
    }

    public void updateSearchQuery(Context context, String str) {
        ViewAdapter viewAdapter;
        RecyclerView recyclerView = this.recyclerViewItems;
        if (recyclerView == null || (viewAdapter = (ViewAdapter) recyclerView.getAdapter()) == null) {
            return;
        }
        viewAdapter.getContainerData().updateSearchQuery(context, str);
    }

    private void updateSearchInfo(ViewAdapter viewAdapter) {
        Assert.assertNotNull(getActivity());
        SearchEntryOptions searchEntryOptions = getSearchEntryOptions(viewAdapter);
        onUpdateSearchOptions.invoke(0, Boolean.valueOf(searchEntryOptions.enabled), searchEntryOptions.hint, searchEntryOptions.containerIdentifier);
    }

    private SearchEntryOptions getSearchEntryOptions(ViewAdapter viewAdapter) {
        Activity activity = getActivity();
        if (activity == null) {
            return SearchEntryOptions.refuse;
        }
        return getSearchEntryOptions(activity, viewAdapter);
    }

    public SearchEntryOptions getSearchEntryOptions() {
        RecyclerView recyclerView = this.recyclerViewItems;
        return getSearchEntryOptions(recyclerView != null ? (ViewAdapter) recyclerView.getAdapter() : null);
    }

    public void updateSearchInfo() {
        RecyclerView recyclerView = this.recyclerViewItems;
        updateSearchInfo(recyclerView != null ? (ViewAdapter) recyclerView.getAdapter() : null);
    }

    int getScrollPosition() {
        RecyclerView.LayoutManager layoutManager = this.recyclerViewItems.getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            return 0;
        }
        return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
    }

    void setScrollPosition(int i) {
        RecyclerView.LayoutManager layoutManager = this.recyclerViewItems.getLayoutManager();
        if (layoutManager == null || i == -1) {
            return;
        }
        layoutManager.scrollToPosition(i);
    }

    class ContainerScrollData {
        public int scrollPosition = 0;

        ContainerScrollData() {
        }
    }
}
