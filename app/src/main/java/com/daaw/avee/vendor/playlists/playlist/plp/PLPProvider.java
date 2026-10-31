package com.daaw.avee.vendor.playlists.playlist.plp;

import com.daaw.avee.vendor.playlists.player.PlayerSupport;
import com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent;
import com.daaw.avee.vendor.playlists.playlist.Media;
import com.daaw.avee.vendor.playlists.playlist.Parallel;
import com.daaw.avee.vendor.playlists.playlist.Playlist;
import com.daaw.avee.vendor.playlists.playlist.Sequence;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider;
import com.mpatric.mp3agic.EncodedText;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public class PLPProvider implements SpecificPlaylistProvider {
    private static final ContentType[] FILETYPES = {new ContentType(new String[]{".plp"}, new String[]{"text/plain"}, new PlayerSupport[0], "Sansa Playlist File")};

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public String getId() {
        return "plp";
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public ContentType[] getContentTypes() {
        return (ContentType[]) FILETYPES.clone();
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist readFrom(InputStream inputStream, String str, Log log) throws Exception {
        if (str == null) {
            str = EncodedText.CHARSET_UTF_16;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, str));
        PLP plp = new PLP();
        plp.setProvider(this);
        String str2 = null;
        boolean z = false;
        boolean z2 = false;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            String strTrim = line.trim();
            if (strTrim.length() > 0) {
                if (z) {
                    if (!z2) {
                        if (!"VERSION 1.20".equals(strTrim)) {
                            log.error("Malformed PLP playlist (no version information)");
                            plp = null;
                            break;
                        }
                        z2 = true;
                    } else {
                        int iIndexOf = strTrim.indexOf(44);
                        if (iIndexOf <= 0) {
                            log.error("Malformed PLP playlist (playlist entry line format)");
                        } else {
                            String strTrim2 = strTrim.substring(0, iIndexOf).trim();
                            if (str2 == null) {
                                str2 = strTrim2;
                            } else if (!str2.equals(strTrim2)) {
                                log.error("Malformed PLP playlist (inconsistent disk specifier)");
                            }
                            plp.getFilenames().add(strTrim.substring(iIndexOf + 1).trim());
                        }
                        plp = null;
                        break;
                    }
                } else {
                    if (!"PLP PLAYLIST".equals(strTrim)) {
                        throw new IllegalArgumentException("Not a PLP playlist format");
                    }
                    z = true;
                }
            }
        }
        if (plp != null && str2 != null) {
            plp.setDiskSpecifier(str2);
        }
        return plp;
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist toSpecificPlaylist(Playlist playlist) throws Exception {
        PLP plp = new PLP();
        plp.setProvider(this);
        addToPlaylist(plp.getFilenames(), playlist.getRootSequence());
        return plp;
    }

    private void addToPlaylist(List<String> list, AbstractPlaylistComponent abstractPlaylistComponent) throws Exception {
        if (abstractPlaylistComponent instanceof Sequence) {
            Sequence sequence = (Sequence) abstractPlaylistComponent;
            if (sequence.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A PLP playlist cannot handle a sequence repeated indefinitely");
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
            throw new IllegalArgumentException("A parallel time container is incompatible with a PLP playlist");
        }
        if (abstractPlaylistComponent instanceof Media) {
            Media media = (Media) abstractPlaylistComponent;
            if (media.getDuration() != null) {
                throw new IllegalArgumentException("A PLP playlist cannot handle a timed media");
            }
            if (media.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A PLP playlist cannot handle a media repeated indefinitely");
            }
            if (media.getSource() != null) {
                for (int i2 = 0; i2 < media.getRepeatCount(); i2++) {
                    list.add(media.getSource().toString());
                }
            }
        }
    }
}
