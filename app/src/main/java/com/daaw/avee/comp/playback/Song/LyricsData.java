package com.daaw.avee.comp.playback.Song;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LyricsData {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_PLAIN = 1;
    public static final int TYPE_SYNCED = 2;
    public String rawText;
    public int type = 0;
    public List<LyricLine> lines = new ArrayList();

    public boolean isEmpty() {
        return this.type == 0 || this.lines == null || this.lines.isEmpty();
    }

    public int getLineIndexAtTime(long j) {
        if (this.type != 2 || this.lines.isEmpty()) {
            return -1;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.lines.size() && this.lines.get(i2).timeMs <= j; i2++) {
            i = i2;
        }
        return i;
    }

    public long getLineStartTime(int i) {
        if (i < 0 || i >= this.lines.size()) {
            return 0L;
        }
        return this.lines.get(i).timeMs;
    }

    public long getNextLineStartTime(int i) {
        if (i + 1 < this.lines.size()) {
            return this.lines.get(i + 1).timeMs;
        }
        if (i >= 0 && i < this.lines.size()) {
            return this.lines.get(i).timeMs + 3000;
        }
        return 0L;
    }

    public static class LyricLine {
        public String text;
        public long timeMs;

        public LyricLine(long j, String str) {
            this.timeMs = j;
            this.text = str;
        }
    }
}
