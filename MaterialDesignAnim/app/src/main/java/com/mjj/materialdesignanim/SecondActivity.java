package com.mjj.materialdesignanim;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initAppBar();

        getIntentData();

    }

    private void getIntentData() {
        int imageRes = getIntent().getIntExtra("image",0);
        if (imageRes!=0){
            ImageView imageView = (ImageView) findViewById(R.id.imageview);
            Glide.with(this).load(imageRes).into(imageView);
        }
    }

    private void initAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                finishAfterTransition();
            }else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
