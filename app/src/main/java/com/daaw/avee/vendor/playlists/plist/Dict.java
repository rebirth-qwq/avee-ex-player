package com.daaw.avee.vendor.playlists.plist;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Dict extends PlistObject {
    private final Hashtable<Key, PlistObject> _objects = new Hashtable<>();
    private transient Key _tmpKey = null;

    public Hashtable<Key, PlistObject> getDictionary() {
        return this._objects;
    }

    public PlistObject put(Key key, PlistObject plistObject) {
        return this._objects.put(key, plistObject);
    }

    public PlistObject put(java.lang.String str, PlistObject plistObject) {
        return this._objects.put(new Key(str), plistObject);
    }

    @Deprecated
    public List<PlistObject> getKeysAndObjects() {
        ArrayList arrayList = new ArrayList(this._objects.size());
        Enumeration<Key> enumerationKeys = this._objects.keys();
        while (enumerationKeys.hasMoreElements()) {
            Key keyNextElement = enumerationKeys.nextElement();
            arrayList.add(keyNextElement);
            arrayList.add(this._objects.get(keyNextElement));
        }
        return arrayList;
    }

    @Deprecated
    public void addKeyOrObject(PlistObject plistObject) {
        plistObject.setParent(this);
        Key key = this._tmpKey;
        if (key == null) {
            if (!(plistObject instanceof Key)) {
                throw new IllegalArgumentException("A key is expected here");
            }
            this._tmpKey = (Key) plistObject;
        } else {
            if (plistObject instanceof Key) {
                throw new IllegalArgumentException("A key is unexpected here");
            }
            put(key, plistObject);
            this._tmpKey = null;
        }
    }

    public PlistObject findObjectByKey(java.lang.String str) {
        return this._objects.get(new Key(str));
    }
}
