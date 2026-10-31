package com.daaw.avee.Common;

import android.os.AsyncTask;
import com.daaw.avee.Common.Func.Func1;

/* JADX INFO: loaded from: classes.dex */
public class VAsyncTaskResultConvert<FromResult, Result> implements IVAsyncTask<Result> {
    private Func1<FromResult, Result> converter;
    private VAsyncTask<FromResult> task;

    public VAsyncTaskResultConvert(VAsyncTask<FromResult> vAsyncTask, Func1<FromResult, Result> func1) {
        this.task = vAsyncTask;
        this.converter = func1;
    }

    @Override // com.daaw.avee.Common.IVAsyncTask
    public void Run(final VAsyncTask.ResultListener<Result> resultListener) {
        this.task.Run(new VAsyncTask.ResultListener<FromResult>() { // from class: com.daaw.avee.Common.VAsyncTaskResultConvert.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // com.daaw.avee.Common.VAsyncTask.ResultListener
            public void onResult(FromResult fromresult, boolean z) {
                resultListener.onResult(VAsyncTaskResultConvert.this.converter.onInvoke(fromresult), z);
            }
        });
    }

    @Override // com.daaw.avee.Common.IVAsyncTask
    public boolean cancel(boolean z) {
        return this.task.cancel(z);
    }

    @Override // com.daaw.avee.Common.IVAsyncTask
    public AsyncTask.Status getStatus() {
        return this.task.getStatus();
    }
}
