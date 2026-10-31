package com.daaw.avee.comp.playback.Song;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.Utils;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
 import com.mpatric.mp3agic.Mp3File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class PlaylistSongMetadataRetriever {
    private static final String TAG = "PlaylistSongMetadata";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void AcquireData(Context context, Uri uri, String str, final Handler handler, final Action1<PlaylistSong.Data> action1) {
        final Object[] objArr = {context, uri, str};
        executorService.submit(new Callable<PlaylistSong.Data>() { // from class: com.daaw.avee.comp.playback.Song.PlaylistSongMetadataRetriever.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public PlaylistSong.Data call() throws Exception {
                final PlaylistSong.Data dataDoInBackground = PlaylistSong.doInBackground(objArr);
                if (handler != null) {
                    Handler handler2 = handler;
                    final Action1 action2 = action1;
                    handler2.post(new Runnable() { // from class: com.daaw.avee.comp.playback.Song.PlaylistSongMetadataRetriever.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            action2.onInvoke(dataDoInBackground);
                        }
                    });
                } else {
                    action1.onInvoke(dataDoInBackground);
                }
                return dataDoInBackground;
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:101:0x01f4  */
    /* JADX WARN: Code duplicated, block: B:97:0x01eb  */
    public static PlaylistSong.DataDetails AcquireDataMediaMetadataRetrieverLocal2(Context context, Uri uri, PlaylistSong.Data data) {
        String strSubstring;
        boolean z;
        String string;
        LyricsData lyricsData;
        String lyrics;
        int columnIndex;
        PlaylistSong.DataDetails dataDetails = new PlaylistSong.DataDetails(data);
        if (uri == null || uri == Uri.EMPTY) {
            return dataDetails;
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        String scheme = uri.getScheme();
        try {
            if ("content".equals(scheme)) {
                try {
                    mediaMetadataRetriever.setDataSource(context, uri);
                    strSubstring = null;
                    z = true;
                } catch (Exception e) {
                    Log.e(TAG, "Can't retrieve content metadata", e);
                    strSubstring = null;
                    z = false;
                }
            } else if ("http".equals(scheme) || "https".equals(scheme)) {
                String path = uri.getPath();
                int length = path.length();
                int iLastIndexOf = path.lastIndexOf(46);
                if (iLastIndexOf < 0) {
                    iLastIndexOf = length - 1;
                }
                int iMax = Math.max(path.lastIndexOf(47, iLastIndexOf), 0) + 1;
                int iIndexOf = path.indexOf(47, iLastIndexOf);
                if (iIndexOf >= 0) {
                    length = iIndexOf;
                }
                try {
                    strSubstring = path.substring(iMax, length);
                    if (strSubstring.length() >= 2) {
                        z = false;
                    } else {
                        strSubstring = null;
                        z = false;
                    }
                } catch (Exception e2) {
                    Log.e(TAG, "Can't retrieve metadata", e2);
                    strSubstring = null;
                    z = false;
                }
            } else {
                try {
                    mediaMetadataRetriever.setDataSource(context, uri);
                    strSubstring = null;
                    z = true;
                } catch (Exception e3) {
                    Log.e(TAG, "Can't retrieve metadata", e3);
                    strSubstring = null;
                    z = false;
                }
            }
        } catch (Exception e4) {
            Log.e(TAG, "Can't retrieve metadata", e4);
            strSubstring = null;
            z = false;
        }
        if (z) {
            dataDetails.isStream = false;
            dataDetails.mimeType = mediaMetadataRetriever.extractMetadata(12);
            dataDetails.trackName = mediaMetadataRetriever.extractMetadata(7);
            dataDetails.artistName = mediaMetadataRetriever.extractMetadata(2);
            dataDetails.albumName = mediaMetadataRetriever.extractMetadata(1);
            dataDetails.duration = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(9));
            dataDetails.trackNum = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(0));
            dataDetails.cdNum = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(14));
            dataDetails.year = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(8));
            dataDetails.albumArtist = mediaMetadataRetriever.extractMetadata(13);
            dataDetails.composer = mediaMetadataRetriever.extractMetadata(4);
            dataDetails.bitRate = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(20));
            dataDetails.width = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(18));
            dataDetails.height = Utils.strToIntSafe(mediaMetadataRetriever.extractMetadata(19));
        } else {
            dataDetails.isStream = true;
            if (strSubstring == null) {
                strSubstring = "";
            }
            dataDetails.secondName = strSubstring;
        }
        if (dataDetails.mimeType == null) {
            dataDetails.mimeType = "";
        }
        if (dataDetails.trackName == null) {
            dataDetails.trackName = "";
        }
        if (dataDetails.albumName == null) {
            dataDetails.albumName = "";
        }
        if (dataDetails.artistName == null) {
            dataDetails.artistName = "";
        }
        if (dataDetails.albumArtist == null) {
            dataDetails.albumArtist = "";
        }
        if (dataDetails.composer == null) {
            dataDetails.composer = "";
        }
        if (uri == null || uri == Uri.EMPTY) {
            string = null;
        } else {
            String scheme2 = uri.getScheme();
            if ("file".equals(scheme2)) {
                string = uri.getPath();
            } else if ("content".equals(scheme2)) {
                try {
                    Cursor cursorQuery = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                    if (cursorQuery != null) {
                        string = (!cursorQuery.moveToFirst() || (columnIndex = cursorQuery.getColumnIndex("_data")) < 0) ? null : cursorQuery.getString(columnIndex);
                        try {
                            cursorQuery.close();
                        } catch (Exception e5) {
                        }
                    } else {
                        string = null;
                    }
                } catch (Exception e6) {
                    string = null;
                }
            } else {
                string = null;
            }
        }
        if (string == null || !string.toLowerCase().endsWith(DefaultHlsExtractorFactory.MP3_FILE_EXTENSION)) {
            lyricsData = null;
        } else {
            try {
                Mp3File mp3File = new Mp3File(string);
                if (!mp3File.hasId3v2Tag() || (lyrics = mp3File.getId3v2Tag().getLyrics()) == null || lyrics.isEmpty()) {
                    lyricsData = null;
                } else {
                    lyricsData = LrcParser.parse(lyrics);
                }
            } catch (Exception e7) {
                lyricsData = null;
            }
        }
        if (lyricsData == null) {
            lyricsData = new LyricsData();
            lyricsData.type = 0;
        }
        dataDetails.lyricsData = lyricsData;
        try {
            mediaMetadataRetriever.release();
        } catch (Exception unused3) {
        }
        return dataDetails;
    }
}
