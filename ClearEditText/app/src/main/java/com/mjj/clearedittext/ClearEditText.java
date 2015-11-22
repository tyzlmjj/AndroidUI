package com.mjj.clearedittext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by 家杰 on 2015/11/20.
 */
public class ClearEditText extends AppCompatEditText {

    //按钮资源
    private final int CLEAR = R.drawable.clearfill;
    //动画时长
    private final int ANIMATOR_TIME = 200;
    //按钮左右间隔,单位DP
    private final int INTERVAL = 5;
    //清除按钮宽度,单位DP
    private final int WIDTH_OF_CLEAR = 23;


    //间隔记录
    private int Interval;
    //清除按钮宽度记录
    private int mWidth_clear;
    //右内边距
    private int mPaddingRight;
    //清除按钮的bitmap
    private Bitmap mBitmap_clear;
    //清除按钮出现动画
    private ValueAnimator mAnimator_visible;
    //消失动画
    private ValueAnimator mAnimator_gone;
    //是否显示的记录
    private boolean isVisible = false;
    //右边添加其他按钮时使用
    private int mRight = 0;

    public ClearEditText(final Context context) {
        super(context);
        init(context);
    }

    public ClearEditText(final Context context,final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditText(final Context context,final AttributeSet attrs,final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setSingleLine();

        mBitmap_clear = createBitmap(CLEAR,context);

        Interval = dp2px(INTERVAL);
        mWidth_clear = dp2px(WIDTH_OF_CLEAR);
        mPaddingRight = Interval + mWidth_clear + Interval ;
        mAnimator_gone = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        mAnimator_visible = ValueAnimator.ofInt(mWidth_clear + Interval,0).setDuration(ANIMATOR_TIME);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //设置内边距
        setPadding(getPaddingLeft(), getPaddingTop(), mPaddingRight+ mRight, getPaddingBottom());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//抗锯齿
        if (mAnimator_visible.isRunning()) {
            int x = (int) mAnimator_visible.getAnimatedValue();
            drawClear(x,canvas);
            invalidate();
        }else if (isVisible){
            drawClear(0,canvas);
        }

        if(mAnimator_gone.isRunning()){
            float scale = (float) mAnimator_gone.getAnimatedValue();
            drawClearGone(scale, canvas);
            invalidate();
        }
    }

    /**
     * 绘制清除按钮出现的图案
     * @param translationX 水平移动距离
     * @param canvas
     */
    protected void drawClear( int translationX,Canvas canvas){
        int right = getWidth()+getScrollX() - Interval - mRight +translationX;
        int left = right-mWidth_clear;
        int top = (getHeight()-mWidth_clear)/2;
        int bottom = top + mWidth_clear;
        Rect rect = new Rect(left,top,right,bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);

    }

    /**
     * 绘制清除按钮消失的图案
     * @param scale 缩放比例
     * @param canvas
     */
    protected void drawClearGone( float scale,Canvas canvas){
        int right = (int) (getWidth()+getScrollX()- Interval - mRight -mWidth_clear*(1f-scale)/2f);
        int left = (int) (getWidth()+getScrollX()- Interval - mRight -mWidth_clear*(scale+(1f-scale)/2f));
        int top = (int) ((getHeight()-mWidth_clear*scale)/2);
        int bottom = (int) (top + mWidth_clear*scale);
        Rect rect = new Rect(left,top,right,bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);
    }

    /**
     * 开始清除按钮的显示动画
     */
    private void startVisibleAnimator() {
        endAnaimator();
        mAnimator_visible.start();
        invalidate();
    }

    /**
     * 开始清除按钮的消失动画
     */
    private void startGoneAnimator() {
        endAnaimator();
        mAnimator_gone.start();
        invalidate();
    }

    /**
     * 结束所有动画
     */
    private void endAnaimator(){
        mAnimator_gone.end();
        mAnimator_visible.end();
    }

    /**
     * Edittext内容变化的监听
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if(text.length()>0) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        }else{
            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }

    /**
     * 触控执行的监听
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            boolean touchable = ( getWidth() - Interval - mRight - mWidth_clear < event.getX() ) && (event.getX() < getWidth() - Interval - mRight);
            if (touchable) {
                setError(null);
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始晃动动画
     */
    public void startShakeAnimation(){
        if(getAnimation() == null){
            this.setAnimation(shakeAnimation(4));
        }
        this.startAnimation(getAnimation());
    }

    /**
     * 晃动动画
     * @param counts 0.5秒钟晃动多少下
     * @return
     */
    private Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    /**
     * 给图标染上当前提示文本的颜色并且转出Bitmap
     * @param resources
     * @param context
     * @return
     */
    public Bitmap createBitmap(int resources,Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, resources);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        return drawableToBitamp(wrappedDrawable);
    }

    /**
     * drawable转换成bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //----------------以下方法为方便子类继承，只使用ClearEditText就没有用处---------------------------------------------------------------------

    public int getInterval() {
        return  Interval;
    }

    public int getmWidth_clear() {
        return mWidth_clear;
    }

    public Bitmap getmBitmap_clear() {
        return mBitmap_clear;
    }

    public void addRight(int right){
        mRight += right;
    }



}
