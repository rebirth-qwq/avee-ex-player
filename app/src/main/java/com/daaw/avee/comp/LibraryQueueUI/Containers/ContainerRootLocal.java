package com.daaw.avee.comp.LibraryQueueUI.Containers;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import androidx.recyclerview.widget.RecyclerView;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.VTaskResult;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.CompositeAdapterData;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.HeaderFooterAdapterData;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.CompositeContainer;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.CursorContainerBase;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData;
import com.daaw.avee.comp.LibraryQueueUI.ViewHolders.ContentItemViewHolder;
import java.lang.ref.WeakReference;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class ContainerRootLocal extends CursorContainerBase {
    public static final String AddressMore = "6";
    /*
     * 联网功能接缝：原网络电台 IceCast / Shoutcast 的实现（comp.IceCast、comp.Shoutcast、
     * vendor.shoutcast、vendor/playlists/rss、vendor/playlists/atom 及 DownloadTask）已连根拔起，
     * 仅保留这两个根列表地址位。将来做“检查更新 / 下载歌词 / 下载字体”等联网功能时，
     * 在 makeCursor() 里 addRow、并在 createChildAdapter() 的 switch 中挂载新容器即可。
     */
    private static final String AddressOnlineReserved7 = "7";
    private static final String AddressOnlineReserved8 = "8";

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.CursorContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public int getItemViewType(int i) {
        return 0;
    }

    public ContainerRootLocal(Context context, int i, WeakReference<IContainerData.ILibraryContainerDataListener> weakReference) {
        super(context, "/", context.getString(R.string.menu_total_library), R.drawable.ic_library_2, i, weakReference);
        init(context);
    }

    static Cursor makeCursor(IContainerData.ILibraryContainerDataListener iLibraryContainerDataListener) {
        // 原版此处写死了原 APK 的资源 ID（如 "2131755181"），工程重新编译后 ID 会重新分配，
        // 导致库首页每一行的图标与文案整体错位。改为在运行期取 R.*，构建时自动绑定正确 ID。
        // 列序：0=_id(地址) 1=标题字符串资源 2=副标题 3=图标 drawable 资源
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "", "", ""});
        matrixCursor.addRow(new String[]{"-1", String.valueOf(R.string.libContainer_Everything), "", String.valueOf(R.drawable.ic_playlist4)});
        matrixCursor.addRow(new String[]{"0", String.valueOf(R.string.libContainer_RecentlyAdded), "", String.valueOf(R.drawable.ic_playlist4_time)});
        matrixCursor.addRow(new String[]{"1", String.valueOf(R.string.libContainer_Albums), "", String.valueOf(R.drawable.ic_album4)});
        matrixCursor.addRow(new String[]{"2", String.valueOf(R.string.libContainer_Artists), "", String.valueOf(R.drawable.ic_artist4)});
        matrixCursor.addRow(new String[]{"3", String.valueOf(R.string.libContainer_Genres), "", String.valueOf(R.drawable.ic_music_book2)});
        matrixCursor.addRow(new String[]{"4", String.valueOf(R.string.libContainer_Playlists), "", String.valueOf(R.drawable.ic_playlist4)});
        matrixCursor.addRow(new String[]{"5", String.valueOf(R.string.libContainer_Folders), "", String.valueOf(R.drawable.ic_folder4)});
        if (iLibraryContainerDataListener != null && iLibraryContainerDataListener.onRequestRootContainerItemsVisible() == 0) {
            matrixCursor.addRow(new String[]{AddressMore, String.valueOf(R.string.libContainer_More), "", String.valueOf(R.drawable.ic_expand2)});
        }
        // 联网条目接缝：此处按需 matrixCursor.addRow(...)
        return matrixCursor;
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase
    public ViewAdapter createAdapter(Context context, int i) {
        return new ViewAdapter(new HeaderFooterAdapterData(this, this, 3, 16), this);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase
    public String getItemPositionToItemAddress(int i) {
        return getItem(i).getString(0);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:41:0x0096  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData
    public ViewAdapter createChildAdapter(Context context, String str) {
        String str2;
        int i;
        int iFindRowAndMove = findRowAndMove("_id", str);
        byte b = 3;
        if (iFindRowAndMove >= 0) {
            String string = context.getString(getItem(iFindRowAndMove).getInt(1));
            i = getItem(iFindRowAndMove).getInt(3);
            str2 = string;
        } else {
            str2 = "";
            i = 0;
        }
        int iHashCode = str.hashCode();
        if (iHashCode != 55) {
            if (iHashCode != 56) {
                if (iHashCode != 1444) {
                    switch (iHashCode) {
                        case 48:
                            if (!str.equals("0")) {
                                b = -1;
                            } else {
                                b = 1;
                            }
                            break;
                        case 49:
                            if (!str.equals("1")) {
                                b = -1;
                            } else {
                                b = 2;
                            }
                            break;
                        case 50:
                            if (!str.equals("2")) {
                                b = -1;
                            }
                            break;
                        case 51:
                            if (!str.equals("3")) {
                                b = -1;
                            } else {
                                b = 4;
                            }
                            break;
                        case 52:
                            if (!str.equals("4")) {
                                b = -1;
                            } else {
                                b = 5;
                            }
                            break;
                        case 53:
                            if (!str.equals("5")) {
                                b = -1;
                            } else {
                                b = 6;
                            }
                            break;
                        default:
                            b = -1;
                            break;
                    }
                } else if (str.equals("-1")) {
                    b = 0;
                } else {
                    b = -1;
                }
            } else if (str.equals(AddressOnlineReserved8)) {
                b = 8;
            } else {
                b = -1;
            }
        } else if (str.equals(AddressOnlineReserved7)) {
            b = 7;
        } else {
            b = -1;
        }
        switch (b) {
            case 0:
                ContainerAllSongs containerAllSongs = new ContainerAllSongs(context, makeChildAddress(str), str2, i, this.pageIndex);
                containerAllSongs.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return containerAllSongs.createOrGetAdapter(context, 0);
            case 1:
                ContainerRecently containerRecently = new ContainerRecently(context, makeChildAddress(str), str2, i, this.pageIndex);
                containerRecently.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return containerRecently.createOrGetAdapter(context, 0);
            case 2:
                ContainerAlbums containerAlbums = new ContainerAlbums(context, makeChildAddress(str), str2, i, this.pageIndex);
                containerAlbums.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return containerAlbums.createOrGetAdapter(context);
            case 3:
                ContainerArtists containerArtists = new ContainerArtists(context, makeChildAddress(str), str2, i, this.pageIndex);
                containerArtists.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return containerArtists.createOrGetAdapter(context);
            case 4:
                ContainerGenres containerGenres = new ContainerGenres(context, makeChildAddress(str), str2, i, this.pageIndex);
                containerGenres.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return containerGenres.createOrGetAdapter(context);
            case 5:
                final ContainerPlaylist containerPlaylist = new ContainerPlaylist(context, makeChildAddress(str), this.pageIndex);
                final ContainerPlaylistFiles containerPlaylistFiles = new ContainerPlaylistFiles(context, makeChildAddress(str), this.pageIndex);
                CompositeContainer compositeContainer = new CompositeContainer(new IContainerData[]{containerPlaylist, containerPlaylistFiles}, new VTaskResult(context.getResources().getString(R.string.libContainer_Playlists)), R.drawable.ic_playlist4, new CompositeContainer.AdapterFactory() { // from class: com.daaw.avee.comp.LibraryQueueUI.Containers.ContainerRootLocal.1
                    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.CompositeContainer.AdapterFactory
                    public ViewAdapter createOrGetAdapter(Context context2, int i2, IContainerData iContainerData) {
                        CompositeAdapterData.SectionDesc[] sectionDescArr = {new CompositeAdapterData.SectionDesc(12), new CompositeAdapterData.SectionDesc(12)};
                        ContainerPlaylist containerPlaylist2 = containerPlaylist;
                        ContainerPlaylistFiles containerPlaylistFiles2 = containerPlaylistFiles;
                        return new ViewAdapter(new HeaderFooterAdapterData(new CompositeAdapterData(sectionDescArr, new ViewAdapter.IAdapterDataProvider[]{containerPlaylist2, containerPlaylistFiles2}, new IContainerData[]{containerPlaylist2, containerPlaylistFiles2}), iContainerData, 6, 1), iContainerData);
                    }
                });
                compositeContainer.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return compositeContainer.createOrGetAdapter(context);
            case 6:
                ContainerFolders containerFolders = new ContainerFolders(context, makeChildAddress(str), str2, i, this.pageIndex);
                containerFolders.setLibraryContainerDataListener(this.libraryContainerDataListenerWeak);
                return containerFolders.createOrGetAdapter(context);
            // 联网条目接缝：原 case 7/8（IceCast / Shoutcast 网络电台）已移除，在此挂载新的联网容器
            default:
                return null;
        }
    }

    public void getView(Cursor cursor, int i, ContentItemViewHolder contentItemViewHolder) {
        contentItemViewHolder.setToDefault(this, getSelectionContainerIdentifier());
        contentItemViewHolder.setImgResource(cursor.getInt(3));
        contentItemViewHolder.setImgColorFilter(this.colorImgArt);
        contentItemViewHolder.txtNum.setVisibility(8);
        contentItemViewHolder.txtItemLine1.setText(cursor.getInt(1));
        contentItemViewHolder.txtItemLine1.setTextColor(this.color);
        contentItemViewHolder.setTxtItemLine2Visibility(8);
        contentItemViewHolder.txtItemDuration.setText("");
        if (cursor.getString(0).equals(AddressMore)) {
            contentItemViewHolder.setImgResource(cursor.getInt(3));
            contentItemViewHolder.setImgColorFilter(this.colorLight);
            contentItemViewHolder.txtItemLine1.setTextColor(this.colorLight);
        }
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.CursorContainerBase
    public Tuple2<Cursor, String> createOrGetCursor(Context context) {
        return new Tuple2<>(makeCursor(this.libraryContainerDataListenerWeak.get()), null);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Adapter.ViewAdapter.IAdapterDataProvider
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Cursor item = getItem(i);
        ContentItemViewHolder contentItemViewHolder = (ContentItemViewHolder) viewHolder;
        contentItemViewHolder.itemPosition = i;
        getView(item, i, contentItemViewHolder);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData
    public boolean getSectionOpened() {
        return onRequestSectionOpenedState.invoke(ContainerRootLocal.class, false).booleanValue();
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData
    public void setSectionOpenedState(boolean z) {
        onSetSectionOpened.invoke(Boolean.valueOf(z), ContainerRootLocal.class);
    }

    @Override // com.daaw.avee.comp.LibraryQueueUI.Containers.Base.CursorContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.ContainerBase, com.daaw.avee.comp.LibraryQueueUI.Containers.Base.IContainerData
    public boolean RefreshContent() {
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext == null) {
            return true;
        }
        setDataAndNotifyDataSetChanged(createOrGetCursor(appContext).obj1, null);
        return true;
    }
}
