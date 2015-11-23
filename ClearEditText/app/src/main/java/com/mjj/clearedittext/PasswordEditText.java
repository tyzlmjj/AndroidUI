package com.mjj.clearedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 家杰 on 2015/11/20.
 */
public class PasswordEditText extends ClearEditText{

    //资源
    private final int INVISIBLE = R.drawable.close;
    private final int VISIBLE = R.drawable.open;
    //按钮宽度dp
    private int mWidth;
    //按钮的bitmap
    private Bitmap mBitmap_invisible;
    private Bitmap mBitmap_visible;
    //间隔
    private int Interval;
    //内容是否可见
    private boolean isVisible = false;

    public PasswordEditText(final Context context) {
        super(context);
        init(context);
    }

    public PasswordEditText(final Context context,final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PasswordEditText(final Context context,final AttributeSet attrs,final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setSingleLine();
        //设置EditText文本为隐藏的(注意！需要在setSingleLine()之后调用)
        setTransformationMethod(PasswordTransformationMethod.getInstance());

        mWidth = getmWidth_clear();
        Interval = getInterval();
        addRight(mWidth+Interval);
        mBitmap_invisible = createBitmap(INVISIBLE,context);
        mBitmap_visible = createBitmap(VISIBLE,context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int right = getWidth()+getScrollX()- Interval ;
        int left = right-mWidth;
        int top = (getHeight()-mWidth)/2;
        int bottom = top + mWidth;
        Rect rect = new Rect(left,top,right,bottom);

        if(isVisible){
            canvas.drawBitmap(mBitmap_visible, null, rect, null);
        }else{
            canvas.drawBitmap(mBitmap_invisible, null, rect, null);
        }
    }

    /**
     * 改写父类的方法
     */
    @Override
    protected void drawClear(int translationX, Canvas canvas) {
        float scale = 1f - (float)(translationX)/(float)(getmWidth_clear()+Interval);
        int right = (int) (getWidth()+getScrollX()- Interval-mWidth-Interval -getmWidth_clear()*(1f-scale)/2f);
        int left = (int) (getWidth()+getScrollX()- Interval-mWidth-Interval -getmWidth_clear()*(scale+(1f-scale)/2f));
        int top = (int) ((getHeight()-getmWidth_clear()*scale)/2);
        int bottom = (int) (top + getmWidth_clear()*scale);
        Rect rect = new Rect(left,top,right,bottom);
        canvas.drawBitmap(getmBitmap_clear(), null, rect, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = ( getWidth() - mWidth - Interval < event.getX() ) && (event.getX() < getWidth() - Interval);
            if (touchable) {
                isVisible = !isVisible;
                if (isVisible){
                    //设置EditText文本为可见的
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //设置EditText文本为隐藏的
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
