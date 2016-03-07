package com.mjj.gaussianblur;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    Bitmap bitmap;
    ImageView imageView;
    ImageView imageView2;
    SeekBar seekBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        textView = (TextView) findViewById(R.id.text);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                imageView.buildDrawingCache();
                bitmap = imageView.getDrawingCache();
                return true;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView2.setAlpha(progress / 100f * 1.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void boxBlur(View v)
    {
        textView.setText("均值模糊耗时："+GaussianBlur.blurByJni_box(bitmap, 3, imageView2)+"毫秒");
    }

    public void gaussianBlur(View v)
    {
        textView.setText("一维高斯耗时：" + GaussianBlur.blurByJni_Gaussian(bitmap, 5, imageView2) + "毫秒");
    }

    public void renderScript(View v)
    {
        textView.setText("RenderScript耗时：" + GaussianBlur.blurByRenderScript(bitmap, 5, imageView2, MainActivity.this) + "毫秒");
    }


}
