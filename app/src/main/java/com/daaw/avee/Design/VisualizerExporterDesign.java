package com.daaw.avee.Design;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import com.daaw.avee.vendor.filechooser.afilechooser.utils.FileUtils;
import com.daaw.avee.Common.Events.WeakDelegateR2;
import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEvent5;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Events.WeakEventR2;
import com.daaw.avee.Common.StatusMsg;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Utils;
import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.ContextData;
import com.daaw.avee.EventsGlobal.EventsGlobalTextNotifier;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.AppPreferences.AppPreferences;
import com.daaw.avee.comp.Common.DirectoryPickerDialog;
import com.daaw.avee.comp.Common.IItemIdentifier;
import com.daaw.avee.comp.VisualUI.Fragment2;
import com.daaw.avee.comp.Visualizer.Elements.LyricsElement;
import com.daaw.avee.comp.Visualizer.FrameCaptureDesc;
import com.daaw.avee.comp.Visualizer.VisualizerViewCore;
import com.daaw.avee.comp.VisualizerExporter.AudioVideoCTSRecorder;
import com.daaw.avee.comp.VisualizerExporter.EncodingUtils;
import com.daaw.avee.comp.VisualizerExporter.VisExportDialog;
import com.daaw.avee.comp.playback.AudioFrameData;
import com.daaw.avee.comp.playback.EventsPlaybackService;
import com.daaw.avee.comp.playback.MediaPlaybackService;
import com.daaw.avee.comp.playback.PlayingMediaInfo;
import com.daaw.avee.comp.playback.Song.PlaylistSong;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import com.daaw.avee.vendor.glutil.graphics.Texture;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class VisualizerExporterDesign {
    private PlaylistSong.Data currentSongDisplayData;
    private PlaylistSong currentTrack;
    private volatile File exportFile;
    private volatile OutputStream exportOStream;
    private VisExportDialog.ExportingProgressStatus exportingStatus;
    private volatile FrameCaptureDesc frameCaptureDesc;
    private AudioFrameData lastAudioFrameData;
    private AudioFrameData lastAudioFrameDataReuse;
    private MainUIDesign mainUIDesign;
    final PlaybackDesign playbackDesign;
    private volatile AudioVideoCTSRecorder videoEncoder;
    public static WeakEvent3<Boolean, Long, Boolean> onExportingChanged = new WeakEvent3<>();
    public static WeakDelegateR2<AudioFrameData, AudioFrameData.Request2, AudioFrameData> getVisualizationData = new WeakDelegateR2<>();
    public static final WeakEvent onExportSuccess = new WeakEvent();
    private static volatile boolean exporting = false;
    private static long audioTrackPositionUs = 0;
    private static long outputTrackPositionUs = 0;
    private static long outputTrackDurationUs = 0;
    private List<Object> listenerRefHolder = new LinkedList();
    private VisExportDialog.ExportSettingsProfile[] exportSettingsProfiles = null;
    private WeakReference<VisualizerViewCore> visualizerSurfaceView = new WeakReference<>(null);
    private long duration = 0;
    private volatile boolean targetVideoForceCompatibleWH = true;
    private volatile int targetFps = 30;
    private volatile long targetFrameTimeUs = 33333;
    private volatile float encodingTargetBitrateMbps = 8.0f;
    private volatile boolean targetExportAudio = true;
    private volatile float targetAudioBitrateKbps = 320.0f;
    private volatile int targetAudioChannels = 2;
    private volatile String targetVideoCodec = "";
    private volatile String targetVideoMimeType = "";
    private volatile int targetVideoCodecProfile = 0;
    private volatile AudioFrameData.Request2 audioFrameRequest = new AudioFrameData.Request2();
    private volatile int startFrameToCapture = 0;
    private volatile int endFrameToCapture = 0;
    private volatile int overrideAudioFrameSamplesInCount = 0;
    private volatile boolean glThreadSignalDoneExporting = false;
    private ExportingStats exportingStats = new ExportingStats(this, null);
    private VisExportDialog.ExportSettings currentExportSettings = null;
    private long currentStartMSec = 0;
    private long currentEndMSec = 0;
    private long currentDullTrackDurationMs = 0;
    private Handler mainThreadHandler = new Handler();
    private volatile int frameCounter = 0;
    private int exportFrameIndex = 0;
    private int loadingResourcesFrameCounter = 0;
    private StatusMsg exportingErrorStatus = new StatusMsg();

    public void checkTrackAhead(long j) {
    }

    void readjustSeek2() {
    }

    static int access$2608(VisualizerExporterDesign visualizerExporterDesign) {
        int i = visualizerExporterDesign.loadingResourcesFrameCounter;
        visualizerExporterDesign.loadingResourcesFrameCounter = i + 1;
        return i;
    }

    public static boolean isExporting() {
        return exporting;
    }

    public static long getOutputTrackPositionUs() {
        return outputTrackPositionUs;
    }

    public static long getOutputTrackDurationUs() {
        return outputTrackDurationUs;
    }

    private class ExportingStats {
        public boolean criticalFailure;

        private ExportingStats() {
            this.criticalFailure = false;
        }

        /* synthetic */ ExportingStats(VisualizerExporterDesign visualizerExporterDesign, ExportingStats exportingStats) {
            this();
        }

        void reset() {
            this.criticalFailure = false;
        }
    }

    public VisualizerExporterDesign(final VisualizerDesign visualizerDesign, final PlaybackDesign playbackDesign, MainUIDesign mainUIDesign) {
        this.playbackDesign = playbackDesign;
        this.mainUIDesign = mainUIDesign;
        Fragment2.onSurfaceCreated.subscribeWeak(new WeakEvent1.Handler<VisualizerViewCore>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.1
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(VisualizerViewCore visualizerViewCore) {
                VisualizerExporterDesign.this.visualizerSurfaceView = new WeakReference(visualizerViewCore);
            }
        }, this.listenerRefHolder);
        Fragment2.onRequestIsExportAvailable.subscribeWeak(new WeakEventR.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Boolean invoke() {
                return Boolean.valueOf(Build.VERSION.SDK_INT >= 18);
            }
        }, this.listenerRefHolder);
        Fragment2.onExportAction.subscribeWeak(new WeakEvent1.Handler<ContextData>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.3
            @Override // com.daaw.avee.Common.Events.WeakEvent1.Handler
            public void invoke(ContextData contextData) {
                if (Build.VERSION.SDK_INT >= 18) {
                    if (contextData.getFragmentManager() != null) {
                        VisExportDialog.createAndShowVisExportDialog(contextData);
                    }
                } else {
                    Context appContext = PlayerCore.s().getAppContext();
                    if (appContext != null) {
                        EventsGlobalTextNotifier.onTextMsg.invoke(appContext.getResources().getString(R.string.feature_requires_at_least_api18));
                    }
                }
            }
        }, this.listenerRefHolder);
        VisExportDialog.onExportAction.subscribeWeak(new WeakEvent4.Handler<Fragment, ContextData, VisExportDialog.ExportSettings, Boolean>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.4
            @Override // com.daaw.avee.Common.Events.WeakEvent4.Handler
            public void invoke(Fragment fragment, ContextData contextData, VisExportDialog.ExportSettings exportSettings, Boolean bool) {
                long j = VisualizerExporterDesign.this.duration;
                AppPreferences.createOrGetInstance().setString(AppPreferences.PREF_String_currentExportSettings, exportSettings.serialize());
                AppPreferences.createOrGetInstance().setString(AppPreferences.PREF_String_currentExportSettingsProfile, exportSettings.profileActive.serialize());
                if (!bool.booleanValue()) {
                    VisualizerExporterDesign.this.startExportCreateFile(fragment, contextData, exportSettings, exportSettings.startMSec, exportSettings.endMSec, j);
                } else if (j < 30000) {
                    VisualizerExporterDesign.this.startExportCreateFile(fragment, contextData, exportSettings, 0L, 15000L, j);
                } else {
                    long j2 = j / 5;
                    VisualizerExporterDesign.this.startExportCreateFile(fragment, contextData, exportSettings, j2, 15000 + j2, j);
                }
            }
        }, this.listenerRefHolder);
        VisExportDialog.onCancelAction.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.5
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                VisualizerExporterDesign.this.stopExport(false, new StatusMsg(), false);
            }
        }, this.listenerRefHolder);
        VisExportDialog.onDestDirAction.subscribeWeak(new WeakEvent3.Handler<Fragment, ContextData, String>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.6
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Fragment fragment, ContextData contextData, String str) {
                DirectoryPickerDialog.createAndShowDialog(contextData, "visExportDestDir", R.string.vis_choose_export_dir_title, str);
            }
        }, this.listenerRefHolder);
        VisExportDialog.onActivityResult.subscribeWeak(new WeakEvent3.Handler<VisExportDialog, Integer, Intent>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.7
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(VisExportDialog visExportDialog, Integer num, Intent intent) {
                if (num.intValue() != 53 && num.intValue() == 54 && Build.VERSION.SDK_INT >= 19) {
                    if (intent != null) {
                        Uri data = intent.getData();
                        if (data != null) {
                            if ((intent.getFlags() & 64) != 0) {
                                tlog.w("offered FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
                                try {
                                    visExportDialog.getActivity().getContentResolver().takePersistableUriPermission(data, 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (VisualizerExporterDesign.this.currentExportSettings != null) {
                                String path = FileUtils.getPath(visExportDialog.getActivity(), data);
                                OutputStream outputStreamOpenOutputStream = null;
                                File file = path != null ? new File(path) : null;
                                if (file != null && file.canWrite()) {
                                    VisualizerExporterDesign.this.startExport(new ContextData(visExportDialog.getActivity()), VisualizerExporterDesign.this.currentExportSettings, file, null, VisualizerExporterDesign.this.currentStartMSec, VisualizerExporterDesign.this.currentEndMSec, VisualizerExporterDesign.this.currentDullTrackDurationMs);
                                    return;
                                }
                                try {
                                    outputStreamOpenOutputStream = visExportDialog.getActivity().getContentResolver().openOutputStream(data);
                                } catch (FileNotFoundException e2) {
                                    e2.printStackTrace();
                                }
                                VisualizerExporterDesign.this.startExport(new ContextData(visExportDialog.getActivity()), VisualizerExporterDesign.this.currentExportSettings, null, outputStreamOpenOutputStream, VisualizerExporterDesign.this.currentStartMSec, VisualizerExporterDesign.this.currentEndMSec, VisualizerExporterDesign.this.currentDullTrackDurationMs);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    tlog.w("PICK_IMAGE_PROPERTY_REQUEST_CODE_KITKAT invalid data");
                }
            }
        }, this.listenerRefHolder);
        DirectoryPickerDialog.onSubmitValue.subscribeWeak(new WeakEvent3.Handler<ContextData, String, String>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.8
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(ContextData contextData, String str, String str2) {
                if (str2.equals("visExportDestDir")) {
                    VisExportDialog.updateDestinationDirValue(str);
                }
            }
        }, this.listenerRefHolder);
        VisExportDialog.onRequestExportSettings.subscribeWeak(new WeakEventR.Handler<VisExportDialog.ExportSettings>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public VisExportDialog.ExportSettings invoke() {
                VisExportDialog.ExportSettings exportSettings = new VisExportDialog.ExportSettings();
                    VisualizerExporterDesign.this.exportSettingsProfiles = new VisExportDialog.ExportSettingsProfile[]{new VisExportDialog.ExportSettingsProfile(0, 1920, 1080, 60, 18.0f, true, 1), new VisExportDialog.ExportSettingsProfile(1, 1920, 1080, 60, 18.0f, true, 0), new VisExportDialog.ExportSettingsProfile(2, 1920, 1080, 30, 12.0f, true, 1), new VisExportDialog.ExportSettingsProfile(3, 1920, 1080, 30, 12.0f, true, 0), new VisExportDialog.ExportSettingsProfile(0, 1280, 720, 60, 12.0f, true, 0), new VisExportDialog.ExportSettingsProfile(1, 1280, 720, 30, 7.5f, true, 0)};
                    exportSettings.profileActiveIndex = 2;
                exportSettings.profiles = VisualizerExporterDesign.this.exportSettingsProfiles;
                exportSettings.profileActive = exportSettings.getProfileClone(exportSettings.profileActiveIndex);
                if (exportSettings.profileActive == null) {
                    exportSettings.profileActive = new VisExportDialog.ExportSettingsProfile(0, 1920, 1080, 30, 8.0f, 1, 128.0f, 0);
                }
                int[] iArr = new int[1];
                List<Tuple2<String, String>> listAvailableCodecsNames = EncodingUtils.getListAvailableCodecsNames(iArr);
                exportSettings.videoCodecs = Utils.getListTupleObj1ToArray(listAvailableCodecsNames);
                exportSettings.videoMimeTypes = Utils.getListTupleObj2ToArray(listAvailableCodecsNames);
                exportSettings.videoCodecActiveIndex = iArr[0];
                exportSettings.videoCodecProfileNames = EncodingUtils.getListAvailableCodecsProfileNames(null);
                exportSettings.destinationDir = VisualizerExporterDesign.this.getExportDirectoryPath("/");
                exportSettings.filename = "";
                if (VisualizerExporterDesign.this.currentSongDisplayData != null) {
                    exportSettings.filename = UtilsFileSys.fixToValidFilename(String.valueOf(VisualizerExporterDesign.this.currentSongDisplayData.trackName) + " exported");
                }
                if (exportSettings.filename.length() < 1) {
                    exportSettings.filename = UtilsFileSys.fixToValidFilename("exported");
                }
                exportSettings.videoForceCompatibleWH = true;
                exportSettings.hideAppLogo = false;
                exportSettings.useHighQualityBlur = false;
                exportSettings.exportAudio = true;
                exportSettings.startMSec = 0;
                if (VisualizerExporterDesign.this.currentSongDisplayData == null) {
                    exportSettings.endMSec = 0;
                } else {
                    exportSettings.endMSec = VisualizerExporterDesign.this.currentSongDisplayData.duration;
                }
                exportSettings.deserialize(AppPreferences.createOrGetInstance().getString(AppPreferences.PREF_String_currentExportSettings));
                exportSettings.profileActive.deserialize(AppPreferences.createOrGetInstance().getString(AppPreferences.PREF_String_currentExportSettingsProfile));
                exportSettings.profileActiveIndex = -1;
                return exportSettings;
            }
        }, this.listenerRefHolder);
        MainActivity.onViewPagerPageSelected.subscribeWeak(new WeakEvent2.Handler<Integer, Activity>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.10
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(Integer num, Activity activity) {
                if (num.intValue() == 2) {
                    playbackDesign.onViewPagerPageSelected(num, activity, VisualizerExporterDesign.exporting);
                } else {
                    playbackDesign.onViewPagerPageSelected(num, activity, false);
                    VisualizerExporterDesign.this.stopExport(false, new StatusMsg(), false);
                }
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onPlayStateChanged.subscribeWeak(new WeakEvent5.Handler<Boolean, Boolean, Integer, String, PlaylistSong>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.11
            @Override // com.daaw.avee.Common.Events.WeakEvent5.Handler
            public void invoke(Boolean bool, Boolean bool2, Integer num, String str, PlaylistSong playlistSong) {
                playbackDesign.onPlayStateChanged(bool, bool2, num, str, playlistSong, VisualizerExporterDesign.exporting);
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestUseHighQualityBlur.subscribeWeak(new WeakEventR.Handler<Boolean>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Boolean invoke() {
                return Boolean.valueOf(visualizerDesign.getUseHighQualityBlur());
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestOverrideAudioFrameSamplesInCount.subscribeWeak(new WeakEventR.Handler<Integer>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Integer invoke() {
                if (VisualizerExporterDesign.exporting) {
                    return Integer.valueOf(VisualizerExporterDesign.this.overrideAudioFrameSamplesInCount);
                }
                return -1;
            }
        }, this.listenerRefHolder);
        AppDesign.onRequestsSoundVisualizationDataExport.subscribeWeak(new WeakEventR2.Handler<AudioFrameData, Integer, AudioFrameData>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.14
            @Override // com.daaw.avee.Common.Events.WeakEventR2.Handler
            public AudioFrameData invoke(AudioFrameData audioFrameData, Integer num) {
                int i = 0;
                if (VisualizerExporterDesign.this.lastAudioFrameDataReuse == null) {
                    VisualizerExporterDesign.this.lastAudioFrameDataReuse = new AudioFrameData();
                }
                if (VisualizerExporterDesign.exporting) {
                    int i2 = VisualizerExporterDesign.this.frameCounter - VisualizerExporterDesign.this.startFrameToCapture;
                    int i3 = VisualizerExporterDesign.this.targetFps;
                    if (i2 < 0 || VisualizerExporterDesign.this.frameCounter > VisualizerExporterDesign.this.endFrameToCapture) {
                        return null;
                    }
                    boolean z = i2 >= VisualizerExporterDesign.this.endFrameToCapture + (-1);
                    VisualizerExporterDesign.this.audioFrameRequest.overridePositionUs = VisualizerExporterDesign.audioTrackPositionUs;
                    AudioFrameData.Request2 request2 = VisualizerExporterDesign.this.audioFrameRequest;
                    double d = i3;
                    Double.isNaN(d);
                    request2.samplesCountUs = (long) Math.ceil(1000000.0d / d);
                    VisualizerExporterDesign.this.audioFrameRequest.samplesCount = num.intValue();
                    AudioFrameData audioFrameDataInvoke = VisualizerExporterDesign.getVisualizationData.invoke(VisualizerExporterDesign.this.lastAudioFrameDataReuse, VisualizerExporterDesign.this.audioFrameRequest, null);
                    if (VisualizerExporterDesign.this.frameCounter % 15 == 0) {
                        VisualizerExporterDesign visualizerExporterDesign = VisualizerExporterDesign.this;
                        visualizerExporterDesign.checkTrackAhead(visualizerExporterDesign.audioFrameRequest.overridePositionUs);
                    }
                    if ((audioFrameDataInvoke == null || !audioFrameDataInvoke.capturedDataFully) && !z) {
                        if (audioFrameDataInvoke != null) {
                            tlog.d("not capturedDataFully posMs: " + (VisualizerExporterDesign.this.audioFrameRequest.overridePositionUs / 1000) + ", readjust and wait " + audioFrameDataInvoke.captureSamplesCount + " / " + audioFrameDataInvoke.getBufferSizeForCapture());
                        } else {
                            tlog.d("not capturedDataFully posMs: " + (VisualizerExporterDesign.this.audioFrameRequest.overridePositionUs / 1000) + ", readjust and wait, outData is null");
                        }
                        VisualizerExporterDesign.this.readjustSeek2();
                        while (i < 10) {
                            if (audioFrameDataInvoke != null) {
                                VisualizerExporterDesign.this.lastAudioFrameDataReuse = audioFrameDataInvoke;
                            }
                            audioFrameDataInvoke = VisualizerExporterDesign.getVisualizationData.invoke(VisualizerExporterDesign.this.lastAudioFrameDataReuse, VisualizerExporterDesign.this.audioFrameRequest, null);
                            if (audioFrameDataInvoke != null && audioFrameDataInvoke.capturedDataFully) {
                                break;
                            }
                            try {
                                Thread.sleep(500L);
                            } catch (Exception e) {
                            }
                            i++;
                        }
                        if (i > 0) {
                            tlog.d("didn't got with first");
                        }
                        if (i >= 10) {
                            tlog.d("failed to get capturedDataFully, gave up");
                            VisualizerExporterDesign.this.exportingErrorStatus.addErrorMessageCollapsed("warning: failed to get audio data for frame");
                        }
                    }
                    if (audioFrameDataInvoke != null) {
                        VisualizerExporterDesign.this.lastAudioFrameDataReuse = audioFrameDataInvoke;
                    }
                    VisualizerExporterDesign.this.lastAudioFrameData = audioFrameDataInvoke;
                    return audioFrameDataInvoke;
                }
                VisualizerExporterDesign.this.glThreadSignalDoneExporting = true;
                return null;
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onDisplayMetaDataStateChanged.subscribeWeak(new WeakEvent4.Handler<PlaylistSong, IItemIdentifier, PlaylistSong.Data, PlayingMediaInfo>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.15
            @Override // com.daaw.avee.Common.Events.WeakEvent4.Handler
            public void invoke(PlaylistSong playlistSong, IItemIdentifier iItemIdentifier, PlaylistSong.Data data, PlayingMediaInfo playingMediaInfo) {
                PlaybackDesign.currentDisplayTrack = playlistSong;
                PlaybackDesign.songDisplayData = data;
                PlaybackDesign.playingMediaInfo = playingMediaInfo;
                if (!VisualizerExporterDesign.exporting) {
                    VisualizerExporterDesign.this.currentSongDisplayData = PlaybackDesign.songDisplayData;
                    VisualizerExporterDesign.this.currentTrack = PlaybackDesign.currentDisplayTrack;
                    VisualizerExporterDesign.this.duration = PlaybackDesign.playingMediaInfo.duration;
                }
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onRequestFrameCaptureMode.subscribeWeak(new WeakEventR.Handler<FrameCaptureDesc>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public FrameCaptureDesc invoke() {
                if (VisualizerExporterDesign.exporting) {
                    return VisualizerExporterDesign.this.frameCaptureDesc;
                }
                VisualizerExporterDesign.this.glThreadSignalDoneExporting = true;
                VisualizerExporterDesign.this.destroyEncoder(null);
                return null;
            }
        }, this.listenerRefHolder);
        VisualizerViewCore.onOffScreenContentRendered.subscribeWeak(new WeakEvent3.Handler<Texture, Boolean, String>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.17
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Texture texture, Boolean bool, final String str) {
                int i;
                if (!VisualizerExporterDesign.exporting) {
                    VisualizerExporterDesign.this.glThreadSignalDoneExporting = true;
                    return;
                }
                if (!bool.booleanValue() || VisualizerExporterDesign.this.loadingResourcesFrameCounter >= 600) {
                    if (VisualizerExporterDesign.this.loadingResourcesFrameCounter >= 600) {
                        VisualizerExporterDesign.this.exportingErrorStatus.addErrorMessageCollapsed("loading resources timeout");
                    }
                    if (!VisualizerExporterDesign.this.frameCaptureDesc.captureToImageBuffer) {
                        if (VisualizerExporterDesign.this.frameCounter == VisualizerExporterDesign.this.startFrameToCapture) {
                            VisualizerExporterDesign.this.exportingErrorStatus.reset();
                            System.gc();
                            if (VisualizerExporterDesign.this.videoEncoder == null) {
                                if (VisualizerExporterDesign.this.lastAudioFrameData != null) {
                                    i = VisualizerExporterDesign.this.lastAudioFrameData.sampleRate;
                                } else {
                                    i = 44100;
                                }
                                if (VisualizerExporterDesign.this.lastAudioFrameData == null) {
                                    VisualizerExporterDesign.this.exportingErrorStatus.addErrorMessage("failed to get audio sampleRate");
                                }
                                try {
                                    String strCreateEncoder = VisualizerExporterDesign.this.createEncoder(texture.getWidth(), texture.getHeight(), i, VisualizerExporterDesign.this.exportFile, VisualizerExporterDesign.this.exportOStream);
                                    if (strCreateEncoder != null) {
                                        VisualizerExporterDesign.this.exportingErrorStatus.addCriticalError(strCreateEncoder);
                                    }
                                } catch (IOException e) {
                                    tlog.e(e, "IOException during encoder creation");
                                    VisualizerExporterDesign.this.exportingErrorStatus.addCriticalError("IO error while creating encoder: " + e.getMessage());
                                }
                            }
                        }
                        if (VisualizerExporterDesign.this.frameCounter >= VisualizerExporterDesign.this.startFrameToCapture && VisualizerExporterDesign.this.frameCounter <= VisualizerExporterDesign.this.endFrameToCapture && VisualizerExporterDesign.this.videoEncoder != null && !VisualizerExporterDesign.this.exportingErrorStatus.hasCriticalError()) {
                            LyricsElement.injectExportTime(VisualizerExporterDesign.outputTrackPositionUs / 1000);
                            VisualizerExporterDesign.this.videoEncoder.frameAvailableSoon();
                            if (!VisualizerExporterDesign.this.videoEncoder.addFrameImageOGL(texture, VisualizerExporterDesign.outputTrackPositionUs, VisualizerExporterDesign.this.exportFrameIndex)) {
                                tlog.w("failed to add video frame");
                                VisualizerExporterDesign.this.exportingErrorStatus.addErrorMessageCollapsed("failed to add video frame");
                            }
                            if (VisualizerExporterDesign.this.lastAudioFrameData != null && VisualizerExporterDesign.this.videoEncoder != null) {
                                VisualizerExporterDesign.this.lastAudioFrameData.capturedDataPositionEndUs = VisualizerExporterDesign.outputTrackPositionUs;
                                VisualizerExporterDesign.this.videoEncoder.audioFrameAvailableSoon(VisualizerExporterDesign.this.lastAudioFrameData.pcmAsDesiredChannelCount(VisualizerExporterDesign.this.videoEncoder.getAudioInputFormatChannelCount(), VisualizerExporterDesign.this.lastAudioFrameData.getBufferSizeForTimedResult()), VisualizerExporterDesign.this.lastAudioFrameData.capturedDataPositionEndUs);
                            }
                        }
                        if ((VisualizerExporterDesign.this.frameCounter == VisualizerExporterDesign.this.endFrameToCapture || VisualizerExporterDesign.this.exportingErrorStatus.hasCriticalError()) && VisualizerExporterDesign.this.videoEncoder != null) {
                            VisualizerExporterDesign.this.videoEncoder.stop(VisualizerExporterDesign.this.exportingErrorStatus);
                        }
                        if (VisualizerExporterDesign.this.frameCounter != VisualizerExporterDesign.this.endFrameToCapture + 30 && !VisualizerExporterDesign.this.exportingErrorStatus.hasCriticalError()) {
                            if ((VisualizerExporterDesign.this.frameCounter + 10) % 20 == 0) {
                                VisualizerExporterDesign.this.mainThreadHandler.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.17.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (VisualizerExporterDesign.this.exportingStatus == null) {
                                            VisualizerExporterDesign.this.exportingStatus = new VisExportDialog.ExportingProgressStatus();
                                        }
                                        VisualizerExporterDesign.this.exportingStatus.active = VisualizerExporterDesign.exporting;
                                        VisualizerExporterDesign.this.exportingStatus.isLoadingResources = false;
                                        VisualizerExporterDesign.this.exportingStatus.progressMax = Math.max(VisualizerExporterDesign.this.endFrameToCapture - VisualizerExporterDesign.this.startFrameToCapture, 1);
                                        VisualizerExporterDesign.this.exportingStatus.progressValue = Math.max(Math.min(VisualizerExporterDesign.this.frameCounter - VisualizerExporterDesign.this.startFrameToCapture, VisualizerExporterDesign.this.exportingStatus.progressMax), 0);
                                        VisExportDialog.updateExportingProgressStatus(VisualizerExporterDesign.this.exportingStatus);
                                    }
                                });
                            }
                        } else {
                            VisualizerExporterDesign visualizerExporterDesign = VisualizerExporterDesign.this;
                            visualizerExporterDesign.destroyEncoder(visualizerExporterDesign.exportingErrorStatus);
                            VisualizerExporterDesign.exporting = false;
                            final StatusMsg statusMsg = new StatusMsg(VisualizerExporterDesign.this.exportingErrorStatus);
                            VisualizerExporterDesign.this.mainThreadHandler.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.17.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    VisualizerExporterDesign.this.stopExport(true, statusMsg, true);
                                }
                            });
                        }
                        VisualizerExporterDesign visualizerExporterDesign2 = VisualizerExporterDesign.this;
                        visualizerExporterDesign2.setFrameCounter(visualizerExporterDesign2.frameCounter + 1);
                        return;
                    }
                    return;
                }
                VisualizerExporterDesign.access$2608(VisualizerExporterDesign.this);
                if (VisualizerExporterDesign.this.loadingResourcesFrameCounter % 30 == 0) {
                    VisualizerExporterDesign.this.mainThreadHandler.post(new Runnable() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.17.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (VisualizerExporterDesign.this.exportingStatus == null) {
                                VisualizerExporterDesign.this.exportingStatus = new VisExportDialog.ExportingProgressStatus();
                            }
                            VisualizerExporterDesign.this.exportingStatus.active = true;
                            VisualizerExporterDesign.this.exportingStatus.isLoadingResources = true;
                            VisualizerExporterDesign.this.exportingStatus.progressMax = 1;
                            VisualizerExporterDesign.this.exportingStatus.progressValue = 0;
                            VisualizerExporterDesign.this.exportingStatus.statText = str;
                            VisExportDialog.updateExportingProgressStatus(VisualizerExporterDesign.this.exportingStatus);
                        }
                    });
                }
            }
        }, this.listenerRefHolder);
        MediaPlaybackService.onPlaybackCompleted.subscribeWeak(new WeakEvent5.Handler<Integer, Boolean, Long, Boolean, PlaylistSong>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.18
            @Override // com.daaw.avee.Common.Events.WeakEvent5.Handler
            public void invoke(Integer num, Boolean bool, Long l, Boolean bool2, PlaylistSong playlistSong) {
                if (!VisualizerExporterDesign.exporting) {
                    playbackDesign.onPlaybackCompleted(num, bool, l, bool2.booleanValue(), playlistSong);
                }
            }
        }, this.listenerRefHolder);
        AppDesign.onRequestRenderQualityEdgesValueExport.subscribeWeak(new WeakEventR1.Handler<Context, Integer>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.19
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public Integer invoke(Context context) {
                return 1;
            }
        }, this.listenerRefHolder);
        AppDesign.onRequestAudioProcessMultiThreadValueExport.subscribeWeak(new WeakEventR1.Handler<Context, Integer>() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.20
            @Override // com.daaw.avee.Common.Events.WeakEventR1.Handler
            public Integer invoke(Context context) {
                return 0;
            }
        }, this.listenerRefHolder);
    }

    public void setFrameCounter(int i) {
        this.frameCounter = i;
        this.exportFrameIndex = this.frameCounter - this.startFrameToCapture;
        int i2 = this.targetFps;
        outputTrackPositionUs = AudioVideoCTSRecorder.computePresentationTime(this.exportFrameIndex, i2);
        audioTrackPositionUs = Math.max(0L, AudioVideoCTSRecorder.computePresentationTime(this.frameCounter, i2));
    }

    public String getExportDirectoryPath(String str) {
        File filesDir;
        try {
            filesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } catch (Exception e) {
            filesDir = null;
        }
        if (filesDir == null) {
            try {
                filesDir = PlayerCore.s().getAppContext().getFilesDir();
            } catch (Exception e2) {
            }
        }
        if (filesDir != null) {
            try {
                return filesDir.getCanonicalPath();
            } catch (Exception e3) {
                return str;
            }
        }
        return str;
    }

    void startExportCreateFile(Fragment fragment, ContextData contextData, VisExportDialog.ExportSettings exportSettings, long j, long j2, long j3) {
        if (exportSettings.destinationDir == null || exportSettings.destinationDir.length() < 1) {
            exportSettings.destinationDir = "/";
        }
        if (exportSettings.filename == null || exportSettings.filename.length() < 1) {
            exportSettings.filename = "exported";
        }
        String strExtractFilenameWithoutExt = UtilsFileSys.extractFilenameWithoutExt(exportSettings.filename);
        this.currentExportSettings = exportSettings;
        this.currentStartMSec = j;
        this.currentEndMSec = j2;
        this.currentDullTrackDurationMs = j3;
        File fileCreateFile = createFile(exportSettings.destinationDir, strExtractFilenameWithoutExt, AudioVideoCTSRecorder.getOutputFileExtensionWithDot());
        try {
            fileCreateFile.createNewFile();
        } catch (IOException e) {
        }
        if (fileCreateFile.canWrite()) {
            startExport(contextData, exportSettings, fileCreateFile, null, j, j2, j3);
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType(AudioVideoCTSRecorder.getOutputFileMimeType());
            intent.putExtra("android.intent.extra.TITLE", strExtractFilenameWithoutExt);
            fragment.startActivityForResult(intent, 54);
        }
    }

    void startExport(ContextData contextData, VisExportDialog.ExportSettings exportSettings, File file, OutputStream outputStream, long j, long j2, long j3) {
        if (Build.VERSION.SDK_INT >= 18 && !exporting) {
            this.exportingErrorStatus.reset();
            this.mainUIDesign.setOverrideForceLockOrient(true);
            System.gc();
            long jMax = Math.max(Math.min(j, j2), 0L);
            long jMin = Math.min(Math.max(jMax, j2), j3 - 50);
            this.targetFps = Math.max(exportSettings.profileActive.frameRate, 1);
            this.targetFrameTimeUs = 1000000 / ((long) this.targetFps);
            this.encodingTargetBitrateMbps = exportSettings.profileActive.videoBitrateMbps;
            this.targetVideoForceCompatibleWH = exportSettings.videoForceCompatibleWH;
            this.targetExportAudio = exportSettings.exportAudio;
            this.targetAudioBitrateKbps = exportSettings.profileActive.audioBitrateKbps;
            this.targetAudioChannels = exportSettings.profileActive.audioChannels;
            this.targetVideoCodec = exportSettings.getVideoCodec(exportSettings.videoCodecActiveIndex);
            this.targetVideoMimeType = exportSettings.getVideoMimeTypeCodec(exportSettings.videoCodecActiveIndex);
            this.targetVideoCodecProfile = exportSettings.profileActive.videoCodecProfile;
            boolean z = exportSettings.hideAppLogo;
            int iMin = Math.min(8192, Math.max(16, exportSettings.profileActive.width));
            int iMin2 = Math.min(8192, Math.max(16, exportSettings.profileActive.height));
            if (this.targetVideoForceCompatibleWH) {
                iMin = AudioVideoCTSRecorder.getCompatibleVideoWidth(iMin);
                iMin2 = AudioVideoCTSRecorder.getCompatibleVideoHeight(iMin2);
            }
            this.frameCaptureDesc = new FrameCaptureDesc();
            this.frameCaptureDesc.captureToImageBuffer = false;
            this.frameCaptureDesc.overrideFrameTimeMs = (int) (this.targetFrameTimeUs / 1000);
            this.frameCaptureDesc.overrideScreenWidth = iMin;
            this.frameCaptureDesc.overrideScreenHeight = iMin2;
            AppPreferences appPreferencesCreateOrGetInstance = AppPreferences.createOrGetInstance();
            this.audioFrameRequest.audioChannelCount = this.targetAudioChannels;
            this.audioFrameRequest.useGlobalSession = appPreferencesCreateOrGetInstance.getBool(AppPreferences.PREF_Bool_visualizerUseGlobalSession);
            this.audioFrameRequest.overridePositionUs = -1L;
            this.audioFrameRequest.audioOffsetMs = appPreferencesCreateOrGetInstance.getInt(AppPreferences.PREF_Int_audioVisOffset);
            long j4 = 1000 * jMax;
            this.startFrameToCapture = (int) (j4 / this.targetFrameTimeUs);
            this.endFrameToCapture = (int) ((1000 * jMin) / this.targetFrameTimeUs);
            this.endFrameToCapture = Math.max(this.startFrameToCapture + 1, this.endFrameToCapture);
            outputTrackDurationUs = ((long) (this.endFrameToCapture - this.startFrameToCapture)) * this.targetFrameTimeUs;
            setFrameCounter(this.startFrameToCapture - 60);
            this.loadingResourcesFrameCounter = 0;
            this.overrideAudioFrameSamplesInCount = 0;
            this.exportFile = file;
            this.exportOStream = outputStream;
            if (this.exportFile == null && this.exportOStream == null) {
                EventsGlobalTextNotifier.onTextMsg.invoke(contextData.getContext().getResources().getString(R.string.vis_export_failed_create_file));
            }
            if (this.exportFile == null && this.exportOStream == null) {
                VisExportDialog.ExportingProgressStatus exportingProgressStatus = new VisExportDialog.ExportingProgressStatus();
                exportingProgressStatus.active = false;
                VisExportDialog.updateExportingProgressStatus(exportingProgressStatus);
                return;
            }
            VisExportDialog.ExportingProgressStatus exportingProgressStatus2 = new VisExportDialog.ExportingProgressStatus();
            exportingProgressStatus2.active = true;
            exportingProgressStatus2.progressValue = 0;
            exportingProgressStatus2.progressMax = 100;
            VisExportDialog.updateExportingProgressStatus(exportingProgressStatus2);
            EventsPlaybackService.Receive.onAction.invoke(1);
            EventsPlaybackService.Receive.onSeekChanged.invoke(0L);
            readjustSeek(j4, true);
            AppPreferences.createOrGetInstance().setInt(AppPreferences.PREF_Int_playbackEngine, 1);
            exporting = true;
            onExportingChanged.invoke(Boolean.valueOf(exporting), Long.valueOf(j3), Boolean.valueOf(z));
            resetVisualizer();
        }
    }

    public void stopExport(boolean z, StatusMsg statusMsg, boolean z2) {
        if (Build.VERSION.SDK_INT >= 18) {
            LyricsElement.injectExportTime(-1L);
            if (z2 || exporting) {
                this.mainUIDesign.setOverrideForceLockOrient(false);
                this.glThreadSignalDoneExporting = false;
                exporting = false;
                onExportingChanged.invoke(Boolean.valueOf(exporting), 0L, false);
                int i = 0;
                while (!this.glThreadSignalDoneExporting && i < 50) {
                    i++;
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!this.glThreadSignalDoneExporting) {
                    tlog.w("glThreadSignalDoneExporting timed out");
                }
                this.exportingStatus = null;
                this.playbackDesign.updateKeepScreenOnLock(false);
                EventsPlaybackService.Receive.onAction.invoke(2);
                VisExportDialog.ExportingProgressStatus exportingProgressStatus = new VisExportDialog.ExportingProgressStatus();
                exportingProgressStatus.active = false;
                VisExportDialog.updateExportingProgressStatus(exportingProgressStatus);
                Context appContext = PlayerCore.s().getAppContext();
                if (appContext != null) {
                    if (this.exportFile != null) {
                        tlog.d("notify file: " + Uri.fromFile(this.exportFile));
                        UtilsFileSys.scanFile(appContext, this.exportFile.getAbsolutePath());
                    }
                    if (z) {
                        if (this.exportFile != null) {
                            Uri uriFromFile = Uri.fromFile(this.exportFile);
                            showFileSavedNotification(3, appContext, "Exported visualizer", uriFromFile, UtilsFileSys.extractFilename(uriFromFile.getPath()), MimeTypes.VIDEO_MP4);
                        }
                        String fullMessage = statusMsg != null ? statusMsg.getFullMessage() : "";
                        if (fullMessage != null && fullMessage.length() > 0) {
                            Activity activityContextAsActivity = PlayerCore.s().getActivityContextAsActivity();
                            if (activityContextAsActivity != null && !activityContextAsActivity.isFinishing()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activityContextAsActivity);
                                builder.setMessage(fullMessage);
                                builder.setTitle(R.string.vis_export_errors_title);
                                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() { // from class: com.daaw.avee.Design.VisualizerExporterDesign.21
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface dialogInterface, int i2) {
                                    }
                                });
                                AlertDialog alertDialogCreate = builder.create();
                                alertDialogCreate.show();
                                TextView textView = (TextView) alertDialogCreate.findViewById(android.R.id.message);
                                if (textView != null) {
                                    textView.setTextSize(2, 14.0f);
                                }
                            }
                        } else {
                            EventsGlobalTextNotifier.onTextMsg.invoke(appContext.getResources().getString(R.string.vis_export_finished));
                        }
                    } else {
                        EventsGlobalTextNotifier.onTextMsg.invoke(appContext.getResources().getString(R.string.vis_export_canceled));
                    }
                }
                this.frameCaptureDesc = null;
                this.exportFile = null;
                resetVisualizer();
                if (z) {
                    onExportSuccess.invoke();
                }
            }
        }
    }

    public static void showFileSavedNotification(int i, Context context, String str, Uri uri, String str2, String str3) {
        if (Build.VERSION.SDK_INT < 24) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(uri, str3);
                PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentTitle(str2).setContentText(str).setSmallIcon(R.drawable.ic_save5).setContentIntent(activity).setLocalOnly(true).setAutoCancel(true);
                notificationManager.notify(i, builder.build());
            } catch (Exception e) {
            }
        }
    }

    File createFile(String str, String str2, String str3) {
        return createFile(str, str2, str3, 0);
    }

    File createFile(String str, String str2, String str3, int i) {
        File file = new File(str, UtilsFileSys.fixToValidFilename(String.valueOf(str2) + " " + i + str3));
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }
        return (!file.exists() || i >= 1000) ? file : createFile(str, str2, str3, i + 1);
    }

    public String createEncoder(int i, int i2, int i3, File file, OutputStream outputStream) throws IOException {
        File fileCreateTempFile;
        String str;
        this.exportingStats.reset();
        destroyEncoder(null);
        tlog.w("initialize VideoSequenceEncoder");
        this.videoEncoder = new AudioVideoCTSRecorder();
        if (file != null || outputStream == null) {
            fileCreateTempFile = file;
        } else {
            try {
                fileCreateTempFile = File.createTempFile("aveeexport", null);
            } catch (IOException e) {
                fileCreateTempFile = file;
            }
        }
        if (fileCreateTempFile != null) {
            String strInitialize = this.videoEncoder.initialize(fileCreateTempFile, outputStream, this.targetFps, i, i2, this.encodingTargetBitrateMbps, this.targetExportAudio, this.targetAudioBitrateKbps, this.targetAudioChannels, i3, this.targetVideoCodec, this.targetVideoMimeType, this.targetVideoCodecProfile);
            if (strInitialize != null) {
                this.exportingStats.criticalFailure = true;
                if (this.targetFps > 40) {
                    str = "Failed to initialize encoder: " + strInitialize + " Try lower value for Fps?";
                } else {
                    str = "Failed to initialize encoder: " + strInitialize;
                }
            } else {
                str = null;
            }
        } else {
            this.exportingStats.criticalFailure = true;
            str = "Failed to create file";
        }
        if (str != null) {
            tlog.w(str);
        }
        return str;
    }

    void destroyEncoder(StatusMsg statusMsg) {
        if (this.videoEncoder != null) {
            this.videoEncoder.finish(statusMsg);
            this.videoEncoder.release();
            this.videoEncoder = null;
        }
    }

    void resetVisualizer() {
        VisualizerViewCore visualizerViewCore = this.visualizerSurfaceView.get();
        if (visualizerViewCore != null) {
            visualizerViewCore.resetVisualizerTh();
        }
    }

    private long readjustSeek(long j, boolean z) {
        EventsPlaybackService.Receive.onSeekChanged.invoke(Long.valueOf(Math.max(0L, j - 300000) / 1000));
        EventsPlaybackService.Receive.onAction.invoke(1);
        return 300L;
    }
}
