package com.daaw.avee.comp.LibraryQueueUI.ContextualActions;

import android.content.Context;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.MultiList;
import com.daaw.avee.Common.Tuple3;
import com.daaw.avee.ContextData;
import com.daaw.avee.comp.Common.IItemIdentifier;
import com.daaw.avee.comp.ContextualActionBar.ActionListenerBase;
import com.daaw.avee.comp.ContextualActionBar.ItemActionBase;
import com.daaw.avee.comp.PlaybackQueue.IPlaylistSongContainerIdentifier;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class ItemActionsSongs {
    public static WeakEventR<IPlaylistSongContainerIdentifier> onRequestSongContainerIdentifier = new WeakEventR<>();
    public static WeakEventR<MultiList<PlaylistSong, IItemIdentifier>> onRequestQueueList = new WeakEventR<>();
    public static WeakEvent2<ContextData, List<ItemsDetails>> onActionViewDetails = new WeakEvent2<>();
    public static WeakEvent2<ContextData, Collection<PlaylistSong>> onSetAsRingtone = new WeakEvent2<>();
    public static WeakEvent1<Integer> onQueuePositionChanged = new WeakEvent1<>();
    public static WeakEvent2<Collection<PlaylistSong>, Integer> onEnqueue = new WeakEvent2<>();
    public static WeakEvent3<List<PlaylistSong>, Integer, IPlaylistSongContainerIdentifier> onOpen2 = new WeakEvent3<>();
    public static WeakEvent4<Context, List<PlaylistSong>, Boolean, ContextData> onLibraryQueueUI_ActionSongSendToPlaylist = new WeakEvent4<>();

    public static class ItemsDetails {
        PlaylistSong song;

        public ItemsDetails(PlaylistSong playlistSong) {
            this.song = playlistSong;
        }

        public PlaylistSong getSong() {
            return this.song;
        }
    }

    public static class ItemActionEnqueue extends ItemActionBase {
        public static ItemActionBase itemAction2Enqueue = new ItemActionEnqueue();

        public ItemActionEnqueue() {
            super(3, true, R.drawable.ic_playlist4, R.string.libItemAction_enqueue);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((EnqueueActionListener2) list2.get(i)).onEnqueue(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onEnqueue.invoke(arrayList, 3);
        }

        public static abstract class EnqueueActionListener2 extends ActionListenerBase {
            protected abstract void onEnqueue(Context context, Object obj, List<PlaylistSong> list);

            public EnqueueActionListener2() {
                super(ItemActionEnqueue.itemAction2Enqueue);
            }
        }
    }

    public static class ItemActionEnqueueNext extends ItemActionBase {
        public static ItemActionBase itemAction2EnqueueNext = new ItemActionEnqueueNext();

        public ItemActionEnqueueNext() {
            super(3, true, R.drawable.ic_playlist4, R.string.libItemAction_enqueueNext);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((EnqueueNextActionListener2) list2.get(i)).onEnqueue(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onEnqueue.invoke(arrayList, 2);
        }

        public static abstract class EnqueueNextActionListener2 extends ActionListenerBase {
            protected abstract void onEnqueue(Context context, Object obj, List<PlaylistSong> list);

            public EnqueueNextActionListener2() {
                super(ItemActionEnqueueNext.itemAction2EnqueueNext);
            }
        }
    }

    public static class EnqueueAllContainerItemAction extends ItemActionBase {
        public static ItemActionBase enqueueContainerItemAction = new EnqueueAllContainerItemAction();

        public EnqueueAllContainerItemAction() {
            super(3, true, R.drawable.ic_playlist4, R.string.libItemAction_enqueueAll);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((EnqueueAllContainerActionListener2) list2.get(i)).onEnqueue(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onEnqueue.invoke(arrayList, 3);
        }

        public static abstract class EnqueueAllContainerActionListener2 extends ActionListenerBase {
            protected abstract void onEnqueue(Context context, Object obj, List<PlaylistSong> list);

            public EnqueueAllContainerActionListener2() {
                super(EnqueueAllContainerItemAction.enqueueContainerItemAction);
            }
        }
    }

    public static class SendToItemAction extends ItemActionBase {
        public static ItemActionBase itemActionSendTo = new SendToItemAction();

        public SendToItemAction() {
            super(4, true, R.drawable.ic_add2, R.string.libItemAction_sendTo);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((SendToActionListener) list2.get(i)).onSendTo(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onLibraryQueueUI_ActionSongSendToPlaylist.invoke(contextData.getContext(), arrayList, false, contextData);
        }

        public static abstract class SendToActionListener extends ActionListenerBase {
            protected abstract void onSendTo(Context context, Object obj, List<PlaylistSong> list);

            public SendToActionListener() {
                super(SendToItemAction.itemActionSendTo);
            }
        }
    }

    public static class ViewDetailsItemAction extends ItemActionBase {
        public static ItemActionBase itemActionViewDetails = new ViewDetailsItemAction();

        public ViewDetailsItemAction() {
            super(6, false, true, R.drawable.ic_gear, R.string.libItemAction_details);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(((ViewDetailsActionListener2) list2.get(i)).onDetails(contextData.getContext(), list.get(i)));
            }
            ItemActionsSongs.onActionViewDetails.invoke(contextData, arrayList);
        }

        public static abstract class ViewDetailsActionListener2 extends ActionListenerBase {
            protected abstract ItemsDetails onDetails(Context context, Object obj);

            public ViewDetailsActionListener2() {
                super(ViewDetailsItemAction.itemActionViewDetails);
            }
        }
    }

    public static class SetAsRingtoneItemAction extends ItemActionBase {
        public static ItemActionBase itemActionSetAsRingtone = new SetAsRingtoneItemAction();

        public SetAsRingtoneItemAction() {
            super(7, false, true, R.drawable.ic_gear, R.string.libItemAction_setAsRingtone);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((SetAsRingtoneActionListener) list2.get(i)).onSetAsRingtone(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onSetAsRingtone.invoke(contextData, arrayList);
        }

        public static abstract class SetAsRingtoneActionListener extends ActionListenerBase {
            protected abstract void onSetAsRingtone(Context context, Object obj, List<PlaylistSong> list);

            public SetAsRingtoneActionListener() {
                super(SetAsRingtoneItemAction.itemActionSetAsRingtone);
            }
        }
    }

    public static class PlayMultiItemAction extends ItemActionBase {
        public static ItemActionBase playMultiItemAction = new PlayMultiItemAction();

        public PlayMultiItemAction() {
            super(2, true, false, R.drawable.ic_playlist4, R.string.libItemAction_playAllMulti, true);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((PlayMultiActionListener2) list2.get(i)).onPlayMulti(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onOpen2.invoke(arrayList, 0, null);
        }

        public static abstract class PlayMultiActionListener2 extends ActionListenerBase {
            protected abstract void onPlayMulti(Context context, Object obj, List<PlaylistSong> list);

            public PlayMultiActionListener2() {
                super(PlayMultiItemAction.playMultiItemAction);
            }
        }
    }

    public static class PlaySingleItemAction extends ItemActionBase {
        public static ItemActionBase playSingleItemAction = new PlaySingleItemAction();

        public PlaySingleItemAction() {
            super(1, false, true, R.drawable.ic_playlist4, R.string.libItemAction_play, true);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ((PlaySingleActionListener2) list2.get(i)).onPlaySingle(contextData.getContext(), list.get(i), arrayList);
            }
            ItemActionsSongs.onOpen2.invoke(arrayList, 0, null);
        }

        public static abstract class PlaySingleActionListener2 extends ActionListenerBase {
            protected abstract void onPlaySingle(Context context, Object obj, List<PlaylistSong> list);

            public PlaySingleActionListener2() {
                super(PlaySingleItemAction.playSingleItemAction);
            }
        }
    }

    public static class PlayAllContainerItemAction extends ItemActionBase {
        private static ItemActionBase playAllContainerItemAction = new PlayAllContainerItemAction();

        public PlayAllContainerItemAction() {
            super(2, false, true, R.drawable.ic_playlist4, R.string.libItemAction_playAll, true);
        }

        @Override // com.daaw.avee.comp.ContextualActionBar.ItemActionBase
        public void executeListBase(ContextData contextData, List<Object> list, List<ActionListenerBase> list2) {
            ArrayList arrayList = new ArrayList();
            IPlaylistSongContainerIdentifier iPlaylistSongContainerIdentifier = null;
            IPlaylistSongContainerIdentifier iPlaylistSongContainerIdentifierInvoke = ItemActionsSongs.onRequestSongContainerIdentifier.invoke(null);
            MultiList<PlaylistSong, IItemIdentifier> multiListInvoke = ItemActionsSongs.onRequestQueueList.invoke(new MultiList<>());
            boolean z = false;
            IPlaylistSongContainerIdentifier iPlaylistSongContainerIdentifier2 = null;
            boolean zBooleanValue = false;
            int iIntValue = 0;
            for (int i = 0; i < list.size(); i++) {
                Tuple3<Integer, IPlaylistSongContainerIdentifier, Boolean> tuple3OnPlayAllContainer = ((PlayAllContainerActionListener2) list2.get(i)).onPlayAllContainer(contextData.getContext(), list.get(i), arrayList, iPlaylistSongContainerIdentifierInvoke, multiListInvoke);
                iIntValue = tuple3OnPlayAllContainer.obj1.intValue();
                iPlaylistSongContainerIdentifier2 = tuple3OnPlayAllContainer.obj2;
                zBooleanValue = tuple3OnPlayAllContainer.obj3.booleanValue();
            }
            if (list.size() > 1) {
                iIntValue = 0;
            } else {
                z = zBooleanValue;
                iPlaylistSongContainerIdentifier = iPlaylistSongContainerIdentifier2;
            }
            if (z) {
                ItemActionsSongs.onQueuePositionChanged.invoke(Integer.valueOf(iIntValue));
            } else {
                ItemActionsSongs.onOpen2.invoke(arrayList, Integer.valueOf(iIntValue), iPlaylistSongContainerIdentifier);
            }
        }

        public static abstract class PlayAllContainerActionListener2 extends ActionListenerBase {
            protected abstract Tuple3<Integer, IPlaylistSongContainerIdentifier, Boolean> onPlayAllContainer(Context context, Object obj, List<PlaylistSong> list, IPlaylistSongContainerIdentifier iPlaylistSongContainerIdentifier, MultiList<PlaylistSong, IItemIdentifier> multiList);

            public PlayAllContainerActionListener2() {
                super(PlayAllContainerItemAction.playAllContainerItemAction);
            }
        }
    }
}
