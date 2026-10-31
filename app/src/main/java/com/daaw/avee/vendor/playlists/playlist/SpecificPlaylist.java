package com.daaw.avee.vendor.playlists.playlist;

import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public interface SpecificPlaylist {
    SpecificPlaylistProvider getProvider();

    void setProvider(SpecificPlaylistProvider specificPlaylistProvider);

    Playlist toPlaylist();

    void writeTo(OutputStream outputStream, String str) throws Exception;
}
