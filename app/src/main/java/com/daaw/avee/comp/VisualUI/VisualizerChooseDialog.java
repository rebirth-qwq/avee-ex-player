package com.daaw.avee.comp.VisualUI;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.daaw.avee.Common.Events.WeakEvent1;
import com.daaw.avee.Common.Events.WeakEvent2;
import com.daaw.avee.Common.Events.WeakEvent3;
import com.daaw.avee.Common.Events.WeakEventR1;
import com.daaw.avee.Common.Tuple2;
import com.daaw.avee.Common.UtilsUI;
import com.daaw.avee.Common.VAsyncTask;
import com.daaw.avee.ContextData;
import com.daaw.avee.comp.Common.VisualizerThemeInfo;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class VisualizerChooseDialog extends DialogFragment {
    private ViewGroup btnRemove;
    private ImageView btnRemoveIcon;
    private TextView btnRemoveTxt;
    GridLayout gridLayout;
    GridLayout gridLayout1;
    private EditText textCustomVizName;
    public static WeakEvent3<VisualizerChooseDialog, Integer, Intent> onActivityResult = new WeakEvent3<>();
    public static WeakEventR1<List<VisualizerThemeInfo>, Tuple2<Integer, List<Integer>>> onRequestSkinThemePresetList = new WeakEventR1<>();
    public static WeakEvent1<VisualizerThemeInfo> onSkinThemePresetSelected = new WeakEvent1<>();
    public static WeakEvent1<VisualizerThemeInfo> onRemoveVisTemplateAction = new WeakEvent1<>();
    public static WeakEvent1<VisualizerThemeInfo> onRemoveVisTemplateAction_Long = new WeakEvent1<>();
    public static WeakEvent2<VisualizerThemeInfo, List<Integer>> onDuplicateVisTemplateAction = new WeakEvent2<>();
    public static WeakEvent2<VisualizerChooseDialog, VisualizerThemeInfo> onSaveToFileVisTemplateAction = new WeakEvent2<>();
    public static WeakEvent2<VisualizerChooseDialog, List<Integer>> onLoadFromFileVisTemplateAction = new WeakEvent2<>();
    public static WeakEvent2<VisualizerChooseDialog, List<Integer>> onNewVisTemplateAction = new WeakEvent2<>();
    public static WeakEventR1<List<VisualizerThemeInfo>, VAsyncTask<List<Bitmap>>> onRequestVisTemplatePreviewImage = new WeakEventR1<>();
    private static WeakReference<VisualizerChooseDialog> instanceWeak = new WeakReference<>(null);
    private VisualizerThemeInfo currentSelectedSkinTheme = null;
    private List<Integer> templateIndexes = null;
    final View[] selectedThumbButton = {null};

    public EditText getTextCustomVizName() {
        return this.textCustomVizName;
    }

    public VisualizerChooseDialog() {
        setStyle(2, 0);
        instanceWeak = new WeakReference<>(this);
    }

    public static VisualizerChooseDialog createAndShowDialog(Activity activity) {
        VisualizerChooseDialog visualizerChooseDialogNewInstance = newInstance();
        UtilsUI.showDialogSafe(visualizerChooseDialogNewInstance, "VisualizerChooseDialog", new ContextData(activity));
        return visualizerChooseDialogNewInstance;
    }

    private static VisualizerChooseDialog newInstance() {
        VisualizerChooseDialog visualizerChooseDialog = new VisualizerChooseDialog();
        visualizerChooseDialog.setArguments(new Bundle());
        return visualizerChooseDialog;
    }

    public static VisualizerChooseDialog getInstance() {
        return instanceWeak.get();
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(2, 0);
    }

    public String getEmojiByUnicode(int i) {
        return new String(Character.toChars(i));
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.dialog_choose_vizstyle, viewGroup, false);
        this.gridLayout = (GridLayout) viewInflate.findViewById(R.id.gridLayoutElements);
        ((ViewGroup) viewInflate.findViewById(R.id.btnDuplicate)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisualizerChooseDialog.onDuplicateVisTemplateAction.invoke(VisualizerChooseDialog.this.currentSelectedSkinTheme, VisualizerChooseDialog.this.templateIndexes);
            }
        });
        ViewGroup viewGroup2 = (ViewGroup) viewInflate.findViewById(R.id.btnRemove);
        this.btnRemove = viewGroup2;
        viewGroup2.setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisualizerChooseDialog.onRemoveVisTemplateAction.invoke(VisualizerChooseDialog.this.currentSelectedSkinTheme);
            }
        });
        this.btnRemove.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                VisualizerChooseDialog.onRemoveVisTemplateAction_Long.invoke(VisualizerChooseDialog.this.currentSelectedSkinTheme);
                return true;
            }
        });
        this.btnRemoveTxt = (TextView) viewInflate.findViewById(R.id.btnRemoveTxt);
        this.btnRemoveIcon = (ImageView) viewInflate.findViewById(R.id.btnRemoveIcon);
        this.textCustomVizName = (EditText) viewInflate.findViewById(R.id.textCustomVizName);
        ((ViewGroup) viewInflate.findViewById(R.id.btnSaveToFile)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WeakEvent2<VisualizerChooseDialog, VisualizerThemeInfo> weakEvent2 = VisualizerChooseDialog.onSaveToFileVisTemplateAction;
                VisualizerChooseDialog visualizerChooseDialog = VisualizerChooseDialog.this;
                weakEvent2.invoke(visualizerChooseDialog, visualizerChooseDialog.currentSelectedSkinTheme);
            }
        });
        ((ViewGroup) viewInflate.findViewById(R.id.btnLoadFromFile)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WeakEvent2<VisualizerChooseDialog, List<Integer>> weakEvent2 = VisualizerChooseDialog.onLoadFromFileVisTemplateAction;
                VisualizerChooseDialog visualizerChooseDialog = VisualizerChooseDialog.this;
                weakEvent2.invoke(visualizerChooseDialog, visualizerChooseDialog.templateIndexes);
            }
        });
        ((ViewGroup) viewInflate.findViewById(R.id.btnAdd)).setOnClickListener(new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VisualizerChooseDialog.onNewVisTemplateAction.invoke(VisualizerChooseDialog.this, null);
            }
        });
        getDialog().setCanceledOnTouchOutside(true);
        requestUpdateVisualizerTemplates();
        return viewInflate;
    }

    @Override // android.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            onActivityResult.invoke(this, Integer.valueOf(i), intent);
        }
    }

    public void requestUpdateVisualizerTemplates() {
        ArrayList arrayList = new ArrayList();
        Tuple2<Integer, List<Integer>> tuple2Invoke = onRequestSkinThemePresetList.invoke(arrayList, new Tuple2<>(-1, new ArrayList()));
        updateVisualizerTemplates(tuple2Invoke.obj1.intValue(), tuple2Invoke.obj2, arrayList);
    }

    public void updateVisualizerTemplates(int i, List<Integer> list, final List<VisualizerThemeInfo> list2) {
        GridLayout gridLayout;
        Activity activity = getActivity();
        if (activity == null || (gridLayout = this.gridLayout) == null) {
            return;
        }
        gridLayout.removeAllViews();
        this.templateIndexes = list;
        if (list == null) {
            this.templateIndexes = new ArrayList();
        }
        final ArrayList arrayList = new ArrayList(list2.size());
        if (list2.size() > 0) {
            View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.6
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int iIntValue = ((Integer) view.getTag()).intValue();
                    if (iIntValue < list2.size()) {
                        VisualizerThemeInfo visualizerThemeInfo = (VisualizerThemeInfo) list2.get(iIntValue);
                        VisualizerChooseDialog visualizerChooseDialog = VisualizerChooseDialog.this;
                        visualizerChooseDialog.setSelectedTemplate(visualizerChooseDialog.selectedThumbButton, view, visualizerThemeInfo);
                        VisualizerChooseDialog.onSkinThemePresetSelected.invoke(visualizerThemeInfo);
                    }
                }
            };
            for (int i2 = 0; i2 < list2.size(); i2++) {
                VisualizerThemeInfo visualizerThemeInfo = list2.get(i2);
                View viewInflate = View.inflate(activity, R.layout.dialog_choose_vizstyle_element, null);
                ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.btnElement);
                imageButton.setOnClickListener(onClickListener);
                imageButton.setTag(Integer.valueOf(i2));
                if (i == visualizerThemeInfo.id) {
                    setSelectedTemplate(this.selectedThumbButton, imageButton, visualizerThemeInfo);
                }
                this.gridLayout.addView(viewInflate);
                arrayList.add(imageButton);
            }
        }
        onRequestVisTemplatePreviewImage.invoke(list2, null).Run(new VAsyncTask.ResultListener<List<Bitmap>>() { // from class: com.daaw.avee.comp.VisualUI.VisualizerChooseDialog.7
            @Override // com.daaw.avee.Common.VAsyncTask.ResultListener
            public void onResult(List<Bitmap> list3, boolean z) {
                if (list3 == null) return; // 预览生成失败/取消时不回填（原实现隐含任务必成功）
                int iMin = Math.min(arrayList.size(), list3.size());
                for (int i3 = 0; i3 < iMin; i3++) {
                    ((ImageButton) arrayList.get(i3)).setImageBitmap(list3.get(i3));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectedTemplate(View[] viewArr, View view, VisualizerThemeInfo visualizerThemeInfo) {
        this.currentSelectedSkinTheme = visualizerThemeInfo;
        UtilsUI.setSelectedView(viewArr, view);
        if (visualizerThemeInfo.isTemplate) {
            this.btnRemove.setEnabled(false);
            this.btnRemoveTxt.setAlpha(0.5f);
            this.btnRemoveIcon.setAlpha(0.5f);
        } else {
            this.btnRemove.setEnabled(true);
            this.btnRemoveTxt.setAlpha(1.0f);
            this.btnRemoveIcon.setAlpha(1.0f);
        }
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }
}
