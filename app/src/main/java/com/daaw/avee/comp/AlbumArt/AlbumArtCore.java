package com.daaw.avee.comp.AlbumArt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.util.Size;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import com.daaw.avee.vendor.aosp.MyThumbnailUtils;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifResourceDecoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResource;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResourceDecoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.daaw.avee.Common.Action;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Func.Func;
import com.daaw.avee.Common.Ref;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.VAsyncTask;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.PlayerCore;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class AlbumArtCore implements IAlbumArtCore {
    public static final boolean THUMB_FROM_RESOLVER = !Utils.PRE_Q;
    public static WeakEventR1<Object, File> onRequestLocalFolder = new WeakEventR1<>();
    private static final Object createInstanceLock = new Object();
    private static volatile WeakReference<AlbumArtCore> instanceWeak = new WeakReference<>(null);
    private static Drawable drawableBg = null;

    private static class ColorKeyTransformation extends GifBitmapWrapperTransformation {
        private BitmapPool bitmapPool;
        private ImageColorKeyParams colorKeyParams;
        private Context context;

        public ColorKeyTransformation(final BitmapPool bitmapPool, Context context, final ImageColorKeyParams imageColorKeyParams) {
            super(bitmapPool, new Transformation<Bitmap>() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.ColorKeyTransformation.1
                @Override // com.bumptech.glide.load.Transformation
                public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
                    Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(resource.get().copy(Bitmap.Config.ARGB_8888, true), i, i2, false);
                    Ref ref = new Ref(null);
                    return BitmapResource.obtain(ColorKey.processBitmapColorKey(bitmapCreateScaledBitmap, imageColorKeyParams, new Ref(null), ref), bitmapPool);
                }

                @Override // com.bumptech.glide.load.Transformation
                public String getId() {
                    return "ck4" + imageColorKeyParams.getStrId();
                }
            });
            this.context = context;
            this.bitmapPool = bitmapPool;
            this.colorKeyParams = imageColorKeyParams;
        }
    }

    private class MyRequestDecoder implements ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> {
        private BitmapPool bitmapPool;
        public String cacheSignature;
        protected Context context;
        private String dataSource;
        private int gen_hint;
        private int gen_hint2;
        private String generateText;
        private boolean isLarge;
        private boolean loadFromMp3Tags;
        protected boolean useCache;

        public MyRequestDecoder(BitmapPool bitmapPool, Context context, boolean z, String str, boolean z2, String str2, int i, int i2, boolean z3) {
            this.bitmapPool = bitmapPool;
            this.context = context;
            this.useCache = z;
            this.dataSource = str;
            this.isLarge = z2;
            this.generateText = str2;
            this.gen_hint = i;
            this.gen_hint2 = i2;
            this.loadFromMp3Tags = z3;
            StringBuilder sb = new StringBuilder();
            sb.append("src:");
            sb.append(str);
            sb.append(":");
            sb.append(i);
            sb.append(":");
            sb.append(i2);
            sb.append("t:");
            sb.append(z3 ? "t" : "f");
            this.cacheSignature = sb.toString();
        }

        @Override // com.bumptech.glide.load.ResourceDecoder
        public Resource<GifBitmapWrapper> decode(ImageVideoWrapper imageVideoWrapper, int i, int i2) throws IOException {
            String str;
            Bitmap bitmapLoadAlbumArtFromTags = (((this.gen_hint & 4) != 0) || !this.loadFromMp3Tags || (str = this.dataSource) == null || str.length() <= 0) ? null : AlbumArtCore.this.loadAlbumArtFromTags(this.dataSource);
            if (bitmapLoadAlbumArtFromTags == null) {
                bitmapLoadAlbumArtFromTags = AlbumArtCore.this.loadVideoThumbOrGenerate(this.dataSource, false, this.generateText, i, i2, this.gen_hint, this.gen_hint2);
            }
            if (bitmapLoadAlbumArtFromTags == null) {
                return null;
            }
            return new GifBitmapWrapperResource(new GifBitmapWrapper(BitmapResource.obtain(bitmapLoadAlbumArtFromTags, this.bitmapPool), null));
        }

        @Override // com.bumptech.glide.load.ResourceDecoder
        public String getId() {
            return this.cacheSignature;
        }
    }

    private class MyRequestListener extends MyRequestDecoder implements RequestListener<Object, GlideDrawable> {
        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(GlideDrawable glideDrawable, Object obj, Target<GlideDrawable> target, boolean z, boolean z2) {
            return false;
        }

        public MyRequestListener(BitmapPool bitmapPool, Context context, boolean z, String str, boolean z2, String str2, int i, int i2) {
            super(bitmapPool, context, z, str, z2, str2, i, i2, false);
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onException(Exception exc, Object obj, Target<GlideDrawable> target, boolean z) {
            GenericRequestBuilder genericRequestBuilderLoad = AlbumArtCore.this.getMyPicasso(this.context).load(Integer.valueOf(R.drawable.dot1x1));
            if (!this.useCache) {
                genericRequestBuilderLoad = genericRequestBuilderLoad.skipMemoryCache(true);
            }
            genericRequestBuilderLoad.decoder((ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper>) this).signature((Key) new StringSignature(this.cacheSignature)).diskCacheStrategy(DiskCacheStrategy.NONE).into(target);
            return true;
        }
    }

    private class MyBitmapRequestListener implements ResourceDecoder<ImageVideoWrapper, Bitmap>, RequestListener<Uri, Bitmap> {
        private BitmapPool bitmapPool;
        public String cacheSignature;
        private Context context;
        private String dataSource;
        private int gen_hint;
        private int gen_hint2;
        private String generateText;
        private boolean isLarge;
        private boolean useCache;

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Bitmap bitmap, Uri uri, Target<Bitmap> target, boolean z, boolean z2) {
            return false;
        }

        public MyBitmapRequestListener(BitmapPool bitmapPool, Context context, boolean z, String str, boolean z2, String str2, int i, int i2, int i3, int i4) {
            this.bitmapPool = bitmapPool;
            this.context = context;
            this.useCache = z;
            this.dataSource = str;
            this.isLarge = z2;
            this.generateText = str2;
            this.gen_hint = i3;
            this.gen_hint2 = i4;
            this.cacheSignature = "ca1:" + str2 + ":" + i3 + ":" + i4 + ":" + i + ":" + i2;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onException(Exception exc, Uri uri, Target<Bitmap> target, boolean z) {
            BitmapRequestBuilder<Integer, Bitmap> bitmapRequestBuilderDiskCacheStrategy = AlbumArtCore.this.getMyPicasso(this.context).load(Integer.valueOf(R.drawable.dot1x1)).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE);
            if (!this.useCache) {
                bitmapRequestBuilderDiskCacheStrategy = bitmapRequestBuilderDiskCacheStrategy.skipMemoryCache(true);
            }
            bitmapRequestBuilderDiskCacheStrategy.decoder((ResourceDecoder<ImageVideoWrapper, Bitmap>) this).signature((Key) new StringSignature(this.cacheSignature)).into(target);
            return true;
        }

        @Override // com.bumptech.glide.load.ResourceDecoder
        public Resource<Bitmap> decode(ImageVideoWrapper imageVideoWrapper, int i, int i2) throws IOException {
            return BitmapResource.obtain(null, this.bitmapPool);
        }

        @Override // com.bumptech.glide.load.ResourceDecoder
        public String getId() {
            return this.cacheSignature;
        }
    }

    public static IAlbumArtCore getInstance() {
        return instanceWeak.get();
    }

    public static IAlbumArtCore createInstance(Context context) {
        AlbumArtCore albumArtCore = instanceWeak.get();
        if (albumArtCore == null) {
            synchronized (createInstanceLock) {
                albumArtCore = instanceWeak.get();
                if (albumArtCore == null) {
                    albumArtCore = new AlbumArtCore();
                    instanceWeak = new WeakReference<>(albumArtCore);
                }
            }
        }
        return albumArtCore;
    }

    private BitmapPool getBitmapPool(Context context) {
        return Glide.get(context).getBitmapPool();
    }

    private ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> getDecoder(Context context, BitmapPool bitmapPool) {
        return new GifBitmapWrapperResourceDecoder(new ImageVideoBitmapDecoder(new StreamBitmapDecoder(bitmapPool, DecodeFormat.PREFER_ARGB_8888), new FileDescriptorBitmapDecoder(bitmapPool, DecodeFormat.PREFER_ARGB_8888)), new GifResourceDecoder(context, bitmapPool), bitmapPool);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RequestManager getMyPicasso(Context context) {
        return Glide.with(context);
    }

    @Override // com.daaw.avee.comp.AlbumArt.IAlbumArtCore
    public void cancelRequest(ImageView imageView) {
        Glide.clear(imageView);
    }

    private boolean shouldTryLoadFromMp3Tags(String str) {
        Uri uri;
        if (str != null && str.length() > 0 && (uri = Uri.parse(str)) != null) {
            String strExtractFilenameExt = UtilsFileSys.extractFilenameExt(uri.getPath());
            if ("mp3".equals(strExtractFilenameExt) || "flac".equals(strExtractFilenameExt) || "ogg".equals(strExtractFilenameExt)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.daaw.avee.comp.AlbumArt.IAlbumArtCore
    public void loadAlbumArt(Uri uri, String str, String str2, String str3, ImageView imageView, boolean z, boolean z2, boolean z3) {
        String str4;
        DrawableRequestBuilder<?> drawableRequestBuilder;
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext != null) {
            Bitmap bitmapLoadFromResolver = loadFromResolver(appContext, uri, imageView.getWidth(), imageView.getHeight());
            if (bitmapLoadFromResolver != null) {
                imageView.setImageBitmap(bitmapLoadFromResolver);
                return;
            }
            String strDecode = Uri.decode(uri.toString());
            boolean zShouldTryLoadFromMp3Tags = shouldTryLoadFromMp3Tags(strDecode);
            if (str == null || str.length() <= 0 || str.charAt(0) != '/') {
                str4 = str;
            } else {
                str4 = "file://" + str;
            }
            Uri uri2 = Uri.parse(str4 != null ? str4 : "file://");
            BitmapPool bitmapPool = getBitmapPool(appContext);
            if (zShouldTryLoadFromMp3Tags) {
                drawableRequestBuilder = setupFromMp3Tags(z, bitmapPool, appContext, true, strDecode, false, str3, 0, -1);
            } else {
                drawableRequestBuilder = setupFromUri(uri2, z, bitmapPool, appContext, true, strDecode, false, str3, 0, -1);
            }
            drawableRequestBuilder.into(imageView);
        }
    }

    private Bitmap loadFromResolver(Context context, Uri uri, int i, int i2) {
        if (THUMB_FROM_RESOLVER) {
            boolean z = i < 1;
            boolean z2 = i2 < 1;
            if (z2 || z) {
                tlog.w("Dimensions were too small");
            }
            if (z) {
                i = 200;
            }
            if (z2) {
                i2 = 200;
            }
            try {
                return context.getContentResolver().loadThumbnail(uri, new Size(i, i2), null);
            } catch (IOException e) {
                tlog.e(e, "");
            }
        }
        return null;
    }

    @Override // com.daaw.avee.comp.AlbumArt.IAlbumArtCore
    public void loadAlbumArtLarge(boolean z, Uri uri, String str, String str2, String str3, boolean z2, ImageLoadedListener imageLoadedListener, int i, int i2, int i3, int i4, int i5, int i6, boolean z3, ImageColorKeyParams imageColorKeyParams, Object obj) {
        int iMax = i < 2 ? Math.max(2, Math.max(i, i2)) : i;
        int iMax2 = i2 < 2 ? Math.max(2, Math.max(iMax, i2)) : i2;
        int iMax3 = i3 < 2 ? Math.max(2, Math.max(i3, i4)) : i3;
        loadASyncAlbumArtLarge2(z, uri, str, str2, str3, z2, imageLoadedListener, iMax, iMax2, iMax3, i4 < 2 ? Math.max(2, Math.max(iMax3, i4)) : i4, i5, i6, z3, imageColorKeyParams, obj);
    }

    public DrawableRequestBuilder<?> setupFromMp3Tags(boolean z, BitmapPool bitmapPool, Context context, boolean z2, String str, boolean z3, String str2, int i, int i2) {
        DrawableTypeRequest<Integer> drawableTypeRequestLoad = getMyPicasso(context).load(Integer.valueOf(R.drawable.ic_launcher));
        drawableTypeRequestLoad.placeholder(R.drawable.placeholderart4);
        drawableTypeRequestLoad.error(R.drawable.placeholderart4);
        if (z) {
            drawableTypeRequestLoad.fitCenter();
        }
        DrawableRequestBuilder<?> drawableRequestBuilderSkipMemoryCache = !z2 ? drawableTypeRequestLoad.skipMemoryCache(true) : drawableTypeRequestLoad;
        MyRequestDecoder myRequestDecoder = new MyRequestDecoder(bitmapPool, context, z2, str, z3, str2, i, i2, true);
        drawableRequestBuilderSkipMemoryCache.signature((Key) new StringSignature(myRequestDecoder.cacheSignature));
        drawableRequestBuilderSkipMemoryCache.decoder((ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper>) myRequestDecoder);
        drawableRequestBuilderSkipMemoryCache.diskCacheStrategy(DiskCacheStrategy.NONE);
        return drawableRequestBuilderSkipMemoryCache;
    }

    public DrawableRequestBuilder<?> setupFromUri(Uri uri, boolean z, BitmapPool bitmapPool, Context context, boolean z2, String str, boolean z3, String str2, int i, int i2) {
        DrawableRequestBuilder<Uri> drawableRequestBuilderDiskCacheStrategy = getMyPicasso(context).load(uri).placeholder(R.drawable.placeholderart4).error(R.drawable.placeholderart4).diskCacheStrategy(DiskCacheStrategy.NONE);
        if (z) {
            drawableRequestBuilderDiskCacheStrategy.fitCenter();
        }
        MyRequestListener myRequestListener = new MyRequestListener(bitmapPool, context, z2, str, z3, str2, i, i2);
        drawableRequestBuilderDiskCacheStrategy.signature((Key) new StringSignature(myRequestListener.cacheSignature));
        drawableRequestBuilderDiskCacheStrategy.listener((RequestListener<? super Uri, GlideDrawable>) myRequestListener);
        drawableRequestBuilderDiskCacheStrategy.decoder(getDecoder(context, bitmapPool));
        return drawableRequestBuilderDiskCacheStrategy;
    }

    public DrawableRequestBuilder<?> setupFromUri(Integer num, boolean z, BitmapPool bitmapPool, Context context, boolean z2, String str, boolean z3, String str2, int i, int i2) {
        DrawableRequestBuilder<Integer> drawableRequestBuilderDiskCacheStrategy = getMyPicasso(context).load(num).placeholder(R.drawable.placeholderart4).error(R.drawable.placeholderart4).diskCacheStrategy(DiskCacheStrategy.NONE);
        if (z) {
            drawableRequestBuilderDiskCacheStrategy.fitCenter();
        }
        MyRequestListener myRequestListener = new MyRequestListener(bitmapPool, context, z2, str, z3, str2, i, i2);
        drawableRequestBuilderDiskCacheStrategy.signature((Key) new StringSignature(myRequestListener.cacheSignature));
        drawableRequestBuilderDiskCacheStrategy.listener((RequestListener<? super Integer, GlideDrawable>) myRequestListener);
        return drawableRequestBuilderDiskCacheStrategy;
    }

    /* JADX WARN: Code duplicated, block: B:56:0x0155  */
    private void loadASyncAlbumArtLarge2(boolean z, Uri uri, final String str, final String str2, String str3, boolean z2, final ImageLoadedListener imageLoadedListener, final int i, final int i2, int i3, int i4, int i5, int i6, boolean z3, final ImageColorKeyParams imageColorKeyParams, Object obj) {
        DrawableRequestBuilder<?> drawableRequestBuilder;
        final DrawableRequestBuilder<?> drawableRequestBuilder2;
        GlideDrawable glideDrawable;
        Context appContext = PlayerCore.s().getAppContext();
        if (appContext != null) {
            final String strDecode = Uri.decode(uri.toString());
            Bitmap bitmapLoadFromResolver = loadFromResolver(appContext, uri, i, i2);
            if (bitmapLoadFromResolver != null) {
                imageLoadedListener.onBitmapLoaded(new ImageResult(bitmapLoadFromResolver), strDecode, str, str2);
                return;
            }
            boolean zShouldTryLoadFromMp3Tags = shouldTryLoadFromMp3Tags(strDecode);
            Target<GlideDrawable> target = new Target<GlideDrawable>() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.1
                @Override // com.bumptech.glide.request.target.Target
                public Request getRequest() {
                    return null;
                }

                @Override // com.bumptech.glide.manager.LifecycleListener
                public void onDestroy() {
                }

                @Override // com.bumptech.glide.request.target.Target
                public void onLoadCleared(Drawable drawable) {
                }

                @Override // com.bumptech.glide.request.target.Target
                public void onLoadStarted(Drawable drawable) {
                }

                @Override // com.bumptech.glide.manager.LifecycleListener
                public void onStart() {
                }

                @Override // com.bumptech.glide.manager.LifecycleListener
                public void onStop() {
                }

                @Override // com.bumptech.glide.request.target.Target
                public void setRequest(Request request) {
                }

                @Override // com.bumptech.glide.request.target.Target
                public /* bridge */ /* synthetic */ void onResourceReady(GlideDrawable glideDrawable2, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    onResourceReady2(glideDrawable2, (GlideAnimation) glideAnimation);
                }

                @Override // com.bumptech.glide.request.target.Target
                public void onLoadFailed(Exception exc, Drawable drawable) {
                    imageLoadedListener.onBitmapLoaded(null, strDecode, str, str2);
                }

                /* JADX INFO: renamed from: onResourceReady, reason: avoid collision after fix types in other method */
                public void onResourceReady2(GlideDrawable glideDrawable2, GlideAnimation glideAnimation) {
                    AlbumArtCore.onGlideDrawableResult(imageLoadedListener, glideDrawable2, strDecode, str, str2, imageColorKeyParams);
                }

                @Override // com.bumptech.glide.request.target.Target
                public void getSize(SizeReadyCallback sizeReadyCallback) {
                    int i7;
                    int i8 = i;
                    if (i8 > 0 && (i7 = i2) > 0) {
                        sizeReadyCallback.onSizeReady(i8, i7);
                    } else {
                        sizeReadyCallback.onSizeReady(256, 256);
                    }
                }
            };
            imageLoadedListener.setUserObject1(target);
            String absolutePath = (i5 & 4) != 0 ? "file://" : str;
            boolean z4 = Looper.myLooper() == Looper.getMainLooper();
            BitmapPool bitmapPool = getBitmapPool(appContext);
            int resIdFromPath = InternalBitmapLoader.getResIdFromPath(absolutePath);
            if (resIdFromPath > 0) {
                drawableRequestBuilder = setupFromUri(Integer.valueOf(resIdFromPath), true, bitmapPool, appContext, z3, strDecode, true, str3, i5, i6);
                drawableRequestBuilder.dontAnimate();
            } else if (absolutePath != null && absolutePath.length() > 0) {
                String fileNameFromPath = LocalBitmapLoader.getFileNameFromPath(absolutePath);
                if (obj != null && fileNameFromPath != null) {
                    File fileInvoke = onRequestLocalFolder.invoke(obj, null);
                    if (fileInvoke == null) {
                        fileInvoke = null;
                    }
                    absolutePath = new File(fileInvoke, fileNameFromPath).getAbsolutePath();
                    if (absolutePath != null && absolutePath.length() > 0 && absolutePath.charAt(0) == '/') {
                        absolutePath = "file://" + absolutePath;
                    }
                    drawableRequestBuilder = setupFromUri(Uri.parse(absolutePath), true, bitmapPool, appContext, z3, strDecode, true, str3, i5, i6);
                    drawableRequestBuilder.dontAnimate();
                } else {
                    drawableRequestBuilder = null;
                }
            } else {
                drawableRequestBuilder = null;
            }
            if (drawableRequestBuilder == null) {
                if (absolutePath != null && absolutePath.length() > 0 && absolutePath.charAt(0) == '/') {
                    absolutePath = "file://" + str;
                }
                if (absolutePath == null || absolutePath.length() <= 0) {
                    absolutePath = "file://";
                }
                Uri uri2 = Uri.parse(absolutePath);
                if (zShouldTryLoadFromMp3Tags) {
                    drawableRequestBuilder2 = setupFromMp3Tags(true, bitmapPool, appContext, z3, strDecode, true, str3, i5, i6);
                } else {
                    drawableRequestBuilder2 = setupFromUri(uri2, true, bitmapPool, appContext, z3, strDecode, true, str3, i5, i6);
                }
                drawableRequestBuilder2.dontAnimate();
            } else {
                drawableRequestBuilder2 = drawableRequestBuilder;
            }
            if (z4) {
                drawableRequestBuilder2.into(target);
                return;
            }
            if (z) {
                try {
                    glideDrawable = drawableRequestBuilder2.into(i, i2).get(1500L, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    tlog.w(e.getMessage());
                    glideDrawable = null;
                }
                onGlideDrawableResult(imageLoadedListener, glideDrawable, strDecode, str, str2, imageColorKeyParams);
                return;
            }
            new VAsyncTask(new VAsyncTask.TaskListener<GlideDrawable>() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.daaw.avee.Common.VAsyncTask.TaskListener
                public GlideDrawable onExecuteInBg(VAsyncTask<GlideDrawable> vAsyncTask) {
                    try {
                        return drawableRequestBuilder2.into(i, i2).get(9000L, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                        return null;
                    } catch (ExecutionException e3) {
                        e3.printStackTrace();
                        return null;
                    } catch (TimeoutException e4) {
                        e4.printStackTrace();
                        return null;
                    }
                }
            }).RunParallel(new VAsyncTask.ResultListener<GlideDrawable>() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.3
                @Override // com.daaw.avee.Common.VAsyncTask.ResultListener
                public void onResult(GlideDrawable glideDrawable2, boolean z5) {
                    AlbumArtCore.onGlideDrawableResult(imageLoadedListener, glideDrawable2, strDecode, str, str2, imageColorKeyParams);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onGlideDrawableResult(ImageLoadedListener imageLoadedListener, GlideDrawable glideDrawable, String str, String str2, String str3, final ImageColorKeyParams imageColorKeyParams) {
        if (glideDrawable == null) {
            imageLoadedListener.onBitmapLoaded(new ImageResult(null), str, str2, str3);
            return;
        }
        if (glideDrawable instanceof GlideBitmapDrawable) {
            final Ref ref = new Ref(null);
            final Ref ref2 = new Ref(null);
            imageLoadedListener.onBitmapLoaded(new ImageResult(ColorKey.processBitmapColorKey(((GlideBitmapDrawable) glideDrawable).getBitmap(), imageColorKeyParams, ref2, ref), new Action() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.4
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.daaw.avee.Common.Action
                public void onInvoke() {
                    ref.obj = null;
                    if (ref2.obj != null) {
                        ((Bitmap) ref2.obj).recycle();
                    }
                    ref2.obj = null;
                }
            }), str, str2, str3);
        } else if (glideDrawable instanceof GifDrawable) {
            GifDrawable gifDrawable = (GifDrawable) glideDrawable;
            final GifDecoder decoder = gifDrawable.getDecoder();
            final Object obj = new Object();
            final Ref ref3 = new Ref(null);
            final Ref ref4 = new Ref(null);
            imageLoadedListener.onBitmapLoaded(new ImageSequenceResult(gifDrawable.getFirstFrame(), new Func<Integer>() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.5
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.daaw.avee.Common.Func.Func
                public Integer onInvoke() {
                    return Integer.valueOf(decoder.getFrameCount());
                }
            }, new Action() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.6
                @Override // com.daaw.avee.Common.Action
                public void onInvoke() {
                    decoder.resetFrameIndex();
                }
            }, new Action() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.7
                @Override // com.daaw.avee.Common.Action
                public void onInvoke() {
                    decoder.advance();
                }
            }, new Func<Bitmap>() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.8
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.daaw.avee.Common.Func.Func
                public Bitmap onInvoke() {
                    Bitmap bitmapProcessBitmapColorKey;
                    synchronized (obj) {
                        bitmapProcessBitmapColorKey = ColorKey.processBitmapColorKey(decoder.getNextFrame(), imageColorKeyParams, ref4, ref3);
                    }
                    return bitmapProcessBitmapColorKey;
                }
            }, new Action() { // from class: com.daaw.avee.comp.AlbumArt.AlbumArtCore.9
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.daaw.avee.Common.Action
                public void onInvoke() {
                    synchronized (obj) {
                        ref3.obj = null;
                        if (ref4.obj != null) {
                            ((Bitmap) ref4.obj).recycle();
                        }
                        ref4.obj = null;
                    }
                }
            }), str, str2, str3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap loadAlbumArtFromTags(String str) {
        ID3v2 id3v2Tag;
        byte[] albumImage;
        try {
            Uri uri = Uri.parse(str);
            if (uri == null) {
                return null;
            }
            Mp3File mp3File = new Mp3File(uri.getPath());
            if (!mp3File.hasId3v2Tag() || (albumImage = (id3v2Tag = mp3File.getId3v2Tag()).getAlbumImage()) == null) {
                return null;
            }
            id3v2Tag.getAlbumImageMimeType();
            return BitmapFactory.decodeStream(new ByteArrayInputStream(albumImage));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap loadVideoThumbOrGenerate(String str, boolean z, String str2, int i, int i2, int i3, int i4) throws IOException {
        Uri uri = Uri.parse(str);
        boolean z2 = (i3 & 1) != 0;
        boolean z3 = (i3 & 4) != 0;
        boolean z4 = (i3 & 2) != 0;
        if (!z3 && uri != null) {
            String path = uri.getPath();
            String strExtractFilenameExt = UtilsFileSys.extractFilenameExt(path);
            if (!"mp3".equals(strExtractFilenameExt) && !"wav".equals(strExtractFilenameExt) && !"ogg".equals(strExtractFilenameExt)) {
                Bitmap bitmapCreateVideoThumbnail = MyThumbnailUtils.createVideoThumbnail(path, z ? 1 : 3);
                if (bitmapCreateVideoThumbnail != null) {
                    return bitmapCreateVideoThumbnail;
                }
            }
        }
        if (str2 != null && str2.length() > 0) {
            float[] fArr = new float[3];
            ColorUtils.colorToHSL(i4, fArr);
            if (fArr[2] > 0.99f) {
                fArr[1] = 1.0f;
            }
            int iAlpha = Color.alpha(i4);
            float fValueInAlphabet = (360.0f * SimpleTextAlbumArtCreator.valueInAlphabet(str2.charAt(0))) + (z2 ? 15.0f : 0.0f);
            return SimpleTextAlbumArtCreator.textAsBitmap(i, i2, str2, ColorUtils.setAlphaComponent(ColorUtils.HSLToColor(new float[]{0.0f + fValueInAlphabet, 0.2f, 1.0f}), z4 ? 0 : 255), ColorUtils.setAlphaComponent(ColorUtils.HSLToColor(new float[]{fValueInAlphabet, 0.9f, 0.6f * fArr[2]}), iAlpha), ColorUtils.setAlphaComponent(ColorUtils.HSLToColor(new float[]{(((int) fValueInAlphabet) + 30) % 360, 0.9f, fArr[2] * 0.4f}), iAlpha), drawableBg);
        }
        return null;
    }
}
