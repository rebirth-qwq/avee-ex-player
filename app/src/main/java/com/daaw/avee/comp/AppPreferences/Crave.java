package com.daaw.avee.comp.AppPreferences;


/* JADX INFO: loaded from: classes.dex */
public class Crave {
    static char decryCh(char c) {
        if (c == '\'') {
            return '\'';
        }
        if (c == ';') {
            return 'l';
        }
        if (c == '=') {
            return 'i';
        }
        if (c == '[') {
            return '-';
        }
        if (c == ']') {
            return '.';
        }
        if (c == '}') {
            return '4';
        }
        switch (c) {
            case ',':
                return 'z';
            case '-':
                return 'j';
            case '.':
                return '}';
            default:
                switch (c) {
                    case '0':
                        return '9';
                    case '1':
                        return '{';
                    case '2':
                        return 'r';
                    case '3':
                        return 'p';
                    case '4':
                        return 'w';
                    case '5':
                        return 'n';
                    case '6':
                        return 'a';
                    case '7':
                        return 'h';
                    case '8':
                        return 'd';
                    case '9':
                        return '=';
                    default:
                        switch (c) {
                            case 'a':
                                return 'f';
                            case 'b':
                                return '8';
                            case 'c':
                                return 'q';
                            case 'd':
                                return '6';
                            case 'e':
                                return '5';
                            case 'f':
                                return ',';
                            case 'g':
                                return '2';
                            case 'h':
                                return 'g';
                            case 'i':
                                return 'y';
                            case 'j':
                                return 'e';
                            case 'k':
                                return 'x';
                            case 'l':
                                return 'o';
                            case 'm':
                                return '3';
                            case 'n':
                                return 'c';
                            case 'o':
                                return 'm';
                            case 'p':
                                return ']';
                            case 'q':
                                return '[';
                            case 'r':
                                return 'b';
                            case 's':
                                return 'k';
                            case 't':
                                return ';';
                            case 'u':
                                return '1';
                            case 'v':
                                return '0';
                            case 'w':
                                return 'u';
                            case 'x':
                                return 's';
                            case 'y':
                                return '7';
                            case 'z':
                                return 'v';
                            case '{':
                                return 't';
                            default:
                                return c;
                        }
                }
        }
    }

    static char encryCh(char c) {
        if (c == '\'') {
            return '\'';
        }
        if (c == ';') {
            return 't';
        }
        if (c == '=') {
            return '9';
        }
        if (c == '[') {
            return 'q';
        }
        if (c == ']') {
            return 'p';
        }
        if (c == '}') {
            return '.';
        }
        switch (c) {
            case ',':
                return 'f';
            case '-':
                return '[';
            case '.':
                return ']';
            default:
                switch (c) {
                    case '0':
                        return 'v';
                    case '1':
                        return 'u';
                    case '2':
                        return 'g';
                    case '3':
                        return 'm';
                    case '4':
                        return '}';
                    case '5':
                        return 'e';
                    case '6':
                        return 'd';
                    case '7':
                        return 'y';
                    case '8':
                        return 'b';
                    case '9':
                        return '0';
                    default:
                        switch (c) {
                            case 'a':
                                return '6';
                            case 'b':
                                return 'r';
                            case 'c':
                                return 'n';
                            case 'd':
                                return '8';
                            case 'e':
                                return 'j';
                            case 'f':
                                return 'a';
                            case 'g':
                                return 'h';
                            case 'h':
                                return '7';
                            case 'i':
                                return '=';
                            case 'j':
                                return '-';
                            case 'k':
                                return 's';
                            case 'l':
                                return ';';
                            case 'm':
                                return 'o';
                            case 'n':
                                return '5';
                            case 'o':
                                return 'l';
                            case 'p':
                                return '3';
                            case 'q':
                                return 'c';
                            case 'r':
                                return '2';
                            case 's':
                                return 'x';
                            case 't':
                                return '{';
                            case 'u':
                                return 'w';
                            case 'v':
                                return 'z';
                            case 'w':
                                return '4';
                            case 'x':
                                return 'k';
                            case 'y':
                                return 'i';
                            case 'z':
                                return ',';
                            case '{':
                                return '1';
                            default:
                                return c;
                        }
                }
        }
    }

    static String encry(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            sb.append(encryCh(str.charAt(i)));
        }
        return sb.toString();
    }

    static String decry(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            sb.append(decryCh(str.charAt(i)));
        }
        return sb.toString();
    }

    static boolean iscry(String str) {
        if (str != null && str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                char cCharAt = str.charAt(i);
                if (decryCh(cCharAt) != cCharAt) {
                    return cCharAt == encryCh('{');
                }
            }
        }
        return false;
    }
}
