package com.daaw.avee.vendor.playlists.playlist.pls;

import com.daaw.avee.vendor.playlists.player.PlayerSupport;
import com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent;
import com.daaw.avee.vendor.playlists.playlist.Media;
import com.daaw.avee.vendor.playlists.playlist.Parallel;
import com.daaw.avee.vendor.playlists.playlist.Playlist;
import com.daaw.avee.vendor.playlists.playlist.Sequence;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider;
import com.daaw.avee.vendor.playlists.playlist.m3u.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public class PLSProvider implements SpecificPlaylistProvider {
    private static final ContentType[] FILETYPES = {new ContentType(new String[]{".pls"}, new String[]{"audio/x-scpls"}, new PlayerSupport[]{new PlayerSupport(PlayerSupport.Player.WINAMP, true, null), new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, false, null), new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null), new PlayerSupport(PlayerSupport.Player.FOOBAR2000, false, null), new PlayerSupport(PlayerSupport.Player.MPLAYER, true, null), new PlayerSupport(PlayerSupport.Player.QUICKTIME, true, null), new PlayerSupport(PlayerSupport.Player.ITUNES, true, null), new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null)}, "Winamp PLSv2 Playlist")};

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public String getId() {
        return "pls";
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
        PLS pls = new PLS();
        pls.setProvider(this);
        int i = -1;
        boolean z = false;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            String strTrim = line.trim();
            if (strTrim.length() > 0) {
                if (!z) {
                    if (!strTrim.equalsIgnoreCase("[playlist]")) {
                        throw new IllegalArgumentException("Not a PLS playlist format");
                    }
                    z = true;
                } else {
                    int iIndexOf = strTrim.indexOf(61);
                    if (iIndexOf <= 0) {
                        log.error("Malformed PLS playlist");
                    } else {
                        String lowerCase = strTrim.substring(0, iIndexOf).trim().toLowerCase();
                        String strTrim2 = strTrim.substring(iIndexOf + 1).trim();
                        if ("numberofentries".equals(lowerCase)) {
                            try {
                                int i2 = Integer.parseInt(strTrim2);
                                if (i2 < 0) {
                                    log.warn("Invalid NumberOfEntries in PLS playlist: " + i2);
                                } else if (i < 0 || i == i2) {
                                    i = i2;
                                } else {
                                    log.error("PLS playlist number of entries already specified with a different value");
                                }
                            } catch (NumberFormatException e) {
                                log.error(e.toString());
                            }
                        } else if (lowerCase.startsWith("file")) {
                            try {
                                int i3 = Integer.parseInt(lowerCase.substring(4)) - 1;
                                for (int size = pls.getResources().size(); size < i3 + 1; size++) {
                                    pls.getResources().add(new Resource());
                                }
                                pls.getResources().get(i3).setLocation(strTrim2);
                            } catch (NumberFormatException e2) {
                                log.error(e2.toString());
                            }
                        } else if (lowerCase.startsWith("title")) {
                            try {
                                int i4 = Integer.parseInt(lowerCase.substring(5)) - 1;
                                for (int size2 = pls.getResources().size(); size2 < i4 + 1; size2++) {
                                    pls.getResources().add(new Resource());
                                }
                                pls.getResources().get(i4).setName(strTrim2);
                            } catch (NumberFormatException e3) {
                                log.error(e3.toString());
                            }
                        } else if (lowerCase.startsWith("length")) {
                            try {
                                int i5 = Integer.parseInt(lowerCase.substring(6)) - 1;
                                for (int size3 = pls.getResources().size(); size3 < i5 + 1; size3++) {
                                    pls.getResources().add(new Resource());
                                }
                                try {
                                    pls.getResources().get(i5).setLength(Long.parseLong(strTrim2));
                                } catch (NumberFormatException e4) {
                                    log.error(e4.toString());
                                }
                            } catch (NumberFormatException e5) {
                                log.error(e5.toString());
                            }
                        } else if (!"version".equals(lowerCase)) {
                            log.warn("Unknown PLS keyword " + lowerCase);
                        } else if (!"2".equals(strTrim2)) {
                            log.error("Unknown PLS version " + strTrim2);
                        }
                    }
                    pls = null;
                    break;
                }
            }
        }
        if (pls != null) {
            if (i < 0) {
                log.warn("No number of entries in PLS playlist");
            } else {
                int size4 = pls.getResources().size() - i;
                if (size4 > 0) {
                    log.warn("Ignoring " + size4 + " extra resources according to the specified number of entries " + i);
                }
                for (int i6 = 0; i6 < size4; i6++) {
                    pls.getResources().remove(i);
                }
            }
        }
        return pls;
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider
    public SpecificPlaylist toSpecificPlaylist(Playlist playlist) throws Exception {
        PLS pls = new PLS();
        pls.setProvider(this);
        addToPlaylist(pls.getResources(), playlist.getRootSequence());
        return pls;
    }

    private void addToPlaylist(List<Resource> list, AbstractPlaylistComponent abstractPlaylistComponent) throws Exception {
        if (abstractPlaylistComponent instanceof Sequence) {
            Sequence sequence = (Sequence) abstractPlaylistComponent;
            if (sequence.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A PLS playlist cannot handle a sequence repeated indefinitely");
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
            throw new IllegalArgumentException("A parallel time container is incompatible with a PLS playlist");
        }
        if (abstractPlaylistComponent instanceof Media) {
            Media media = (Media) abstractPlaylistComponent;
            if (media.getDuration() != null) {
                throw new IllegalArgumentException("A PLS playlist cannot handle a timed media");
            }
            if (media.getRepeatCount() < 0) {
                throw new IllegalArgumentException("A PLS playlist cannot handle a media repeated indefinitely");
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
