package com.daaw.avee.comp.Common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.flask.colorpicker.slider.AlphaSlider;

/* JADX INFO: loaded from: classes.dex */
public class MyAlphaSlider extends AlphaSlider {
    public MyAlphaSlider(Context context) {
        super(context);
    }

    public MyAlphaSlider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyAlphaSlider(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.flask.colorpicker.slider.AbsCustomSlider, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if ((action == 0 || action == 2) && zOnTouchEvent) {
            attemptClaimDrag();
        }
        return zOnTouchEvent;
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
}
