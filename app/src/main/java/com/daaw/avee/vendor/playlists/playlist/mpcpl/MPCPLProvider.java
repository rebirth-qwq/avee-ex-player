package com.daaw.avee.vendor.playlists.playlist.mpcpl;

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
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public class MPCPLProvider implements SpecificPlaylistProvider {
    private static final ContentType[] FILETYPES = {new ContentType(new String[]{".mpcpl"}, new String[]{"text/plain"}, new PlayerSupport[]{new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null)}, "Media Player Classic Playlist")};

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public String getId() {
        return "mpcpl";
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public ContentType[] getContentTypes() {
        return (ContentType[]) FILETYPES.clone();
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist readFrom(InputStream inputStream, String str, Log log) throws Exception {
        if (str == null) {
            str = "UTF-8";
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, str));
        MPCPL mpcpl = new MPCPL();
        mpcpl.setProvider(this);
        boolean z = false;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return mpcpl;
            }
            String strTrim = line.trim();
            if (strTrim.length() > 0) {
                if (!z) {
                    if (!strTrim.equalsIgnoreCase("MPCPLAYLIST")) {
                        throw new IllegalArgumentException("Not a MPCPL playlist format");
                    }
                    z = true;
                } else {
                    int iIndexOf = strTrim.indexOf(44);
                    if (iIndexOf <= 0) {
                        log.error("Malformed MPCPL playlist entry " + strTrim);
                    } else {
                        String strTrim2 = strTrim.substring(0, iIndexOf).trim();
                        String strSubstring = strTrim.substring(iIndexOf + 1);
                        int iIndexOf2 = strSubstring.indexOf(44);
                        if (iIndexOf2 <= 0) {
                            log.error("Malformed MPCPL playlist entry " + strSubstring);
                        } else {
                            String lowerCase = strSubstring.substring(0, iIndexOf2).trim().toLowerCase();
                            String strTrim3 = strSubstring.substring(iIndexOf2 + 1).trim();
                            try {
                                int i = Integer.parseInt(strTrim2) - 1;
                                for (int size = mpcpl.getResources().size(); size < i + 1; size++) {
                                    mpcpl.getResources().add(new Resource());
                                }
                                Resource resource = mpcpl.getResources().get(i);
                                if ("filename".equals(lowerCase)) {
                                    resource.setFilename(strTrim3);
                                } else if ("type".equals(lowerCase)) {
                                    resource.setType(strTrim3);
                                } else if ("subtitle".equals(lowerCase)) {
                                    resource.setSubtitle(strTrim3);
                                } else {
                                    log.warn("Unknown MPCPL keyword " + lowerCase);
                                }
                            } catch (NumberFormatException e) {
                                log.error(e.toString());
                            }
                        }
                    }
                    return null;
                }
            }
        }
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist toSpecificPlaylist(Playlist playlist) throws Exception {
        MPCPL mpcpl = new MPCPL();
        mpcpl.setProvider(this);
        addToPlaylist(mpcpl.getResources(), playlist.getRootSequence());
        return mpcpl;
    }

    private void addToPlaylist(List<Resource> list, AbstractPlaylistComponent abstractPlaylistComponent) throws Exception {
        if (abstractPlaylistComponent instanceof Sequence) {
            Sequence sequence = (Sequence) abstractPlaylistComponent;
            if (sequence.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A MPCPL playlist cannot handle a sequence repeated indefinitely");
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
            throw new IllegalArgumentException("A parallel time container is incompatible with a MPCPL playlist");
        }
        if (abstractPlaylistComponent instanceof Media) {
            Media media = (Media) abstractPlaylistComponent;
            if (media.getDuration() != null) {
                throw new IllegalArgumentException("A MPCPL playlist cannot handle a timed media");
            }
            if (media.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A MPCPL playlist cannot handle a media repeated indefinitely");
            }
            if (media.getSource() != null) {
                for (int i2 = 0; i2 < media.getRepeatCount(); i2++) {
                    Resource resource = new Resource();
                    resource.setFilename(media.getSource().toString());
                    list.add(resource);
                }
            }
        }
    }
}
