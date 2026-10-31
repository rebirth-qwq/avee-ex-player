package com.daaw.avee.comp.Legal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.ContextData;
import com.daaw.avee.MainActivity;
import com.daaw.avee.vendor.licenses.LicensesFragment;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class EulaDialog extends LicensesFragment {
    public static WeakEvent1<EulaDialog> onAcceptEula = new WeakEvent1<>();
    public static WeakEvent1<EulaDialog> onRejectEula = new WeakEvent1<>();
    public static WeakEvent1<EulaDialog> onDismissEula = new WeakEvent1<>();
    private static String fragmentTag = "EulaDialog";

    public static void createAndShow(ContextData contextData, boolean z) {
        EulaDialog eulaDialog = new EulaDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("keyShowCloseButton", false);
        bundle.putInt("licenseResId", R.raw.eula_english);
        bundle.putInt("titleResId", 0);
        bundle.putString("extraUrl", "");
        eulaDialog.setArguments(bundle);
        eulaDialog.setCancelable(false);
        UtilsUI.displayOneDialog(contextData, eulaDialog, fragmentTag);
    }

    @Override // com.daaw.avee.vendor.licenses.LicensesFragment
    protected void onDialogBuild(AlertDialog.Builder builder) {
        super.onDialogBuild(builder);
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            builder.setNegativeButton(mainActivity.getString(R.string.abc_refuse), new DialogInterface.OnClickListener() { // from class: com.daaw.avee.comp.Legal.EulaDialog.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    EulaDialog.onRejectEula.invoke(EulaDialog.this);
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton(mainActivity.getString(R.string.abc_agreed), new DialogInterface.OnClickListener() { // from class: com.daaw.avee.comp.Legal.EulaDialog.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    EulaDialog.onAcceptEula.invoke(EulaDialog.this);
                    dialogInterface.dismiss();
                }
            });
        }
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        onDismissEula.invoke(this);
    }
}
