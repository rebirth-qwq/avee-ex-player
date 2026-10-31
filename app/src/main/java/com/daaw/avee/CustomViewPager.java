package com.daaw.avee;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

/* JADX INFO: loaded from: classes.dex */
public class CustomViewPager extends ViewPager {
    boolean eventFired;
    float lastProgress;
    OnSwipeOutListener listener;
    float maxProgress;
    float startDragX;
    float swipeDistMin;

    public interface OnSwipeOutListener {
        void onSwipeOutAtEnd();

        void onSwipeOutAtStart();

        void onSwipeProgressUpdate(float f);
    }

    public CustomViewPager(Context context) {
        super(context);
        this.eventFired = false;
        this.swipeDistMin = 0.0f;
        this.lastProgress = 0.0f;
        this.maxProgress = 0.0f;
        this.swipeDistMin = context.getResources().getDimension(re.ex.avee.R.dimen.out_of_bound_swipe_dist);
    }

    public CustomViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.eventFired = false;
        this.swipeDistMin = 0.0f;
        this.lastProgress = 0.0f;
        this.maxProgress = 0.0f;
        this.swipeDistMin = context.getResources().getDimension(re.ex.avee.R.dimen.out_of_bound_swipe_dist);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener onSwipeOutListener) {
        this.listener = onSwipeOutListener;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        if ((action & 255) == 0) {
            this.startDragX = x;
            this.eventFired = false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x006b  */
    /* JADX WARN: Code duplicated, block: B:27:0x0074  */
    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        int i = action & 255;
        if (i == 0) {
            this.startDragX = x;
            this.eventFired = false;
            this.lastProgress = 0.0f;
            this.maxProgress = 0.0f;
            this.listener.onSwipeProgressUpdate(0.0f);
        } else if (i != 1) {
            if (i == 2 && !this.eventFired) {
                if (getCurrentItem() == 0) {
                    f2 = x - this.startDragX;
                    f3 = this.swipeDistMin;
                } else {
                    if (getCurrentItem() == getAdapter().getCount() - 1) {
                        f2 = this.startDragX - x;
                        f3 = this.swipeDistMin;
                    } else {
                        f = 0.0f;
                    }
                    float fMax = Math.max(this.maxProgress, f);
                    this.maxProgress = fMax;
                    this.listener.onSwipeProgressUpdate(fMax);
                    if (x - this.startDragX <= this.swipeDistMin && getCurrentItem() == 0) {
                        this.maxProgress = 0.0f;
                        this.listener.onSwipeProgressUpdate(0.0f);
                        this.listener.onSwipeOutAtStart();
                        this.eventFired = true;
                    } else if (this.startDragX - x > this.swipeDistMin && getCurrentItem() == getAdapter().getCount() - 1) {
                        this.maxProgress = 0.0f;
                        this.listener.onSwipeProgressUpdate(0.0f);
                        this.listener.onSwipeOutAtEnd();
                        this.eventFired = true;
                    }
                }
                f = f2 / f3;
                float fMax2 = Math.max(this.maxProgress, f);
                this.maxProgress = fMax2;
                this.listener.onSwipeProgressUpdate(fMax2);
                if (x - this.startDragX <= this.swipeDistMin) {
                    if (this.startDragX - x > this.swipeDistMin) {
                        this.maxProgress = 0.0f;
                        this.listener.onSwipeProgressUpdate(0.0f);
                        this.listener.onSwipeOutAtEnd();
                        this.eventFired = true;
                    }
                } else if (this.startDragX - x > this.swipeDistMin) {
                    this.maxProgress = 0.0f;
                    this.listener.onSwipeProgressUpdate(0.0f);
                    this.listener.onSwipeOutAtEnd();
                    this.eventFired = true;
                }
            }
        } else if (!this.eventFired) {
            this.maxProgress = 0.0f;
            this.listener.onSwipeProgressUpdate(0.0f);
        }
        return super.onTouchEvent(motionEvent);
    }
}
