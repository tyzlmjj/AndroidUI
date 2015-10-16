package com.mjj.viewflipper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.text);
        final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.addView(getImageView(R.mipmap.abcde_a));
        flipper.addView(getImageView(R.mipmap.abcde_b));
        flipper.addView(getImageView(R.mipmap.abcde_d));
        flipper.setInAnimation(this, R.anim.push_up_in);
        flipper.setOutAnimation(this, R.anim.push_up_out);
        flipper.setFlipInterval(3000);
        flipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setText((flipper.getDisplayedChild()+1)+"/"+flipper.getChildCount());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        textView.setText((flipper.getDisplayedChild()+1)+"/"+flipper.getChildCount());
        flipper.startFlipping();
    }

    public TextView getTextView(String text){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(50f);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public ImageView getImageView(int res){
        ImageView imageView = new ImageView(this);

        imageView.setBackgroundResource(res);

        return  imageView;

    }



}
