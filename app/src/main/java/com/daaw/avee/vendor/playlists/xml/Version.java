package com.daaw.avee.vendor.playlists.xml;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class Version implements Cloneable, Serializable {
    public static Version CURRENT = new Version();
    private static final long serialVersionUID = 0;
    private int _revision;
    private int _step;
    private int _version;

    public static Version valueOf(String str) {
        int iIndexOf = str.indexOf(46);
        if (iIndexOf < 0) {
            throw new IllegalArgumentException("The format of a version string is <version.revision.step>");
        }
        if (iIndexOf == 0) {
            throw new IllegalArgumentException("No version part in version string '" + str + '\'');
        }
        int i = iIndexOf + 1;
        if (i >= str.length()) {
            throw new IllegalArgumentException("No revision/step part in version string '" + str + '\'');
        }
        int iIndexOf2 = str.indexOf(46, i);
        if (iIndexOf2 < 0) {
            throw new IllegalArgumentException("The format of a version string is <version.revision.step>");
        }
        if (iIndexOf2 == i) {
            throw new IllegalArgumentException("No revision part in version string '" + str + '\'');
        }
        int i2 = iIndexOf2 + 1;
        if (i2 >= str.length()) {
            throw new IllegalArgumentException("No step part in version string '" + str + '\'');
        }
        return new Version(Integer.parseInt(str.substring(0, iIndexOf)), Integer.parseInt(str.substring(i, iIndexOf2)), Integer.parseInt(str.substring(i2)));
    }

    private Version() {
        this._version = 0;
        this._revision = 0;
        this._step = 0;
    }

    public Version(int i, int i2, int i3) {
        setVersion(i);
        setRevision(i2);
        setStep(i3);
    }

    private void setVersion(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Version number is negative");
        }
        this._version = i;
    }

    public int getVersion() {
        return this._version;
    }

    private void setRevision(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Revision number is negative");
        }
        this._revision = i;
    }

    public int getRevision() {
        return this._revision;
    }

    private void setStep(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Step number is negative");
        }
        this._step = i;
    }

    public int getStep() {
        return this._step;
    }

    public int compareTo(Object obj) {
        return hashCode() - ((Version) obj).hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof Version) && hashCode() == obj.hashCode();
    }

    public int hashCode() {
        return ((this._version & 1023) << 20) | ((this._revision & 1023) << 10) | (this._step & 1023);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._version);
        sb.append('.');
        sb.append(this._revision);
        sb.append('.');
        sb.append(this._step);
        return sb.toString();
    }
}
