package com.daaw.avee.vendor.playlists.playlist;

/* JADX INFO: loaded from: classes.dex */
public class Sequence extends AbstractTimeContainer {
    @Override // com.daaw.avee.vendor.playlists.playlist.AbstractTimeContainer, com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent
    public void acceptDown(PlaylistVisitor playlistVisitor) throws Exception {
        playlistVisitor.beginVisitSequence(this);
        super.acceptDown(playlistVisitor);
        playlistVisitor.endVisitSequence(this);
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent
    public void acceptUp(PlaylistVisitor playlistVisitor) throws Exception {
        playlistVisitor.beginVisitSequence(this);
        super.acceptUp(playlistVisitor);
        playlistVisitor.endVisitSequence(this);
    }
}
