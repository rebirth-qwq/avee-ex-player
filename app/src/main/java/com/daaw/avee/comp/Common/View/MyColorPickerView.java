package com.daaw.avee.comp.Common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.flask.colorpicker.ColorPickerView;

/* JADX INFO: loaded from: classes.dex */
public class MyColorPickerView extends ColorPickerView {
    public MyColorPickerView(Context context) {
        super(context);
    }

    public MyColorPickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyColorPickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyColorPickerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // com.flask.colorpicker.ColorPickerView, android.view.View
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
