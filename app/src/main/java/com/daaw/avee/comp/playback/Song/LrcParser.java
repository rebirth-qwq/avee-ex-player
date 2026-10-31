package com.daaw.avee.comp.playback.Song;

import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class LrcParser {
    private static final Pattern TIME_TAG_PATTERN = Pattern.compile("\\[(\\d{2}):(\\d{2})(?:\\.(\\d{2,3}))?\\]");

    /* JADX WARN: Code duplicated, block: B:35:0x0094  */
    public static LyricsData parse(String str) {
        boolean z;
        LyricsData lyricsData = new LyricsData();
        if (str == null || str.isEmpty()) {
            lyricsData.type = 0;
            return lyricsData;
        }
        lyricsData.rawText = str;
        for (String str2 : str.split("\n")) {
            String strTrim = str2.trim();
            if (!strTrim.isEmpty()) {
                Matcher matcher = TIME_TAG_PATTERN.matcher(strTrim);
                ArrayList arrayList = new ArrayList();
                int iEnd = 0;
                while (matcher.find()) {
                    long j = Long.parseLong(matcher.group(1));
                    long j2 = Long.parseLong(matcher.group(2));
                    String strGroup = matcher.group(3);
                    long j3 = 0;
                    if (strGroup != null) {
                        if (strGroup.length() == 3) {
                            j3 = Long.parseLong(strGroup);
                        } else {
                            j3 = Long.parseLong(strGroup) * 10;
                        }
                    }
                    arrayList.add(Long.valueOf(j3 + (j * ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS) + (j2 * 1000)));
                    iEnd = matcher.end();
                }
                String strTrim2 = strTrim.substring(iEnd).trim();
                if (!strTrim2.isEmpty() || arrayList.isEmpty()) {
                    if (arrayList.isEmpty()) {
                        if (strTrim.startsWith("[") && strTrim.endsWith("]") && strTrim.contains(":")) {
                            int iIndexOf = strTrim.indexOf(58);
                            int iIndexOf2 = strTrim.indexOf(93);
                            if (iIndexOf <= 0 || iIndexOf >= iIndexOf2) {
                                lyricsData.lines.add(new LyricsData.LyricLine(-1L, strTrim2));
                            }
                        } else {
                            lyricsData.lines.add(new LyricsData.LyricLine(-1L, strTrim2));
                        }
                    } else {
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            lyricsData.lines.add(new LyricsData.LyricLine(((Long) it.next()).longValue(), strTrim2));
                        }
                    }
                }
            }
        }
        if (lyricsData.lines.isEmpty()) {
            lyricsData.type = 0;
        } else {
            Iterator<LyricsData.LyricLine> it2 = lyricsData.lines.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    z = false;
                    break;
                }
                if (it2.next().timeMs >= 0) {
                    z = true;
                    break;
                }
            }
            if (z) {
                lyricsData.type = 2;
                Collections.sort(lyricsData.lines, new Comparator<LyricsData.LyricLine>() { // from class: com.daaw.avee.comp.playback.Song.LrcParser.1
                    @Override // java.util.Comparator
                    public int compare(LyricsData.LyricLine lyricLine, LyricsData.LyricLine lyricLine2) {
                        return Long.compare(lyricLine.timeMs, lyricLine2.timeMs);
                    }
                });
            } else {
                lyricsData.type = 1;
            }
        }
        return lyricsData;
    }

    public static boolean isSynced(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return TIME_TAG_PATTERN.matcher(str).find();
    }
}
