package com.daaw.avee.comp.ContextualActionBar;

import com.daaw.avee.comp.Common.IGeneralItemContainerIdentifier;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/* JADX INFO: loaded from: classes.dex */
public class ItemSelection<T> {
    private Object containerIdentifier;
    private List<T> items = new ArrayList();

    public ItemSelection(Object obj) {
        Assert.assertNotNull(obj);
        this.containerIdentifier = obj;
    }

    public Object getContainerIdentifier() {
        return this.containerIdentifier;
    }

    public boolean containsItem(T t) {
        return this.items.contains(t);
    }

    public void addSelection(One<T> one) {
        if (this.containerIdentifier.equals(((One) one).containerIdentifier) && !this.items.contains(((One) one).item)) {
            this.items.add((T) ((One) one).item);
        }
    }

    public void subtractSelection(One<T> one) {
        if (this.containerIdentifier.equals(((One) one).containerIdentifier)) {
            this.items.remove(((One) one).item);
        }
    }

    public static class One<T> {
        private IGeneralItemContainerIdentifier containerIdentifier;
        private T item;

        public One(IGeneralItemContainerIdentifier iGeneralItemContainerIdentifier, T t) {
            Assert.assertNotNull(iGeneralItemContainerIdentifier);
            Assert.assertNotNull(t);
            this.containerIdentifier = iGeneralItemContainerIdentifier;
            this.item = t;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof One)) {
                return false;
            }
            One one = (One) obj;
            return this.containerIdentifier.equals(one.containerIdentifier) && this.item.equals(one.item);
        }

        public int hashCode() {
            return this.containerIdentifier.hashCode() + this.item.hashCode();
        }

        public IGeneralItemContainerIdentifier getContainerIdentifier() {
            return this.containerIdentifier;
        }

        public boolean containsItem(T t) {
            return this.item.equals(t);
        }

        public T getItemIdentifier() {
            return this.item;
        }
    }
}
