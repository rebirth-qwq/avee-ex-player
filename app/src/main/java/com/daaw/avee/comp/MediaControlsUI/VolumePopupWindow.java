package com.daaw.avee.comp.MediaControlsUI;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.Tuple3;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.ContextData;
import com.google.android.material.badge.BadgeDrawable;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
class VolumePopupWindow extends PopupWindow {
    private TextView audioVolumeText;
    private TextView balanceText;
    private TextView crossFadeText;
    private float crossFadeTextSizeDefault;
    private float crossFadeTextSizeSmall;
    private ImageButton eqButton;
    private View expandableView;
    private Handler handler;
    private ImageButton muteButton;
    private SeekBar seekBarAudioVolume;
    private SeekBar seekBarBalance;
    private SeekBar seekBarCrossFade;
    private ImageButton toggleExpandButton;

    public VolumePopupWindow(LayoutInflater layoutInflater, View view) {
        super(view.getContext(), (AttributeSet) null, 0, R.style.MyListPopupWindowDarkStyle);
        this.handler = new Handler(new Handler.Callback() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                return false;
            }
        });
        View viewInflate = View.inflate(view.getContext(), R.layout.popup_media_controls_volume, null);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.btnVolumeMute);
        this.muteButton = imageButton;
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onToggleMuteAction.invoke();
                VolumePopupWindow.this.handler.sendEmptyMessageDelayed(0, 900L);
            }
        });
        this.seekBarAudioVolume = (SeekBar) viewInflate.findViewById(R.id.seekBarVolume);
        this.audioVolumeText = (TextView) viewInflate.findViewById(R.id.txtVolume);
        final Tuple2<Integer, Integer> tuple2Invoke = MediaControlsUI.onRequestAudioVolumeState.invoke(new Tuple2<>(0, 0));
        onAudioVolumeChanged(tuple2Invoke.obj1.intValue(), tuple2Invoke.obj2.intValue(), false);
        this.seekBarAudioVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                VolumePopupWindow.this.onAudioVolumeChanged(i, ((Integer) tuple2Invoke.obj2).intValue(), true);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                VolumePopupWindow.this.handler.removeMessages(0);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                VolumePopupWindow.this.handler.sendEmptyMessageDelayed(0, 900L);
            }
        });
        boolean zBooleanValue = MediaControlsUI.onRequestAudioViewExpandedState.invoke(false).booleanValue();
        this.expandableView = viewInflate.findViewById(R.id.layoutExpandable);
        this.toggleExpandButton = (ImageButton) viewInflate.findViewById(R.id.btnExpand);
        updateExapned(zBooleanValue, true);
        this.toggleExpandButton.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (VolumePopupWindow.this.expandableView.getVisibility() == 0) {
                    VolumePopupWindow.this.updateExapned(false, false);
                    MediaControlsUI.onSetAudioViewExpandedState.invoke(false);
                } else {
                    VolumePopupWindow.this.updateExapned(true, false);
                    MediaControlsUI.onSetAudioViewExpandedState.invoke(true);
                }
            }
        });
        this.seekBarBalance = (SeekBar) viewInflate.findViewById(R.id.seekBarVolumeBalance);
        this.balanceText = (TextView) viewInflate.findViewById(R.id.txtVolumeBalanceStatus);
        final Tuple2<Integer, Integer> tuple2Invoke2 = MediaControlsUI.onRequestAudioBalanceState.invoke(new Tuple2<>(0, 0));
        onAudioStereoBalanceChanged(tuple2Invoke2.obj1.intValue(), tuple2Invoke2.obj2.intValue(), false);
        this.seekBarBalance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                VolumePopupWindow.this.onAudioStereoBalanceChanged(i, ((Integer) tuple2Invoke2.obj2).intValue(), true);
            }
        });
        this.seekBarCrossFade = (SeekBar) viewInflate.findViewById(R.id.seekBarCrossfade);
        this.crossFadeText = (TextView) viewInflate.findViewById(R.id.txtCrossfadeStatus);
        this.crossFadeTextSizeSmall = layoutInflater.getContext().getResources().getDimensionPixelSize(R.dimen.textSizeM3);
        this.crossFadeTextSizeDefault = this.crossFadeText.getTextSize();
        final Tuple3<Integer, Integer, Float> tuple3Invoke = MediaControlsUI.onRequestCrossFadeState.invoke(new Tuple3<>(0, 0, Float.valueOf(1.0f)));
        onCrossFadeChanged(tuple3Invoke.obj1.intValue(), tuple3Invoke.obj2.intValue(), tuple3Invoke.obj3.floatValue(), false);
        this.seekBarCrossFade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.6
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                VolumePopupWindow.this.onCrossFadeChanged(i, ((Integer) tuple3Invoke.obj2).intValue(), ((Float) tuple3Invoke.obj3).floatValue(), true);
            }
        });
        ImageButton imageButton2 = (ImageButton) viewInflate.findViewById(R.id.btnEq);
        this.eqButton = imageButton2;
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.VolumePopupWindow.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VolumePopupWindow.this.handler.sendEmptyMessageDelayed(0, 900L);
                MediaControlsUI.onActionEq.invoke(new ContextData(view2));
            }
        });
        setContentView(viewInflate);
        setHeight(-2);
        setWidth(-2);
        setOutsideTouchable(true);
        setFocusable(true);
        onVolumeMuteChanged(MediaControlsUI.onRequestVolumeMuteState.invoke(false).booleanValue(), MediaControlsUI.onRequestAudioEffectsActiveState.invoke(false).booleanValue());
        onEqStateChanged(MediaControlsUI.onRequestEqState.invoke(false).booleanValue());
        if (Build.VERSION.SDK_INT >= 17) {
            int[] iArr = new int[2];
            Point point = new Point();
            view.getLocationInWindow(iArr);
            view.getDisplay().getRealSize(point);
            iArr[1] = point.y - iArr[1];
            showAtLocation(view, BadgeDrawable.BOTTOM_START, iArr[0], iArr[1]);
            return;
        }
        int dimension = (int) view.getResources().getDimension(R.dimen.player_controls_volume_popup_offset);
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int iHeight = rect.height();
        int[] iArr2 = new int[2];
        view.getLocationOnScreen(iArr2);
        showAtLocation(view, BadgeDrawable.BOTTOM_START, iArr2[0], (iHeight - iArr2[1]) + dimension);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExapned(boolean z, boolean z2) {
        if (z) {
            ViewPropertyAnimator viewPropertyAnimatorRotation = this.toggleExpandButton.animate().rotation(90.0f);
            if (z2) {
                viewPropertyAnimatorRotation.setDuration(0L);
            }
            this.expandableView.setVisibility(0);
            return;
        }
        ViewPropertyAnimator viewPropertyAnimatorRotation2 = this.toggleExpandButton.animate().rotation(0.0f);
        if (z2) {
            viewPropertyAnimatorRotation2.setDuration(0L);
        }
        this.expandableView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAudioVolumeChanged(int i, int i2, boolean z) {
        int iRound = Math.round((((float) i) / ((float) i2)) * 100.0f);
        TextView textView = this.audioVolumeText;
        textView.setText(textView.getResources().getQuantityString(R.plurals.audio_volume_x, iRound, Integer.valueOf(iRound)));
        if (!z) {
            this.seekBarAudioVolume.setMax(i2);
            this.seekBarAudioVolume.setProgress(i);
        } else {
            MediaControlsUI.onSetAudioVolume.invoke(Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAudioStereoBalanceChanged(int i, int i2, boolean z) {
        int i3 = i2 / 2;
        int iRound = Math.round(((((float) (i - i3))) / ((float) i3)) * 100.0f);
        TextView textView = this.balanceText;
        textView.setText(textView.getResources().getQuantityString(R.plurals.audio_stereo_balance_x, iRound, Integer.valueOf(iRound)));
        if (!z) {
            this.seekBarBalance.setMax(i2);
            this.seekBarBalance.setProgress(i);
        } else {
            MediaControlsUI.onSetAudioStereoBalance.invoke(Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCrossFadeChanged(int i, int i2, float f, boolean z) {
        if (i == 0) {
            this.crossFadeText.setText(R.string.playback_crossfade_off);
        } else if (i == 1) {
            this.crossFadeText.setText(R.string.playback_gapless);
        } else {
            float f2 = (i - 1) * f;
            TextView textView = this.crossFadeText;
            textView.setText(textView.getResources().getQuantityString(R.plurals.playback_crossFade_x_sec, (int) f2, Float.valueOf(f2)));
        }
        if (!z) {
            this.seekBarCrossFade.setMax(i2);
            this.seekBarCrossFade.setProgress(i);
        } else {
            MediaControlsUI.onSetCrossFade.invoke(Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    public void onVolumeMuteChanged(boolean z, boolean z2) {
        if (z) {
            ImageButton imageButton = this.muteButton;
            imageButton.setColorFilter(UtilsUI.getAttrColor(imageButton, R.attr.mediaControlsHighLight));
        } else {
            ImageButton imageButton2 = this.muteButton;
            imageButton2.setColorFilter(UtilsUI.getAttrColor(imageButton2, R.attr.mediaControlsFG));
        }
    }

    public void onEqStateChanged(boolean z) {
        if (z) {
            this.eqButton.setColorFilter(UtilsUI.getAttrColor(this.muteButton, R.attr.mediaControlsHighLight));
        } else {
            this.eqButton.setColorFilter(UtilsUI.getAttrColor(this.muteButton, R.attr.mediaControlsFG));
        }
    }
}
