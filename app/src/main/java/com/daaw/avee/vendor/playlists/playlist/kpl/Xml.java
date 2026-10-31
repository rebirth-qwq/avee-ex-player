package com.daaw.avee.vendor.playlists.playlist.kpl;

import com.daaw.avee.vendor.playlists.playlist.Media;
import com.daaw.avee.vendor.playlists.playlist.Playlist;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist;
import com.daaw.avee.vendor.playlists.playlist.SpecificPlaylistProvider;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.daaw.avee.vendor.playlists.content.Content;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* JADX INFO: loaded from: classes.dex */
public class Xml implements SpecificPlaylist {
    private transient SpecificPlaylistProvider _provider = null;
    private final List<Entry> _entries = new ArrayList();
    private final Info _info = new Info();

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist
    public void setProvider(SpecificPlaylistProvider specificPlaylistProvider) {
        this._provider = specificPlaylistProvider;
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist
    public SpecificPlaylistProvider getProvider() {
        return this._provider;
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist
    public void writeTo(OutputStream outputStream, String str) throws Exception {
        Document documentNewDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        int i = 0;
        documentNewDocument.setStrictErrorChecking(false);
        Element elementCreateElement = documentNewDocument.createElement("xml");
        documentNewDocument.appendChild(elementCreateElement);
        for (Entry entry : this._entries) {
            if (entry.getFilename() != null) {
                Element elementCreateElement2 = documentNewDocument.createElement(Integer.toString(i));
                elementCreateElement2.setAttribute("filename", entry.getFilename());
                elementCreateElement.appendChild(elementCreateElement2);
                if (entry.getTag() != null) {
                    Element elementCreateElement3 = documentNewDocument.createElement("tag");
                    elementCreateElement3.setAttribute("artist", entry.getTag().getArtist());
                    elementCreateElement3.setAttribute("album", entry.getTag().getAlbum());
                    elementCreateElement3.setAttribute("title", entry.getTag().getTitle());
                    elementCreateElement3.setAttribute("year", entry.getTag().getYear());
                    elementCreateElement3.setAttribute("comment", entry.getTag().getComment());
                    elementCreateElement3.setAttribute("genre", entry.getTag().getGenre());
                    elementCreateElement3.setAttribute("track", entry.getTag().getTrack());
                    elementCreateElement3.setAttribute("gid", entry.getTag().getGid());
                    elementCreateElement3.setAttribute("has_tag", entry.getTag().getHasTag());
                    elementCreateElement2.appendChild(elementCreateElement3);
                }
                i++;
            }
        }
        Element elementCreateElement4 = documentNewDocument.createElement("info");
        elementCreateElement4.setAttribute("creation_day", this._info.getCreationDayString());
        elementCreateElement4.setAttribute("modified_day", this._info.getModifiedDayString());
        elementCreateElement4.setAttribute("author", this._info.getAuthor());
        elementCreateElement4.setAttribute("player", this._info.getPlayer());
        elementCreateElement4.setAttribute("player_version", this._info.getPlayerVersion());
        elementCreateElement4.setAttribute("kpl_version", this._info.getKplVersion());
        elementCreateElement.appendChild(elementCreateElement4);
        DOMSource dOMSource = new DOMSource(documentNewDocument);
        StreamResult streamResult = new StreamResult(outputStream);
        Transformer transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
        transformerNewTransformer.setOutputProperty("omit-xml-declaration", "yes");
        transformerNewTransformer.setOutputProperty("indent", "yes");
        if (str != null) {
            transformerNewTransformer.setOutputProperty("encoding", str);
        }
        transformerNewTransformer.transform(dOMSource, streamResult);
        outputStream.flush();
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.SpecificPlaylist
    public Playlist toPlaylist() {
        Playlist playlist = new Playlist();
        for (Entry entry : this._entries) {
            if (entry.getFilename() != null) {
                Media media = new Media();
                media.setSource(new Content(entry.getFilename()));
                playlist.getRootSequence().addComponent(media);
            }
        }
        playlist.normalize();
        return playlist;
    }

    public List<Entry> getEntries() {
        return this._entries;
    }

    public Info getInfo() {
        return this._info;
    }
}
