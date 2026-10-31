package com.daaw.avee.vendor.dragsort;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes.dex */
public class DragSortRecycler extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {
    OnDragStateChangedListener dragStateChangedListener;
    private int fingerAnchorY;
    private int fingerOffsetInViewY;
    private int fingerY;
    private BitmapDrawable floatingItem;
    private Rect floatingItemBounds;
    private Rect floatingItemStatingBounds;
    private boolean isDragging;
    OnItemMovedListener moveInterface;
    final String TAG = "DragSortRecycler";
    final boolean DEBUG = false;
    private int dragHandleWidth = 0;
    private int selectedDragItemPos = -1;
    private float autoScrollWindow = 0.1f;
    private float autoScrollSpeed = 0.5f;
    private float floatingItemAlpha = 0.5f;
    private int floatingItemBgColor = 0;
    private int viewHandleId = -1;
    View draggingItemView = null;
    Paint bgColor = new Paint();
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() { // from class: com.daaw.avee.vendor.dragsort.DragSortRecycler.1
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            DragSortRecycler.this.debugLog("Scrolled: " + i + " " + i2);
            DragSortRecycler dragSortRecycler = DragSortRecycler.this;
            dragSortRecycler.fingerAnchorY = dragSortRecycler.fingerAnchorY - i2;
        }
    };

    public interface OnDragStateChangedListener {
        void onDragStart();

        void onDragStop();
    }

    public interface OnItemMovedListener {
        void onItemMoved(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debugLog(String str) {
    }

    protected boolean canDragOver(int i) {
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public RecyclerView.OnScrollListener getScrollListener() {
        return this.scrollListener;
    }

    public void setOnItemMovedListener(OnItemMovedListener onItemMovedListener) {
        this.moveInterface = onItemMovedListener;
    }

    public void setViewHandleId(int i) {
        this.viewHandleId = i;
    }

    public void setLeftDragArea(int i) {
        this.dragHandleWidth = i;
    }

    public void setFloatingAlpha(float f) {
        this.floatingItemAlpha = f;
    }

    public void setFloatingBgColor(int i) {
        this.floatingItemBgColor = i;
    }

    public void setAutoScrollWindow(float f) {
        this.autoScrollWindow = f;
    }

    public void setAutoScrollSpeed(float f) {
        this.autoScrollSpeed = f;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        debugLog("getItemOffsets");
        debugLog("View top = " + view.getTop());
        if (this.selectedDragItemPos != -1) {
            int childPosition = recyclerView.getChildPosition(view);
            debugLog("itemPos =" + childPosition);
            if (canDragOver(childPosition)) {
                if (childPosition == this.selectedDragItemPos) {
                    view.setVisibility(4);
                    return;
                }
                view.setVisibility(0);
                float fHeight = this.floatingItemBounds.top + (this.floatingItemBounds.height() / 2);
                if (childPosition > this.selectedDragItemPos && view.getTop() < fHeight) {
                    float top = (fHeight - view.getTop()) / view.getHeight();
                    if (top > 1.0f) {
                        top = 1.0f;
                    }
                    rect.top = -((int) (this.floatingItemBounds.height() * top));
                    rect.bottom = (int) (this.floatingItemBounds.height() * top);
                }
                if (childPosition >= this.selectedDragItemPos || view.getBottom() <= fHeight) {
                    return;
                }
                float bottom = (view.getBottom() - fHeight) / view.getHeight();
                float f = bottom <= 1.0f ? bottom : 1.0f;
                rect.top = (int) (this.floatingItemBounds.height() * f);
                rect.bottom = -((int) (this.floatingItemBounds.height() * f));
                return;
            }
            return;
        }
        rect.top = 0;
        rect.bottom = 0;
        view.setVisibility(0);
    }

    private int getNewPostion(RecyclerView recyclerView) {
        int childPosition;
        int childCount = recyclerView.getLayoutManager().getChildCount();
        float fHeight = this.floatingItemBounds.top + (this.floatingItemBounds.height() / 2);
        int i = 0;
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = recyclerView.getLayoutManager().getChildAt(i3);
            if (childAt.getVisibility() == 0 && (childPosition = recyclerView.getChildPosition(childAt)) != this.selectedDragItemPos) {
                float top = childAt.getTop() + (childAt.getHeight() / 2);
                if (fHeight > top) {
                    if (childPosition > i) {
                        i = childPosition;
                    }
                } else if (fHeight <= top && childPosition < i2) {
                    i2 = childPosition;
                }
            }
        }
        debugLog("above = " + i + " below = " + i2);
        if (i2 == Integer.MAX_VALUE) {
            return i < this.selectedDragItemPos ? i + 1 : i;
        }
        if (i2 < this.selectedDragItemPos) {
            i2++;
        }
        return i2 - 1;
    }

    public void StartDragging(View view) {
        this.draggingItemView = view;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        debugLog("onInterceptTouchEvent");
        View view = this.draggingItemView;
        if (view == null) {
            return false;
        }
        debugLog("Started Drag");
        setIsDragging(true);
        this.floatingItem = createFloatingBitmap(view);
        int y = (int) motionEvent.getY();
        this.fingerAnchorY = y;
        this.fingerOffsetInViewY = y - view.getTop();
        this.fingerY = this.fingerAnchorY;
        this.selectedDragItemPos = recyclerView.getChildPosition(view);
        debugLog("selectedDragItemPos = " + this.selectedDragItemPos);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        debugLog("onTouchEvent");
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (motionEvent.getAction() == 1 && this.selectedDragItemPos != -1) {
                int newPostion = getNewPostion(recyclerView);
                OnItemMovedListener onItemMovedListener = this.moveInterface;
                if (onItemMovedListener != null) {
                    onItemMovedListener.onItemMoved(this.selectedDragItemPos, newPostion);
                }
            }
            this.draggingItemView = null;
            setIsDragging(false);
            this.selectedDragItemPos = -1;
            this.floatingItem = null;
            recyclerView.invalidateItemDecorations();
            return;
        }
        int y = (int) motionEvent.getY();
        this.fingerY = y;
        if (this.floatingItem != null) {
            this.floatingItemBounds.top = y - this.fingerOffsetInViewY;
            if (this.floatingItemBounds.top < (-this.floatingItemStatingBounds.height()) / 2) {
                this.floatingItemBounds.top = (-this.floatingItemStatingBounds.height()) / 2;
            }
            Rect rect = this.floatingItemBounds;
            rect.bottom = rect.top + this.floatingItemStatingBounds.height();
            this.floatingItem.setBounds(this.floatingItemBounds);
        }
        float f2 = 0.0f;
        if (this.fingerY > recyclerView.getHeight() * (1.0f - this.autoScrollWindow)) {
            f2 = this.fingerY - (recyclerView.getHeight() * (1.0f - this.autoScrollWindow));
        } else if (this.fingerY < recyclerView.getHeight() * this.autoScrollWindow) {
            f2 = this.fingerY - (recyclerView.getHeight() * this.autoScrollWindow);
        }
        // 原版字节码：算出 f2 后（含两分支都不命中的 :cond_4 → :goto_1 直通路径），
        // debugLog / scrollBy / invalidateItemDecorations 三条【无条件】执行。
        // 曾被误写成 if (f2 != 0.0f) 才执行；浮动项由 ItemDecoration 绘制，
        // 少了每帧 invalidateItemDecorations 就会导致列表中段拖动时跟手动画消失。
        debugLog("Scroll: " + f2);
        recyclerView.scrollBy(0, (int) (f2 * this.autoScrollSpeed));
        recyclerView.invalidateItemDecorations();
    }

    private void setIsDragging(boolean z) {
        if (z != this.isDragging) {
            this.isDragging = z;
            OnDragStateChangedListener onDragStateChangedListener = this.dragStateChangedListener;
            if (onDragStateChangedListener != null) {
                if (z) {
                    onDragStateChangedListener.onDragStart();
                } else {
                    onDragStateChangedListener.onDragStop();
                }
            }
        }
    }

    public void setOnDragStateChangedListener(OnDragStateChangedListener onDragStateChangedListener) {
        this.dragStateChangedListener = onDragStateChangedListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        BitmapDrawable bitmapDrawable = this.floatingItem;
        if (bitmapDrawable != null) {
            bitmapDrawable.setAlpha((int) (this.floatingItemAlpha * 255.0f));
            this.bgColor.setColor(this.floatingItemBgColor);
            canvas.drawRect(this.floatingItemBounds, this.bgColor);
            this.floatingItem.draw(canvas);
        }
    }

    private BitmapDrawable createFloatingBitmap(View view) {
        this.floatingItemStatingBounds = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        this.floatingItemBounds = new Rect(this.floatingItemStatingBounds);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(this.floatingItemStatingBounds.width(), this.floatingItemStatingBounds.height(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmapCreateBitmap));
        BitmapDrawable bitmapDrawable = new BitmapDrawable(view.getResources(), bitmapCreateBitmap);
        bitmapDrawable.setBounds(this.floatingItemBounds);
        return bitmapDrawable;
    }
}
