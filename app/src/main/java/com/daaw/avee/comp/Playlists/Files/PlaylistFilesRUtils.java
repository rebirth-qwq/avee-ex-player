package com.daaw.avee.comp.Playlists.Files;

import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.kpl.Entry;
import com.daaw.avee.vendor.playlists.playlist.kpl.Xml;
import com.daaw.avee.vendor.playlists.playlist.m3u.M3U;
import com.daaw.avee.vendor.playlists.playlist.m3u.Resource;
import com.daaw.avee.vendor.playlists.playlist.mpcpl.MPCPL;
import com.daaw.avee.vendor.playlists.playlist.pla.PLA;
import com.daaw.avee.vendor.playlists.playlist.plp.PLP;
import com.daaw.avee.vendor.playlists.playlist.pls.PLS;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PlaylistFilesRUtils {

    public static class ReadParameters {
        public String playlistPath;
    }

    static String makeSongPathAbsolute(String str, String str2) {
        return str;
    }

    static int readFromSpecificPlaylist(SpecificPlaylist specificPlaylist, ReadParameters readParameters, List<PlaylistSong> list) {
        if (specificPlaylist == null) {
            return 0;
        }
        if (specificPlaylist instanceof PLA) {
            return readFromSpecificPlaylist((PLA) specificPlaylist, readParameters, list);
        }
        if (specificPlaylist instanceof Xml) {
            return readFromSpecificPlaylist((Xml) specificPlaylist, readParameters, list);
        }
        if (specificPlaylist instanceof PLS) {
            return readFromSpecificPlaylist((PLS) specificPlaylist, readParameters, list);
        }
        if (specificPlaylist instanceof MPCPL) {
            return readFromSpecificPlaylist((MPCPL) specificPlaylist, readParameters, list);
        }
        if (specificPlaylist instanceof PLP) {
            return readFromSpecificPlaylist((PLP) specificPlaylist, readParameters, list);
        }
        if (specificPlaylist instanceof M3U) {
            return readFromSpecificPlaylist((M3U) specificPlaylist, readParameters, list);
        }
        return 0;
    }

    static int readFromSpecificPlaylist(PLA pla, ReadParameters readParameters, List<PlaylistSong> list) {
        List<String> filenames = pla.getFilenames();
        Iterator<String> it = filenames.iterator();
        while (it.hasNext()) {
            list.add(makePlaylistSong(-1L, Utils.fixIncludedNullTerminatorString(it.next()), readParameters));
        }
        return filenames.size();
    }

    static int readFromSpecificPlaylist(Xml xml, ReadParameters readParameters, List<PlaylistSong> list) {
        List<Entry> entries = xml.getEntries();
        Iterator<Entry> it = entries.iterator();
        while (it.hasNext()) {
            list.add(makePlaylistSong(-1L, it.next().getFilename(), readParameters));
        }
        return entries.size();
    }

    static int readFromSpecificPlaylist(PLS pls, ReadParameters readParameters, List<PlaylistSong> list) {
        List<Resource> resources = pls.getResources();
        for (Resource resource : resources) {
            list.add(makePlaylistSong(-1L, resource.getLocation(), resource.getName(), null, readParameters));
        }
        return resources.size();
    }

    static int readFromSpecificPlaylist(MPCPL mpcpl, ReadParameters readParameters, List<PlaylistSong> list) {
        List<com.daaw.avee.vendor.playlists.playlist.mpcpl.Resource> resources = mpcpl.getResources();
        for (com.daaw.avee.vendor.playlists.playlist.mpcpl.Resource resource : resources) {
            list.add(makePlaylistSong(-1L, resource.getFilename(), null, resource.getSubtitle(), readParameters));
        }
        return resources.size();
    }

    static int readFromSpecificPlaylist(PLP plp, ReadParameters readParameters, List<PlaylistSong> list) {
        List<String> filenames = plp.getFilenames();
        Iterator<String> it = filenames.iterator();
        while (it.hasNext()) {
            list.add(makePlaylistSong(-1L, it.next(), readParameters));
        }
        return filenames.size();
    }

    static int readFromSpecificPlaylist(M3U m3u, ReadParameters readParameters, List<PlaylistSong> list) {
        m3u.setExtensionM3U(true);
        List<Resource> resources = m3u.getResources();
        for (Resource resource : resources) {
            list.add(makePlaylistSong(-1L, resource.getLocation(), resource.getName(), null, readParameters));
        }
        return resources.size();
    }

    static PlaylistSong makePlaylistSong(long j, String str, ReadParameters readParameters) {
        return new PlaylistSong(j, makeSongPath(str, readParameters.playlistPath));
    }

    static PlaylistSong makePlaylistSong(long j, String str, String str2, String str3, ReadParameters readParameters) {
        return new PlaylistSong(j, makeSongPath(str, readParameters.playlistPath), str2, str3);
    }

    static String makeSongPath(String str, String str2) {
        if (str.startsWith("/")) {
            return makeSongPathAbsolute(str, str2);
        }
        if (str.startsWith("\\")) {
            return makeSongPathAbsolute(str, str2);
        }
        if (str.startsWith("..")) {
            String strMakeSongPathRelative = makeSongPathRelative(str, str2);
            return strMakeSongPathRelative == null ? makeSongPathAbsolute(str, str2) : strMakeSongPathRelative;
        }
        if (str.contains(":")) {
            return makeSongPathAbsolute(str, str2);
        }
        String strMakeSongPathRelative2 = makeSongPathRelative(str, str2);
        return strMakeSongPathRelative2 == null ? makeSongPathAbsolute(str, str2) : strMakeSongPathRelative2;
    }

    static String makeSongPathRelative(String str, String str2) {
        File parentFile;
        File file = new File(str2);
        if (!file.isDirectory() && (parentFile = file.getParentFile()) != null) {
            file = parentFile;
        }
        File file2 = new File(file, str);
        if (!file2.exists()) {
            return null;
        }
        try {
            return file2.getCanonicalPath();
        } catch (IOException unused) {
            return file2.getAbsolutePath();
        }
    }
}
