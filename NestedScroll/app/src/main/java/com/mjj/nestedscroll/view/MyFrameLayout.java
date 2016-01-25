package com.mjj.nestedscroll.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;


public class MyFrameLayout extends FrameLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private View headView;

    private View targetView;

    private int targetOldTop;

    private final float RATIO = 0.3f;

    public MyFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        headView = getChildAt(0);
        headView.setTranslationY(-headView.getHeight() / 4);
        targetView = getChildAt(1);
        targetOldTop = targetView.getTop();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.i("asd", "onStartNestedScroll(View " + child.getClass() + ", View " + target.getClass() + ", int " + nestedScrollAxes + ")");
        if(nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && targetView.getScrollY() == 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.i("asd", "onNestedScrollAccepted(View child, View target, int " + nestedScrollAxes + ")");
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(final View target) {
        Log.i("asd", "onStopNestedScroll");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("asd", "onNestedScroll(View target, int " + dxConsumed + ", int " + dyConsumed + ", int " + dxUnconsumed + ", int" + dyUnconsumed + ")");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)
    {
        int y = 0;
        if(targetView.getScrollY() == 0)
        {
           y = offset(targetView, dy);
        }
        consumed[0] = dx;
        consumed[1] = y;
        Log.i("asd", "onNestedPreScroll-" + "dx:" + dx + " dy:" + dy);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i("asd", "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i("asd", "onNestedPreFling");
        return target.getScrollY() == 0;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.i("asd", "getNestedScrollAxes");
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    private int offset(View target, int dy)
    {
        int targetTop = target.getTop();
        int offset;
        if(dy > 0)
        {
            offset = (targetTop > dy)?dy:targetTop;
        }
        else
        {
            offset = (targetTop+(-dy) < targetOldTop)?dy: targetTop- targetOldTop;
        }
        headView.setTranslationY(headView.getTranslationY() + -offset * RATIO);
        TopAndBottomOffset(target, -offset);
        return offset;
    }

    private void TopAndBottomOffset(View v, int offset)
    {
        int top = v.getTop() + offset;
        int bottom = v.getBottom();

        v.setTop(top);
        v.setBottom(bottom);
    }

}

