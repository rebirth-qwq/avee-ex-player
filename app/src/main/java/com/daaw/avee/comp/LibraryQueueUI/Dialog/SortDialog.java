package com.daaw.avee.comp.LibraryQueueUI.Dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import com.daaw.avee.Common.Events.WeakEvent4;
import com.daaw.avee.Common.Events.WeakEventR3;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.ContextData;
import com.daaw.avee.Design.SortDesign;
import com.daaw.avee.comp.Common.IGeneralItemContainerIdentifier;
import java.util.ArrayList;
import java.util.List;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class SortDialog extends PopupWindow {
    private static final String arg1 = "arg1";
    public static WeakEventR3<Integer, IGeneralItemContainerIdentifier, String, SortDesign.SortOptions> onRequestSortOptions = new WeakEventR3<>();
    public static WeakEvent4<Integer, IGeneralItemContainerIdentifier, Integer, Integer> onSubmitSortOptions = new WeakEvent4<>();
    private List<CheckBox> chkBoxList;
    private List<RadioButton> radioBtnList;

    public static SortDialog createAndShowDialog(ContextData contextData, View view, String str) {
        return new SortDialog(contextData, view, str);
    }

    public SortDialog(ContextData contextData, View view, String str) {
        super(view.getContext(), (AttributeSet) null, 0, R.style.MyListPopupWindowDarkStyle);
        this.radioBtnList = null;
        this.chkBoxList = null;
        onCreateDialog(view.getContext(), view, str);
    }

    public void onCreateDialog(Context context, View view, String str) {
        final IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier = null;
        SortDesign.SortOptions sortOptionsInvoke = onRequestSortOptions.invoke(-1, null, str, null);
        View viewInflate = View.inflate(view.getContext(), R.layout.dialog_sort, null);
        LinearLayout linearLayout = (LinearLayout) viewInflate.findViewById(R.id.layoutCheckOptions);
        RadioGroup radioGroup = (RadioGroup) viewInflate.findViewById(R.id.radioGroupOptions);
        Space space = (Space) viewInflate.findViewById(R.id.space);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(9, -1);
        this.radioBtnList = new ArrayList(sortOptionsInvoke == null ? 0 : sortOptionsInvoke.radioOptions.size());
        this.chkBoxList = new ArrayList(sortOptionsInvoke == null ? 0 : sortOptionsInvoke.checkOptions.size());
        if (sortOptionsInvoke != null) {
            for (Tuple2<Integer, String> tuple2 : sortOptionsInvoke.radioOptions) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setTag(tuple2.obj1);
                radioButton.setText(tuple2.obj2);
                radioGroup.addView(radioButton, layoutParams);
                if (tuple2.obj1.intValue() == sortOptionsInvoke.selectedRadioOption) {
                    radioGroup.check(radioButton.getId());
                }
                this.radioBtnList.add(radioButton);
            }
            if (sortOptionsInvoke.checkOptions.size() > 0) {
                space.setVisibility(0);
            } else {
                space.setVisibility(8);
            }
            for (Tuple2<Integer, String> tuple3 : sortOptionsInvoke.checkOptions) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setTag(tuple3.obj1);
                checkBox.setText(tuple3.obj2);
                if ((tuple3.obj1.intValue() & sortOptionsInvoke.maskCheckOptions) != 0) {
                    checkBox.setChecked(true);
                }
                linearLayout.addView(checkBox, layoutParams);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.daaw.avee.comp.LibraryQueueUI.Dialog.SortDialog.1
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        SortDialog.this.notifyOptionChange(-1, iGeneralItemContainerIdentifier);
                    }
                });
                this.chkBoxList.add(checkBox);
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.daaw.avee.comp.LibraryQueueUI.Dialog.SortDialog.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i) {
                SortDialog.this.notifyOptionChange(-1, iGeneralItemContainerIdentifier);
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
        List<RadioButton> list = this.radioBtnList;
        if (list == null || this.chkBoxList == null) {
            return;
        }
        int iIntValue = 0;
        int iIntValue2 = 0;
        for (RadioButton radioButton : list) {
            if (radioButton.isChecked()) {
                iIntValue2 = ((Integer) radioButton.getTag()).intValue();
            }
        }
        for (CheckBox checkBox : this.chkBoxList) {
            if (checkBox.isChecked()) {
                iIntValue |= ((Integer) checkBox.getTag()).intValue();
            }
        }
        onSubmitSortOptions.invoke(Integer.valueOf(i), iGeneralItemContainerIdentifier, Integer.valueOf(iIntValue2), Integer.valueOf(iIntValue));
    }
}
