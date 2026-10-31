package com.daaw.avee.comp.VisualizerExporter;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.daaw.avee.Common.ShortBufferResourcePool;
import com.daaw.avee.Common.tlog;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public class AudioEncoderCore {
    private static final String MIME_TYPE = "audio/mp4a-latm";
    private static final String TAG = "AudioEncoderCore";
    protected static final int TIMEOUT_USEC = 33333;
    private static final boolean VERBOSE = false;
    private int inputChannelCount;
    private MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mEncoder;
    private MediaMuxerWrapper mMuxer;
    private int mTrackIndex;

    public static class EncodeData {
        public ShortBufferResourcePool.ShortBufferResource buffer = null;
        public int length = 0;
        public long presentationTimeUs = 0;
    }

    private static MediaCodecInfo selectCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    public AudioEncoderCore(MediaMuxerWrapper mediaMuxerWrapper) throws IOException {
        this(mediaMuxerWrapper, 44100, 1, 64000, 0);
    }

    public AudioEncoderCore(MediaMuxerWrapper mediaMuxerWrapper, int i, int i2, int i3, int i4) throws IOException {
        this.mBufferInfo = new MediaCodec.BufferInfo();
        MediaFormat mediaFormatCreateAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", i, i2);
        mediaFormatCreateAudioFormat.setInteger("bitrate", i3);
        mediaFormatCreateAudioFormat.setInteger("channel-count", i2);
        if (i4 > 0) {
            mediaFormatCreateAudioFormat.setInteger("max-input-size", i4 * i2 * 2);
        }
        MediaCodec mediaCodecCreateByCodecName = MediaCodec.createByCodecName(selectCodec("audio/mp4a-latm").getName());
        this.mEncoder = mediaCodecCreateByCodecName;
        mediaCodecCreateByCodecName.configure(mediaFormatCreateAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mEncoder.start();
        this.mMuxer = mediaMuxerWrapper;
        this.inputChannelCount = i2;
        this.mTrackIndex = -1;
    }

    public void release() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
    }

    public int getInputFormatChannelCount() {
        return this.inputChannelCount;
    }

    protected boolean encode(EncodeData encodeData, boolean z, boolean z2) {
        int iDequeueInputBuffer;
        ByteBuffer[] inputBuffers = this.mEncoder.getInputBuffers();
        do {
            iDequeueInputBuffer = this.mEncoder.dequeueInputBuffer(33333L);
            int i = 0;
            if (iDequeueInputBuffer >= 0) {
                ByteBuffer byteBuffer = inputBuffers[iDequeueInputBuffer];
                byteBuffer.clear();
                if (encodeData.buffer != null) {
                    int iMin = Math.min(byteBuffer.capacity() / 2, encodeData.length);
                    if (byteBuffer.capacity() / 2 < encodeData.length) {
                        tlog.w("inputBuffer too small: " + byteBuffer.capacity() + " encodeData.length: " + encodeData.length);
                    }
                    if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
                        while (i < iMin) {
                            short s = encodeData.buffer.obj[i];
                            byteBuffer.put((byte) (s & 255));
                            byteBuffer.put((byte) ((s & 65280) >> 8));
                            i++;
                        }
                    } else {
                        while (i < iMin) {
                            short s2 = encodeData.buffer.obj[i];
                            byteBuffer.put((byte) ((s2 & 65280) >> 8));
                            byteBuffer.put((byte) (s2 & 255));
                            i++;
                        }
                    }
                    encodeData.buffer.recycle();
                    encodeData.buffer = null;
                }
                if (encodeData.length <= 0 || z) {
                    this.mEncoder.queueInputBuffer(iDequeueInputBuffer, 0, 0, encodeData.presentationTimeUs, 4);
                    return true;
                }
                this.mEncoder.queueInputBuffer(iDequeueInputBuffer, 0, byteBuffer.position(), encodeData.presentationTimeUs, 0);
                return true;
            }
        } while (iDequeueInputBuffer != -1);
        return false;
    }

    public void drainEncoder(boolean z, boolean z2) {
        if (z2) {
            this.mEncoder.signalEndOfInputStream();
        }
        ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
        while (true) {
            int iDequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 33333L);
            if (iDequeueOutputBuffer == -1) {
                if (!z2) {
                    return;
                }
            } else if (iDequeueOutputBuffer == -3) {
                outputBuffers = this.mEncoder.getOutputBuffers();
            } else if (iDequeueOutputBuffer == -2) {
                MediaFormat outputFormat = this.mEncoder.getOutputFormat();
                Log.d(TAG, "encoder output format changed: " + outputFormat);
                this.mTrackIndex = this.mMuxer.addTrack(outputFormat, MimeTypes.BASE_TYPE_AUDIO);
            } else if (iDequeueOutputBuffer < 0) {
                Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + iDequeueOutputBuffer);
            } else if (this.mMuxer.isMuxerStarted()) {
                ByteBuffer byteBuffer = outputBuffers[iDequeueOutputBuffer];
                if (byteBuffer == null) {
                    throw new RuntimeException("encoderOutputBuffer " + iDequeueOutputBuffer + " was null");
                }
                if ((this.mBufferInfo.flags & 2) != 0) {
                    this.mBufferInfo.size = 0;
                }
                if (this.mBufferInfo.size > 0) {
                    if (this.mBufferInfo.size < 0 || this.mBufferInfo.offset < 0 || this.mBufferInfo.presentationTimeUs < 0) {
                        Log.w(TAG, "Invalid bufferInfo, size:  " + this.mBufferInfo.size + " offset: " + this.mBufferInfo.offset + " presentationTimeUs: " + this.mBufferInfo.presentationTimeUs);
                    }
                    byteBuffer.position(this.mBufferInfo.offset);
                    byteBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                    this.mMuxer.writeSampleData(this.mTrackIndex, byteBuffer, this.mBufferInfo);
                }
                this.mEncoder.releaseOutputBuffer(iDequeueOutputBuffer, false);
                if ((this.mBufferInfo.flags & 4) != 0) {
                    if (z2 || !z) {
                        return;
                    }
                    Log.w(TAG, "reached end of stream unexpectedly");
                    return;
                }
            } else {
                Log.w(TAG, "##### Muxer not started yet");
                this.mEncoder.releaseOutputBuffer(iDequeueOutputBuffer, false);
            }
        }
    }
}
