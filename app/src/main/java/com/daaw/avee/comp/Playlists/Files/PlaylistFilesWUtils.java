package com.daaw.avee.comp.Playlists.Files;

import android.content.Context;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.kpl.Entry;
import com.daaw.avee.vendor.playlists.playlist.kpl.Xml;
import com.daaw.avee.vendor.playlists.playlist.m3u.M3U;
import com.daaw.avee.vendor.playlists.playlist.m3u.Resource;
import com.daaw.avee.vendor.playlists.playlist.mpcpl.MPCPL;
import com.daaw.avee.vendor.playlists.playlist.pla.PLA;
import com.daaw.avee.vendor.playlists.playlist.plp.PLP;
import com.daaw.avee.vendor.playlists.playlist.pls.PLS;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PlaylistFilesWUtils {

    public static class AppendParameters {
        public String playlistPath;
        public boolean writeRelativePaths;
    }

    static int appendToSpecificPlaylist(SpecificPlaylist specificPlaylist, AppendParameters appendParameters, List<PlaylistSong> list) {
        if (list == null || specificPlaylist == null) {
            return 0;
        }
        if (specificPlaylist instanceof PLA) {
            return appendToSpecificPlaylist((PLA) specificPlaylist, appendParameters, list);
        }
        if (specificPlaylist instanceof Xml) {
            return appendToSpecificPlaylist((Xml) specificPlaylist, appendParameters, list);
        }
        if (specificPlaylist instanceof PLS) {
            return appendToSpecificPlaylist((PLS) specificPlaylist, appendParameters, list);
        }
        if (specificPlaylist instanceof MPCPL) {
            return appendToSpecificPlaylist((MPCPL) specificPlaylist, appendParameters, list);
        }
        if (specificPlaylist instanceof PLP) {
            return appendToSpecificPlaylist((PLP) specificPlaylist, appendParameters, list);
        }
        if (specificPlaylist instanceof M3U) {
            return appendToSpecificPlaylist((M3U) specificPlaylist, appendParameters, list);
        }
        return 0;
    }

    static int appendToSpecificPlaylist(PLA pla, AppendParameters appendParameters, List<PlaylistSong> list) {
        Iterator<PlaylistSong> it = list.iterator();
        while (it.hasNext()) {
            pla.getFilenames().add(getDataPathForPlaylist(it.next(), appendParameters.playlistPath, appendParameters.writeRelativePaths));
        }
        return list.size();
    }

    static int appendToSpecificPlaylist(Xml xml, AppendParameters appendParameters, List<PlaylistSong> list) {
        for (PlaylistSong playlistSong : list) {
            Entry entry = new Entry();
            entry.setFilename(getDataPathForPlaylist(playlistSong, appendParameters.playlistPath, appendParameters.writeRelativePaths));
            xml.getEntries().add(entry);
        }
        return list.size();
    }

    static int appendToSpecificPlaylist(PLS pls, AppendParameters appendParameters, List<PlaylistSong> list) {
        Context appContext = PlayerCore.s().getAppContext();
        for (PlaylistSong playlistSong : list) {
            Resource resource = new Resource();
            resource.setLocation(getDataPathForPlaylist(playlistSong, appendParameters.playlistPath, appendParameters.writeRelativePaths));
            if (appContext != null) {
                PlaylistSong.Data dataBlocking = playlistSong.getDataBlocking(appContext);
                resource.setName(dataBlocking.trackName);
                resource.setLength(dataBlocking.getDurationSeconds());
            }
            pls.getResources().add(resource);
        }
        return list.size();
    }

    static int appendToSpecificPlaylist(MPCPL mpcpl, AppendParameters appendParameters, List<PlaylistSong> list) {
        for (PlaylistSong playlistSong : list) {
            com.daaw.avee.vendor.playlists.playlist.mpcpl.Resource resource = new com.daaw.avee.vendor.playlists.playlist.mpcpl.Resource();
            resource.setFilename(getDataPathForPlaylist(playlistSong, appendParameters.playlistPath, appendParameters.writeRelativePaths));
            mpcpl.getResources().add(resource);
        }
        return list.size();
    }

    static int appendToSpecificPlaylist(PLP plp, AppendParameters appendParameters, List<PlaylistSong> list) {
        Iterator<PlaylistSong> it = list.iterator();
        while (it.hasNext()) {
            plp.getFilenames().add(getDataPathForPlaylist(it.next(), appendParameters.playlistPath, appendParameters.writeRelativePaths));
        }
        return list.size();
    }

    static int appendToSpecificPlaylist(M3U m3u, AppendParameters appendParameters, List<PlaylistSong> list) {
        Context appContext = PlayerCore.s().getAppContext();
        m3u.setExtensionM3U(true);
        for (PlaylistSong playlistSong : list) {
            Resource resource = new Resource();
            resource.setLocation(getDataPathForPlaylist(playlistSong, appendParameters.playlistPath, appendParameters.writeRelativePaths));
            if (appContext != null) {
                PlaylistSong.Data dataBlocking = playlistSong.getDataBlocking(appContext);
                resource.setName(dataBlocking.trackName);
                resource.setLength(dataBlocking.getDurationSeconds());
            }
            m3u.getResources().add(resource);
        }
        return list.size();
    }

    static String getDataPathForPlaylist(PlaylistSong playlistSong, String str, boolean z) {
        String dataSourceForPlaylist = playlistSong.getDataSourceForPlaylist();
        if (dataSourceForPlaylist.startsWith("file://") && dataSourceForPlaylist.length() > 7) {
            dataSourceForPlaylist = dataSourceForPlaylist.substring(7, dataSourceForPlaylist.length());
        }
        return (z && new File(dataSourceForPlaylist).exists()) ? UtilsFileSys.getRelativePath(dataSourceForPlaylist, str, "/") : dataSourceForPlaylist;
    }
}
