package me.majiajie.viewdraghelper;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 滑动布局
 */
public class ViewDragHelperLayout extends LinearLayout
{
    private ViewDragHelper mViewDragHelper;

    private View mFirstView;
    private View mSecondView;
    private View mThirdiew;

    private Point mFirstPoint = new Point();

    public ViewDragHelperLayout(Context context) {
        super(context);
        init();
    }

    public ViewDragHelperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewDragHelperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mViewDragHelper = ViewDragHelper.create(this,callback);
        //设置开启边界触控
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        mFirstView = getChildAt(0);
        mSecondView = getChildAt(1);
        mThirdiew = getChildAt(2);
    }

    /**
     * 父布局更新子视图的偏移位置。子视图使用{@link android.widget.Scroller}.
     * <p>须调用{@link #invalidate()}触发</p>
     */
    @Override
    public void computeScroll()
    {
        if(mViewDragHelper.continueSettling(true))
        {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        mFirstPoint.set(mFirstView.getLeft(),mFirstView.getTop());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }



    ViewDragHelper.Callback callback = new ViewDragHelper.Callback()
    {

        /**
         * 限制View是否可以拖动，true：可以拖动
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId)
        {
            return child != mSecondView;
        }

        /**
         * 控制水平移动
         * @param child
         * @param left  拖动的View即将到达的左边X坐标(坐标是相对父布局的，非整个屏幕)
         * @param dx    水平拖动的像素值
         * @return 确切移动到的左侧X坐标
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            return left;
        }

        /**
         * 控制垂直移动
         * @param child
         * @param top   拖动的View即将到达的顶部Y坐标(坐标是相对父布局的，非整个屏幕)
         * @param dy    垂直拖动的像素值
         * @return  确切移动到的顶部Y坐标
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy)
        {
            return top;
        }

        /**
         * 当view被捕获时回调
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * 手指释放的时候调用
         * @param releasedChild 释放的View
         * @param xvel          从开始拖动到放开手指所便宜的X轴像素
         * @param yvel          从开始拖动到放开手指所便宜的Y轴像素
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel)
        {
            if(releasedChild == mFirstView)
            {
                mViewDragHelper.settleCapturedViewAt(mFirstPoint.x,mFirstPoint.y);
                invalidate();
            }
        }

        /**
         * 当触摸到边界时回调。
         * @param edgeFlags 边界值，如{@link ViewDragHelper#EDGE_LEFT}
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId)
        {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        /**
         * true的时候会锁住当前的边界，false则unLock。
         */
        @Override
        public boolean onEdgeLock(int edgeFlags)
        {
            return super.onEdgeLock(edgeFlags);
        }

        /**
         * 在边界拖动时调用
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId)
        {
            //使指定View跟随手指拖动
            mViewDragHelper.captureChildView(mSecondView,pointerId);
        }

        /**
         * 水平拖动范围限制，返回>0才可拖动
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return super.getViewHorizontalDragRange(child);
        }

        /**
         * 垂直拖动范围限制，返回>0才可拖动
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        /**
         * 改变同一个坐标（x,y）去寻找captureView位置的方法。（具体在：findTopChildUnder方法中）
         */
        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        /**
         * 当ViewDragHelper状态发生变化时回调（IDLE,DRAGGING,SETTING[自动滚动时]）
         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        /**
         *  View在父布局中的位置变更
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }
    };



}
