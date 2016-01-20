package com.mjj.materialprogressdrawable;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int[] colors = {
            0xFFFF0000,0xFFFF7F00,0xFFFFFF00,0xFF00FF00
            ,0xFF00FFFF,0xFF0000FF,0xFF8B00FF};

    private final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;

    private ImageView imageView;

    private MaterialProgressDrawable mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);

        mProgress = new MaterialProgressDrawable(this,imageView);

        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        //圈圈颜色,可以是多种颜色
        mProgress.setColorSchemeColors(colors);
        //设置圈圈的各种大小
        mProgress.updateSizes(MaterialProgressDrawable.LARGE);

        imageView.setImageDrawable(mProgress);
    }

    private ValueAnimator valueAnimator;

    public void doAnim_Visable(View v)
    {
        if(valueAnimator == null)
        {
            valueAnimator = valueAnimator.ofFloat(0f,1f);
            valueAnimator.setDuration(600);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float n = (float) animation.getAnimatedValue();
                    //圈圈的旋转角度
                    mProgress.setProgressRotation(n*0.5f);
                    //圈圈周长，0f-1F
                    mProgress.setStartEndTrim(0f, n*0.8f);
                    //箭头大小，0f-1F
                    mProgress.setArrowScale(n);
                    //透明度，0-255
                    mProgress.setAlpha((int) (255*n));
                }
            });
        }

        if(!valueAnimator.isRunning())
        {
            if(mProgress.getAlpha()==255)
            {
                Toast.makeText(this, "已经显示了呀！还想怎样", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //是否显示箭头
                mProgress.showArrow(true);
                valueAnimator.start();
            }
        }
    }

    boolean start = false;

    public void start(View v)
    {
        if(mProgress.getAlpha() == 255)
        {
            if (!start)
            {
                mProgress.start();
                start = true;
            }
            else
            {
                Toast.makeText(this,"已经在转了",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this,"还没有显示",Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(View v)
    {
        if (start)
        {
            mProgress.stop();
            mProgress.setStartEndTrim(0f, 0.8f);
            start = false;
        }
        else
        {
            Toast.makeText(this,"又没有转",Toast.LENGTH_SHORT).show();
        }
    }
}
