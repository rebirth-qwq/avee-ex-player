package com.daaw.avee.comp.Visualizer.Elements;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.daaw.avee.Common.Vec2f;
import com.daaw.avee.Common.Vec2i;
import com.daaw.avee.Common.Vec3f;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.LocalFontLoader;
import com.daaw.avee.comp.Visualizer.Graphic.CharSet;
import com.daaw.avee.comp.Visualizer.Graphic.RenderPassData;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.Graphic.SpriteFont;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.comp.Visualizer.InternalVisualizationDataProvider;
import com.daaw.avee.comp.playback.MediaPlaybackService;
import com.daaw.avee.comp.playback.Song.LyricsData;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
 import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class LyricsElement extends TextElement {
    private static final int ANIM_BASIC = 0;
    private static final int ANIM_FADE = 1;
    private static final int ANIM_MOVE = 2;
    private static final int ANIM_SCROLL = 3;
    private static final int DIRECTION_DOWN = 1;
    private static final int DIRECTION_UP = 0;
    private static final String FALLBACK_TEXT = "No Lyrics";
    private static final int TRANSITION_FADE = 1;
    private static final int TRANSITION_FADE_SCALE = 3;
    private static final int TRANSITION_INSTANT = 0;
    private static final int TRANSITION_SCALE = 2;
    public static final String typeName = "Lyrics";
    private LyricsData lyricsData;
    private static final String[] animTypeNames = {"Basic", "Fade", "Move", "Scroll"};
    private static final String[] curveNames = {"Linear", "EaseIn", "EaseInQuad", "EaseInCubic", "EaseOut", "EaseOutQuad", "EaseOutCubic", "EaseInOut", "EaseInOutQuad", "EaseInOutCubic", "Circular", "Back", "Bounce", "Elastic", "Sine"};
    private static final String[] directionNames = {"Up", "Down"};
    private static final String[] transitionNames = {"Instant", "Fade", "Scale", "FadeAndScale"};
    private static volatile long sInjectedTimeMs = -1;
    private int fadeInDuration = 300;
    private int fadeOutDuration = 300;
    private int fadeInCurve = 0;
    private int fadeOutCurve = 0;
    private float moveStartY = 0.0f;
    private float moveEndY = 0.0f;
    private int moveDurationStart = 1000;
    private int moveDurationEnd = 1000;
    private int moveInCurve = 0;
    private int moveOutCurve = 0;
    private int lineCount = 5;
    private int highlightLineIndex = 3;
    private float scrollSpeed = 1.0f;
    private int scrollCurve = 0;
    private float lineSpacing = 1.0f;
    private int scrollDirection = 0;
    private int normalColor = 1728053247;
    private float normalAlpha = 0.6f;
    private float normalScale = 1.0f;
    private int highlightColor = -1;
    private float highlightAlpha = 1.0f;
    private float highlightScale = 1.2f;
    private float highlightYOffset = 0.0f;
    private int highlightTransition = 0;
    private float fadeDuration = 300.0f;
    private float scaleInDuration = 300.0f;
    private float scaleOutDuration = 300.0f;
    private int scaleInCurve = 0;
    private int scaleOutCurve = 0;
    private int animType = 0;
    private int lineIndex = 0;
    private int timeOffset = 0;
    private float currentScrollOffset = 0.0f;
    private long lastScrollUpdateTime = 0;
    private int lastCurrentIndex = -1;
    private int previousIndex = -1;
    private float transitionProgress = 1.0f;
    private long transitionStartTime = 0;
    private HashMap<String, SpriteFont> fontCache = new HashMap<>();
    private HashMap<String, Float> visualCenterCache = new HashMap<>();
    private String currentSongPath = null;
    private boolean showFallbackText = true;
    private String templateFolderPath = "";

    public static void injectExportTime(long j) {
        sInjectedTimeMs = j;
    }

    public LyricsElement() {
        setCustomFontPath("internal_3");
        setFontSize(30);
        setPosition(0.0f, 0.5f);
        setLocalPosition(0.0f, 0.0f);
        setTextAlign(0);
        setText(FALLBACK_TEXT);
        tryLoadLyrics();
    }

    private float applyCurve(float f, int i) {
        switch (i) {
            case 0:
            default:
                return f;
            case 1:
                return f * f;
            case 2:
                return f * f;
            case 3:
                return f * f * f;
            case 4:
                return 1.0f - ((1.0f - f) * (1.0f - f));
            case 5:
                return 1.0f - ((1.0f - f) * (1.0f - f));
            case 6:
                return 1.0f - ((float) Math.pow(1.0f - f, 3.0d));
            case 7:
                return f < 0.5f ? 2.0f * f * f : 1.0f - (((float) Math.pow(((-2.0f) * f) + 2.0f, 2.0d)) / 2.0f);
            case 8:
                return f < 0.5f ? 2.0f * f * f : 1.0f - (((float) Math.pow(((-2.0f) * f) + 2.0f, 2.0d)) / 2.0f);
            case 9:
                return f < 0.5f ? 4.0f * f * f * f : 1.0f - (((float) Math.pow(((-2.0f) * f) + 2.0f, 3.0d)) / 2.0f);
            case 10:
                return 1.0f - ((float) Math.sqrt(1.0f - (f * f)));
            case 11:
                return ((((1.70158f + 1.0f) * f) * f) * f) - ((1.70158f * f) * f);
            case 12:
                if (f < 1.0f / 2.75f) {
                    return f * 7.5625f * f;
                }
                if (f < 2.0f / 2.75f) {
                    float f2 = f - (1.5f / 2.75f);
                    return (7.5625f * f2 * f2) + 0.75f;
                }
                if (f < 2.5f / 2.75f) {
                    float f3 = f - (2.25f / 2.75f);
                    return (7.5625f * f3 * f3) + 0.9375f;
                }
                float f4 = f - (2.625f / 2.75f);
                return (7.5625f * f4 * f4) + 0.984375f;
            case 13:
                if (f != 0.0f && f != 1.0f) {
                    return Math.max(0.0f, Math.min(1.0f, ((float) Math.pow(2.0d, (-10.0f) * f)) * ((float) Math.sin((f - 0.075f) * 20.0f * 3.1415927f))));
                }
                return f;
            case 14:
                return 1.0f - ((float) Math.cos((3.1415927f * f) / 2.0f));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:24:0x004c A[Catch: Exception -> 0x006f, TryCatch #0 {Exception -> 0x006f, blocks: (B:17:0x002e, B:19:0x0032, B:21:0x003a, B:23:0x0047, B:24:0x004c, B:26:0x0057, B:27:0x005c, B:29:0x0066), top: B:78:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:26:0x0057 A[Catch: Exception -> 0x006f, TryCatch #0 {Exception -> 0x006f, blocks: (B:17:0x002e, B:19:0x0032, B:21:0x003a, B:23:0x0047, B:24:0x004c, B:26:0x0057, B:27:0x005c, B:29:0x0066), top: B:78:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:27:0x005c A[Catch: Exception -> 0x006f, TryCatch #0 {Exception -> 0x006f, blocks: (B:17:0x002e, B:19:0x0032, B:21:0x003a, B:23:0x0047, B:24:0x004c, B:26:0x0057, B:27:0x005c, B:29:0x0066), top: B:78:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:29:0x0066 A[Catch: Exception -> 0x006f, TRY_LEAVE, TryCatch #0 {Exception -> 0x006f, blocks: (B:17:0x002e, B:19:0x0032, B:21:0x003a, B:23:0x0047, B:24:0x004c, B:26:0x0057, B:27:0x005c, B:29:0x0066), top: B:78:0x002e }] */
    /* JADX WARN: Code duplicated, block: B:33:0x0073  */
    /* JADX WARN: Code duplicated, block: B:41:0x0092 A[Catch: Exception -> 0x00e6, TryCatch #3 {Exception -> 0x00e6, blocks: (B:39:0x008b, B:40:0x008f, B:41:0x0092, B:42:0x0096, B:44:0x009e, B:45:0x00aa, B:47:0x00b2, B:48:0x00be, B:50:0x00c6, B:51:0x00d2, B:53:0x00da), top: B:85:0x008b }] */
    private Typeface loadTypeface(RenderState renderState, String str) {
        Typeface typefaceCreateFromAsset;
        File file;
        Context appContext;
        Typeface typefaceCreateFromAsset2;
        InternalVisualizationDataProvider internalVisualizationDataProvider;
        if (str == null || str.isEmpty()) {
            return Typeface.DEFAULT;
        }
        if (renderState != null && (internalVisualizationDataProvider = renderState.res.visualizationData) != null) {
            try {
                Typeface typefaceOnRequestFont = internalVisualizationDataProvider.onRequestFont(str, AppPreferences.getCurrentThemeId());
                if (typefaceOnRequestFont != null && typefaceOnRequestFont != Typeface.DEFAULT) {
                    return typefaceOnRequestFont;
                }
            } catch (Exception e) {
            }
        }
        if (str.startsWith(LocalFontLoader.Prefix)) {
            String strSubstring = str.substring(5);
            try {
                if (this.templateFolderPath == null || this.templateFolderPath.isEmpty()) {
                    file = new File(strSubstring);
                    if (file.exists()) {
                        typefaceCreateFromAsset2 = Typeface.createFromFile(file);
                    } else {
                        appContext = PlayerCore.s().getAppContext();
                        if (appContext != null) {
                            typefaceCreateFromAsset2 = Typeface.createFromAsset(appContext.getAssets(), strSubstring);
                        } else {
                            typefaceCreateFromAsset2 = Typeface.DEFAULT;
                        }
                    }
                } else {
                    File file2 = new File(this.templateFolderPath, strSubstring);
                    if (file2.exists()) {
                        typefaceCreateFromAsset2 = Typeface.createFromFile(file2);
                    } else {
                        file = new File(strSubstring);
                        if (file.exists()) {
                            typefaceCreateFromAsset2 = Typeface.createFromFile(file);
                        } else {
                            appContext = PlayerCore.s().getAppContext();
                            if (appContext != null) {
                                typefaceCreateFromAsset2 = Typeface.createFromAsset(appContext.getAssets(), strSubstring);
                            } else {
                                typefaceCreateFromAsset2 = Typeface.DEFAULT;
                            }
                        }
                    }
                }
                return typefaceCreateFromAsset2;
            } catch (Exception e2) {
                return Typeface.DEFAULT;
            }
        }
        if (!str.startsWith("internal_")) {
            if ("default_bold".equals(str)) {
                return Typeface.DEFAULT_BOLD;
            }
            try {
                File file3 = new File(str);
                if (file3.exists()) {
                    return Typeface.createFromFile(file3);
                }
            } catch (Exception e3) {
            }
            try {
                Context appContext2 = PlayerCore.s().getAppContext();
                if (appContext2 != null && !str.startsWith("/") && !str.startsWith("http")) {
                    return Typeface.createFromAsset(appContext2.getAssets(), str);
                }
            } catch (Exception e4) {
            }
            return Typeface.DEFAULT;
        }
        Context appContext3 = PlayerCore.s().getAppContext();
        if (appContext3 == null) {
            return Typeface.DEFAULT;
        }
        try {
            switch (str) {
                case "internal_0":
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext3.getAssets(), "fonts/greatvibes-regular.otf");
                    break;
                case "internal_1":
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext3.getAssets(), "fonts/notomono-regular.ttf");
                    break;
                case "internal_2":
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext3.getAssets(), "fonts/orbitron-medium.otf");
                    break;
                case "internal_3":
                    typefaceCreateFromAsset = Typeface.createFromAsset(appContext3.getAssets(), "fonts/orkney-light.otf");
                    break;
                default:
                    typefaceCreateFromAsset = Typeface.DEFAULT;
                    break;
            }
            return typefaceCreateFromAsset;
        } catch (Exception e5) {
            return Typeface.DEFAULT;
        }
    }

    private SpriteFont getFontForSize(RenderState renderState, int i) {
        String customFontPath = getCustomFontPath();
        String str = String.valueOf(customFontPath) + "_" + i;
        if (this.fontCache.containsKey(str)) {
            SpriteFont spriteFont = this.fontCache.get(str);
            if (spriteFont == null || !spriteFont.isValid()) {
                this.fontCache.remove(str);
            } else {
                return spriteFont;
            }
        }
        SpriteFont spriteFont2 = new SpriteFont(loadTypeface(renderState, customFontPath), i, CharSet.createAscii32to126());
        this.fontCache.put(str, spriteFont2);
        return spriteFont2;
    }

    private float getVisualCenterOffset(Typeface typeface, int i, String str) {
        String str2 = String.valueOf(typeface.toString()) + "_" + i + "_" + str;
        if (this.visualCenterCache.containsKey(str2)) {
            return this.visualCenterCache.get(str2).floatValue();
        }
        float fMax = 0.35f;
        try {
            Paint paint = new Paint();
            paint.setTypeface(typeface);
            paint.setTextSize(i);
            paint.setAntiAlias(true);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float f = -fontMetrics.ascent;
            float f2 = fontMetrics.descent;
            float f3 = f + f2;
            if (f3 > 0.0f) {
                fMax = Math.max(0.2f, Math.min(0.6f, (f2 - f) / (2.0f * f3)));
                this.visualCenterCache.put(str2, Float.valueOf(fMax));
                return fMax;
            }
        } catch (Exception e) {
        }
        try {
            Paint paint2 = new Paint();
            paint2.setTypeface(typeface);
            paint2.setTextSize(i);
            paint2.setAntiAlias(true);
            Rect rect = new Rect();
            paint2.getTextBounds(str, 0, Math.min(str.length(), 10), rect);
            float f4 = rect.top;
            float f5 = rect.bottom;
            float f6 = f5 - f4;
            if (f6 > 0.0f) {
                fMax = Math.max(0.2f, Math.min(0.6f, (-(f4 + f5)) / (2.0f * f6)));
                this.visualCenterCache.put(str2, Float.valueOf(fMax));
                return fMax;
            }
        } catch (Exception e2) {
        }
        this.visualCenterCache.put(str2, Float.valueOf(fMax));
        return fMax;
    }

    private float getVisualCenterOffset(SpriteFont spriteFont, String str) {
        int iIntValue = 30;
        Typeface typeface = Typeface.DEFAULT;
        try {
            Field declaredField = SpriteFont.class.getDeclaredField("typeface");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(spriteFont);
            if (obj instanceof Typeface) {
                typeface = (Typeface) obj;
            }
        } catch (Exception e) {
        }
        try {
            Field declaredField2 = SpriteFont.class.getDeclaredField("size");
            declaredField2.setAccessible(true);
            Object obj2 = declaredField2.get(spriteFont);
            if (!(obj2 instanceof Integer)) {
                iIntValue = 30;
            } else {
                iIntValue = ((Integer) obj2).intValue();
            }
        } catch (Exception e2) {
        }
        return getVisualCenterOffset(typeface, iIntValue, str);
    }

    private void tryLoadLyrics() {
        // ── 严格按原版 smali 还原 ──
        // 原版是【两个独立的 try 区】，共用同一个 :catch_0 处理器：
        //   :try_start_0 .. :try_end_0  覆盖 getAppContext / getInstance / getCurrentSong
        //   :try_start_1 .. :try_end_1  覆盖 path 比较 / getDataBlocking / lyricsData 判断
        // jadx 把两段合并成一个 try，把 currentSong==null 的 else 分支也塞进内层，
        // 导致 resetToFallback 调用点从 4 处降到 3 处、指令数 62→53。
        // 语义差别在于：内层异常同样落到 catch_0（行为一致），但 jadx 版的
        // “currentSong != null 才进入 path 比较”与“path 相等就直接跳过”两处
        // 短路分支被重排，会让 currentSongPath 的写入时机偏移。
        Context appContext;
        MediaPlaybackService mediaPlaybackService;
        PlaylistSong currentSong;
        try {
            appContext = PlayerCore.s().getAppContext();
            if (appContext == null) {
                // 原版 :cond_1 → if-nez v1 失败即跳到 :cond_0 :goto_0 return-void
                return;
            }
            mediaPlaybackService = MediaPlaybackService.getInstance();
            if (mediaPlaybackService == null) {
                return;
            }
            currentSong = mediaPlaybackService.getCurrentSong();
            if (currentSong == null) {
                resetToFallback();
                return;
            }
        } catch (Exception e) {
            resetToFallback();
            return;
        }

        try {
            String construcPath = currentSong.getConstrucPath();
            if (construcPath == null) {
                construcPath = "";
            }
            String str = this.currentSongPath;
            if (construcPath.equals(str) && str != null) {
                // 原版：path 相同 → :cond_0 :goto_0 return-void（不做任何重载）
                return;
            }
            this.currentSongPath = construcPath;
            PlaylistSong.Data dataBlocking = currentSong.getDataBlocking(appContext);
            if (dataBlocking == null) {
                resetToFallback();
                return;
            }
            LyricsData lyricsData = dataBlocking.lyricsData;
            if (lyricsData != null && !lyricsData.isEmpty()) {
                this.lyricsData = lyricsData;
                this.showFallbackText = false;
                markNeedReCreateGLResources();
                return;
            }
            resetToFallback();
        } catch (Exception e) {
            resetToFallback();
        }
    }

    private void resetToFallback() {
        this.lyricsData = null;
        this.showFallbackText = true;
        setText(FALLBACK_TEXT);
        markNeedReCreateGLResources();
    }

    public void setLyricsData(LyricsData lyricsData) {
        if (lyricsData == null || lyricsData.isEmpty()) {
            resetToFallback();
            return;
        }
        this.lyricsData = lyricsData;
        this.showFallbackText = false;
        markNeedReCreateGLResources();
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onDestroyGLResources(RenderState renderState) {
        super.onDestroyGLResources(renderState);
        for (SpriteFont spriteFont : this.fontCache.values()) {
            if (spriteFont != null) {
                spriteFont.dispose();
            }
        }
        this.fontCache.clear();
        this.visualCenterCache.clear();
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
        setColor(customPropertiesList.getPropertyInt("color", -1));
        setFontSize(customPropertiesList.getPropertyInt("fontSize", 30));
        String propertyString = customPropertiesList.getPropertyString("typeFace", "");
        if (propertyString != null && !propertyString.isEmpty()) {
            setCustomFontPath(propertyString);
            for (SpriteFont spriteFont : this.fontCache.values()) {
                if (spriteFont != null) {
                    spriteFont.dispose();
                }
            }
            this.fontCache.clear();
            if (this.font1 != null) {
                this.font1.dispose();
                this.font1 = null;
            }
            markNeedReCreateGLResources();
        }
        setTextAlign(customPropertiesList.getPropertySelectStringIndex("textAlign", alignModes, 0));
        this.lineIndex = customPropertiesList.getPropertyInt("lineIndex", 0);
        if (this.lineIndex < 0) {
            this.lineIndex = 0;
        }
        if (this.lineIndex > 1) {
            this.lineIndex = 1;
        }
        this.timeOffset = customPropertiesList.getPropertyInt("timeOffset", 0);
        if (this.timeOffset < -1000) {
            this.timeOffset = -1000;
        }
        if (this.timeOffset > 1000) {
            this.timeOffset = 1000;
        }
        this.animType = customPropertiesList.getPropertySelectStringIndex("animType", animTypeNames, this.animType);
        CustomPropertiesList child = customPropertiesList.getChild("fade");
        if (child != null) {
            this.fadeInDuration = child.getPropertyInt("fadeInDuration", 300);
            this.fadeOutDuration = child.getPropertyInt("fadeOutDuration", 300);
            this.fadeInCurve = child.getPropertySelectStringIndex("fadeInCurve", curveNames, 0);
            this.fadeOutCurve = child.getPropertySelectStringIndex("fadeOutCurve", curveNames, 0);
            if (this.fadeInDuration < 0) {
                this.fadeInDuration = 0;
            }
            if (this.fadeOutDuration < 0) {
                this.fadeOutDuration = 0;
            }
        }
        CustomPropertiesList child2 = customPropertiesList.getChild("move");
        if (child2 != null) {
            this.moveStartY = child2.getPropertyFloat("moveStartY", 0.0f);
            this.moveEndY = child2.getPropertyFloat("moveEndY", 0.0f);
            this.moveDurationStart = child2.getPropertyInt("moveDurationStart", 1000);
            this.moveDurationEnd = child2.getPropertyInt("moveDurationEnd", 1000);
            this.moveInCurve = child2.getPropertySelectStringIndex("moveInCurve", curveNames, 0);
            this.moveOutCurve = child2.getPropertySelectStringIndex("moveOutCurve", curveNames, 0);
            if (this.moveDurationStart < 0) {
                this.moveDurationStart = 0;
            }
            if (this.moveDurationEnd < 0) {
                this.moveDurationEnd = 0;
            }
        }
        CustomPropertiesList child3 = customPropertiesList.getChild("scroll");
        if (child3 != null) {
            CustomPropertiesList child4 = child3.getChild("lyrics");
            if (child4 != null) {
                this.lineCount = child4.getPropertyInt("lineCount", 5);
                if (this.lineCount < 1) {
                    this.lineCount = 1;
                }
                if (this.lineCount > 15) {
                    this.lineCount = 15;
                }
                this.lineSpacing = child4.getPropertyFloat("lineSpacing", 1.0f);
                if (this.lineSpacing < 0.5f) {
                    this.lineSpacing = 0.5f;
                }
                if (this.lineSpacing > 2.0f) {
                    this.lineSpacing = 2.0f;
                }
                this.scrollDirection = child4.getPropertySelectStringIndex("scrollDirection", directionNames, 0);
                this.highlightLineIndex = child4.getPropertyInt("highlightLineIndex", 3);
                if (this.highlightLineIndex < 1) {
                    this.highlightLineIndex = 1;
                }
                if (this.highlightLineIndex > this.lineCount) {
                    this.highlightLineIndex = this.lineCount;
                }
            }
            CustomPropertiesList child5 = child3.getChild("normal");
            if (child5 != null) {
                this.normalColor = child5.getPropertyInt("normalColor", 1728053247);
                this.normalAlpha = child5.getPropertyFloat("normalAlpha", 0.6f);
                this.normalScale = child5.getPropertyFloat("normalScale", 1.0f);
                if (this.normalScale < 0.5f) {
                    this.normalScale = 0.5f;
                }
                if (this.normalScale > 2.0f) {
                    this.normalScale = 2.0f;
                }
            }
            CustomPropertiesList child6 = child3.getChild("animation");
            if (child6 != null) {
                this.scrollSpeed = child6.getPropertyFloat("scrollSpeed", 1.0f);
                if (this.scrollSpeed < 0.1f) {
                    this.scrollSpeed = 0.1f;
                }
                if (this.scrollSpeed > 5.0f) {
                    this.scrollSpeed = 5.0f;
                }
                this.scrollCurve = child6.getPropertySelectStringIndex("scrollCurve", curveNames, 0);
                this.highlightTransition = child6.getPropertySelectStringIndex("highlightTransition", transitionNames, 0);
                if (this.highlightTransition == 1 || this.highlightTransition == 3) {
                    this.fadeDuration = child6.getPropertyFloat("fadeDuration", 300.0f);
                    if (this.fadeDuration < 50.0f) {
                        this.fadeDuration = 50.0f;
                    }
                    if (this.fadeDuration > 1000.0f) {
                        this.fadeDuration = 1000.0f;
                    }
                } else {
                    this.fadeDuration = 300.0f;
                }
                if (this.highlightTransition == 2 || this.highlightTransition == 3) {
                    this.scaleInDuration = child6.getPropertyFloat("scaleInDuration", 300.0f);
                    this.scaleOutDuration = child6.getPropertyFloat("scaleOutDuration", 300.0f);
                    this.scaleInCurve = child6.getPropertySelectStringIndex("scaleInCurve", curveNames, 0);
                    this.scaleOutCurve = child6.getPropertySelectStringIndex("scaleOutCurve", curveNames, 0);
                    if (this.scaleInDuration < 50.0f) {
                        this.scaleInDuration = 50.0f;
                    }
                    if (this.scaleInDuration > 1000.0f) {
                        this.scaleInDuration = 1000.0f;
                    }
                    if (this.scaleOutDuration < 50.0f) {
                        this.scaleOutDuration = 50.0f;
                    }
                    if (this.scaleOutDuration > 1000.0f) {
                        this.scaleOutDuration = 1000.0f;
                    }
                } else {
                    this.scaleInDuration = 300.0f;
                    this.scaleOutDuration = 300.0f;
                    this.scaleInCurve = 0;
                    this.scaleOutCurve = 0;
                }
            }
            CustomPropertiesList child7 = child3.getChild("highlight");
            if (child7 != null) {
                this.highlightColor = child7.getPropertyInt("highlightColor", -1);
                this.highlightAlpha = child7.getPropertyFloat("highlightAlpha", 1.0f);
                this.highlightScale = child7.getPropertyFloat("highlightScale", 1.2f);
                if (this.highlightScale < 0.5f) {
                    this.highlightScale = 0.5f;
                }
                if (this.highlightScale > 2.5f) {
                    this.highlightScale = 2.5f;
                }
                this.highlightYOffset = child7.getPropertyFloat("highlightYOffset", 0.0f);
                if (this.highlightYOffset < -50.0f) {
                    this.highlightYOffset = -50.0f;
                }
                if (this.highlightYOffset > 50.0f) {
                    this.highlightYOffset = 50.0f;
                }
            }
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        super.onReadCustomization(customPropertiesList, iDependencyResourceCounter);
        customPropertiesList.setCustomizationName(MainActivity.getInstance().getString(R.string.element_title_bar_name_lyrics));
        customPropertiesList.putPropertyIntAsCRGBA("color", this.color1, "appearance");
        customPropertiesList.putPropertyIntAsRange("fontSize", this.fontSize, "appearance", 8, 100);
        customPropertiesList.putPropertyStringAsAssetPr("typeFace", getCustomFontPath(), "appearance", fontTypefaces);
        customPropertiesList.putSelectString("textAlign", alignModes[this.textAlign], "appearance", alignModes);
        customPropertiesList.putPropertyIntAsRange("lineIndex", this.lineIndex, "index", 0, 1);
        customPropertiesList.putPropertyIntAsRange("timeOffset", this.timeOffset, "index", -1000, 1000);
        customPropertiesList.putSelectString("animType", animTypeNames[this.animType], "animation", animTypeNames);
        if (this.animType == 1) {
            CustomPropertiesList customPropertiesListPutEmptyChild = customPropertiesList.putEmptyChild("fade", "", "animation", new String[0]);
            customPropertiesListPutEmptyChild.putPropertyIntAsRange("fadeInDuration", this.fadeInDuration, "", 0, PathInterpolatorCompat.MAX_NUM_POINTS);
            customPropertiesListPutEmptyChild.putPropertyIntAsRange("fadeOutDuration", this.fadeOutDuration, "", 0, PathInterpolatorCompat.MAX_NUM_POINTS);
            customPropertiesListPutEmptyChild.putSelectString("fadeInCurve", curveNames[this.fadeInCurve], "", curveNames);
            customPropertiesListPutEmptyChild.putSelectString("fadeOutCurve", curveNames[this.fadeOutCurve], "", curveNames);
        }
        if (this.animType == 2) {
            CustomPropertiesList customPropertiesListPutEmptyChild2 = customPropertiesList.putEmptyChild("move", "", "animation", new String[0]);
            customPropertiesListPutEmptyChild2.putPropertyFloatAsRange("moveStartY", this.moveStartY, "", -200.0f, 200.0f);
            customPropertiesListPutEmptyChild2.putPropertyFloatAsRange("moveEndY", this.moveEndY, "", -200.0f, 200.0f);
            customPropertiesListPutEmptyChild2.putPropertyIntAsRange("moveDurationStart", this.moveDurationStart, "", 0, PathInterpolatorCompat.MAX_NUM_POINTS);
            customPropertiesListPutEmptyChild2.putPropertyIntAsRange("moveDurationEnd", this.moveDurationEnd, "", 0, PathInterpolatorCompat.MAX_NUM_POINTS);
            customPropertiesListPutEmptyChild2.putSelectString("moveInCurve", curveNames[this.moveInCurve], "", curveNames);
            customPropertiesListPutEmptyChild2.putSelectString("moveOutCurve", curveNames[this.moveOutCurve], "", curveNames);
        }
        if (this.animType == 3) {
            CustomPropertiesList customPropertiesListPutEmptyChild3 = customPropertiesList.putEmptyChild("scroll", "", "animation", new String[0]);
            CustomPropertiesList customPropertiesListPutEmptyChild4 = customPropertiesListPutEmptyChild3.putEmptyChild("lyrics", "", "", new String[0]);
            customPropertiesListPutEmptyChild4.putPropertyIntAsRange("lineCount", this.lineCount, "", 1, 15);
            customPropertiesListPutEmptyChild4.putPropertyFloatAsRange("lineSpacing", this.lineSpacing, "", 0.5f, 2.0f);
            customPropertiesListPutEmptyChild4.putSelectString("scrollDirection", directionNames[this.scrollDirection], "", directionNames);
            customPropertiesListPutEmptyChild4.putPropertyIntAsRange("highlightLineIndex", this.highlightLineIndex, "", 1, 15);
            CustomPropertiesList customPropertiesListPutEmptyChild5 = customPropertiesListPutEmptyChild3.putEmptyChild("normal", "", "", new String[0]);
            customPropertiesListPutEmptyChild5.putPropertyIntAsCRGBA("normalColor", this.normalColor, "");
            customPropertiesListPutEmptyChild5.putPropertyFloatAsRange("normalAlpha", this.normalAlpha, "", 0.0f, 1.0f);
            customPropertiesListPutEmptyChild5.putPropertyFloatAsRange("normalScale", this.normalScale, "", 0.5f, 2.0f);
            CustomPropertiesList customPropertiesListPutEmptyChild6 = customPropertiesListPutEmptyChild3.putEmptyChild("animation", "", "", new String[0]);
            customPropertiesListPutEmptyChild6.putPropertyFloatAsRange("scrollSpeed", this.scrollSpeed, "", 0.1f, 5.0f);
            customPropertiesListPutEmptyChild6.putSelectString("scrollCurve", curveNames[this.scrollCurve], "", curveNames);
            customPropertiesListPutEmptyChild6.putSelectString("highlightTransition", transitionNames[this.highlightTransition], "", transitionNames);
            if (this.highlightTransition == 1 || this.highlightTransition == 3) {
                customPropertiesListPutEmptyChild6.putPropertyFloatAsRange("fadeDuration", this.fadeDuration, "", 50.0f, 1000.0f);
            }
            if (this.highlightTransition == 2 || this.highlightTransition == 3) {
                customPropertiesListPutEmptyChild6.putPropertyFloatAsRange("scaleInDuration", this.scaleInDuration, "", 50.0f, 1000.0f);
                customPropertiesListPutEmptyChild6.putPropertyFloatAsRange("scaleOutDuration", this.scaleOutDuration, "", 50.0f, 1000.0f);
                customPropertiesListPutEmptyChild6.putSelectString("scaleInCurve", curveNames[this.scaleInCurve], "", curveNames);
                customPropertiesListPutEmptyChild6.putSelectString("scaleOutCurve", curveNames[this.scaleOutCurve], "", curveNames);
            }
            CustomPropertiesList customPropertiesListPutEmptyChild7 = customPropertiesListPutEmptyChild3.putEmptyChild("highlight", "", "", new String[0]);
            customPropertiesListPutEmptyChild7.putPropertyIntAsCRGBA("highlightColor", this.highlightColor, "");
            customPropertiesListPutEmptyChild7.putPropertyFloatAsRange("highlightAlpha", this.highlightAlpha, "", 0.0f, 1.0f);
            customPropertiesListPutEmptyChild7.putPropertyFloatAsRange("highlightScale", this.highlightScale, "", 0.5f, 2.5f);
            customPropertiesListPutEmptyChild7.putPropertyFloatAsRange("highlightYOffset", this.highlightYOffset, "", -50.0f, 50.0f);
        }
        String customFontPath = getCustomFontPath();
        if (customFontPath != null && !customFontPath.isEmpty() && iDependencyResourceCounter != null) {
            iDependencyResourceCounter.PutDependencyResourceName(customFontPath);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRender(RenderState renderState, FrameBuffer frameBuffer) {
        tryLoadLyrics();
        // ── 原版 smali（onRender）恢复 ──
        //   @8  if-eqz v0, :cond_2      // isEmpty()==false 才继续去渲染歌词
        //   :cond_0  invoke-super onRender(renderState, frameBuffer)
        //   :goto_0  return-void        // ★ 画完 fallback 立即返回
        // jadx 丢掉了这个 return。缺失后当 lyricsData 为 null（还没解析出歌词、
        // 或无歌词时的 resetToFallback）会继续执行 this.lyricsData.type → 每帧 NPE
        // → GL 线程被异常中断 → 画面卡死在一帧并反复闪黑。
        // 这正是“添加歌词元素后一直抽搐闪黑”的根因。
        if (this.showFallbackText || this.lyricsData == null || this.lyricsData.isEmpty()) {
            super.onRender(renderState, frameBuffer);
            return;
        }
        if (this.lyricsData.type == 1) {
            renderPlain(renderState);
            return;
        }
        if (this.lyricsData.type == 2) {
            long currentPosition = getCurrentPosition() + ((long) this.timeOffset);
            switch (this.animType) {
                case 1:
                    renderFade(renderState, currentPosition);
                    break;
                case 2:
                    renderMove(renderState, currentPosition);
                    break;
                case 3:
                    renderScroll(renderState, currentPosition);
                    break;
                default:
                    renderBasic(renderState, currentPosition);
                    break;
            }
        }
    }

    private void renderBasic(RenderState renderState, long j) {
        if (this.font1 == null || !this.font1.isValid()) {
            onCreateGLResources(renderState);
            if (this.font1 == null || !this.font1.isValid()) {
                return;
            }
        }
        String lineAtTime = getLineAtTime(j);
        if (lineAtTime != null) {
            renderLine(renderState, lineAtTime, 1.0f, 0.0f, 1.0f, this.color1);
        }
    }

    private void renderFade(RenderState renderState, long j) {
        if (this.font1 == null || !this.font1.isValid()) {
            onCreateGLResources(renderState);
            if (this.font1 == null || !this.font1.isValid()) {
                return;
            }
        }
        String lineAtTime = getLineAtTime(j);
        if (lineAtTime != null) {
            long[] lineTiming = getLineTiming(j);
            renderLine(renderState, lineAtTime, calculateFadeAlpha(lineTiming[3], lineTiming[2]), 0.0f, 1.0f, this.color1);
        }
    }

    private void renderMove(RenderState renderState, long j) {
        if (this.font1 == null || !this.font1.isValid()) {
            onCreateGLResources(renderState);
            if (this.font1 == null || !this.font1.isValid()) {
                return;
            }
        }
        String lineAtTime = getLineAtTime(j);
        if (lineAtTime != null) {
            long[] lineTiming = getLineTiming(j);
            renderLine(renderState, lineAtTime, 1.0f, calculateMoveOffsetY(lineTiming[3], lineTiming[2]), 1.0f, this.color1);
        }
    }

    private void renderScroll(RenderState renderState, long j) {
        int i;
        int size;
        int i2;
        float f;
        int iBlendColors;
        float f2;
        float f3;
        if (this.font1 == null || !this.font1.isValid()) {
            onCreateGLResources(renderState);
            if (this.font1 == null || !this.font1.isValid()) {
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= this.lyricsData.lines.size()) {
                break;
            }
            if (i4 < this.lyricsData.lines.size() - 1 && this.lyricsData.lines.get(i4).timeMs == this.lyricsData.lines.get(i4 + 1).timeMs) {
                if (this.lineIndex == 0) {
                    arrayList.add(this.lyricsData.lines.get(i4));
                    i4++;
                } else {
                    i4++;
                    arrayList.add(this.lyricsData.lines.get(i4));
                }
            } else {
                arrayList.add(this.lyricsData.lines.get(i4));
            }
            i3 = i4 + 1;
        }
        if (!arrayList.isEmpty()) {
            int i5 = 0;
            int i6 = 0;
            while (true) {
                int i7 = i6;
                i = i5;
                if (i7 >= arrayList.size()) {
                    break;
                }
                i5 = ((LyricsData.LyricLine) arrayList.get(i7)).timeMs <= j ? i7 : i;
                i6 = i7 + 1;
            }
            if (this.lastCurrentIndex != i) {
                this.previousIndex = this.lastCurrentIndex;
                this.lastCurrentIndex = i;
                this.transitionProgress = 0.0f;
                this.transitionStartTime = System.currentTimeMillis();
            }
            if (this.transitionProgress < 1.0f) {
                this.transitionProgress = Math.min(1.0f, (System.currentTimeMillis() - this.transitionStartTime) / getTransitionDuration());
            }
            float f4 = this.fontSize * 1.6f * this.lineSpacing;
            float fCenterY = measureDrawRect(renderState.res.getMeter()).centerY();
            int size2 = i - (this.highlightLineIndex - 1);
            int size3 = (this.lineCount + size2) - 1;
            if (size2 < 0) {
                size3 -= size2;
                size2 = 0;
            }
            if (size3 >= arrayList.size()) {
                size2 -= (size3 - arrayList.size()) + 1;
                size3 = arrayList.size() - 1;
                if (size2 < 0) {
                    size2 = 0;
                }
            }
            if ((size3 - size2) + 1 < this.lineCount) {
                int i8 = this.lineCount / 2;
                int i9 = i - i8;
                int i10 = ((this.lineCount - i8) - 1) + i;
                if (i9 < 0) {
                    i9 = 0;
                }
                if (i10 >= arrayList.size()) {
                    size = arrayList.size() - 1;
                    i2 = i9;
                } else {
                    size = i10;
                    i2 = i9;
                }
            } else {
                size = size3;
                i2 = size2;
            }
            float f5 = (i * f4) - (((this.lineCount - 1) * f4) / 2.0f);
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (this.lastScrollUpdateTime == 0) {
                this.lastScrollUpdateTime = jCurrentTimeMillis;
            }
            float fMin = Math.min((jCurrentTimeMillis - this.lastScrollUpdateTime) / 1000.0f, 0.05f);
            this.lastScrollUpdateTime = jCurrentTimeMillis;
            this.currentScrollOffset = ((f5 - this.currentScrollOffset) * applyCurve(Math.min(1.0f, fMin * this.scrollSpeed * 3.0f), this.scrollCurve)) + this.currentScrollOffset;
            float f6 = this.scrollDirection == 0 ? 1.0f : -1.0f;
            int i11 = i2;
            while (i11 <= size) {
                float f7 = fCenterY - ((this.currentScrollOffset - (i11 * f4)) * f6);
                boolean z = i11 == i;
                boolean z2 = i11 == this.previousIndex;
                if (this.highlightTransition != 0 && this.transitionProgress < 1.0f) {
                    if (z) {
                        if (this.highlightTransition == 1) {
                            float fApplyCurve = applyCurve(this.transitionProgress, 0);
                            f = this.normalAlpha + ((this.highlightAlpha - this.normalAlpha) * fApplyCurve);
                            iBlendColors = blendColors(this.normalColor, this.highlightColor, fApplyCurve);
                            f2 = this.normalScale;
                            f3 = 0.0f;
                        } else if (this.highlightTransition == 2) {
                            float fApplyCurve2 = applyCurve(this.transitionProgress, this.scaleInCurve);
                            f = this.normalAlpha;
                            iBlendColors = this.normalColor;
                            f2 = this.normalScale + ((this.highlightScale - this.normalScale) * fApplyCurve2);
                            f3 = fApplyCurve2 * this.highlightYOffset;
                        } else if (this.highlightTransition == 3) {
                            float fApplyCurve3 = applyCurve(this.transitionProgress, 0);
                            float fApplyCurve4 = applyCurve(this.transitionProgress, 0);
                            f = ((this.highlightAlpha - this.normalAlpha) * fApplyCurve3) + this.normalAlpha;
                            iBlendColors = blendColors(this.normalColor, this.highlightColor, fApplyCurve3);
                            f2 = this.normalScale + ((this.highlightScale - this.normalScale) * fApplyCurve4);
                            f3 = this.highlightYOffset * fApplyCurve4;
                        } else {
                            f = this.normalAlpha;
                            iBlendColors = this.normalColor;
                            f2 = this.normalScale;
                            f3 = 0.0f;
                        }
                    } else if (z2) {
                        if (this.highlightTransition == 1) {
                            float fApplyCurve5 = 1.0f - applyCurve(this.transitionProgress, 0);
                            f = this.normalAlpha + ((this.highlightAlpha - this.normalAlpha) * fApplyCurve5);
                            iBlendColors = blendColors(this.normalColor, this.highlightColor, fApplyCurve5);
                            f2 = this.normalScale;
                            f3 = 0.0f;
                        } else if (this.highlightTransition == 2) {
                            float fApplyCurve6 = 1.0f - applyCurve(this.transitionProgress, this.scaleOutCurve);
                            f = this.normalAlpha;
                            iBlendColors = this.normalColor;
                            f2 = this.normalScale + ((this.highlightScale - this.normalScale) * fApplyCurve6);
                            f3 = fApplyCurve6 * this.highlightYOffset;
                        } else if (this.highlightTransition == 3) {
                            float fApplyCurve7 = 1.0f - applyCurve(this.transitionProgress, 0);
                            float fApplyCurve8 = 1.0f - applyCurve(this.transitionProgress, 0);
                            f = ((this.highlightAlpha - this.normalAlpha) * fApplyCurve7) + this.normalAlpha;
                            iBlendColors = blendColors(this.normalColor, this.highlightColor, fApplyCurve7);
                            f2 = this.normalScale + ((this.highlightScale - this.normalScale) * fApplyCurve8);
                            f3 = this.highlightYOffset * fApplyCurve8;
                        } else {
                            f = this.normalAlpha;
                            iBlendColors = this.normalColor;
                            f2 = this.normalScale;
                            f3 = 0.0f;
                        }
                    } else {
                        f = this.normalAlpha;
                        iBlendColors = this.normalColor;
                        f2 = this.normalScale;
                        f3 = 0.0f;
                    }
                } else if (z) {
                    f = this.highlightAlpha;
                    iBlendColors = this.highlightColor;
                    f2 = this.highlightScale;
                    f3 = this.highlightYOffset;
                } else {
                    f = this.normalAlpha;
                    iBlendColors = this.normalColor;
                    f2 = this.normalScale;
                    f3 = 0.0f;
                }
                renderLine(renderState, ((LyricsData.LyricLine) arrayList.get(i11)).text, f, (f7 - fCenterY) + f3, f2, iBlendColors);
                i11++;
            }
        }
    }

    private float getTransitionDuration() {
        switch (this.highlightTransition) {
            case 1:
                return this.fadeDuration;
            case 2:
                return Math.max(this.scaleInDuration, this.scaleOutDuration);
            case 3:
                return Math.max(Math.max(this.fadeDuration, this.scaleInDuration), this.scaleOutDuration);
            default:
                return 300.0f;
        }
    }

    private int blendColors(int i, int i2, float f) {
        int i3 = (i >> 24) & 255;
        int i4 = (i >> 16) & 255;
        int i5 = (i >> 8) & 255;
        int i6 = i & 255;
        return (((int) (((((i2 >> 24) & 255) - i3) * f) + i3)) << 24) | (((int) (((((i2 >> 16) & 255) - i4) * f) + i4)) << 16) | (((int) (i5 + ((((i2 >> 8) & 255) - i5) * f))) << 8) | ((int) (i6 + (((i2 & 255) - i6) * f)));
    }

    private String getLineAtTime(long j) {
        if (this.lyricsData == null || this.lyricsData.lines.isEmpty()) {
            return null;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.lyricsData.lines.size(); i2++) {
            if (this.lyricsData.lines.get(i2).timeMs <= j) {
                i = i2;
            }
        }
        long j2 = this.lyricsData.lines.get(i).timeMs;
        ArrayList arrayList = new ArrayList();
        while (i >= 0 && i < this.lyricsData.lines.size() && this.lyricsData.lines.get(i).timeMs == j2) {
            arrayList.add(0, this.lyricsData.lines.get(i).text);
            i--;
        }
        if (this.lineIndex < arrayList.size()) {
            return (String) arrayList.get(this.lineIndex);
        }
        return null;
    }

    private long[] getLineTiming(long j) {
        long j2;
        int i = 0;
        for (int i2 = 0; i2 < this.lyricsData.lines.size(); i2++) {
            if (this.lyricsData.lines.get(i2).timeMs <= j) {
                i = i2;
            }
        }
        long j3 = this.lyricsData.lines.get(i).timeMs;
        if (i + 1 < this.lyricsData.lines.size()) {
            j2 = this.lyricsData.lines.get(i + 1).timeMs;
        } else {
            j2 = 3000 + j3;
        }
        long jMax = Math.max(1L, j2 - j3);
        return new long[]{j, j3, jMax, Math.max(0L, Math.min(jMax, j - j3))};
    }

    private float calculateFadeAlpha(long j, long j2) {
        float fMax = Math.max(0.0f, Math.min(1.0f, ((float) j) / ((float) j2)));
        if (j2 < this.fadeInDuration + this.fadeOutDuration) {
            return fMax < 0.5f ? fMax * 2.0f : 2.0f - (fMax * 2.0f);
        }
        float f = ((float) this.fadeInDuration) / ((float) j2);
        float f2 = 1.0f - ((((float) this.fadeOutDuration)) / ((float) j2));
        if (fMax < f) {
            return applyCurve(fMax / f, this.fadeInCurve);
        }
        if (fMax > f2) {
            return 1.0f - applyCurve((fMax - f2) / (1.0f - f2), this.fadeOutCurve);
        }
        return 1.0f;
    }

    private float calculateMoveOffsetY(long j, long j2) {
        if (j2 <= 0 || (this.moveDurationStart <= 0 && this.moveDurationEnd <= 0)) {
            return this.moveStartY;
        }
        float fMin = Math.min(this.moveDurationStart, j2);
        float fMax = Math.max(0L, j2 - ((long) this.moveDurationEnd));
        if (fMin > fMax) {
            fMin = fMax / 2.0f;
            fMax = 0.001f + fMin;
        }
        float f = j;
        if (f <= fMin) {
            return (applyCurve(Math.max(0.0f, Math.min(1.0f, f / fMin)), this.moveInCurve) * (this.moveEndY - this.moveStartY)) + this.moveStartY;
        }
        if (f >= fMax) {
            return (applyCurve(Math.max(0.0f, Math.min(1.0f, 1.0f - ((j2 - f) / this.moveDurationEnd))), this.moveOutCurve) * (this.moveStartY - this.moveEndY)) + this.moveEndY;
        }
        return this.moveEndY;
    }

    private void renderLine(RenderState renderState, String str, float f, float f2, float f3, int i) {
        Typeface typeface = Typeface.DEFAULT;
        float f4;
        int i2 = (16777215 & i) | (((int) (255.0f * f)) << 24);
        int iMax = Math.max(8, Math.min(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, (int) (this.fontSize * f3)));
        SpriteFont fontForSize = getFontForSize(renderState, iMax);
        if ((fontForSize == null || !fontForSize.isValid()) && ((fontForSize = this.font1) == null || !fontForSize.isValid())) {
            onCreateGLResources(renderState);
            fontForSize = this.font1;
            if (fontForSize == null || !fontForSize.isValid()) {
                return;
            }
        }
        SpriteFont spriteFont = fontForSize;
        Vec2i vec2iMeasureText = renderState.res.getFontRenderer().measureText(spriteFont, str);
        float f5 = vec2iMeasureText.x;
        float f6 = vec2iMeasureText.y;
        RectF rectFMeasureDrawRect = measureDrawRect(renderState.res.getMeter());
        float fWidth = rectFMeasureDrawRect.left + (rectFMeasureDrawRect.width() * 0.5f);
        float fCenterY = rectFMeasureDrawRect.centerY() + f2;
        Typeface typeface2 = Typeface.DEFAULT;
        try {
            Field declaredField = SpriteFont.class.getDeclaredField("typeface");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(spriteFont);
            typeface = obj instanceof Typeface ? (Typeface) obj : typeface2;
        } catch (Exception e) {
        }
        float visualCenterOffset = f6 * getVisualCenterOffset(typeface, iMax, str);
        switch (getTextAlign()) {
            case 1:
                f4 = rectFMeasureDrawRect.right - f5;
                break;
            case 2:
                f4 = rectFMeasureDrawRect.left;
                break;
            default:
                f4 = fWidth - (f5 / 2.0f);
                break;
        }
        createRotationAndVpMat(renderState, this.vpMatTmp, fWidth, fCenterY, measureDrawRot(renderState.getRes().getMeter()));
        renderState.res.getFontRenderer().drawText(renderState, spriteFont, new Vec3f(f4, fCenterY + visualCenterOffset, 0.0f), str, i2, 0, false, 0, 0, 0, 0, new RenderPassData(getBlendMode(), spriteFont.getEntryTexture(), renderState.res.getBufferRenderer_atlasBufferVPMat(), this.shaderOnBindAction));
    }

    private void renderPlain(RenderState renderState) {
        if (this.font1 == null || !this.font1.isValid()) {
            onCreateGLResources(renderState);
            if (this.font1 == null || !this.font1.isValid()) {
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (LyricsData.LyricLine lyricLine : this.lyricsData.lines) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(lyricLine.text);
        }
        String strMeasureText = renderState.res.meter.measureText(sb.toString());
        Vec2i vec2iMeasureText = renderState.res.getFontRenderer().measureText(this.font1, strMeasureText);
        RectF rectFMeasureDrawRect = measureDrawRect(renderState.res.getMeter(), vec2iMeasureText);
        createRotationAndVpMat(renderState, this.vpMatTmp, rectFMeasureDrawRect.left + (rectFMeasureDrawRect.width() * 0.5f), rectFMeasureDrawRect.top - (rectFMeasureDrawRect.height() * 0.5f), measureDrawRot(renderState.getRes().getMeter()));
        Vec2f drawOffset = getDrawOffset(rectFMeasureDrawRect, vec2iMeasureText.x);
        renderState.res.getFontRenderer().drawText(renderState, this.font1, new Vec3f(rectFMeasureDrawRect.left + drawOffset.x, drawOffset.y + rectFMeasureDrawRect.top, 0.0f), strMeasureText, this.color1, 0, false, 0, 0, 0, 0, new RenderPassData(getBlendMode(), this.font1.getEntryTexture(), renderState.res.getBufferRenderer_atlasBufferVPMat(), this.shaderOnBindAction));
    }

    private long getCurrentPosition() {
        if (sInjectedTimeMs >= 0) {
            return sInjectedTimeMs;
        }
        try {
            MediaPlaybackService mediaPlaybackService = MediaPlaybackService.getInstance();
            if (mediaPlaybackService != null) {
                return mediaPlaybackService.positionSafe();
            }
            return 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.TextElement
    public Vec2f getDrawOffset(RectF rectF) {
        return super.getDrawOffset(rectF);
    }
}
