package com.daaw.avee.vendor.playlists.playlist;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractTimeContainer extends AbstractPlaylistComponent {
    private final List<AbstractPlaylistComponent> _components = new ArrayList();

    public AbstractPlaylistComponent[] getComponents() {
        AbstractPlaylistComponent[] abstractPlaylistComponentArr = new AbstractPlaylistComponent[this._components.size()];
        this._components.toArray(abstractPlaylistComponentArr);
        return abstractPlaylistComponentArr;
    }

    public void addComponent(AbstractPlaylistComponent abstractPlaylistComponent) {
        abstractPlaylistComponent.setParent(this);
        this._components.add(abstractPlaylistComponent);
    }

    public void addComponent(int i, AbstractPlaylistComponent abstractPlaylistComponent) {
        abstractPlaylistComponent.setParent(this);
        this._components.add(i, abstractPlaylistComponent);
    }

    public boolean removeComponent(AbstractPlaylistComponent abstractPlaylistComponent) {
        abstractPlaylistComponent.setParent(null);
        return this._components.remove(abstractPlaylistComponent);
    }

    public AbstractPlaylistComponent removeComponent(int i) {
        AbstractPlaylistComponent abstractPlaylistComponentRemove = this._components.remove(i);
        abstractPlaylistComponentRemove.setParent(null);
        return abstractPlaylistComponentRemove;
    }

    public int getComponentsNumber() {
        return this._components.size();
    }

    @Override // com.daaw.avee.vendor.playlists.playlist.AbstractPlaylistComponent
    public void acceptDown(PlaylistVisitor playlistVisitor) throws Exception {
        for (AbstractPlaylistComponent abstractPlaylistComponent : getComponents()) {
            abstractPlaylistComponent.acceptDown(playlistVisitor);
        }
    }
}
