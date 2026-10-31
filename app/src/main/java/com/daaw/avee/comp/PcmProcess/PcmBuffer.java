package com.daaw.avee.comp.PcmProcess;

import com.daaw.avee.Common.RandomAccessFileTh;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.playback.AudioFrameData;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class PcmBuffer {
    private static int lastBytesIgnoreCount = 5236;
    private String filePrefix;
    private long totalBufferTimeDurationUs;
    private ByteBuffer zeroBuf = ByteBuffer.allocate(20000);
    private final Object locker = new Object();
    private int lastIndex = -1;
    private long lastBufferPresentationTimeUs = -1;
    private int lastReadRafFillPos = 0;
    private BufferEntry readBufferEntryTmp = new BufferEntry();
    private byte[][] tmpBuff = (byte[][]) Array.newInstance((Class<?>) byte.class, 1, 0);
    private int bufferSampleRate = 44100;
    private int bufferChannelCount = 2;
    private int maxSizeInBytes = 0;
    private RandomAccessFileTh raf = null;
    private int rafFillPos = 0;
    private long currentBufferOffsetCorrection = 0;

    public PcmBuffer(String str, long j) {
        this.filePrefix = str;
        this.totalBufferTimeDurationUs = j;
    }

    public void dispose() {
        RandomAccessFileTh randomAccessFileTh = this.raf;
        if (randomAccessFileTh != null) {
            randomAccessFileTh.close();
        }
        this.raf = null;
    }

    private void createRaf(int i, int i2, int i3) {
        this.bufferSampleRate = i;
        this.bufferChannelCount = i2;
        this.maxSizeInBytes = UtilsVisualizerDataProvider.durationUsToBytes(this.totalBufferTimeDurationUs, i2, i3, i);
        int i4 = 0;
        this.rafFillPos = 0;
        RandomAccessFileTh randomAccessFileTh = this.raf;
        if (randomAccessFileTh != null) {
            randomAccessFileTh.close();
        }
        do {
            RandomAccessFileTh randomAccessFileThCreateTemp = RandomAccessFileTh.createTemp(this.filePrefix + i4, this.maxSizeInBytes + 5120);
            this.raf = randomAccessFileThCreateTemp;
            i4++;
            if (randomAccessFileThCreateTemp != null) {
                break;
            }
        } while (i4 < 3);
        this.currentBufferOffsetCorrection = 0L;
    }

    public void onNewSourceTh() {
        synchronized (this.locker) {
            this.rafFillPos = 0;
            this.currentBufferOffsetCorrection = 0L;
        }
    }

    public void onPcmData(ByteBuffer byteBuffer, long j, int i, int i2, int i3, int i4) {
        long jMax = Math.max(0L, j);
        if (this.lastIndex == i && this.lastBufferPresentationTimeUs == jMax) {
            return;
        }
        this.lastBufferPresentationTimeUs = jMax;
        this.lastIndex = i;
        if (this.raf == null || this.bufferSampleRate != i2 || this.bufferChannelCount != i3) {
            tlog.w("createRaf");
            synchronized (this.locker) {
                createRaf(i2, i3, i4);
            }
        }
        if (this.raf == null) {
            return;
        }
        int iLimit = byteBuffer.limit() - byteBuffer.position();
        int iDurationUsToBytes = UtilsVisualizerDataProvider.durationUsToBytes(jMax, this.bufferChannelCount, i4, this.bufferSampleRate) - iLimit;
        if (iDurationUsToBytes < 0) {
            tlog.w("startByte: " + iDurationUsToBytes + "; TimeUs: " + jMax);
        }
        if (iDurationUsToBytes < 0) {
            iDurationUsToBytes = 0;
        }
        int iPosition = byteBuffer.position();
        int iLimit2 = byteBuffer.limit();
        int iDurationUsToBytes2 = UtilsVisualizerDataProvider.durationUsToBytes(500000L, this.bufferChannelCount, i4, this.bufferSampleRate);
        synchronized (this.locker) {
            if (this.raf != null) {
                long j2 = iDurationUsToBytes - this.rafFillPos;
                if (Math.abs(j2) < iDurationUsToBytes2) {
                    iDurationUsToBytes = this.rafFillPos;
                } else if (j2 != 0) {
                    tlog.w("newDataFillDelta: " + j2);
                }
                this.rafFillPos = iLimit + iDurationUsToBytes;
                this.raf.writeData(iDurationUsToBytes, byteBuffer);
            }
        }
        byteBuffer.limit(iLimit2);
        byteBuffer.position(iPosition);
    }

    public AudioFrameData getVisData(long j, AudioFrameData.Request request, AudioFrameData audioFrameData, int i) {
        boolean z;
        ByteBuffer asByteBufferNotThSafe;
        int iRemaining;
        audioFrameData.capturedDataFully = false;
        audioFrameData.capturedDataPositionUs = j;
        audioFrameData.newestAvailableBufferPositionUs = 0L;
        audioFrameData.availableBuffersStartPositionUs = Long.MAX_VALUE;
        audioFrameData.availableBuffersEndPositionUs = Long.MIN_VALUE;
        if (this.raf == null) {
            return audioFrameData;
        }
        audioFrameData.init(this.bufferSampleRate, request.audioChannelCount, request.samplesCount, UtilsVisualizerDataProvider.durationUsToFrames(request.samplesCountUs, this.bufferSampleRate));
        long j2 = j + ((long) (request.audioOffsetMs * 1000));
        int iDurationUsToBytes = UtilsVisualizerDataProvider.durationUsToBytes(j2, this.bufferChannelCount, i, this.bufferSampleRate);
        int iFramesToBytes = UtilsVisualizerDataProvider.framesToBytes(audioFrameData.getBufferSizeForCapture(), this.bufferChannelCount, i);
        int iFramesToBytes2 = UtilsVisualizerDataProvider.framesToBytes(audioFrameData.getBufferSizeForTimedResult(), this.bufferChannelCount, i);
        long j3 = iDurationUsToBytes - this.lastReadRafFillPos;
        if (Math.abs(j3) < i) {
            iDurationUsToBytes = this.lastReadRafFillPos;
        } else if (j3 != 0) {
            tlog.w("! readPosDelta: " + j3);
        }
        this.lastReadRafFillPos = iFramesToBytes2 + iDurationUsToBytes;
        int i2 = this.maxSizeInBytes;
        int i3 = iDurationUsToBytes + iFramesToBytes;
        int i4 = i2 - i3;
        int iMax = Math.max(0, i3 - i2);
        int iMax2 = Math.max(0, iFramesToBytes - iMax);
        int i5 = iDurationUsToBytes + iMax2;
        if (i5 > this.rafFillPos - 10) {
            z = i4 <= lastBytesIgnoreCount;
            StringBuilder sb = new StringBuilder();
            sb.append("need read to");
            sb.append(z ? "/ignore: " : ": ");
            sb.append(i5);
            sb.append("; raf filled to: ");
            sb.append(this.rafFillPos);
            sb.append("; maxBytes: ");
            sb.append(this.maxSizeInBytes);
            sb.append("; bytesTillEnd: ");
            sb.append(i4);
            tlog.w(sb.toString());
            if (!z) {
                return audioFrameData;
            }
        } else {
            z = false;
        }
        synchronized (this.locker) {
            try {
                try {
                    if (this.raf == null) {
                        return audioFrameData;
                    }
                    if (z) {
                        this.zeroBuf.position(0);
                        asByteBufferNotThSafe = this.zeroBuf;
                    } else {
                        asByteBufferNotThSafe = this.raf.readAsByteBufferNotThSafe(iDurationUsToBytes);
                    }
                    ByteBuffer byteBuffer = asByteBufferNotThSafe;
                    if (iMax2 > byteBuffer.remaining()) {
                        tlog.w("readSizeInBytes [" + iMax2 + "] > buf.remainingInBytes [" + byteBuffer.remaining() + "]");
                        iRemaining = byteBuffer.remaining();
                    } else {
                        iRemaining = iMax2;
                    }
                    this.readBufferEntryTmp.finishInit(byteBuffer, 0L, this.bufferSampleRate, this.bufferChannelCount, iRemaining, i, this.tmpBuff);
                    short[] sArr = new short[2];
                    float[] fArr = new float[1];
                    int bufferData3 = this.readBufferEntryTmp.getBufferData3(audioFrameData.pcmBuffer, audioFrameData.pcmBufferChannelCount, 0, 0, sArr, fArr, new long[1]);
                    audioFrameData.capturedDataFully = iMax + bufferData3 >= audioFrameData.getBufferSizeForCapture();
                    audioFrameData.captureSamplesCount = bufferData3;
                    audioFrameData.capturedDataPositionEndUs = j2 + UtilsVisualizerDataProvider.framesToDurationUs(bufferData3, audioFrameData.sampleRate);
                    audioFrameData.minPcmValue = sArr[0];
                    audioFrameData.maxPcmValue = sArr[1];
                    audioFrameData.rms = (float) Math.sqrt((1.0f / audioFrameData.getBufferSizeForCapture()) * fArr[0]);
                    audioFrameData.valid = true;
                    return audioFrameData;
                } catch (Throwable th) {
                    throw th;
                }
            } catch (Throwable th2) {
                audioFrameData.valid = false;
                return audioFrameData;
            }
        }
    }
}
