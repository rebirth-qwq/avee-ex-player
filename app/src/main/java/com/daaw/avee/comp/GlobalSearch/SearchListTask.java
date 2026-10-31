package com.daaw.avee.comp.GlobalSearch;

import android.content.Context;
import android.os.AsyncTask;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.FilterComparable;
import java.lang.ref.WeakReference;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SearchListTask<T1> extends AsyncTask<String, Object, Void> {
    private List<T1> originalList;
    private WeakReference<IResultReceiver<T1>> receiver;
    private FilterComparable<T1> searchFilter;
    private int fileCounter = 0;
    IResultReceiverInternal<T1> resultReceiver0 = new IResultReceiverInternal<T1>() { // from class: com.daaw.avee.comp.GlobalSearch.SearchListTask.1
        @Override // com.daaw.avee.comp.GlobalSearch.SearchListTask.IResultReceiverInternal
        public void onItemFileFound(T1 t1) {
            SearchListTask.access$008(SearchListTask.this);
            SearchListTask.this.publishProgress(t1);
        }

        @Override // com.daaw.avee.comp.GlobalSearch.SearchListTask.IResultReceiverInternal
        public boolean isCancelled() {
            return SearchListTask.this.isCancelled();
        }
    };

    public interface IResultReceiver<T1> {
        void onItemFileFound(AsyncTask asyncTask, T1 t1);

        void onSearchFinished(AsyncTask asyncTask, boolean z);

        void onSearchStarted(AsyncTask asyncTask);
    }

    private interface IResultReceiverInternal<T1> {
        boolean isCancelled();

        void onItemFileFound(T1 t1);
    }

    static /* synthetic */ int access$008(SearchListTask searchListTask) {
        int i = searchListTask.fileCounter;
        searchListTask.fileCounter = i + 1;
        return i;
    }

    public SearchListTask(Context context, List<T1> list, FilterComparable<T1> filterComparable, WeakReference<IResultReceiver<T1>> weakReference) {
        this.originalList = list;
        this.searchFilter = filterComparable;
        this.receiver = weakReference;
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        IResultReceiver<T1> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onSearchStarted(this);
        }
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Void r2) {
        IResultReceiver<T1> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onSearchFinished(this, true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.os.AsyncTask
    protected void onProgressUpdate(Object... objArr) {
        IResultReceiver<T1> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onItemFileFound(this, (T1) objArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onCancelled(Void r2) {
        IResultReceiver<T1> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onSearchFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code duplicated, block: B:13:0x0025 A[Catch: Exception -> 0x005f, TryCatch #0 {Exception -> 0x005f, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0032, B:17:0x0038, B:18:0x003e, B:20:0x0044, B:23:0x0051, B:25:0x0059), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:16:0x0032 A[Catch: Exception -> 0x005f, LOOP:0: B:11:0x001f->B:16:0x0032, LOOP_END, TryCatch #0 {Exception -> 0x005f, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0032, B:17:0x0038, B:18:0x003e, B:20:0x0044, B:23:0x0051, B:25:0x0059), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0044 A[Catch: Exception -> 0x005f, TryCatch #0 {Exception -> 0x005f, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0032, B:17:0x0038, B:18:0x003e, B:20:0x0044, B:23:0x0051, B:25:0x0059), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:23:0x0051 A[Catch: Exception -> 0x005f, TryCatch #0 {Exception -> 0x005f, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0032, B:17:0x0038, B:18:0x003e, B:20:0x0044, B:23:0x0051, B:25:0x0059), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:33:0x0031 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:35:0x0050 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:36:0x0059 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:38:0x003e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:9:0x0018  */
    @Override // android.os.AsyncTask
    public Void doInBackground(String... strArr) {
        String strPreProcessQuery;
        String str = strArr[0];
        if (this.searchFilter == null) {
            return null;
        }
        if (str != null) {
            try {
                if (str.isEmpty()) {
                    strPreProcessQuery = null;
                } else {
                    strPreProcessQuery = this.searchFilter.preProcessQuery(str);
                }
                for (T1 t1 : this.originalList) {
                    if (this.resultReceiver0.isCancelled()) {
                        break;
                    }
                    this.searchFilter.preProcessItem(t1);
                }
                for (T1 t2 : this.originalList) {
                    if (this.resultReceiver0.isCancelled()) {
                        break;
                    }
                    if (this.searchFilter.compare(strPreProcessQuery, t2)) {
                        this.resultReceiver0.onItemFileFound(t2);
                    }
                }
            } catch (Exception e) {
                tlog.w("doInBackground Exception: " + e.getMessage());
            }
        } else {
            strPreProcessQuery = null;
            for (T1 t3 : this.originalList) {
                if (this.resultReceiver0.isCancelled()) {
                    break;
                }
                this.searchFilter.preProcessItem(t3);
            }
            for (T1 t4 : this.originalList) {
                if (this.resultReceiver0.isCancelled()) {
                    break;
                }
                if (this.searchFilter.compare(strPreProcessQuery, t4)) {
                    this.resultReceiver0.onItemFileFound(t4);
                }
            }
        }
        return null;
    }
}
