package com.daaw.avee.comp.playback.Song;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import com.daaw.avee.Common.Action1;
import com.daaw.avee.Common.MediaStoreUtils;
import com.daaw.avee.Common.VAsyncTask;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.AlbumArt.AlbumArtCore;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class PlaylistSong {
    private static final String TAG = "PlaylistSong";
    private static final String TAG2 = "PlaylistSong";
    private volatile AsyncLoadingData asyncLoadingData;
    private Uri idPath = Uri.EMPTY;
    private String providedTitle;
    private volatile Data songData;
    public static final Data emptyData = new Data(Uri.EMPTY);
    private static final Data notsetData = new Data(Uri.EMPTY);
    private static final Data loadingData = new Data(Uri.EMPTY);
    public static final PlaylistSong EmptySong = new PlaylistSong(emptyData);
    private static final String[] mCursorCols = {"_id", "artist", "album", "title", "mime_type", "album_id", "artist_id", "duration"};
    private static String unknown_artist_name = null;
    private static String unknown_album_name = null;

    public interface OnDataReadyListener {
        void onDataReady(Data data, Object obj, Object obj2);
    }

    private PlaylistSong(Data data) {
        this.songData = data;
    }

    public PlaylistSong(Uri uri) {
        Log.d("PlaylistSong", "PlaylistSong(Uri) constructor: " + uri);
        init(uri, null);
    }

    public PlaylistSong(String str) {
        Log.d("PlaylistSong", "PlaylistSong(String) constructor: " + str);
        if (str == null) {
            init(Uri.EMPTY, null);
        } else {
            init(Uri.parse(str), null);
        }
    }

    public PlaylistSong(long j, Uri uri) {
        Log.d("PlaylistSong", "PlaylistSong(long, Uri) constructor: " + uri);
        init(uri, null);
    }

    public PlaylistSong(long j, String str) {
        Log.d("PlaylistSong", "PlaylistSong(long, String) constructor: " + str);
        if (str == null) {
            init(Uri.EMPTY, null);
        } else {
            init(Uri.parse(str), null);
        }
    }

    public PlaylistSong(File file) {
        Log.d("PlaylistSong", "PlaylistSong(File) constructor: " + file);
        init(Uri.fromFile(file), null);
    }

    public PlaylistSong(long j, String str, String str2) {
        Log.d("PlaylistSong", "PlaylistSong(long, String, String) constructor: " + str);
        if (str == null) {
            init(Uri.EMPTY, str2);
        } else {
            init(Uri.parse(str), str2);
        }
    }


    public PlaylistSong(long j, String str, String str2, String str3) {
        init(str == null ? Uri.EMPTY : Uri.parse(str), str2);
    }

    public PlaylistSong(long j, String str, String str2, String str3, String str4) {
        init(str == null ? Uri.EMPTY : Uri.parse(str), str2);
    }

    public static List<PlaylistSong> makeSongListFromDataSourceList(List<String> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(new PlaylistSong(-1L, it.next()));
        }
        return arrayList;
    }

    /* JADX WARN: Code duplicated, block: B:95:0x0279  */
    private static Data AcquireDataMediaStore(Context context, Uri uri, String str) {
        Cursor cursorQuery;
        Cursor cursor;
        Cursor cursor2 = null;
        Log.d("PlaylistSong", "AcquireDataMediaStore: uri=" + uri);
        try {
            if (unknown_artist_name == null) {
                unknown_artist_name = context.getString(R.string.unknown_artist);
                unknown_album_name = context.getString(R.string.unknown_album);
            }
        } catch (Exception e) {
            unknown_artist_name = context.getString(R.string.unknown_artist);
            unknown_album_name = context.getString(R.string.unknown_album);
        }
        Data data = new Data(uri);
        if (uri == null) {
            Log.d("PlaylistSong", "AcquireDataMediaStore: uri is null, returning empty data");
            return data;
        }
        String scheme = uri.getScheme();
        Log.d("PlaylistSong", "AcquireDataMediaStore: scheme=" + scheme);
        if (scheme != null && ("http".equals(scheme) || "https".equals(scheme))) {
            data.isStream = true;
            String lastPathSegment = uri.getLastPathSegment();
            if (lastPathSegment == null || lastPathSegment.isEmpty()) {
                lastPathSegment = "Stream";
            }
            data.trackName = lastPathSegment;
            data.artistName = unknown_artist_name;
            data.albumName = unknown_album_name;
            Log.d("PlaylistSong", "AcquireDataMediaStore: stream mode, trackName=" + data.trackName);
            return data;
        }
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor3 = null;
        try {
            if (uri.toString().startsWith("content://media/external/audio/media/")) {
                Log.d("PlaylistSong", "AcquireDataMediaStore: querying MediaStore");
                cursorQuery = contentResolver.query(uri, mCursorCols, null, null, null);
                if (cursorQuery != null) {
                    try {
                        try {
                            if (cursorQuery.moveToFirst()) {
                                data.audioId = cursorQuery.getLong(cursorQuery.getColumnIndex("_id"));
                                data.trackName = cursorQuery.getString(cursorQuery.getColumnIndex("title"));
                                data.artistName = cursorQuery.getString(cursorQuery.getColumnIndex("artist"));
                                data.albumName = cursorQuery.getString(cursorQuery.getColumnIndex("album"));
                                data.albumId = cursorQuery.getLong(cursorQuery.getColumnIndex("album_id"));
                                data.artistId = cursorQuery.getLong(cursorQuery.getColumnIndex("artist_id"));
                                data.duration = cursorQuery.getInt(cursorQuery.getColumnIndex("duration"));
                                if (data.artistName == null || data.artistName.isEmpty()) {
                                    data.artistName = unknown_artist_name;
                                }
                                if (data.albumName == null || data.albumName.isEmpty()) {
                                    data.albumName = unknown_album_name;
                                }
                                if (data.trackName == null || data.trackName.isEmpty()) {
                                    data.trackName = uri.getLastPathSegment();
                                }
                                try {
                                    DataDetails dataDetailsAcquireDataMediaMetadataRetrieverLocal2 = PlaylistSongMetadataRetriever.AcquireDataMediaMetadataRetrieverLocal2(context, uri, data);
                                    if (dataDetailsAcquireDataMediaMetadataRetrieverLocal2.lyricsData == null || dataDetailsAcquireDataMediaMetadataRetrieverLocal2.lyricsData.type == 0) {
                                        Log.d("PlaylistSong", "AcquireDataMediaStore: no lyrics found");
                                    } else {
                                        data.lyricsData = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.lyricsData;
                                        Log.d("PlaylistSong", "AcquireDataMediaStore: lyrics loaded");
                                    }
                                } catch (Exception e2) {
                                    Log.e("PlaylistSong", "AcquireDataMediaStore: failed to load lyrics", e2);
                                }
                                data.finish(contentResolver);
                                if (cursorQuery != null) {
                                    cursorQuery.close();
                                }
                                Log.d("PlaylistSong", "AcquireDataMediaStore: returning data from MediaStore");
                                if (cursorQuery != null) {
                                    cursorQuery.close();
                                }
                                return data;
                            }
                        } catch (Exception e3) {
                            cursor = cursorQuery;
                            try {
                                Log.e("PlaylistSong", "AcquireDataMediaStore exception", e3);
                                if (cursor != null) {
                                    cursor.close();
                                }
                            } catch (Throwable th) {
                                cursorQuery = cursor;
                                if (cursorQuery != null) {
                                    cursorQuery.close();
                                }
                                throw th;
                            }
                        }
                    } catch (Throwable th2) {
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        throw th2;
                    }
                }
            } else {
                String path = uri.getPath();
                Log.d("PlaylistSong", "AcquireDataMediaStore: not MediaStore URI, path=" + path);
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        data.filePath = path;
                        Log.d("PlaylistSong", "AcquireDataMediaStore: file exists, filePath=" + path);
                        String name = file.getName();
                        int iLastIndexOf = name.lastIndexOf(46);
                        if (iLastIndexOf > 0) {
                            name = name.substring(0, iLastIndexOf);
                        }
                        data.trackName = name;
                        data.artistName = unknown_artist_name;
                        data.albumName = unknown_album_name;
                        data.sizeInBytes = file.length();
                        if (0 != 0) {
                            cursor3.close();
                        }
                        Log.d("PlaylistSong", "AcquireDataMediaStore: returning data from file, trackName=" + name);
                        if (0 != 0) {
                            cursor2.close();
                        }
                        return data;
                    }
                    Log.d("PlaylistSong", "AcquireDataMediaStore: file does not exist: " + path);
                    cursorQuery = null;
                } else {
                    cursorQuery = null;
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Exception e4) {
            cursor = null;
        } catch (Throwable th3) {
            cursorQuery = null;
        }
        String lastPathSegment2 = uri.getLastPathSegment();
        if (lastPathSegment2 == null || lastPathSegment2.isEmpty()) {
            lastPathSegment2 = "Unknown";
        }
        data.trackName = lastPathSegment2;
        data.artistName = unknown_artist_name;
        data.albumName = unknown_album_name;
        Log.d("PlaylistSong", "AcquireDataMediaStore: fallback, trackName=" + data.trackName);
        return data;
    }

    private static Data acquireData(Context context, Uri uri, String str) {
        Log.d("PlaylistSong", "acquireData: uri=" + uri);
        return AcquireDataMediaStore(context, uri, str);
    }

    private static boolean applyDetailsFromMeta(Data data, Context context, Uri uri) {
        Log.d("PlaylistSong", "applyDetailsFromMeta: ENTERED, uri=" + uri);
        DataDetails dataDetailsAcquireDataMediaMetadataRetrieverLocal2 = PlaylistSongMetadataRetriever.AcquireDataMediaMetadataRetrieverLocal2(context, uri, null);
        if (dataDetailsAcquireDataMediaMetadataRetrieverLocal2.trackName.isEmpty()) {
            Log.d("PlaylistSong", "applyDetailsFromMeta: trackName is empty, returning false");
            return false;
        }
        Log.d("PlaylistSong", "applyDetailsFromMeta: trackName=" + dataDetailsAcquireDataMediaMetadataRetrieverLocal2.trackName);
        data.isStream = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.isStream;
        data.trackName = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.trackName;
        data.albumName = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.albumName.isEmpty() ? unknown_album_name : dataDetailsAcquireDataMediaMetadataRetrieverLocal2.albumName;
        data.artistName = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.artistName.isEmpty() ? unknown_artist_name : dataDetailsAcquireDataMediaMetadataRetrieverLocal2.artistName;
        data.duration = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.duration;
        data.trackNum = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.trackNum;
        data.cdNum = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.cdNum;
        data.year = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.year;
        data.lyricsData = dataDetailsAcquireDataMediaMetadataRetrieverLocal2.lyricsData;
        Log.d("PlaylistSong", "applyDetailsFromMeta: lyricsData=" + (data.lyricsData != null ? "not null, type=" + data.lyricsData.type : "null"));
        return true;
    }

    protected static Data doInBackground(Object... objArr) {
        Context context = (Context) objArr[0];
        Uri uri = (Uri) objArr[1];
        String str = (String) objArr[2];
        Log.d("PlaylistSong", "doInBackground: uri=" + uri);
        return acquireData(context, uri, str);
    }

    public void init(Uri uri, String str) {
        Log.d("PlaylistSong", "init: uri=" + uri + ", providedTitle=" + str);
        if (uri == null) {
            uri = Uri.EMPTY;
        }
        this.idPath = uri;
        this.providedTitle = str;
        this.songData = notsetData;
    }

    private void startAcquireData(Handler handler) {
        Log.d("PlaylistSong", "startAcquireData: ENTERED");
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext == null) {
            Log.e("PlaylistSong", "startAcquireData: context is NULL");
            return;
        }
        Log.d("PlaylistSong", "startAcquireData: context OK, calling AcquireData");
        this.songData = loadingData;
        PlaylistSongMetadataRetriever.AcquireData(appContext, this.idPath, this.providedTitle, handler, new Action1<Data>() { // from class: com.daaw.avee.comp.playback.Song.PlaylistSong.1
            @Override // com.daaw.avee.Common.Action1
            public void onInvoke(Data data) {
                PlaylistSong.this.onDataAcquired(data);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataAcquired(Data data) {
        Log.d("PlaylistSong", "onDataAcquired: data=" + (data != null ? "not null, trackName=" + data.trackName : "null"));
        AsyncLoadingData asyncLoadingData = this.asyncLoadingData;
        this.asyncLoadingData = null;
        this.songData = data;
        if (asyncLoadingData != null) {
            asyncLoadingData.listener.onDataReady(this.songData, asyncLoadingData.userData1, asyncLoadingData.userData2);
        }
    }

    public boolean compare(PlaylistSong playlistSong) {
        return playlistSong != null && playlistSong.idPath.equals(this.idPath);
    }

    public boolean equals(Object obj) {
        return (obj instanceof PlaylistSong) && compare((PlaylistSong) obj);
    }

    public int hashCode() {
        return this.idPath.hashCode();
    }

    public String getConstrucPath() {
        return Uri.decode(this.idPath.toString());
    }

    public String getDataSourceForPlaylist() {
        return Uri.decode(this.idPath.toString());
    }

    public long getDataSourceForNativePlaylist() {
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext == null) {
            return 0L;
        }
        long j = getDataBlocking(appContext).audioId;
        if (j <= 0) {
            j = 0;
        }
        return j;
    }

    public IMediaDataSource getMediaDataSource() {
        return new MediaDataSourceOther(this.idPath);
    }

    public boolean isStream() {
        return Data.isUrlStream(this.idPath);
    }

    public Data getData(Handler handler, OnDataReadyListener onDataReadyListener, Object obj, Object obj2) {
        Log.d("PlaylistSong", "getData(handler): ENTERED");
        Data data = this.songData;
        if (data == notsetData) {
            Log.d("PlaylistSong", "getData: notsetData, starting acquire");
            this.asyncLoadingData = new AsyncLoadingData(this, onDataReadyListener, obj, obj2);
            startAcquireData(handler);
            return emptyData;
        }
        if (data == loadingData) {
            Log.d("PlaylistSong", "getData: loadingData");
            this.asyncLoadingData = new AsyncLoadingData(this, onDataReadyListener, obj, obj2);
            return emptyData;
        }
        Log.d("PlaylistSong", "getData: data already loaded");
        this.asyncLoadingData = null;
        return data;
    }

    public Data getData() {
        Log.d("PlaylistSong", "getData(): ENTERED");
        Data data = this.songData;
        if (data == notsetData || data == loadingData) {
            this.asyncLoadingData = null;
            if (data == notsetData) {
                Log.d("PlaylistSong", "getData: notsetData, starting acquire");
                startAcquireData(null);
            }
            return emptyData;
        }
        this.asyncLoadingData = null;
        return data;
    }

    public Data getDataBlocking(Context context) {
        Log.d("PlaylistSong", "getDataBlocking: ENTERED");
        Data data = this.songData;
        if (data == emptyData || data == loadingData || data == notsetData) {
            Log.d("PlaylistSong", "getDataBlocking: data not ready, calling acquireData");
            return acquireData(context, this.idPath, this.providedTitle);
        }
        Log.d("PlaylistSong", "getDataBlocking: data already loaded");
        return data;
    }

    public DataDetails getDataDetailsBlocking(Context context) {
        Log.d("PlaylistSong", "getDataDetailsBlocking: ENTERED");
        return PlaylistSongMetadataRetriever.AcquireDataMediaMetadataRetrieverLocal2(context, this.idPath, getDataBlocking(context));
    }

    public class AsyncLoadingData {
        public final OnDataReadyListener listener;
        public final Object userData1;
        public final Object userData2;

        public AsyncLoadingData(PlaylistSong playlistSong, OnDataReadyListener onDataReadyListener, Object obj, Object obj2) {
            this.listener = onDataReadyListener;
            this.userData1 = obj;
            this.userData2 = obj2;
        }
    }

    public static class Data {
        private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        public long albumId;
        public long audioId;
        public int cdNum;
        public final Uri dataSource;
        public int duration;
        public String filePath;
        public int height;
        public boolean isStream;
        public LyricsData lyricsData;
        public long sizeInBytes;
        public int trackNum;
        public int width;
        public int year;
        public String trackName = "";
        public String albumName = "";
        public String artistName = "";
        public long artistId = -1;
        public String albumArt = "";

        public Data(Uri uri) {
            this.dataSource = uri;
            Log.d("PlaylistSong", "Data constructed: " + uri);
        }

        public static boolean isUrlStream(Uri uri) {
            String scheme = uri.getScheme();
            return "http".equals(scheme) || "https".equals(scheme);
        }

        public void finish(ContentResolver contentResolver) throws Throwable {
            Cursor cursorQuerySafe;
            this.albumArt = "";
            if (contentResolver != null && AlbumArtCore.THUMB_FROM_RESOLVER) {
                try {
                    cursorQuerySafe = MediaStoreUtils.querySafe(contentResolver, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album_art"}, "_id=?", new String[]{String.valueOf(this.albumId)}, null);
                    if (cursorQuerySafe != null) {
                        try {
                            if (cursorQuerySafe.moveToFirst()) {
                                this.albumArt = cursorQuerySafe.getString(cursorQuerySafe.getColumnIndex("album_art"));
                            }
                        } catch (Throwable th) {
                            if (cursorQuerySafe != null) {
                                cursorQuerySafe.close();
                            }
                            throw th;
                        }
                    }
                    if (cursorQuerySafe != null) {
                        cursorQuerySafe.close();
                    }
                } catch (Throwable th2) {
                    cursorQuerySafe = null;
                }
            }
        }

        public boolean isArtistKnown() {
            return this.artistId > 0;
        }

        public boolean isAlbumKnown() {
            return this.artistId > 0;
        }

        public boolean isArtistKnownOrSecondName() {
            long j = this.artistId;
            return j > 0 || j == -2;
        }

        public String getAlbumArtPath0Str() {
            String albumArtPath1Str = getAlbumArtPath1Str();
            return (albumArtPath1Str == null || albumArtPath1Str.length() <= 0) ? getAlbumArtPath00Str() : albumArtPath1Str;
        }

        public String getAlbumArtPath00Str() {
            if (this.audioId <= 0) {
                return null;
            }
            return "content://media/external/audio/media/" + this.audioId + "/albumart";
        }

        public String getAlbumArtPath1Str() {
            return this.albumArt;
        }

        public String getAlbumArtGenerateStr() {
            if (isArtistKnown() && this.artistName.length() > 1) {
                return this.artistName;
            }
            String str = this.trackName;
            int iIndexOf = str.indexOf(45);
            if (iIndexOf < 3) {
                iIndexOf = str.indexOf("_-_");
            }
            if (iIndexOf < 3) {
                if (str.indexOf(32) >= 0) {
                    iIndexOf = str.indexOf(95);
                } else {
                    iIndexOf = str.indexOf("__");
                }
            }
            if (iIndexOf < 3) {
                return str;
            }
            if (str.charAt(iIndexOf - 1) == ' ') {
                iIndexOf--;
            }
            return str.substring(0, iIndexOf);
        }

        public int getDurationSeconds() {
            return this.duration / 1000;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Data) {
                return this.dataSource.equals(((Data) obj).dataSource);
            }
            return false;
        }
    }

    public static class DataDetails {
        public String albumArtist;
        public String albumName;
        public String artistName;
        public int bitRate;
        public int cdNum;
        public String composer;
        public final Data data;
        public int duration;
        public int height;
        public boolean isStream;
        public LyricsData lyricsData;
        public String mimeType;
        public String secondName;
        public String trackName;
        public int trackNum;
        public int width;
        public int year;

        public DataDetails(Data data) {
            this.data = data;
            Log.d("PlaylistSong", "DataDetails constructed");
        }

        public int getDuration() {
            return this.data.duration;
        }
    }

    private static class PlaylistSongOwnSerialExecutor implements Executor {
        private Runnable active;
        private final ArrayDeque<Runnable> tasks = new ArrayDeque<>();

        private PlaylistSongOwnSerialExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public synchronized void execute(final Runnable runnable) {
            this.tasks.offer(new Runnable() { // from class: com.daaw.avee.comp.playback.Song.PlaylistSong.PlaylistSongOwnSerialExecutor.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        runnable.run();
                    } finally {
                        PlaylistSongOwnSerialExecutor.this.scheduleNext();
                    }
                }
            });
            if (this.active == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            this.active = this.tasks.poll();
            if (this.active != null) {
                VAsyncTask.THREAD_POOL_EXECUTOR_2.execute(this.active);
            }
        }
    }
}
