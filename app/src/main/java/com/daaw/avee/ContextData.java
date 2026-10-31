package com.daaw.avee;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import junit.framework.Assert;

/* JADX INFO: loaded from: classes.dex */
public class ContextData {
    private Activity activity;

    public ContextData(Activity activity) {
        this.activity = activity;
    }

    public ContextData(View view) {
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                this.activity = (Activity) context;
                break;
            }
        }
        Assert.assertNotNull(this.activity);
    }

    public FragmentManager getFragmentManager() {
        return this.activity.getFragmentManager();
    }

    public LayoutInflater getLayoutInflater() {
        return this.activity.getLayoutInflater();
    }

    public Context getContext() {
        return this.activity;
    }

    public Activity getActivity() {
        return this.activity;
    }
}
