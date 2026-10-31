package com.daaw.avee.Common;

import android.os.AsyncTask;

/* JADX INFO: loaded from: classes.dex */
public interface IVAsyncTask<Result> {
    void Run(VAsyncTask.ResultListener<Result> resultListener);

    boolean cancel(boolean z);

    AsyncTask.Status getStatus();
}
