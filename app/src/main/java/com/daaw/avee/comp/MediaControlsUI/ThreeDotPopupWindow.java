package com.daaw.avee.comp.MediaControlsUI;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.daaw.avee.Common.UtilsUI;
import com.google.android.material.badge.BadgeDrawable;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
class ThreeDotPopupWindow extends PopupWindow {
    private ImageView btn3;
    private ImageView btn4;
    int buttonColor;
    private ImageView[] buttonIcons;
    private TextView[] buttonTexts;
    private Handler handler;
    private ImageView musicSys0Button;
    private ImageView musicSys1Button;
    private ImageView repeatAllButton;
    private ImageView repeatOnceButton;

    public void onShuffleModeChanged(int i) {
    }

    public ThreeDotPopupWindow(View view) {
        super(view.getContext(), (AttributeSet) null, 0, R.style.MyListPopupWindowDarkStyle);
        this.buttonTexts = new TextView[7];
        this.buttonIcons = new ImageView[7];
        this.handler = new Handler(new Handler.Callback() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                if (message.what != 0) {
                    return false;
                }
                UtilsUI.dismissSafe(ThreeDotPopupWindow.this);
                return false;
            }
        });
        this.buttonColor = UtilsUI.getAttrColor(view, R.attr.mediaControlsFG);
        View viewInflate = View.inflate(view.getContext(), R.layout.popup_media_controls_overflow, null);
        View[] viewArr = new View[7];
        viewArr[0] = viewInflate.findViewById(R.id.groupPlaybackEngine0);
        viewArr[1] = viewInflate.findViewById(R.id.groupPlaybackEngine1);
        viewArr[2] = viewInflate.findViewById(R.id.groupVideoFit);
        viewArr[3] = viewInflate.findViewById(R.id.groupSwitchScreen);
        viewArr[4] = viewInflate.findViewById(R.id.groupRepeatOnce);
        viewArr[5] = viewInflate.findViewById(R.id.groupRepeatAll);
        this.musicSys0Button = (ImageView) viewInflate.findViewById(R.id.btnPlaybackEngine0);
        viewArr[0].setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onSelectMusicSysAction.invoke(0);
            }
        });
        this.musicSys1Button = (ImageView) viewInflate.findViewById(R.id.btnPlaybackEngine1);
        viewArr[1].setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onSelectMusicSysAction.invoke(1);
            }
        });
        this.btn3 = (ImageView) viewInflate.findViewById(R.id.btnVideoFit);
        viewArr[2].setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onToggleVideoScalingMode.invoke();
            }
        });
        this.btn4 = (ImageView) viewInflate.findViewById(R.id.btnSwitchScreen);
        viewArr[3].setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onToggleVisualPreferShowContent.invoke();
            }
        });
        this.repeatOnceButton = (ImageView) viewInflate.findViewById(R.id.btnRepeatOnce);
        viewArr[4].setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onSetRepeatMode.invoke(Integer.valueOf(MediaControlsUI.onRequestRepeatMode.invoke(0).intValue() != 1 ? 1 : 0));
            }
        });
        this.repeatAllButton = (ImageView) viewInflate.findViewById(R.id.btnRepeatAll);
        viewArr[5].setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.MediaControlsUI.ThreeDotPopupWindow.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                MediaControlsUI.onSetRepeatMode.invoke(Integer.valueOf(MediaControlsUI.onRequestRepeatMode.invoke(0).intValue() != 2 ? 2 : 0));
            }
        });
        setContentView(viewInflate);
        setHeight(-2);
        setWidth(-2);
        setOutsideTouchable(true);
        setFocusable(true);
        this.btn4.setColorFilter(this.buttonColor);
        updateVideoScaleMode(MediaControlsUI.onRequestVideoScalingMode.invoke(0).intValue());
        onMusicSysChanged(MediaControlsUI.onRequestMusicSystemIndex.invoke(-1).intValue());
        onRepeatModeChanged(MediaControlsUI.onRequestRepeatMode.invoke(0).intValue());
        onShuffleModeChanged(MediaControlsUI.onRequestShuffleMode.invoke(0).intValue());
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
        int dimension2 = (int) view.getResources().getDimension(R.dimen.player_controls_volume_popup_offset_x);
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int iHeight = rect.height();
        int[] iArr2 = new int[2];
        view.getLocationOnScreen(iArr2);
        showAtLocation(view, BadgeDrawable.BOTTOM_START, iArr2[0] - dimension2, (iHeight - iArr2[1]) + dimension);
    }

    public void onRepeatModeChanged(int i) {
        if (i == 1) {
            ImageView imageView = this.repeatOnceButton;
            imageView.setColorFilter(UtilsUI.getAttrColor(imageView, R.attr.mediaControlsHighLight));
            this.repeatAllButton.setColorFilter(this.buttonColor);
        } else if (i == 2) {
            this.repeatOnceButton.setColorFilter(this.buttonColor);
            ImageView imageView2 = this.repeatAllButton;
            imageView2.setColorFilter(UtilsUI.getAttrColor(imageView2, R.attr.mediaControlsHighLight));
        } else {
            this.repeatOnceButton.setColorFilter(this.buttonColor);
            this.repeatAllButton.setColorFilter(this.buttonColor);
        }
    }

    public void onMusicSysChanged(int i) {
        if (i == 0) {
            ImageView imageView = this.musicSys0Button;
            imageView.setColorFilter(UtilsUI.getAttrColor(imageView, R.attr.mediaControlsHighLight));
            ImageView imageView2 = this.musicSys1Button;
            imageView2.setColorFilter(UtilsUI.getAttrColor(imageView2, R.attr.mediaControlsFG));
            return;
        }
        ImageView imageView3 = this.musicSys0Button;
        imageView3.setColorFilter(UtilsUI.getAttrColor(imageView3, R.attr.mediaControlsFG));
        ImageView imageView4 = this.musicSys1Button;
        imageView4.setColorFilter(UtilsUI.getAttrColor(imageView4, R.attr.mediaControlsHighLight));
    }

    public void updateVideoScaleMode(int i) {
        if (i == 1) {
            this.btn3.setImageResource(R.drawable.ic_vis_fit3);
        } else if (i == 2) {
            this.btn3.setImageResource(R.drawable.ic_vis_fit_crop3);
        } else if (i == 3) {
            this.btn3.setImageResource(R.drawable.ic_vis_stretch3);
        }
        this.btn3.setColorFilter(this.buttonColor);
    }
}
