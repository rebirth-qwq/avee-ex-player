package com.daaw.avee.comp.Playlists.Files;

import com.daaw.avee.vendor.playlists.playlist.Media;
import com.daaw.avee.vendor.playlists.playlist.Playlist;
import com.daaw.avee.vendor.playlists.playlist.Sequence;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider;
import com.daaw.avee.vendor.playlists.playlist.m3u.M3U;
import com.daaw.avee.vendor.playlists.playlist.m3u.M3UProvider;
import com.daaw.avee.vendor.playlists.playlist.mpcpl.MPCPLProvider;
import com.daaw.avee.vendor.playlists.playlist.pla.PLAProvider;
import com.daaw.avee.vendor.playlists.playlist.plp.PLPProvider;
import com.daaw.avee.vendor.playlists.playlist.pls.PLSProvider;
import com.daaw.avee.Common.StatusMsg;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.daaw.avee.vendor.playlists.content.Content;
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.playlists.logging.LogFactory;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public class PlaylistFilesUtils {
    private static PlaylistFilesUtils instance;
    private final Log logger = LogFactory.getLog(getClass());
    private Iterable<SpecificPlaylistProvider> serviceLoader;

    public PlaylistFilesUtils() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PLAProvider());
        arrayList.add(new PLSProvider());
        arrayList.add(new MPCPLProvider());
        arrayList.add(new PLPProvider());
        arrayList.add(new M3UProvider());
        this.serviceLoader = arrayList;
    }

    public static PlaylistFilesUtils s() {
        if (instance == null) {
            instance = new PlaylistFilesUtils();
        }
        return instance;
    }

    public static String makePlaylistPath(String str, String str2, PlaylistFilesType playlistFilesType) {
        if (str == null || str.length() <= 0) {
            str = "//";
        } else if (str.charAt(str.length() - 1) != '/') {
            str = str + "/";
        }
        return str + str2 + "." + playlistFilesType.fileExtension;
    }

    private SpecificPlaylist myReadFrom(URL url) throws IOException {
        for (SpecificPlaylistProvider specificPlaylistProvider : this.serviceLoader) {
            URLConnection uRLConnectionOpenConnection = url.openConnection();
            uRLConnectionOpenConnection.setAllowUserInteraction(false);
            uRLConnectionOpenConnection.setConnectTimeout(10000);
            uRLConnectionOpenConnection.setDoInput(true);
            uRLConnectionOpenConnection.setDoOutput(false);
            uRLConnectionOpenConnection.setReadTimeout(60000);
            uRLConnectionOpenConnection.setUseCaches(true);
            uRLConnectionOpenConnection.connect();
            String contentEncoding = uRLConnectionOpenConnection.getContentEncoding();
            InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
            try {
                SpecificPlaylist from = specificPlaylistProvider.readFrom(inputStream, contentEncoding, this.logger);
                inputStream.close();
                return from;
            } catch (Exception e) {
                try {
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Playlist provider " + specificPlaylistProvider.getId() + " cannot unmarshal <" + url + ">", e);
                    } else if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Playlist provider " + specificPlaylistProvider.getId() + " cannot unmarshal <" + url + ">: " + e);
                    }
                    inputStream.close();
                } catch (Throwable th) {
                    inputStream.close();
                    throw th;
                }
            }
        }
        return null;
    }

    private SpecificPlaylistProvider findProviderByExtension(String str) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        SpecificPlaylistProvider specificPlaylistProvider = null;
        for (SpecificPlaylistProvider specificPlaylistProvider2 : this.serviceLoader) {
            for (ContentType contentType : specificPlaylistProvider2.getContentTypes()) {
                if (contentType.matchExtension(lowerCase)) {
                    specificPlaylistProvider = specificPlaylistProvider2;
                    break;
                }
            }
            if (specificPlaylistProvider != null) {
                break;
            }
        }
        return specificPlaylistProvider;
    }

    public List<PlaylistSong> getSongsFromPlaylistFile(String str) {
        SpecificPlaylist specificPlaylistMyReadFrom;
        ArrayList arrayList = null;
        try {
            URL url = new URL("file://" + str);
            File file = new File(str);
            if (PlayerCore.s().getAppContext() == null) {
                return null;
            }
            try {
                specificPlaylistMyReadFrom = myReadFrom(url);
            } catch (IOException e) {
                tlog.w(e.getMessage());
                specificPlaylistMyReadFrom = null;
            }
            if (specificPlaylistMyReadFrom == null) {
                return null;
            }
            arrayList = new ArrayList();
            if (file.exists()) {
                PlaylistFilesRUtils.ReadParameters readParameters = new PlaylistFilesRUtils.ReadParameters();
                try {
                    readParameters.playlistPath = file.getCanonicalPath();
                } catch (Exception unused) {
                    readParameters.playlistPath = file.getAbsolutePath();
                }
                PlaylistFilesRUtils.readFromSpecificPlaylist(specificPlaylistMyReadFrom, readParameters, arrayList);
            }
            return arrayList;
        } catch (MalformedURLException unused2) {
        }
        return null;
    }

    public int createPlaylist(StatusMsg statusMsg, String str, PlaylistFilesType playlistFilesType, boolean z) {
        return createPlaylist(statusMsg, str, playlistFilesType, null, z);
    }

    public int createPlaylist(StatusMsg statusMsg, String str, PlaylistFilesType playlistFilesType, List<String> list, boolean z) {
        return addDataSourceToPlaylistFile(statusMsg, str, list, true, z);
    }

    public int addDataSourceToPlaylistFile(StatusMsg statusMsg, String str, List<String> list, boolean z, boolean z2) {
        return addToPlaylistFile(statusMsg, str, PlaylistSong.makeSongListFromDataSourceList(list), z, z2);
    }

    public int addToPlaylistFile(StatusMsg statusMsg, String str, List<PlaylistSong> list, boolean z, boolean z2) {
        URL url;
        SpecificPlaylist specificPlaylist;
        SpecificPlaylistProvider provider;
        try {
            if (str.startsWith("file:")) {
                url = new URL(str);
            } else {
                url = new URL("file://" + str);
            }
            File file = new File(str);
            if (file.exists()) {
                try {
                    specificPlaylist = myReadFrom(url);
                    try {
                        provider = specificPlaylist.getProvider();
                        try {
                            if (specificPlaylist instanceof M3U) {
                                ((M3U) specificPlaylist).setExtensionM3U(true);
                            }
                            if (z) {
                                try {
                                    specificPlaylist = provider.toSpecificPlaylist(new Playlist());
                                } catch (Exception e) {
                                    tlog.w(e.getMessage());
                                }
                            }
                        } catch (Exception e2) {
                            tlog.w(e2.getMessage());
                        }
                    } catch (Exception e3) {
                        provider = null;
                    }
                } catch (IOException e4) {
                    specificPlaylist = null;
                    provider = null;
                }
                if (specificPlaylist == null || provider == null) {
                    if (!z) {
                        return 0;
                    }
                    try {
                        specificPlaylist = findProviderByExtension(str).toSpecificPlaylist(new Playlist());
                    } catch (Exception e5) {
                        tlog.w(e5.getMessage());
                    }
                }
            } else {
                try {
                    specificPlaylist = findProviderByExtension(str).toSpecificPlaylist(new Playlist());
                } catch (Exception e6) {
                    tlog.w(e6.getMessage());
                    specificPlaylist = null;
                }
            }
            if (specificPlaylist == null) {
                tlog.w("error specificPlaylist is null");
                statusMsg.addCriticalError("Error specificPlaylist is null");
                return 0;
            }
            PlaylistFilesWUtils.AppendParameters appendParameters = new PlaylistFilesWUtils.AppendParameters();
            appendParameters.writeRelativePaths = z2;
            try {
                appendParameters.playlistPath = file.getCanonicalPath();
            } catch (Exception unused) {
                appendParameters.playlistPath = file.getAbsolutePath();
            }
            int iAppendToSpecificPlaylist = PlaylistFilesWUtils.appendToSpecificPlaylist(specificPlaylist, appendParameters, list);
            try {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    parentFile.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    specificPlaylist.writeTo(fileOutputStream, null);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e7) {
                    tlog.w("outputSpecificPlaylist.writeTo: " + e7.getMessage());
                    statusMsg.addCriticalErrorException("OutputSpecificPlaylist", e7);
                }
                return iAppendToSpecificPlaylist;
            } catch (Exception e8) {
                statusMsg.addCriticalErrorException("File Output", e8);
                return 0;
            }
        } catch (MalformedURLException unused2) {
            statusMsg.addCriticalError("MalformedURLException");
            return 0;
        }
    }

    private void lizzyAddToPlaylistAsFile(Sequence sequence, File file, boolean z, File file2, boolean z2) throws IOException {
        File[] fileArrListFiles;
        int i = 0;
        if (file.isDirectory()) {
            if (!z || (fileArrListFiles = file.listFiles()) == null) {
                return;
            }
            int length = fileArrListFiles.length;
            while (i < length) {
                lizzyAddToPlaylistAsFile(sequence, fileArrListFiles[i], z2, file2, z2);
                i++;
            }
            return;
        }
        if (file.isFile()) {
            String path = file.getPath();
            if (file2 != null) {
                File canonicalFile = file.getCanonicalFile();
                if (!canonicalFile.equals(file2)) {
                    File parentFile = canonicalFile.getParentFile();
                    File parentFile2 = file2.getParentFile();
                    if (parentFile.equals(parentFile2)) {
                        path = file.getName();
                    } else {
                        StringBuilder sb = new StringBuilder(file.getName());
                        File parentFile3 = parentFile.getParentFile();
                        while (true) {
                            File file3 = parentFile;
                            parentFile = parentFile3;
                            if (parentFile == null) {
                                break;
                            }
                            sb.insert(0, '/');
                            String name = file3.getName();
                            if (!"/".equals(name) && !"\\".equals(name)) {
                                sb.insert(0, name);
                            }
                            if (parentFile.equals(parentFile2)) {
                                path = sb.toString();
                                break;
                            }
                            parentFile3 = parentFile.getParentFile();
                        }
                    }
                    i = 1;
                }
            } else {
                i = 1;
            }
            if (i != 0) {
                Media media = new Media();
                media.setSource(new Content(path));
                sequence.addComponent(media);
            }
        }
    }
}
