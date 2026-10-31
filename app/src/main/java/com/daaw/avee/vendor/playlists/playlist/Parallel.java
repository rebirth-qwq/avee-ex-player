package com.daaw.avee.vendor.playlists.playlist;

/* JADX INFO: loaded from: classes.dex */
public class Parallel extends AbstractTimeContainer {
    @Override // com.daaw.avee.vendor.playlists.playlist.AbstractTimeContainer, com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent
    public void acceptDown(PlaylistVisitor playlistVisitor) throws Exception {
        playlistVisitor.beginVisitParallel(this);
        super.acceptDown(playlistVisitor);
        playlistVisitor.endVisitParallel(this);
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent
    public void acceptUp(PlaylistVisitor playlistVisitor) throws Exception {
        playlistVisitor.beginVisitParallel(this);
        super.acceptUp(playlistVisitor);
        playlistVisitor.endVisitParallel(this);
    }
}
