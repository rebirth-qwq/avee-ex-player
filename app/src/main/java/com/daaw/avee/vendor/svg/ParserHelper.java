package com.daaw.avee.vendor.svg;

/* JADX INFO: loaded from: classes2.dex */
public class ParserHelper {
    private static final double[] pow10 = new double[128];
    private char current;
    private final int n;
    public int pos;
    private final char[] s;

    public ParserHelper(String str, int i) {
        try {
            char[] charArray = str.toCharArray();
            this.s = charArray;
            this.pos = i;
            this.n = charArray.length;
            this.current = charArray[i];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private char read() {
        int i = this.pos;
        if (i < this.n) {
            this.pos = i + 1;
        }
        int i2 = this.pos;
        if (i2 == this.n) {
            return (char) 0;
        }
        return this.s[i2];
    }

    public void skipWhitespace() {
        while (this.pos < this.n && Character.isWhitespace(this.s[this.pos])) {
            advance();
        }
    }

    public void skipNumberSeparator() {
        while (this.pos < this.n) {
            char c = this.s[this.pos];
            if (c != '\t' && c != '\n' && c != ' ' && c != ',') {
                return;
            }
            advance();
        }
    }

    public void advance() {
        this.current = read();
    }

    // 严格按原版 smali（com.larvalabs.svgandroid.ParserHelper.parseFloat, 309 条指令）还原。
    // 原算法：尾数最多并入 9 位；小数每并入一位让指数 -1；整数部分超出 9 位的每位让指数 +1；
    // 随后可选 e/E 指数最多 3 位，再交给 buildFloat(mantissa, exponent)。
    // 之前那版用 countIntDigits(mant)-digits 推算指数，对 "161.18387" 得到指数 0
    // → 返回 16118387.0（放大约 10 万倍）→ 字形路径坐标爆炸、轮廓退化，
    // 表现为“11 号模板 Bars 选字母后只显示一点点”。Circle/SidedPolygon 不走 SVG 解析，
    // 所以只有字母形状受影响。
    public float parseFloat() {
        int mantissa = 0;
        int exponent = 0;
        int numDigits = 0;
        boolean neg = false;
        boolean expNeg = false;

        char c = this.current;
        if (c == '-' || c == '+') {
            neg = (c == '-');
            advance();
            c = this.current;
        }

        if (c == '.') {
            // 前导小数点形式，如 ".5"
            advance();
            c = this.current;
        } else if (c >= '0' && c <= '9') {
            while (c >= '0' && c <= '9') {
                if (numDigits < 9) {
                    mantissa = (mantissa * 10) + (c - '0');
                    numDigits++;
                } else {
                    exponent++;
                }
                advance();
                c = this.current;
            }
            if (c == '.') {
                advance();
                c = this.current;
            }
        } else {
            // 原版 packed-switch 默认分支：返回 NaN
            return Float.NaN;
        }

        while (c >= '0' && c <= '9') {
            if (numDigits < 9) {
                mantissa = (mantissa * 10) + (c - '0');
                numDigits++;
                exponent--;
            }
            advance();
            c = this.current;
        }

        if (c == 'e' || c == 'E') {
            advance();
            c = this.current;
            if (c == '+' || c == '-') {
                expNeg = (c == '-');
                advance();
                c = this.current;
            }
            int e = 0;
            int eDigits = 0;
            while (c >= '0' && c <= '9' && eDigits < 3) {
                e = (e * 10) + (c - '0');
                eDigits++;
                advance();
                c = this.current;
            }
            if (eDigits == 0) {
                reportUnexpectedCharacterError(c);
                return 0.0f;
            }
            exponent += expNeg ? -e : e;
        }

        float f = buildFloat(mantissa, exponent);
        return neg ? -f : f;
    }

    private void reportUnexpectedCharacterError(char c) {
        throw new RuntimeException("Unexpected char '" + c + "'.");
    }

    public static float buildFloat(int i, int i2) {
        double d;
        if (i2 < -125 || i == 0) {
            return 0.0f;
        }
        if (i2 >= 128) {
            return i > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        if (i2 == 0) {
            return i;
        }
        if (i >= 67108864) {
            i++;
        }
        double d2 = i;
        double[] dArr = pow10;
        if (i2 > 0) {
            double d3 = dArr[i2];
            Double.isNaN(d2);
            d = d2 * d3;
        } else {
            double d4 = dArr[-i2];
            Double.isNaN(d2);
            d = d2 / d4;
        }
        return (float) d;
    }

    static {
        int i = 0;
        while (true) {
            double[] dArr = pow10;
            if (i >= dArr.length) {
                break;
            }
            dArr[i] = Math.pow(10.0d, i);
            i++;
        }
    }

    public float nextFloat() {
        skipWhitespace();
        float f = parseFloat();
        skipNumberSeparator();
        return f;
    }

    public int nextFlag() {
        skipWhitespace();
        int i = this.current - '0';
        this.current = read();
        skipNumberSeparator();
        return i;
    }
}
