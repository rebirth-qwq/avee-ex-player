package com.daaw.avee.comp.LibraryQueueUI.Dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEventR3;
import com.daaw.avee.ContextData;
import com.daaw.avee.comp.Common.IGeneralItemContainerIdentifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class ViewSettingsDialog extends PopupWindow {
    private static final String arg1 = "arg1";
    public static WeakEventR3<Integer, IGeneralItemContainerIdentifier, String, ViewOptions> onRequestOptionsState = new WeakEventR3<>();
    public static WeakEvent3<Integer, IGeneralItemContainerIdentifier, ViewOptions> onSubmitOptions = new WeakEvent3<>();
    private List<CheckBox> chkBoxList;
    private List<RadioButton> radioBtnList;

    public static class ViewOptions {
        public boolean showAlbumArt;
        public int viewMode;
    }

    public static ViewSettingsDialog createAndShowDialog(ContextData contextData, View view, String str) {
        return new ViewSettingsDialog(contextData, view, str);
    }

    public ViewSettingsDialog(ContextData contextData, View view, String str) {
        super(view.getContext(), (AttributeSet) null, 0, R.style.MyListPopupWindowDarkStyle);
        this.radioBtnList = null;
        this.chkBoxList = null;
        onCreateDialog(view.getContext(), view, str);
    }

    public void onCreateDialog(Context context, View view, String str) {
        final IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier = null;
        ViewOptions viewOptionsInvoke = onRequestOptionsState.invoke(-1, null, str, null);
        View viewInflate = View.inflate(view.getContext(), R.layout.popup_dialog_view, null);
        RadioGroup radioGroup = (RadioGroup) viewInflate.findViewById(R.id.radioGroupOptions);
        this.radioBtnList = new ArrayList();
        this.chkBoxList = new ArrayList();
        this.radioBtnList.add((RadioButton) viewInflate.findViewById(R.id.radioViewSmall));
        this.radioBtnList.add((RadioButton) viewInflate.findViewById(R.id.radioViewMedium));
        this.radioBtnList.add((RadioButton) viewInflate.findViewById(R.id.radioViewLarge));
        this.chkBoxList.add((CheckBox) viewInflate.findViewById(R.id.chkShowArt));
        if (viewOptionsInvoke.viewMode >= 0 && viewOptionsInvoke.viewMode < this.radioBtnList.size()) {
            radioGroup.check(this.radioBtnList.get(viewOptionsInvoke.viewMode).getId());
        }
        this.chkBoxList.get(0).setChecked(viewOptionsInvoke.showAlbumArt);
        Iterator<CheckBox> it = this.chkBoxList.iterator();
        while (it.hasNext()) {
            it.next().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.daaw.avee.comp.LibraryQueueUI.Dialog.ViewSettingsDialog.1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    ViewSettingsDialog.this.notifyOptionChange(-1, iGeneralItemContainerIdentifier);
                }
            });
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.daaw.avee.comp.LibraryQueueUI.Dialog.ViewSettingsDialog.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i) {
                ViewSettingsDialog.this.notifyOptionChange(-1, iGeneralItemContainerIdentifier);
            }
        });
        setContentView(viewInflate);
        setHeight(-2);
        setWidth(-2);
        setOutsideTouchable(true);
        setFocusable(true);
        showAsDropDown(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOptionChange(int i, IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier) {
        if (this.radioBtnList == null || this.chkBoxList == null) {
            return;
        }
        int i2 = 0;
        while (i2 < this.radioBtnList.size()) {
            if (this.radioBtnList.get(i2).isChecked()) {
                ViewOptions viewOptions = new ViewOptions();
                viewOptions.viewMode = i2;
                viewOptions.showAlbumArt = this.chkBoxList.get(0).isChecked();
                onSubmitOptions.invoke(Integer.valueOf(i), iGeneralItemContainerIdentifier, viewOptions);
            }
            i2++;
        }
        i2 = 0;
        ViewOptions viewOptions2 = new ViewOptions();
        viewOptions2.viewMode = i2;
        viewOptions2.showAlbumArt = this.chkBoxList.get(0).isChecked();
        onSubmitOptions.invoke(Integer.valueOf(i), iGeneralItemContainerIdentifier, viewOptions2);
    }
}
