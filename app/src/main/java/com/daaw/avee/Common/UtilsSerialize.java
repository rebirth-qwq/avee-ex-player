package com.daaw.avee.Common;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class UtilsSerialize {
    public static String replaceCharsInString(char c, char c2, String str) {
        boolean z;
        int i = 0;
        while (true) {
            if (i >= str.length()) {
                z = false;
                break;
            }
            if (str.charAt(i) == c) {
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == c) {
                sb.append(c2);
            } else {
                sb.append(str.charAt(i2));
            }
        }
        return sb.toString();
    }

    public static String serializeIterable(CharSequence charSequence, Iterable iterable) {
        return TextUtils.join(charSequence, iterable);
    }

    public static String serializeArray(CharSequence charSequence, Object[] objArr) {
        return TextUtils.join(charSequence, objArr);
    }

    public static String[] deserializeIterable(String str, String str2) {
        return str2 == null ? new String[0] : TextUtils.split(str2, str);
    }

    public static List<String> deserializeIterableAsList(String str, String str2) {
        if (str2 == null) {
            return new ArrayList();
        }
        return Arrays.asList(TextUtils.split(str2, str));
    }

    public static String serializeIterableSkipInvalidWithAdd(CharSequence charSequence, Iterable iterable) {
        StringBuilder sb = new StringBuilder();
        Iterator it = iterable.iterator();
        boolean z = true;
        while (it.hasNext()) {
            String string = it.next().toString();
            if (!string.contains(charSequence)) {
                if (z) {
                    z = false;
                } else {
                    sb.append(charSequence);
                }
                sb.append(string);
            }
        }
        return sb.toString();
    }

    public static String serializeIterableSkipInvalidWithAdd(CharSequence charSequence, Iterable iterable, Object obj, boolean z) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(obj);
        return serializeIterableSkipInvalidWithAdd(charSequence, iterable, (Iterable) arrayList, z);
    }

    public static String serializeIterableSkipInvalidWithAdd(CharSequence charSequence, Iterable iterable, Iterable iterable2, boolean z) {
        StringBuilder sb = new StringBuilder();
        Iterator it = iterable.iterator();
        boolean z2 = true;
        while (it.hasNext()) {
            String string = it.next().toString();
            if (!string.isEmpty() || !z) {
                if (!string.contains(charSequence)) {
                    if (z2) {
                        z2 = false;
                    } else {
                        sb.append(charSequence);
                    }
                    sb.append(string);
                }
            }
        }
        if (iterable2 != null) {
            Iterator it2 = iterable2.iterator();
            while (it2.hasNext()) {
                String string2 = it2.next().toString();
                if (!string2.isEmpty() || !z) {
                    if (!string2.contains(charSequence)) {
                        if (z2) {
                            z2 = false;
                        } else {
                            sb.append(charSequence);
                        }
                        sb.append(string2);
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String serializeIterableSkipInvalidWithExclude(CharSequence charSequence, Iterable iterable, Object obj) {
        return serializeIterableSkipInvalidWithExclude(charSequence, iterable, obj, false);
    }

    public static String serializeIterableSkipInvalidWithExclude(CharSequence charSequence, Iterable iterable, Object obj, boolean z) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        for (Object obj2 : iterable) {
            if (!obj2.equals(obj)) {
                String string = obj2.toString();
                if (!string.isEmpty() || !z) {
                    if (!string.contains(charSequence)) {
                        if (z2) {
                            z2 = false;
                        } else {
                            sb.append(charSequence);
                        }
                        sb.append(string);
                    }
                }
            }
        }
        return sb.toString();
    }
}
