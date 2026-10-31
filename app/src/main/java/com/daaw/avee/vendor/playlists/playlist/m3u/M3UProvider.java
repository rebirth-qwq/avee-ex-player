package com.daaw.avee.vendor.playlists.playlist.m3u;

import com.daaw.avee.vendor.playlists.player.PlayerSupport;
import com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent;
import com.daaw.avee.vendor.playlists.playlist.Media;
import com.daaw.avee.vendor.playlists.playlist.Parallel;
import com.daaw.avee.vendor.playlists.playlist.Playlist;
import com.daaw.avee.vendor.playlists.playlist.Sequence;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public class M3UProvider implements SpecificPlaylistProvider {
    private static final ContentType[] FILETYPES = {new ContentType(new String[]{".m3u"}, new String[]{"audio/x-mpegurl", "audio/mpegurl"}, new PlayerSupport[]{new PlayerSupport(PlayerSupport.Player.WINAMP, true, null), new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, true, null), new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, true, null), new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null), new PlayerSupport(PlayerSupport.Player.FOOBAR2000, true, null), new PlayerSupport(PlayerSupport.Player.MPLAYER, true, null), new PlayerSupport(PlayerSupport.Player.QUICKTIME, true, null), new PlayerSupport(PlayerSupport.Player.ITUNES, true, null), new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null)}, "Winamp M3U"), new ContentType(new String[]{".m3u8"}, new String[]{"audio/x-mpegurl", "audio/mpegurl"}, new PlayerSupport[]{new PlayerSupport(PlayerSupport.Player.WINAMP, true, null), new PlayerSupport(PlayerSupport.Player.FOOBAR2000, true, null)}, "Winamp M3U8"), new ContentType(new String[]{".m4u"}, new String[]{"video/x-mpegurl"}, new PlayerSupport[0], "M4U Playlist"), new ContentType(new String[]{".ram"}, new String[]{"audio/vnd.rn-realaudio", "audio/x-pn-realaudio"}, new PlayerSupport[]{new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null), new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null)}, "Real Audio Metadata (RAM)")};

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public String getId() {
        return "m3u";
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public ContentType[] getContentTypes() {
        return (ContentType[]) FILETYPES.clone();
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist readFrom(InputStream inputStream, String str, Log log) throws Exception {
        String strTrim;
        if (str == null) {
            str = "UTF-8";
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, str));
        M3U m3u = new M3U();
        m3u.setProvider(this);
        while (true) {
            String strSubstring = null;
            String strTrim2 = null;
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return m3u;
                }
                strTrim = line.trim();
                if (strTrim.length() > 0) {
                    char cCharAt = strTrim.charAt(0);
                    if (cCharAt == '<' || cCharAt == '[') {
                        throw new IllegalArgumentException("Doesn't seem to be a M3U playlist (and related ones)");
                    }
                    if (cCharAt == '#') {
                        if (strTrim.toUpperCase(Locale.ENGLISH).startsWith("#EXTINF")) {
                            int iIndexOf = strTrim.indexOf(44, 0);
                            if (iIndexOf >= 0) {
                                strSubstring = strTrim.substring(iIndexOf + 1, strTrim.length());
                            }
                            int iIndexOf2 = strTrim.indexOf(58, 0);
                            if (iIndexOf2 >= 0 && iIndexOf2 < iIndexOf) {
                                strTrim2 = strTrim.substring(iIndexOf2 + 1, iIndexOf).trim();
                            }
                        }
                    } else {
                        // 非注释行，跳出内层循环处理为资源
                        break;
                    }
                }
            }
            Resource resource = new Resource();
            resource.setLocation(strTrim);
            resource.setName(strSubstring);
            if (strTrim2 != null) {
                resource.setLength(Long.parseLong(strTrim2));
            }
            m3u.getResources().add(resource);
        }
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist toSpecificPlaylist(Playlist playlist) throws Exception {
        M3U m3u = new M3U();
        m3u.setProvider(this);
        addToPlaylist(m3u.getResources(), playlist.getRootSequence());
        return m3u;
    }

    private void addToPlaylist(List<Resource> list, AbstractPlaylistComponent abstractPlaylistComponent) throws Exception {
        if (abstractPlaylistComponent instanceof Sequence) {
            Sequence sequence = (Sequence) abstractPlaylistComponent;
            if (sequence.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A M3U playlist cannot handle a sequence repeated indefinitely");
            }
            AbstractPlaylistComponent[] components = sequence.getComponents();
            for (int i = 0; i < sequence.getRepeatCount(); i++) {
                for (AbstractPlaylistComponent abstractPlaylistComponent2 : components) {
                    addToPlaylist(list, abstractPlaylistComponent2);
                }
            }
            return;
        }
        if (abstractPlaylistComponent instanceof Parallel) {
            throw new IllegalArgumentException("A parallel time container is incompatible with a M3U playlist");
        }
        if (abstractPlaylistComponent instanceof Media) {
            Media media = (Media) abstractPlaylistComponent;
            if (media.getDuration() != null) {
                throw new IllegalArgumentException("A M3U playlist cannot handle a timed media");
            }
            if (media.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A M3U playlist cannot handle a media repeated indefinitely");
            }
            if (media.getSource() != null) {
                for (int i2 = 0; i2 < media.getRepeatCount(); i2++) {
                    Resource resource = new Resource();
                    resource.setLocation(media.getSource().toString());
                    if (media.getSource().getDuration() >= 0) {
                        resource.setLength((media.getSource().getDuration() + 999) / 1000);
                    }
                    list.add(resource);
                }
            }
        }
    }
}
