package com.daaw.avee.Design;

import com.daaw.avee.Common.Events.WeakEvent;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.ContextData;
import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.SleepTimer.SleepTimer;
import com.daaw.avee.comp.SleepTimer.SleepTimerConfig;
import com.daaw.avee.comp.SleepTimer.SleepTimerDialog;
import com.daaw.avee.comp.playback.EventsPlaybackService;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SleepTimerDesign {
    private List<Object> listenerRefHolder = new LinkedList();

    public SleepTimerDesign() {
        MainActivity.onMainUIAction.subscribeWeak(new WeakEvent2.Handler<Integer, ContextData>() { // from class: com.daaw.avee.Design.SleepTimerDesign.1
            @Override // com.daaw.avee.Common.Events.WeakEvent2.Handler
            public void invoke(Integer num, ContextData contextData) {
                SleepTimer sleepTimerCreateOrGetInstance;
                if (contextData.getFragmentManager() == null) {
                    return;
                }
                if (num.intValue() == 1) {
                    SleepTimerDialog.createAndShowSleepTimerDialog(contextData);
                } else {
                    if (num.intValue() != 10 || (sleepTimerCreateOrGetInstance = SleepTimer.createOrGetInstance()) == null) {
                        return;
                    }
                    SleepTimerConfig config = sleepTimerCreateOrGetInstance.getConfig();
                    sleepTimerCreateOrGetInstance.configure(false, config.minutes, sleepTimerCreateOrGetInstance.getSleepAtTime(), config.playLastSongToEnd);
                }
            }
        }, this.listenerRefHolder);
        MainActivity.onMainUIRequestSleepTimerConfig.subscribeWeak(new WeakEventR.Handler<SleepTimerConfig>() { // from class: com.daaw.avee.Design.SleepTimerDesign.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public SleepTimerConfig invoke() {
                SleepTimer sleepTimerCreateOrGetInstance = SleepTimer.createOrGetInstance();
                if (sleepTimerCreateOrGetInstance == null) {
                    return new SleepTimerConfig();
                }
                return sleepTimerCreateOrGetInstance.getConfig();
            }
        }, this.listenerRefHolder);
        SleepTimer.onSleepTimerConfigChanged.subscribeWeak(new WeakEvent3.Handler<Boolean, Integer, Boolean>() { // from class: com.daaw.avee.Design.SleepTimerDesign.3
            @Override // com.daaw.avee.Common.Events.WeakEvent3.Handler
            public void invoke(Boolean bool, Integer num, Boolean bool2) {
                SleepTimerConfig sleepTimerConfig = new SleepTimerConfig();
                sleepTimerConfig.enabled = bool.booleanValue();
                sleepTimerConfig.minutes = num.intValue();
                sleepTimerConfig.playLastSongToEnd = bool2.booleanValue();
                MainActivity mainActivity = MainActivity.getInstance();
                if (mainActivity != null) {
                    mainActivity.updateSleepTimerIndicatorTh(sleepTimerConfig.enabled, false);
                }
            }
        }, this.listenerRefHolder);
        SleepTimer.onSleepTimerFire.subscribeWeak(new WeakEvent.Handler() { // from class: com.daaw.avee.Design.SleepTimerDesign.4
            @Override // com.daaw.avee.Common.Events.WeakEvent.Handler
            public void invoke() {
                EventsPlaybackService.Receive.onAction.invoke(3);
            }
        }, this.listenerRefHolder);
        SleepTimerDialog.onSleepTimerUISubmit.subscribeWeak(new WeakEvent4.Handler<Boolean, Integer, Date, Boolean>() { // from class: com.daaw.avee.Design.SleepTimerDesign.5
            @Override // com.daaw.avee.Common.Events.WeakEvent4.Handler
            public void invoke(Boolean bool, Integer num, Date date, Boolean bool2) {
                SleepTimer sleepTimerCreateOrGetInstance = SleepTimer.createOrGetInstance();
                if (sleepTimerCreateOrGetInstance == null) {
                    return;
                }
                sleepTimerCreateOrGetInstance.configure(bool.booleanValue(), num.intValue(), date, bool2.booleanValue());
            }
        }, this.listenerRefHolder);
        SleepTimerDialog.onSleepTimerUIRequestSleepTimerConfig.subscribeWeak(new WeakEventR.Handler<SleepTimerConfig>() { // from class: com.daaw.avee.Design.SleepTimerDesign.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public SleepTimerConfig invoke() {
                SleepTimer sleepTimerCreateOrGetInstance = SleepTimer.createOrGetInstance();
                if (sleepTimerCreateOrGetInstance == null) {
                    return new SleepTimerConfig();
                }
                return sleepTimerCreateOrGetInstance.getConfig();
            }
        }, this.listenerRefHolder);
        SleepTimerDialog.onSleepTimerUIRequestRemainingSeconds.subscribeWeak(new WeakEventR.Handler<Integer>() { // from class: com.daaw.avee.Design.SleepTimerDesign.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public Integer invoke() {
                SleepTimer sleepTimerCreateOrGetInstance = SleepTimer.createOrGetInstance();
                if (sleepTimerCreateOrGetInstance == null) {
                    return 0;
                }
                return Integer.valueOf(sleepTimerCreateOrGetInstance.getRemainingSeconds());
            }
        }, this.listenerRefHolder);
        SleepTimerDialog.onSleepTimerUIRequestSleepAtTime.subscribeWeak(new WeakEventR.Handler() { // from class: com.daaw.avee.Design.-$$Lambda$SleepTimerDesign$QvdwOxUasRD4RAntQuqr5EhP5gY
            @Override // com.daaw.avee.Common.Events.WeakEventR.Handler
            public final Object invoke() {
                return SleepTimerDesign.lambda$new$0();
            }
        }, this.listenerRefHolder);
    }

    static /* synthetic */ Tuple2 lambda$new$0() {
        SleepTimer sleepTimerCreateOrGetInstance = SleepTimer.createOrGetInstance();
        if (sleepTimerCreateOrGetInstance == null) {
            return new Tuple2(new Date(), false);
        }
        return new Tuple2(sleepTimerCreateOrGetInstance.getSleepAtTime(), Boolean.valueOf(sleepTimerCreateOrGetInstance.getConfig().enabled));
    }
}
