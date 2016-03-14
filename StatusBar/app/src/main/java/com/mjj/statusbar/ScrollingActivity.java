package com.mjj.statusbar;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置toolbar高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
        {
            toolbar.getLayoutParams().height = getAppBarHeight();
            toolbar.setPadding(toolbar.getPaddingLeft(),getStatusBarHeight(),toolbar.getPaddingRight(),toolbar.getPaddingBottom());
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            ScrollingActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private int getAppBarHeight()
    {
        return dip2px(56)+getStatusBarHeight();
    }

    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private  int dip2px(float dipValue)
    {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
