package com.daaw.avee.comp.Visualizer.Graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.Vec2i;
import com.daaw.avee.Common.tlog;
import java.util.HashMap;
import java.util.Map;
import com.daaw.avee.vendor.glutil.graphics.ITexture;

/* JADX INFO: loaded from: classes.dex */
public class SpriteFont {
    private static final int INITIAL_ATLAS_SIZE = 1024;
    private static final int MAX_ATLAS_SIZE = 4096;
    private Bitmap atlasBitmap;
    private Canvas atlasCanvas;
    private int atlasHeight;
    private AtlasTexture atlasTexture;
    private int atlasWidth;
    private CharSet charSet;
    private int fontMetricAscent;
    private int fontMetricBottom;
    private int fontMetricDescent;
    private int fontMetricHeight;
    private int fontMetricMaxHeight;
    private int fontMetricTop;
    private Map<String, Glyph> glyphMap;
    private int nextFreeX;
    private int nextFreeY;
    private Paint paint;
    private int rowHeight;
    private VTexture texture;
    private Vec2f textureDim;
    private boolean textureDirty;
    private boolean valid;

    public SpriteFont(Typeface typeface, int i) {
        this(typeface, i, null);
    }

    public SpriteFont(Typeface typeface, int i, CharSet charSet) {
        this.valid = true;
        this.charSet = charSet;
        this.glyphMap = new HashMap();
        try {
            initPaint(typeface, i);
            initAtlas();
        } catch (Exception e) {
            tlog.e(e, "SpriteFont initialization failed");
            this.valid = false;
        }
    }

    public SpriteFont(String str, int i, Rect[] rectArr, CharSet charSet) {
        this.valid = true;
        this.charSet = charSet;
        this.fontMetricHeight = i;
        this.fontMetricMaxHeight = i;
        this.glyphMap = new HashMap();
        for (int i2 = 0; i2 < charSet.count(); i2++) {
            Glyph glyph = new Glyph();
            glyph.x = rectArr[i2].left;
            glyph.y = rectArr[i2].top;
            glyph.width = rectArr[i2].width();
            glyph.height = i;
            glyph.spaceWidth = rectArr[i2].width();
            glyph.spaceHeight = i;
            glyph.visualXOffset = 0.0f;
            glyph.visualYOffset = 0.0f;
            this.glyphMap.put(String.valueOf(charSet.getCharByIndex(i2)), glyph);
        }
        this.texture = new VTexture(str, 9729, 9729, 10497, false);
        this.atlasTexture = new AtlasTexture(this.texture, true);
        this.textureDim = new Vec2f(this.texture.getWidth(), this.texture.getHeight());
        this.paint = null;
        this.atlasBitmap = null;
        this.atlasCanvas = null;
    }

    private void initPaint(Typeface typeface, int i) {
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(i);
        paint.setAntiAlias(true);
        paint.setColor(-1);
        paint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        this.fontMetricTop = fontMetricsInt.top;
        this.fontMetricBottom = fontMetricsInt.bottom;
        this.fontMetricAscent = fontMetricsInt.ascent;
        this.fontMetricDescent = fontMetricsInt.descent;
        this.fontMetricHeight = fontMetricsInt.descent - fontMetricsInt.ascent;
        this.fontMetricMaxHeight = fontMetricsInt.bottom - fontMetricsInt.top;
        this.paint = paint;
    }

    private void initAtlas() {
        this.atlasWidth = 1024;
        this.atlasHeight = 1024;
        this.atlasBitmap = Bitmap.createBitmap(this.atlasWidth, this.atlasHeight, Bitmap.Config.ARGB_8888);
        this.atlasCanvas = new Canvas(this.atlasBitmap);
        this.atlasCanvas.drawColor(0);
        this.nextFreeX = 0;
        this.nextFreeY = 0;
        this.rowHeight = 0;
        this.textureDirty = false;
    }

    private void ensureGlyph(String str) {
        if (!this.glyphMap.containsKey(str)) {
            if (this.paint == null) {
                tlog.w("ensureGlyph called on external bitmap font, ignoring");
                return;
            }
            float[] fArr = new float[1];
            if (str.length() > 0) {
                this.paint.getTextWidths(str, fArr);
            }
            Rect rect = new Rect();
            this.paint.getTextBounds(str, 0, str.length(), rect);
            int iMax = Math.max(2, rect.width() + 4);
            int i = this.fontMetricMaxHeight + 4;
            if (this.nextFreeX + iMax + 2 > this.atlasWidth) {
                this.nextFreeX = 0;
                this.nextFreeY += this.rowHeight + 2;
                this.rowHeight = 0;
            }
            if (this.nextFreeY + i + 2 > this.atlasHeight) {
                expandAtlas();
                ensureGlyph(str);
                return;
            }
            this.atlasCanvas.drawText(str, (this.nextFreeX + 2) - rect.left, (this.nextFreeY + 2) - this.fontMetricTop, this.paint);
            Glyph glyph = new Glyph();
            glyph.x = this.nextFreeX + 2;
            glyph.y = this.nextFreeY + 2;
            glyph.width = iMax;
            glyph.height = i;
            glyph.visualXOffset = rect.left;
            glyph.visualYOffset = this.fontMetricMaxHeight;
            glyph.spaceWidth = fArr[0] > 0.0f ? fArr[0] : iMax;
            glyph.spaceHeight = this.fontMetricHeight;
            this.glyphMap.put(str, glyph);
            this.nextFreeX += iMax + 2;
            this.rowHeight = Math.max(this.rowHeight, i);
            this.textureDirty = true;
        }
    }

