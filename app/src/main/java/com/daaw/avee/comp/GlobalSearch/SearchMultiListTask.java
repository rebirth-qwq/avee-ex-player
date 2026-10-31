package com.daaw.avee.comp.GlobalSearch;

import android.content.Context;
import android.os.AsyncTask;
import com.daaw.avee.Common.MultiList;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.tlog;
import com.daaw.avee.comp.LibraryQueueUI.Containers.Base.FilterComparable;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class SearchMultiListTask<T1, T2> extends AsyncTask<String, Object, Void> {
    private MultiList<T1, T2> originalList;
    private WeakReference<IResultReceiver<T1, T2>> receiver;
    private FilterComparable<T1> searchFilter;
    private int fileCounter = 0;
    IResultReceiverInternal<T1, T2> resultReceiver0 = new IResultReceiverInternal<T1, T2>() { // from class: com.daaw.avee.comp.GlobalSearch.SearchMultiListTask.1
        @Override // com.daaw.avee.comp.GlobalSearch.SearchMultiListTask.IResultReceiverInternal
        public void onItemDirFound(Tuple2<T1, T2> tuple2) {
        }

        @Override // com.daaw.avee.comp.GlobalSearch.SearchMultiListTask.IResultReceiverInternal
        public void onItemFileFound(Tuple2<T1, T2> tuple2) {
            SearchMultiListTask.access$008(SearchMultiListTask.this);
            SearchMultiListTask.this.publishProgress(tuple2);
        }

        @Override // com.daaw.avee.comp.GlobalSearch.SearchMultiListTask.IResultReceiverInternal
        public boolean isCancelled() {
            return SearchMultiListTask.this.isCancelled();
        }
    };

    public interface IResultReceiver<T1, T2> {
        void onItemDirFound(AsyncTask asyncTask, Tuple2<T1, T2> tuple2);

        void onItemFileFound(AsyncTask asyncTask, Tuple2<T1, T2> tuple2);

        void onSearchFinished(AsyncTask asyncTask, boolean z);

        void onSearchStarted(AsyncTask asyncTask);
    }

    private interface IResultReceiverInternal<T1, T2> {
        boolean isCancelled();

        void onItemDirFound(Tuple2<T1, T2> tuple2);

        void onItemFileFound(Tuple2<T1, T2> tuple2);
    }

    static /* synthetic */ int access$008(SearchMultiListTask searchMultiListTask) {
        int i = searchMultiListTask.fileCounter;
        searchMultiListTask.fileCounter = i + 1;
        return i;
    }

    public SearchMultiListTask(Context context, MultiList<T1, T2> multiList, FilterComparable<T1> filterComparable, WeakReference<IResultReceiver<T1, T2>> weakReference) {
        this.originalList = multiList;
        this.searchFilter = filterComparable;
        this.receiver = weakReference;
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        IResultReceiver<T1, T2> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onSearchStarted(this);
        }
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Void r2) {
        IResultReceiver<T1, T2> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onSearchFinished(this, true);
        }
    }

    @Override // android.os.AsyncTask
    protected void onProgressUpdate(Object... objArr) {
        IResultReceiver<T1, T2> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onItemFileFound(this, (Tuple2) objArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onCancelled(Void r2) {
        IResultReceiver<T1, T2> iResultReceiver = this.receiver.get();
        if (iResultReceiver != null) {
            iResultReceiver.onSearchFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code duplicated, block: B:13:0x0025 A[Catch: Exception -> 0x0067, TryCatch #0 {Exception -> 0x0067, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0034, B:17:0x003c, B:18:0x0042, B:20:0x0048, B:23:0x0057, B:25:0x0061), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:16:0x0034 A[Catch: Exception -> 0x0067, LOOP:0: B:11:0x001f->B:16:0x0034, LOOP_END, TryCatch #0 {Exception -> 0x0067, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0034, B:17:0x003c, B:18:0x0042, B:20:0x0048, B:23:0x0057, B:25:0x0061), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0048 A[Catch: Exception -> 0x0067, TryCatch #0 {Exception -> 0x0067, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0034, B:17:0x003c, B:18:0x0042, B:20:0x0048, B:23:0x0057, B:25:0x0061), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:23:0x0057 A[Catch: Exception -> 0x0067, TryCatch #0 {Exception -> 0x0067, blocks: (B:6:0x000b, B:8:0x0011, B:10:0x0019, B:11:0x001f, B:13:0x0025, B:16:0x0034, B:17:0x003c, B:18:0x0042, B:20:0x0048, B:23:0x0057, B:25:0x0061), top: B:30:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:33:0x0033 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:35:0x0056 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:36:0x0061 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:38:0x0042 A[SYNTHETIC] */
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
                for (Tuple2<T1, T2> tuple2 : this.originalList) {
                    if (this.resultReceiver0.isCancelled()) {
                        break;
                    }
                    this.searchFilter.preProcessItem(tuple2.obj1);
                }
                for (Tuple2<T1, T2> tuple3 : this.originalList) {
                    if (this.resultReceiver0.isCancelled()) {
                        break;
                    }
                    if (this.searchFilter.compare(strPreProcessQuery, tuple3.obj1)) {
                        this.resultReceiver0.onItemFileFound(tuple3);
                    }
                }
            } catch (Exception e) {
                tlog.w("doInBackground Exception: " + e.getMessage());
            }
        } else {
            strPreProcessQuery = null;
            for (Tuple2<T1, T2> tuple4 : this.originalList) {
                if (this.resultReceiver0.isCancelled()) {
                    break;
                }
                this.searchFilter.preProcessItem(tuple4.obj1);
            }
            for (Tuple2<T1, T2> tuple5 : this.originalList) {
                if (this.resultReceiver0.isCancelled()) {
                    break;
                }
                if (this.searchFilter.compare(strPreProcessQuery, tuple5.obj1)) {
                    this.resultReceiver0.onItemFileFound(tuple5);
                }
            }
        }
        return null;
    }
}
