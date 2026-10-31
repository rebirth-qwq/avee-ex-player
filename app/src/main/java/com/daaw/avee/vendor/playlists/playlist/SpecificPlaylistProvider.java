package com.daaw.avee.vendor.playlists.playlist;

import java.io.InputStream;
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public interface SpecificPlaylistProvider {
    ContentType[] getContentTypes();

    String getId();

    SpecificPlaylist readFrom(InputStream inputStream, String str, Log log) throws Exception;

    SpecificPlaylist toSpecificPlaylist(Playlist playlist) throws Exception;
}
