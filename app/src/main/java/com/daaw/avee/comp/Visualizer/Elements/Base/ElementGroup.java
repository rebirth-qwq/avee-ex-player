package com.daaw.avee.comp.Visualizer.Elements.Base;

import com.daaw.avee.comp.Common.VisualizerThemeInfo;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.Element;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.ICompositionDependencies;
import com.daaw.avee.comp.Visualizer.ICustomization;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;

/* JADX INFO: loaded from: classes.dex */
public abstract class ElementGroup<T extends Element, ChildC extends CustomPropertiesList, ThisC extends ICustomization<ChildC>> extends Element {
    protected List<T> childList;

    public abstract T CreateFactoryElement(int i, String str, String str2);

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
    }

    public ElementGroup() {
        super(2, 1.0f, 1.0f);
        this.childList = new ArrayList();
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void dispose() {
        Iterator<T> it = this.childList.iterator();
        while (it.hasNext()) {
            it.next().dispose();
        }
        super.dispose();
    }

    public void addChild(T t, int i) {
        Assert.assertEquals("addChild", (Object) null, t.parent);
        this.childList.add(i, t);
        t.parent = this;
    }

    public void addChildAtBeginning(T t) {
        Assert.assertEquals("addChildAtBeginning", (Object) null, t.parent);
        this.childList.add(0, t);
        t.parent = this;
    }

    public void addChildAtEnd(T t) {
        Assert.assertEquals("addChildAtEnd", (Object) null, t.parent);
        this.childList.add(t);
        t.parent = this;
    }

    public void removeChild(T t) {
        Assert.assertEquals("removeChild", this, t.parent);
        this.childList.remove(t);
        t.parent = null;
    }

    public T removeChildAt(int i) {
        T tRemove = this.childList.remove(i);
        tRemove.parent = null;
        return tRemove;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void markNeedReCreateGLResources() {
        super.markNeedReCreateGLResources();
        Iterator<T> it = this.childList.iterator();
        while (it.hasNext()) {
            it.next().markNeedReCreateGLResources();
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onEarlyUpdate(IRenderState iRenderState, FrameBuffer frameBuffer, ICompositionDependencies iCompositionDependencies) {
        super.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
        earlyUpdateChilds(iRenderState, frameBuffer, iCompositionDependencies);
    }

    protected void earlyUpdateChilds(IRenderState iRenderState, FrameBuffer frameBuffer, ICompositionDependencies iCompositionDependencies) {
        Iterator<T> it = this.childList.iterator();
        while (it.hasNext()) {
            it.next().onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
        }
    }

    protected void renderOffscreenContentChilds(RenderState renderState) {
        for (T t : this.childList) {
            if (t.shouldRender()) {
                t.onRenderOffscreenContent(renderState);
            }
        }
    }

    protected void renderChilds(RenderState renderState, FrameBuffer frameBuffer) {
        for (T t : this.childList) {
            if (t.shouldRender()) {
                t.onRender(renderState, frameBuffer);
                t.onRenderHighlight(renderState);
            } else {
                t.onRenderInvisible(renderState, frameBuffer);
            }
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRenderPreview(IRenderState iRenderState, FrameBuffer frameBuffer) {
        super.onRenderPreview(iRenderState, frameBuffer);
        for (T t : this.childList) {
            if (t.shouldRender()) {
                t.onRenderPreview(iRenderState, frameBuffer);
            }
        }
    }

    protected boolean getCustomization(ThisC thisc, IDependencyResourceCounter iDependencyResourceCounter) {
        if (thisc == null) {
            return false;
        }
        int size = this.childList.size();
        for (int i = 0; i < size; i++) {
            if (!this.childList.get(i).getCustomization((CustomPropertiesList) thisc.createDataEntry(), 0, iDependencyResourceCounter)) {
                return false;
            }
        }
        return true;
    }

    protected boolean setCustomization(ThisC thisc) {
        // 逐条对齐原 dex（smali: ElementGroup.setCustomization(ICustomization)Z）。
        // 原版两处工厂返回 null 的语义【不同】，jadx 曾把它们糊成同一条路径：
        //   · 新增分支（size >= childList.size()）工厂为 null → if-eqz v4, :cond_3
        //     即跳过 setCustomization 且 size 不自增，只 i++（原 jadx 版无此跳转 → IndexOutOfBounds）。
        //   · 复用分支（size < childList.size()）不可复用 → dispose 后工厂为 null 时
        //     if-eqz v4, :cond_2，而 :cond_2 紧接 :goto_1，即仍然执行 setCustomization 并 size++。
        //     （上一版误按 :cond_3 处理，遗留已 dispose 的子元素 → 后续条目整体错位，
        //       表现为“切了模板画面仍停在旧内容 / 嵌套组合渲染不对”。）
        if (thisc == null) {
            return false;
        }
        int entryCount = thisc.getEntryCount();
        int size = 0;
        for (int i = 0; i < entryCount; i++) {
            CustomPropertiesList customPropertiesList = (CustomPropertiesList) thisc.getEntry(i);
            String typeName = customPropertiesList.getTypeName();
            String versionId = customPropertiesList.getVersionId();
            if (size >= this.childList.size()) {
                Element elementCreateFactoryElement = CreateFactoryElement(i, typeName, versionId);
                if (elementCreateFactoryElement == null) {
                    continue;                                   // 原版 if-eqz v4, :cond_3
                }
                addChildAtEnd((T) elementCreateFactoryElement);
                size = this.childList.size() - 1;               // 原版 sub-int/2addr v2, v3
            } else {
                T existing = this.childList.get(size);
                if (!VisualizerThemeInfo.CompareElementReUsability(existing, typeName, versionId)) {
                    this.childList.get(size).dispose();          // 原版 get(size)->dispose()
                    Element elementCreateFactoryElement2 = CreateFactoryElement(i, typeName, versionId);
                    if (elementCreateFactoryElement2 != null) {
                        this.childList.set(size, (T) elementCreateFactoryElement2); // 原版 List.set
                    }
                    // 工厂为 null 时不跳过：原版落到 :cond_2 → :goto_1
                }
            }
            this.childList.get(size).setCustomization(customPropertiesList, i); // 原版 :goto_1
            size++;                                             // 原版 add-int/2addr v2, v3
        }
        while (size < this.childList.size()) {
            List<T> list = this.childList;
            list.remove(list.size() - 1).dispose();
        }
        return true;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public Element findElementById(int i) {
        Element elementFindElementById = super.findElementById(i);
        if (elementFindElementById != null) {
            return elementFindElementById;
        }
        Iterator<T> it = this.childList.iterator();
        while (it.hasNext()) {
            Element elementFindElementById2 = it.next().findElementById(i);
            if (elementFindElementById2 != null) {
                return elementFindElementById2;
            }
        }
        return null;
    }
}
