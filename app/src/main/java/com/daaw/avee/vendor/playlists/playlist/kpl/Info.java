package com.daaw.avee.vendor.playlists.playlist.kpl;

import com.daaw.avee.vendor.playlists.lang.StringUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class Info {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date _creation_day = null;
    private Date _modified_day = null;
    private String _author = null;
    private String _player = null;
    private String _player_version = null;
    private String _kpl_version = "1";

    public String getCreationDayString() {
        String str;
        if (this._creation_day == null) {
            return null;
        }
        synchronized (DATE_FORMAT) {
            str = DATE_FORMAT.format(this._creation_day);
        }
        return str;
    }

    public void setCreationDayString(String str) throws ParseException {
        String strNormalize = StringUtils.normalize(str);
        if (strNormalize == null) {
            this._creation_day = null;
            return;
        }
        synchronized (DATE_FORMAT) {
            this._creation_day = DATE_FORMAT.parse(strNormalize);
        }
    }

    public Date getCreationDay() {
        return this._creation_day;
    }

    public void setCreationDay(Date date) {
        this._creation_day = date;
    }

    public String getModifiedDayString() {
        String str;
        if (this._modified_day == null) {
            return null;
        }
        synchronized (DATE_FORMAT) {
            str = DATE_FORMAT.format(this._modified_day);
        }
        return str;
    }

    public void setModifiedDayString(String str) throws ParseException {
        String strNormalize = StringUtils.normalize(str);
        if (strNormalize == null) {
            this._modified_day = null;
            return;
        }
        synchronized (DATE_FORMAT) {
            this._modified_day = DATE_FORMAT.parse(strNormalize);
        }
    }

    public Date getModifiedDay() {
        return this._modified_day;
    }

    public void setModifiedDay(Date date) {
        this._modified_day = date;
    }

    public String getAuthor() {
        return this._author;
    }

    public void setAuthor(String str) {
        this._author = StringUtils.normalize(str);
    }

    public String getPlayer() {
        return this._player;
    }

    public void setPlayer(String str) {
        this._player = StringUtils.normalize(str);
    }

    public String getPlayerVersion() {
        return this._player_version;
    }

    public void setPlayerVersion(String str) {
        this._player_version = StringUtils.normalize(str);
    }

    public String getKplVersion() {
        return this._kpl_version;
    }

    public void setKplVersion(String str) {
        this._kpl_version = StringUtils.normalize(str);
    }
}