    private void expandAtlas() {
        int iMin = Math.min(this.atlasWidth * 2, 4096);
        int iMin2 = Math.min(this.atlasHeight * 2, 4096);
        if (iMin > 4096 || iMin2 > 4096) {
            tlog.w("Atlas reached max size, cannot add more glyphs");
            return;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMin, iMin2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.drawColor(0);
        canvas.drawBitmap(this.atlasBitmap, 0.0f, 0.0f, (Paint) null);
        this.atlasBitmap = bitmapCreateBitmap;
        this.atlasCanvas = canvas;
        this.atlasWidth = iMin;
        this.atlasHeight = iMin2;
        this.textureDirty = true;
    }

    private void rebuildTexture() {
        if (this.textureDirty) {
            int iPow2roundup = pow2roundup(this.atlasWidth);
            int iPow2roundup2 = pow2roundup(this.atlasHeight);
            if (this.texture != null) {
                this.texture.dispose();
            }
            this.texture = new VTexture(Bitmap.createBitmap(this.atlasBitmap, 0, 0, iPow2roundup, iPow2roundup2), 9729, 9729, 10497, false);
            this.atlasTexture = new AtlasTexture(this.texture, true);
            this.textureDim = new Vec2f(iPow2roundup, iPow2roundup2);
            this.textureDirty = false;
        }
    }

    public Glyph getSpriteDescByChar(char c) {
        String strValueOf = String.valueOf(c);
        ensureGlyph(strValueOf);
        Glyph glyph = this.glyphMap.get(strValueOf);
        if (glyph == null && this.charSet != null && this.charSet.count() > 0) {
            glyph = this.glyphMap.get(String.valueOf(this.charSet.getCharByIndex(0)));
        }
        if (glyph == null) {
            Glyph glyph2 = new Glyph();
            glyph2.width = 10.0f;
            glyph2.height = this.fontMetricMaxHeight;
            glyph2.spaceWidth = 10.0f;
            glyph2.spaceHeight = this.fontMetricHeight;
            glyph2.visualYOffset = this.fontMetricMaxHeight;
            return glyph2;
        }
        return glyph;
    }

    public ITexture getTexture() {
        rebuildTexture();
        return this.texture;
    }

    public IAtlasTexture getEntryTexture() {
        rebuildTexture();
        return this.atlasTexture;
    }

    public boolean isValid() {
        return this.valid;
    }

    public float fontHeight() {
        return this.fontMetricHeight;
    }

    public float fontMaxHeight() {
        return this.fontMetricMaxHeight;
    }

    public Vec2f textureDim() {
        rebuildTexture();
        return this.textureDim;
    }

    public Vec2i measureText(String str) {
        int i = 0;
        float f = 0.0f;
        if (this.paint != null || str == null) {
            while (i < str.length()) {
                int iCharCount = Character.charCount(str.codePointAt(i)) + i;
                while (iCharCount < str.length()) {
                    int iCodePointAt = str.codePointAt(iCharCount);
                    if (!isCombiningCharacter(iCodePointAt)) {
                        break;
                    }
                    iCharCount += Character.charCount(iCodePointAt);
                }
                String strSubstring = str.substring(i, iCharCount);
                ensureGlyph(strSubstring);
                Glyph glyph = this.glyphMap.get(strSubstring);
                if (glyph != null) {
                    f += glyph.spaceWidth;
                }
                i = iCharCount;
            }
            return new Vec2i((int) f, (int) fontHeight());
        }
        while (true) {
            int i2 = i;
            if (i2 < str.length()) {
                Glyph glyph2 = this.glyphMap.get(String.valueOf(str.charAt(i2)));
                if (glyph2 != null) {
                    f += glyph2.spaceWidth;
                }
                i = i2 + 1;
            } else {
                return new Vec2i((int) f, (int) fontHeight());
            }
        }
    }

    public int measureTextY() {
        return (int) fontHeight();
    }

    private boolean isCombiningCharacter(int i) {
        byte type = (byte) Character.getType(i);
        return type == 8 || type == 7 || type == 6;
    }

    public void dispose() {
        if (this.atlasBitmap != null && !this.atlasBitmap.isRecycled()) {
            this.atlasBitmap.recycle();
        }
        if (this.texture != null) {
            this.texture.dispose();
        }
        if (this.atlasTexture != null) {
            this.atlasTexture.dispose();
        }
        if (this.glyphMap != null) {
            this.glyphMap.clear();
        }
    }

    static int pow2roundup(int i) {
        return Utils.pow2roundup(i, 4096);
    }

    public class Glyph {
        public float height;
        public float spaceHeight;
        public float spaceWidth;
        public float visualXOffset;
        public float visualYOffset;
        public float width;
        public float x;
        public float y;

        public Glyph() {
        }
    }
}
