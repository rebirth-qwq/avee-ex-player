package com.daaw.avee.vendor.playlists.playlist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;
import com.daaw.avee.vendor.playlists.content.type.ContentType;
import com.daaw.avee.vendor.playlists.logging.LogFactory;
import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes.dex */
public final class SpecificPlaylistFactory {
    private static SpecificPlaylistFactory _instance;
    private final Log _logger = LogFactory.getLog(getClass());
    private final ServiceLoader<SpecificPlaylistProvider> _serviceLoader = ServiceLoader.load(SpecificPlaylistProvider.class);

    public static SpecificPlaylistFactory getInstance() {
        synchronized (SpecificPlaylistFactory.class) {
            if (_instance == null) {
                _instance = new SpecificPlaylistFactory();
            }
        }
        return _instance;
    }

    private SpecificPlaylistFactory() {
    }

    public void reloadProviders() {
        this._serviceLoader.reload();
    }

    public SpecificPlaylist readFrom(URL url) throws IOException {
        for (SpecificPlaylistProvider specificPlaylistProvider : this._serviceLoader) {
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
                SpecificPlaylist from = specificPlaylistProvider.readFrom(inputStream, contentEncoding, this._logger);
                inputStream.close();
                return from;
            } catch (Exception e) {
                try {
                    if (this._logger.isTraceEnabled()) {
                        this._logger.trace("Playlist provider " + specificPlaylistProvider.getId() + " cannot unmarshal <" + url + ">", e);
                    } else if (this._logger.isDebugEnabled()) {
                        this._logger.debug("Playlist provider " + specificPlaylistProvider.getId() + " cannot unmarshal <" + url + ">: " + e);
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

    public SpecificPlaylist readFrom(File file) throws IOException {
        return readFrom(file.toURI().toURL());
    }

    public SpecificPlaylistProvider findProviderById(String str) {
        for (SpecificPlaylistProvider specificPlaylistProvider : this._serviceLoader) {
            if (str.equalsIgnoreCase(specificPlaylistProvider.getId())) {
                return specificPlaylistProvider;
            }
        }
        return null;
    }

    public SpecificPlaylistProvider findProviderByExtension(String str) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        SpecificPlaylistProvider specificPlaylistProvider = null;
        for (SpecificPlaylistProvider specificPlaylistProvider2 : this._serviceLoader) {
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

    public List<SpecificPlaylistProvider> getProviders() {
        ArrayList arrayList = new ArrayList();
        Iterator<SpecificPlaylistProvider> it = this._serviceLoader.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }
}
