package com.daaw.avee.Common;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
 import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class UtilsMusic {
    public static int findSongInList(List<PlaylistSong> list, PlaylistSong playlistSong) {
        return findSongInList(list, playlistSong, 0);
    }

    public static int findSongInList(List<PlaylistSong> list, PlaylistSong playlistSong, int i) {
        while (i < list.size()) {
            if (list.get(i).compare(playlistSong)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static List<PlaylistSong> songListFromCursor(Cursor cursor) {
        return songListFromCursor(cursor, null);
    }

    public static List<PlaylistSong> songListFromCursor(Cursor cursor, List<PlaylistSong> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        int columnIndex = cursor.getColumnIndex("audio_id");
        if (columnIndex == -1) {
            columnIndex = cursor.getColumnIndex("_id");
        }
        if (columnIndex == -1) {
            return list;
        }
        while (cursor.moveToNext()) {
            list.add(new PlaylistSong(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(columnIndex))));
        }
        return list;
    }

    public static int playlistIdForPlaylist(Context context, String str) {
        Cursor cursorQuerySafe = MediaStoreUtils.querySafe(context.getContentResolver(), MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "name=?", new String[]{str}, "name");
        int i = -1;
        if (cursorQuerySafe != null) {
            cursorQuerySafe.moveToFirst();
            i = cursorQuerySafe.isAfterLast() ? -1 : cursorQuerySafe.getInt(0);
            cursorQuerySafe.close();
        }
        return i;
    }

    public static void getPlayLists(Context context, List<Long> list, List<String> list2) {
        String[] strArr = {"_id", "name"};
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            System.out.println("resolver = null");
            return;
        }
        Cursor cursorQuerySafe = MediaStoreUtils.querySafe(contentResolver, MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, strArr, "name != ''", null, "name");
        if (cursorQuerySafe != null && cursorQuerySafe.getCount() > 0) {
            cursorQuerySafe.moveToFirst();
            while (!cursorQuerySafe.isAfterLast()) {
                list.add(Long.valueOf(cursorQuerySafe.getLong(0)));
                String string = cursorQuerySafe.getString(1);
                if (string == null) {
                    string = "unnamed";
                }
                list2.add(string);
                cursorQuerySafe.moveToNext();
            }
        }
        if (cursorQuerySafe != null) {
            cursorQuerySafe.close();
        }
    }

    public static List<PlaylistSong> getMostRecentTrackListByCount(Context context, int i) {
        Cursor cursorQuerySafe = MediaStoreUtils.querySafe(context.getContentResolver(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "is_music != 0", null, "date_added DESC limit " + i);
        if (cursorQuerySafe != null) {
            List<PlaylistSong> listSongListFromCursor = songListFromCursor(cursorQuerySafe);
            cursorQuerySafe.close();
            return listSongListFromCursor;
        }
        return new ArrayList();
    }
}
